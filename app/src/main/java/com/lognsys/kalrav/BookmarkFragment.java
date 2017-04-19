package com.lognsys.kalrav;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lognsys.kalrav.db.DramaInfoDAOImpl;
import com.lognsys.kalrav.db.FavouritesInfoDAOImpl;
import com.lognsys.kalrav.model.DramaInfo;
import com.lognsys.kalrav.model.FavouritesInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookmarkFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookmarkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookmarkFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView myRecyclerView;
    private OnFragmentInteractionListener mListener;
    List<FavouritesInfo> favouritesInfos;
    List<DramaInfo> dramaInfos;
    FavouritesInfoDAOImpl favouritesInfoDAOimpl;
    DramaInfoDAOImpl dramaInfoDAOimpl;
    public BookmarkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookmarkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookmarkFragment newInstance(String param1, String param2) {
        BookmarkFragment fragment = new BookmarkFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        myRecyclerView = (RecyclerView) view.findViewById(R.id.cardView);
        myRecyclerView.setHasFixedSize(true);
        initializeList();
        return view;


    }

    private void initializeList() {
        try {
            favouritesInfoDAOimpl =new FavouritesInfoDAOImpl(getContext());
            favouritesInfos=favouritesInfoDAOimpl.getAllFav();
            dramaInfoDAOimpl =new DramaInfoDAOImpl(getContext());

            if(favouritesInfos!=null && favouritesInfos.size()>0){
                Log.d("","Test initializeList favouritesInfos "+favouritesInfos);
                Log.d("","Test initializeList favouritesInfos size "+favouritesInfos.size());



                for (FavouritesInfo favouritesInfo: favouritesInfos) {
                    Log.d("","Test getDramaListByFavId favouritesInfo.getDrama_id() "+favouritesInfo.getDrama_id());

                    dramaInfos=dramaInfoDAOimpl.getDramaListByFavId(favouritesInfo.getDrama_id());
                    Log.d("","Test getDramaListByFavId dramaInfos "+dramaInfos);
                    Log.d("","Test getDramaListByFavId dramaInfos size "+dramaInfos.size());

                }
            }
        }
        catch (Exception e){
            Log.d("","Test initializeList Exception "+ e);

        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
