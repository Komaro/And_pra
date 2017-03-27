package la.kaka.mobliecalc;

import android.icu.text.DateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText input_box;
    double num_save, num_oper;
    String operation_symbol;
    Boolean oper_flag = false;
    Boolean exc_flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input_box = (EditText)findViewById(R.id.input_box);
    }

    public void onClick_num(View target)
    {
        Button temp = (Button)target;

        if(oper_flag)
        {
            input_box.setText("");
            oper_flag = false;
        }

        if(target.getId() == R.id.button_dot)
        {
            if(input_box.getText().toString().contains(".") || input_box.getText().toString().equals(""))
            {
                return;
            }
        }

        input_box.setText(input_box.getText().toString().concat(temp.getText().toString()));
    }

    public void onClick_oper(View target)
    {
        switch(target.getId())
        {
            case R.id.button_plu:

                setting("+");
                break;

            case R.id.button_min:

                setting("-");
                break;

            case R.id.button_mul:

                setting("*");
                break;

            case R.id.button_div:

                setting("/");
                break;

            case R.id.button_del:

                reset();
                break;

            case R.id.button_equal:

                operation();
                break;
        }
    }

    public void operation()
    {
        if(exc_flag)
        {
            num_oper = Double.parseDouble(input_box.getText().toString());
        }

        switch (operation_symbol) {
            case "+":

                input_box.setText(Double.toString(num_save + num_oper));
                break;

            case "-":

                input_box.setText(Double.toString(num_save - num_oper));
                break;

            case "*":

                input_box.setText(Double.toString(num_save * num_oper));
                break;

            case "/":

                input_box.setText(Double.toString(num_save / num_oper));
                break;
        }

        num_save = Double.parseDouble(input_box.getText().toString());
        num_oper = 0;

        exc_flag = false;
    }

    public void setting(String oper)
    {
        if(num_save != 0 && exc_flag)
        {
            operation();

            Toast.makeText(this, "operation", Toast.LENGTH_SHORT).show();

            oper_flag = true;
            operation_symbol = oper;
            exc_flag = true;

            return;
        }

        operation_symbol = oper;

        num_save = Double.parseDouble(input_box.getText().toString());
        oper_flag = true;
        exc_flag = true;
    }

    public void reset()
    {
        input_box.setText("");
        num_save = 0;
        num_oper = 0;
        operation_symbol = "";
        oper_flag = false;
        exc_flag = false;
    }
}
