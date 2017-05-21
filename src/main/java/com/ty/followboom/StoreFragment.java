package com.ty.followboom;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.amazon.device.iap.PurchasingListener;
import com.amazon.device.iap.PurchasingService;
import com.amazon.device.iap.model.FulfillmentResult;
import com.amazon.device.iap.model.ProductDataResponse;
import com.amazon.device.iap.model.PurchaseResponse;
import com.amazon.device.iap.model.PurchaseUpdatesResponse;
import com.amazon.device.iap.model.PurchaseUpdatesResponse.RequestStatus;
import com.amazon.device.iap.model.Receipt;
import com.amazon.device.iap.model.UserDataResponse;
import com.amazon.iap.consumable.AmazonIAPHelper;
import com.anjlab.android.iab.v3.SkuDetails;
import com.forwardwin.base.widgets.ToastHelper;
import com.ty.followboom.adapters.StoreListAdapter;
import com.ty.followboom.entities.BuyCoinsConfirmData;
import com.ty.followboom.entities.Goods;
import com.ty.followboom.helpers.GoodsHelper;
import com.ty.followboom.helpers.TrackHelper;
import com.ty.followboom.helpers.VLTools;
import com.ty.followboom.models.CoinsManager;
import com.ty.followboom.models.LikeServerInstagram;
import com.ty.followboom.okhttp.RequestCallback;
import com.ty.followboom.okhttp.responses.BuyCoinsConfirmResponse;
import com.ty.followboom.views.CustomDialog;
import com.ty.instaview.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StoreFragment extends BaseFragment implements OnClickListener {
    private static final int AMAZON_PURCHASE_FAILED = 4;
    private static final int AMAZON_PURCHASE_SUCCEED = 3;
    private static final int AUTHENTICATION_FAILED = 2;
    private static final int GET_PRICE_FAILED = 1;
    private static final int GET_PRICE_SUCCEED = 0;
    private static final String TAG = "StoreFragment";
    private static final String TAG_AMAZON = "amazon";
    private static final String TAG_IABV3 = "iabv3";
    private Handler mAmazonPurchasingHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Log.d(StoreFragment.TAG_AMAZON, "get price succeed");
                    VLTools.hideLoadingView(StoreFragment.this.mLoadingView, StoreFragment.this.mLoadingItem, StoreFragment.this.mLoadingFailedView);
                    StoreFragment.this.setSkuDetails();
                    StoreFragment.this.mStoreListAdapter.setGoodsData(GoodsHelper.getGoodsDataCoins(StoreFragment.this.getActivity()));
                    StoreFragment.this.mStoreListAdapter.notifyDataSetChanged();
                    return;
                case 1:
                    Log.d(StoreFragment.TAG_AMAZON, "get price failed");
                    VLTools.hideLoadingView(StoreFragment.this.mLoadingView, StoreFragment.this.mLoadingItem, StoreFragment.this.mLoadingFailedView);
                    StoreFragment.this.mStoreListAdapter.setGoodsData(GoodsHelper.getGoodsDataCoins(StoreFragment.this.getActivity()));
                    StoreFragment.this.mStoreListAdapter.notifyDataSetChanged();
                    return;
                case 2:
                    VLTools.logout(StoreFragment.this.getActivity());
                    return;
                default:
                    return;
            }
        }
    };
    private CustomDialog mCustomDialog;
    private OnClickListener mCustomDialogClickListener = new OnClickListener() {
        public void onClick(View v) {
            int id = v.getId();
            if (R.id.positive_button == id) {
                Log.d("StoreFragment", "buy confirmed");
                TrackHelper.track("StoreFragment", TrackHelper.ACTION_CONFIRM, "click");
                if (VLTools.isAmazon(StoreFragment.this.getActivity())) {
                    PurchasingService.purchase(StoreFragment.this.mGoods.getGoodsId());
                } else if (!VLTools.isYandex(StoreFragment.this.getActivity())) {
                    if (VLTools.isGoogle(StoreFragment.this.getActivity())) {
                        ((MainActivity) StoreFragment.this.getActivity()).mBillingProcessor.purchase(StoreFragment.this.getActivity(), StoreFragment.this.mGoods.getGoodsId());
                    } else {
                        ((MainActivity) StoreFragment.this.getActivity()).mBillingProcessor.purchase(StoreFragment.this.getActivity(), StoreFragment.this.mGoods.getGoodsId());
                    }
                }
                StoreFragment.this.mCustomDialog.dismiss();
            } else if (R.id.negative_button == id) {
                Log.d("StoreFragment", "buy canceled");
                TrackHelper.track("StoreFragment", TrackHelper.ACTION_CANCEL, "click");
                StoreFragment.this.mCustomDialog.dismiss();
            }
        }
    };
    private Handler mGetPriceHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Log.d(StoreFragment.TAG_IABV3, "get price succeed");
                    VLTools.hideLoadingView(StoreFragment.this.mLoadingView, StoreFragment.this.mLoadingItem, StoreFragment.this.mLoadingFailedView);
                    StoreFragment.this.setSkuDetails();
                    StoreFragment.this.mStoreListAdapter.setGoodsData(GoodsHelper.getGoodsDataCoins(StoreFragment.this.getActivity()));
                    StoreFragment.this.mStoreListAdapter.notifyDataSetChanged();
                    return;
                case 1:
                    Log.d(StoreFragment.TAG_IABV3, "get price failed");
                    VLTools.hideLoadingView(StoreFragment.this.mLoadingView, StoreFragment.this.mLoadingItem, StoreFragment.this.mLoadingFailedView);
                    StoreFragment.this.mStoreListAdapter.setGoodsData(GoodsHelper.getGoodsDataCoins(StoreFragment.this.getActivity()));
                    StoreFragment.this.mStoreListAdapter.notifyDataSetChanged();
                    return;
                case 2:
                    VLTools.logout(StoreFragment.this.getActivity());
                    return;
                default:
                    return;
            }
        }
    };
    private Goods mGoods;
    private OnClickListener mItemClickListener = new OnClickListener() {
        public void onClick(View view) {
            if ((view.getTag() instanceof Goods) && (StoreFragment.this.getActivity() instanceof MainActivity)) {
                StoreFragment.this.mGoods = (Goods) view.getTag();
                if (((MainActivity) StoreFragment.this.getActivity()).mReadyToPurchase) {
                    TrackHelper.track("StoreFragment", TrackHelper.ACTION_BUY, StoreFragment.this.mGoods.getGoodsId());
                    StoreFragment.this.mCustomDialog = new CustomDialog(StoreFragment.this.getActivity(), StoreFragment.this.getResources().getString(R.string.buy_coins_confirm_title), String.format(StoreFragment.this.getResources().getString(R.string.buy_coins_confirm_content), new Object[]{StoreFragment.this.mGoods.getTitle().replace(" {}", ""), Double.valueOf(StoreFragment.this.mGoods.getPrice())}), StoreFragment.this.mCustomDialogClickListener);
                    StoreFragment.this.mCustomDialog.show();
                    return;
                }
                ToastHelper.showToast(StoreFragment.this.getActivity(), StoreFragment.this.getResources().getString(R.string.iab_prepare_not_completed));
            } else if (!(view.getTag() instanceof String) || !(StoreFragment.this.getActivity() instanceof MainActivity)) {
            }
        }
    };
    private LinearLayout mLoadingFailedView;
    private LinearLayout mLoadingItem;
    private LinearLayout mLoadingView;
    private PurchasingListener mPurchasingListener = new PurchasingListener() {
        public void onUserDataResponse(UserDataResponse userDataResponse) {
            Log.d("StoreFragment", "onUserDataResponse");
        }

        public void onProductDataResponse(ProductDataResponse productDataResponse) {
            Log.d("StoreFragment", "onProductDataResponse");
            try {
                StoreFragment.this.mSkuDetails = AmazonIAPHelper.getSkusDetails(productDataResponse);
                StoreFragment.this.mAmazonPurchasingHandler.sendEmptyMessage(0);
            } catch (Exception e) {
                StoreFragment.this.mAmazonPurchasingHandler.sendEmptyMessage(1);
            }
        }

        public void onPurchaseResponse(PurchaseResponse purchaseResponse) {
            Log.d("StoreFragment", String.format("onPurchaseResponse userid:%s receiptid:%s", new Object[]{purchaseResponse.getUserData().getUserId(), purchaseResponse.getReceipt().getReceiptId()}));
            if (purchaseResponse.getRequestStatus().name().equalsIgnoreCase("successful")) {
                LikeServerInstagram.getSingleton().buyAmazonCoinsConfirm(StoreFragment.this.getActivity(), purchaseResponse.getReceipt().getSku(), purchaseResponse.getReceipt().getReceiptId(), purchaseResponse.getUserData().getUserId(), new BuyCoinsConfirmCallback(purchaseResponse.getReceipt().getReceiptId()));
            } else {
                ToastHelper.showToast(StoreFragment.this.getActivity(), "Purchase failed, please try it again.");
            }
        }

        public void onPurchaseUpdatesResponse(PurchaseUpdatesResponse purchaseUpdatesResponse) {
            Log.d("StoreFragment", "onPurchaseUpdatesResponse");
            if (purchaseUpdatesResponse != null && purchaseUpdatesResponse.getRequestStatus() == RequestStatus.SUCCESSFUL && purchaseUpdatesResponse.getReceipts().size() > 0) {
                for (Receipt receipt : purchaseUpdatesResponse.getReceipts()) {
                    LikeServerInstagram.getSingleton().buyAmazonCoinsConfirm(StoreFragment.this.getActivity(), receipt.getSku(), receipt.getReceiptId(), purchaseUpdatesResponse.getUserData().getUserId(), new BuyCoinsConfirmCallback(receipt.getReceiptId()));
                }
            }
        }
    };
    private List<SkuDetails> mSkuDetails;
    private ListView mStoreList;
    private StoreListAdapter mStoreListAdapter;

    private class BuyCoinsConfirmCallback extends RequestCallback<BuyCoinsConfirmResponse> {
        private String receiptId;

        public BuyCoinsConfirmCallback(String receiptId) {
            this.receiptId = receiptId;
        }

        public void onResponse(BuyCoinsConfirmResponse buyCoinsConfirmResponse) {
            if (buyCoinsConfirmResponse.isSuccessful()) {
                BuyCoinsConfirmData buyCoinsConfirmData = buyCoinsConfirmResponse.getData();
                PurchasingService.notifyFulfillment(this.receiptId, FulfillmentResult.FULFILLED);
                CoinsManager.getSingleton().getAccountInfo().setCoins(buyCoinsConfirmData.getCoin());
                Toast.makeText(StoreFragment.this.getActivity(), "Purchase confirm succeed", 0).show();
                return;
            }
            PurchasingService.notifyFulfillment(this.receiptId, FulfillmentResult.UNAVAILABLE);
            Toast.makeText(StoreFragment.this.getActivity(), String.format("Purchase confirm failed reason:%s", new Object[]{buyCoinsConfirmResponse.getStatus().getStatusMsg()}), 0).show();
        }

        public void onFailure(Exception e) {
            Toast.makeText(StoreFragment.this.getActivity(), "Purchase confirm failed reason:" + e.toString(), 0).show();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TrackHelper.track("StoreFragment", "show", "");
        if (VLTools.isAmazon(getActivity())) {
            AmazonIAPHelper.setupIAPOnCreate(getActivity(), this.mPurchasingListener);
        }
        this.mRootView = inflater.inflate(R.layout.fragment_store, container, false);
        onActivate(this.mRootView);
        return this.mRootView;
    }

    private void setSkuDetails() {
        if (VLTools.verifyArrayList((ArrayList) this.mSkuDetails) && GoodsHelper.getGoodsDataCoins(getActivity()) != null) {
            ArrayList<Goods> goodsList = GoodsHelper.getGoodsDataCoins(getActivity());
            if (VLTools.verifyArrayList(goodsList)) {
                Iterator it = goodsList.iterator();
                while (it.hasNext()) {
                    Goods goods = (Goods) it.next();
                    for (SkuDetails skuDetails : this.mSkuDetails) {
                        if (goods.getGoodsId().equals(skuDetails.productId)) {
                            goods.setSkuDetails(skuDetails);
                        }
                    }
                }
            }
        }
    }

    private void onActivate(View v) {
        initTitleViews();
        initContentView();
        initData();
    }

    private void initData() {
        if (VLTools.isAmazon(getActivity())) {
            getPriceFromAmazon(GoodsHelper.getProductIds(getActivity()));
        } else if (VLTools.isGoogle(getActivity())) {
            getPriceFromGoogle(GoodsHelper.getProductIds(getActivity()));
        }
    }

    private void initLoadingView() {
        this.mLoadingView = (LinearLayout) this.mRootView.findViewById(R.id.loading_view);
        this.mLoadingItem = (LinearLayout) this.mRootView.findViewById(R.id.loading_item);
        this.mLoadingFailedView = (LinearLayout) this.mRootView.findViewById(R.id.loading_failed);
        VLTools.hideLoadingView(this.mLoadingView, this.mLoadingItem, this.mLoadingFailedView);
        this.mLoadingFailedView.setOnClickListener(this);
    }

    private void initContentView() {
        this.mStoreList = (ListView) this.mRootView.findViewById(R.id.store_list);
        this.mStoreListAdapter = new StoreListAdapter(getActivity());
        this.mStoreListAdapter.setGoodsData(GoodsHelper.getGoodsDataCoins(getActivity()));
        this.mStoreListAdapter.setItemClickListener(this.mItemClickListener);
        this.mStoreList.setAdapter(this.mStoreListAdapter);
    }

    private void getPriceFromGoogle(final ArrayList<String> productIdList) {
        new AsyncTask<Integer, Integer, String>() {
            protected String doInBackground(Integer... arg0) {
                try {
                    StoreFragment.this.mSkuDetails = ((MainActivity) StoreFragment.this.getActivity()).mBillingProcessor.getPurchaseListingDetails(productIdList);
                    StoreFragment.this.mGetPriceHandler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (e.getMessage() == null || !e.getMessage().contains(VLTools.AUTHENTICATE)) {
                        StoreFragment.this.mGetPriceHandler.sendEmptyMessage(1);
                    } else {
                        StoreFragment.this.mGetPriceHandler.sendEmptyMessage(2);
                    }
                }
                return "";
            }
        }.execute(new Integer[]{Integer.valueOf(0)});
    }

    private void getPriceFromAmazon(ArrayList<String> productIdList) {
        AmazonIAPHelper.getProductData(productIdList);
    }

    public void onResume() {
        super.onResume();
        if (VLTools.isAmazon(getActivity())) {
            PurchasingService.getPurchaseUpdates(false);
        } else if (!VLTools.isGoogle(getActivity())) {
        }
    }

    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.loading_failed:
                VLTools.showLoadingView(this.mLoadingView, this.mLoadingItem, this.mLoadingFailedView);
                return;
            default:
                return;
        }
    }
}
