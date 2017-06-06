package dom.shimmer.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import dom.shimmer.R;
import dom.shimmer.adapter.IDomModel;
import dom.shimmer.model.DomModel;

/**
 * Created by kevindom on 31/05/17.
 */

public class DomCard extends FrameLayout {
    private static final int RENDER_TIME = 24;

    private Handler handler;
    private Runnable animation;
    private CardView cardView;
    private FloatingActionButton button;
    private IDomModel model;

    private View shaderContainer;
    private int icon = R.drawable.ic_open;


    public DomCard(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public DomCard(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DomCard(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        this.model = new DomModel();
        View root = inflate(context, R.layout.card_layout, this);
        this.cardView = (CardView) root.findViewById(R.id.cardView);
        this.handler = new Handler();
        this.button = (FloatingActionButton) root.findViewById(R.id.floatingActionButton);
        this.button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showShader(!model.isOpen());
                Animation rotate;
                if (model.isOpen()) {
                    button.setImageResource(icon);
                    rotate = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                } else {
                    button.setImageResource(R.drawable.ic_clear);
                    rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                }
                rotate.setDuration(300);
                rotate.setFillAfter(true);
                button.startAnimation(rotate);
                model.toggle();
            }
        });
        if (attrs != null) {
            TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.DomCard);
            int resId = attributes.getResourceId(R.styleable.DomCard_buttonIcon, 0);
            if (resId != 0) {
                this.button.setImageResource(resId);
                this.icon = resId;
            }
            int color = attributes.getColor(R.styleable.DomCard_buttonColor, 0);
            if (color != 0) {
                button.setBackgroundTintList(ColorStateList.valueOf(color));
            }
            attributes.recycle();
        }
        this.shaderContainer = new LinearLayout(context);
        this.shaderContainer.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0));
        this.shaderContainer.setBackgroundColor(button.getBackgroundTintList().getDefaultColor());
    }

    public View getShaderContainer() {
        return shaderContainer;
    }

    public void setShaderContainer(View view) {
        if (view == null) return;
        this.shaderContainer = view;
        this.shaderContainer.setBackgroundColor(button.getBackgroundTintList().getDefaultColor());
    }

    private void showShader(final boolean enabled) {
        final ViewGroup.LayoutParams params = shaderContainer.getLayoutParams();
        handler.removeCallbacks(animation);
        animation = new Runnable() {
            private float delta = 1.2f;

            @Override
            public void run() {
                invalidate();
                delta += 0.2;
                if (enabled) {
                    if (params.height < cardView.getMeasuredHeight()) {
                        if (params.height <= 0) params.height = 10;
                        params.height = (int) (params.height * delta);
                        handler.postDelayed(this, RENDER_TIME);
                    } else {
                        params.height = LayoutParams.MATCH_PARENT;
                        delta = 1.2f;
                    }
                } else {
                    if (params.height == LayoutParams.MATCH_PARENT)
                        params.height = cardView.getHeight();
                    if (params.height > 0) {
                        params.height = (int) (params.height / delta);
                        handler.postDelayed(this, RENDER_TIME);
                    } else {
                        delta = 1.2f;
                    }
                }
                shaderContainer.setLayoutParams(params);
            }
        };
        handler.postDelayed(animation, RENDER_TIME);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final int childCount = getChildCount();
        if (childCount > 0) {
            final Handler childViewHandler = new Handler();
            childViewHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (cardView.getChildCount() != childCount - 2) {
                        for (int i = 0; i < childCount; i++) {
                            View v = getChildAt(i);
                            if (v != null && !v.equals(cardView) && !v.equals(button)) {
                                removeView(v);
                                cardView.addView(v);
                            }
                        }
                        childViewHandler.postDelayed(this, 50);
                    }
                    else {
                        cardView.addView(shaderContainer);
                        shaderContainer.getLayoutParams().height = 0;
                    }
                }
            }, 50);
        }
    }

    public void setModel(IDomModel model) {
        this.model = model;
    }

    @Override
    public boolean performClick() {
        if (shaderContainer.getLayoutParams() != null)
            this.button.performClick();
        return model.isOpening();
    }
}
