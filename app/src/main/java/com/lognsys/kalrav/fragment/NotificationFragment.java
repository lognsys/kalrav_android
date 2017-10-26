package com.lognsys.kalrav.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.lognsys.kalrav.HomeActivity;
import com.lognsys.kalrav.R;
import com.lognsys.kalrav.db.NotificationDAOImpl;
import com.lognsys.kalrav.model.NotificationInfo;
import com.lognsys.kalrav.util.KalravApplication;

import java.util.List;


public class NotificationFragment extends Fragment {


    // Log tag
    private static final String TAG = NotificationFragment.class.getSimpleName();
    private ProgressDialog pDialog;
   // private List<NotificationInfo> notificationList = new ArrayList<NotificationInfo>();
    private ListView listView;
     CustomListAdapter adapter;
    NotificationDAOImpl impl;
    NotificationInfo info;
    List<NotificationInfo> listNotificationInfo;


    @Override
    public View  onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        impl=new NotificationDAOImpl(getActivity());

        listView = (ListView) view.findViewById(R.id.list);
        readNotification();
//       adapter = new CustomListAdapter(this.getContext(), MockObjects.getNotificationInfoList());
//        listView.setAdapter(adapter);

        return view;
    }

    public void readNotification() {
        listNotificationInfo=impl.getAllNotificationInfo();

        if(listNotificationInfo!=null && listNotificationInfo.size()>0){
            /*if(listNotificationInfo.size()>10){
                Log.d("NotificationListView","Rest Notification List listNotificationInfo.size()============== "+listNotificationInfo.size());

               int count = impl.deleteNotificationInfo();
                Log.d("NotificationListView","Rest Notification List count()============== "+count);

            }*/
            adapter = new CustomListAdapter(this.getContext(), listNotificationInfo);
            listView.setAdapter(adapter);
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }




     class CustomListAdapter extends BaseAdapter {
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
                holder.delete = (ImageView) convertView.findViewById(R.id.imgdelete);
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.message = (TextView) convertView.findViewById(R.id.message);
                holder.realname = (TextView) convertView.findViewById(R.id.realname);
                holder.bookingid = (TextView) convertView.findViewById(R.id.bookingId);
                holder.confirmationcode = (TextView) convertView.findViewById(R.id.confirmationcode);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            if (imageLoader == null)
                imageLoader = KalravApplication.getInstance().getImageLoader();

            NetworkImageView thumbNail = (NetworkImageView) convertView
                    .findViewById(R.id.thumbnail);


            // getting movie data for the row
            final NotificationInfo info = notificationInfos.get(position);

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

            if(info.getBookingId()!=0)
                holder.bookingid.setText("Booking Id = "+info.getBookingId());
            else{
                holder.bookingid.setVisibility(View.GONE);
            }
            if(info.getConfirmationCode()!=null)
                holder.confirmationcode.setText("Confirmation code = "+info.getConfirmationCode());
            else{
                holder.confirmationcode.setVisibility(View.GONE);
            }

            if(info.getRealname()!=null)
                holder.realname.setText(info.getRealname());
            else{
                holder.realname.setVisibility(View.GONE);
            }
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NotificationDAOImpl notificationDAO=new NotificationDAOImpl(context);
                    int deleteCount =notificationDAO.deleteNotificationInfoById(info.get_id());

                    Log.d("FCM","DELETE notificationInfo BY deleteCount "+deleteCount);
                    int countremaining =notificationDAO.countNotificationAfterDelete();
                    Log.d("FCM","DELETE notificationInfo  countremaining "+countremaining);
                    if(countremaining==0){
                        Log.d("FCM","DELETE notificationInfo if countremaining "+countremaining);
                        Log.d("FCM","DELETE notificationInfo if countremaining goto home activity ");

                        Intent  intent = new Intent(getActivity(),
                                HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    }
                    else{
                        readNotification();
                       adapter.notifyDataSetChanged();

                        Log.d("FCM","DELETE notificationInfo else countremaining "+countremaining);

                    }

                }
            });


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
        private class Holder {
            public TextView title;
            public TextView message;
            public ImageView delete;
            public TextView realname;
            public TextView bookingid;
            public TextView confirmationcode;
            int pos;
        }
    }
}

