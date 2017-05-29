package la.kaka.lifecare.Service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2017-05-24.
 */

public class ExerciseService extends IntentService {

    public ExerciseService(String name) {
        super(name);
    }

    @Nullable
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {


    }
}
