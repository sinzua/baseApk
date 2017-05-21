package com.nativex.monetization.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import com.nativex.common.Log;
import com.nativex.common.Message;
import com.nativex.common.StringConstants;
import com.nativex.common.Violation;
import com.nativex.monetization.dialogs.custom.MessageDialog;
import com.nativex.monetization.enums.StringResources;
import com.nativex.monetization.theme.ThemeElementTypes;
import com.nativex.monetization.theme.ThemeManager;
import java.util.List;

public class DialogManager {
    private static DialogManager instance = null;
    private static Dialog networkConnectionDialog = null;

    public class DialogTextHolder {
        String body = null;
        String title = null;

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBody() {
            return this.body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public boolean equals(Object o) {
            if (o == null || !(o instanceof DialogTextHolder)) {
                return false;
            }
            DialogTextHolder holder = (DialogTextHolder) o;
            if (this.title != null) {
                if (!this.title.equals(holder.title)) {
                    return false;
                }
            } else if (holder.title != null) {
                return false;
            }
            if (this.body != null) {
                if (!this.body.equals(holder.body)) {
                    return false;
                }
            } else if (holder.body != null) {
                return false;
            }
            return true;
        }
    }

    private DialogManager() {
    }

    public static synchronized DialogManager getInstance() {
        DialogManager dialogManager;
        synchronized (DialogManager.class) {
            if (instance == null) {
                instance = new DialogManager();
            }
            dialogManager = instance;
        }
        return dialogManager;
    }

    public static void release() {
        dismissNetworkConnectionDialog();
        instance = null;
    }

    private static void dismissNetworkConnectionDialog() {
        if (networkConnectionDialog != null) {
            networkConnectionDialog.dismiss();
            networkConnectionDialog = null;
        }
    }

