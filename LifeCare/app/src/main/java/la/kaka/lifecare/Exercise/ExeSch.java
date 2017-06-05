package la.kaka.lifecare.Exercise;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Switch;

import la.kaka.lifecare.List.list_item;
import la.kaka.lifecare.List.listview_adpter;
import java.util.ArrayList;

import la.kaka.lifecare.DB.DB_Helper;
import la.kaka.lifecare.R;

/**
 * Created by Kaka on 2017-06-04.
 */

public class ExeSch extends AppCompatActivity implements CalendarView.OnDateChangeListener, AdapterView.OnItemClickListener, View.OnClickListener{

    //DB
    DB_Helper helper;
    SQLiteDatabase db;
    ListView exe_list;
    Cursor cursor;
    listview_adpter adpter;
    ArrayList<list_item> items = new ArrayList<list_item>();

    //Sch change
    CalendarView exe_cal;
    Switch check_switch;
    int year = 0, month = 0, day = 0;
    int index;

    //Intent Result Code
    static final public int CLOSE_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exesch);

        exe_list = (ListView)findViewById(R.id.exe_list);
        exe_cal = (CalendarView)findViewById(R.id.exe_cal);
        check_switch = (Switch)findViewById(R.id.check_switch);

        make_listview();

        exe_list.setOnItemClickListener(this);
        exe_cal.setOnDateChangeListener(this);
        check_switch.setOnClickListener(this);

    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.day = dayOfMonth;

        Log.i("cal_msg", "CALENDER : " + this.year + " " + this.month + " " + this.day);

        String select_date = year + "-" + (month + 1) + "-" + day;

        for(list_item cursor : items)
        {
            if(cursor.date.equals(select_date))
            {
                int index = items.indexOf(cursor);
                this.index = index;

                exe_list.setSelection(index);

                activation_check(index);

                return;
            }
        }

        check_switch.setChecked(false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String[] date = ((String) parent.getAdapter().getItem(position)).split("-");

        this.year = Integer.parseInt(date[0]);
        this.month = Integer.parseInt(date[1]) - 1;
        this.day = Integer.parseInt(date[2]);

        index = position;

        Log.i("cal_msg","CALENDER : LIST SELECT " + Integer.parseInt(date[0]) + " " + Integer.parseInt(date[1]) + " " + Integer.parseInt(date[2]) + " " + index);

        activation_check(index);
    }

    @Override
    public void onClick(View v) {

        Switch temp = (Switch)v;

        if(temp.isChecked())
        {
            //Activation on
            items.get(index).activation_check = 1;
            Log.i("db_msg","DATA BASE : ACTIVATION ON");
        }
        else
        {
            //Activation off
            items.get(index).activation_check = 0;
            Log.i("db_msg","DATA BASE : ACTIVATION OFF");

        }

        db.execSQL("UPDATE ExerciseTime SET switch = '" + items.get(index).activation_check + "' WHERE date = '" + items.get(index).date +  "'");
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
            startActivityForResult(in, CLOSE_CODE);
        }
        else
        {
            try {
                String del_date = year + "-" + (month + 1) + "-" + day;
                db.execSQL("DELETE FROM ExerciseTime WHERE date = '" + del_date + "'");
                Log.i("db_msg", "DATE BASE : DELETE " + del_date);

                items.remove(this.index);
                list_reset();
            }
            catch (SQLiteException ex)
            {
                Log.i("db_msg", "DATA BASE : " + ex.getMessage().toString());
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void list_reset()
    {
        exe_list.clearChoices();
        adpter.notifyDataSetChanged();
    }

    private void activation_check(int index)
    {
        if(items.get(index).activation_check == 1)
        {
            check_switch.setChecked(true);
        }
        else
        {
            check_switch.setChecked(false);
        }
    }

    private void make_listview()
    {
        items.clear();

        helper = new DB_Helper(this);
        db = helper.getReadableDatabase();

        cursor = db.rawQuery("SELECT * FROM ExerciseTime", null);

        while(cursor.moveToNext())
        {
            String date = cursor.getString(0);
            String exe = cursor.getString(1);
            String time = cursor.getString(2);
            int activation_check = cursor.getInt(3);

            items.add(new list_item(date, exe, time, activation_check));
        }

        adpter = new listview_adpter(this, items);

        exe_list.setAdapter(adpter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CLOSE_CODE) {
            if(resultCode == RESULT_OK)
            {
                make_listview();
                list_reset();
            }
        }
    }
}
