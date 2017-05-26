package com.lognsys.kalrav.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lognsys.kalrav.R;
import com.lognsys.kalrav.model.DramaInfo;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import noman.weekcalendar.WeekCalendar;
import noman.weekcalendar.listener.OnDateClickListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AuditoriumListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AuditoriumListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AuditoriumListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    int dramaInfoId;



    private WeekCalendar weekCalendar;
    private OnFragmentInteractionListener mListener;

    public AuditoriumListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AuditoriumListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AuditoriumListFragment newInstance(String param1, String param2) {
        AuditoriumListFragment fragment = new AuditoriumListFragment();
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
        View view = inflater.inflate(R.layout.fragment_auditorium_list, container, false);
        dramaInfoId = (int) getArguments().getSerializable("dramaInfoId");
        // Inflate the layout for this fragment
        /*SimpleDateFormat curFormater = new SimpleDateFormat("EEE dd");
        GregorianCalendar date = new GregorianCalendar();
        String[] dateStringArray = new String[7];
        date.set(GregorianCalendar.WEEK_OF_MONTH, date.get(GregorianCalendar.DATE)-date.get(GregorianCalendar.DAY_OF_WEEK)+1);
        for (int day = 0; day < 7; day++) {
            dateStringArray[day] = curFormater.format(date.getTime());
            date.setFirstDayOfWeek(day);
            date.roll(Calendar.DAY_OF_MONTH, true);
            Log.d("","HELLO WORLD DAYS: "+dateStringArray[day]);
        }*/
        weekCalendar = (WeekCalendar)view.findViewById(R.id.weekCalendar);
        weekCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(DateTime dateTime) {
                Toast.makeText(getActivity(), "You Selected " + dateTime.toString(), Toast
                        .LENGTH_SHORT).show();
            }

        });
        /*weekCalendar.setOnWeekChangeListener(new OnWeekChangeListener() {
            @Override
            public void onWeekChange(DateTime firstDayOfTheWeek, boolean forward) {
                Toast.makeText(getActivity(), "Week changed: " + firstDayOfTheWeek +
                        " Forward: " + forward, Toast.LENGTH_SHORT).show();
            }
        });*/
        return view;
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
      /*  if (context instanceof OnFragmentInteractionListener) {
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
