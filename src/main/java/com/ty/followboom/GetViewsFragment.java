package com.ty.followboom;

import android.view.View;
import android.widget.AdapterView;
import com.forwardwin.base.widgets.ToastHelper;
import com.ty.entities.PostItem;
import com.ty.followboom.adapters.PostGridViewAdapter;
import com.ty.followboom.helpers.TrackHelper;
import com.ty.followboom.helpers.VLTools;
import com.ty.instaview.R;

public class GetViewsFragment extends PostFragment {
    protected void onActivate(View v) {
        this.mAdapter = new PostGridViewAdapter(getActivity(), 5);
        super.onActivate(v);
        this.mTitle.setText(R.string.get_views_hint);
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        TrackHelper.track(TrackHelper.CATEGORY_GET_VIEWS, TrackHelper.ACTION_ITEM, "click");
        PostItem videoPost = this.mAdapter.getItem(position);
        if (videoPost.getMedia_type() == 2) {
            VLTools.gotoPostActivity(getActivity(), videoPost, 5);
        } else {
            ToastHelper.showToast(getActivity(), "This is not a video, choose a video to get views!");
        }
    }
}
