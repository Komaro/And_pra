/*
    //TAG
    network
    title
 */

package com.example.kaka.mediaplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    public final Handler httphandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what)
            {
                case 0: //HTTP FAILED

                    Toast.makeText(getApplicationContext(), "LIST LOAD FAILED", Toast.LENGTH_SHORT).show();
                    Log.i("network", "NETWORK : HTTP CONNECT FAILED");
                    break;

                case 1: //HTTP SUCCESS

                    catch_string = builder.toString();
                    catch_list = catch_string.split("┃");

                    ArrayAdapter<String> list_output = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, catch_list);
                    playlist.setAdapter(list_output);

                    Log.i("network", "NETWORK : HTTP CONNECT SUCCESS");
                    break;

                case 2 : //DOWNLOAD COMPLETE

                    try {
                        music_player.reset();
                        music_player.setDataSource(getCacheDir() + catch_list[msg.arg1] + ".mp3");
                        music_player.prepare();
                        music_player.start();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;

                case 3: // PROGRESSBAR REFRESH

                    download_pro.setProgress(msg.arg1);
                    break;
            }
        }
    };

    public StringBuilder builder;
    public BufferedReader reader;

    ListView playlist;
    String catch_string;
    String[] catch_list;

    private MediaPlayer music_player;
    private ProgressBar download_pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playlist = (ListView)findViewById(R.id.playlist);
        download_pro = (ProgressBar)findViewById(R.id.download_pro);
        music_player = new MediaPlayer();
        music_player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        출처: http://unikys.tistory.com/350 [All-round programmer]

        playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                new DownloadMusic().execute(catch_list[position], httphandle, position, getCacheDir());
            }
        });

        // SERVER URL
        String url = "http://108.61.200.205/request_playlist.php";
        int param = 1;
        String message = "";

        new CallHttp(url, httphandle).start();
}

    // HTTP CALL
    private class CallHttp extends Thread {

        private String url;
        private Handler hand;

        public CallHttp(String url,  Handler hand)
        {
            this.url = url;
            this.hand = hand;
        }

        @Override
        public void run() {
            Message message = hand.obtainMessage();
            URL send_url;

            // HttpClient DEPRECATE
            // REFERENCE : http://mommoo.tistory.com/5
            try
            {
                send_url = new URL(this.url);
                HttpURLConnection connection = (HttpURLConnection)send_url.openConnection();

                connection.setRequestMethod("GET");
                connection.setDoOutput(false); // WRITING
                connection.setDoInput(true); // READING
                connection.setUseCaches(false);
                connection.setDefaultUseCaches(false);

                // INPUT STREAM OPEN
                InputStream is = connection.getInputStream();

                builder = new StringBuilder(); // STRING OBJECT
                reader = new BufferedReader(new InputStreamReader(is, "UTF-8")); // STRING SETTING

                String line;

                while((line = reader.readLine()) != null)
                {
                    builder.append(line);
                }

                message.what = 1;
                hand.sendMessage(message);
            }
            catch (Exception ex)
            {
                message.what = 0;
                hand.sendMessage(message);

                Log.i("network", "CALL HTTP : " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}
