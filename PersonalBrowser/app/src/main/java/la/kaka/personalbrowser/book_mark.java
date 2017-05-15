package la.kaka.personalbrowser;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by Administrator on 2017-05-10.
 */

public class book_mark extends AppCompatActivity implements View.OnClickListener{

    Button exit_button, view_button;
    Cursor cursor;
    DB_Helper helper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark);

        exit_button = (Button)findViewById(R.id.exit_button);
        view_button = (Button)findViewById(R.id.view_button);

        exit_button.setOnClickListener(this);
        view_button.setOnClickListener(this);

        cursor = db.rawQuery("SELECT * FROM BookMark", null);

        startManagingCursor(cursor);

        String[] from = {"name", "url", "date"};
        int[] to = { };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2,cursor, from,to);

        ListView view = (ListView)findViewById(R.id.book_makr_list);
        view.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.view_button)
        {

        }
        else
        {

        }
    }
}
