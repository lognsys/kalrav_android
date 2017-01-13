package com.lognsys.kalrav.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.lognsys.kalrav.R;
import com.lognsys.kalrav.model.Notification;
import com.lognsys.kalrav.util.Constants;
import com.lognsys.kalrav.util.KalravApplication;

import java.util.List;

/**
 * Created by pdoshi on 09/01/17.
 */

public class CustomListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Notification> notifications;
    ImageLoader imageLoader = KalravApplication.getInstance().getImageLoader();

    public CustomListAdapter(Context context, List<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @Override
    public int getCount() {
        return notifications.size();
    }

    @Override
    public Object getItem(int location) {
        return notifications.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        if (imageLoader == null)
            imageLoader = KalravApplication.getInstance().getImageLoader();

        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView message = (TextView) convertView.findViewById(R.id.message);
        TextView genre = (TextView) convertView.findViewById(R.id.genre);
        TextView timeStamp = (TextView) convertView.findViewById(R.id.timestamp);

        // getting movie data for the row
        Notification m = notifications.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.getImageUrl(), imageLoader);


        String genreType = m.getGenre();

        switch (genreType) {

            case Constants.GENRE_CHARITY:
                thumbNail.setDefaultImageResId(Constants.notificationImages[2]);
                break;
            case Constants.GENRE_TRIP:
                thumbNail.setDefaultImageResId(Constants.notificationImages[1]);
                break;
            case Constants.GENRE_GENERAL:
                thumbNail.setDefaultImageResId(Constants.notificationImages[0]);
                break;
            default:
                thumbNail.setDefaultImageResId(Constants.notificationImages[0]);

        }


        // title
        title.setText(m.getTitle());

        // rating
        message.setText(m.getMessage());

        // genre
        genre.setText(m.getGenre());

        // timeStamp
        timeStamp.setText(String.valueOf(m.getTimeStamp()));

        return convertView;
    }

}
