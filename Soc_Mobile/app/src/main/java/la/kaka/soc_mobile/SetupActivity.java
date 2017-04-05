package la.kaka.soc_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by Administrator on 2017-04-05.
 */

public class SetupActivity extends ActionBarActivity{

    RadioGroup group;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        group = (RadioGroup)findViewById(R.id.radio_group);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                Intent intent = new Intent();
                String send_string = "";

                switch(checkedId)
                {
                    case R.id.radio_1:

                        send_string = "초보자";
                        break;

                    case R.id.radio_2:

                        send_string = "중급";
                        break;

                    case R.id.radio_3:

                        send_string = "고급";
                        break;
                }

                intent.putExtra("INPUT_TEXT", send_string);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


}
