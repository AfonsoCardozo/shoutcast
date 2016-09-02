package com.byteshaft.shoutcast;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by s9iper1 on 9/2/16.
 */
public class Helpers {

    public static Bitmap downloadImage(String link) {
        Bitmap myBitmap = null;
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            try {
                InputStream input = connection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(input);

            } catch (Exception e) {
                e.fillInStackTrace();
                Log.v("ERROR", "Errorchence : " + e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myBitmap;
    }

    public static TelephonyManager getTelephonyManager() {
        return (TelephonyManager) AppGlobals.getContext().getSystemService(Context.TELEPHONY_SERVICE);
    }
}
