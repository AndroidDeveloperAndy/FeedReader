package com.hackspace.andy.readrss.loader.Implementation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private ImageView imgBmHabra;
    //TODO talk with me please, explane hardcode url.
    final static String PICTURE_URL = "https://pp.vk.me/c625620/v625620167/2ac69/m412UXyPZPE.jpg";
    private Bitmap bmIcon;

    private static final String TAG = DownloadImageTask.class.getName();

    //TODO AsyncTask should't know about View layer. If you load an image return image bitmap.
    public DownloadImageTask(ImageView bmImage) {
        this.imgBmHabra = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        try (final InputStream in = new java.net.URL(PICTURE_URL).openStream()) {
            bmIcon = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e(TAG, "Error load image!", e);
        }
        return bmIcon;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    protected void onPostExecute(Bitmap resultImage) {
        imgBmHabra.setImageBitmap(resultImage);
    }
}