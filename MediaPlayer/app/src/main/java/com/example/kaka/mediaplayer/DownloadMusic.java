package com.example.kaka.mediaplayer;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2017-06-14.
 */

public class DownloadMusic extends AsyncTask {

    String title;
    URL send_url;
    Handler hand;
    int position;


    protected void onPreExecute() {

        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Object o) {

        Message message = hand.obtainMessage();
        message.what = 2;
        message.arg1 = position;

        hand.sendMessage(message);

        super.onPostExecute(o);
    }

    @Override
    protected Object doInBackground(Object[] params) {

        this.title = params[0].toString();
        this.hand = (Handler)params[1];
        this.position = (int)params[2];
        String path = params[3].toString();

        // HttpClient DEPRECATE
        // REFERENCE : http://mommoo.tistory.com/5
        try {

            String encode_title = URLEncoder.encode(title, "UTF-8");
            encode_title = encode_title.replace("+", "%20");

            String url = "http://108.61.200.205/MusicList/" + encode_title + ".mp3";
            Log.i("title", "TITLE : " + encode_title + ".mp3");

            send_url = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) send_url.openConnection();

            connection.setRequestMethod("GET");
            connection.setDoOutput(false); // WRITING
            connection.setDoInput(true); // READING
            connection.setUseCaches(false);
            connection.setDefaultUseCaches(false);
            connection.setConnectTimeout(10000);

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                File file = new File(path + title + ".mp3");
                FileOutputStream fileoutput = new FileOutputStream(file);
                InputStream inputstream = connection.getInputStream();

                int downloadedSize = 0;
                byte[] buffer = new byte[1024];
                int bufferLength = 0;
                int total = connection.getContentLength();

                while((bufferLength = inputstream.read(buffer)) > 0)
                {
                    fileoutput.write(buffer, 0 , bufferLength);
                    downloadedSize += bufferLength;
                    publishProgress((int)(downloadedSize * 100) / total);
                }
                fileoutput.close();
            }
        }
        catch (Exception ex)
        {
            Log.i("download", "DOWNLOAD : " + ex.getMessage());
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);

        Message message = hand.obtainMessage();
        message.what = 3;
        message.arg1 = (int)values[0];
        hand.sendMessage(message);
    }
}
