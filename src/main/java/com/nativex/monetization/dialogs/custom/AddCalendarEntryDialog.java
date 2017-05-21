package com.nativex.monetization.dialogs.custom;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import com.nativex.common.Utilities;
import com.nativex.monetization.theme.ThemeElementTypes;
import com.nativex.monetization.theme.ThemeManager;
import com.nativex.network.volley.DefaultRetryPolicy;
import java.util.List;

public class AddCalendarEntryDialog extends BaseDialog {
    private AddCalendarEntryDialogBody body;
    private final OnClickListener onClose = new OnClickListener() {
        public void onClick(View v) {
            if (v.getId() == StorePictureDialogBody.ID_BODY_BUTTON) {
                if (AddCalendarEntryDialog.this.userButtonListener != null) {
                    AddCalendarEntryDialog.this.userButtonListener.onClick(v);
                }
            } else if (v.getId() == MessageDialogTitle.ID_CLOSE && AddCalendarEntryDialog.this.userCloseListener != null) {
                AddCalendarEntryDialog.this.userCloseListener.onClick(v);
            }
            AddCalendarEntryDialog.this.dismiss();
        }
    };
    private OnClickListener userButtonListener;
    private OnClickListener userCloseListener;

    public static class Calendar {
        private final int id;
        private final String name;

        public Calendar(String name, int id) {
            this.name = name;
            this.id = id;
            if (Utilities.stringIsEmpty(name)) {
                name = "<No Name>";
            }
        }

        public String getName() {
            return this.name;
        }

        public int getId() {
            return this.id;
        }
    }

    public interface OnCalendarEntryClicked {
        void onClick(Calendar calendar);
    }

    public /* bridge */ /* synthetic */ void dismiss() {
        super.dismiss();
    }

    public /* bridge */ /* synthetic */ void onOrientationChange() {
        super.onOrientationChange();
    }

    public /* bridge */ /* synthetic */ void setDialogMaxHeight(int i) {
        super.setDialogMaxHeight(i);
    }

    public /* bridge */ /* synthetic */ void setDialogMaxWidth(int i) {
        super.setDialogMaxWidth(i);
    }

    public /* bridge */ /* synthetic */ void setDialogMinHeight(int i) {
        super.setDialogMinHeight(i);
    }

    public /* bridge */ /* synthetic */ void setDialogMinWidth(int i) {
        super.setDialogMinWidth(i);
    }

    public AddCalendarEntryDialog(Context context) {
        super(context);
        init();
    }

    private void init() {
        MessageDialogTitle title = new MessageDialogTitle(getContext());
        this.body = new AddCalendarEntryDialogBody(getContext());
        addView(title);
        addView(this.body);
        setDialogBackground(ThemeManager.getDrawable(ThemeElementTypes.MESSAGE_DIALOG_BACKGROUND));
        title.setLayoutParams(new LayoutParams(-1, -2));
        title.setOnCloseClickListener(this.onClose);
        this.body.setButtonClickListener(this.onClose);
        this.body.setLayoutParams(new LayoutParams(-1, -2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        title.setTitle("Confirm adding calendar entry");
    }

    public void addCalendars(List<Calendar> calendars) {
        this.body.addCalendars(calendars);
    }

    public void setBodyText(String text) {
        if (this.body != null) {
            this.body.setText(text);
        }
    }

    public void setButtonText(String text) {
        if (this.body != null) {
            this.body.setButtonText(text);
        }
    }

    public void setOnButtonClickListener(OnClickListener listener) {
        this.userButtonListener = listener;
    }

    public void setOnCloseClickListener(OnClickListener listener) {
        this.userCloseListener = listener;
    }

    public void setOnCalendarClickedListener(OnCalendarEntryClicked listener) {
        if (this.body != null) {
            this.body.setOnCalendarClickedListener(listener);
        }
    }
}
