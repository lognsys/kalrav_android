package com.lognsys.kalrav.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.lognsys.kalrav.HomeActivity;
import com.lognsys.kalrav.R;
import com.lognsys.kalrav.fragment.ConfirmFragment;
import com.lognsys.kalrav.fragment.FragmentDramaDetail;
import com.lognsys.kalrav.model.NotificationInfo;
import com.lognsys.kalrav.util.Constants;
import com.lognsys.kalrav.util.KalravApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pdoshi on 09/01/17.
 */

public class CustomListAdapter extends BaseAdapter {
     Context context;
    private LayoutInflater inflater;
    private List<NotificationInfo> notificationInfos;
    ImageLoader imageLoader = KalravApplication.getInstance().getImageLoader();

    public CustomListAdapter(Context context, List<NotificationInfo> notificationInfos) {
        this.context = context;
        this.notificationInfos = notificationInfos;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return notificationInfos.size();
    }

    @Override
    public Object getItem(int location) {
        return notificationInfos.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;

        if (convertView == null) {
            // Inflate the view since it does not exist
            convertView = inflater.inflate(R.layout.list_row, parent, false);

            // Create and save off the holder in the tag so we get quick access to inner fields
            // This must be done for performance reasons
            holder = new Holder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.message = (TextView) convertView.findViewById(R.id.message);
            holder.realname = (TextView) convertView.findViewById(R.id.genre);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        if (imageLoader == null)
            imageLoader = KalravApplication.getInstance().getImageLoader();

        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);


        // getting movie data for the row
        NotificationInfo info = notificationInfos.get(position);

        Log.d("NotificationListView","Rest Notification List info.getDramaId()============== "+info.getDramaId());


        holder.pos = position;
        // title
//        title.setText(info.getTitle());
        if(info.getDramaTitle()!=null)
            holder.title.setText(info.getDramaTitle());

        // rating
//        message.setText(m.getMessage());

        if(info.getMessage()!=null)
            holder.message.setText(info.getMessage());


        // genre
      //  genre.setText(m.getGenre());

        if(info.getRealname()!=null)
            holder.realname.setText(info.getRealname());

        // timeStamp
//        timeStamp.setText(String.valueOf(m.getTimeStamp()));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Holder holder = (Holder) v.getTag();
                NotificationInfo notificationInfo = (NotificationInfo) getItem(holder.pos);
                Log.d("FCM","FCM convertView notificationInfo "+notificationInfo);

                if(notificationInfo!=null && notificationInfo.getDramaId()!=0){
                     int dramaId = notificationInfo.getDramaId();
                    Log.d("FCM","FCM generateNotification dramaId "+dramaId);
                    boolean isNotification=true;
                    int navItemIndex=5;
                    Intent notificationIntent=new Intent(context, HomeActivity.class);
                    notificationIntent.putExtra("id", dramaId);
                    notificationIntent.putExtra("navItemIndex", navItemIndex);
                    notificationIntent.putExtra("isNotification", isNotification);
                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(notificationIntent);
                }

            }
        });
        return convertView;
    }

    /** View holder for the views we need access to */
    private static class Holder {
        public TextView title;
        public TextView message;
        public TextView realname;
        int pos;
    }
}
