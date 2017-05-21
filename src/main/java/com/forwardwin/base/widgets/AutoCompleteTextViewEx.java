package com.forwardwin.base.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

public class AutoCompleteTextViewEx extends AutoCompleteTextView {
    public void performEmptyFilter(String text) {
        performFiltering(text, 0);
    }

    public boolean enoughToFilter() {
        return true;
    }

    public AutoCompleteTextViewEx(Context context) {
        super(context);
    }

    public AutoCompleteTextViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
