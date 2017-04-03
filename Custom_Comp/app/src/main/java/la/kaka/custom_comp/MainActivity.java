package la.kaka.custom_comp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final VolumeControlView view = (VolumeControlView)findViewById(R.id.volueme);
        view.setKnobListener(new VolumeControlView.KnobListener(){

            @Override
            public void onChanged(double angle) {
                if(angle > 0)
                {
                    ;
                }
                else
                {
                    ;
                }
            }
        });
    }
}
