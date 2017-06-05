package la.kaka.lifecare.List;

/**
 * Created by Administrator on 2017-05-17.
 */

public class list_item {

    public String date, exe, time;
    public int activation_check;

    public list_item(String date, String exe, String time, int activation_check)
    {
        this.date = date;
        this.exe = exe;
        this.time = time;
        this.activation_check = activation_check;
    }
}
