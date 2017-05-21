package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.View;
import com.nativex.network.volley.DefaultRetryPolicy;

class FloatingActionButtonHoneycombMr1 extends FloatingActionButtonEclairMr1 {
    private boolean mIsHiding;

    FloatingActionButtonHoneycombMr1(View view, ShadowViewDelegate shadowViewDelegate) {
        super(view, shadowViewDelegate);
    }

    boolean requirePreDrawListener() {
        return true;
    }

    void onPreDraw() {
        updateFromViewRotation(this.mView.getRotation());
    }

    void hide(@Nullable final InternalVisibilityChangedListener listener) {
        if (this.mIsHiding || this.mView.getVisibility() != 0) {
            if (listener != null) {
                listener.onHidden();
            }
        } else if (!ViewCompat.isLaidOut(this.mView) || this.mView.isInEditMode()) {
            this.mView.setVisibility(8);
            if (listener != null) {
                listener.onHidden();
            }
        } else {
            this.mView.animate().scaleX(0.0f).scaleY(0.0f).alpha(0.0f).setDuration(200).setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).setListener(new AnimatorListenerAdapter() {
                public void onAnimationStart(Animator animation) {
                    FloatingActionButtonHoneycombMr1.this.mIsHiding = true;
                    FloatingActionButtonHoneycombMr1.this.mView.setVisibility(0);
                }

                public void onAnimationCancel(Animator animation) {
                    FloatingActionButtonHoneycombMr1.this.mIsHiding = false;
                }

                public void onAnimationEnd(Animator animation) {
                    FloatingActionButtonHoneycombMr1.this.mIsHiding = false;
                    FloatingActionButtonHoneycombMr1.this.mView.setVisibility(8);
                    if (listener != null) {
                        listener.onHidden();
                    }
                }
            });
        }
    }

    void show(@Nullable final InternalVisibilityChangedListener listener) {
        if (this.mView.getVisibility() == 0) {
            return;
        }
        if (!ViewCompat.isLaidOut(this.mView) || this.mView.isInEditMode()) {
            this.mView.setVisibility(0);
            this.mView.setAlpha(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            this.mView.setScaleY(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            this.mView.setScaleX(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            if (listener != null) {
                listener.onShown();
                return;
            }
            return;
        }
        this.mView.setAlpha(0.0f);
        this.mView.setScaleY(0.0f);
        this.mView.setScaleX(0.0f);
        this.mView.animate().scaleX(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT).scaleY(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT).alpha(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT).setDuration(200).setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).setListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animation) {
                FloatingActionButtonHoneycombMr1.this.mView.setVisibility(0);
            }

            public void onAnimationEnd(Animator animation) {
                if (listener != null) {
                    listener.onShown();
                }
            }
        });
    }

    private void updateFromViewRotation(float rotation) {
        if (this.mShadowDrawable != null) {
            this.mShadowDrawable.setRotation(-rotation);
        }
        if (this.mBorderDrawable != null) {
            this.mBorderDrawable.setRotation(-rotation);
        }
    }
}
