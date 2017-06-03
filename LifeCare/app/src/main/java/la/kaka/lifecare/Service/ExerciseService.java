package la.kaka.lifecare.Service;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import la.kaka.lifecare.DB.DB_Helper;

/**
 * Created by Kaka on 2017-06-03.
 */

public class ExerciseService extends Service {

    //Delay
    public static final int delay = 3000;

    //DB
    DB_Helper helper;
    SQLiteDatabase db;

    //Timer

    Runnable work = new Runnable() {
        @Override
        public void run() {
            Log.i("exe_msg", "EXE SERVICE : LOOP");
            handler.postDelayed(work, delay);
        }
    };
    Handler handler = new Handler();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("exe_msg", "EXE SERVICE : SERVICE START");
    }

    @Override
    public void onDestroy() {
        Log.i("exe_msg", "EXE SERVICE : SERVICE STOP");
        handler.removeMessages(0);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        handler.postDelayed(work, delay);
        return super.onStartCommand(intent, flags, startId);
    }
}
