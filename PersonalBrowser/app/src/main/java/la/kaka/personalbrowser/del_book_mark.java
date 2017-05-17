package la.kaka.personalbrowser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Administrator on 2017-05-10.
 */

public class del_book_mark extends AppCompatActivity implements View.OnClickListener{

    Button del_button, exit_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_book_mark);

        del_button = (Button)findViewById(R.id.del_button);
        exit_button = (Button)findViewById(R.id.d_exit_button);

        del_button.setOnClickListener(this);
        exit_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.del_button)
        {

        }
        else
        {
            finish();
        }
    }
}
