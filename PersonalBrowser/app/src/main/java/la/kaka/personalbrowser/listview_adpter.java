package la.kaka.personalbrowser;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

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
        return arr.get(position).name;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.list_view_layout, null, true);

        TextView name = (TextView)rowView.findViewById(R.id.name_text);
        TextView url = (TextView)rowView.findViewById(R.id.url_text);
        TextView date = (TextView)rowView.findViewById(R.id.date_text);

        name.setText(arr.get(position).name);
        url.setText(arr.get(position).url);
        date.setText(arr.get(position).date);

        return rowView;
    }
}
