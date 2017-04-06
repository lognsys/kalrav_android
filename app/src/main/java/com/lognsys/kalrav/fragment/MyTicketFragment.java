package com.lognsys.kalrav.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.lognsys.kalrav.R;

/**
 * Created by admin on 05-04-2017.
 */

public class MyTicketFragment extends Fragment {
    ImageView imageView;
    // Button button;
    //EditText editText;
    TextView tvDramaName, tvAuditorium, tvGroupName, tvDateAndTime, tvTicketNumber, tvDramaLanguage;
    String DramaName, Auditorium, GroupName, DateAndTime, TicketNumber, DramaLanguage;
    String EditTextValue;
    Thread thread;
    public final static int QRcodeWidth = 500;
    Bitmap bitmap;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_my_ticket, container, false);


        imageView = (ImageView) view.findViewById(R.id.imageView);
        tvDramaName = (TextView) view.findViewById(R.id.tvDramaName);
        tvAuditorium = (TextView) view.findViewById(R.id.tvAuditorium);
        tvGroupName = (TextView) view.findViewById(R.id.tvGroupName);
        tvDateAndTime = (TextView) view.findViewById(R.id.tvDramaTiming);
        tvTicketNumber = (TextView) view.findViewById(R.id.tvticketNumber);
        tvDramaLanguage = (TextView) view.findViewById(R.id.tvDramaLanguage);
        // editText = (EditText) view.findViewById(R.id.editText);
        //button = (Button) view.findViewById(R.id.button);

     /*   button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditTextValue = editText.getText().toString();

                try {
                    bitmap = TextToImageEncode(EditTextValue);

                    imageView.setImageBitmap(bitmap);

                } catch (WriterException e) {
                    e.printStackTrace();
                }

            }
        });*/
        DramaName = tvDramaName.getText().toString().trim();
        Auditorium = tvAuditorium.getText().toString().trim();
        GroupName = tvGroupName.getText().toString().trim();
        DateAndTime = tvDateAndTime.getText().toString().trim();
        TicketNumber = tvTicketNumber.getText().toString().trim();
        DramaLanguage = tvDramaLanguage.getText().toString().trim();
        try {
            bitmap = TextToImageEncode(DramaName+"\n\r"+Auditorium+"\n\r"+GroupName+"\n\r"+DateAndTime+"\n\r"+TicketNumber+"\n\r"+DramaLanguage);

            imageView.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
        return view;
    }

    Bitmap TextToImageEncode(String Value) throws WriterException {

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
        return bitmap;
    }

}
