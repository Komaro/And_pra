/*
    //TAG
    network



 */

package com.example.kaka.mediaplayer;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {




    private final Handler httphandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what)
            {
                case 0: //HTTP FAILED

                    break;

                case 1: //HTTP SUCCESS

                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // SERVER URL
        String url = "http://108.61.200.205/";
        int param = 1;
        String message = "";

        new CallHttp(url, param, message, httphandle);
    }


    // HTTP CALL
    private class CallHttp extends Thread {

        private String url, msg;
        private int para;
        private Handler hand;


        public CallHttp(String url, int para, String msg, Handler hand)
        {
            this.url = url;
            this.para = para;
            this.msg = msg;
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

                StringBuilder builder = new StringBuilder(); // STRING OBJECT
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8")); // STRING SETTING

                String line;

                while((line = reader.readLine()) != null)
                {

                }

            }
            catch (Exception ex)
            {
                message.what = 0;
                message.arg1 = 0;
                hand.sendMessage(message);

                Log.i("network", "CALL HTTP : " + ex.getMessage());
            }
        }
    }
}
