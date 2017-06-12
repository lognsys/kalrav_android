package com.lognsys.kalrav.schemes;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lognsys.kalrav.R;
import com.lognsys.kalrav.model.SeatExample;

import by.anatoldeveloper.hallscheme.hall.HallScheme;
import by.anatoldeveloper.hallscheme.hall.ScenePosition;
import by.anatoldeveloper.hallscheme.hall.Seat;
import by.anatoldeveloper.hallscheme.hall.SeatListener;
import by.anatoldeveloper.hallscheme.view.ZoomableImageView;

/**
 * Created by Nublo on 06.12.2015.
 * Copyright Nublo
 */
public class SchemeBhaidasFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.basic_scheme_fragment, container, false);
        ZoomableImageView imageView = (ZoomableImageView) rootView.findViewById(R.id.zoomable_image);
        HallScheme scheme = new HallScheme(imageView, basicScheme(), getActivity());
        scheme.setSceneName(getString(R.string.all_eye_here));
        scheme.setScenePosition(ScenePosition.SOUTH);
        return rootView;
    }

    public Seat[][] basicScheme() {//[40][52]
        Seat seats[][] = new Seat[36][50];
        int k = 0;
        int k1 = 0;
        int k2 = 0;
        int k3 = 0;
        int k4 = 0;
        int k5 = 0;
        int k6 = 0;
        int k7 = 0;
        int k8 = 0;
        int k9 = 0;
        int k10 = 0;
        int k11 = 0;
        int k12 = 0;
        int k13 = 0;

        String[] rowname={"GG","FF","EE","DD","CC","BB","AA","Z","Y","","X","W","V","U","T","S","R","","Q","P","O","N","M","L","K","J","H","G","F","E","D","C","B","A","A1"};
        for (int i = 0; i < 36; i++)
            for(int j = 0; j < 50; j++) {
                SeatExample seat = new SeatExample();
                seats[i][j] = seat;
                seat.status = HallScheme.SeatStatus.EMPTY;
                seat.color = Color.argb(255, 60, 179, 113);
//              for Gth  row
               if(i==0){
                if (j == 0 || j == 49) {

                    seat.status = HallScheme.SeatStatus.INFO;
                    seat.marker=rowname[0];
                }
                else{
                    seat.id = ++k;
                    seat.status = HallScheme.SeatStatus.FREE;
                    seat.marker = String.valueOf(seat.id);
                    seat.selectedSeatMarker=String.valueOf(seat.id);
                    seat.price=200;
                }
               }


               // for Fth  to Rth  row
               if(i>1){
//              i==10  is space after Yth  row
                   if(i!=10 && i!=18 && j>0 && j<50) {

//                       setting row name
                       if (i<18 && (j == 2 || j == 19 || j == 32 || j == 49)) {
                           seat.status = HallScheme.SeatStatus.INFO;
//                        rowname is a string arry  consist of string in array
                           seat.marker = rowname[i - 1];
                       } else {
                           //from Uth row to  Rth  row there is 10 seats
                           if(i>13 && i<18){
    //                          left seats +space+ middle seats +space  +right seats
                                if((j>2 && j<13) || (j>20 && j<31) || (j>38 && j<49)){

                                    seat.id = ++k1;
                                    if(seat.id>10){
                                        seat.id= seat.id+5;
                                    }
                                    seat.status = HallScheme.SeatStatus.FREE;
                                    seat.marker = String.valueOf(seat.id);
                                    seat.selectedSeatMarker=String.valueOf(seat.id);
                                    seat.price=200;
                                }
                                else{
                                    seat.status = HallScheme.SeatStatus.EMPTY;
                                }
                           }
                           else if(i>18 && i<24){// Q to Mth row
                               if ((j == 3 || j == 17 || j == 34 || j == 48)) {
                                   seat.status = HallScheme.SeatStatus.INFO;
//                                  rowname is a string arry  consist of string in array
                                   seat.marker = rowname[i - 1];

                               }
                               if((j>3 && j<16) || (j>18 && j<33) || (j>35 && j<48)){
                                   seat.id = ++k2;
                                   seat.status = HallScheme.SeatStatus.FREE;
                                   seat.marker = String.valueOf(seat.id);
                                   seat.selectedSeatMarker=String.valueOf(seat.id);
                                   seat.price=200;
                               }

                           }// Q to Mth row ends
                           else if(i>23 && i<26){// L to Kth row
                               if ((j == 4 || j == 17 || j == 34 || j == 47)) {
                                   seat.status = HallScheme.SeatStatus.INFO;
//                                  rowname is a string arry  consist of string in array
                                   seat.marker = rowname[i - 1];

                               }
                               if((j>4 && j<16) || (j>18 && j<33) || (j>35 && j<47)){
                                   seat.id = ++k3;
                                   seat.status = HallScheme.SeatStatus.FREE;
                                   seat.marker = String.valueOf(seat.id);
                                   seat.selectedSeatMarker=String.valueOf(seat.id);
                                   seat.price=200;
                               }

                           }// L to Kth row ends
                           else if(i>25 && i<28){// J to Hth row
                               if ((j == 5 || j == 17 || j == 34 || j == 46)) {
                                   seat.status = HallScheme.SeatStatus.INFO;
//                                  rowname is a string arry  consist of string in array
                                   seat.marker = rowname[i - 1];

                               }
                               if((j>5 && j<16) || (j>18 && j<33) || (j>35 && j<46)){
                                   seat.id = ++k4;
                                   seat.status = HallScheme.SeatStatus.FREE;
                                   seat.marker = String.valueOf(seat.id);
                                   seat.selectedSeatMarker=String.valueOf(seat.id);
                                   seat.price=200;
                               }

                           }// J to Hth row
                           else if(i==28){// Gth row
                               if ((j == 6 || j == 17 || j == 34 || j == 45)) {
                                   seat.status = HallScheme.SeatStatus.INFO;
//                                  rowname is a string arry  consist of string in array
                                   seat.marker = rowname[i - 1];

                               }
                               if((j>6 && j<16) || (j>18 && j<33) || (j>35 && j<45)){
                                   seat.id = ++k5;
                                   seat.status = HallScheme.SeatStatus.FREE;
                                   seat.marker = String.valueOf(seat.id);
                                   seat.selectedSeatMarker=String.valueOf(seat.id);
                                   seat.price=200;
                               }

                           }// Gth row ends
                           else if(i==29){// F row
                               if ((j == 7 || j == 17 || j == 34 || j == 44)) {
                                   seat.status = HallScheme.SeatStatus.INFO;
//                                  rowname is a string arry  consist of string in array
                                   seat.marker = rowname[i - 1];

                               }
                               if((j>7 && j<16) || (j>18 && j<33) || (j>35 && j<44)){
                                   seat.id = ++k6;
                                   seat.status = HallScheme.SeatStatus.FREE;
                                   seat.marker = String.valueOf(seat.id);
                                   seat.selectedSeatMarker=String.valueOf(seat.id);
                                   seat.price=200;
                               }

                           }// Fth row  ends
                           else if(i==30){ //E row
                               if ((j == 8 || j == 17 || j == 34 || j == 43)) {
                                   seat.status = HallScheme.SeatStatus.INFO;
//                                  rowname is a string arry  consist of string in array
                                   seat.marker = rowname[i - 1];

                               }
                               if((j>8 && j<16) || (j>18 && j<33) || (j>35 && j<43)){
                                   seat.id = ++k7;
                                   seat.status = HallScheme.SeatStatus.FREE;
                                   seat.marker = String.valueOf(seat.id);
                                   seat.selectedSeatMarker=String.valueOf(seat.id);
                                   seat.price=200;
                               }

                           }// Eth row ends
                           else if(i==31){ //D row
                               if ((j == 9 || j == 17 || j == 34 || j == 42)) {
                                   seat.status = HallScheme.SeatStatus.INFO;
//                                  rowname is a string arry  consist of string in array
                                   seat.marker = rowname[i - 1];

                               }
                               if((j>9 && j<16) || (j>18 && j<33) || (j>35 && j<42)){
                                   seat.id = ++k8;
                                   seat.status = HallScheme.SeatStatus.FREE;
                                   seat.marker = String.valueOf(seat.id);
                                   seat.selectedSeatMarker=String.valueOf(seat.id);
                                   seat.price=200;
                               }

                           }// Dth row ends
                           else if(i==32){ //C row
                               if ((j == 10 || j == 17 || j == 34 || j == 41)) {
                                   seat.status = HallScheme.SeatStatus.INFO;
//                                  rowname is a string arry  consist of string in array
                                   seat.marker = rowname[i - 1];

                               }
                               if((j>10 && j<16) || (j>18 && j<33) || (j>35 && j<41)){
                                   seat.id = ++k9;
                                   seat.status = HallScheme.SeatStatus.FREE;
                                   seat.marker = String.valueOf(seat.id);
                                   seat.selectedSeatMarker=String.valueOf(seat.id);
                                   seat.price=200;
                               }

                           }// Cth row ends
                           else if(i==33){ //B row
                               if ((j == 11 || j == 17 || j == 34 || j == 40)) {
                                   seat.status = HallScheme.SeatStatus.INFO;
//                                  rowname is a string arry  consist of string in array
                                   seat.marker = rowname[i - 1];

                               }
                               if((j>11 && j<16) || (j>18 && j<33) || (j>35 && j<40)){
                                   seat.id = ++k10;
                                   seat.status = HallScheme.SeatStatus.FREE;
                                   seat.marker = String.valueOf(seat.id);
                                   seat.selectedSeatMarker=String.valueOf(seat.id);
                                   seat.price=200;
                               }

                           }// Bth row ends
                           else if(i==34){ // Ath row
                               if ((j == 12 || j == 17 || j == 34 || j == 39)) {
                                   seat.status = HallScheme.SeatStatus.INFO;
//                                  rowname is a string arry  consist of string in array
                                   seat.marker = rowname[i - 1];

                               }
                               if((j>12 && j<16) || (j>19 && j<33) || (j>35 && j<39)){
                                   seat.id = ++k11;
                                   seat.status = HallScheme.SeatStatus.FREE;
                                   seat.marker = String.valueOf(seat.id);
                                   seat.selectedSeatMarker=String.valueOf(seat.id);
                                   seat.price=200;
                               }

                           }// Ath row ends
                           else if(i==35){ // A1th row
                               if (( j == 17 || j == 34 )) {
                                   seat.status = HallScheme.SeatStatus.INFO;
//                                  rowname is a string arry  consist of string in array
                                   seat.marker = rowname[i - 1];

                               }
                               if( (j>19 && j<33)){
                                   seat.id = ++k12;
                                   seat.status = HallScheme.SeatStatus.FREE;
                                   seat.marker = String.valueOf(seat.id);
                                   seat.selectedSeatMarker=String.valueOf(seat.id);
                                   seat.price=200;
                               }
                           }// A1th row ends
                           else{
//                             left seats +middle seats +right seats
                               seat.id = (j-2);
                               if(seat.id>18 && seat.id<52){
                                   seat.id=seat.id-3;
                               }
                               if(seat.id>=29){
                                   seat.id=seat.id-3;
                               }
                               Log.d("", "Bhaidas seat.id k13=seat.id== " + (seat.id));
                               seat.status = HallScheme.SeatStatus.FREE;
                               seat.marker = String.valueOf(seat.id);
                               seat.selectedSeatMarker=String.valueOf(seat.id);
                               seat.price=200;


                           }

                       }
                       if ((i<18)&& (j == 20 || j == 18 || j == 1 || j == 31 || j == 33)) {
                           seat.status = HallScheme.SeatStatus.EMPTY;
                       }
                   }
                   else{
                       seat.status = HallScheme.SeatStatus.EMPTY;
                   }


               }

            }

        return seats;
    }

}