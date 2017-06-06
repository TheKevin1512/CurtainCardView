package dom.shimmer.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
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

import dom.shimmer.R;
import dom.shimmer.adapter.IDomModel;
import dom.shimmer.model.DomModel;

/**
 * Created by kevindom on 31/05/17.
 */

public class DomCard extends FrameLayout {
    private static final int RENDER_TIME = 25;
    private static final String TAG = "DomCard";
    private Paint paint;
    private Handler handler;
    private Runnable animation;
    private CardView cardView;
    private FloatingActionButton button;
    private IDomModel model;

    private ViewGroup shaderContainer;


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
        this.paint = new Paint();
        this.paint.setColor(Color.BLACK);
        this.paint.setAlpha(60);
        this.handler = new Handler();
        this.button = (FloatingActionButton) root.findViewById(R.id.floatingActionButton);
        this.button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation rotate;
                if (model.isOpen()) {
                    button.setImageResource(R.drawable.ic_open);
                    rotate = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    showShader(false);
                } else {
                    button.setImageResource(R.drawable.ic_clear);
                    rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    showShader(true);
                }
                rotate.setDuration(300);
                rotate.setFillAfter(true);
                button.startAnimation(rotate);
                model.toggle();
            }
        });
    }

    public ViewGroup getShaderContainer() {
        return shaderContainer;
    }

    public void setShaderContainer(ViewGroup container) {
        this.shaderContainer = container;
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
                if (enabled){
                    if (params.height < cardView.getHeight()) {
                        if (params.height == 0) params.height = 10;
                        params.height = (int) (params.height * delta);
                        handler.postDelayed(this, RENDER_TIME);
                    } else {
                        delta = 1.2f;
                    }
                } else {
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
                    } else cardView.addView(shaderContainer);
                }
            }, 50);
        }
    }

    public void setModel(IDomModel model) {
        this.model = model;
    }
}
