package la.kaka.lifecare.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import la.kaka.lifecare.DB.DB_Helper;
import la.kaka.lifecare.MainSetting;
import la.kaka.lifecare.R;

/**
 * Created by Kaka on 2017-06-03.
 */

public class ExerciseService extends Service {

    //Delay (defualt - 61000 ~ 65000)
    public static final int delay = 5000;

    //DB
    DB_Helper helper;
    SQLiteDatabase db;
    Cursor cursor;

    //Alarm
    MediaPlayer player;
    int count = 0;
    //Alarm Stop
    BroadcastReceiver mReceiver;

    //Timer
    Date time;
    SimpleDateFormat sdf_date;
    SimpleDateFormat sdf_time;
    String date;

    Runnable work = new Runnable() {
        @Override
        public void run() {

            Log.i("exe_msg", "EXE SERVICE : LOOP");

            cursor = db.rawQuery("SELECT * FROM ExerciseTime", null);

            Log.i("alarm_msg", "ALARM : " + date + " " + sdf_time.format(time));

            while(cursor.moveToNext())
            {
                //Check Activation
                if(cursor.getInt(3) == 1)
                {
                    Log.i("alarm_msg", "ALARM : CHECK ACTIVATION " +  cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2));

                    //Check Date
                    if(date.equals(cursor.getString(0)))
                    {
                        Log.i("alarm_msg", "ALARM : CHECK DATE " + cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2));

                        String[] time_now = sdf_time.format(time).split(":");
                        String[] time_cursor = cursor.getString(2).split(":");

                        int check_time = (60 * (Integer.parseInt(time_cursor[0]) - Integer.parseInt(time_now[0]))) + (Integer.parseInt(time_cursor[1]) - Integer.parseInt(time_now[1]));

                        //Log.i("alarm_msg", "ALARM : CHECK TIME " + (Integer.parseInt(time_cursor[0]) - Integer.parseInt(time_now[0])));
                        //Log.i("alarm_msg", "ALARM : CHECK TIME " + (Integer.parseInt(time_cursor[1]) - Integer.parseInt(time_now[1])));
                        Log.i("alarm_msg", "ALARM : CHECK TIME " + check_time);

                        if(check_time < 10 && check_time > -5) {

                            Intent in = new Intent(getApplicationContext(), MainSetting.class);
                            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            in.putExtra("alarm_dialog", true);

                            //Check Time
                            if (check_time == 10)
                            {
                                alarm_play((float) 0.5);
                                in.putExtra("short_gap",false);
                                startActivity(in);
                            }
                            else if (check_time == 5)
                            {
                                alarm_play((float) 0.75);
                                in.putExtra("short_gap",false);
                                startActivity(in);
                            }
                            else if (check_time > -5 && check_time < 1)
                            {
                                alarm_play((float) 1);
                                in.putExtra("short_gap",true);
                                startActivity(in);
                            }
                        }
                    }
                }
            }

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

        helper = new DB_Helper(this);
        db = helper.getReadableDatabase();

        Log.i("exe_msg", "EXE SERVICE : SERVICE START");

        player = new MediaPlayer();
        player = MediaPlayer.create(getApplicationContext(), R.raw.default_alarm);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(count++ == 3)
                {
                    count = 0;
                    player.stop();
                }
            }
        });

        //Broadcasting receiver
        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction("la.kaka.lifecare.ALARM_STOP_BROAD_CAST");

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getExtras().getBoolean("stopping"))
                {
                    player.stop();
                    try {
                        player.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    count = 0;

                    if(intent.getExtras().getBoolean("alarm_close"))
                    {
                        db.execSQL("UPDATE ExerciseTime SET switch = 0 WHERE date = '" + date + "'");
                        Log.i("exe_msg", "EXE SERVICE : ALARM COLSE");
                    }
                }
            }
        };

        registerReceiver(mReceiver, intentfilter);
    }

    @Override
    public void onDestroy() {
        Log.i("exe_msg", "EXE SERVICE : SERVICE STOP");
        handler.removeMessages(0);
        player.stop();
        player.release();
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        time = new Date();
        sdf_date = new SimpleDateFormat("yyyy-M-d");
        sdf_time = new SimpleDateFormat("HH:mm");
        date = sdf_date.format(time);

        handler.postDelayed(work, delay);
        return super.onStartCommand(intent, flags, startId);
    }

    private void alarm_play(float volume)
    {
        //bgm looping
        player.setLooping(true);

        //bgm volume (0 ~ 1)
        player.setVolume(volume, volume);

        player.start();
    }
}