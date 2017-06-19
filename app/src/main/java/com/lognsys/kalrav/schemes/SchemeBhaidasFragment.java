package com.lognsys.kalrav.schemes;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.lognsys.kalrav.R;
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
public class SchemeBhaidasFragment extends Fragment {

    Auditorium auditorium;
    ArrayList<AuditoriumPriceRange> auditoriumPriceRangeList;
    List<SeatExample> itemsList;
    ZoomableImageView imageView;
    Button btnProceed;
    HallScheme scheme;
    int n=91;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.basic_scheme_fragment, container, false);

        ZoomableImageView imageView = (ZoomableImageView) rootView.findViewById(R.id.zoomable_image);
        itemsList= (List<SeatExample>) getArguments().getSerializable("itemsList");
        Log.d("BHAIDAS"," BHAIDAS itemsList === "+(itemsList)+ "itemsList SIZE "+itemsList.size());

        final HallScheme scheme = new HallScheme(imageView, basicScheme(), getActivity());
        scheme.setSceneName(getString(R.string.all_eye_here));
        scheme.setScenePosition(ScenePosition.SOUTH);
        Button btnProceed=(Button)rootView.findViewById(R.id.btnProceed);
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Seat> seatList=scheme.getListOfSelectedSeats();
                if (seatList != null && seatList.size() > 0) {
                    for (int i=0;i<seatList.size();i++){
                        Seat seat=seatList.get(i);
                        Toast.makeText(getActivity(), "Your seat number : "+seat.marker()+seat.id()+"Your seat total price : "+seat.getTotal(), Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    Toast.makeText(getActivity(), "Please select atleast one seat to proceed further", Toast.LENGTH_SHORT).show();

                }
//                ArrayList<Seat> items=(ArrayList<Seat>)seatList;
//                Fragment fff=new ConfirmFragment();
//                Bundle args = new Bundle();
////                args.putInt("totalPrice", totalPrice);
////                args.putSerializable("timeSlot",timeSlot);
////                args.putSerializable("dramaInfo",dramaInfo);
//                args.putSerializable("seats", (ArrayList<Seat>) items);
//                fff.setArguments(args);
//                if (fff != null){
//                    switchFragment(fff);
//                }
//                else {
//                    Toast.makeText(getActivity(), "Please select your seat no. ", Toast.LENGTH_SHORT).show();
//                }
            }
        });
        return rootView;
    }

    public Seat[][] basicScheme() {//[40][52]
        Seat seats[][] = new Seat[37][50];


        String[] rowname={"GG","","FF","EE","DD","CC","BB","AA","Z","Y","","X","W","V","U","T","S","R","","Q","P","O","N","M","L","K","J","H","G","F","E","D","C","B","A","A1"};
        for (int i = 0; i < 37; i++){
            int k = 0;
            int a1throw = 12;
            int athrow = 9;
            int bthrow = 8;
            int cthrow = 7;
            int dthrow = 6;
            int ethrow = 5;
            int fthrow = 4;
            int gthrow = 3;

                for(int j = 0; j < 50; j++) {
                    try {
                        SeatExample seat = new SeatExample();
                    seats[i][j] = seat;
                    seat.status = HallScheme.SeatStatus.EMPTY;
                    seat.color = Color.argb(255, 60, 179, 113);
//              for Gth  row
                    if(i==1){
                        if (j == 0 || j == 49) {

                            seat.status = HallScheme.SeatStatus.INFO;
                            seat.marker=rowname[0];
                            seat.selectedSeatMarker= seat.marker;
                        }
                        else{
                            seat.id = ++k;
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.marker=rowname[0];
                            seat.selectedSeatMarker=String.valueOf(seat.id);
                            seat.price=200;
                        }
                    }


                    // for FFth  to Rth  row
                    if(i>2){
//              i==11  is space after Yth  row
                        if(i!=11 && i!=19 && j>0 && j<50) {

//                       setting row name
                            if (i<19 && (j == 2 || j == 19 || j == 32 || j == 49)) {
                                seat.status = HallScheme.SeatStatus.INFO;
//                        rowname is a string arry  consist of string in array
                                seat.marker = rowname[i - 1];
                            } else {
                                //from Uth row to  Rth  row there is 10 seats
                                if(i>14 && i<19){
                                    //                          left seats +space+ middle seats +space  +right seats
                                    if((j>2 && j<13) || (j>20 && j<31) || (j>38 && j<49)){

                                        seat.id = ++k;
                                        if(seat.id>10){
                                            seat.id=k+5;
                                        }
                                        if(seat.id>25){
                                            seat.id=seat.id+5;
                                        }

                                        seat.marker = rowname[i - 1];
                                        seat.status = HallScheme.SeatStatus.FREE;
                                        seat.selectedSeatMarker=String.valueOf(seat.id);
                                        seat.price=200;
                                    }
                                    else{
//                                    seat.id = ++k;

                                        seat.status = HallScheme.SeatStatus.EMPTY;
                                    }
                                }
                                else if(i>19 && i<25){// Q to Mth row
                                    if ((j == 3 || j == 17 || j == 34 || j == 48)) {
                                        seat.status = HallScheme.SeatStatus.INFO;
//                                  rowname is a string arry  consist of string in array
                                        seat.marker = rowname[i - 1];
//                                   seat.id = ++k;
                                    }
                                    if((j>3 && j<16) || (j>18 && j<33) || (j>35 && j<48)){
                                        seat.id = ++k;
                                        seat.status = HallScheme.SeatStatus.FREE;
                                        seat.marker = rowname[i - 1];
                                        seat.selectedSeatMarker=String.valueOf(seat.id);
                                        seat.price=200;
                                    }

                                }// Q to Mth row ends
                                else if(i>24 && i<27){// L to Kth row
                                    if ((j == 4 || j == 17 || j == 34 || j == 47)) {
                                        seat.status = HallScheme.SeatStatus.INFO;
//                                  rowname is a string arry  consist of string in array
                                        seat.marker = rowname[i - 1];
//                                   seat.id = ++k;
                                    }
                                    if((j>4 && j<16) || (j>18 && j<33) || (j>35 && j<47)){
                                        seat.id =1+(++k);
                                        seat.status = HallScheme.SeatStatus.FREE;
                                        seat.marker = rowname[i - 1];
                                        seat.selectedSeatMarker=String.valueOf(seat.id);
                                        seat.price=200;
                                    }

                                }// L to Kth row ends
                                else if(i>26 && i<29){// J to Hth row
                                    if ((j == 5 || j == 17 || j == 34 || j == 46)) {
                                        seat.status = HallScheme.SeatStatus.INFO;
//                                  rowname is a string arry  consist of string in array
                                        seat.marker = rowname[i - 1];
//                                   seat.id = ++k;
                                    }
                                    if((j>5 && j<16) || (j>18 && j<33) || (j>35 && j<46)){
                                        seat.id = (2+(++k));
                                        seat.status = HallScheme.SeatStatus.FREE;
                                        seat.marker = rowname[i - 1];
                                        seat.selectedSeatMarker=String.valueOf(seat.id);
                                        seat.price=200;
                                    }

                                }// J to Hth row
                                else if(i==29){// Gth row
                                    if ((j == 6 || j == 17 || j == 34 || j == 45)) {
                                        seat.status = HallScheme.SeatStatus.INFO;
//                                  rowname is a string arry  consist of string in array
                                        seat.marker = rowname[i - 1];
//                                   seat.id = ++k;
                                    }
                                    if((j>6 && j<16) || (j>18 && j<33) || (j>35 && j<45)){
                                        seat.id = ++gthrow;
                                        seat.status = HallScheme.SeatStatus.FREE;
                                        seat.marker = rowname[i - 1];
                                        seat.selectedSeatMarker=String.valueOf(seat.id);
                                        seat.price=200;
                                    }

                                }// Gth row ends
                                else if(i==30){// F row
                                    if ((j == 7 || j == 17 || j == 34 || j == 44)) {
                                        seat.status = HallScheme.SeatStatus.INFO;
//                                  rowname is a string arry  consist of string in array
                                        seat.marker = rowname[i - 1];
//                                   seat.id = ++k;
                                    }
                                    if((j>7 && j<16) || (j>18 && j<33) || (j>35 && j<44)){
                                        seat.id = ++fthrow;
                                        seat.status = HallScheme.SeatStatus.FREE;
                                        seat.marker = rowname[i - 1];
                                        seat.selectedSeatMarker=String.valueOf(seat.id);
                                        seat.price=200;
                                    }

                                }// Fth row  ends
                                else if(i==31){ //E row
                                    if ((j == 8 || j == 17 || j == 34 || j == 43)) {
                                        seat.status = HallScheme.SeatStatus.INFO;
//                                  rowname is a string arry  consist of string in array
                                        seat.marker = rowname[i - 1];
//                                   seat.id = ++k;
                                    }
                                    if((j>8 && j<16) || (j>18 && j<33) || (j>35 && j<43)){
                                        seat.id = ++ethrow;
                                        seat.status = HallScheme.SeatStatus.FREE;
                                        seat.marker = rowname[i - 1];
                                        seat.selectedSeatMarker=String.valueOf(seat.id);
                                        seat.price=200;
                                    }

                                }// Eth row ends
                                else if(i==32){ //D row
                                    if ((j == 9 || j == 17 || j == 34 || j == 42)) {
                                        seat.status = HallScheme.SeatStatus.INFO;
//                                  rowname is a string arry  consist of string in array
                                        seat.marker = rowname[i - 1];
//                                   seat.id = ++k;
                                    }
                                    if((j>9 && j<16) || (j>18 && j<33) || (j>35 && j<42)){
                                        seat.id = ++dthrow;
                                        seat.status = HallScheme.SeatStatus.FREE;
                                        seat.marker = rowname[i - 1];
                                        seat.selectedSeatMarker=String.valueOf(seat.id);
                                        seat.price=200;
                                    }

                                }// Dth row ends
                                else if(i==33){ //C row
                                    if ((j == 10 || j == 17 || j == 34 || j == 41)) {
                                        seat.status = HallScheme.SeatStatus.INFO;
//                                  rowname is a string arry  consist of string in array
                                        seat.marker = rowname[i - 1];
//                                   seat.id = ++k;
                                    }
                                    if((j>10 && j<16) || (j>18 && j<33) || (j>35 && j<41)){
                                        seat.id = ++cthrow;
                                        seat.status = HallScheme.SeatStatus.FREE;
                                        seat.marker = rowname[i - 1];
                                        seat.selectedSeatMarker=String.valueOf(seat.id);
                                        seat.price=200;
                                    }

                                }// Cth row ends
                                else if(i==34){ //B row
                                    if ((j == 11 || j == 17 || j == 34 || j == 40)) {
                                        seat.status = HallScheme.SeatStatus.INFO;
//                                  rowname is a string arry  consist of string in array
                                        seat.marker = rowname[i - 1];
                                        //   seat.id = ++k;
                                    }
                                    if((j>11 && j<16) || (j>18 && j<33) || (j>35 && j<40)){
                                        seat.id = ++bthrow;
                                        seat.status = HallScheme.SeatStatus.FREE;
                                        seat.marker = rowname[i - 1];
                                        seat.selectedSeatMarker=String.valueOf(seat.id);
                                        seat.price=200;
                                    }

                                }// Bth row ends
                                else if(i==35){ // Ath row
                                    if ((j == 12 || j == 17 || j == 34 || j == 39)) {
                                        seat.status = HallScheme.SeatStatus.INFO;
//                                  rowname is a string arry  consist of string in array
                                        seat.marker = rowname[i - 1];
//                                   seat.id = ++k;
                                    }
                                    if((j>12 && j<16) || (j>18 && j<33) || (j>35 && j<39)){
                                        seat.id = ++athrow;
                                        seat.status = HallScheme.SeatStatus.FREE;
                                        seat.marker = rowname[i - 1];
                                        seat.selectedSeatMarker=String.valueOf(seat.id);
                                        seat.price=200;
                                    }

                                }// Ath row ends
                                else if(i==36){ // A1th row
                                    if (( j == 17 || j == 34 )) {
                                        seat.status = HallScheme.SeatStatus.INFO;
//                                  rowname is a string arry  consist of string in array
                                        seat.marker = rowname[i - 1];
//                                   seat.id = ++k;
                                    }
                                    if( (j>18 && j<33)){
                                        seat.id = ++a1throw;
                                        seat.status = HallScheme.SeatStatus.FREE;
                                        seat.marker = rowname[i - 1];
                                        seat.selectedSeatMarker=String.valueOf(seat.id);
                                        seat.price=200;
                                    }
                                }// A1th row ends
                                else{
//                             left seats +middle seats +right seats

                                    seat.id = (++k)-1;
                                    if(seat.id>17){
                                        seat.id=seat.id-2;
                                    }
                                    if(seat.id>27){
                                        seat.id=seat.id-2;
                                    }
                                    Log.d("", "Bhaidas left seats +middle seats +right seats =========== " + (seat.id));
                                    seat.status = HallScheme.SeatStatus.FREE;
                                    seat.marker = rowname[i - 1];
                                    seat.selectedSeatMarker=String.valueOf(seat.id);
                                    seat.price=200;


                                }

                            }
                            if ((i<19)&& (j == 20 || j == 18 || j == 1 || j == 31 || j == 33)) {
                                seat.status = HallScheme.SeatStatus.EMPTY;
                            }
                        }
                        else{
                            seat.status = HallScheme.SeatStatus.EMPTY;
                        }


                    }
                }
            catch (ArrayIndexOutOfBoundsException e){
                    Toast.makeText(getActivity(),"Seats Are not in Range ",Toast.LENGTH_SHORT).show();
                }
                }


//            incrementing by  2 rows range of rows 1 to 28
            int rows=0;
            for( k=0;k<itemsList.size();k++){
                for(int m=0;m<itemsList.size();m++){
                    SeatExample seat = new SeatExample();
                    seat.status = HallScheme.SeatStatus.BUSY;
                    int colvalue=0;
                    if(itemsList.get(k).getIrow()>=12)
                    {
                        rows=itemsList.get(k).getIrow()+2;
//                   row (M to 0) start               <16                                  >13
                        if(rows>13 && rows<17 && itemsList.get(k).getIrow()<=rows)
                        {

                            if(itemsList.get(k).getJrow()>20 && itemsList.get(k).getJrow()<27)
                            {
                                colvalue=33-itemsList.get(k).getJrow()-6;
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.id =itemsList.get(k).getJrow();
                                seat.selectedSeatMarker = String.valueOf(seat.id);
                            }
                            else if(itemsList.get(k).getJrow()>6 && itemsList.get(k).getJrow()<21)
                            {
                                colvalue=33-itemsList.get(k).getJrow()-3;
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.id =itemsList.get(k).getJrow();
                                seat.selectedSeatMarker = String.valueOf(seat.id);
                            }
                            else{
                                colvalue=33-itemsList.get(k).getJrow();
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.id =itemsList.get(k).getJrow();
                                seat.selectedSeatMarker = String.valueOf(seat.id);
                            }
                        }//                   row M to O ends

//                   row H to L start                   <=21                                >16
                        if(rows>16 && rows<21 && itemsList.get(k).getJrow()>0 && itemsList.get(k).getJrow()<19)
                        {

                            if(itemsList.get(k).getJrow()>16 && itemsList.get(k).getJrow()<19)
                            {
                                colvalue=24-itemsList.get(k).getJrow()-1;
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.id =itemsList.get(k).getJrow();
                                seat.selectedSeatMarker = String.valueOf(seat.id);

                            }
                            else if(itemsList.get(k).getJrow()>2 && itemsList.get(k).getJrow()<17)
                            {
                                colvalue=26-itemsList.get(k).getJrow();
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.id =itemsList.get(k).getJrow();
                                seat.selectedSeatMarker = String.valueOf(seat.id);

                            }
                            else{
                                if(itemsList.get(k).getJrow()>0 && itemsList.get(k).getJrow()<3 ) {
                                    colvalue = 29 - itemsList.get(k).getJrow();
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id = itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                            }
                        }//                   row H to L ends
                        else{
                            if(rows<21 && rows>16){
                                int values = (n - (rows - 2));
                                if(values<74){
                                    values=values-1;
                                }
                                char character = (char) values;

                                seat.marker = String.valueOf(character);
                                seat.status = HallScheme.SeatStatus.INFO;
                            }
                        }
//                   row G start
                        if(rows ==21)
                        {
                            if(itemsList.get(k).getJrow()>0 && itemsList.get(k).getJrow()<15)
                            {
                                colvalue=26-itemsList.get(k).getJrow()-2;

                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.id =itemsList.get(k).getJrow();
                                seat.selectedSeatMarker = String.valueOf(seat.id);

                            }else{
                                if(rows==21){
                                    int values = (n - (rows - 2));
                                    if(values<74){
                                        values=values-1;
                                    }
                                    char character = (char) values;

                                    seat.marker = String.valueOf(character);
                                    seat.status = HallScheme.SeatStatus.INFO;
                                }
                            }
                        }//                   row G ends

                        //                   row F to C start          >=25                                    <29
                        if(rows>21 && rows<28 )
                        {
                            rows=rows+2;
                            Log.d("","itemsList === rows inside rows itemsList.get(k).getJrow() ===== "+(itemsList.get(k).getJrow()));

                            colvalue=32-itemsList.get(k).getJrow()-4;
//                        Log.d("","itemsList findAllItems colvalue ================ "+colvalue);
                            if(colvalue>3 && colvalue<16)
                            {
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.id =itemsList.get(k).getJrow();
                                seat.selectedSeatMarker = String.valueOf(seat.id);
                            }
                            else{
                                if(itemsList.get(k).getJrow()==1){
                                    Log.d("","getJrow AT K ==========="+itemsList.get(k).getJrow()
                                            +" K ========== "+k
                                            +" k+1 ==="+(k+1)
                                            +" 32-itemsList.get(k).getJrow()-k+1 ==="+(32-itemsList.get(k).getJrow()-k+1));

                                    colvalue=31-itemsList.get(k).getJrow();
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }else{
                                    colvalue=31-itemsList.get(k).getJrow();
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                seat.color = Color.argb(255, 60, 179, 113);

                            }
                        }//                   row F to C ends

//                   row B start
                        if(rows == 28)
                        {

                            if(itemsList.get(k).getJrow()>0 && itemsList.get(k).getJrow()<12)
                            {
                                colvalue=30-itemsList.get(k).getJrow();

                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.id =itemsList.get(k).getJrow();
                                seat.selectedSeatMarker = String.valueOf(seat.id);

                            }else if(itemsList.get(k).getJrow()>11 && itemsList.get(k).getJrow()<23)
                            {
                                colvalue=30- itemsList.get(k).getJrow()-3;
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.id =itemsList.get(k).getJrow();
                                seat.selectedSeatMarker = String.valueOf(seat.id);
                            }
                            else if(itemsList.get(k).getJrow()>22)
                            {
                                colvalue=30- itemsList.get(k).getJrow()-3;

                                if(colvalue==0){
                                    int values = (n - (rows - 2));
                                    if(values<74){
                                        values=values-1;
                                    }
                                    char character = (char) values;

                                    seat.marker = String.valueOf(character);
                                    seat.status = HallScheme.SeatStatus.INFO;
                                }
                                else{
                                    seat.status = HallScheme.SeatStatus.EMPTY;
                                }

                            }

                        }//                   row B ends

//                   row A start
                        if(rows==29 )
                        {
                            if(itemsList.get(k).getJrow()>0 && itemsList.get(k).getJrow()<10)
                            {
                                colvalue=28-itemsList.get(k).getJrow();

                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.id =itemsList.get(k).getJrow();
                                seat.selectedSeatMarker = String.valueOf(seat.id);

                            }else if(itemsList.get(k).getJrow()>9 && itemsList.get(k).getJrow()<19)
                            {
                                colvalue=28 - itemsList.get(k).getJrow()-3;
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.id =itemsList.get(k).getJrow();
                                seat.selectedSeatMarker = String.valueOf(seat.id);
                            } else{
                                if(itemsList.get(k).getJrow()>21)
                                    seat.status = HallScheme.SeatStatus.EMPTY;
                            }
                        }
                        //                   row A ends
                    }else{
                        rows=itemsList.get(k).getIrow();
//                   row (FF to Y) start
                        if(rows>1 && rows<10)
                        {
                            rows= itemsList.get(k).getIrow()+1;
                            if(itemsList.get(k).getJrow()>25 && itemsList.get(k).getJrow()<41)
                            {
                                colvalue=50-itemsList.get(k).getJrow()-2;
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.id =itemsList.get(k).getJrow();
                                seat.selectedSeatMarker = String.valueOf(seat.id);
                            }
                            else if(itemsList.get(k).getJrow()>15 && itemsList.get(k).getJrow()<26)
                            {
                                colvalue=40-itemsList.get(k).getJrow()-3;
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.id =itemsList.get(k).getJrow();
                                seat.selectedSeatMarker = String.valueOf(seat.id);
                            }
                            else{
                                if(itemsList.get(k).getJrow()>0 && itemsList.get(k).getJrow()<16){
                                    colvalue=2+itemsList.get(k).getJrow();
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else{
                                    {
                                        seat.status = HallScheme.SeatStatus.EMPTY;
                                        seat.marker=rowname[rows];
                                    }
                                }
                            }
                        }//                   row FF to Y ends


//                   row GG start
                        if(itemsList.get(k).getIrow()==1)
                        {
                            rows= itemsList.get(k).getIrow();

                            if(itemsList.get(k).getJrow()>0 && itemsList.get(k).getJrow()<49)
                            {
                                colvalue=itemsList.get(k).getJrow();
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.id =itemsList.get(k).getJrow();
                                seat.selectedSeatMarker = String.valueOf(seat.id);
                            }
                            else{
                                seat.status = HallScheme.SeatStatus.INFO;
                                seat.marker=rowname[0];
                            }
                        }//                   row GG ends


                    }
                    seats[rows][colvalue]=seat;

                }
            }

        }
        return seats;
    }

}