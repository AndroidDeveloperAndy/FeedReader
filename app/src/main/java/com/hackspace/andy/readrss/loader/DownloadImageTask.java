package com.hackspace.andy.readrss.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;


public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private ImageView imgBmHabra;
    private String urlDisplay;
    private Bitmap bmIcon;

    private static final String TAG = DownloadImageTask.class.getName();

    public DownloadImageTask(ImageView bmImage) {
        this.imgBmHabra = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        urlDisplay = urls[0];
        try (final InputStream in = new java.net.URL(urlDisplay).openStream()) {
            bmIcon = BitmapFactory.decodeStream(in);
            isCancelled();
        } catch (Exception e) {
            Log.e(TAG, "Error load image!", e);
        }
        return bmIcon;
    }


    protected void onPostExecute(Bitmap resultImage) {
        imgBmHabra.setImageBitmap(resultImage);
    }
}