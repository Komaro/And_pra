/****************************************
    //Control
    static final int CONTROL_EX = 1;

    //Action
    static final int EX_ON = 11;
    static final int EX_OFF = 12;

    //Log tag
        binding_msg
        service_msg
 *****************************************/

package la.kaka.lifecare;

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
import android.widget.CompoundButton;
import android.widget.Switch;

import la.kaka.lifecare.Service.ControlService;

public class MainSetting extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    //service
    Messenger control_messenger = null;
    Message send_message;
    boolean bound;

    //view
    Switch exe_switch;

    // Service Binding
    private ServiceConnection control_connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            control_messenger = new Messenger(service);
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            control_messenger = null;
            bound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_setting);

        exe_switch = (Switch)findViewById(R.id.exe_switch);

        exe_switch.setOnCheckedChangeListener(this);

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

            Log.i("binding_msg", "Binding : unbind");
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        //synchronization check
        if(!bound)
        {
            exe_switch.setChecked(bound);
            return;
        }

        if(isChecked)
        {
            send_message = Message.obtain(null, ControlService.CONTROL_EX, ControlService.EX_ON, 0);
        }
        else if(!isChecked)
        {
            send_message = Message.obtain(null, ControlService.CONTROL_EX, ControlService.EX_OFF, 0);
        }

        try {
            control_messenger.send(send_message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
