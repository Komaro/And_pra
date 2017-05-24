package la.kaka.lifecare;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2017-05-24.
 */



public class ControlService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}
