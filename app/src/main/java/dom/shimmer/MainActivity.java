package dom.shimmer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        LinearLayout shaderContainer = new LinearLayout(this);
        shaderContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        shaderContainer.setOrientation(LinearLayout.VERTICAL);

        domCard.setShaderContainer(shaderContainer);
    }
}
