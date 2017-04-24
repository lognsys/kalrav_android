package com.lognsys.kalrav.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.lognsys.kalrav.R;
import com.lognsys.kalrav.db.DramaInfoDAOImpl;
import com.lognsys.kalrav.db.FavouritesInfoDAOImpl;
import com.lognsys.kalrav.model.DramaInfo;
import com.lognsys.kalrav.model.FavouritesInfo;
import com.lognsys.kalrav.util.KalravApplication;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    FavouritesInfo favouritesInfo;
    List<FavouritesInfo> favouritesInfos;
    MyAdapter adapter;
    private static final String JSON_URL="http://www.json-generator.com/api/json/get/bVjwLYiZAi?indent=2";
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
        favouritesInfo=new FavouritesInfo();
        favouritesInfos=new ArrayList<FavouritesInfo>();
        if(KalravApplication.getInstance().isConnectedToInternet()) {
            displaydrama();
        }
        else{
            Toast.makeText(getContext(),getString(R.string.network_connection),Toast.LENGTH_SHORT).show();
        }

    }

    private void displaydrama() {
        KalravApplication.getInstance().getPrefs().showpDialog(getContext());

        JsonArrayRequest req = new JsonArrayRequest(JSON_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {

                            for (int i=0; i<response.length(); i++) {
                                DramaInfo dramaInfo=new DramaInfo();

                                JSONObject jsonObject= null;
                                try {
                                    jsonObject = (response.getJSONObject(i));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(),
                                            "Error: 1 " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                                String id=jsonObject.getString("id");
                                dramaInfo.setId(Integer.parseInt(id));

                                String drama_name=jsonObject.getString("drama_name");
                                dramaInfo.setDrama_name(drama_name);
                                Log.d("","Test Doin drama_name "+drama_name);

                                String datetime=jsonObject.getString("datetime");
                                dramaInfo.setDatetime(datetime);

                                String photo_link=jsonObject.getString("photo_link");
                                dramaInfo.setLink_photo(photo_link);

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

                                KalravApplication.getInstance().getPrefs().hidepDialog(getContext());

                                if(dramaInfo!= null && dramaInfo.getId()!=0){
                                    String isFav=favouritesInfoDAOImpl.findfavBy(dramaInfo.getId());
                                    dramaInfo.setIsfav(isFav);
                                    dramaInfoDAO.addDrama(dramaInfo);

                                    dramaInfos= (ArrayList<DramaInfo>) dramaInfoDAO.getAllDrama();
                                    Log.d("","Test onPost dramaInfos.size "+dramaInfos.size());
                                    if (dramaInfos.size() > 0 & dramaInfos != null) {
                                        adapter=new MyAdapter(dramaInfos);
                                        myRecyclerView.setAdapter(adapter);
                                    }
                                }

                            }

                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("","JSonException Exception "+e);

                            Toast.makeText(getContext(),
                                    getString(R.string.no_data_available),
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("","Error: volly Exception " + error);
                Toast.makeText(getContext(),
                        getString(R.string.unknown_error),
                        Toast.LENGTH_LONG).show();
                KalravApplication.getInstance().getPrefs().hidepDialog(getContext());
            }
        });

        KalravApplication.getInstance().addToRequestQueue(req);

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
        return view;
    }



    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private ArrayList<DramaInfo> list;

        public MyAdapter(ArrayList<DramaInfo> Data) {
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
            if(dramaInfo[0].getIsfav()!=null && dramaInfo[0].getIsfav().equalsIgnoreCase("true")){
                holder.bookmarkImageView.setImageResource(R.mipmap.ic_like);

            }
            else{
                holder.bookmarkImageView.setImageResource(R.mipmap.ic_unlike);

            }

            // holder.coverImageView.setTag(list.get(position).getImageResourceId());
            holder.bookmarkImageView.setOnClickListener(new View.OnClickListener() {
                private boolean stateChanged;
                @Override
                public void onClick(View v) {
                    dramaInfo[0] =list.get(position);
                    Log.d("DramaFragment","Fragment draminfo id "+ dramaInfo[0].getId());
                    if(dramaInfo[0].getIsfav()==null || !dramaInfo[0].getIsfav().equalsIgnoreCase("true")){
                        holder.bookmarkImageView.setImageResource(R.mipmap.ic_like);
                        Toast.makeText(v.getContext(), "Added to Favourite ", Toast.LENGTH_SHORT).show();
                        if(dramaInfo[0] !=null && dramaInfo[0].getId()!=0){
                            favouritesInfo.setDrama_id(dramaInfo[0].getId());
                            favouritesInfo.setFav("true");

                            favouritesInfoDAOImpl.addFav(favouritesInfo);
                            favouritesInfos=favouritesInfoDAOImpl.getAllFav();
                            Log.d("DramaFragment","Fragment favouritesInfos size "+favouritesInfos.size());
                            dramaInfo[0].setIsfav("true");
                            dramaInfoDAO.updateDrama(dramaInfo[0]);

                        }
                    }
                    else {
                        favouritesInfo.setDrama_id(dramaInfo[0].getId());
                        int count= favouritesInfoDAOImpl.deleteFav(favouritesInfo);
                        Log.d("","Fragment  delete count"+count);
                        holder.bookmarkImageView.setImageResource(R.mipmap.ic_unlike);
                        dramaInfo[0].setIsfav("false");
                        dramaInfoDAO.updateDrama(dramaInfo[0]);
                        Toast.makeText(v.getContext(), "Remove from Favourite", Toast.LENGTH_SHORT).show();

                    }
                }
            });
            holder.textGroupname.setText(dramaInfo[0].getGroup_name());

            holder.shareImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 Uri bmpUri = getLocalBitmapUri(holder.coverImageView,dramaInfo[0]);

                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.putExtra(Intent.EXTRA_STREAM, bmpUri);
                    share.putExtra(Intent.EXTRA_TEXT,"Drama Details : \n Drama Name : "+dramaInfo[0].getDrama_name()
                                                        +" \n Drama Date : "+dramaInfo[0].getDatetime()+" Drama Time : "+dramaInfo[0].getTime()
                                                        +" \n Drama Group Name : "+dramaInfo[0].getGroup_name()+" Drama Language : "+dramaInfo[0].getDrama_language());
                    share.setType("image/jpeg");
                    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    getContext().startActivity(Intent.createChooser(share, "Share image File"));

                }
            });
        }

        private Uri getLocalBitmapUri(ImageView coverImageView , DramaInfo dramaInfo) {
            Log.d("","getLocalBitmapUri  coverImageView.getDrawable() "+ coverImageView.getDrawable());

            Drawable drawable = coverImageView.getDrawable();
            Bitmap bmp = null;
            if (drawable instanceof BitmapDrawable){
                Log.d("","getLocalBitmapUri  coverImageView.getDrawable()).getBitmap() "+((BitmapDrawable) coverImageView.getDrawable()).getBitmap());

                bmp = ((BitmapDrawable) coverImageView.getDrawable()).getBitmap();

            } else {
                return null;
            }
            Log.d("","getLocalBitmapUri  bmp "+ bmp);

            // Store image to default external storage directory
            Uri bmpUri = null;
            try {
                File file =  new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS), "share_image_" + dramaInfo.getDrama_name() + ".png");
                file.getParentFile().mkdirs();
                FileOutputStream out = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.close();
                Log.d("","getLocalBitmapUri  file "+ file);

                bmpUri = Uri.fromFile(file);
                Log.d("","getLocalBitmapUri  bmpUri "+ bmpUri);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmpUri;
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


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,  int[] grantResults) {
        switch (requestCode) {

            case 100:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    Log.d("","shareIt onRequestPermissionsResult bm "+bm);

//                    shareIt(bm);
                }
                break;

            default:
                break;
        }
    }
}


