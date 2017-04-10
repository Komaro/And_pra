package la.kaka.calendar_color;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class MainActivity extends Activity {

    private TimePicker tPicker;
    private CalendarView calView;
    private EditText text;
    private EditText text1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tPicker = (TimePicker) findViewById(R.id.timePicker2);
        calView = (CalendarView) findViewById(R.id.calendarView2);
        text = (EditText) findViewById(R.id.editText1);
        text1 = (EditText) findViewById(R.id.editText2);

        registerForContextMenu(text);
        registerForContextMenu(text1);

        calView.setOnDateChangeListener(new OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year,
                                            int month, int dayOfMonth) {
                // TODO Auto-generated method stub

                text.setText(" " + year + " : " + month + " : " + dayOfMonth);
            }
        });
        tPicker.setOnTimeChangedListener(new OnTimeChangedListener() {
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                text1.setText(" " + hourOfDay + " : " + minute);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("색상");
        menu.add(0,1,0, "빨간색");
        menu.add(0,2,0, "녹색");
        menu.add(0,3,0, "파란색");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case 1 :

                text.setTextColor(Color.RED);
                text1.setTextColor(Color.RED);
                return true;

            case 2:

                text.setTextColor(Color.GREEN);
                text1.setTextColor(Color.GREEN);
                return true;

            case 3:

                text.setTextColor(Color.BLUE);
                text1.setTextColor(Color.BLUE);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
}
