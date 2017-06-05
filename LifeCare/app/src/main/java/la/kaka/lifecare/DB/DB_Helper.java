package la.kaka.lifecare.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017-05-15.
 */

public class DB_Helper extends SQLiteOpenHelper {

    private static final String DB_NAME = "LIFE CARE";
    private static final int DB_VERSION = 2;

    public DB_Helper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ExerciseTime (" +
                   "date TEXT PRIMARY KEY, " +
                   "exe TEXT, " +
                   "time TEXT, " +
                   "switch INTEGER);");

        db.execSQL("CREATE TABLE Walking (date TEXT PRIMARY KEY, " +
                   "count INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ExerciseTime");
        db.execSQL("DROP TABLE IF EXISTS Working");
        onCreate(db);
    }
}
