package la.kaka.tempconvertor;

import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText input_temp;
    RadioButton radio_ct, radio_ft;
    Boolean ct, ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input_temp = (EditText)findViewById(R.id.input_temp);
        radio_ct = (RadioButton)findViewById(R.id.radio_ct);
        radio_ft = (RadioButton)findViewById(R.id.radio_ft);
    }


    public void onClicked(View view)
    {
        String input_string;
        String check_string;
        Float input_number;

        switch(view.getId())
        {
            case R.id.button_convert :
            {
                if(input_temp.getText().length() == 0)
                {
                    Toast.makeText(this, "정확한 값을 입력하세요.", Toast.LENGTH_LONG).show();

                    return;
                }

                input_string = input_temp.getText().toString();

                check_string = input_string.substring(input_string.length()-1);

                String temp = input_string.substring(0, input_string.length()-1);

                input_number = Float.parseFloat(temp.toString());

                if(check_string.equals("c"))
                {
                    input_temp.setText(String.valueOf(convertCelsiusToFahrenheit(input_number)));
                    radio_ft.setChecked(true);
                    radio_ct.setChecked(false);
                }
                else if(check_string.equals("f"))
                {
                    input_temp.setText(String.valueOf(convertFahrenheitToCelsius(input_number)));
                    radio_ct.setChecked(true);
                    radio_ft.setChecked(false);
                }
                else
                {
                    Toast.makeText(this, "정확한 단위를 입력하세요.", Toast.LENGTH_SHORT).show();
                }

                ct = false;
                ft = false;
            }
        }
    }


    private float convertFahrenheitToCelsius(float fahrenheit)
    {
        return ((fahrenheit - 32) * 5 / 9);
    }

    private float convertCelsiusToFahrenheit(float celsius)
    {
        return ((celsius * 9) / 5) + 32;
    }
}
