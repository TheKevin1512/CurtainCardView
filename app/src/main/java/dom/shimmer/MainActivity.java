package dom.shimmer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import dom.shimmer.view.DomCard;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.domCard)
    DomCard domCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        View shaderContainer = ViewGroup.inflate(this, R.layout.shader_layout, null);
//        domCard.setShaderContainer(shaderContainer);
//        domCard.performClick();
    }
}
