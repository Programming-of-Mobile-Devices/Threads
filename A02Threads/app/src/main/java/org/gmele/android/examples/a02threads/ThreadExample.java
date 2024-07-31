package org.gmele.android.examples.a02threads;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class ThreadExample extends AppCompatActivity implements View.OnClickListener
{
        final String[] Sites = {"http://www.uniwa.gr", "http://www.ice.uniwa.gr",
            "https://www.in.gr", "http://www.google.gr"};
        TextView TvMess;
        Button BtDoit;
        Button BtNext;


        @Override
        protected void onCreate (Bundle savedInstanceState)
        {
            super.onCreate (savedInstanceState);
            setContentView (R.layout.threads_lay);
            TvMess = (TextView) findViewById (R.id.TvMessage);
            BtDoit = (Button) findViewById (R.id.BtDoit);
            BtDoit.setOnClickListener (this);
            BtNext = (Button) findViewById (R.id.BtNext);
            BtNext.setOnClickListener (this);
        }

        @Override
        public void onClick (View v)
        {
            if (v == BtDoit)
            {
                int Page = new Random ().nextInt (4);
                MyThread Mt = new MyThread (this, Sites[Page]);
                Mt.start ();
            }
            if (v == BtNext)
            {
                Intent intent = new Intent (getApplicationContext (), Timers.class);
                startActivity (intent);
            }
        }

        public void ShowMessage (String Mess)
        {
            Message msg = new Message ();
            Bundle bun = new Bundle ();
            bun.putString ("Message", Mess);
            msg.setData (bun);
            MyHandler.sendMessage (msg);
        }

        Handler MyHandler = new Handler ()
        {
            @Override
            public void handleMessage (Message Mess)
            {
                Bundle b = Mess.getData ();
                String tbp = b.getString ("Message");
                TvMess.setText (tbp);
            }
        };
}

class MyThread extends Thread
{
    ThreadExample Father;
    String Site;

    MyThread (ThreadExample f, String s)
    {
        Father = f;
        Site = s;
    }

    @Override
    public void run ()
    {
        int count = 0;
        Father.ShowMessage ("Getting: " + Site);
        try
        {
            URL url = new URL (Site);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream Inp = urlConnection.getInputStream();
            BufferedReader Reader = new BufferedReader (new InputStreamReader (Inp));
            while (Reader.read () != -1)
                count++;
            Reader.close ();
            urlConnection.disconnect();
            Father.ShowMessage (Site + " size: " + count);
        }
        catch (IOException e)
        {
            System.err.println (e.getMessage ());
        }

    }
}
