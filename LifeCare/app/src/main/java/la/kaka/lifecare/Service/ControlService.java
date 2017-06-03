package la.kaka.lifecare.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.util.Log;

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

        Log.i("binding_msg", "BINDING : BIND");

        return mMessenger.getBinder();
    }
}
