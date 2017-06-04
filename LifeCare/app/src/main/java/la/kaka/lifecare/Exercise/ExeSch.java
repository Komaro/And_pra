package la.kaka.lifecare.Exercise;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ListView;
import la.kaka.lifecare.List.list_item;
import la.kaka.lifecare.List.listview_adpter;
import java.util.ArrayList;

import la.kaka.lifecare.DB.DB_Helper;
import la.kaka.lifecare.R;

/**
 * Created by Kaka on 2017-06-04.
 */

public class ExeSch extends AppCompatActivity implements CalendarView.OnDateChangeListener, View.OnClickListener{

    //DB
    DB_Helper helper;
    SQLiteDatabase db;
    ListView exe_list;
    Cursor cursor;
    listview_adpter adpter;
    ArrayList<list_item> items = new ArrayList<list_item>();

    //Sch change
    CalendarView exe_cal;
    int year = 0, month = 0, day = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exesch);

        exe_list = (ListView)findViewById(R.id.exe_list);
        exe_cal = (CalendarView)findViewById(R.id.exe_cal);

        helper = new DB_Helper(this);
        db = helper.getReadableDatabase();

        cursor = db.rawQuery("SELECT * FROM ExerciseTime", null);

        while(cursor.moveToNext())
        {
            String date = cursor.getString(1);
            String exe = cursor.getString(2);
            String time = cursor.getString(3);

            items.add(new list_item(date, exe, time));
        }

        adpter = new listview_adpter(this, items);
        exe_list.setAdapter(adpter);

        exe_cal.setOnDateChangeListener(this);
        exe_cal.setOnClickListener(this);
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.day = dayOfMonth;

        Log.i("cal_msg", "CALENDER : " + this.year + " " + this.month + " " + this.day);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exe_menu, menu);
        Log.i("menu_msg", "MENU : EXE MENU CREATE");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.reg_button)
        {
            Intent in = new Intent(getApplicationContext(), ExeReg.class);
            in.putExtra("year", year);
            in.putExtra("month", month);
            in.putExtra("day", day);
            startActivity(in);
        }
        else
        {

        }

        return super.onOptionsItemSelected(item);
    }
}
