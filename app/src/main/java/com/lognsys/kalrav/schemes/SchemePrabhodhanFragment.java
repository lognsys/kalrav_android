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

import java.util.List;

import by.anatoldeveloper.hallscheme.hall.HallScheme;
import by.anatoldeveloper.hallscheme.hall.ScenePosition;
import by.anatoldeveloper.hallscheme.hall.Seat;
import by.anatoldeveloper.hallscheme.view.ZoomableImageView;

/**
 * Created by Nublo on 06.12.2015.
 * Copyright Nublo
 */
public class SchemePrabhodhanFragment extends Fragment {

    private static final String TAG="SchemePrabhodhanFragment";


    List<SeatExample> itemsList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.basic_scheme_fragment, container, false);
        ZoomableImageView imageView = (ZoomableImageView) rootView.findViewById(R.id.zoomable_image);

        itemsList= (List<SeatExample>) getArguments().getSerializable("itemsList");

        HallScheme scheme = new HallScheme(imageView, basicScheme(), getActivity());
        scheme.setSceneName(getString(R.string.all_eye_here));
        scheme.setScenePosition(ScenePosition.SOUTH);
        return rootView;
    }

    public Seat[][] basicScheme() {//[40][52]
        Seat seats[][] = new Seat[37][37];

        String[] rowname = {"Row AA to KK - Rs .",
                "KK", "JJ", "HH", "GG",
                "FF", "EE", "DD", "CC",
                "BB", "AA", "", "Row A to Y - Rs .",
                "Y", "X", "W", "V", "U", "T", "S", "R",
                "P", "O", "N", "SP", "M", "L", "K", "J",
                "H", "G", "F", "E", "D", "C", "B", "A"};
        for (int i = 0; i < 37; i++) {
            int k = 0;
            for (int j = 0; j < 37; j++) {
                SeatExample seat = new SeatExample();
                seats[i][j] = seat;
                seat.status = HallScheme.SeatStatus.EMPTY;
                seat.color = Color.argb(255, 60, 179, 113);
//              for 0th  row name : title
                if (i == 0 || i == 12) {
                    if (j == 19) {
                        seat.status = HallScheme.SeatStatus.INFO;
                        seat.price = 200;
                        seat.marker = rowname[i] + String.valueOf(seat.price);
                    }
                }
                // for KKth  to AAth  row
                if (i > 0) { // kk th  row
                    if (i == 1) {
                        if (j == 0) {
                            seat.status = HallScheme.SeatStatus.INFO;
                            seat.marker = rowname[i];
                        }
                        if ((i == 1 && j > 11 && j < 24)) {
                            seat.id = ++k;
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.marker = String.valueOf(seat.id);
                            seat.selectedSeatMarker = String.valueOf(seat.id);
                            seat.price = 200;
                            Log.d(TAG, " kk th row");
                            ;
                        }
                    }
                    if (i > 1 && i < 3) {// jjth row
                        if (j == 0) {
                            seat.status = HallScheme.SeatStatus.INFO;
                            seat.marker = rowname[i];
                        }
                        if (j == 10 || j == 25) {
                            seat.status = HallScheme.SeatStatus.EMPTY;
                        }
                        if ((j > 5 && j < 10) || (j > 10 && j < 25) || (j > 25 && j < 30)) {
                            seat.id = ++k;
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.marker = String.valueOf(seat.id);
                            seat.selectedSeatMarker = String.valueOf(seat.id);
                            seat.price = 200;

                            Log.d(TAG, " kk th row");
                        }
                    }
                    if (i > 2 && i < 4) {//hh row
                        if (j == 0) {
                            seat.status = HallScheme.SeatStatus.INFO;
                            seat.marker = rowname[i];
                        }
                        if (j == 10 || j == 25) {
                            seat.status = HallScheme.SeatStatus.EMPTY;
                        }
                        if ((j > 3 && j < 10) || (j > 10 && j < 25) || (j > 25 && j < 33)) {
                            seat.id = ++k;
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.marker = String.valueOf(seat.id);
                            seat.selectedSeatMarker = String.valueOf(seat.id);
                            seat.price = 200;
                        }
                    }
                    if (i > 3 && i < 8) { //gg, ff, ee, dd row
                        if (j == 0) {
                            seat.status = HallScheme.SeatStatus.INFO;
                            seat.marker = rowname[i];
                        }
                        if (j == 10 || j == 25) {
                            seat.status = HallScheme.SeatStatus.EMPTY;
                        }
                        if ((j > 0 && j < 10) || (j > 10 && j < 25) || (j > 25 && j < 35)) {
                            seat.id = ++k;
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.marker = String.valueOf(seat.id);
                            seat.selectedSeatMarker = String.valueOf(seat.id);
                            seat.price = 200;
                        }
                    }
                    if (i > 7 && i < 10) { // cc, bb row
                        if (j == 0) {
                            seat.status = HallScheme.SeatStatus.INFO;
                            seat.marker = rowname[i];
                        }
                        if (j == 10 || j == 25) {
                            seat.status = HallScheme.SeatStatus.EMPTY;
                        }
                        if ((j > 1 && j < 10) || (j > 10 && j < 25) || (j > 25 && j < 34)) {
                            seat.id = ++k;
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.marker = String.valueOf(seat.id);
                            seat.selectedSeatMarker = String.valueOf(seat.id);
                            seat.price = 200;
                        }
                    }
                    if (i == 10) {// aa row
                        if (j == 0) {
                            seat.status = HallScheme.SeatStatus.INFO;
                            seat.marker = rowname[i];
                        }
                        if (j == 10 || j == 25) {
                            seat.status = HallScheme.SeatStatus.EMPTY;
                        }
                        if ((j > 0 && j < 10) || (j > 10 && j < 25) || (j > 25 && j < 32)) {
                            seat.id = ++k;
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.marker = String.valueOf(seat.id);
                            seat.selectedSeatMarker = String.valueOf(seat.id);
                            seat.price = 200;
                        }
                    }
                    if (i == 13) { //y th  row
                        if (j == 0) {
                            seat.status = HallScheme.SeatStatus.INFO;
                            seat.marker = rowname[i];
                        }
                        if ((i == 13 && j > 12 && j < 23)) {
                            seat.id =1+( ++k);
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.marker = String.valueOf(seat.id);
                            seat.selectedSeatMarker = String.valueOf(seat.id);
                            seat.price = 200;
                        }
                    }
                    if (i == 14) {// X row
                        if (j == 0) {
                            seat.status = HallScheme.SeatStatus.INFO;
                            seat.marker = rowname[i];
                        }
                        if (j == 11 || j == 24) {
                            seat.status = HallScheme.SeatStatus.EMPTY;
                        }
                        if ((j > 8 && j < 11) || (j > 11 && j < 24) || (j > 24 && j < 27)) {
                            seat.id = ++k;
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.marker = String.valueOf(seat.id);
                            seat.selectedSeatMarker = String.valueOf(seat.id);
                            seat.price = 200;
                        }
                    }
                    if (i == 15) {// W row
                        if (j == 0) {
                            seat.status = HallScheme.SeatStatus.INFO;
                            seat.marker = rowname[i];
                        }
                        if (j == 11 || (j > 22 && j < 25)) {
                            seat.status = HallScheme.SeatStatus.EMPTY;
                        }
                        if ((j > 5 && j < 11) || (j > 11 && j < 23) || (j > 24 && j < 29)) {
                            seat.id = ++k;
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.marker = String.valueOf(seat.id);
                            seat.selectedSeatMarker = String.valueOf(seat.id);
                            seat.price = 200;
                        }
                    }
                    if (i == 16) {// V row
                        if (j == 0) {
                            seat.status = HallScheme.SeatStatus.INFO;
                            seat.marker = rowname[i];
                        }
                        if (j == 11 || j == 24) {
                            seat.status = HallScheme.SeatStatus.EMPTY;
                        }
                        if ((j > 3 && j < 11) || (j > 11 && j < 24) || (j > 24 && j < 33)) {
                            seat.id = ++k;
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.marker = String.valueOf(seat.id);
                            seat.selectedSeatMarker = String.valueOf(seat.id);
                            seat.price = 200;
                        }
                    }
                    if (i == 17) {// U row
                        if (j == 0) {
                            seat.status = HallScheme.SeatStatus.INFO;
                            seat.marker = rowname[i];
                        }
                        if (j == 11 || (j > 22 && j < 25)) {
                            seat.status = HallScheme.SeatStatus.EMPTY;
                        }
                        if ((j > 0 && j < 11) || (j > 11 && j < 23) || (j > 24 && j < 35)) {
                            seat.id = ++k;
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.marker = String.valueOf(seat.id);
                            seat.selectedSeatMarker = String.valueOf(seat.id);
                            seat.price = 200;
                        }
                    }
                    if (i == 18) {// T row
                        if (j == 0) {
                            seat.status = HallScheme.SeatStatus.INFO;
                            seat.marker = rowname[i];
                        }
                        if (j == 11 || j == 24) {
                            seat.status = HallScheme.SeatStatus.EMPTY;
                        }
                        if ((j > 0 && j < 11) || (j > 11 && j < 24) || (j > 24 && j < 35)) {
                            seat.id = ++k;
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.marker = String.valueOf(seat.id);
                            seat.selectedSeatMarker = String.valueOf(seat.id);
                            seat.price = 200;
                        }
                    }
                    if (i == 19) {// S row
                        if (j == 0) {
                            seat.status = HallScheme.SeatStatus.INFO;
                            seat.marker = rowname[i];
                        }
                        if (j == 11 || (j > 22 && j < 25)) {
                            seat.status = HallScheme.SeatStatus.EMPTY;
                        }
                        if ((j > 0 && j < 11) || (j > 11 && j < 23) || (j > 24 && j < 35)) {
                            seat.id = ++k;
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.marker = String.valueOf(seat.id);
                            seat.selectedSeatMarker = String.valueOf(seat.id);
                            seat.price = 200;
                        }
                    }
                    if (i == 20) {// R row
                        if (j == 0) {
                            seat.status = HallScheme.SeatStatus.INFO;
                            seat.marker = rowname[i];
                        }
                        if (j == 11 || j == 24) {
                            seat.status = HallScheme.SeatStatus.EMPTY;
                        }
                        if ((j > 0 && j < 11) || (j > 11 && j < 24) || (j > 24 && j < 35)) {
                            seat.id = ++k;
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.marker = String.valueOf(seat.id);
                            seat.selectedSeatMarker = String.valueOf(seat.id);
                            seat.price = 200;
                        }
                    }
                    if (i == 21) {// P row
                        if (j == 0) {
                            seat.status = HallScheme.SeatStatus.INFO;
                            seat.marker = rowname[i];
                        }
                        if (j == 11 || (j > 22 && j < 25)) {
                            seat.status = HallScheme.SeatStatus.EMPTY;
                        }
                        if ((j > 0 && j < 11) || (j > 11 && j < 23) || (j > 24 && j < 35)) {
                            seat.id = ++k;
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.marker = String.valueOf(seat.id);
                            seat.selectedSeatMarker = String.valueOf(seat.id);
                            seat.price = 200;
                        }
                    }
                }
                if (i == 22) {// O row
                    if (j == 0) {
                        seat.status = HallScheme.SeatStatus.INFO;
                        seat.marker = rowname[i];
                    }
                    if (j == 11 || j == 24) {
                        seat.status = HallScheme.SeatStatus.EMPTY;
                    }
                    if ((j > 0 && j < 11) || (j > 11 && j < 24) || (j > 24 && j < 35)) {
                        seat.id = ++k;
                        seat.status = HallScheme.SeatStatus.FREE;
                        seat.marker = String.valueOf(seat.id);
                        seat.selectedSeatMarker = String.valueOf(seat.id);
                        seat.price = 200;
                    }
                }
                if (i == 23) {// N row
                    if (j == 0) {
                        seat.status = HallScheme.SeatStatus.INFO;
                        seat.marker = rowname[i];
                    }
                    if (j == 11 || (j > 22 && j < 25)) {
                        seat.status = HallScheme.SeatStatus.EMPTY;
                    }
                    if ((j > 0 && j < 11) || (j > 11 && j < 23) || (j > 24 && j < 35)) {
                        seat.id = ++k;
                        seat.status = HallScheme.SeatStatus.FREE;
                        seat.marker = String.valueOf(seat.id);
                        seat.selectedSeatMarker = String.valueOf(seat.id);
                        seat.price = 200;
                    }
                }
                if (i == 24) {// SP row
                    if (j == 0) {
                        seat.status = HallScheme.SeatStatus.INFO;
                        seat.marker = rowname[i];
                    }
                    if (j == 11 || j == 24) {
                        seat.status = HallScheme.SeatStatus.EMPTY;
                    }
                    if ((j > 1 && j < 11) || (j > 11 && j < 24) || (j > 24 && j < 34)) {
                        seat.id = ++k;
                        seat.status = HallScheme.SeatStatus.FREE;
                        seat.marker = String.valueOf(seat.id);
                        seat.selectedSeatMarker = String.valueOf(seat.id);
                        seat.price = 200;
                    }
                }
                if (i > 24 && i < 32) {// M row to F row
                    if (j == 0) {
                        seat.status = HallScheme.SeatStatus.INFO;
                        seat.marker = rowname[i];
                    }
                    if (j == 11 || j == 24) {
                        seat.status = HallScheme.SeatStatus.EMPTY;
                    }
                    if ((j > 0 && j < 11) || ((((i % 2 == 0) && (j > 11 && j < 23)) || (!(i % 2 == 0) && (j > 11 && j < 24)))) || (j > 24 && j < 35)) {
                        seat.id = ++k;
                        seat.status = HallScheme.SeatStatus.FREE;
                        seat.marker = String.valueOf(seat.id);
                        seat.selectedSeatMarker = String.valueOf(seat.id);
                        seat.price = 200;
                    }
                }
                if (i > 31 && i < 35) {// E row to C row
                    if (j == 0) {
                        seat.status = HallScheme.SeatStatus.INFO;
                        seat.marker = rowname[i];
                    }
                    if (j == 11 || j == 24) {
                        seat.status = HallScheme.SeatStatus.EMPTY;
                    }
                    if ((j > 1 && j < 11) || ((((i % 2 == 0) && (j > 11 && j < 23)) || (!(i % 2 == 0) && (j > 11 && j < 24)))) || (j > 24 && j < 34)) {
                        seat.id = ++k;
                        seat.status = HallScheme.SeatStatus.FREE;
                        seat.marker = String.valueOf(seat.id);
                        seat.selectedSeatMarker = String.valueOf(seat.id);
                        seat.price = 200;
                    }
                }
                if (i == 35) {// B row
                    if (j == 0) {
                        seat.status = HallScheme.SeatStatus.INFO;
                        seat.marker = rowname[i];
                    }
                    if (j == 11 || j == 24) {
                        seat.status = HallScheme.SeatStatus.EMPTY;
                    }
                    if ((j > 2 && j < 11) || (j > 11 && j < 24) || (j > 24 && j < 33)) {
                        seat.id = ++k;
                        seat.status = HallScheme.SeatStatus.FREE;
                        seat.marker = String.valueOf(seat.id);
                        seat.selectedSeatMarker = String.valueOf(seat.id);
                        seat.price = 200;
                    }
                }
                if (i == 36) {// A row
                    if (j == 0) {
                        seat.status = HallScheme.SeatStatus.INFO;
                        seat.marker = rowname[i];
                    }
                    if (j == 11 || (j > 22 && j < 25)) {
                        seat.status = HallScheme.SeatStatus.EMPTY;
                    }
                    if ((j > 3 && j < 11) || (j > 11 && j < 23) || (j > 24 && j < 32)) {
                        seat.id = ++k;
                        seat.status = HallScheme.SeatStatus.FREE;
                        seat.marker = String.valueOf(seat.id);
                        seat.selectedSeatMarker = String.valueOf(seat.id);
                        seat.price = 200;
                    }
                }

            }

//            incrementing by  2 rows range of rows 1 to 28
            int rows=0;
            for(int l=0;l<itemsList.size();l++){
                for(int m=0;m<itemsList.size();m++){
                    SeatExample seat = new SeatExample();
                    seat.status = HallScheme.SeatStatus.BUSY;
                    int colvalue=0;
                    {


//                   row KK start
                        if(itemsList.get(l).getIrow()==1)
                        {
                            rows= itemsList.get(l).getIrow();

                            if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<13)
                            {
                                colvalue=11+itemsList.get(l).getJrow();
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.id =itemsList.get(l).getJrow();
                                seat.selectedSeatMarker = String.valueOf(seat.id);
                            }
                        }//                   row KK ends

//                   row JJ start
                        if(itemsList.get(l).getIrow()==2)
                        {
                            rows= itemsList.get(l).getIrow();
                            if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<23){
                                if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<19)
                            {
                                if(itemsList.get(l).getJrow()>4){
                                    colvalue=6+itemsList.get(l).getJrow();
                                }
                                else{
                                    colvalue=5+itemsList.get(l).getJrow();

                                }
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.id =itemsList.get(l).getJrow();
                                seat.selectedSeatMarker = String.valueOf(seat.id);
                            }
                            else {
                                colvalue=7+itemsList.get(l).getJrow();
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.id =itemsList.get(l).getJrow();
                                seat.selectedSeatMarker = String.valueOf(seat.id);

                            }
                            }
                            else{
                                    seat.status = HallScheme.SeatStatus.INFO;
                                    seat.marker = rowname[2];
                            }
                        }//                   row JJ ends


//                   row HH start
                        if(itemsList.get(l).getIrow()==3)
                        {
                            rows= itemsList.get(l).getIrow();
                            if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<28){

                                if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<21)
                                {
                                    if(itemsList.get(l).getJrow()>6){
                                        colvalue=4+itemsList.get(l).getJrow();

                                    }
                                    else{
                                        colvalue=3+itemsList.get(l).getJrow();

                                    }
                                     seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else {
                                    colvalue=5+itemsList.get(l).getJrow();
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);

                                }
                            }
                            else{
                                seat.status = HallScheme.SeatStatus.INFO;
                                seat.marker = rowname[3];

                            }
                        }//                   row HH ends


