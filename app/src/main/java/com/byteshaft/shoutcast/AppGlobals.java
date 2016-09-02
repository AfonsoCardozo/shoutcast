package com.byteshaft.shoutcast;

import android.app.Application;
import android.content.Context;

/**
 * Created by s9iper1 on 9/2/16.
 */
public class AppGlobals extends Application {

    private static boolean songPlaying = false;
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();

    }

    public static Context getContext() {
        return sContext;
    }

    public static void setSongPlaying(boolean status) {
        songPlaying = status;
    }

    public static boolean getSongStatus() {
        return songPlaying;
    }
}
