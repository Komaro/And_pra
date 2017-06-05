package la.kaka.lifecare.Exercise;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import la.kaka.lifecare.DB.DB_Helper;
import la.kaka.lifecare.R;

/**
 * Created by H on 2017-06-04.
 */

public class ExeReg extends AppCompatActivity {

    //date & time setting
    DatePicker date_select;
    TimePicker time_select;
    EditText exe_input;

    Button create_buttton;

    //DB
    DB_Helper helper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exereg);

        date_select = (DatePicker)findViewById(R.id.date_select);
        time_select = (TimePicker)findViewById(R.id.time_select);
        exe_input = (EditText)findViewById(R.id.exe_input);
        create_buttton = (Button)findViewById(R.id.create_button);

        Intent in = getIntent();

        if(in.getExtras().getInt("year") != 0) {
            date_select.updateDate(in.getExtras().getInt("year"),in.getExtras().getInt("month"),in.getExtras().getInt("day"));
        }

        create_buttton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                helper = new DB_Helper(getApplicationContext());

                String date = date_select.getYear() + "-" +
                              (date_select.getMonth() + 1) + "-" +
                               date_select.getDayOfMonth();
                String time;

                if (Build.VERSION.SDK_INT >= 23) {
                    time = Integer.toString(time_select.getHour()) + ":" +
                            Integer.toString(time_select.getMinute()) ;
                }
                else {
                    time = Integer.toString(time_select.getCurrentHour()) + ":" +
                            Integer.toString(time_select.getCurrentMinute()) ;
                }

                try
                {
                    db = helper.getWritableDatabase();

                    db.execSQL("INSERT INTO ExerciseTime(date, exe, time, switch) VALUES('" + date.toString() + "', '"
                                                                   + exe_input.getText().toString() + "', '"
                                                                   + time.toString() + "', 1);");


                    //db.execSQL("INSERT INTO ExerciseTime VALUES('2017-05-05','11:12','test', 1);");

                    Toast.makeText(getApplicationContext(), date + "\n" + time + "에 등록되었습니다.", Toast.LENGTH_SHORT).show();

                    Log.i("db_msg", "DATA BASE : CREATE SUCCESS");


                    //Sending Close Activity Code to ExeSch
                    Intent in = new Intent();

                    in.putExtra("SEND_CLOSE", 1);
                    setResult(RESULT_OK, in);

                    finish();
                }
                catch (SQLiteException ex)
                {
                    Log.i("db_msg", "DATA BASE : " + ex.getMessage().toString());
                    Log.i("db_msg", "DATA BASE : " + date + " " + exe_input.getText().toString() + " " + time);
                }
            }
        });
    }
}