//                   row GG - DD start
                        if(itemsList.get(l).getIrow()>3 && itemsList.get(l).getIrow()<8)
                        {
                            rows= itemsList.get(l).getIrow();
                            if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<33){

                                if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<24)
                                {
                                    if(itemsList.get(l).getJrow()>9){
                                        colvalue=1+itemsList.get(l).getJrow();

                                    }
                                    else{
                                        colvalue=itemsList.get(l).getJrow();

                                    }
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else {
                                    colvalue=2+itemsList.get(l).getJrow();
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);

                                }
                            }
                            else{
                                seat.status = HallScheme.SeatStatus.INFO;
                                seat.marker = rowname[rows];

                            }
                        }//                   row GG - DD  ends

//                   row CC - BB start
                        if(itemsList.get(l).getIrow()>7 && itemsList.get(l).getIrow()<10)
                        {
                            rows= itemsList.get(l).getIrow();
                            if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<32){

                                if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<23)
                                {
                                    if(itemsList.get(l).getJrow()>8){
                                        colvalue=2+itemsList.get(l).getJrow();

                                    }
                                    else{
                                        colvalue=1+itemsList.get(l).getJrow();

                                    }
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else {
                                    colvalue=3+itemsList.get(l).getJrow();
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);

                                }
                            }
                            else{
                                seat.status = HallScheme.SeatStatus.INFO;
                                seat.marker = rowname[rows];

                            }
                        }//                   row CC - BB  ends

