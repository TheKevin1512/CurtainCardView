package dom.shimmer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import dom.shimmer.view.CurtainCardView;

public class MainActivity extends AppCompatActivity {

    private CurtainCardView cardView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.cardView = (CurtainCardView) findViewById(R.id.domCard);
        this.button = (Button) findViewById(R.id.btn);
        this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardView.setIcon(R.drawable.ic_clear);
            }
        });
    }
}
