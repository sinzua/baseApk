package pl.tajchert.sample;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import com.nativex.network.volley.DefaultRetryPolicy;
import java.util.Iterator;
import pl.tajchert.waitingdots.R;

public class DotsTextView extends TextView {
    private boolean autoPlay;
    private JumpingSpan dotOne;
    private JumpingSpan dotThree;
    private JumpingSpan dotTwo;
    private Handler handler;
    private boolean isHide;
    private boolean isPlaying;
    private int jumpHeight;
    private boolean lockDotOne;
    private boolean lockDotThree;
    private boolean lockDotTwo;
    private AnimatorSet mAnimatorSet = new AnimatorSet();
    private int period;
    private int showSpeed = 700;
    private long startTime;
    private float textWidth;

    public DotsTextView(Context context) {
        super(context);
        init(context, null);
    }

    public DotsTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DotsTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.handler = new Handler(Looper.getMainLooper());
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaitingDots);
            this.period = typedArray.getInt(R.styleable.WaitingDots_period, 6000);
            this.jumpHeight = typedArray.getInt(R.styleable.WaitingDots_jumpHeight, (int) (getTextSize() / 4.0f));
            this.autoPlay = typedArray.getBoolean(R.styleable.WaitingDots_autoplay, true);
            typedArray.recycle();
        }
        this.dotOne = new JumpingSpan();
        this.dotTwo = new JumpingSpan();
        this.dotThree = new JumpingSpan();
        SpannableString spannable = new SpannableString("Loading...");
        spannable.setSpan(this.dotOne, 7, 8, 33);
        spannable.setSpan(this.dotTwo, 8, 9, 33);
        spannable.setSpan(this.dotThree, 9, 10, 33);
        setText(spannable, BufferType.SPANNABLE);
        this.textWidth = getPaint().measureText(".", 0, 1);
        createDotJumpAnimator(this.dotOne, 0).addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                DotsTextView.this.invalidate();
            }
        });
        this.mAnimatorSet.playTogether(new Animator[]{dotOneJumpAnimator, createDotJumpAnimator(this.dotTwo, (long) (this.period / 6)), createDotJumpAnimator(this.dotThree, (long) ((this.period * 2) / 6))});
        this.isPlaying = this.autoPlay;
        if (this.autoPlay) {
            start();
        }
    }

    public void start() {
        this.isPlaying = true;
        setAllAnimationsRepeatCount(-1);
        this.mAnimatorSet.start();
    }

    private ObjectAnimator createDotJumpAnimator(JumpingSpan jumpingSpan, long delay) {
        ObjectAnimator jumpAnimator = ObjectAnimator.ofFloat(jumpingSpan, "translationY", new float[]{0.0f, (float) (-this.jumpHeight)});
        jumpAnimator.setEvaluator(new TypeEvaluator<Number>() {
            public Number evaluate(float fraction, Number from, Number to) {
                return Double.valueOf(Math.max(0.0d, Math.sin((((double) fraction) * 3.141592653589793d) * 2.0d)) * ((double) (to.floatValue() - from.floatValue())));
            }
        });
        jumpAnimator.setDuration((long) this.period);
        jumpAnimator.setStartDelay(delay);
        jumpAnimator.setRepeatCount(-1);
        jumpAnimator.setRepeatMode(1);
        return jumpAnimator;
    }

    public void stop() {
        this.isPlaying = false;
        setAllAnimationsRepeatCount(0);
    }

    private void setAllAnimationsRepeatCount(int repeatCount) {
        Iterator it = this.mAnimatorSet.getChildAnimations().iterator();
        while (it.hasNext()) {
            Animator animator = (Animator) it.next();
            if (animator instanceof ObjectAnimator) {
                ((ObjectAnimator) animator).setRepeatCount(repeatCount);
            }
        }
    }

    public void hide() {
        createDotHideAnimator(this.dotThree, 2.0f).start();
        ObjectAnimator dotTwoMoveRightToLeft = createDotHideAnimator(this.dotTwo, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        dotTwoMoveRightToLeft.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                DotsTextView.this.invalidate();
            }
        });
        dotTwoMoveRightToLeft.start();
        this.isHide = true;
    }

    public void show() {
        createDotShowAnimator(this.dotThree, 2).start();
        ObjectAnimator dotTwoMoveRightToLeft = createDotShowAnimator(this.dotTwo, 1);
        dotTwoMoveRightToLeft.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                DotsTextView.this.invalidate();
            }
        });
        dotTwoMoveRightToLeft.start();
        this.isHide = false;
    }

    private ObjectAnimator createDotHideAnimator(JumpingSpan span, float widthMultiplier) {
        return createDotHorizontalAnimator(span, 0.0f, (-this.textWidth) * widthMultiplier);
    }

    private ObjectAnimator createDotShowAnimator(JumpingSpan span, int widthMultiplier) {
        return createDotHorizontalAnimator(span, (-this.textWidth) * ((float) widthMultiplier), 0.0f);
    }

    private ObjectAnimator createDotHorizontalAnimator(JumpingSpan span, float from, float to) {
        ObjectAnimator dotThreeMoveRightToLeft = ObjectAnimator.ofFloat(span, "translationX", new float[]{from, to});
        dotThreeMoveRightToLeft.setDuration((long) this.showSpeed);
        return dotThreeMoveRightToLeft;
    }

    public void showAndPlay() {
        show();
        start();
    }

    public void hideAndStop() {
        hide();
        stop();
    }

    public boolean isHide() {
        return this.isHide;
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    public void setJumpHeight(int jumpHeight) {
        this.jumpHeight = jumpHeight;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
}
