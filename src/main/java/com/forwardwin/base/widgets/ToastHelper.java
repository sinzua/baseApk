package com.forwardwin.base.widgets;

import android.content.Context;
import android.widget.Toast;

public class ToastHelper {
    public static void showToast(Context context, String toastString) {
        Toast.makeText(context, toastString, 0).show();
    }
}