    public synchronized Dialog showNoNetworkConnectivityDialog(final Context context) {
        Dialog dialog;
        if (context == null) {
            try {
                Log.d("DialogManager: showNoNetworkConnectivityDialog - No context set.");
                dialog = null;
            } catch (Exception e) {
                Log.e("DialogManager: Unexpected exception caught in showNoNetworkConnectivityDialog().", e);
                dialog = null;
            }
        } else {
            dismissNetworkConnectionDialog();
            Drawable alertIcon = ThemeManager.getDrawable(ThemeElementTypes.LOGO);
            dialog = new MessageDialog(context);
            networkConnectionDialog = dialog;
            dialog.setTitle("NativeX");
            dialog.setMessage(StringsManager.getString(StringResources.NO_NETWORK_CONNECTIVITY));
            dialog.setButtonText("Connect");
            dialog.setDialogIcon(alertIcon);
            dialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    DialogManager.networkConnectionDialog = null;
                    ThemeManager.reset();
                }
            });
            dialog.setOnButtonClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent wifiIntent = new Intent("android.settings.WIFI_SETTINGS");
                    wifiIntent.setFlags(268435456);
                    context.startActivity(wifiIntent);
                    dialog.dismiss();
                }
            });
            if (context instanceof Activity) {
                final Activity activity = (Activity) context;
                DensityManager.getDeviceScreenSize(activity);
                MonetizationSharedDataManager.checkTheme();
                dialog.setOwnerActivity(activity);
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            View v = new View(activity) {
                                @SuppressLint({"MissingSuperCall"})
                                protected void onDetachedFromWindow() {
                                    dialog.dismiss();
                                }
                            };
                            v.setVisibility(8);
                            activity.addContentView(v, new LayoutParams(0, 0));
                        } catch (Exception e) {
                            Log.e("Unhandled exception", e);
                        }
                    }
                });
            }
            dialog.show();
        }
        return dialog;
    }

    public synchronized ProgressDialog showProgressDialog(Context context) {
        ProgressDialog progressDialog;
        if (context == null) {
            try {
                Log.e("DialogManager.showProgressDialog() - No context set");
                progressDialog = null;
            } catch (Exception e) {
                Log.e("DialogManager: Unexpected exception caught in showProgressDialog().", e);
                progressDialog = null;
            }
        } else {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(StringsManager.getString(StringResources.PROGRESS_DIALOG_LOADING));
            progressDialog.show();
            progressDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    ThemeManager.reset();
                }
            });
            if (context instanceof Activity) {
                final Activity activity = (Activity) context;
                progressDialog.setOwnerActivity(activity);
                DensityManager.getDeviceScreenSize(activity);
                MonetizationSharedDataManager.checkTheme();
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            View v = new View(activity) {
                                @SuppressLint({"MissingSuperCall"})
                                protected void onDetachedFromWindow() {
                                    progressDialog.dismiss();
                                }
                            };
                            v.setVisibility(8);
                            activity.addContentView(v, new LayoutParams(0, 0));
                        } catch (Exception e) {
                            Log.e("Unhandled exception", e);
                        }
                    }
                });
            }
        }
        return progressDialog;
    }

    public synchronized Dialog showMessagesDialog(Context parent, List<Message> messages, List<Violation> violations) {
        Dialog dialog;
        if (parent == null) {
            try {
                Log.d("DialogManager.showMessageDialog: No parent context set");
                dialog = null;
            } catch (Exception e) {
                Log.e("DialogManager: Unexpected exception caught in showMessagesDialog().", e);
                dialog = null;
            }
        } else if (messages.size() + violations.size() <= 0) {
            Log.d("No messages or violations to display");
            dialog = null;
        } else {
            DialogTextHolder dialogText = createDialogStrings(messages, violations);
            if (dialogText == null) {
                dialog = null;
            } else {
                messages.clear();
                violations.clear();
                dialog = new MessageDialog(parent);
                dialog.setTitle(dialogText.title);
                dialog.setMessage(dialogText.body);
                dialog.setButtonText(StringConstants.MESSAGE_DIALOG_BUTTON_TEXT);
                dialog.setOnButtonClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        ThemeManager.reset();
                    }
                });
                if (parent instanceof Activity) {
                    final Activity activity = (Activity) parent;
                    dialog.setOwnerActivity(activity);
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                View v = new View(activity) {
                                    @SuppressLint({"MissingSuperCall"})
                                    protected void onDetachedFromWindow() {
                                        dialog.dismiss();
                                    }
                                };
                                v.setVisibility(8);
                                activity.addContentView(v, new LayoutParams(0, 0));
                            } catch (Exception e) {
                                Log.e("Unhandled exception", e);
                            }
                        }
                    });
                }
                dialog.show();
            }
        }
        return dialog;
    }

    DialogTextHolder createDialogStrings(List<Message> messages, List<Violation> violations) {
        try {
            int msgCount = messages.size();
            int vioCount = violations.size();
            if (msgCount + vioCount <= 0) {
                return null;
            }
            String dialogTitle;
            StringBuilder dialogBody = new StringBuilder();
            DialogTextHolder dialogText = new DialogTextHolder();
            if (msgCount + vioCount > 1) {
                if (msgCount > 0) {
                    for (Message message : messages) {
                        dialogBody.append(message.getDisplayName());
                        dialogBody.append("\n");
                        dialogBody.append(message.getDisplayText());
                        dialogBody.append("\n\n");
                        Log.d("Response Message -> Reference Name: \"" + message.getReferenceName() + "\" Display Name: \"" + message.getDisplayName() + "\" Display Text: \"" + message.getDisplayText() + "\"");
                    }
                }
                if (vioCount > 0) {
                    for (Violation violation : violations) {
                        dialogBody.append(violation.getEntity());
                        dialogBody.append("\n");
                        dialogBody.append(violation.getMessage());
                        dialogBody.append("\n\n");
                        Log.d("Response Violation -> Entity: \"" + violation.getEntity() + "\" Message: \"" + violation.getMessage() + "\"");
                    }
                    dialogBody.delete(dialogBody.length() - 2, dialogBody.length());
                }
            } else if (msgCount == 1) {
                dialogBody.append(((Message) messages.get(0)).getDisplayText());
            } else if (vioCount == 1) {
                dialogBody.append(((Violation) violations.get(0)).getMessage());
            }
            if (msgCount + vioCount != 1) {
                dialogTitle = MonetizationSharedDataManager.getApplicationName();
            } else if (msgCount == 1) {
                dialogTitle = ((Message) messages.get(0)).getDisplayName();
            } else {
                dialogTitle = ((Violation) violations.get(0)).getEntity();
            }
            dialogText.body = dialogBody.toString();
            dialogText.title = dialogTitle;
            return dialogText;
        } catch (Exception e) {
            Log.e("DialogManager: Unexpected exception caught in createDialogStrings().", e);
            return null;
        }
    }
}
