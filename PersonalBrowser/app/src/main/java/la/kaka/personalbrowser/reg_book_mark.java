package la.kaka.personalbrowser;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Administrator on 2017-05-10.
 */

public class reg_book_mark extends AppCompatActivity implements View.OnClickListener{

    Button exit_button, save_button;
    EditText input_name, input_url, input_date;
    DB_Helper helper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_book_mark);

        exit_button = (Button)findViewById(R.id.exit_button);
        save_button = (Button)findViewById(R.id.save_button);

        exit_button.setOnClickListener(this);
        save_button.setOnClickListener(this);

        input_name = (EditText)findViewById(R.id.input_name);
        input_url = (EditText)findViewById(R.id.input_url);
        input_date = (EditText)findViewById(R.id.input_date);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.save_button)
        {
            helper = new DB_Helper(this);

            try
            {
                db = helper.getWritableDatabase();

                db.execSQL("INSERT INTO BookMark VALUES('"  + input_name.getText().toString() +
                        "', '" + input_url.getText().toString() +
                        "', '" + input_date.getText().toString() + "');");

                Toast.makeText(this, "등록되었습니다.", Toast.LENGTH_SHORT).show();
            }
            catch (SQLiteException ex)
            {
                Log.i("_db", "DB_ERROR : " + ex.getMessage().toString());
            }
        }
        else
        {
            finish();
        }
    }
}
