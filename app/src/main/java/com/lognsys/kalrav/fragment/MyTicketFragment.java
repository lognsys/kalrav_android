package com.lognsys.kalrav.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.lognsys.kalrav.R;
import com.lognsys.kalrav.db.BookingInfoDAOImpl;
import com.lognsys.kalrav.model.BookingInfo;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by admin on 05-04-2017.
 */

public class MyTicketFragment extends Fragment {
    ImageView imageView,dramaImage;
    // Button button;
    //EditText editText;
    TextView tvDramaName, tvAuditorium, textGroupName, textDramaTiming,textUserName,textTotalnoofticket,textTotalprice,
            tvTicketNumber, tvDramaLanguage,textDramaDate,textBookingDate,textBookingTime;
    String DramaName, Auditorium,TotalPrice, GroupName, DateAndTime, TicketNumber,BookingDateTime,UserName,TotalTicketBooked;
    String EditTextValue;
    Thread thread;
    public final static int QRcodeWidth = 500;
    Bitmap bitmap;
    BookingInfo bookingInfo;
    BookingInfoDAOImpl ticketInfoDAO;
    ArrayList<BookingInfo> ticketInfos;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_my_ticket, container, false);
//        bookingInfo=(BookingInfo) getArguments().getSerializable("bookingInfo");
        ticketInfoDAO=new BookingInfoDAOImpl(getContext());
        ticketInfos=ticketInfoDAO.getAllTicket();

        dramaImage = (ImageView) view.findViewById(R.id.dramaImage);

        imageView = (ImageView) view.findViewById(R.id.imageView);
        tvDramaName = (TextView) view.findViewById(R.id.tvDramaName);

        tvAuditorium = (TextView) view.findViewById(R.id.tvAuditorium);
        textGroupName = (TextView) view.findViewById(R.id.textGroupName);

        textDramaDate = (TextView) view.findViewById(R.id.textDramaDate);
        textDramaTiming = (TextView) view.findViewById(R.id.textDramaTiming);

        textBookingDate = (TextView) view.findViewById(R.id.textBookingDate);
        textBookingTime = (TextView) view.findViewById(R.id.textBookingTime);

        textUserName = (TextView) view.findViewById(R.id.textUserName);

        tvTicketNumber = (TextView) view.findViewById(R.id.tvticketNumber);
        textTotalnoofticket = (TextView) view.findViewById(R.id.textTotalnoofticket);
        textTotalprice    = (TextView) view.findViewById(R.id.textTotalprice);


        tvDramaLanguage = (TextView) view.findViewById(R.id.tvDramaLanguage);
       if(ticketInfos!=null) {
           Log.d("","Text ticketInfos.size() ===== "+ticketInfos.size());
           for (int i = 0; i < ticketInfos.size(); i++) {
                bookingInfo = ticketInfos.get(i);
                if (bookingInfo != null) {
                    tvDramaName.setText(bookingInfo.getDrama_name());
                    textDramaDate.setText(bookingInfo.getDrama_datetime());
//                    textDramaTiming.setText(bookingInfo.getDrama_time());
                    textBookingDate.setText(bookingInfo.getBooked_datetime());
//                    textBookingTime.setText(bookingInfo.getBooked_time());
                    textUserName.setText(bookingInfo.getUser_name());
                    tvAuditorium.setText(bookingInfo.getAuditorium_name());
                    textGroupName.setText(bookingInfo.getDrama_name());
                    textTotalnoofticket.setText(bookingInfo.getSeats_no_of_seats_booked());
                    tvTicketNumber.setText(bookingInfo.getSeart_seat_no());
                    textTotalprice.setText((int) bookingInfo.getSeats_total_price());
                    if (bookingInfo.getDrama_photo() != null) {
                        Picasso.with(getContext()).load(bookingInfo.getDrama_photo()).into(dramaImage);
                    } else {
                        Picasso.with(getContext()).load(String.valueOf(getResources().getDrawable(R.drawable.stub, null))).into(dramaImage);
                    }
                    Log.d("", "Text bookingInfo.getBitmat ===== " + bookingInfo.getBitmapQRCode());

                    if (bookingInfo.getBitmapQRCode() != null) {
                        imageView.setImageBitmap(bookingInfo.getBitmapQRCode());
                    }
                }
            }
        }
        DramaName = "Drama Name : "+tvDramaName.getText().toString().trim();
        Auditorium = "Auditorium Name" +tvAuditorium.getText().toString().trim();
        GroupName = "Group Name : "+textGroupName.getText().toString().trim();
        DateAndTime = "Drama Date and Time : "+ textDramaDate.getText().toString().trim()+" " +textDramaTiming.getText().toString();
        BookingDateTime ="Booking Date and Time :"+ textBookingDate.getText().toString().trim()+" " +textBookingTime.getText().toString();
        UserName="Booked by : "+ textUserName.getText().toString().trim();
        TicketNumber ="Ticket numbers :"+ tvTicketNumber.getText().toString().trim();
        TotalTicketBooked ="Total Ticket numbers :"+ textTotalnoofticket.getText().toString().trim();
        TotalPrice="TotalPrice Rs : "+ textTotalprice.getText().toString();
