package com.lognsys.kalrav.schemes;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lognsys.kalrav.HomeActivity;
import com.lognsys.kalrav.R;
import com.lognsys.kalrav.fragment.ConfirmFragment;
import com.lognsys.kalrav.model.Auditorium;
import com.lognsys.kalrav.model.AuditoriumPriceRange;
import com.lognsys.kalrav.model.SeatExample;

import java.util.ArrayList;
import java.util.List;

import by.anatoldeveloper.hallscheme.hall.HallScheme;
import by.anatoldeveloper.hallscheme.hall.ScenePosition;
import by.anatoldeveloper.hallscheme.hall.Seat;
import by.anatoldeveloper.hallscheme.hall.SeatListener;
import by.anatoldeveloper.hallscheme.view.ZoomableImageView;

/**
 * Created by Nublo on 06.12.2015.
 * Copyright Nublo
 */
public class SchemePrabhodhanFragment extends Fragment {

    private static final String TAG="SchemePrabhodhanFragment";

    List<SeatExample> itemsList;
    Auditorium auditorium;
    ArrayList<AuditoriumPriceRange> auditoriumPriceRangeList;
    Button btnProceed;
    HallScheme scheme;
    RecyclerView listViewPrices;
    ZoomableImageView imageView;
    MyAdapter adapter;
    int dramaInfoId;
    String time,strDate;
    String[] rowname = {"",
            "KK", "JJ", "HH", "GG",
            "FF", "EE", "DD", "CC",
            "BB", "AA", "", "",
            "Y", "X", "W", "V", "U", "T", "S", "R",
            "P", "O", "N", "SP", "M", "L", "K", "J",
            "H", "G", "F", "E", "D", "C", "B", "A"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.basic_scheme_fragment, container, false);
         imageView = (ZoomableImageView) rootView.findViewById(R.id.zoomable_image);
        auditorium= (Auditorium) getArguments().getSerializable("auditorium");

