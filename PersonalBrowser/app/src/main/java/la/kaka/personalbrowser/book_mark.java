package la.kaka.personalbrowser;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-05-10.
 */

public class book_mark extends AppCompatActivity implements View.OnClickListener{

    Button exit_button, view_button;
    Cursor cursor;
    DB_Helper helper;
    SQLiteDatabase db;
    listview_adpter adpter;
    ArrayList<list_item> items = new ArrayList<>();
    ListView book_mark_list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark);

        exit_button = (Button)findViewById(R.id.exit_button);
        view_button = (Button)findViewById(R.id.view_button);

        exit_button.setOnClickListener(this);
        view_button.setOnClickListener(this);

        helper = new DB_Helper(this);
        db = helper.getReadableDatabase();

        cursor = db.rawQuery("SELECT * FROM BookMark", null);

        while(cursor.moveToNext())
        {
            String name = cursor.getString(0);
            String url = cursor.getString(1);
            String date = cursor.getString(2);

            items.add(new list_item(name, url, date));
        }

        adpter = new listview_adpter(this, items);

        book_mark_list = (ListView)findViewById(R.id.book_mark_list);
        book_mark_list.setAdapter(adpter);
    }



    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.view_button)
        {

        }
        else
        {
            finish();
        }
    }
}