//        new RegisteredTask(DramaName,Auditorium,GroupName,DateAndTime,TicketNumber,BookingDateTime,UserName,
//                imageView,TotalTicketBooked,TotalPrice).execute();

        return view;
    }


    //Register and inserting  user records
    private class RegisteredTask extends AsyncTask<Void, Bitmap, Bitmap> {
        String DramaName, Auditorium, GroupName, DateAndTime, TicketNumber,BookingDateTime,UserName,TotalTicketBooked,TotalPrice;
        ImageView imageView;
        String qrText;
        ProgressDialog dialog;

        public RegisteredTask(String DramaName, String Auditorium, String GroupName, String DateAndTime,
                              String TicketNumber,String BookingDateTime,String UserName,ImageView imageView,
                              String TotalTicketBooked,String TotalPrice) {
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
                        Bitmap Bitmap = BitmapFactory.decodeStream(fileInputStream);
                        return  Bitmap;

                    } catch (FileNotFoundException fnf) {
                        fnf.printStackTrace();
                    }

                } else {
              /*  bitmap = TextToImageEncode(DramaName + "\n\r" + Auditorium + "\n\r" + GroupName + "\n\r" + DateAndTime + "\n\r" + TicketNumber + "\n\r" + DramaLanguage);
                File cacheDir = getDataFolder(getContext());
                File cacheFile = new File(cacheDir, "localFileName.jpg");
                try {
                    FileOutputStream outputStream = new FileOutputStream(cacheFile);
                    byte buffer[] = new byte[1024];
                    int loadedSize = 0;

                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);


                    imageView.setImageBitmap(bitmap);
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
//                    imageView.setImageBitmap(createBitmapOfQRCode());
                }


                // bitmap = TextToImageEncode(DramaName+"\n\r"+Auditorium+"\n\r"+GroupName+"\n\r"+DateAndTime+"\n\r"+TicketNumber+"\n\r"+DramaLanguage);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            dialog.dismiss();
            qrText = this.DramaName + "\n\r" + this.Auditorium + "\n\r" + this.GroupName +
                    "\n\r" + this.DateAndTime + "\n\r" + this.TicketNumber + "\n\r" + this.BookingDateTime+
                    "\n\r" + this.UserName+ "\n\r" + this.TotalTicketBooked+ "\n\r" + this.TotalPrice;
          if(bitmap!=null) {
              if (detectBarCode(bitmap) !=null && detectBarCode(bitmap).equals(qrText)) {
                  this.imageView.setImageBitmap(bitmap);
              } else {
                  //bitmap= TextToImageEncode(DramaName + "\n\r" + Auditorium + "\n\r" + GroupName + "\n\r" + DateAndTime + "\n\r" + TicketNumber + "\n\r" + DramaLanguage);
                  this.imageView.setImageBitmap(createBitmapOfQRCode());
              }
          }
          else{
              this.imageView.setImageBitmap(createBitmapOfQRCode());

          }
//            Intent i = new Intent(RegisterActivity.this, HomeActivity.class);
//            i.putExtra("groupname", this.groupname);
//            startActivity(i);
//            finish();

        }
    }



    public Bitmap createBitmapOfQRCode() {
        File cacheDir = getDataFolder(getContext());
        File cacheFile = new File(cacheDir, "QRCode.jpg");
        try {
            bitmap = TextToImageEncode(DramaName + "\n\r" + Auditorium + "\n\r" + GroupName + "\n\r" + DateAndTime + "\n\r" + TicketNumber +
                    "\n\r" + BookingDateTime+ "\n\r" +UserName+ "\n\r" + TotalTicketBooked + "\n\r" +TotalPrice);

            FileOutputStream outputStream = new FileOutputStream(cacheFile);
            byte buffer[] = new byte[1024];
            int loadedSize = 0;

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);


            imageView.setImageBitmap(bitmap);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
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
}