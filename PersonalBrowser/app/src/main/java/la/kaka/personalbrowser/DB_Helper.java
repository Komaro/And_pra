package la.kaka.personalbrowser;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017-05-15.
 */

public class DB_Helper extends SQLiteOpenHelper {

    private static final String DB_NAME = "BOOK_MARK";
    private static final int DB_VERSION = 2;

    public DB_Helper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE BookMark (name VARCHAR2(30), url VARCHAR2(30), date VARCHAR2(20));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS BookMark");
        onCreate(db);
    }
}
