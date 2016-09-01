package com.byteshaft.shoutcast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.pits.library.radio.RadioListener;
import com.pits.library.radio.RadioManager;


public class MainActivity extends AppCompatActivity implements RadioListener {

    private RadioManager mRadioManager;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        setSupportActionBar(toolbar);
        button = (Button) findViewById(R.id.button);
        mRadioManager = RadioManager.with(this, MainActivity.class);
        mRadioManager.registerListener(this);
        mRadioManager.setLogging(true);
        mRadioManager.connect();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mRadioManager.isPlaying()) {
                    mRadioManager.startRadio("http://108.178.53.106:8000/");
                }
                else {
                    mRadioManager.stopRadio();
                }

            }
        });
    }

    @Override
    public void onRadioLoading() {
        Log.i("TAG", "Loading");

    }

    @Override
    public void onRadioConnected() {
        System.out.println("OK");
        Log.i("TAG", "connected");
    }

    @Override
    public void onRadioStarted() {
        Log.i("TAG", "started");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.setBackgroundResource(R.mipmap.pause);
            }
        });

    }

    @Override
    public void onRadioStopped() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.setBackgroundResource(R.mipmap.play);
            }
        });

    }

    @Override
    public void onMetaDataReceived(String s, String s1) {
        Log.i("s", " "+ s);
        Log.i("s1", " "+s1);

    }

    @Override
    public void onError() {

    }

    @Override
    public void songInfo(final String title) {

    }
}
