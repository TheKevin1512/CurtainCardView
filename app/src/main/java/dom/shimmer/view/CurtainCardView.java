package dom.shimmer.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;

import dom.shimmer.R;
import dom.shimmer.animation.ResizeAnimation;

/**
 * Created by kevindom on 31/05/17.
 */

public class CurtainCardView extends FrameLayout {
    private CardView cardView;
    private FloatingActionButton button;

    private View curtain;
    private int icon = R.drawable.ic_open;
    private boolean isOpen;

    private int height;
    private int width;

    public CurtainCardView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public CurtainCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CurtainCardView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        View root = inflate(context, R.layout.card_layout, this);
        this.cardView = (CardView) root.findViewById(R.id.cardView);
        this.button = (FloatingActionButton) root.findViewById(R.id.floatingActionButton);
        this.button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurtain(!isOpen);
                Animation rotate;
                if (isOpen) {
                    button.setImageResource(icon);
                    rotate = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                } else {
                    button.setImageResource(R.drawable.ic_clear);
                    rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                }
                rotate.setDuration(300);
                rotate.setFillAfter(true);
                button.startAnimation(rotate);
                isOpen = !isOpen;
            }
        });
        applyStyling(context.obtainStyledAttributes(attrs, R.styleable.CurtainCardView));
    }

    private void applyStyling(TypedArray attributes) {
        int resId = attributes.getResourceId(R.styleable.CurtainCardView_buttonIcon, icon);
        setIcon(resId);
        int color = attributes.getColor(R.styleable.CurtainCardView_buttonColor, Color.CYAN);
        button.setBackgroundTintList(ColorStateList.valueOf(color));
        attributes.recycle();
    }

    public void setIcon(int icon) {
        this.button.setImageResource(icon);
        this.icon = icon;
    }

    public View getCurtain() {
        return curtain;
    }

    public void setCurtain(View view) {
        if (view == null) return;
        if (curtain != null) cardView.removeView(curtain);
        this.curtain = view;
        this.curtain.setVisibility(INVISIBLE);
        this.curtain.setBackgroundColor(button.getBackgroundTintList().getDefaultColor());
        this.cardView.addView(curtain);
    }

    private void showCurtain(final boolean enabled) {
        if (curtain == null) return;
        Animation resizeAnimation;

        if (enabled) {
            resizeAnimation = new ResizeAnimation(
                    curtain,
                    width == 0 ? curtain.getWidth() : width,
                    0,
                    width == 0 ? curtain.getWidth() : width,
                    height== 0 ? curtain.getHeight(): height
            );
        } else {
            resizeAnimation = new ResizeAnimation(curtain,
                    width == 0 ? curtain.getWidth() : width,
                    height== 0 ? curtain.getHeight() : height,
                    width == 0 ? curtain.getWidth() : width,
                    0
            );
        }
        resizeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (isOpen)
                    curtain.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!isOpen)
                    curtain.setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        resizeAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        resizeAnimation.setFillAfter(true);
        startAnimation(resizeAnimation);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.height == 0) this.height = cardView.getHeight();
        if (this.width == 0) this.width = cardView.getWidth();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            View viewToMove = getChildAt(i);
            if (!(viewToMove.equals(cardView)) && !(viewToMove.equals(button))) {
                removeView(viewToMove);
                cardView.addView(viewToMove);
            }
        }
        if (curtain != null)
            cardView.addView(curtain);
    }


    @Override
    public boolean performClick() {
        if (curtain.getLayoutParams() != null)
            this.button.performClick();
        return isOpen;
    }
}
