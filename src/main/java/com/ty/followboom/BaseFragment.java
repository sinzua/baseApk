package com.ty.followboom;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import com.ty.instaview.R;

public class BaseFragment extends Fragment implements OnClickListener {
    private static final String TAG = "BaseFragment";
    protected View mRootView;

    protected void initTitleViews() {
    }

    public void onClick(View v) {
        int id = v.getId();
        if (R.id.sidebar_button == id || R.id.download_button != id) {
        }
    }
}
