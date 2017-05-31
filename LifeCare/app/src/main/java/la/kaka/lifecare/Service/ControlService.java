package la.kaka.lifecare.Service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2017-05-24.
 */

public class ControlService extends Service {

    //Control
    public static final int CONTROL_EX = 1;

    //Action
    public static final int EX_ON = 11;
    public static final int EX_OFF = 12;

    final Messenger mMessenger = new Messenger(new ControlHandler());

    //Receive Handler
    Handler exchange_handler = new Handler()    {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    //Service
    Intent in;
    Messenger messenger = new Messenger(exchange_handler);

    class ControlHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what)
            {
                case CONTROL_EX:

                    in = new Intent(getApplicationContext(), ExerciseService.class);

                    if(msg.arg1 == EX_ON)
                    {
                        startService(in);
                    }
                    else
                    {
                        stopService(in);
                    }

                    break;

            }

            super.handleMessage(msg);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Log.i("binding_msg", "Binding : bind");

        return mMessenger.getBinder();
    }

    @Override
    public void onDestroy() {
        Log.i("test", "destroy");
        super.onDestroy();
    }
}
