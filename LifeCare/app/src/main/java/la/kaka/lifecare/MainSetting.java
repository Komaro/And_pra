package la.kaka.lifecare;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ResourceBundle;

public class MainSetting extends AppCompatActivity {

    Messenger control_messenger = null;
    boolean bound;

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
        }
    }
}
