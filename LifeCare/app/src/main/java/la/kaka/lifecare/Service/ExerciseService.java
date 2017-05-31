package la.kaka.lifecare.Service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import la.kaka.lifecare.DB.DB_Helper;

/**
 * Created by Administrator on 2017-05-24.
 */

public class ExerciseService extends IntentService {

    //DB
    DB_Helper helper;
    SQLiteDatabase db;

    //Timer

    Timer timer;
    TimerTask task;


    @Override
    public void onCreate() {
        super.onCreate();

        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                Log.i("exe_msg", "EXE SERVICE : loop");
            }
        };
    }

    public ExerciseService() {
        super("ExerciseService");
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        Log.i("exe_msg", "EXE SERVICE : SERVICE START");
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        Log.i("exe_msg", "EXE SERVICE : SERVICE STOP");
        timer.cancel();
        super.onDestroy();
    }

    @Nullable
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        timer.schedule(task, 5000);
    }
}
