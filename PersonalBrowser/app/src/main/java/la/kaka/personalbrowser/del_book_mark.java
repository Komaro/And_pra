package la.kaka.personalbrowser;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-05-10.
 */

public class del_book_mark extends AppCompatActivity implements View.OnClickListener{

    Button del_button, exit_button;
    Cursor cursor;
    DB_Helper helper;
    SQLiteDatabase db;
    listview_adpter adpter;
    ArrayList<list_item> items = new ArrayList<>();
    ListView del_book_mark_list;
    String select_url;
    int select_position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_book_mark);

        del_button = (Button)findViewById(R.id.del_button);
        exit_button = (Button)findViewById(R.id.d_exit_button);

        del_button.setOnClickListener(this);
        exit_button.setOnClickListener(this);

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

        del_book_mark_list = (ListView)findViewById(R.id.del_book_mark_list);
        del_book_mark_list.setAdapter(adpter);

        del_book_mark_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                select_url = (String) parent.getAdapter().getItem(position);
                select_position = position;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.del_button)
        {
            if(!select_url.equals(""))
            {
                db.execSQL("DELETE FROM BookMark WHERE url = '" + select_url + "'");
                items.remove(select_position);
                del_book_mark_list.clearChoices();
                adpter.notifyDataSetChanged();
            }
        }
        else
        {
            finish();
        }
    }
}
