package com.byteshaft.shoutcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.byteshaft.requests.HttpRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.pits.library.radio.RadioListener;
import com.pits.library.radio.RadioManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity implements RadioListener, View.OnClickListener, HttpRequest.OnReadyStateChangeListener {

    private RadioManager mRadioManager;
    private ImageButton button;
    private String streamUrl = "http://108.178.53.106:6100";
    private List<String> channelsList;
    private Button channelOne;
    private Button channelTwo;
    private Button channelThree;
    private Button channelFour;
    private Button channelFive;
    private Button[] channelsButton;
    private ImageView imageArt;
    private TextView songTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        setSupportActionBar(toolbar);
        button = (ImageButton) findViewById(R.id.button);
        channelOne = (Button) findViewById(R.id.channel_one);
        channelTwo = (Button) findViewById(R.id.channel_two);
        channelThree = (Button) findViewById(R.id.channel_three);
        channelFour = (Button) findViewById(R.id.channel_four);
        channelFive = (Button) findViewById(R.id.channel_five);
        imageArt = (ImageView) findViewById(R.id.art_image);
        songTitle = (TextView) findViewById(R.id.song_name);
        channelOne.setOnClickListener(this);
        channelTwo.setOnClickListener(this);
        channelThree.setOnClickListener(this);
        channelFour.setOnClickListener(this);
        channelFive.setOnClickListener(this);;
        channelsList = Arrays.asList(getResources().getStringArray(R.array.channels_array));
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);
        mRadioManager = RadioManager.with(this, MainActivity.class);
        mRadioManager.registerListener(this);
        mRadioManager.setLogging(true);
        mRadioManager.connect();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mRadioManager.isPlaying()) {
                    mRadioManager.startRadio(streamUrl);
                }
                else {
                    mRadioManager.stopRadio();
                }

            }
        });
        channelsButton = new Button[] {channelOne, channelTwo,
                channelThree, channelFour, channelFive};
        TelephonyManager telephonyManager = Helpers.getTelephonyManager();
        telephonyManager.listen(mCallStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(mOutGoingCallListener, intentFilter);
    }

    @Override
    public void onRadioLoading() {
        Log.i("TAG", "Loading");

    }

    @Override
    public void onRadioConnected() {
        Log.i("TAG", "connected");
        mRadioManager.startRadio(streamUrl);
        setButtonEffect(channelOne);
    }

    @Override
    public void onRadioStarted() {
        Log.i("TAG", "started");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AppGlobals.setSongPlaying(true);
                button.setBackgroundResource(R.mipmap.pause);
            }
        });

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onRadioStopped() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AppGlobals.setSongPlaying(false);
                button.setBackgroundResource(R.mipmap.play);
            }
        });

    }

    @Override
    public void onMetaDataReceived(String s, String s1) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void songInfo(final String title) {
        Log.e("TAG", title);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                songTitle.setText(Html.fromHtml(title));
                if (title.contains(":.:")) {
                    String[] split = title.split(":.:");
                    String artist = split[0];
                    String songTitle = split[1];
                    Log.e("TAGIMAGE", songTitle);
                    getImageForTitle(songTitle);
                } else if (title.contains("-|-")) {
                    String[] split = title.split("\\|");
                    String artist = split[0];
                    String songTitle = split[1].replaceAll("-", "");
                    Log.e("TAGIMAGE", songTitle);
                    getImageForTitle(songTitle);
                } else if (title.contains("-:-")) {
                    String[] split = title.split("-:-");
                    String artist = split[0];
                    String songTitle = split[1];
                    Log.e("TAGIMAGE", songTitle);
                    getImageForTitle(songTitle);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int index = channelsList.indexOf(streamUrl);
        switch (index) {
            case 0:
                setButtonEffect(channelOne);
                break;
            case 1:
                setButtonEffect(channelTwo);
                break;
            case 2:
                setButtonEffect(channelThree);
                break;
            case 3:
                setButtonEffect(channelFour);
                break;
            case 4:
                setButtonEffect(channelFive);
                break;
            default:
                setButtonEffect(channelOne);
                break;

        }

    }

    @Override
    public void onClick(View view) {
        imageArt.setBackground(getResources()
                .getDrawable(R.drawable.image_art_placeholder));
        switch (view.getId()) {
            case R.id.channel_one:
                mRadioManager.stopRadio();
                streamUrl = channelsList.get(0);
                mRadioManager.startRadio(streamUrl);
                setButtonEffect(channelOne);
            break;
            case R.id.channel_two:
                mRadioManager.stopRadio();
                streamUrl = channelsList.get(1);
                mRadioManager.startRadio(streamUrl);
                setButtonEffect(channelTwo);
                break;
            case R.id.channel_three:
                mRadioManager.stopRadio();
                streamUrl = channelsList.get(2);
                mRadioManager.startRadio(streamUrl);
                setButtonEffect(channelThree);
                break;
            case R.id.channel_four:
                mRadioManager.stopRadio();
                streamUrl = channelsList.get(3);
                mRadioManager.startRadio(streamUrl);
                setButtonEffect(channelFour);
                break;
            case R.id.channel_five:
                mRadioManager.stopRadio();
                streamUrl = channelsList.get(4);
                mRadioManager.startRadio(streamUrl);
                setButtonEffect(channelFive);
                break;
            default:
                mRadioManager.stopRadio();
                streamUrl = channelsList.get(0);
                mRadioManager.startRadio(streamUrl);
                setButtonEffect(channelOne);
                break;
        }
    }

    private void setButtonEffect(Button clickedButton) {
        for (Button button: channelsButton) {
            if (button == clickedButton) {
                button.setBackgroundResource(R.drawable.channel_button_pressed);
                button.setTextColor(getResources().getColor(android.R.color.black));
            } else {
                button.setBackgroundResource(R.drawable.channel_button_normal);
                button.setTextColor(getResources().getColor(android.R.color.white));
            }

        }
    }

    private void getImageForTitle(String title) {
        HttpRequest request = new HttpRequest(getApplicationContext());
        request.setOnReadyStateChangeListener(this);
        String url = String.format("https://itunes.apple.com/search?term=%s&media=music&limit=1", title);
        request.open("GET", url.replaceAll(" ", "%20"));
        request.send();
    }

    @Override
    public void onReadyStateChange(HttpRequest request, int readyState) {
        switch (readyState) {
            case HttpRequest.STATE_DONE:
                switch (request.getStatus()) {
                    case HttpURLConnection.HTTP_OK:
                        // Request successful
                        String response = request.getResponseText();
                        Log.i("TAG", request.getResponseText());
                        if (!response.trim().isEmpty() && response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getInt("resultCount") > 0) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                                    JSONObject songDetails = jsonArray.getJSONObject(0);
                                    String imageUrl = songDetails.getString("artworkUrl100")
                                            .replaceAll("100", "450");
                                    Log.e("TAG", "Image " + imageUrl);
                                    new SetImageArt().execute(imageUrl);
                                } else {
                                    imageArt.setBackground(getResources()
                                            .getDrawable(R.drawable.image_art_placeholder));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case HttpURLConnection.HTTP_BAD_REQUEST:
                        // Something was wrong
                        request.getResponseText();
                        request.getStatusText();
                        break;
                }
        }

    }

    class SetImageArt extends AsyncTask<String, String, BitmapDrawable> {

        @Override
        protected BitmapDrawable doInBackground(String... strings) {
            Bitmap bitmap = Helpers.downloadImage(strings[0]);
            Bitmap blurredBitmap = null;
            if (bitmap != null) {
                blurredBitmap = BlurBuilder.blur(getApplicationContext(), bitmap);
            }
            return new BitmapDrawable(bitmap);
        }

        @Override
        protected void onPostExecute(BitmapDrawable bitmapDrawable) {
            super.onPostExecute(bitmapDrawable);
            if (bitmapDrawable != null) {
                imageArt.setBackground(null);
                imageArt.setBackground(bitmapDrawable);
            } else {
                imageArt.setBackground(getResources()
                        .getDrawable(R.drawable.image_art_placeholder));
            }
        }
    }

    private PhoneStateListener mCallStateListener = new PhoneStateListener() {
        boolean songWasOn = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    if (AppGlobals.getSongStatus()) {
                        mRadioManager.stopRadio();
                        songWasOn = true;
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    if (songWasOn) {
                        mRadioManager.startRadio(streamUrl);
                    }
                    break;
            }
        }
    };

    private BroadcastReceiver mOutGoingCallListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AppGlobals.getSongStatus()) {
                mRadioManager.stopRadio();
            }
        }
    };
}