        itemsList= (List<SeatExample>) getArguments().getSerializable("itemsList");
        dramaInfoId=  getArguments().getInt("dramaInfoId");
        time=  getArguments().getString("time");
        strDate=  getArguments().getString("strDate");
        populateData(rootView);
        scheme = new HallScheme(imageView, basicScheme(), getActivity());
        scheme.setSceneName(getString(R.string.all_eye_here));
        scheme.setScenePosition(ScenePosition.SOUTH);
        return rootView;
    }

    private void switchFragment(Fragment fff) {
        if (getActivity() == null)
            return;
        if (getActivity() instanceof HomeActivity) {
            HomeActivity feeds = (HomeActivity) getActivity();
            feeds.switchContent(fff);
        }
    }

    private void populateData(View rootView) {
        if (auditorium!=null){
            auditoriumPriceRangeList= getPricerange(this.auditorium);
        }
        imageView = (ZoomableImageView) rootView.findViewById(R.id.zoomable_image);
        btnProceed = (Button) rootView.findViewById(R.id.btnProceed);
        listViewPrices = (RecyclerView) rootView.findViewById(R.id.pricelist);
        listViewPrices.setHasFixedSize(true);

        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        listViewPrices.setLayoutManager(MyLayoutManager);
        if(auditoriumPriceRangeList!=null && auditoriumPriceRangeList.size()>0){
            adapter =new MyAdapter(auditoriumPriceRangeList);
            listViewPrices.setAdapter( adapter);

        }

        scheme = new HallScheme(imageView, basicScheme(), getActivity());
        scheme.setScenePosition(ScenePosition.SOUTH);
        scheme.setSceneName(getString(R.string.all_eye_here));
        scheme.setSeatListener(new SeatListener() {

            @Override
            public void selectSeat(int id) {
                Toast.makeText(getActivity(), "select seat " + id, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void unSelectSeat(int id) {
                Toast.makeText(getActivity(), "unSelect seat " + id, Toast.LENGTH_SHORT).show();
            }

        });

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Seat> seatList=scheme.getListOfSelectedSeats();
                if (seatList != null && seatList.size() > 0) {
                    for (int i=0;i<seatList.size();i++){
                        SeatExample seat= (SeatExample) seatList.get(i);
                        Toast.makeText(getActivity(), "Your seat number : "+seat.marker()+seat.id()+"Your seat total price : "+seat.getTotal(), Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    Toast.makeText(getActivity(), "Please select atleast one seat to proceed further", Toast.LENGTH_SHORT).show();

                } ArrayList<Seat> items=(ArrayList<Seat>)seatList;
                Fragment fff=new ConfirmFragment();
                Bundle args = new Bundle();
                args.putSerializable("dramaInfoId",dramaInfoId);
                args.putString("time", time);
                args.putString("strDate", strDate);
                args.putSerializable("seats", (ArrayList<Seat>) items);
                fff.setArguments(args);
                if (fff != null){
                    switchFragment(fff);
                }
                else {
                    Toast.makeText(getActivity(), "Please select your seat no. ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public Seat[][] basicScheme() {//[40][52]
        Seat seats[][] = new Seat[37][37];


        for (int i = 0; i < 37; i++) {
            int k = 0;
            for (int j = 0; j < 37; j++) {
                SeatExample seat = new SeatExample();
                seats[i][j] = seat;
                seat.status = HallScheme.SeatStatus.EMPTY;
                seat.color = Color.argb(255, 60, 179, 113);
//              for 0th  row name : title
               /* if (i == 0 || i == 12) {
                    if (j == 19) {
                        seat.status = HallScheme.SeatStatus.INFO;
                        seat.price = 200;
                        seat.marker = rowname[i] + String.valueOf(seat.price);
                    }
                }*/
                // for KKth  to AAth  row
                if (i > 0) { // kk th  row
                    if (i == 1) {
                        if (j == 0) {
                            seat.status = HallScheme.SeatStatus.INFO;

                        }
                        if ((i == 1 && j > 11 && j < 24)) {
                            seat.id = ++k;
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.selectedSeatMarker = String.valueOf(seat.id);
                            Log.d(TAG, " kk th row");
                            seat.marker = rowname[i];
                        }
                    }
                    if (i > 1 && i < 3) {// jjth row

                        seat.marker = rowname[i];
                        if (j == 0) {
                            seat.status = HallScheme.SeatStatus.INFO;
                        }
                        if (j == 10 || j == 25) {
                            seat.status = HallScheme.SeatStatus.EMPTY;
                        }
                        if ((j > 5 && j < 10) || (j > 10 && j < 25) || (j > 25 && j < 30)) {
                            seat.id = ++k;
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.selectedSeatMarker = String.valueOf(seat.id);

                            Log.d(TAG, " kk th row");
                        }
                    }
                    if (i > 2 && i < 4) {//hh row

                        seat.marker = rowname[i];
                        if (j == 0) {
                            seat.status = HallScheme.SeatStatus.INFO;
                        }
                        if (j == 10 || j == 25) {
                            seat.status = HallScheme.SeatStatus.EMPTY;
                        }
                        if ((j > 3 && j < 10) || (j > 10 && j < 25) || (j > 25 && j < 33)) {
                            seat.id = ++k;
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.selectedSeatMarker = String.valueOf(seat.id);
                            seat.price = 200;
                        }
                    }
                    if (i > 3 && i < 8) { //gg, ff, ee, dd row
                        seat.marker = rowname[i];
                        if (j == 0) {
                            seat.status = HallScheme.SeatStatus.INFO;
                        }
                        if (j == 10 || j == 25) {
                            seat.status = HallScheme.SeatStatus.EMPTY;
                        }
                        if ((j > 0 && j < 10) || (j > 10 && j < 25) || (j > 25 && j < 35)) {
                            seat.id = ++k;
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.selectedSeatMarker = String.valueOf(seat.id);
                            seat.price = 200;
                        }
                    }
                    if (i > 7 && i < 10) { // cc, bb row
                        seat.marker = rowname[i];
                        if (j == 0) {
                            seat.status = HallScheme.SeatStatus.INFO;
                        }
                        if (j == 10 || j == 25) {
                            seat.status = HallScheme.SeatStatus.EMPTY;
                        }
                        if ((j > 1 && j < 10) || (j > 10 && j < 25) || (j > 25 && j < 34)) {
                            seat.id = ++k;
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.selectedSeatMarker = String.valueOf(seat.id);
                        }
                    }
                    if (i == 10) {// aa row
                        seat.marker = rowname[i];
                        if (j == 0) {
                            seat.status = HallScheme.SeatStatus.INFO;
                        }
                        if (j == 10 || j == 25) {
                            seat.status = HallScheme.SeatStatus.EMPTY;
                        }
                        if ((j > 0 && j < 10) || (j > 10 && j < 25) || (j > 25 && j < 32)) {
                            seat.id = ++k;
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.selectedSeatMarker = String.valueOf(seat.id);
                        }
                    }
                    if (i == 13) { //y th  row
                        seat.marker = rowname[i];
                        if (j == 0) {
                            seat.status = HallScheme.SeatStatus.INFO;
                            seat.marker = rowname[i];
                        }
                        if ((i == 13 && j > 12 && j < 23)) {
                            seat.id =1+( ++k);
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.selectedSeatMarker = String.valueOf(seat.id);
                        }
                    }
                    if (i == 14) {// X row
                        seat.marker = rowname[i];
                        if (j == 0) {
                            seat.status = HallScheme.SeatStatus.INFO;
                        }
                        if (j == 11 || j == 24) {
                            seat.status = HallScheme.SeatStatus.EMPTY;
                        }
                        if ((j > 8 && j < 11) || (j > 11 && j < 24) || (j > 24 && j < 27)) {
                            seat.id = ++k;
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.selectedSeatMarker = String.valueOf(seat.id);
                        }
                    }
                    if (i == 15) {// W row
                        seat.marker = rowname[i];
                        if (j == 0) {
                            seat.status = HallScheme.SeatStatus.INFO;
                        }
                        if (j == 11 || (j > 22 && j < 25)) {
                            seat.status = HallScheme.SeatStatus.EMPTY;
                        }
                        if ((j > 5 && j < 11) || (j > 11 && j < 23) || (j > 24 && j < 29)) {
                            seat.id = ++k;
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.selectedSeatMarker = String.valueOf(seat.id);
                        }
                    }
                    if (i == 16) {// V row
                        seat.marker = rowname[i];
                        if (j == 0) {
                            seat.status = HallScheme.SeatStatus.INFO;
                        }
                        if (j == 11 || j == 24) {
                            seat.status = HallScheme.SeatStatus.EMPTY;
                        }
                        if ((j > 3 && j < 11) || (j > 11 && j < 24) || (j > 24 && j < 33)) {
                            seat.id = ++k;
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.selectedSeatMarker = String.valueOf(seat.id);
                        }
                    }
                    if (i > 16 && i < 24) {// U row to N row
                        seat.marker = rowname[i];
                        if (j == 0) {
                            seat.status = HallScheme.SeatStatus.INFO;
                        }
                        if (j == 11 || j == 24) {
                            seat.status = HallScheme.SeatStatus.EMPTY;
                        }
                        if ((j > 0 && j < 11) || ((((i % 2 == 0) && (j > 11 && j < 24)) || (!(i % 2 == 0) && (j > 11 && j < 23)))) || (j > 24 && j < 35)) {
                            seat.id = ++k;
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.selectedSeatMarker = String.valueOf(seat.id);
                        }
                    }
                }
                if (i == 24) {// SP row
                    seat.marker = rowname[i];
                    if (j == 0) {
                        seat.status = HallScheme.SeatStatus.INFO;
                    }
                    if (j == 11 || j == 24) {
                        seat.status = HallScheme.SeatStatus.EMPTY;
                    }
                    if ((j > 1 && j < 11) || (j > 11 && j < 24) || (j > 24 && j < 34)) {
                        seat.id = ++k;
                        seat.status = HallScheme.SeatStatus.FREE;
                        seat.selectedSeatMarker = String.valueOf(seat.id);
                    }
                }
                if (i > 24 && i < 32) {// M row to F row
                    seat.marker = rowname[i];
                    if (j == 0) {
                        seat.status = HallScheme.SeatStatus.INFO;
                    }
                    if (j == 11 || j == 24) {
                        seat.status = HallScheme.SeatStatus.EMPTY;
                    }
                    if ((j > 0 && j < 11) || ((((i % 2 == 0) && (j > 11 && j < 23)) || (!(i % 2 == 0) && (j > 11 && j < 24)))) || (j > 24 && j < 35)) {
                        seat.id = ++k;
                        seat.status = HallScheme.SeatStatus.FREE;
                        seat.selectedSeatMarker = String.valueOf(seat.id);
                    }
                }
                if (i > 31 && i < 35) {// E row to C row
                    seat.marker = rowname[i];
                    if (j == 0) {
                        seat.status = HallScheme.SeatStatus.INFO;
                    }
                    if (j == 11 || j == 24) {
                        seat.status = HallScheme.SeatStatus.EMPTY;
                    }
                    if ((j > 1 && j < 11) || ((((i % 2 == 0) && (j > 11 && j < 23)) || (!(i % 2 == 0) && (j > 11 && j < 24)))) || (j > 24 && j < 34)) {
                        seat.id = ++k;
                        seat.status = HallScheme.SeatStatus.FREE;
                        seat.selectedSeatMarker = String.valueOf(seat.id);
                    }
                }
                if (i == 35) {// B row
                    seat.marker = rowname[i];
                    if (j == 0) {
                        seat.status = HallScheme.SeatStatus.INFO;
                    }
                    if (j == 11 || j == 24) {
                        seat.status = HallScheme.SeatStatus.EMPTY;
                    }
                    if ((j > 2 && j < 11) || (j > 11 && j < 24) || (j > 24 && j < 33)) {
                        seat.id = ++k;
                        seat.status = HallScheme.SeatStatus.FREE;
                        seat.selectedSeatMarker = String.valueOf(seat.id);
                    }
                }
                if (i == 36) {// A row
                    seat.marker = rowname[i];
                    if (j == 0) {
                        seat.status = HallScheme.SeatStatus.INFO;
                    }
                    if (j == 11 || (j > 22 && j < 25)) {
                        seat.status = HallScheme.SeatStatus.EMPTY;
                    }
                    if ((j > 3 && j < 11) || (j > 11 && j < 23) || (j > 24 && j < 32)) {
                        seat.id = ++k;
                        seat.status = HallScheme.SeatStatus.FREE;
                        seat.selectedSeatMarker = String.valueOf(seat.id);
                    }
                }
                if(i>0){
                    for(int p=0;p<auditoriumPriceRangeList.size();p++){
                        if(j>0 && j<49){
                            if(i>=auditoriumPriceRangeList.get(p).getIstart() && i<=auditoriumPriceRangeList.get(p).getIend()){
                                seat.price=auditoriumPriceRangeList.get(p).getPrice();
                            }
                        }

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
                            }else{
                                seat.status = HallScheme.SeatStatus.EMPTY;
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


                        if(itemsList.get(l).getIrow()>=10)
                        {
                            rows= itemsList.get(l).getIrow()+2;
                            if(rows==13){//                   row Y start
                                if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<12)
                                {
                                    colvalue=11+itemsList.get(l).getJrow();
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }  else{
                                    seat.status = HallScheme.SeatStatus.INFO;
                                    seat.marker = rowname[rows];
                                }// row Y End

                            }
                            if(rows==14){
                                // X row
                                if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<17){
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
                                }// X End
                            }
                            if(rows==15){// W row

                                if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<21){
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
                            }// W row End
                            if(rows==16){// V row
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
                            }// V row End
                            if(rows==17){// U row
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
                            }// U row End
                            if(rows==18){// T row
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
                            }// T row Ends
                            if(rows==19){// S row
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
                            }// S row End
                            if(rows==20){// R row
                                Log.d("","rows row R start "+rows);

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
                            }// R row
                            if(rows==21){// P row
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
                            }// P row
                            if(rows==22){// O row
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
                            }// O row
                            if(rows==23){// N row
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
                            }// N row
                            if(rows==24){// SP row

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
                            }// SP ends
                            if(rows>24&& rows<32){// M to  F row
                                    if (itemsList.get(l).getJrow() > 0 && itemsList.get(l).getJrow() < 32) {

                                        if (itemsList.get(l).getJrow() > 0 && itemsList.get(l).getJrow() < 11) {
                                            colvalue = itemsList.get(l).getJrow();
                                        }
                                        else if (((rows % 2 == 0) && itemsList.get(l).getJrow() > 10 && itemsList.get(l).getJrow() < 22)
                                                || (!(rows % 2 == 0) && itemsList.get(l).getJrow() > 10 && itemsList.get(l).getJrow() < 23)) {
                                            colvalue = 1 + itemsList.get(l).getJrow();
                                        } else if (((rows % 2 == 0) && itemsList.get(l).getJrow() > 21)) {
                                            colvalue = 3 + itemsList.get(l).getJrow();
                                        } else if ((!(rows % 2 == 0) && itemsList.get(l).getJrow() > 21)) {
                                            colvalue = 2 + itemsList.get(l).getJrow();
                                        }
                                        seat.color = Color.argb(255, 60, 179, 113);
                                        seat.id =itemsList.get(l).getJrow();
                                        seat.selectedSeatMarker = String.valueOf(seat.id);
                                    /*else{
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
                            }// M to  F row End
                            if(rows>31 && rows<35){// E row to C row
                                if (itemsList.get(l).getJrow() > 0 && itemsList.get(l).getJrow() < 31) {

                                    if (itemsList.get(l).getJrow() > 0 && itemsList.get(l).getJrow() < 10) {
                                        colvalue =1+ itemsList.get(l).getJrow();
                                    }
                                    else if (((rows % 2 == 0) && itemsList.get(l).getJrow() > 9 && itemsList.get(l).getJrow() < 21)
                                            || (!(rows % 2 == 0) && itemsList.get(l).getJrow() > 9 && itemsList.get(l).getJrow() < 22)) {
                                        colvalue = 2 + itemsList.get(l).getJrow();
                                    } else if (((rows % 2 == 0) && itemsList.get(l).getJrow() > 21)) {
                                        colvalue = 3 + itemsList.get(l).getJrow();
                                    } else if ((!(rows % 2 == 0) && itemsList.get(l).getJrow() > 21)) {
                                        colvalue = 3 + itemsList.get(l).getJrow();
                                    }
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(l).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);

                                }
                                else{
                                    seat.status = HallScheme.SeatStatus.INFO;
                                    seat.marker = rowname[rows];

                                }
                            }// E row to C row End
                            if(rows==35){// B row
                                if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<29){
                                    if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<21)
                                    {
                                        if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<9){
                                            colvalue=2+itemsList.get(l).getJrow();
                                        }
                                        else if(itemsList.get(l).getJrow()>8 && itemsList.get(l).getJrow()<21){
                                            colvalue=3+itemsList.get(l).getJrow();
                                        }
                                        else{
                                            colvalue=4+itemsList.get(l).getJrow();
                                        }
                                        seat.color = Color.argb(255, 60, 179, 113);
                                        seat.id =itemsList.get(l).getJrow();
                                        seat.selectedSeatMarker = String.valueOf(seat.id);
                                    }
                                    else {
                                        colvalue=4+itemsList.get(l).getJrow();
                                        seat.color = Color.argb(255, 60, 179, 113);
                                        seat.id =itemsList.get(l).getJrow();
                                        seat.selectedSeatMarker = String.valueOf(seat.id);

                                    }
                                }
                                else{
                                    seat.status = HallScheme.SeatStatus.INFO;
                                    seat.marker = rowname[rows];
                                }
                            }// B row End
                            if(rows==36){// A row
                                if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<26){
                                    if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<19)
                                    {
                                        if(itemsList.get(l).getJrow()>0 && itemsList.get(l).getJrow()<8){
                                            colvalue=3+itemsList.get(l).getJrow();
                                        }
                                        else if(itemsList.get(l).getJrow()>7 && itemsList.get(l).getJrow()<19){
                                            colvalue=4+itemsList.get(l).getJrow();
                                        }
                                        else{
                                            colvalue=6+itemsList.get(l).getJrow();
                                        }
                                        seat.color = Color.argb(255, 60, 179, 113);
                                        seat.id =itemsList.get(l).getJrow();
                                        seat.selectedSeatMarker = String.valueOf(seat.id);
                                    }
                                    else {
                                        colvalue=6+itemsList.get(l).getJrow();
                                        seat.color = Color.argb(255, 60, 179, 113);
                                        seat.id =itemsList.get(l).getJrow();
                                        seat.selectedSeatMarker = String.valueOf(seat.id);
                                    }
                                }
                                else{
                                    seat.status = HallScheme.SeatStatus.INFO;
                                    seat.marker = rowname[rows];
                                }
                            }// A row End
                        }
                    }
                    seats[rows][colvalue]=seat;
                }
            }

        }
        return seats;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<AuditoriumPriceRange> list;

        public MyAdapter(List<AuditoriumPriceRange> Data) {
            list = Data;
//            Log.d("", "MyAdapter constructore list "+list+" list size ==="+list.size());

        }
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {
            // create a new view
            View view=null;
            AuditoriumPriceRange auditorium =list.get(position);

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.auditoriumpricelist_fragmentdetail, parent, false);
            MyAdapter.MyViewHolder holder = new MyAdapter.MyViewHolder(view);


            return holder;


        }



        @Override
        public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position) {

            AuditoriumPriceRange auditorium = list.get(position);
            int iValue=auditorium.getIstart();
            String strIvalue =rowname[iValue];
            Log.d("", "MyAdapter  strIvalue==="+strIvalue);

            int iEndValue=auditorium.getIend();
            String strIEndvalue =rowname[iEndValue];
            Log.d("", "MyAdapter  strIvalue==="+strIvalue);
            holder.textIStart.setText("Row "+strIvalue);
            holder.textIEnd.setText(" To  "+strIEndvalue);
            holder.textPrice.setText("-Rs ."+String.valueOf(auditorium.getPrice()));
        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView textIStart;
            public TextView textIEnd;
            public TextView textPrice;


            public MyViewHolder(View itemView) {
                super(itemView);
                textIStart = (TextView) itemView.findViewById(R.id.textIStart);
                textIEnd = (TextView) itemView.findViewById(R.id.textIEnd);
                textPrice = (TextView) itemView.findViewById(R.id.textPrice);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition(); // gets item position

            }
        }
    }

    private ArrayList<AuditoriumPriceRange> getPricerange(Auditorium auditorium) {
        ArrayList<AuditoriumPriceRange> lists=new ArrayList<AuditoriumPriceRange>();
        if(auditorium!=null && auditorium.getAuditoriumPriceRanges()!=null && auditorium.getAuditoriumPriceRanges().size()>0){
            for(int i=0;i<auditorium.getAuditoriumPriceRanges().size();i++){

                lists.add(auditorium.getAuditoriumPriceRanges().get(i));
            }
        }
        return lists;
    }
}