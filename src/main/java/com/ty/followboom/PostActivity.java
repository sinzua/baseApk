package com.ty.followboom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.forwardwin.base.widgets.JsonSerializer;
import com.forwardwin.base.widgets.ToastHelper;
import com.ty.entities.ImageVersion;
import com.ty.entities.PostItem;
import com.ty.followboom.adapters.LikesListAdapter;
import com.ty.followboom.entities.CoinsInAccount;
import com.ty.followboom.entities.Goods;
import com.ty.followboom.helpers.GoodsHelper;
import com.ty.followboom.helpers.PicassoHelper;
import com.ty.followboom.helpers.TrackHelper;
import com.ty.followboom.helpers.VLTools;
import com.ty.followboom.models.CoinsManager;
import com.ty.followboom.models.LikeServerInstagram;
import com.ty.followboom.okhttp.RequestCallback;
import com.ty.followboom.okhttp.responses.CommonCoinsResponse;
import com.ty.followboom.views.CustomDialog;
import com.ty.instaview.R;

public class PostActivity extends Activity implements OnClickListener {
    public static final int GET_LIKES = 2;
    public static final int GET_LOOPS = 5;
    public static final int GET_REVINES = 4;
    public static final int GET_VIEWS = 5;
    private static final String TAG = "GetLikesActivity";
    public static final String TYPE = "type";
    public static final String VIDEO_POST_PARAMS = "video_post";
    private CustomDialog mCustomDialog;
    private OnClickListener mCustomDialogClickListener = new OnClickListener() {
        public void onClick(View v) {
            int id = v.getId();
            if (R.id.positive_button == id) {
                TrackHelper.track("GetLikesActivity", TrackHelper.ACTION_GET_LIKES, TrackHelper.LABEL_POSITIVE);
                try {
                    if (5 == PostActivity.this.mType) {
                        LikeServerInstagram.getSingleton().getViews(PostActivity.this, Long.toString(PostActivity.this.mVideoPost.getPk()), ((ImageVersion) PostActivity.this.mVideoPost.getImage_versions().get(0)).getUrl(), ((ImageVersion) PostActivity.this.mVideoPost.getImage_versions().get(1)).getUrl(), ((ImageVersion) PostActivity.this.mVideoPost.getImage_versions().get(0)).getUrl(), PostActivity.this.mGoods.getGoodsId(), PostActivity.this.mType, PostActivity.this.mVideoPost.getUser().getUsername(), PostActivity.this.mVideoPost.getCode(), PostActivity.this.mVideoPost.getOrganic_tracking_token(), Long.valueOf(Long.parseLong(PostActivity.this.mVideoPost.getUser().getPk())), Long.valueOf(PostActivity.this.mVideoPost.getTaken_at()), (int) PostActivity.this.mVideoPost.getView_count(), PostActivity.this.mGetLikesCallback);
                    } else {
                        LikeServerInstagram.getSingleton().getLikes(PostActivity.this, Long.toString(PostActivity.this.mVideoPost.getPk()), ((ImageVersion) PostActivity.this.mVideoPost.getImage_versions().get(0)).getUrl(), ((ImageVersion) PostActivity.this.mVideoPost.getImage_versions().get(1)).getUrl(), ((ImageVersion) PostActivity.this.mVideoPost.getImage_versions().get(0)).getUrl(), PostActivity.this.mGoods.getGoodsId(), PostActivity.this.mType, PostActivity.this.mVideoPost.getUser().getUsername(), PostActivity.this.mVideoPost.getCode(), PostActivity.this.mVideoPost.getLike_count(), PostActivity.this.mGetLikesCallback);
                    }
                    PostActivity.this.mCustomDialog.dismiss();
                    PostActivity.this.mProgressDialog = ProgressDialog.show(PostActivity.this, PostActivity.this.getResources().getString(R.string.deal_processing_title), PostActivity.this.getResources().getString(R.string.deal_processing_content));
                } catch (Exception e) {
                    Toast.makeText(PostActivity.this, PostActivity.this.getResources().getString(R.string.get_item_again), 0).show();
                    PostActivity.this.mCustomDialog.dismiss();
                }
            } else if (R.id.negative_button == id) {
                TrackHelper.track("GetLikesActivity", TrackHelper.ACTION_GET_LIKES, TrackHelper.LABEL_NEGATIVE);
                PostActivity.this.mCustomDialog.dismiss();
            } else if (R.id.sidebar_button == id) {
                TrackHelper.track(TrackHelper.CATEGORY_MAIN, TrackHelper.ACTION_SIDERBAR, "click");
                PostActivity.this.finish();
            } else if (R.id.download_button == id) {
                TrackHelper.track(TrackHelper.CATEGORY_MAIN, TrackHelper.ACTION_DOWNLOAD_FRAGMENT, "click");
            }
        }
    };
    protected ImageView mDownloadButton;
    protected TextView mFragmentTitle;
    private RequestCallback<CommonCoinsResponse> mGetLikesCallback = new RequestCallback<CommonCoinsResponse>() {
        public void onResponse(CommonCoinsResponse commonCoinsResponse) {
            if (PostActivity.this.mProgressDialog != null) {
                PostActivity.this.mProgressDialog.dismiss();
            }
            if (commonCoinsResponse != null && commonCoinsResponse.isSuccessful()) {
                CoinsInAccount coinsInAccount = commonCoinsResponse.getData();
                if (coinsInAccount != null) {
                    CoinsManager.getSingleton().setCoins(coinsInAccount.getCoinsInAccount());
                }
                if (2 == PostActivity.this.mType) {
                    Toast.makeText(PostActivity.this, PostActivity.this.getResources().getString(R.string.get_likes_succeed), 0).show();
                } else if (5 == PostActivity.this.mType) {
                    Toast.makeText(PostActivity.this, PostActivity.this.getResources().getString(R.string.get_views_succeed), 0).show();
                }
            } else if (commonCoinsResponse == null || !commonCoinsResponse.isSessionExpired()) {
                Toast.makeText(PostActivity.this, PostActivity.this.getResources().getString(R.string.get_item_again), 0).show();
            } else {
                VLTools.gotoLogin(PostActivity.this);
            }
        }

        public void onFailure(Exception e) {
            if (PostActivity.this.mProgressDialog != null) {
                PostActivity.this.mProgressDialog.dismiss();
            }
            Toast.makeText(PostActivity.this, PostActivity.this.getResources().getString(R.string.get_item_again), 0).show();
        }
    };
    private Goods mGoods;
    private ImageView mImageView;
    private OnClickListener mItemClickListener = new OnClickListener() {
        public void onClick(View view) {
            PostActivity.this.mGoods = (Goods) view.getTag();
            if (PostActivity.this.mGoods == null || PostActivity.this.mGoods.getTitle() == null) {
                Toast.makeText(PostActivity.this, PostActivity.this.getResources().getString(R.string.get_item_again), 0).show();
                return;
            }
            TrackHelper.track("GetLikesActivity", TrackHelper.ACTION_GET_LIKES, PostActivity.this.mGoods.getGoodsId());
            String title = "";
            String content = "";
            if (2 == PostActivity.this.mType) {
                title = PostActivity.this.getResources().getString(R.string.buy_likes_confirm_title);
                content = String.format(PostActivity.this.getResources().getString(R.string.buy_likes_confirm_content), new Object[]{PostActivity.this.mGoods.getTitle().replace(" {}", ""), VLTools.removePointIfHave(PostActivity.this.mGoods.getPrice() + "")});
            } else if (5 == PostActivity.this.mType) {
                title = PostActivity.this.getResources().getString(R.string.buy_views_confirm_title);
                content = String.format(PostActivity.this.getResources().getString(R.string.buy_views_confirm_content), new Object[]{PostActivity.this.mGoods.getTitle().replace(" {}", ""), VLTools.removePointIfHave(PostActivity.this.mGoods.getPrice() + "")});
            }
            if (PostActivity.this.mGoods.getPrice() <= Double.parseDouble(CoinsManager.getSingleton().getAccountInfo().getCoins())) {
                PostActivity.this.mCustomDialog = new CustomDialog(PostActivity.this, title, content, PostActivity.this.mCustomDialogClickListener);
                PostActivity.this.mCustomDialog.show();
                return;
            }
            ToastHelper.showToast(PostActivity.this, "Coins not enough, try get more coins!");
        }
    };
    private TextView mLikeCount;
    private ListView mLikeList;
    private LikesListAdapter mLikesListAdapter;
    private ImageView mLogo;
    private ProgressDialog mProgressDialog;
    protected ImageView mSidebarButton;
    private int mType;
    private PostItem mVideoPost;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_get_likes);
        this.mVideoPost = (PostItem) JsonSerializer.getInstance().deserialize(getIntent().getStringExtra(VIDEO_POST_PARAMS), PostItem.class);
        this.mType = getIntent().getIntExtra("type", 2);
        initTitleViews();
        IGApplication.getInstance().addActivity(this);
        TrackHelper.track("GetLikesActivity", "show", "");
        onActivate();
    }

    private void onActivate() {
        this.mSidebarButton.setImageResource(R.drawable.ic_back);
        this.mImageView = (ImageView) findViewById(R.id.image_view);
        this.mLogo = (ImageView) findViewById(R.id.logo);
        this.mLikeCount = (TextView) findViewById(R.id.like_count);
        if (this.mVideoPost != null) {
            PicassoHelper.setImageView(this, this.mImageView, ((ImageVersion) this.mVideoPost.getImage_versions().get(0)).getUrl(), R.drawable.placeholder);
            if (2 == this.mType) {
                this.mLogo.setImageResource(R.drawable.ic_heart);
                this.mLikeCount.setText("" + this.mVideoPost.getLike_count());
            } else if (5 == this.mType) {
                this.mLogo.setImageResource(R.drawable.ic_views);
                this.mLikeCount.setText("" + VLTools.removePointIfHave(Double.toString(this.mVideoPost.getView_count())));
            }
        }
        this.mLikeList = (ListView) findViewById(R.id.like_list);
        this.mLikesListAdapter = new LikesListAdapter(this);
        if (2 == this.mType) {
            this.mLikesListAdapter.setGoodsData(GoodsHelper.getGoodsDataLikes(this));
        } else if (5 == this.mType) {
            this.mLikesListAdapter.setGoodsData(GoodsHelper.getGoodsDataLoops(this));
        }
        this.mLikesListAdapter.setItemClickListener(this.mItemClickListener);
        this.mLikeList.setAdapter(this.mLikesListAdapter);
        this.mProgressDialog = new ProgressDialog(this);
    }

    protected void initTitleViews() {
        this.mSidebarButton = (ImageView) findViewById(R.id.sidebar_button);
        this.mFragmentTitle = (TextView) findViewById(R.id.fragment_title);
        if (2 == this.mType) {
            this.mFragmentTitle.setText(R.string.fragment_getlikes);
        } else if (5 == this.mType) {
            this.mFragmentTitle.setText(R.string.fragment_getviews);
        }
        this.mDownloadButton = (ImageView) findViewById(R.id.download_button);
        this.mDownloadButton.setVisibility(8);
        this.mSidebarButton.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sidebar_button:
                TrackHelper.track("GetLikesActivity", TrackHelper.ACTION_BACK, "click");
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                return;
            default:
                return;
        }
    }
}
