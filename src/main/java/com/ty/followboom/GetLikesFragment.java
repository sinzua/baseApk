package com.ty.followboom;

import android.view.View;
import android.widget.AdapterView;
import com.ty.followboom.adapters.PostGridViewAdapter;
import com.ty.followboom.helpers.TrackHelper;
import com.ty.followboom.helpers.VLTools;
import com.ty.instaview.R;

public class GetLikesFragment extends PostFragment {
    protected void onActivate(View v) {
        this.mAdapter = new PostGridViewAdapter(getActivity(), 2);
        super.onActivate(v);
        this.mTitle.setText(R.string.get_likes_hint);
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        TrackHelper.track(TrackHelper.CATEGORY_GET_LIKES, TrackHelper.ACTION_ITEM, "click");
        VLTools.gotoPostActivity(getActivity(), this.mAdapter.getItem(position), 2);
    }
}
