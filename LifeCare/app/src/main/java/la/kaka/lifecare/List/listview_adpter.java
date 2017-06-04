package la.kaka.lifecare.List;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import la.kaka.lifecare.R;

/**
 * Created by Administrator on 2017-05-17.
 */

public class listview_adpter extends BaseAdapter {

    Activity context;
    ArrayList<list_item> arr;

    public listview_adpter(Activity context, ArrayList<list_item> arr) {

        this.arr = arr;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int position) {
        return arr.get(position).date;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.exelist_view_laytou, null, true);

        TextView date = (TextView)rowView.findViewById(R.id.date_text);
        TextView exe = (TextView)rowView.findViewById(R.id.exe_text);
        TextView time = (TextView)rowView.findViewById(R.id.time_text);

        date.setText(arr.get(position).date);
        exe.setText(arr.get(position).exe);
        time.setText(arr.get(position).time);

        return rowView;
    }
}
