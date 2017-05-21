package com.nativex.monetization.mraid;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nativex.common.Utilities;
import com.nativex.monetization.dialogs.custom.AddCalendarEntryDialog;
import com.nativex.monetization.dialogs.custom.AddCalendarEntryDialog.Calendar;
import com.nativex.monetization.dialogs.custom.AddCalendarEntryDialog.OnCalendarEntryClicked;
import com.nativex.monetization.dialogs.custom.StorePictureDialog;
import com.nativex.monetization.manager.DensityManager;
import com.nativex.monetization.mraid.MRAIDUtils.Features;
import com.nativex.monetization.mraid.MRAIDUtils.JSCommands;
import com.nativex.monetization.mraid.MRAIDUtils.JSDialogAction;
import com.nativex.monetization.mraid.objects.CalendarEntryData;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MRAIDWorkerFactory {

    private static class CalendarWorker extends MRAIDDialogWorker {
        private final OnClickListener closeListener = new OnClickListener() {
            public void onClick(View v) {
                CalendarWorker.this.finishWorker();
            }
        };
        private AddCalendarEntryDialog confirmationDialog;
        private final CalendarEntryData data;
        private final OnCalendarEntryClicked listener = new OnCalendarEntryClicked() {
            public void onClick(Calendar calendar) {
                if (calendar != null) {
                    CalendarWorker.this.addCalendarEntry(calendar);
                    CalendarWorker.this.dismissDialog();
                }
            }
        };
        private final OnClickListener onProceed = new OnClickListener() {
            public void onClick(View v) {
                try {
                    MRAIDCalendarUtils.createCalendarEntry(CalendarWorker.this.getContainer().getActivity(), null, CalendarWorker.this.data);
                    CalendarWorker.this.finishWorker();
                } catch (Exception e) {
                    MRAIDLogger.e("Failed to create calendar entry", e);
                    if (CalendarWorker.this.getContainer() != null) {
                        CalendarWorker.this.getContainer().fireErrorEvent("Could not create calendar event", e, JSCommands.CREATE_CALENDAR_EVENT);
                    }
                    CalendarWorker.this.finishWorker();
                } catch (Throwable th) {
                    CalendarWorker.this.finishWorker();
                }
            }
        };

        public CalendarWorker(MRAIDContainer container, CalendarEntryData data) {
            super(container);
            this.data = data;
            showDialog(container.getActivity());
        }

        public void dismissDialog() {
            if (this.confirmationDialog != null) {
                this.confirmationDialog.dismiss();
                this.confirmationDialog = null;
            }
        }

        public void showDialog(Activity activity) {
            try {
                dismissDialog();
                this.confirmationDialog = new AddCalendarEntryDialog(activity);
                this.confirmationDialog.setOnCalendarClickedListener(this.listener);
                this.confirmationDialog.setOnCloseClickListener(this.closeListener);
                if (Features.CALENDAR.getSupportLevel() == 1) {
                    this.confirmationDialog.setOnButtonClickListener(this.closeListener);
                    this.confirmationDialog.addCalendars(MRAIDCalendarUtils.queryCalendarsList(getContainer().getActivity()));
                    this.confirmationDialog.setBodyText("You are requested to add a calendar event. Please choose a calendar to add the event to:");
                    this.confirmationDialog.setButtonText("Cancel");
                } else {
                    this.confirmationDialog.setOnButtonClickListener(this.onProceed);
                    this.confirmationDialog.setBodyText("You are requested to add a calendar event.");
                    this.confirmationDialog.setButtonText("Proceed");
                }
                this.confirmationDialog.show();
            } catch (Exception e) {
                MRAIDLogger.e("Failed to create confirmation dialog when creating calendar event", e);
                if (getContainer() != null) {
                    getContainer().fireErrorEvent("", e, JSCommands.CREATE_CALENDAR_EVENT);
                }
                finishWorker();
            }
        }

        private void addCalendarEntry(Calendar calendar) {
            try {
                MRAIDCalendarUtils.createCalendarEntry(getContainer().getActivity(), calendar, this.data);
            } catch (Exception e) {
                getContainer().fireErrorEvent(null, e, JSCommands.CREATE_CALENDAR_EVENT);
            } finally {
                finishWorker();
            }
        }

        public void release() {
            dismissDialog();
        }
    }

    private static class JSDialogWorker extends MRAIDDialogWorker {
        private final JSDialogAction action;
        private Dialog dialog = null;
        private final OnDismissListener dialogDismissed = new OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                JSDialogWorker.this.finishWorker();
            }
        };
        private boolean jsResultHandled = false;
        private final String message;
        private final JsResult result;

        public JSDialogWorker(MRAIDContainer container, String url, String message, JsResult result, JSDialogAction action) {
            super(container);
            this.message = message;
            this.result = result;
            this.action = action;
        }

        public void showDialog(Activity activity) {
            if (activity == null) {
                finishWorker();
            }
            if (this.dialog == null) {
                this.dialog = createDialog(activity);
            }
            this.dialog.setOnDismissListener(this.dialogDismissed);
            this.dialog.show();
        }

        private Dialog createDialog(Activity context) {
            Builder builder = new Builder(context);
            switch (this.action) {
                case BEFORE_UNLOAD:
                    buildBeforeUnloadDialog(builder);
                    break;
                case CONFIRM:
                    buildConfirmDialog(builder);
                    break;
                case PROMPT:
                    buildPromptDialog(context, builder);
                    break;
                default:
                    buildAlertDialog(builder);
                    break;
            }
            return builder.create();
        }

        private void buildAlertDialog(Builder builder) {
            builder.setTitle("JS Alert");
            builder.setMessage(this.message);
            builder.setPositiveButton(17039370, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    JSDialogWorker.this.confirmResult();
                }
            });
            builder.setCancelable(false);
        }

        private void buildBeforeUnloadDialog(Builder builder) {
            builder.setTitle("Leaving page");
            builder.setMessage(this.message);
            builder.setPositiveButton(17039370, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    JSDialogWorker.this.confirmResult();
                }
            });
            builder.setNegativeButton(17039360, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    JSDialogWorker.this.cancelResult();
                }
            });
        }

        private void buildConfirmDialog(Builder builder) {
            builder.setTitle("JS Confirm:");
            builder.setMessage(this.message);
            builder.setPositiveButton(17039370, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    JSDialogWorker.this.confirmResult();
                }
            });
            builder.setNegativeButton(17039360, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    JSDialogWorker.this.cancelResult();
                }
            });
            builder.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    JSDialogWorker.this.cancelResult();
                }
            });
        }

        private void buildPromptDialog(Activity context, Builder builder) {
            LayoutParams params = new LayoutParams(-1, -2);
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(1);
            layout.setGravity(1);
            layout.setLayoutParams(params);
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(-1, -2);
            TextView text = new TextView(context);
            text.setLayoutParams(textParams);
            text.setText(this.message);
            new LinearLayout.LayoutParams(-1, -2).topMargin = DensityManager.getDIP(context, 6.0f);
            final EditText edit = new EditText(context);
            edit.setHorizontallyScrolling(true);
            edit.setSelectAllOnFocus(true);
            edit.setInputType(1);
            edit.setTypeface(Typeface.DEFAULT_BOLD);
            edit.setHint("Enter text");
            layout.addView(text);
            layout.addView(edit);
            builder.setTitle("JS Prompt:");
            builder.setView(layout);
            builder.setPositiveButton(17039370, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    JSDialogWorker.this.confirmResult(edit.getText().toString());
                }
            });
            builder.setNegativeButton(17039360, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    JSDialogWorker.this.cancelResult();
                }
            });
            builder.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    JSDialogWorker.this.cancelResult();
                }
            });
        }

        public void dismissDialog() {
            if (this.dialog != null) {
                this.dialog.dismiss();
                this.dialog = null;
            }
        }

        private void confirmResult(String text) {
            if (this.result != null && (this.result instanceof JsPromptResult)) {
                this.jsResultHandled = true;
                ((JsPromptResult) this.result).confirm(text);
            }
        }

        private void confirmResult() {
            if (this.result != null) {
                this.jsResultHandled = true;
                this.result.confirm();
            }
        }

        private void cancelResult() {
            if (this.result != null) {
                this.jsResultHandled = true;
                this.result.cancel();
            }
        }

        public void release() {
            dismissDialog();
            if (!this.jsResultHandled) {
                cancelResult();
            }
        }
    }

    private static class StorePictureWorker extends MRAIDDialogWorker {
        private boolean cancelled = false;
        private StorePictureDialog confirmDialog;
        private final OnClickListener onCloseListener = new OnClickListener() {
            public void onClick(View v) {
                if (StorePictureWorker.this.userConfirmed == null) {
                    StorePictureWorker.this.userConfirmed = Boolean.valueOf(false);
                }
                MRAIDLogger.d("Picture discarded");
                StorePictureWorker.this.finishWorker();
            }
        };
        private final OnClickListener onProceedListener = new OnClickListener() {
            public void onClick(View v) {
                StorePictureWorker.this.userConfirmed = Boolean.valueOf(true);
                if (StorePictureWorker.this.pictureLocation != null) {
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                StorePictureWorker.this.copyFile();
                            } catch (Exception e) {
                                MRAIDLogger.e("Unhandled exception", e);
                            }
                        }
                    }).start();
                }
            }
        };
        private String pictureLocation = null;
        private Bitmap scaledForDialog = null;
        private String url;
        private Boolean userConfirmed = null;

        StorePictureWorker(MRAIDContainer container) {
            super(container);
        }

        public void dismissDialog() {
            if (this.confirmDialog != null) {
                this.confirmDialog.dismiss();
                this.confirmDialog = null;
            }
        }

        public void showDialog(Activity activity) {
            try {
                dismissDialog();
                if (this.userConfirmed == null && !this.cancelled) {
                    this.confirmDialog = new StorePictureDialog(getContainer().getActivity());
                    this.confirmDialog.setOnButtonClickListener(this.onProceedListener);
                    this.confirmDialog.setOnCloseClickListener(this.onCloseListener);
                    if (this.scaledForDialog != null) {
                        this.confirmDialog.setImage(this.scaledForDialog);
                    }
                    this.confirmDialog.show();
                }
            } catch (Exception e) {
                MRAIDLogger.e("Failed to create confirmation dialog when storing picture", e);
                if (getContainer() != null) {
                    getContainer().fireErrorEvent("", e, JSCommands.STORE_PICTURE);
                }
                finishWorker();
            }
        }

        public void storePicture() {
            showDialog(getContainer().getActivity());
            MRAIDAsyncManager.downloadPicture(getContainer().getContext(), this.url, new OnActionComplete() {
                public void onActionComplete(String data, boolean success) {
                    if (success && data != null) {
                        Log.d("ANTON", "Picture downloaded");
                        StorePictureWorker.this.onPictureDownloaded(data);
                    }
                }
            });
        }

        private void onPictureDownloaded(String pictureLocation) {
            try {
                this.pictureLocation = pictureLocation;
                if (this.cancelled) {
                    finishWorker();
                } else if (this.userConfirmed == null) {
                    this.scaledForDialog = Utilities.decodeSampledBitmapFromInternalMemory(getContainer().getContext(), pictureLocation, 200, 200);
                    if (this.confirmDialog != null) {
                        setPictureToDialogMainThread(pictureLocation);
                    }
                } else if (this.userConfirmed.booleanValue()) {
                    copyFile();
                } else {
                    finishWorker();
                }
            } catch (Exception e) {
                Log.e("ANTON", "Exception", e);
            }
        }

        private void copyFile() {
            Exception e;
            Throwable th;
            InputStream stream = null;
            OutputStream fOut = null;
            try {
                String extension = getPictureExtension(this.url);
                String filename = "image-" + Utilities.getDateInFormat("yyyy_MM_dd_HH_mm_ss");
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                path.mkdirs();
                File file = new File(path, filename + extension);
                while (file.exists()) {
                    file = new File(path, filename + "(" + 1 + ")" + extension);
                }
                file.createNewFile();
                stream = getContainer().getContext().openFileInput(this.pictureLocation);
                OutputStream fOut2 = new FileOutputStream(file);
                try {
                    Utilities.savePictureStreamToFile(fOut2, stream);
                    MediaScannerConnection.scanFile(getContainer().getContext(), new String[]{file.toString()}, null, new OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            MRAIDLogger.d("Image detected in gallery.");
                        }
                    });
                    MRAIDLogger.d("Picture Stored");
                    if (fOut2 != null) {
                        try {
                            fOut2.close();
                        } catch (Exception e2) {
                        }
                    }
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (Exception e3) {
                        }
                    }
                    finishWorker();
                    fOut = fOut2;
                } catch (Exception e4) {
                    e = e4;
                    fOut = fOut2;
                    try {
                        MRAIDLogger.e("Exception caught when storing picture", e);
                        if (getContainer() != null) {
                            getContainer().fireErrorEvent(null, e, JSCommands.STORE_PICTURE);
                        }
                        if (fOut != null) {
                            try {
                                fOut.close();
                            } catch (Exception e5) {
                            }
                        }
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (Exception e6) {
                            }
                        }
                        finishWorker();
                    } catch (Throwable th2) {
                        th = th2;
                        if (fOut != null) {
                            try {
                                fOut.close();
                            } catch (Exception e7) {
                            }
                        }
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (Exception e8) {
                            }
                        }
                        finishWorker();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fOut = fOut2;
                    if (fOut != null) {
                        fOut.close();
                    }
                    if (stream != null) {
                        stream.close();
                    }
                    finishWorker();
                    throw th;
                }
            } catch (Exception e9) {
                e = e9;
                MRAIDLogger.e("Exception caught when storing picture", e);
                if (getContainer() != null) {
                    getContainer().fireErrorEvent(null, e, JSCommands.STORE_PICTURE);
                }
                if (fOut != null) {
                    fOut.close();
                }
                if (stream != null) {
                    stream.close();
                }
                finishWorker();
            }
        }

        private void deleteFile() {
            getContainer().getContext().deleteFile(this.pictureLocation);
        }

        private void setPictureToDialogMainThread(String pictureLocation) {
            getContainer().post(new Runnable() {
                public void run() {
                    try {
                        ((ImageView) StorePictureWorker.this.confirmDialog.findViewById(444334)).setImageBitmap(StorePictureWorker.this.scaledForDialog);
                    } catch (Exception e) {
                        MRAIDLogger.e("Unhandled exception", e);
                    }
                }
            });
        }

        private String getPictureExtension(String url) {
            int lastDotPosition = url.lastIndexOf(46);
            if (lastDotPosition > -1) {
                return url.substring(lastDotPosition);
            }
            return "";
        }

        public void release() {
            this.cancelled = true;
            if (this.pictureLocation != null) {
                deleteFile();
            }
            if (this.confirmDialog != null) {
                this.confirmDialog.dismiss();
            }
            if (this.scaledForDialog != null) {
                this.scaledForDialog.recycle();
            }
        }
    }

    static final MRAIDDialogWorker createStorePictureWorker(MRAIDContainer container, String url) {
        StorePictureWorker worker = new StorePictureWorker(container);
        worker.url = url;
        worker.storePicture();
        return worker;
    }

    static final MRAIDDialogWorker createCalendarWorker(MRAIDContainer container, CalendarEntryData data) {
        return new CalendarWorker(container, data);
    }

    static final MRAIDDialogWorker createJSDialogWorker(MRAIDContainer container, String url, String message, JsResult result, JSDialogAction action) {
        JSDialogWorker worker = new JSDialogWorker(container, url, message, result, action);
        worker.showDialog(container.getActivity());
        return worker;
    }
}
