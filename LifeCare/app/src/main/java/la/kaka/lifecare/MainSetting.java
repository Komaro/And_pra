/****************************************
    //Control
        CONTROL_EX = 1;
        CONTROL_WO = 2;

    //Action
        EX_ON = 11;
        EX_OFF = 12;
        WORKING_ON = 21;
        WORKING_OFF = 22;

    //Log tag
        binding_msg
        service_msg
        exe_msg
        cal_msg
        db_msg
        alarm_msg
        working_msg
 *****************************************/

package la.kaka.lifecare;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import la.kaka.lifecare.DB.DB_Helper;
import la.kaka.lifecare.Exercise.ExeSch;
import la.kaka.lifecare.Service.ControlService;

public class MainSetting extends AppCompatActivity implements View.OnClickListener{

    //DB Create
    DB_Helper helper = new DB_Helper(this);
    SQLiteDatabase db;

    //Date Print
    Date date;

    // service
    Messenger control_messenger = null;
    Message send_message;
    boolean bound;

    // view
    Switch exe_switch, work_switch;
    Button reset_button;
    TextView date_text;

    // Walking & Exercise Service Broadcasting
    BroadcastReceiver mReceiver;
    TextView walk_view, length_view;
    Intent in;

    // Service Binding
    private ServiceConnection control_connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            control_messenger = new Messenger(service);
            bound = true;
            Log.i("binding_msg","BINDING : BOUND IS TRUE");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            control_messenger = null;
            bound = false;
            Log.i("binding_msg","BINDING : BOUND IS FALSE");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_setting);

        exe_switch = (Switch)findViewById(R.id.exe_switch);
        work_switch = (Switch)findViewById(R.id.walk_switch);
        walk_view = (TextView)findViewById(R.id.walk_view);
        length_view = (TextView)findViewById(R.id.length_view);
        reset_button = (Button)findViewById(R.id.reset_button);
        date_text = (TextView)findViewById(R.id.date_text);

        exe_switch.setOnClickListener(this);
        work_switch.setOnClickListener(this);
        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walking_reset_broadcasting();
            }
        });

        // Service running check
        checkService("la.kaka.lifecare.Service.ExerciseService", exe_switch);
        checkService("la.kaka.lifecare.Service.WalkingService", work_switch);

        // Working Service Broadcasting receiver
        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction("la.kaka.lifecare.WORK_BROAD_CAST");

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(!intent.getExtras().getString("stepService").equals(""))
                {
                    String catch_step = intent.getExtras().getString("stepService");

                    Log.i("step", "STEP : " + catch_step);
                    walk_view.setText(catch_step);

                    DecimalFormat df = new DecimalFormat("#,###.##");
                    double catch_length = Float.parseFloat(catch_step) * 0.65;
                    length_view.setText(df.format(catch_length));
                }
            }
        };

        registerReceiver(mReceiver, intentfilter);

        // Alarm Dialog
        try {
            this.in = getIntent();
            if (this.in != null) {
                if (this.in.getExtras().getBoolean("alarm_dialog"))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("알람을 종료하시겠습니까?");
                    builder.setPositiveButton("종료", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if(in.getExtras().getBoolean("short_gap"))
                            {
                                alarm_stop_broadcasting(true);
                            }
                            else
                            {
                                alarm_stop_broadcasting(false);
                            }

                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        }
        catch(Exception ex)
        {
            Log.i("alarm_msg", "ALARM : ALARM DIALOG EXCEPTION - " +  ex.getMessage().toString());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // create ControlService class and Binding
        bindService(new Intent(this, ControlService.class), control_connection, Context.BIND_AUTO_CREATE);

        //Date Print
        date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");

        date_text.setText(sdf.format(date));
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Unbinding
        if(bound)
        {
            unbindService(control_connection);
            bound = false;

            Log.i("binding_msg", "BINDING : UNBIND");
        }
    }

    @Override
    public void onClick(View v) {

        Switch temp = (Switch)v;

        // ControlService synchronizing check
        if(!bound)
        {
            exe_switch.setChecked(bound);
            return;
        }

        switch(temp.getId())
        {
            case R.id.exe_switch :

                if(temp.isChecked())
                {
                    send_message = Message.obtain(null, ControlService.CONTROL_EX, ControlService.EX_ON, 0);
                }
                else if(!temp.isChecked())
                {
                    send_message = Message.obtain(null, ControlService.CONTROL_EX, ControlService.EX_OFF, 0);
                }

                break;

            case R.id.walk_switch :

                if(temp.isChecked())
                {
                    send_message = Message.obtain(null, ControlService.CONTROL_WO, ControlService.WORKING_ON, 0);
                }
                else if(!temp.isChecked())
                {
                    send_message = Message.obtain(null, ControlService.CONTROL_WO, ControlService.WORKING_OFF, 0);
                }

                break;
        }

        try {
            control_messenger.send(send_message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // Service check
    public void checkService(String service_name, Switch target_switch)
    {
        ActivityManager manager = (ActivityManager)this.getSystemService(Activity.ACTIVITY_SERVICE);

        for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if(service_name.equals(service.service.getClassName()))
            {
                Log.i("exe_msg", "EXE SERVICE : EXE SERVICE IS RUNNING");
                target_switch.setChecked(true);
                return;
            }
        }

        target_switch.setChecked(false);
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        Log.i("menu_msg", "MENU : MAIN MENU CREATE");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent in = null;

        switch (item.getItemId())
        {
            case R.id.exe_menu:

                in = new Intent(this, ExeSch.class);
                break;
        }

        startActivity(in);
        return super.onOptionsItemSelected(item);
    }

    // Alarm Stop Broadcasting
    private void alarm_stop_broadcasting(boolean short_check)
    {
        Intent in = new Intent("la.kaka.lifecare.ALARM_STOP_BROAD_CAST");
        in.putExtra("stopping", true);
        if(short_check)
        {
            in.putExtra("alarm_close", true);
        }
        else
        {
            in.putExtra("alarm_close", false);
        }
        sendBroadcast(in);
    }

    // Walking Service Reset Broadcasting
    private void walking_reset_broadcasting()
    {
        Intent in = new Intent("la.kaka.lifecare.WORK_RESET_BROAD_CAST");
        in.putExtra("reset", true);
        sendBroadcast(in);

        walk_view.setText("0");
        length_view.setText("0.0");

        helper = new DB_Helper(this);
        db = helper.getReadableDatabase();

        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            db.execSQL("UPDATE walking SET count = 0 WHERE date = '" + sdf.format(date) + "'");
            Log.i("walking_msg","WALKING : RESET COUNT " + sdf.format(date));
        }
        catch (SQLiteException ex)
        {
            Log.i("walking_msg","WALKING : RESET - " + ex.getMessage());
        }
    }
}