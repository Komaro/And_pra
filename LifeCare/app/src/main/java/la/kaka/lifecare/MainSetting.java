/****************************************
    //Control
    static final int CONTROL_EX = 1;

    //Action
    static final int EX_ON = 11;
    static final int EX_OFF = 12;

    //Log tag
        binding_msg
        service_msg
        exe_msg
        cal_msg
 *****************************************/

package la.kaka.lifecare;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ApplicationErrorReport;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import la.kaka.lifecare.Service.ControlService;
import la.kaka.lifecare.Service.ExerciseService;

public class MainSetting extends AppCompatActivity implements View.OnClickListener{

    // service
    Messenger control_messenger = null;
    Message send_message;
    boolean bound;

    // view
    Switch exe_switch;

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
        exe_switch.setOnClickListener(this);

        // ExerciseService running check
        if(checkService("la.kaka.lifecare.Service.ExerciseService"))
        {
            Log.i("exe_msg", "EXE SERVICE : EXE SERVICE IS RUNNING");
            exe_switch.setChecked(true);
        }
        else
        {
            Log.i("exe_msg", "EXE SERVICE : EXE SERVICE IS NOT RUNNING");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // create ControlService class and Binding
        bindService(new Intent(this, ControlService.class), control_connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        //Unbinding
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

        if(temp.isChecked())
        {
            send_message = Message.obtain(null, ControlService.CONTROL_EX, ControlService.EX_ON, 0);
        }
        else if(!temp.isChecked())
        {
            send_message = Message.obtain(null, ControlService.CONTROL_EX, ControlService.EX_OFF, 0);
        }

        try {
            control_messenger.send(send_message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    //Service check
    public boolean checkService(String service_name)
    {
        ActivityManager manager = (ActivityManager)this.getSystemService(Activity.ACTIVITY_SERVICE);

        for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if(service_name.equals(service.service.getClassName()))
            {
                return true;
            }
        }

        return false;
    }

    //Option menu

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
}