//                   row AA start
                        if(itemsList.get(l).getIrow()==10)
                        {
                            rows= itemsList.get(l).getIrow();
                            if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<31){

                                if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<24)
                                {
                                    if(itemsList.get(l).getJrow()>9 && itemsList.get(l).getJrow()<24){
                                        colvalue=1+itemsList.get(l).getJrow();

                                    }
                                    else{
                                        colvalue=itemsList.get(l).getJrow();

                                    }
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else {
                                    colvalue=2+itemsList.get(l).getJrow();
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);

                                }
                            }
                            else{
                                seat.status = HallScheme.SeatStatus.INFO;
                                seat.marker = rowname[rows];

                            }
                        }//                   row AA  ends

//                   row Y start
                        if(itemsList.get(l).getIrow()==11)
                        {
                            rows= itemsList.get(l).getIrow()+2;

                            if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<12)
                            {
                                colvalue=11+itemsList.get(l).getJrow();
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.id =itemsList.get(l).getJrow();
                                seat.selectedSeatMarker = String.valueOf(seat.id);
                            }
                        }//                   row Y  ends
                        if (itemsList.get(l).getIrow() == 12) {// X row
                            rows= itemsList.get(l).getIrow()+2;


                            if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<23){
                                if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<15)
                                {
                                    if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<3){
                                        colvalue=8+itemsList.get(l).getJrow();
                                    }
                                    else if(itemsList.get(l).getJrow()>2 && itemsList.get(l).getJrow()<15){
                                        colvalue=9+itemsList.get(l).getJrow();
                                    }
                                    else{
                                        colvalue=10+itemsList.get(l).getJrow();
                                    }
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else {
                                    colvalue=10+itemsList.get(l).getJrow();
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);

                                }
                            }
                            else{
                                seat.status = HallScheme.SeatStatus.INFO;
                                seat.marker = rowname[rows];
                            }
                        } // X ends
                        if (itemsList.get(l).getIrow() == 13) {// W row
                            rows= itemsList.get(l).getIrow()+2;


                            if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<23){
                                if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<17)
                                {
                                    if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<6){
                                        colvalue=5+itemsList.get(l).getJrow();
                                    }
                                    else if(itemsList.get(l).getJrow()>5 && itemsList.get(l).getJrow()<17){
                                        colvalue=6+itemsList.get(l).getJrow();
                                    }

                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else {
                                    colvalue=8+itemsList.get(l).getJrow();
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);

                                }
                            }
                            else{
                                seat.status = HallScheme.SeatStatus.INFO;
                                seat.marker = rowname[rows];
                            }
                        } // W ends
                        if (itemsList.get(l).getIrow() == 14) {// V row
                        rows= itemsList.get(l).getIrow()+2;


                        if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<29){
                            if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<20)
                            {
                                if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<8){
                                    colvalue=3+itemsList.get(l).getJrow();
                                }
                                else if(itemsList.get(l).getJrow()>7 && itemsList.get(l).getJrow()<20){
                                    colvalue=4+itemsList.get(l).getJrow();
                                }
                                else{
                                    colvalue=5+itemsList.get(l).getJrow();
                                }
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.id =itemsList.get(l).getJrow();
                                seat.selectedSeatMarker = String.valueOf(seat.id);
                            }
                            else {
                                colvalue=5+itemsList.get(l).getJrow();
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.id =itemsList.get(l).getJrow();
                                seat.selectedSeatMarker = String.valueOf(seat.id);

                            }
                        }
                        else{
                            seat.status = HallScheme.SeatStatus.INFO;
                            seat.marker = rowname[rows];
                        }
                    } // V ends
                        if (itemsList.get(l).getIrow() == 15) {// U row
                            rows= itemsList.get(l).getIrow()+2;


                            if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<32){
                                if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<22)
                                {
                                    if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<11){
                                        colvalue=itemsList.get(l).getJrow();
                                    }
                                    else if(itemsList.get(l).getJrow()>10 && itemsList.get(l).getJrow()<22){
                                        colvalue=1+itemsList.get(l).getJrow();
                                    }
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else {
                                    colvalue=3+itemsList.get(l).getJrow();
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);

                                }
                            }
                            else{
                                seat.status = HallScheme.SeatStatus.INFO;
                                seat.marker = rowname[rows];
                            }
                        } // U ends
                        if (itemsList.get(l).getIrow() == 16) {// T row
                            rows= itemsList.get(l).getIrow()+2;


                            if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<33){
                                if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<23)
                                {
                                    if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<11){
                                        colvalue=itemsList.get(l).getJrow();
                                    }
                                    else if(itemsList.get(l).getJrow()>10 && itemsList.get(l).getJrow()<23){
                                        colvalue=1+itemsList.get(l).getJrow();
                                    }

                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else {
                                    colvalue=2+itemsList.get(l).getJrow();
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);

                                }
                            }
                            else{
                                seat.status = HallScheme.SeatStatus.INFO;
                                seat.marker = rowname[rows];
                            }
                        } // T ends
                        if (itemsList.get(l).getIrow() == 17) {// S row
                            rows= itemsList.get(l).getIrow()+2;


                            if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<32){
                                if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<22)
                                {
                                    if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<11){
                                        colvalue=itemsList.get(l).getJrow();
                                    }
                                    else if(itemsList.get(l).getJrow()>10 && itemsList.get(l).getJrow()<22){
                                        colvalue=1+itemsList.get(l).getJrow();
                                    }
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else {
                                    colvalue=3+itemsList.get(l).getJrow();
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);

                                }
                            }
                            else{
                                seat.status = HallScheme.SeatStatus.INFO;
                                seat.marker = rowname[rows];
                            }
                        } // S ends
                        if (itemsList.get(l).getIrow() == 18) {// R row
                            rows= itemsList.get(l).getIrow()+2;


                            if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<33){
                                if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<23)
                                {
                                    if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<11){
                                        colvalue=itemsList.get(l).getJrow();
                                    }
                                    else if(itemsList.get(l).getJrow()>10 && itemsList.get(l).getJrow()<23){
                                        colvalue=1+itemsList.get(l).getJrow();
                                    }
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else {
                                    colvalue=2+itemsList.get(l).getJrow();
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);

                                }
                            }
                            else{
                                seat.status = HallScheme.SeatStatus.INFO;
                                seat.marker = rowname[rows];
                            }
                        } // R ends
                        if (itemsList.get(l).getIrow() == 19) {// P row
                            rows= itemsList.get(l).getIrow()+2;


                            if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<32){
                                if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<22)
                                {
                                    if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<11){
                                        colvalue=itemsList.get(l).getJrow();
                                    }
                                    else if(itemsList.get(l).getJrow()>10 && itemsList.get(l).getJrow()<22){
                                        colvalue=1+itemsList.get(l).getJrow();
                                    }
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else {
                                    colvalue=3+itemsList.get(l).getJrow();
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);

                                }
                            }
                            else{
                                seat.status = HallScheme.SeatStatus.INFO;
                                seat.marker = rowname[rows];
                            }
                        } // P ends
                        if (itemsList.get(l).getIrow() == 20) {// O row
                            rows= itemsList.get(l).getIrow()+2;


                            if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<33){
                                if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<23)
                                {
                                    if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<11){
                                        colvalue=itemsList.get(l).getJrow();
                                    }
                                    else if(itemsList.get(l).getJrow()>10 && itemsList.get(l).getJrow()<23){
                                        colvalue=1+itemsList.get(l).getJrow();
                                    }
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else {
                                    colvalue=2+itemsList.get(l).getJrow();
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);

                                }
                            }
                            else{
                                seat.status = HallScheme.SeatStatus.INFO;
                                seat.marker = rowname[rows];
                            }
                        } // O ends
                        if (itemsList.get(l).getIrow() == 21) {// N row
                            rows= itemsList.get(l).getIrow()+2;


                            if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<32){
                                if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<22)
                                {
                                    if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<11){
                                        colvalue=3+itemsList.get(l).getJrow();
                                    }
                                    else if(itemsList.get(l).getJrow()>10 && itemsList.get(l).getJrow()<22){
                                        colvalue=1+itemsList.get(l).getJrow();
                                    }
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else {
                                    colvalue=2+itemsList.get(l).getJrow();
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);

                                }
                            }
                            else{
                                seat.status = HallScheme.SeatStatus.INFO;
                                seat.marker = rowname[rows];
                            }
                        } // N ends

                        if (itemsList.get(l).getIrow() == 22) {// SP row
                            rows= itemsList.get(l).getIrow()+2;


                            if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<31){
                                if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<22)
                                {
                                    if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<10){
                                        colvalue=1+itemsList.get(l).getJrow();
                                    }
                                    else if(itemsList.get(l).getJrow()>9 && itemsList.get(l).getJrow()<22){
                                        colvalue=2+itemsList.get(l).getJrow();
                                    }
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else {
                                    colvalue=3+itemsList.get(l).getJrow();
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);

                                }
                            }
                            else{
                                seat.status = HallScheme.SeatStatus.INFO;
                                seat.marker = rowname[rows];
                            }
                        } // SP ends
                        if (itemsList.get(l).getIrow() > 22 && itemsList.get(l).getIrow()<30) {// M to  F row
                            rows = itemsList.get(l).getIrow() + 2;

                            if (itemsList.get(l).getIrow() > 22 && itemsList.get(l).getIrow()<31) {

                                if (itemsList.get(l).getJrow() > 0 && itemsList.get(l).getJrow() < 33) {

                                    if (itemsList.get(l).getJrow() > 0 && itemsList.get(l).getJrow() < 11) {
                                        colvalue = itemsList.get(l).getJrow();
                                    }
                                    else if (((rows % 2 == 0) && itemsList.get(l).getJrow() > 10 && itemsList.get(l).getJrow() < 22)
                                            || (!(rows % 2 == 0) && itemsList.get(l).getJrow() > 10 && itemsList.get(l).getJrow() < 23)) {
                                        colvalue = 1 + itemsList.get(l).getJrow();
                                    } else if (((rows % 2 == 0) && itemsList.get(l).getJrow() > 22)) {
                                        colvalue = 3 + itemsList.get(l).getJrow();
                                    } else if ((!(rows % 2 == 0) && itemsList.get(l).getJrow() > 22)) {
                                        colvalue = 2 + itemsList.get(l).getJrow();
                                    }
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);

                                }/*else{
                                    colvalue = 3 + itemsList.get(l).getJrow();
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }*/
                            }
                            else{
                                seat.status = HallScheme.SeatStatus.INFO;
                                seat.marker = rowname[rows];

                            }
                        } //  M to  F  End

                        if (itemsList.get(l).getIrow() > 29 && itemsList.get(l).getIrow()  < 33) {// E row to C row
                            rows = itemsList.get(l).getIrow() + 2;

                            if (itemsList.get(l).getJrow() > 0 && itemsList.get(l).getJrow() < 33) {

                                if (itemsList.get(l).getJrow() > 1 && itemsList.get(l).getJrow() < 10) {
                                    colvalue =1+ itemsList.get(l).getJrow();
                                }
                                else if (((rows % 2 == 0) && itemsList.get(l).getJrow() > 9 && itemsList.get(l).getJrow() < 22)
                                        || (!(rows % 2 == 0) && itemsList.get(l).getJrow() > 9 && itemsList.get(l).getJrow() < 23)) {
                                    colvalue = 1 + itemsList.get(l).getJrow();
                                } else if (((rows % 2 == 0) && itemsList.get(l).getJrow() > 22)) {
                                    colvalue = 3 + itemsList.get(l).getJrow();
                                } else if ((!(rows % 2 == 0) && itemsList.get(l).getJrow() > 22)) {
                                    colvalue = 2 + itemsList.get(l).getJrow();
                                }
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.id =itemsList.get(l).getJrow();
                                seat.selectedSeatMarker = String.valueOf(seat.id);

                        }
                        else{
                            seat.status = HallScheme.SeatStatus.INFO;
                            seat.marker = rowname[rows];

                        }
                        }
                    }
                    seats[rows][colvalue]=seat;

                }
            }

        }
        return seats;
    }
}