package la.kaka.soc_mobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button button_1, button_2;
    static final int GET_STRING = 1;
    String get_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_1 = (Button)findViewById(R.id.button_1);
        button_2 = (Button)findViewById(R.id.button_2);

    }

    public void onClick(View view)
    {
        Intent intent;

        switch(view.getId())
        {
            case R.id.button_1:

                intent = new Intent(MainActivity.this, IntroActivity.class);
                startActivity(intent);
                break;

            case R.id.button_2:

                intent = new Intent(MainActivity.this, SetupActivity.class);
                startActivityForResult(intent, GET_STRING);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GET_STRING)
        {
            if(resultCode == RESULT_OK)
            {
                Toast.makeText(this, data.getStringExtra("INPUT_TEXT"), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
