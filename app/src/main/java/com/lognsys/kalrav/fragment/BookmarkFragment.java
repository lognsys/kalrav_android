package com.lognsys.kalrav.fragment;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lognsys.kalrav.R;
import com.lognsys.kalrav.db.DramaInfoDAOImpl;
import com.lognsys.kalrav.db.FavouritesInfoDAOImpl;
import com.lognsys.kalrav.model.DramaInfo;
import com.lognsys.kalrav.model.FavouritesInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class BookmarkFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView myRecyclerView;
    List<FavouritesInfo> favouritesInfos;
    ArrayList<DramaInfo> dramaInfos;
    FavouritesInfoDAOImpl favouritesInfoDAOimpl;
    DramaInfoDAOImpl dramaInfoDAOimpl;


//    public static BookmarkFragment newInstance(String param1, String param2) {
//        BookmarkFragment fragment = new BookmarkFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        initializeList(view);
        return view;


    }

    private void initializeList(View view) {
        try {
            myRecyclerView = (RecyclerView) view.findViewById(R.id.cardView);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            myRecyclerView.setLayoutManager(mLayoutManager);
            myRecyclerView.setItemAnimator(new DefaultItemAnimator());
            Log.d("","Test initializeList myRecyclerView ");

            favouritesInfoDAOimpl =new FavouritesInfoDAOImpl(getContext());
            favouritesInfos=favouritesInfoDAOimpl.getAllFav();

            dramaInfoDAOimpl =new DramaInfoDAOImpl(getContext());

            if(favouritesInfos!=null && favouritesInfos.size()>0){
                Log.d("","Test initializeList favouritesInfos size "+favouritesInfos.size());

//               for (int i=0;i<favouritesInfos.size();i++)
               {
//                   FavouritesInfo favouritesInfo=favouritesInfos.get(i);
//                    Log.d("","Test getDramaListByFavId favouritesInfo.getDrama_id() "+favouritesInfo.getDrama_id());

                    dramaInfos=  dramaInfoDAOimpl.getDramaListByFavId((ArrayList<FavouritesInfo>) favouritesInfos);
                    Log.d("","Test getDramaListByFavId dramaInfos "+dramaInfos);
                    Log.d("","Test getDramaListByFavId dramaInfos size "+dramaInfos.size());

                }
                if(dramaInfos!= null && dramaInfos.size()>0){
                    BookmarkFragment.MyAdapter adapter=new BookmarkFragment.MyAdapter(dramaInfos);
                    myRecyclerView.setAdapter(adapter);
                    myRecyclerView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            DramaInfo dramaInfo=(DramaInfo)view.getTag();
                            Fragment fragment = new FragmentDramaDetail();

                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

                        }
                    });

                }
            }
        }
        catch (Exception e){
            Log.d("","Test initializeList Exception "+ e);

        }
    }


    public class MyAdapter extends RecyclerView.Adapter<BookmarkFragment.MyAdapter.MyViewHolder> {
        private ArrayList<DramaInfo> list;

        public MyAdapter(ArrayList<DramaInfo> Data) {
            Log.d("", "MyAdapter constructore Data"+Data+"   Size ===" +Data.size());

            list = Data;
            Log.d("", "MyAdapter constructore list "+list+" list size ==="+list.size());

        }


        @Override
        public BookmarkFragment.MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {
            // create a new view
            Log.d("", "MyAdapter onCreateViewHolder ");
            View view=null;
            DramaInfo dramaInfo =list.get(position);

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.bookmarks_items, parent, false);
            BookmarkFragment.MyAdapter.MyViewHolder holder = new BookmarkFragment.MyAdapter.MyViewHolder(view);


            return holder;


        }

        @Override
        public void onBindViewHolder(final BookmarkFragment.MyAdapter.MyViewHolder holder, final int position) {
            Log.d("", "MyAdapter onBindViewHolder ");

            final DramaInfo[] dramaInfo = {list.get(position)};
            Picasso.with(getContext()).load(dramaInfo[0].getLink_photo()).into(holder.dramaImage);
            holder.textDramaName.setText(dramaInfo[0].getTitle());
            holder.tvDramaLength.setText(dramaInfo[0].getDrama_length());
            holder.textDramaDate.setText(dramaInfo[0].getDatetime());
            holder.textDramaTiming.setText(dramaInfo[0].getTime());
            holder.textDramaLanguage.setText(dramaInfo[0].getDrama_language());
            holder.textDramaGenre.setText(dramaInfo[0].getGenre());
            holder.textDramaName.setText(dramaInfo[0].getTitle());
            holder.textDramaGroupname.setText(dramaInfo[0].getGroup_name());

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public ImageView dramaImage;
            public TextView textDramaName;
            public TextView tvDramaLength;
            public TextView textDramaDate;
            public TextView textDramaTiming;
            public TextView textDramaLanguage;
            public TextView textDramaGenre;
            public TextView textDramaGroupname;


            public MyViewHolder(View itemView) {
                super(itemView);
                dramaImage = (ImageView) itemView.findViewById(R.id.dramaImage);
                textDramaName = (TextView) itemView.findViewById(R.id.textDramaName);
                tvDramaLength = (TextView) itemView.findViewById(R.id.tvDramaLength);
                textDramaDate = (TextView) itemView.findViewById(R.id.textDramaDate);
                textDramaTiming= (TextView) itemView.findViewById(R.id.textDramaTiming);
                textDramaLanguage= (TextView) itemView.findViewById(R.id.textDramaLanguage);
                textDramaGenre= (TextView) itemView.findViewById(R.id.textDramaGenre);
                textDramaGroupname= (TextView) itemView.findViewById(R.id.textDramaGroupname);

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




}
