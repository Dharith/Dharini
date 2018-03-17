package edu.nus.trailblazelearn.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.VideoView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by dpak1 on 3/13/2018.
 */

public class RetriveImageBitmap extends AsyncTask<String, Bitmap, Void>{
    private Context context;
    private ImageView imageView;
    public RetriveImageBitmap(Context context, ImageView imageView) {
        this.context = context;
        this.imageView = imageView;
    }

      public static Bitmap bm = null;
    public Bitmap getBitmp(String uri) {
        URL aURL = null;
        Bitmap bm = null;
        try {
            URLConnection conn = new URL(uri).openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;
    }

        URL aURL = null;
    @Override
    protected Void doInBackground(String... strings) {
        try {
            URLConnection conn = new URL(strings[0]).openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        imageView.setImageBitmap(bm);

    }
}
