package com.lognsys.kalrav.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.lognsys.kalrav.R;
import com.lognsys.kalrav.model.DramaInfo;
import com.lognsys.kalrav.util.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;


public class DramaFragment extends Fragment {
    ArrayList<DramaInfo> listitems = new ArrayList<>();
    RecyclerView myRecyclerView;
    static Bitmap bm;
    AdView mAdView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeList();
        getActivity().setTitle("Kalrav Arts");
    }

    public void initializeList() {
        listitems.clear();

        for (int i = 0; i < 3; i++) {

            DramaInfo item = new DramaInfo();
            item.setDrama_name(Constants.dramaNames[i]);
            item.setImageResourceId(Constants.dramaImages[i]);
            item.setGroup_name(Constants.dramaGroupNames[i]);
            listitems.add(item);

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

        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (listitems.size() > 0 & myRecyclerView != null) {
            myRecyclerView.setAdapter(new MyAdapter(listitems));
        }
        myRecyclerView.setLayoutManager(MyLayoutManager);
        myRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new FragmentDramaDetail();


                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

            }
        });

        return view;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public ImageView coverImageView;
        public ImageView likeImageView;
        public ImageView shareImageView;
        public TextView textGroupname;

        public MyViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            coverImageView = (ImageView) v.findViewById(R.id.coverImageView);
            likeImageView = (ImageView) v.findViewById(R.id.likeImageView);
            shareImageView = (ImageView) v.findViewById(R.id.shareImageView);
            textGroupname= (TextView) v.findViewById(R.id.textGroupname);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = new FragmentDramaDetail();


                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                    Log.i("RecyclerView Item ", String.valueOf(getLayoutPosition()));

                }
            });
        }
    }


    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private ArrayList<DramaInfo> list;

        public MyAdapter(ArrayList<DramaInfo> Data) {
            list = Data;
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {
            // create a new view
            View view=null;

                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycle_items, parent, false);
                MyViewHolder holder = new MyViewHolder(view);


                return holder;


        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            holder.titleTextView.setText(list.get(position).getDrama_name());
            holder.coverImageView.setImageResource(list.get(position).getImageResourceId());
            // holder.coverImageView.setTag(list.get(position).getImageResourceId());
            holder.likeImageView.setTag(R.drawable.ic_like);
            holder.textGroupname.setText(list.get(position).getGroup_name());

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

   /* private boolean isPackageInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }*/

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


