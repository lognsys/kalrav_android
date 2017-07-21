package com.lognsys.kalrav.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lognsys.kalrav.R;
import com.lognsys.kalrav.adapter.CustomListAdapter;
import com.lognsys.kalrav.db.NotificationDAOImpl;
import com.lognsys.kalrav.model.NotificationInfo;

import java.util.List;


public class NotificationFragment extends Fragment {


    // Log tag
    private static final String TAG = NotificationFragment.class.getSimpleName();
    private ProgressDialog pDialog;
   // private List<NotificationInfo> notificationList = new ArrayList<NotificationInfo>();
    private ListView listView;
    private CustomListAdapter adapter;
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

    private void readNotification() {
        listNotificationInfo=impl.getAllNotificationInfo();

        adapter = new CustomListAdapter(this.getContext(), listNotificationInfo);
        listView.setAdapter(adapter);


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
}
