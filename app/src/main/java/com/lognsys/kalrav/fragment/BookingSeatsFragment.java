package com.lognsys.kalrav.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lognsys.kalrav.R;
import com.lognsys.kalrav.adapter.MyRecyclerViewAdapter;
import com.lognsys.kalrav.model.TimeSlot;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookingSeatsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookingSeatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookingSeatsFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TimeSlot timeSlot;
    private OnFragmentInteractionListener mListener;

    public BookingSeatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookingSeatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookingSeatsFragment newInstance(String param1, String param2) {
        BookingSeatsFragment fragment = new BookingSeatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    MyRecyclerViewAdapter adapterFirstrow,adapterLastrow,adapterMiddlerow;
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
        View view = inflater.inflate(R.layout.fragment_booking_seats, container, false);
        // data to populate the RecyclerView with
        String[] data = {"A","1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "B","1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "C","1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "D","1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "E","1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "F","1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "G","1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "H","1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        timeSlot= (TimeSlot) getArguments().getSerializable("timeSlot");
        Log.d("","Dialog onItemClick timeSlot "+timeSlot);
        Log.d("","Dialog onItemClick timeSlot getDramaId "+timeSlot.getDramaId());
        Log.d("","Dialog onItemClick timeSlot getDateSlot "+timeSlot.getDateSlot());
        Log.d("","Dialog onItemClick timeSlot getTimeSlot "+timeSlot.getTimeSlot());


        RecyclerView recycleLastRow = (RecyclerView)view.findViewById(R.id.recycleLastRow);
//      setup price
        TextView texttimeDate = (TextView)view.findViewById(R.id.texttimeDate);
        TextView textLastRowPrice = (TextView)view.findViewById(R.id.textLastRowPrice);
        texttimeDate.setText(timeSlot.getDateSlot() +" | "+timeSlot.getTimeSlot());
        TextView confirm = (TextView)view.findViewById(R.id.confirm);

        int numberOfColumns = 11;
        int lastRowPrice = Integer.parseInt(textLastRowPrice.getText().toString());
        adapterLastrow = new MyRecyclerViewAdapter(getActivity(), data,lastRowPrice,confirm);
        recycleLastRow.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        recycleLastRow.setAdapter(adapterLastrow);


        return view ;

    }
    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.top = space;
            } else {
                outRect.top = 0;
            }
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
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
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
