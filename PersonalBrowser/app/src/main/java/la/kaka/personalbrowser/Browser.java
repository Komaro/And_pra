package la.kaka.personalbrowser;

import android.content.Intent;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Browser extends AppCompatActivity {

    Button serch_button;
    EditText url;
    WebView browser;
    static final int MARK_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        browser = (WebView)findViewById(R.id.browser);
        browser.setWebViewClient(new WebViewClient());
        url = (EditText)findViewById(R.id.url);
        serch_button = (Button)findViewById(R.id.serch_button);
        serch_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serching_page();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bookmark_menu, menu);

        return true;
    }

    public void serching_page()
    {
        if(url.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "URL을 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }

        String input_url = url.getText().toString();

        browser.getSettings().setLoadsImagesAutomatically(true);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        browser.loadUrl(input_url);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = new Intent();

        switch (item.getItemId())
        {
            case R.id.book_mark:
                intent.setClass(this, book_mark.class);
                break;

            case R.id.reg_book_mark:
                intent.setClass(this, reg_book_mark.class);
                break;

            case R.id.del_book_mark:
                intent.setClass(this, del_book_mark.class);
                break;
        }

        startActivityForResult(intent, MARK_CODE);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MARK_CODE) {
            if(resultCode == RESULT_OK)
            {
                url.setText(data.getStringExtra("SEND_URL"));
                serching_page();
            }
        }
    }
}
