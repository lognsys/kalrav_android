package com.lognsys.kalrav.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.lognsys.kalrav.FCM.FCMService;
import com.lognsys.kalrav.R;
import com.lognsys.kalrav.db.DramaInfoDAOImpl;
import com.lognsys.kalrav.db.BookingInfoDAOImpl;
import com.lognsys.kalrav.model.Auditorium;
import com.lognsys.kalrav.model.BookingInfo;
import com.lognsys.kalrav.model.DramaInfo;
import com.lognsys.kalrav.model.NotificationInfo;
import com.lognsys.kalrav.model.Ratings;
import com.lognsys.kalrav.model.TimeSlot;
import com.lognsys.kalrav.util.CallAPI;
import com.lognsys.kalrav.util.Constants;
import com.lognsys.kalrav.util.KalravApplication;
import com.lognsys.kalrav.util.PropertyReader;
import com.lognsys.kalrav.util.Services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import by.anatoldeveloper.hallscheme.hall.Seat;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ConfirmFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    //Properties
    private PropertyReader propertyReader;
    private Properties properties;
    public static final String PROPERTIES_FILENAME = "kalrav_android.properties";

    CallAPI callAPI;
    private OnFragmentInteractionListener mListener;

    private EditText editName;
    private EditText editGroupname;
    private EditText editemailid;
    private EditText editSeatNumber;
    private EditText editNoOfSeatsBooked;
    private EditText editTimeSlot;
    private EditText editAuditoriumName;
    private EditText editTotalPrice;

    private Button btnTicket;

    private TextInputLayout input_layout_name;
    private TextInputLayout input_layout_groupname;
    private TextInputLayout input_layout_emailid;
    private TextInputLayout input_layout_seat_numbers;
    private TextInputLayout input_layout_no_of_seats;
    private TextInputLayout input_layout_time_slot;
    private TextInputLayout input_layout_audi_name;
    private TextInputLayout input_layout_total_price;
    TimeSlot timeSlot;
    DramaInfo dramaInfo;
    String time,strDate;
    int dramaInfoId;
    Auditorium auditorium;
    double totalPrice;
    ImageView img;
    ArrayList<Seat> mSeats;
    String DramaName, Auditorium,TotalPrice, GroupName, DateAndTime, TicketNumber,BookingDateTime,UserName,TotalTicketBooked;
    Bitmap bitmapQRCode,bitmap;
    public final static int QRcodeWidth = 500;
    BookingInfoDAOImpl bookingInfoDAO;
    BookingInfo bookingInfo;
    DramaInfoDAOImpl dramaInfoDAO;
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
        dramaInfoDAO = new DramaInfoDAOImpl(getActivity());
        dramaInfoId = getArguments().getInt("dramaInfoId");
        dramaInfo= dramaInfoDAO.getDramaByDramaId(dramaInfoId);
        auditorium= (Auditorium) (getArguments().getSerializable("auditorium"));
        propertyReader = new PropertyReader(getActivity());
        properties = propertyReader.getMyProperties(PROPERTIES_FILENAME);

        bookingInfoDAO =new BookingInfoDAOImpl(getContext());
        bookingInfo =new BookingInfo();
        time=  getArguments().getString("time");
        strDate=  getArguments().getString("strDate");
        mSeats = (ArrayList<Seat>) getArguments().getSerializable("seats");
       populateView(view);
        return view;
    }

    private void populateView(View view) {
        callAPI=new CallAPI((AppCompatActivity) getActivity());
        input_layout_name=(TextInputLayout)view.findViewById(R.id.input_layout_name);
        input_layout_groupname=(TextInputLayout)view.findViewById(R.id.input_layout_groupname);
        input_layout_emailid=(TextInputLayout)view.findViewById(R.id.input_layout_emailid);
        input_layout_seat_numbers=(TextInputLayout)view.findViewById(R.id.input_layout_seat_numbers);
        input_layout_no_of_seats=(TextInputLayout)view.findViewById(R.id.input_layout_no_of_seats);
        input_layout_time_slot=(TextInputLayout)view.findViewById(R.id.input_layout_time_slot);
        input_layout_audi_name=(TextInputLayout)view.findViewById(R.id.input_layout_audi_name);
        input_layout_total_price=(TextInputLayout)view.findViewById(R.id.input_layout_total_price);

        editName=(EditText)view.findViewById(R.id.editName);
        editGroupname=(EditText)view.findViewById(R.id.editGroupname);
        editemailid=(EditText)view.findViewById(R.id.editemailid);
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

        editAuditoriumName.setText(auditorium.getAudiName());
        if(time!=null && strDate!=null){
            editTimeSlot.setText(strDate+" | "+time);
        }

        btnTicket=(Button) view.findViewById(R.id.btnTicket);
        btnTicket.setOnClickListener(this);

        if(mSeats!= null && mSeats.size()>0){
            int seatno=mSeats.size();
            Log.d("","size ===== "+mSeats.size());
            editNoOfSeatsBooked.setText(String.valueOf(seatno));
            StringBuilder stringBuilder=new StringBuilder();
            for (int i= 0;i<mSeats.size(); i++){
                Seat seat=mSeats.get(i);
                stringBuilder.append(seat.marker()+seat.selectedSeat()+" , ");
                totalPrice=seat.getTotal();
            }
            editSeatNumber.setText(stringBuilder.toString());
            if(totalPrice>0){
                editTotalPrice.setText(String.valueOf("Rs "+totalPrice));
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
                 AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                 builderSingle.setIcon(R.mipmap.ic_launcher);
                 builderSingle.setTitle("Booking Confirmation");
                 builderSingle.setMessage("Are you sure? you want to book drama.");
                 builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         dialog.dismiss();
                     }
                 });
                 builderSingle.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         if(dramaInfo != null /*&& timeSlot!= null*/){
//                             callAPI=new CallAPI((AppCompatActivity) getActivity());


                           bookingInfo.setDrama_id(dramaInfo.getId());
//                     monika added demo userid
                             bookingInfo.setUser_id(1);

                             DramaName = "Drama Name : "+dramaInfo.getTitle();
                             Auditorium = "Auditorium Name : " +editAuditoriumName.getText().toString();
                             GroupName = "Group Name : "+dramaInfo.getGroup_name();
                             DateAndTime = "Drama Date and Time : "+ dramaInfo.getDatetime()+" " +dramaInfo.getTime();
                             BookingDateTime ="Booking Date and Time :"+strDate+" " +time;
                             UserName="Booked by : "+ editName.getText().toString();
                             TicketNumber ="Ticket numbers :"+ editSeatNumber.getText().toString();
                             TotalTicketBooked ="Total Ticket numbers :"+editNoOfSeatsBooked.getText().toString();
                             TotalPrice="TotalPrice Rs : "+String.valueOf(totalPrice);
                             dialog.dismiss();
        /*                      KalravApplication.getInstance().getPrefs().showDialog(getContext());

                             new ConfirmFragment.GenerateQRCodeTask(DramaName,Auditorium,GroupName,DateAndTime,TicketNumber,BookingDateTime,UserName,
                                     TotalTicketBooked,TotalPrice, bitmapQRCode).execute();
*/
                            int totalNoTickets=Integer.parseInt(editNoOfSeatsBooked.getText().toString());
                            String bookedDT=strDate+" " +time;
                             Log.d("Confirm Fragment","bookedSeats dramaInfo "+dramaInfo +" getCustomer_id  = "+KalravApplication.getInstance().getPrefs().getCustomer_id() );
                             KalravApplication.getInstance().getPrefs().showDialog(getContext());
                             bookedSeats(dramaInfo,KalravApplication.getInstance().getPrefs().getCustomer_id(),
                                     bookedDT, editSeatNumber.getText().toString(),totalNoTickets,totalPrice,auditorium);
                         }
                     }
                 });
                 AlertDialog dialog = builderSingle.create();
                 dialog.show();

                 Button buttonNegative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                 Button buttonPositive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

                 if(buttonNegative != null) {
                     buttonNegative.setTextColor(Color.BLACK);
                 }if(buttonPositive != null) {
                     buttonPositive.setTextColor(Color.BLACK);
                 }
            }
        }
    }

    private void bookedSeats(final DramaInfo dramaInfo, String customer_id, final String bookingDateTime, String seatnumber, int totalTicketBooked, final double totalPrice, final Auditorium auditorium) {
        JSONObject params = new JSONObject();
        try {

            params.put("dramas_id", dramaInfo.getId());
            params.put("booking_date", bookingDateTime);
            params.put("users_id", Integer.parseInt(customer_id));
            params.put("auditoriums_id",Integer.parseInt(auditorium.getAudiId()));
            params.put("price", totalPrice);
            Log.d("Confirm Fragment","bookedSeats seatnumber "+seatnumber +" seatnumber  = "+seatnumber );
            JSONArray jsonArray =new JSONArray();

            if(seatnumber!=null){
                String [] seatsdetails=seatnumber.split(" , ");
                Log.d("Confirm Fragment","bookedSeats seatsdetails "+seatsdetails +" seatsdetails size = "+seatsdetails.length );
                for(int i =0;i<seatsdetails.length;i++){
                    jsonArray.put(seatsdetails[i]) ;
                }
            }
            params.put("seatnumber", jsonArray);
            params.put("no_of_seats",totalTicketBooked);
            Log.d("Confirm Fragment","bookedSeats params " +params);

        } catch (Exception e) {
            Log.d("Confirm Fragment","bookedSeats Exception "+e  );

            e.printStackTrace();
        }

        final String bookingconfirm=properties.getProperty(Constants.API_URL_BOOKING.bookingconfirm.name());

        final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,
                bookingconfirm, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try{
                            Log.d("Confirm Fragment","bookedSeats response " +response);
//
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            Log.d("Confirm Fragment","bookedSeats response " +response);
//                            {"uniqueCode":"MTU4MTIwMTctMDgtMTAgMTE6MjU6MDA=","bookingId":36}

                            BookingInfo bookingInfo=new BookingInfo();

/*
{"dramas_id":2,"booking_date":"2017-08-11 01:25:00","users_id":58,"auditoriums_id":1,"price":400,"seatnumber":["Z1","Z2"],"no_of_seats":2}
*/
                            try {
                                Bitmap bitmapQRCode = null;
                                File file = new File(getDataFolder(getActivity()), "QRCode.jpg");
                                Log.d("Confirm Fragment","bookedSeats GenerateQRCode file.exists() " +file.exists());
//
                                if (file.exists()) {
                                    //Do action
                                    File dataFile = new File(getDataFolder(getActivity()), "QRCode.jpg");
                                    try {
                                        InputStream fileInputStream = new FileInputStream(dataFile);
                                        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                                        bitmapOptions.inJustDecodeBounds = false;
                                        bitmapQRCode= BitmapFactory.decodeStream(fileInputStream);
                                        Log.d("GenerateQRCode"," GenerateQRCode doInBackground this.bitmapQRCode "+bitmapQRCode);


                                    } catch (FileNotFoundException fnf) {
                                        Log.d("GenerateQRCode"," GenerateQRCode doInBackground FileNotFoundException "+fnf);

                                    }

                                }
                                bookingInfo.set_id(jsonObject.getInt("bookingId"));
                                bookingInfo.setConfirmation_code(jsonObject.getString("uniqueCode"));
                                bookingInfo.setDrama_name(dramaInfo.getTitle());
                                bookingInfo.setDrama_id(dramaInfo.getId());
                                bookingInfo.setAuditorium_name(auditorium.getAudiName());
                                if (bitmapQRCode == null){
                                    Log.d("GenerateQRCode"," GenerateQRCode onPostExecute createBitmapOfQRCode() "+createBitmapOfQRCode());
                                    bitmapQRCode=createBitmapOfQRCode();
                                }
                                bookingInfo.setDrama_id(dramaInfo.getId());
                                bookingInfo.setUser_id(Integer.parseInt(KalravApplication.getInstance().getPrefs().getCustomer_id()));
//                                bookingInfo.setDrama_name(dramaInfo.getTitle());
                                bookingInfo.setDrama_group_name(dramaInfo.getGroup_name());
                                bookingInfo.setDrama_photo(dramaInfo.getLink_photo());
                                bookingInfo.setDrama_datetime(bookingDateTime);
                                String dateString=new Date().toString();
                                bookingInfo.setBooked_datetime(dateString);
                                bookingInfo.setSeats_total_price((totalPrice));
                                bookingInfo.setSeats_no_of_seats_booked(editNoOfSeatsBooked.getText().toString());
                                bookingInfo.setSeart_seat_no(editSeatNumber.getText().toString());
                                bookingInfo.setAuditorium_name(editAuditoriumName.getText().toString());
                                bookingInfo.setUser_name(editName.getText().toString());
                                if(bitmapQRCode!=null){
                                    Log.d("GenerateQRCode"," GenerateQRCode onPostExecute this.bitmapQRCode "+bitmapQRCode);
                                    bookingInfo.setBitmapQRCode(bitmapQRCode);
                                }
                                bookingInfo.setUser_emailid(editemailid.getText().toString());
                                Log.d("GenerateQRCode"," GenerateQRCode onPostExecute bookingInfo getBitmapQRCode "+ bookingInfo.getBitmapQRCode());
                                Toast.makeText(getContext(),"Your tickets are confirmed",Toast.LENGTH_SHORT).show();
                                bookingInfoDAO.addTicket(bookingInfo);
                                KalravApplication.getInstance().getPrefs().hidepDialog(getContext());
                                callFragment();
                            } catch (Exception e) {
                                Log.d("GenerateQRCode"," GenerateQRCode doInBackground Exception "+e);

                            }

                        }
                        catch (JSONException e){
                            Log.d("Confirm Fragment","bookedSeats JSONException " +e);
//
                        }

                        //  YOUR RESPONSE
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
//                Log.d("Response","Rest volleyError networkResponse.data " +volleyError.networkResponse.data);

                String json = null;
                String str=null;
                byte[] response=null;
                if(volleyError.networkResponse.data!=null)
                    response = volleyError.networkResponse.data;
                Log.d("Response","bookedSeats volleyError response " +response);
                try {
                    str = new String(response, "UTF-8");
                    Log.d("Response","bookedSeats volleyError str toString  " +str.toString() );

                    try {
                        JSONObject object=new JSONObject(str.toString());
                        Log.d("Response","exception inside object  " +object);

                        int  statusCode=object.getInt("statusCode");
                        Log.d("Response","Rest inside statusCode  " +statusCode);

                        if(statusCode==400){
                            String msg=object.getString("msg");
                            displayMessage(msg);
                        }
                        else if(statusCode==406){
                            String msg=object.getString("msg");
                            displayMessage(msg);
                        } else if(statusCode==404){
                            String msg=object.getString("msg");
                            displayMessage(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            //Somewhere that has access to a context
            public void displayMessage(String toastString){
                Log.d("Response","Rest volleyError toastString  " +toastString );

                Toast.makeText(getApplicationContext(), toastString, Toast.LENGTH_LONG).show();
            }


        });
        KalravApplication.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    //Register and inserting  user records
    private class GenerateQRCodeTask extends AsyncTask<Void, Bitmap, Bitmap> {
        String DramaName, Auditorium, GroupName, DateAndTime, TicketNumber,BookingDateTime,UserName,TotalTicketBooked,TotalPrice;
        ImageView imageView;
        String qrText;
        Bitmap bitmapQRCode;
       public GenerateQRCodeTask(String DramaName, String Auditorium, String GroupName, String DateAndTime,
                              String TicketNumber,String BookingDateTime,String UserName,
                              String TotalTicketBooked,String TotalPrice,Bitmap bitmapQRCode) {
            this.DramaName = DramaName;
            this.Auditorium = Auditorium;
            this.GroupName = GroupName;
            this.DateAndTime = DateAndTime;
            this.TicketNumber = TicketNumber;
            this.BookingDateTime = BookingDateTime;
            this.UserName = UserName;
            this.imageView=imageView;
            this.TotalTicketBooked=TotalTicketBooked;
            this.TotalPrice=TotalPrice;
            this.bitmapQRCode=bitmapQRCode;

        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            qrText = this.DramaName + "\n\r" + this.Auditorium + "\n\r" + this.GroupName +
                    "\n\r" + this.DateAndTime + "\n\r" + this.TicketNumber+ "\n\r" + this.BookingDateTime +
                    "\n\r" + this.UserName+ "\n\r" + this.TotalTicketBooked+ "\n\r" + this.TotalPrice;
            try {
                File file = new File(getDataFolder(getActivity()), "QRCode.jpg");
                if (file.exists()) {
                    //Do action
                    File dataFile = new File(getDataFolder(getActivity()), "QRCode.jpg");
                    try {
                        InputStream fileInputStream = new FileInputStream(dataFile);
                        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                        bitmapOptions.inJustDecodeBounds = false;
                        this.bitmapQRCode= BitmapFactory.decodeStream(fileInputStream);
                        Log.d("GenerateQRCode"," GenerateQRCode doInBackground this.bitmapQRCode "+this.bitmapQRCode);
                        return this.bitmapQRCode;


                    } catch (FileNotFoundException fnf) {
                        Log.d("GenerateQRCode"," GenerateQRCode doInBackground FileNotFoundException "+fnf);

                    }

                }

            } catch (Exception e) {
                Log.d("GenerateQRCode"," GenerateQRCode doInBackground Exception "+e);

            }
            return this.bitmapQRCode;
        }



        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
           try {
               qrText = this.DramaName + "\n\r" + this.Auditorium + "\n\r" + this.GroupName +
                       "\n\r" + this.DateAndTime + "\n\r" + this.TicketNumber + "\n\r" + this.BookingDateTime +
                       "\n\r" + this.UserName + "\n\r" + this.TotalTicketBooked + "\n\r" + this.TotalPrice;
               Log.d("GenerateQRCode"," GenerateQRCode onPostExecute this.bitmapQRCode "+this.bitmapQRCode);

               if (this.bitmapQRCode != null) {
                   if (detectBarCode(this.bitmapQRCode) != null && detectBarCode(this.bitmapQRCode).equals(qrText)) {
                       Log.d("GenerateQRCode"," GenerateQRCode onPostExecute inside if this.bitmapQRCode "+this.bitmapQRCode);

//                       this.imageView.setImageBitmap(bitmap);
                   } else {
                       //bitmap= TextToImageEncode(DramaName + "\n\r" + Auditorium + "\n\r" + GroupName + "\n\r" + DateAndTime + "\n\r" + TicketNumber + "\n\r" + DramaLanguage);
//                       this.imageView.setImageBitmap(createBitmapOfQRCode());
                       Log.d("GenerateQRCode"," GenerateQRCode onPostExecute inside else createBitmapOfQRCode()"+createBitmapOfQRCode());

                   }
               } else {
                   Log.d("GenerateQRCode"," GenerateQRCode onPostExecute createBitmapOfQRCode() "+createBitmapOfQRCode());
                   this.bitmapQRCode=createBitmapOfQRCode();
               }
               bookingInfo.setDrama_id(dramaInfo.getId());
//                     monika added demo userid
               bookingInfo.setUser_id(1);
               bookingInfo.setDrama_name(dramaInfo.getTitle());
               bookingInfo.setDrama_group_name(dramaInfo.getGroup_name());
               bookingInfo.setDrama_photo(dramaInfo.getLink_photo());
//               bookingInfo.setDrama_date(dramaInfo.getDatetime());
//               bookingInfo.setDrama_time(dramaInfo.getTime());
//               bookingInfo.setBooked_time(time);
//               bookingInfo.setBooked_date(strDate);
               String confirmationCode = UUID.randomUUID().toString();

               bookingInfo.setConfirmation_code(confirmationCode);
               bookingInfo.setSeats_total_price((totalPrice));
               bookingInfo.setSeats_no_of_seats_booked(editNoOfSeatsBooked.getText().toString());
               bookingInfo.setSeart_seat_no(editSeatNumber.getText().toString());
               bookingInfo.setAuditorium_name(editAuditoriumName.getText().toString());
               bookingInfo.setUser_name(editName.getText().toString());
               if(this.bitmapQRCode!=null){
                   Log.d("GenerateQRCode"," GenerateQRCode onPostExecute this.bitmapQRCode "+this.bitmapQRCode);
                   bookingInfo.setBitmapQRCode(this.bitmapQRCode);
               }
               bookingInfo.setUser_emailid(editemailid.getText().toString());
               Log.d("GenerateQRCode"," GenerateQRCode onPostExecute bookingInfo getBitmapQRCode "+ bookingInfo.getBitmapQRCode());
               Toast.makeText(getContext(),"Your tickets are confirmed",Toast.LENGTH_SHORT).show();
               bookingInfoDAO.addTicket(bookingInfo);
               FCMService fcmService=new FCMService();
               NotificationInfo notificationInfo=new NotificationInfo();
               notificationInfo.setMessage("Your booking is confirm by Booking id : "+bookingInfo.get_id() +"and Your Confirmation code :"+bookingInfo.getConfirmation_code());
               fcmService.generateNotification(notificationInfo);
               KalravApplication.getInstance().getPrefs().hidepDialog(getContext());
               callFragment();


           }catch (Exception e){
               Log.d("GenerateQRCode"," GenerateQRCode onPostExecute Exception "+e);


           }
        }

        public void callFragment() {
            Fragment fragment = new DramaFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

        }


    }


    public void callFragment() {
        Fragment fragment = new DramaFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

    }

    public Bitmap createBitmapOfQRCode() {
       Log.d("","createBitmapOfQRCode getDataFoldergetContext() "+getDataFolder(getContext()));
        File cacheDir = getDataFolder(getContext());
        Log.d("","createBitmapOfQRCode cacheDir "+cacheDir);
        File cacheFile = new File(cacheDir, "QRCode.jpg");
        Log.d("","createBitmapOfQRCode cacheFile "+cacheFile);
        try {
            bitmap = TextToImageEncode(DramaName + "\n\r" + Auditorium + "\n\r" + GroupName + "\n\r" + DateAndTime + "\n\r" + TicketNumber +
                    "\n\r" + BookingDateTime+ "\n\r" +UserName+ "\n\r" + TotalTicketBooked + "\n\r" +TotalPrice);
            Log.d("","createBitmapOfQRCode  bitmap "+bitmap);

            FileOutputStream outputStream = null;
            if(cacheFile!=null) {
                outputStream = new FileOutputStream(cacheFile);
                Log.d("", "createBitmapOfQRCode else outputStream " + outputStream);
            }
            else{
                 cacheDir = getDataFolder(getContext());
                Log.d("","createBitmapOfQRCode else cacheDir "+cacheDir);
                 cacheFile = new File(cacheDir, "QRCode.jpg");
                outputStream = new FileOutputStream(cacheFile);
                Log.d("","createBitmapOfQRCode else cacheFile "+cacheFile);
                Log.d("","createBitmapOfQRCode else outputStream "+outputStream);

            }
            byte buffer[] = new byte[1024];
            int loadedSize = 0;

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            Log.d("", "createBitmapOfQRCode  bitmap " + bitmap);

//            imageView.setImageBitmap(bitmap);
            outputStream.close();
        } catch (Exception e) {
            Log.d("", "createBitmapOfQRCode  Exception " + e);
        }
        return bitmap;
    }

    Bitmap TextToImageEncode(String Value) throws WriterException {
        ProgressDialog progress = ProgressDialog.show(getActivity(), "QR Code",
                "Please wait", true);
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        progress.dismiss();
        return bitmap;
    }

    public File getDataFolder(Context context) {
        File dataDir = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            dataDir = new File(Environment.getExternalStorageDirectory(), "/kalrav_android");
            if (!dataDir.isDirectory()) {
                dataDir.mkdirs();
            }
        }

        if (!dataDir.isDirectory()) {
            dataDir = context.getFilesDir();
        }
        return dataDir;
    }

    public String detectBarCode(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        int[] intArray = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(intArray, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        LuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), intArray);
        Reader reader = new QRCodeReader();
        try {
            Result result = reader.decode(new BinaryBitmap(new HybridBinarizer(source)));

            return result.getText();
        } catch (NotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (ChecksumException e) {
            e.printStackTrace();
            return null;
        } catch (FormatException e) {
            e.printStackTrace();
            return null;
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
