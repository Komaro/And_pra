package la.kaka.lifecare.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import la.kaka.lifecare.DB.DB_Helper;

/**
 * Created by H on 2017-06-05.
 */

public class WalkingService extends Service implements SensorEventListener{

    // DB
    DB_Helper helper;
    SQLiteDatabase db;
    Cursor cursor;

    // Sensor
    private int count;

    private long lastTime;
    private float speed;
    private float lastx;
    private float lasty;
    private float lastz;

    private float x, y, z;
    private static final int SHAKE_THRESHOLD = 400;

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;

    // Date
    Date time;
    String date;

    // Broadcasting
    BroadcastReceiver mReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i("msg", "START SERVICE");

        if(accelerometerSensor != null)
        {
            sensorManager.registerListener(this, accelerometerSensor, sensorManager.SENSOR_DELAY_GAME);
        }

        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            long currrentTime = System.currentTimeMillis();
            long gabOfTime = (currrentTime - lastTime);

            if(gabOfTime > 100)
            {
                lastTime = currrentTime;

                x = event.values[0];
                y = event.values[1];
                z = event.values[2];

                speed = Math.abs(x + y + z - lastx - lasty - lastz) / gabOfTime * 10000;

                if(speed > SHAKE_THRESHOLD)
                {
                    count++;

                    String msg = count / 2 + "";

                    Intent in = new Intent("la.kaka.lifecare.WORK_BROAD_CAST");
                    in.putExtra("stepService", msg);
                    sendBroadcast(in);
                }

                lastx = event.values[0];
                lasty = event.values[1];
                lastz = event.values[2];
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        //Test
        //count = 0;

        //Today Count Load
        helper = new DB_Helper(this);
        db = helper.getReadableDatabase();

        time = new Date();
        SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy-MM-dd");
        date = sdf_date.format(time);

        try {
            db.execSQL("INSERT INTO Walking(date, count) VALUES('" + date + "', 0)");
            count = 0;
        }
        catch(SQLiteException ex)
        {
            cursor = db.rawQuery("SELECT * FROM Walking WHERE date = '" + date + "'", null);
            cursor.moveToNext();

            count = cursor.getInt(1);

            Log.i("walking_msg","WALKING : CREATE - " + ex.getMessage() + " COUNT : " + count);
        }

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //Broadcasting receiver
        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction("la.kaka.lifecare.WORK_RESET_BROAD_CAST");

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getExtras().getBoolean("reset"))
                {
                    count = 0;
                }
            }
        };

        registerReceiver(mReceiver, intentfilter);

        Log.i("walking_msg", "WALKING : WALKING SERVICE START");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i("msg", "END SERVICE");
        if(sensorManager != null)
        {
            sensorManager.unregisterListener(this);
        }

        try
        {
            db.execSQL("UPDATE walking SET count = " + count + "  WHERE date = '" + date + "'");
            count = 0;
        }
        catch (SQLiteException ex)
        {
            Log.i("walking_msg","WALKING : DESTROY - " + ex.getMessage());
        }

        Log.i("walking_msg", "WALKING : WALKING SERVICE STOP");
    }
}