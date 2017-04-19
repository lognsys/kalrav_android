package com.lognsys.kalrav.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.lognsys.kalrav.R;
import com.lognsys.kalrav.db.DramaInfoDAOImpl;
import com.lognsys.kalrav.db.FavouritesInfoDAO;
import com.lognsys.kalrav.db.FavouritesInfoDAOImpl;
import com.lognsys.kalrav.db.UserInfoDAOImpl;
import com.lognsys.kalrav.model.DramaInfo;
import com.lognsys.kalrav.model.FavouritesInfo;
import com.lognsys.kalrav.model.TimeSlot;
import com.lognsys.kalrav.model.UserInfo;
import com.lognsys.kalrav.util.Constants;
import com.lognsys.kalrav.util.KalravApplication;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class DramaFragment extends Fragment {
    ArrayList<DramaInfo> listitems = new ArrayList<>();
    RecyclerView myRecyclerView;
    static Bitmap bm;
    AdView mAdView;
    ArrayList<DramaInfo> dramaInfos;
    DramaInfoDAOImpl dramaInfoDAO;
    FavouritesInfoDAOImpl favouritesInfoDAOImpl;
//   http://www.json-generator.com/api/json/get/bYJqZHImiG?indent=2
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Kalrav Arts");
    }

    public void initializeList() {
        listitems.clear();
        dramaInfoDAO = new DramaInfoDAOImpl(getActivity());
        favouritesInfoDAOImpl = new FavouritesInfoDAOImpl(getActivity());
        dramaInfos = new ArrayList<DramaInfo>() ;
        if(KalravApplication.getInstance().isConnectedToInternet()) {
            new JSONParse().execute("http://www.json-generator.com/api/json/get/bVjwLYiZAi?indent=2");
        }
        else{
          Toast.makeText(getContext(),"Please check  your network connection",Toast.LENGTH_SHORT).show();
        }
    }
    private class JSONParse extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.d("","Test onpre ");
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try{

//                Log.d("","Test Doin ");

                Log.d("","Test Doin params[0] "+params[0]);
                URL u = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                InputStream is = conn.getInputStream();

            // Read the stream
                byte[] b = new byte[1024];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                while ( is.read(b) != -1)
                    baos.write(b);

                String JSONResp = new String(baos.toByteArray());
//                Log.d("","Test Doin JSONResp "+JSONResp);

                JSONArray arr = new JSONArray(JSONResp);
                Log.d("","Test Doin arr.length() "+arr.length());

                for (int i=0; i<arr.length(); i++) {
                    DramaInfo dramaInfo=new DramaInfo();

                    JSONObject jsonObject=(arr.getJSONObject(i));
                    String id=jsonObject.getString("id");
                    dramaInfo.setId(Integer.parseInt(id));
//                    Log.d("","Test Doin id) "+id);

                    String drama_name=jsonObject.getString("drama_name");
                    dramaInfo.setDrama_name(drama_name);
                    Log.d("","Test Doin drama_name "+drama_name);

                    String datetime=jsonObject.getString("datetime");
                    dramaInfo.setDatetime(datetime);
//                    Log.d("","Test Doin datetime "+datetime);

                    String photo_link=jsonObject.getString("photo_link");
                    dramaInfo.setLink_photo(photo_link);
//                    Log.d("","Test Doin photo_link "+photo_link);

                    String group_name=jsonObject.getString("group_name");
                    dramaInfo.setGroup_name(group_name);

                    String drama_length=jsonObject.getString("drama_length");
                    dramaInfo.setDrama_length(drama_length);

                    String drama_time=jsonObject.getString("time");
                    dramaInfo.setTime(drama_time);
                    StringBuilder sb;

                    JSONArray jsonArray=jsonObject.getJSONArray("drama_language");
                    if(jsonArray!=null && jsonArray.length()>0){
                        sb=new StringBuilder();
                        for(int j =0;j<jsonArray.length();j++){
                            sb.append(jsonArray.getString(j)+" , ");
                        }
                        dramaInfo.setDrama_language(sb.toString());
                    }

                    JSONArray jsonArrayGenre=jsonObject.getJSONArray("drama_genre");
                    if(jsonArrayGenre!=null && jsonArrayGenre.length()>0){
                        sb=new StringBuilder();
                        for(int j =0;j<jsonArrayGenre.length();j++){
                            {

                                sb.append(jsonArrayGenre.getString(j)+" , ");
                            }
                        }
                        dramaInfo.setGenre(sb.toString());
                    }
                    String briefDescription=jsonObject.getString("briefDescription");
                    dramaInfo.setBriefDescription(briefDescription);

//                    Log.d("","Test Doin group_name "+group_name);
                    dramaInfoDAO.addDrama(dramaInfo);
                }

                return null;
            }
            catch(Exception t) {
              Log.d("","Test Throwable "+t);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String json) {
            pDialog.dismiss();

            Log.d("","Test onPost ");
            dramaInfos= (ArrayList<DramaInfo>) dramaInfoDAO.getAllDrama();
            Log.d("","Test onPost dramaInfos.size "+dramaInfos.size());
            if (dramaInfos.size() > 0 & dramaInfos != null) {
                MyAdapter adapter=new MyAdapter(dramaInfos);
                myRecyclerView.setAdapter(adapter);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_drama, container, false);
        myRecyclerView = (RecyclerView) view.findViewById(R.id.cardView);
        myRecyclerView.setHasFixedSize(true);

        mAdView = (AdView)view.findViewById(R.id.listener_av_main);

        mAdView.setAdListener(new AdListener() {
            private void showToast(String message) {
                View view = mAdView;
                if (view != null) {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAdLoaded() {
                showToast("Ad loaded.");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                showToast(String.format("Ad failed to load with error code %d.", errorCode));
            }

            @Override
            public void onAdOpened() {
                showToast("Ad opened.");
            }

            @Override
            public void onAdClosed() {
                showToast("Ad closed.");
            }

            @Override
            public void onAdLeftApplication() {
                showToast("Ad left application.");
            }
        });

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .setContentUrl("THIS is kalrav app")
                .build();



        mAdView.loadAd(adRequest);

        initializeList();
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        myRecyclerView.setLayoutManager(MyLayoutManager);

        myRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        DramaInfo dramaInfo=(DramaInfo)view.getTag();
                        Fragment fragment = new FragmentDramaDetail();

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

            }
        });

        return view;
    }



    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private ArrayList<DramaInfo> list;

        public MyAdapter(ArrayList<DramaInfo> Data) {
            Log.d("", "MyAdapter constructore Data"+Data+"   Size ===" +Data.size());

            list = Data;
            Log.d("", "MyAdapter constructore list "+list+" list size ==="+list.size());

        }


        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {
            // create a new view
            Log.d("", "MyAdapter onCreateViewHolder ");
            View view=null;
            DramaInfo dramaInfo =list.get(position);

            view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycle_items, parent, false);
                MyViewHolder holder = new MyViewHolder(view);


                return holder;


        }

        @Override
        public void onBindViewHolder(final MyAdapter.MyViewHolder holder, final int position) {
            Log.d("", "MyAdapter onBindViewHolder ");

            final DramaInfo[] dramaInfo = {list.get(position)};
            holder.titleTextView.setText(dramaInfo[0].getDrama_name());
//            holder.coverImageView.setImageResource(Integer.parseInt(list.get(position).getLink_photo()));
            Picasso.with(getContext()).load(dramaInfo[0].getLink_photo()).into(holder.coverImageView);


            // holder.coverImageView.setTag(list.get(position).getImageResourceId());
           holder.bookmarkImageView.setOnClickListener(new View.OnClickListener() {
                private boolean stateChanged;
                @Override
                public void onClick(View v) {
                   dramaInfo[0] =list.get(position);
                    FavouritesInfo favouritesInfo=new FavouritesInfo();

                    Log.d("","Bookmark  dramaInfo.getId() "+ dramaInfo[0].getId());

                    dramaInfos.add(dramaInfo[0]);
                    if(stateChanged) {
                       Toast.makeText(v.getContext(), "Remove from Favourite", Toast.LENGTH_SHORT).show();
                        if(dramaInfo[0] !=null && dramaInfo[0].getId()!=0){

                            favouritesInfo.setDrama_id(dramaInfo[0].getId());
                        }
                       int count= favouritesInfoDAOImpl.deleteFav(favouritesInfo);
                        Log.d("","Bookmark  dramaInfo count"+count);
                        holder.bookmarkImageView.setImageResource(R.mipmap.ic_unlike);
                    }
                    else {
                        holder.bookmarkImageView.setImageResource(R.mipmap.ic_like);
                        Toast.makeText(v.getContext(), "Added to Favourite ", Toast.LENGTH_SHORT).show();
                        if(dramaInfo[0] !=null && dramaInfo[0].getId()!=0){

                            favouritesInfo.setDrama_id(dramaInfo[0].getId());
                        }
                        favouritesInfoDAOImpl.addFav(favouritesInfo);

                    }
                    stateChanged = !stateChanged;


                }
            });
            holder.textGroupname.setText(dramaInfo[0].getGroup_name());

            holder.shareImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bm=BitmapFactory.decodeResource(getResources(),list.get(position).getImageResourceId());
                    checkPermission();

                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView titleTextView;
            public ImageView coverImageView;
            public ImageView bookmarkImageView;
            public ImageView shareImageView;
            public TextView textGroupname;


            public MyViewHolder(View itemView) {
                super(itemView);
                titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
                coverImageView = (ImageView) itemView.findViewById(R.id.coverImageView);
                bookmarkImageView = (ImageView) itemView.findViewById(R.id.likeImageView);
                shareImageView = (ImageView) itemView.findViewById(R.id.shareImageView);
                textGroupname= (TextView) itemView.findViewById(R.id.textGroupname);
                Log.d("", "MyAdapter MyViewHolder ");
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition(); // gets item position
                if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                    DramaInfo dramaInfo = list.get(position);
                    // We can access the data within the views
                    Log.d("", "MyAdapter getAdapterPosition list.get(position) "+ list.get(position));
                    Log.d("", "MyAdapter getAdapterPosition MyViewHolder dramaInfo "+dramaInfo);
                    Log.d("", "MyAdapter getAdapterPositionMyViewHolder dramaInfo.getName "+dramaInfo.getDrama_name());
                   if(dramaInfo!=null) {
                       Bundle bundle = new Bundle();
                       bundle.putSerializable("dramaInfo", dramaInfo);
                       Fragment fragment = new FragmentDramaDetail();
                       fragment.setArguments(bundle);
                       getActivity().getSupportFragmentManager().beginTransaction()
                               .replace(R.id.frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                   }
                }
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    private void shareIt(Bitmap bm) {
//sharing implementation here
       /* Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Enhoy Drama");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Drama on 12/02/ 2017 at 10:00 Am ");
        startActivity(Intent.createChooser(sharingIntent, "Share via"));*/
      ///  Bitmap bm = BitmapFactory.decodeFile(R.id.);
       /* Bitmap icon = bm;
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Drama on 12/02/ 2017 at 10:00 Am ");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = getActivity().getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                values);


        OutputStream outstream;
        try {
            outstream = getActivity().getContentResolver().openOutputStream(uri);
            icon.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
            outstream.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share Image"));*/
        try {
            Bitmap adv = bm;
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            adv.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            File f = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "temporary_file.jpg");
            Uri uri = Uri.fromFile(f );
            try {
                f.createNewFile();
                new FileOutputStream(f).write(bytes.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            share.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            /*share.putExtra(Intent.EXTRA_STREAM,
                    Uri.parse((File)Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg"));*/
            share.putExtra(Intent.EXTRA_STREAM,uri);
            share.putExtra(Intent.EXTRA_TEXT, "Drama Gujarati thali, Drama time 12/2/1017, 10:20 am");
            startActivity(Intent.createChooser(share, "Share Image"));
           /* if (isPackageInstalled("com.whatsapp", getActivity())) {
                share.setPackage("com.whatsapp");
                startActivity(Intent.createChooser(share, "Share Image"));

            } else {

                Toast.makeText(getActivity(), "Please Install Whatsapp", Toast.LENGTH_LONG).show();
            }*/
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


    private void checkPermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        } else {
            shareIt(bm);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,  int[] grantResults) {
        switch (requestCode) {

            case 100:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    shareIt(bm);
                }
                break;

            default:
                break;
        }
    }
}


