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

//This Service Is Meaningless, Need Fix
public class ControlService extends Service {

    //Control
    public static final int CONTROL_EX = 1;
    public static final int CONTROL_WO = 2;

    //Action
    //Exercise
    public static final int EX_ON = 11;
    public static final int EX_OFF = 12;
    //Working
    public static final int WORKING_ON = 21;
    public static final int WORKING_OFF = 22;

    final Messenger mMessenger = new Messenger(new ControlHandler());

    //Service
    Intent in;

    // Control Service
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
                    else if(msg.arg1 == EX_OFF)
                    {
                        stopService(in);
                    }

                    break;

                case CONTROL_WO:

                    in = new Intent(getApplicationContext(), WalkingService.class);

                    if(msg.arg1 == WORKING_ON)
                    {
                        startService(in);
                    }
                    else if(msg.arg1 == WORKING_OFF)
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
