package com.test.andorid.newsrss;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Administrator on 2/2/2016.
 */
public class InteractiveArrayAdapter extends ArrayAdapter<Map<String, String>> {

    private final ArrayList<Map<String, String>> list;
    private final Activity context;
    RoundImage roundImage;

    public InteractiveArrayAdapter(Activity context, ArrayList<Map<String, String>> list) {
        super(context, -1, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected TextView text1;
        protected TextView text2;
        protected ImageView imageView;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.basic_list_item, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.text1 = (TextView) view.findViewById(R.id.firstLine);
            viewHolder.text2 = (TextView) view.findViewById(R.id.secondLine);
            viewHolder.imageView = (ImageView)view.findViewById(R.id.charact);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        if(list.get(position).get("image")!=null) {
            new ImageLoadTask(list.get(position).get("image"),holder.imageView).execute();

        }
        holder.text1.setText(list.get(position).get("title"));
        holder.text2.setText(list.get(position).get("pubDate"));
        return view;
    }
    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            Resources res =  context.getResources();
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(res, result);
            roundedBitmapDrawable.setCornerRadius(Math.max(result.getWidth(), result.getHeight()) / 2.0f);
            imageView.setImageDrawable(roundedBitmapDrawable);
        }
       
    }




}
