package wave.rn.yy.com.yy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import wave.rn.yy.com.yy.R;

public class MainActivity extends Activity {
    private ImageView head;
    private WaveView wave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wave = findViewById(R.id.wave);
        head = findViewById(R.id.head);
        wave.start();
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wave.isWave()) {
                    wave.stop();
                } else {
                    wave.start();

                }
            }
        });
    }
}