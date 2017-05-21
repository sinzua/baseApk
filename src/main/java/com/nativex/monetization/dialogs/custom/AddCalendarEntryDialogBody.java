package com.nativex.monetization.dialogs.custom;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.nativex.common.Log;
import com.nativex.monetization.dialogs.custom.AddCalendarEntryDialog.Calendar;
import com.nativex.monetization.dialogs.custom.AddCalendarEntryDialog.OnCalendarEntryClicked;
import com.nativex.monetization.theme.ThemeElementTypes;
import com.nativex.monetization.theme.ThemeManager;
import com.nativex.network.volley.DefaultRetryPolicy;
import java.util.List;

public class AddCalendarEntryDialogBody extends LinearLayout {
    @IdRes
    private static final int ID_BODY_BUTTON = 4344345;
    public static final int ID_BODY_IMAGE = 444334;
    @IdRes
    private static final int ID_BODY_TEXT = 4344343;
    private AdvancedButton button = null;
    private OnCalendarEntryClicked onCalendarClickedListener;
    private final OnClickListener onCalendarEntryClick = new OnClickListener() {
        public void onClick(View v) {
            if ((v.getTag() instanceof Calendar) && AddCalendarEntryDialogBody.this.onCalendarClickedListener != null) {
                AddCalendarEntryDialogBody.this.onCalendarClickedListener.onClick((Calendar) v.getTag());
            }
        }
    };
    private LinearLayout scrollContainer = null;
    private ScrollView scroller = null;
    private TextView text = null;

    public AddCalendarEntryDialogBody(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.scrollContainer = new LinearLayout(getContext());
        this.text = new TextView(getContext());
        this.scroller = new ScrollView(getContext());
        this.button = new AdvancedButton(getContext());
        this.scrollContainer.addView(this.text);
        this.scroller.addView(this.scrollContainer);
        addView(this.scroller);
        addView(this.button);
        this.scrollContainer.setOrientation(1);
        setOrientation(1);
        this.text.setId(ID_BODY_TEXT);
        this.scrollContainer.setLayoutParams(new LayoutParams(-1, -2));
        this.text.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        this.text.setPadding(5, 5, 5, 10);
        this.text.setTextSize(16.0f);
        this.text.setTextColor(ThemeManager.getColor(ThemeElementTypes.MESSAGE_DIALOG_BODY_TEXT_COLOR).intValue());
        this.text.setShadowLayer(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, 0.0f, -2.0f, ViewCompat.MEASURED_STATE_MASK);
        this.text.setGravity(17);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        params.setMargins(5, 5, 5, 5);
        this.scroller.setLayoutParams(params);
        ThemeManager.setViewBackground(this.scroller, ThemeElementTypes.MESSAGE_DIALOG_BODY_BACKGROUND);
        params = new LinearLayout.LayoutParams(-1, -2);
        params.setMargins(70, 5, 70, 5);
        this.button.setBackgroundPressedDrawable(ThemeManager.getDrawable(ThemeElementTypes.MESSAGE_DIALOG_OK_BUTTON_BACKGROUND_PRESSED));
        this.button.setBackgroundDepressedDrawable(ThemeManager.getDrawable(ThemeElementTypes.MESSAGE_DIALOG_OK_BUTTON_BACKGROUND_NORMAL));
        this.button.setPressedTextColor(ThemeManager.getColor(ThemeElementTypes.MESSAGE_DIALOG_OK_BUTTON_TEXT_COLOR).intValue());
        this.button.setDepressedTextColor(ThemeManager.getColor(ThemeElementTypes.MESSAGE_DIALOG_OK_BUTTON_TEXT_COLOR).intValue());
        this.button.setFocusedTextColor(ThemeManager.getColor(ThemeElementTypes.MESSAGE_DIALOG_OK_BUTTON_TEXT_COLOR).intValue());
        this.button.setLayoutParams(params);
        this.button.setId(4344345);
    }

    public void setText(String text) {
        try {
            this.text.setText(text);
        } catch (Exception e) {
            Log.e("MessageDialogBody: Unexpected exception caught in setText()");
            e.printStackTrace();
        }
    }

    public void setTextSize(int textSize) {
        try {
            this.text.setTextSize((float) textSize);
        } catch (Exception e) {
            Log.e("MessageDialogBody: Unexpected exception caught in setTextSize()");
            e.printStackTrace();
        }
    }

    public void setTextColor(int color) {
        try {
            this.text.setTextColor(color);
        } catch (Exception e) {
            Log.e("MessageDialogBody: Unexpected exception caught in setTextColor()");
            e.printStackTrace();
        }
    }

    public void setButtonText(String text) {
        this.button.setText(text);
    }

    public void setButtonClickListener(OnClickListener listener) {
        this.button.setOnClickListener(listener);
    }

    public void release() {
        if (this.scroller != null) {
            this.scroller.setBackgroundDrawable(null);
            this.scroller.removeAllViews();
            this.scroller = null;
        }
        if (this.button != null) {
            this.button.release();
            this.button = null;
        }
        removeAllViews();
    }

    public void setOnCalendarClickedListener(OnCalendarEntryClicked listener) {
        this.onCalendarClickedListener = listener;
    }

    public void addCalendars(List<Calendar> calendars) {
        for (Calendar calendar : calendars) {
            addCalendarView(calendar);
        }
    }

    private void addCalendarView(Calendar calendar) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        params.gravity = 8388611;
        params.setMargins(5, 5, 5, 5);
        TextView textView = new TextView(getContext());
        textView.setBackgroundColor(Color.argb(80, 80, 80, 80));
        textView.setText(calendar.getName());
        textView.setMinHeight(50);
        textView.setLayoutParams(params);
        textView.setTextColor(ThemeManager.getColor(ThemeElementTypes.MESSAGE_DIALOG_BODY_TEXT_COLOR).intValue());
        textView.setTextSize(20.0f);
        textView.setOnClickListener(this.onCalendarEntryClick);
        textView.setTag(calendar);
        this.scrollContainer.addView(textView);
    }
}
