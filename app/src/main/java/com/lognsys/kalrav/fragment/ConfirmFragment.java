package com.lognsys.kalrav.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lognsys.kalrav.R;
import com.lognsys.kalrav.db.DramaInfoDAOImpl;
import com.lognsys.kalrav.db.SQLiteHelper;
import com.lognsys.kalrav.db.TicketInfoDAOImpl;
import com.lognsys.kalrav.model.DramaInfo;
import com.lognsys.kalrav.model.TicketsInfo;
import com.lognsys.kalrav.model.TimeSlot;
import com.lognsys.kalrav.util.KalravApplication;
import com.lognsys.kalrav.util.Services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConfirmFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private EditText editName;
    private EditText editGroupname;
    private EditText editemailid;
    private EditText editConfirmationCode;
    private EditText editSeatNumber;
    private EditText editNoOfSeatsBooked;
    private EditText editTimeSlot;
    private EditText editAuditoriumName;
    private EditText editTotalPrice;

    private Button btnTicket;

    private TextInputLayout input_layout_name;
    private TextInputLayout input_layout_groupname;
    private TextInputLayout input_layout_emailid;
    private TextInputLayout input_layout_confirmationcode;
    private TextInputLayout input_layout_seat_numbers;
    private TextInputLayout input_layout_no_of_seats;
    private TextInputLayout input_layout_time_slot;
    private TextInputLayout input_layout_audi_name;
    private TextInputLayout input_layout_total_price;
    TimeSlot timeSlot;
    DramaInfo dramaInfo;
    int totalPrice;
    ArrayList<String> mSeats;

    ArrayList<TicketsInfo> dramaInfos;
    TicketInfoDAOImpl ticketInfoDAO;
    TicketsInfo ticketsInfo;
    public ConfirmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfirmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfirmFragment newInstance(String param1, String param2) {
        ConfirmFragment fragment = new ConfirmFragment();
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
        final View view = inflater.inflate(R.layout.fragment_confirm, container, false);

        totalPrice = getArguments().getInt("totalPrice");
        timeSlot= (TimeSlot) getArguments().getSerializable("timeSlot");
        dramaInfo= (DramaInfo) getArguments().getSerializable("dramaInfo");
        ticketInfoDAO =new TicketInfoDAOImpl(getContext());
        ticketsInfo=new TicketsInfo();
        Log.d("","Dialog onItemClick timeSlot "+timeSlot);
        Log.d("","Dialog onItemClick dramaInfo "+dramaInfo);

        mSeats = getArguments().getStringArrayList("seats");
       populateView(view);
        return view;
    }

    private void populateView(View view) {
        input_layout_name=(TextInputLayout)view.findViewById(R.id.input_layout_name);
        input_layout_groupname=(TextInputLayout)view.findViewById(R.id.input_layout_groupname);
        input_layout_emailid=(TextInputLayout)view.findViewById(R.id.input_layout_emailid);
        input_layout_confirmationcode=(TextInputLayout)view.findViewById(R.id.input_layout_confirmationcode);
        input_layout_seat_numbers=(TextInputLayout)view.findViewById(R.id.input_layout_seat_numbers);
        input_layout_no_of_seats=(TextInputLayout)view.findViewById(R.id.input_layout_no_of_seats);
        input_layout_time_slot=(TextInputLayout)view.findViewById(R.id.input_layout_time_slot);
        input_layout_audi_name=(TextInputLayout)view.findViewById(R.id.input_layout_audi_name);
        input_layout_total_price=(TextInputLayout)view.findViewById(R.id.input_layout_total_price);

        editName=(EditText)view.findViewById(R.id.editName);
        editGroupname=(EditText)view.findViewById(R.id.editGroupname);
        editemailid=(EditText)view.findViewById(R.id.editemailid);
        editConfirmationCode=(EditText)view.findViewById(R.id.editConfirmationCode);
        editSeatNumber=(EditText)view.findViewById(R.id.editSeatNumber);
        editNoOfSeatsBooked=(EditText)view.findViewById(R.id.editNoOfSeatsBooked);
        editTimeSlot=(EditText)view.findViewById(R.id.editTimeSlot);
        editAuditoriumName=(EditText)view.findViewById(R.id.editAuditoriumName);
        editTotalPrice=(EditText)view.findViewById(R.id.editTotalPrice);
    if(dramaInfo!=null ){
        editGroupname.setText(dramaInfo.getGroup_name());
    }
        if(KalravApplication.getInstance().getPrefs().getName()!=null){
            editName.setText(KalravApplication.getInstance().getPrefs().getName());
        }
        if(KalravApplication.getInstance().getPrefs().getEmail()!=null){
            editemailid.setText(KalravApplication.getInstance().getPrefs().getEmail());
        }

        editAuditoriumName.setText("Kalrav");
        if(dramaInfo!=null && timeSlot!=null){
            editTimeSlot.setText(timeSlot.getDateSlot()+" | "+timeSlot.getTimeSlot());
        }

        btnTicket=(Button) view.findViewById(R.id.btnTicket);
        btnTicket.setOnClickListener(this);
        if(totalPrice>0){
            editTotalPrice.setText(String.valueOf("Rs "+totalPrice));
        }
        if(mSeats!= null && mSeats.size()>0){
            int seatno=mSeats.size();
            Log.d("","size ===== "+mSeats.size());
            editNoOfSeatsBooked.setText(String.valueOf(seatno));
            StringBuilder stringBuilder=new StringBuilder();
            for (int i= 0;i<mSeats.size(); i++){
                String seat=mSeats.get(i);
                stringBuilder.append(seat+" , ");
            }
            editSeatNumber.setText(stringBuilder.toString());
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

    @Override
    public void onClick(View v) {
        if (editName.getText().toString().length() == 0) {
            input_layout_name.setError(getString(R.string.hint_name));
            editName.requestFocus();
        } else {

             if (editemailid.getText().toString().length() == 0) {
                input_layout_emailid.setError(getString(R.string.hint_email));
                editemailid.requestFocus();
            } else if (!Services.isEmailValid(editemailid.getText().toString())) {
                input_layout_emailid.setError(getString(R.string.error_emailid));
                editemailid.requestFocus();
             }
            else if (Services.isEmpty(editSeatNumber.getText().toString())) {
                input_layout_seat_numbers.setError(getString(R.string.error_seats));
                editSeatNumber.requestFocus();
//                input_layout_confirmationcode.setErrorEnabled(false);
            }   else if (Services.isEmpty(editNoOfSeatsBooked.getText().toString())) {
                input_layout_no_of_seats.setError(getString(R.string.error_seats));
                input_layout_seat_numbers.setErrorEnabled(false);
                editNoOfSeatsBooked.requestFocus();
            }   else if (Services.isEmpty(editTimeSlot.getText().toString())) {
                input_layout_time_slot.setError(getString(R.string.error_timeslot));
                editTimeSlot.requestFocus();
                input_layout_no_of_seats.setErrorEnabled(false);
            }   else if (Services.isEmpty(editAuditoriumName.getText().toString())) {
                input_layout_audi_name.setError(getString(R.string.error_auditorium));
                editAuditoriumName.requestFocus();
                input_layout_time_slot.setErrorEnabled(false);
            }   else if (Services.isEmpty(editTotalPrice.getText().toString())) {
                input_layout_total_price.setError(getString(R.string.error_total_price));
                editTotalPrice.requestFocus();
                input_layout_audi_name.setErrorEnabled(false);
            }
            else {
                input_layout_total_price.setErrorEnabled(false);
                 if(dramaInfo != null && timeSlot!= null){


                     ticketsInfo.setDrama_id(dramaInfo.getId());
//                     monika added demo userid
                     ticketsInfo.setUser_id(1);
                     ticketsInfo.setDrama_name(dramaInfo.getDrama_name());
                     ticketsInfo.setDrama_group_name(dramaInfo.getGroup_name());
                     ticketsInfo.setDrama_photo(dramaInfo.getLink_photo());
                     ticketsInfo.setDrama_date(dramaInfo.getDatetime());
                     ticketsInfo.setDrama_time(dramaInfo.getTime());
                     ticketsInfo.setBooked_time(timeSlot.getTimeSlot());
                     ticketsInfo.setBooked_date(timeSlot.getDateSlot());
                     ticketsInfo.setConfirmation_code("XII123");
                     ticketsInfo.setSeats_total_price(String.valueOf(totalPrice));
                     ticketsInfo.setSeats_no_of_seats_booked(editNoOfSeatsBooked.getText().toString());
                     ticketsInfo.setSeart_seat_no(editSeatNumber.getText().toString());
                     ticketsInfo.setAuditorium_name(editAuditoriumName.getText().toString());
                     ticketsInfo.setUser_name(editName.getText().toString());
                     ticketsInfo.setUser_emailid(editemailid.getText().toString());

                     ticketInfoDAO.addTicket(ticketsInfo);


                 }

//                ticketsInfo.setDrmama_image("");
                Fragment fragment = new MyTicketFragment();
                Bundle args = new Bundle();
                args.putSerializable("ticketsInfo",  ticketsInfo);
                fragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
//
            }
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
