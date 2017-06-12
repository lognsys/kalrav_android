package com.lognsys.kalrav.schemes;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lognsys.kalrav.R;
import com.lognsys.kalrav.model.SeatExample;

import by.anatoldeveloper.hallscheme.hall.HallScheme;
import by.anatoldeveloper.hallscheme.hall.ScenePosition;
import by.anatoldeveloper.hallscheme.hall.Seat;
import by.anatoldeveloper.hallscheme.view.ZoomableImageView;

/**
 * Created by Nublo on 06.12.2015.
 * Copyright Nublo
 */
public class SchemePrabhodhanFragment extends Fragment {

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
        Seat seats[][] = new Seat[37][37];
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

        String[] rowname={"Row AA to KK - Rs .",
                "KK","JJ","HH","GG",
                "FF","EE","DD","CC",
                "BB","AA","","Row A to Y - Rs .",
                "Y","X","W","V","U","T","S","R",
                "P","O","N","SP","M","L","K","J",
                "H","G","F","E","D","C","B","A"};
        for (int i = 0; i < 37; i++)
            for(int j = 0; j < 37; j++) {
                SeatExample seat = new SeatExample();
                seats[i][j] = seat;
                seat.status = HallScheme.SeatStatus.EMPTY;
                seat.color = Color.argb(255, 60, 179, 113);
//              for 0th  row name : title
               if(i==0 || i==12){
                if (j == 19) {
                    seat.status = HallScheme.SeatStatus.INFO;
                    seat.price=200;
                    seat.marker=rowname[i]+ String.valueOf(seat.price);
                }
               }
               // for KKth  to AAth  row
               if(i>0){ // kk th  row
                   if(i==1 ){
                       if(j==0){
                           seat.status = HallScheme.SeatStatus.INFO;
                           seat.marker=rowname[i];
                       }
                       if((i==1 && j>11 && j<24)){
                           seat.id = ++k;
                           seat.status = HallScheme.SeatStatus.FREE;
                           seat.marker = String.valueOf(seat.id);
                           seat.selectedSeatMarker=String.valueOf(seat.id);
                           seat.price=200;
                       }
                   }
                   if(i>1 && i<3){// jjth row
                       if(j==0){
                           seat.status = HallScheme.SeatStatus.INFO;
                           seat.marker=rowname[i];
                       }
                       if(j==10||j==25){
                           seat.status = HallScheme.SeatStatus.EMPTY;
                       }
                       if((j>5 && j<10)||(j>10 && j<25) ||(j>25 && j<30)){
                           seat.id = ++k;
                           seat.status = HallScheme.SeatStatus.FREE;
                           seat.marker = String.valueOf(seat.id);
                           seat.selectedSeatMarker=String.valueOf(seat.id);
                           seat.price=200;
                       }
                   }
                   if(i>2 && i<4){//hh row
                       if(j==0){
                           seat.status = HallScheme.SeatStatus.INFO;
                           seat.marker=rowname[i];
                       }
                       if(j==10||j==25){
                           seat.status = HallScheme.SeatStatus.EMPTY;
                       }
                       if((j>3 && j<10)||(j>10 && j<25) ||(j>25 && j<33)){
                           seat.id = ++k;
                           seat.status = HallScheme.SeatStatus.FREE;
                           seat.marker = String.valueOf(seat.id);
                           seat.selectedSeatMarker=String.valueOf(seat.id);
                           seat.price=200;
                       }
                   }
                   if(i>3 && i<8){ //gg, ff, ee, dd row
                       if(j==0){
                           seat.status = HallScheme.SeatStatus.INFO;
                           seat.marker=rowname[i];
                       }
                       if(j==10||j==25){
                           seat.status = HallScheme.SeatStatus.EMPTY;
                       }
                       if((j>0 && j<10)||(j>10 && j<25) ||(j>25 && j<35)){
                           seat.id = ++k;
                           seat.status = HallScheme.SeatStatus.FREE;
                           seat.marker = String.valueOf(seat.id);
                           seat.selectedSeatMarker=String.valueOf(seat.id);
                           seat.price=200;
                       }
                   }
                   if(i>7 && i<10){ // cc, bb row
                       if(j==0){
                           seat.status = HallScheme.SeatStatus.INFO;
                           seat.marker=rowname[i];
                       }
                       if(j==10||j==25){
                           seat.status = HallScheme.SeatStatus.EMPTY;
                       }
                       if((j>1 && j<10)||(j>10 && j<25) ||(j>25 && j<34)){
                           seat.id = ++k;
                           seat.status = HallScheme.SeatStatus.FREE;
                           seat.marker = String.valueOf(seat.id);
                           seat.selectedSeatMarker=String.valueOf(seat.id);
                           seat.price=200;
                       }
                   }
                   if(i==10){// aa row
                       if(j==0){
                           seat.status = HallScheme.SeatStatus.INFO;
                           seat.marker=rowname[i];
                       }
                       if(j==10||j==25){
                           seat.status = HallScheme.SeatStatus.EMPTY;
                       }
                       if((j>0 && j<10)||(j>10 && j<25) ||(j>25 && j<32)){
                           seat.id = ++k;
                           seat.status = HallScheme.SeatStatus.FREE;
                           seat.marker = String.valueOf(seat.id);
                           seat.selectedSeatMarker=String.valueOf(seat.id);
                           seat.price=200;
                       }
                   }
                   if( i==13){ //y th  row
                       if(j==0){
                           seat.status = HallScheme.SeatStatus.INFO;
                           seat.marker=rowname[i];
                       }
                       if((i==13 && j>12 && j<23)){
                           seat.id = ++k;
                           seat.status = HallScheme.SeatStatus.FREE;
                           seat.marker = String.valueOf(seat.id);
                           seat.selectedSeatMarker=String.valueOf(seat.id);
                           seat.price=200;
                       }
                   }
                   if(i==14){// X row
                       if(j==0){
                           seat.status = HallScheme.SeatStatus.INFO;
                           seat.marker=rowname[i];
                       }
                       if(j==11||j==24){
                           seat.status = HallScheme.SeatStatus.EMPTY;
                       }
                       if((j>8 && j<11)||(j>11 && j<24) ||(j>24 && j<27)){
                           seat.id = ++k;
                           seat.status = HallScheme.SeatStatus.FREE;
                           seat.marker = String.valueOf(seat.id);
                           seat.selectedSeatMarker=String.valueOf(seat.id);
                           seat.price=200;
                       }
                   }
                   if(i==15){// W row
                       if(j==0){
                           seat.status = HallScheme.SeatStatus.INFO;
                           seat.marker=rowname[i];
                       }
                       if(j==11||(j>22 && j<25)){
                           seat.status = HallScheme.SeatStatus.EMPTY;
                       }
                       if((j>5 && j<11)||(j>11 && j<23) ||(j>24 && j<29)){
                           seat.id = ++k;
                           seat.status = HallScheme.SeatStatus.FREE;
                           seat.marker = String.valueOf(seat.id);
                           seat.selectedSeatMarker=String.valueOf(seat.id);
                           seat.price=200;
                       }
                   }
                   if(i==16){// V row
                       if(j==0){
                           seat.status = HallScheme.SeatStatus.INFO;
                           seat.marker=rowname[i];
                       }
                       if(j==11||j==24){
                           seat.status = HallScheme.SeatStatus.EMPTY;
                       }
                       if((j>3 && j<11)||(j>11 && j<24) ||(j>24 && j<34)){
                           seat.id = ++k;
                           seat.status = HallScheme.SeatStatus.FREE;
                           seat.marker = String.valueOf(seat.id);
                           seat.selectedSeatMarker=String.valueOf(seat.id);
                           seat.price=200;
                       }
                   }
                   if(i==17){// U row
                       if(j==0){
                           seat.status = HallScheme.SeatStatus.INFO;
                           seat.marker=rowname[i];
                       }
                       if(j==11|| (j>22 && j<25)){
                           seat.status = HallScheme.SeatStatus.EMPTY;
                       }
                       if((j>0 && j<11)||(j>11 && j<23) ||(j>24 && j<35)){
                           seat.id = ++k;
                           seat.status = HallScheme.SeatStatus.FREE;
                           seat.marker = String.valueOf(seat.id);
                           seat.selectedSeatMarker=String.valueOf(seat.id);
                           seat.price=200;
                       }
                   }
                   if(i==18){// T row
                       if(j==0){
                           seat.status = HallScheme.SeatStatus.INFO;
                           seat.marker=rowname[i];
                       }
                       if(j==11||j==24){
                           seat.status = HallScheme.SeatStatus.EMPTY;
                       }
                       if((j>0 && j<11)||(j>11 && j<24) ||(j>24 && j<35)){
                           seat.id = ++k;
                           seat.status = HallScheme.SeatStatus.FREE;
                           seat.marker = String.valueOf(seat.id);
                           seat.selectedSeatMarker=String.valueOf(seat.id);
                           seat.price=200;
                       }
                   }
                   if(i==19){// S row
                       if(j==0){
                           seat.status = HallScheme.SeatStatus.INFO;
                           seat.marker=rowname[i];
                       }
                       if(j==11|| (j>22 && j<25)){
                           seat.status = HallScheme.SeatStatus.EMPTY;
                       }
                       if((j>0 && j<11)||(j>11 && j<23) ||(j>24 && j<35)){
                           seat.id = ++k;
                           seat.status = HallScheme.SeatStatus.FREE;
                           seat.marker = String.valueOf(seat.id);
                           seat.selectedSeatMarker=String.valueOf(seat.id);
                           seat.price=200;
                       }
                   }
                   if(i==20){// R row
                       if(j==0){
                           seat.status = HallScheme.SeatStatus.INFO;
                           seat.marker=rowname[i];
                       }
                       if(j==11||j==24){
                           seat.status = HallScheme.SeatStatus.EMPTY;
                       }
                       if((j>0 && j<11)||(j>11 && j<24) ||(j>24 && j<35)){
                           seat.id = ++k;
                           seat.status = HallScheme.SeatStatus.FREE;
                           seat.marker = String.valueOf(seat.id);
                           seat.selectedSeatMarker=String.valueOf(seat.id);
                           seat.price=200;
                       }
                   }
                   if(i==21){// P row
                       if(j==0){
                           seat.status = HallScheme.SeatStatus.INFO;
                           seat.marker=rowname[i];
                       }
                       if(j==11|| (j>22 && j<25)){
                           seat.status = HallScheme.SeatStatus.EMPTY;
                       }
                       if((j>0 && j<11)||(j>11 && j<23) ||(j>24 && j<35)){
                           seat.id = ++k;
                           seat.status = HallScheme.SeatStatus.FREE;
                           seat.marker = String.valueOf(seat.id);
                           seat.selectedSeatMarker=String.valueOf(seat.id);
                           seat.price=200;
                       }
                   }
               }
                if(i==20){// R row
                    if(j==0){
                        seat.status = HallScheme.SeatStatus.INFO;
                        seat.marker=rowname[i];
                    }
                    if(j==11||j==24){
                        seat.status = HallScheme.SeatStatus.EMPTY;
                    }
                    if((j>0 && j<11)||(j>11 && j<24) ||(j>24 && j<35)){
                        seat.id = ++k;
                        seat.status = HallScheme.SeatStatus.FREE;
                        seat.marker = String.valueOf(seat.id);
                        seat.selectedSeatMarker=String.valueOf(seat.id);
                        seat.price=200;
                    }
                }
                if(i==21){// P row
                    if(j==0){
                        seat.status = HallScheme.SeatStatus.INFO;
                        seat.marker=rowname[i];
                    }
                    if(j==11|| (j>22 && j<25)){
                        seat.status = HallScheme.SeatStatus.EMPTY;
                    }
                    if((j>0 && j<11)||(j>11 && j<23) ||(j>24 && j<35)){
                        seat.id = ++k;
                        seat.status = HallScheme.SeatStatus.FREE;
                        seat.marker = String.valueOf(seat.id);
                        seat.selectedSeatMarker=String.valueOf(seat.id);
                        seat.price=200;
                    }
                }
                if(i==22){// O row
                    if(j==0){
                        seat.status = HallScheme.SeatStatus.INFO;
                        seat.marker=rowname[i];
                    }
                    if(j==11||j==24){
                        seat.status = HallScheme.SeatStatus.EMPTY;
                    }
                    if((j>0 && j<11)||(j>11 && j<24) ||(j>24 && j<35)){
                        seat.id = ++k;
                        seat.status = HallScheme.SeatStatus.FREE;
                        seat.marker = String.valueOf(seat.id);
                        seat.selectedSeatMarker=String.valueOf(seat.id);
                        seat.price=200;
                    }
                }
                if(i==23){// N row
                    if(j==0){
                        seat.status = HallScheme.SeatStatus.INFO;
                        seat.marker=rowname[i];
                    }
                    if(j==11|| (j>22 && j<25)){
                        seat.status = HallScheme.SeatStatus.EMPTY;
                    }
                    if((j>0 && j<11)||(j>11 && j<23) ||(j>24 && j<35)){
                        seat.id = ++k;
                        seat.status = HallScheme.SeatStatus.FREE;
                        seat.marker = String.valueOf(seat.id);
                        seat.selectedSeatMarker=String.valueOf(seat.id);
                        seat.price=200;
                    }
                }
                if(i==24){// SP row
                    if(j==0){
                        seat.status = HallScheme.SeatStatus.INFO;
                        seat.marker=rowname[i];
                    }
                    if(j==11||j==24){
                        seat.status = HallScheme.SeatStatus.EMPTY;
                    }
                    if((j>1 && j<11)||(j>11 && j<24) ||(j>24 && j<34)){
                        seat.id = ++k;
                        seat.status = HallScheme.SeatStatus.FREE;
                        seat.marker = String.valueOf(seat.id);
                        seat.selectedSeatMarker=String.valueOf(seat.id);
                        seat.price=200;
                    }
                }
                if(i>24 && i<32){// M row to F row
                    if(j==0){
                        seat.status = HallScheme.SeatStatus.INFO;
                        seat.marker=rowname[i];
                    }
                    if(j==11||j==24){
                        seat.status = HallScheme.SeatStatus.EMPTY;
                    }
                    if((j>0 && j<11)||((((i%2==0)&& (j>11 && j<23)) ||(!(i%2==0)&&(j>11 && j<24)))) ||(j>24 && j<35)){
                        seat.id = ++k;
                        seat.status = HallScheme.SeatStatus.FREE;
                        seat.marker = String.valueOf(seat.id);
                        seat.selectedSeatMarker=String.valueOf(seat.id);
                        seat.price=200;
                    }
                }
                if(i>31 && i<35){// E row to C row
                    if(j==0){
                        seat.status = HallScheme.SeatStatus.INFO;
                        seat.marker=rowname[i];
                    }
                    if(j==11||j==24){
                        seat.status = HallScheme.SeatStatus.EMPTY;
                    }
                    if((j>1 && j<11)||((((i%2==0)&& (j>11 && j<23)) ||(!(i%2==0)&&(j>11 && j<24)))) ||(j>24 && j<34)){
                        seat.id = ++k;
                        seat.status = HallScheme.SeatStatus.FREE;
                        seat.marker = String.valueOf(seat.id);
                        seat.selectedSeatMarker=String.valueOf(seat.id);
                        seat.price=200;
                    }
                }
                if(i==35){// B row
                    if(j==0){
                        seat.status = HallScheme.SeatStatus.INFO;
                        seat.marker=rowname[i];
                    }
                    if(j==11||j==24){
                        seat.status = HallScheme.SeatStatus.EMPTY;
                    }
                    if((j>2 && j<11)||(j>11 && j<24) ||(j>24 && j<33)){
                        seat.id = ++k;
                        seat.status = HallScheme.SeatStatus.FREE;
                        seat.marker = String.valueOf(seat.id);
                        seat.selectedSeatMarker=String.valueOf(seat.id);
                        seat.price=200;
                    }
                }
                if(i==36){// A row
                    if(j==0){
                        seat.status = HallScheme.SeatStatus.INFO;
                        seat.marker=rowname[i];
                    }
                    if(j==11|| (j>22 && j<25)){
                        seat.status = HallScheme.SeatStatus.EMPTY;
                    }
                    if((j>3 && j<11)||(j>11 && j<23) ||(j>24 && j<32)){
                        seat.id = ++k;
                        seat.status = HallScheme.SeatStatus.FREE;
                        seat.marker = String.valueOf(seat.id);
                        seat.selectedSeatMarker=String.valueOf(seat.id);
                        seat.price=200;
                    }
                }

            }

        return seats;
    }

}