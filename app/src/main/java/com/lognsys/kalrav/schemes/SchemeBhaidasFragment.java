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
public class SchemeBhaidasFragment extends Fragment {
    String[] rowname={"GG","","FF","EE","DD","CC","BB","AA","Z","Y","","X","W","V","U","T",
            "S","R","","Q","P","O","N","M","L","K","J","H","G","F","E","D","C","B","A","A1"};
    Auditorium auditorium;
    ArrayList<AuditoriumPriceRange> auditoriumPriceRangeList;
    List<SeatExample> itemsList;
    ZoomableImageView imageView;
    Button btnProceed;
    HallScheme scheme;
    RecyclerView listViewPrices;
    MyAdapter adapter;
    int n=91;
    int dramaInfoId;
    String time,strDate;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.basic_scheme_fragment, container, false);
        auditorium= (Auditorium) getArguments().getSerializable("auditorium");
        itemsList= (List<SeatExample>) getArguments().getSerializable("itemsList");
        dramaInfoId=  getArguments().getInt("dramaInfoId");
        time=  getArguments().getString("time");
        strDate=  getArguments().getString("strDate");
        populateData(rootView);

        return rootView;
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

        adapter =new MyAdapter(auditoriumPriceRangeList);

        listViewPrices.setAdapter( adapter);

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

                }  ArrayList<Seat> items=(ArrayList<Seat>)seatList;
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

    private void switchFragment(Fragment fff) {
        if (getActivity() == null)
            return;
        if (getActivity() instanceof HomeActivity) {
            HomeActivity feeds = (HomeActivity) getActivity();
            feeds.switchContent(fff);
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<SchemeBhaidasFragment.MyAdapter.MyViewHolder> {
        private List<AuditoriumPriceRange> list;

        public MyAdapter(List<AuditoriumPriceRange> Data) {
            list = Data;
//            Log.d("", "MyAdapter constructore list "+list+" list size ==="+list.size());

        }
        @Override
        public SchemeBhaidasFragment.MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {
            // create a new view
            View view=null;
            AuditoriumPriceRange auditorium =list.get(position);

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.auditoriumpricelist_fragmentdetail, parent, false);
            SchemeBhaidasFragment.MyAdapter.MyViewHolder holder = new SchemeBhaidasFragment.MyAdapter.MyViewHolder(view);


            return holder;


        }



        @Override
        public void onBindViewHolder(SchemeBhaidasFragment.MyAdapter.MyViewHolder holder, int position) {

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
    public Seat[][] basicScheme() {//[40][52]
        Seat seats[][] = new Seat[40][50];



        for (int i = 0; i < 40; i++){
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
                                    }
                                }// A1th row ends
                                else{
//                             left seats +middle seats +right seats
                                    if(!(i>36)){
                                        seat.id = (++k)-1;
                                        if(seat.id>17){
                                            seat.id=seat.id-2;
                                        }
                                        if(seat.id>27){
                                            seat.id=seat.id-2;
                                        }
                                       seat.status = HallScheme.SeatStatus.FREE;
                                        seat.marker = rowname[i - 1];
                                        seat.selectedSeatMarker=String.valueOf(seat.id);


                                    }
                                    else{
                                        seat.status=HallScheme.SeatStatus.EMPTY;
                                    }


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
                    if(itemsList.get(k).getIrow()>=10)
                    {
                        rows=itemsList.get(k).getIrow()+2;
//                   row (X to V) start
                        if(rows>11 && rows<15 )
                        {
                            if(itemsList.get(k).getJrow()>25 && itemsList.get(k).getJrow()<41)
                            {
                                colvalue=65-itemsList.get(k).getJrow();
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.id =itemsList.get(k).getJrow();
                                seat.selectedSeatMarker = String.valueOf(seat.id);
                            }
                            else if(itemsList.get(k).getJrow()>15 && itemsList.get(k).getJrow()<26)
                            {
                                colvalue=itemsList.get(k).getJrow()+5;
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
                        }//                   row X to V ends

//                   row U to R start                   <=21                                >16
                        if(rows>14 && rows<19)
                        {

                            if(itemsList.get(k).getJrow()>30 && itemsList.get(k).getJrow()<41)
                            {
                                colvalue=itemsList.get(k).getJrow()+8;
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.id =itemsList.get(k).getJrow();
                                seat.selectedSeatMarker = String.valueOf(seat.id);
                            }
                            else if(itemsList.get(k).getJrow()>15 && itemsList.get(k).getJrow()<26)
                            {
                                colvalue=itemsList.get(k).getJrow()+5;
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.id =itemsList.get(k).getJrow();
                                seat.selectedSeatMarker = String.valueOf(seat.id);
                            }
                            else{
                                if(itemsList.get(k).getJrow()>0 && itemsList.get(k).getJrow()<11){
                                    colvalue=2+itemsList.get(k).getJrow();
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else{
                                        seat.status = HallScheme.SeatStatus.EMPTY;
                                        seat.marker=rowname[rows];
                                    }

                                }
                        }//                   row U to R ends

//                   row Q to M start                   <=21                                >16
                        if(rows>18 && rows<37)
                        {

                            rows=rows+1;
//                             row Q to M start
                            if(rows>19 && rows<25){
                                if(itemsList.get(k).getJrow()>26 && itemsList.get(k).getJrow()<39)
                                {
                                    colvalue=itemsList.get(k).getJrow()+9;
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else if(itemsList.get(k).getJrow()>12 && itemsList.get(k).getJrow()<27)
                                {
                                    colvalue=itemsList.get(k).getJrow()+6;
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else{
                                    if(itemsList.get(k).getJrow()>0 && itemsList.get(k).getJrow()<13){
                                        colvalue=3+itemsList.get(k).getJrow();
                                        seat.color = Color.argb(255, 60, 179, 113);
                                        seat.id =itemsList.get(k).getJrow();
                                        seat.selectedSeatMarker = String.valueOf(seat.id);
                                    }
                                    else{
                                        seat.status = HallScheme.SeatStatus.EMPTY;
                                        seat.marker=rowname[rows];
                                    }

                                }
                            }// row Q to M ends

//                   row L to K start                   <=21                                >16
                            if(rows>24 && rows<27)
                            {

                                if(itemsList.get(k).getJrow()>26 && itemsList.get(k).getJrow()<38)
                                {
                                    colvalue=itemsList.get(k).getJrow()+9;
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else if(itemsList.get(k).getJrow()>12 && itemsList.get(k).getJrow()<27)
                                {
                                    colvalue=itemsList.get(k).getJrow()+6;
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else{
                                    if(itemsList.get(k).getJrow()>1 && itemsList.get(k).getJrow()<13){
                                        colvalue=3+itemsList.get(k).getJrow();
                                        seat.color = Color.argb(255, 60, 179, 113);
                                        seat.id =itemsList.get(k).getJrow();
                                        seat.selectedSeatMarker = String.valueOf(seat.id);
                                    }
                                    else{
                                        seat.status = HallScheme.SeatStatus.EMPTY;
                                        seat.marker=rowname[rows];
                                    }

                                }
                            }//    L to K Ends

//                   row J to H start                   <=21                                >16
                            if(rows>26 && rows<29)
                            {

                                if(itemsList.get(k).getJrow()>26 && itemsList.get(k).getJrow()<37)
                                {
                                    colvalue=itemsList.get(k).getJrow()+9;
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else if(itemsList.get(k).getJrow()>12 && itemsList.get(k).getJrow()<27)
                                {
                                    colvalue=itemsList.get(k).getJrow()+6;
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else{
                                    if(itemsList.get(k).getJrow()>2 && itemsList.get(k).getJrow()<13){
                                        colvalue=3+itemsList.get(k).getJrow();
                                        seat.color = Color.argb(255, 60, 179, 113);
                                        seat.id =itemsList.get(k).getJrow();
                                        seat.selectedSeatMarker = String.valueOf(seat.id);
                                    }
                                    else{
                                        seat.status = HallScheme.SeatStatus.EMPTY;
                                        seat.marker=rowname[rows];
                                    }

                                }
                            }//    J to H Ends

//                   row G start
                            if(rows ==29)
                            {
                                if(itemsList.get(k).getJrow()>26 && itemsList.get(k).getJrow()<36)
                                {
                                    colvalue=itemsList.get(k).getJrow()+9;
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else if(itemsList.get(k).getJrow()>12 && itemsList.get(k).getJrow()<27)
                                {
                                    colvalue=itemsList.get(k).getJrow()+6;
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else{
                                    if(itemsList.get(k).getJrow()>3 && itemsList.get(k).getJrow()<13){
                                        colvalue=3+itemsList.get(k).getJrow();
                                        seat.color = Color.argb(255, 60, 179, 113);
                                        seat.id =itemsList.get(k).getJrow();
                                        seat.selectedSeatMarker = String.valueOf(seat.id);
                                    }
                                    else{
                                        seat.status = HallScheme.SeatStatus.EMPTY;
                                        seat.marker=rowname[rows];
                                    }

                                }
                            }//                   row G ends

//                   row F start
                            if(rows ==30)
                            {
                                if(itemsList.get(k).getJrow()>26 && itemsList.get(k).getJrow()<35)
                                {
                                    colvalue=itemsList.get(k).getJrow()+9;
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else if(itemsList.get(k).getJrow()>12 && itemsList.get(k).getJrow()<27)
                                {
                                    colvalue=itemsList.get(k).getJrow()+6;
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else{
                                    if(itemsList.get(k).getJrow()>4 && itemsList.get(k).getJrow()<13){
                                        colvalue=3+itemsList.get(k).getJrow();
                                        seat.color = Color.argb(255, 60, 179, 113);
                                        seat.id =itemsList.get(k).getJrow();
                                        seat.selectedSeatMarker = String.valueOf(seat.id);
                                    }
                                    else{
                                        seat.status = HallScheme.SeatStatus.EMPTY;
                                        seat.marker=rowname[rows];
                                    }

                                }
                            }//                   row F ends

//                   row E start
                            if(rows ==31)
                            {
                                if(itemsList.get(k).getJrow()>26 && itemsList.get(k).getJrow()<34)
                                {
                                    colvalue=itemsList.get(k).getJrow()+9;
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else if(itemsList.get(k).getJrow()>12 && itemsList.get(k).getJrow()<27)
                                {
                                    colvalue=itemsList.get(k).getJrow()+6;
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else{
                                    if(itemsList.get(k).getJrow()>5 && itemsList.get(k).getJrow()<13){
                                        colvalue=3+itemsList.get(k).getJrow();
                                        seat.color = Color.argb(255, 60, 179, 113);
                                        seat.id =itemsList.get(k).getJrow();
                                        seat.selectedSeatMarker = String.valueOf(seat.id);
                                    }
                                    else{
                                        seat.status = HallScheme.SeatStatus.EMPTY;
                                        seat.marker=rowname[rows];
                                    }

                                }
                            }//                   row E ends

//                   row D start
                            if(rows ==32)
                            {
                                if(itemsList.get(k).getJrow()>26 && itemsList.get(k).getJrow()<33)
                                {
                                    colvalue=itemsList.get(k).getJrow()+9;
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else if(itemsList.get(k).getJrow()>12 && itemsList.get(k).getJrow()<27)
                                {
                                    colvalue=itemsList.get(k).getJrow()+6;
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else{
                                    if(itemsList.get(k).getJrow()>6 && itemsList.get(k).getJrow()<13){
                                        colvalue=3+itemsList.get(k).getJrow();
                                        seat.color = Color.argb(255, 60, 179, 113);
                                        seat.id =itemsList.get(k).getJrow();
                                        seat.selectedSeatMarker = String.valueOf(seat.id);
                                    }
                                    else{
                                        seat.status = HallScheme.SeatStatus.EMPTY;
                                        seat.marker=rowname[rows];
                                    }

                                }
                            }//                   row D ends
                            if(rows>36){

                                seat.status = HallScheme.SeatStatus.EMPTY;
                            }
//                   row C start
                            if(rows ==33)
                            {
                                if(itemsList.get(k).getJrow()>26 && itemsList.get(k).getJrow()<32)
                                {
                                    colvalue=itemsList.get(k).getJrow()+9;
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else if(itemsList.get(k).getJrow()>12 && itemsList.get(k).getJrow()<27)
                                {
                                    colvalue=itemsList.get(k).getJrow()+6;
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else{
                                    if(itemsList.get(k).getJrow()>7 && itemsList.get(k).getJrow()<13){
                                        colvalue=3+itemsList.get(k).getJrow();
                                        seat.color = Color.argb(255, 60, 179, 113);
                                        seat.id =itemsList.get(k).getJrow();
                                        seat.selectedSeatMarker = String.valueOf(seat.id);
                                    }
                                    else{
                                        seat.status = HallScheme.SeatStatus.EMPTY;
                                        seat.marker=rowname[rows];
                                    }

                                }
                            }//                   row C ends

//                   row B start
                            if(rows ==34)
                            {
                                if(itemsList.get(k).getJrow()>26 && itemsList.get(k).getJrow()<31)
                                {
                                    colvalue=itemsList.get(k).getJrow()+9;
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else if(itemsList.get(k).getJrow()>12 && itemsList.get(k).getJrow()<27)
                                {
                                    colvalue=itemsList.get(k).getJrow()+6;
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else{
                                    if(itemsList.get(k).getJrow()>8 && itemsList.get(k).getJrow()<13){
                                        colvalue=3+itemsList.get(k).getJrow();
                                        seat.color = Color.argb(255, 60, 179, 113);
                                        seat.id =itemsList.get(k).getJrow();
                                        seat.selectedSeatMarker = String.valueOf(seat.id);
                                    }
                                    else{
                                        seat.status = HallScheme.SeatStatus.EMPTY;
                                        seat.marker=rowname[rows];
                                    }

                                }
                            }//                   row B ends

//                   row A start
                            if(rows ==35)
                            {
                                if(itemsList.get(k).getJrow()>26 && itemsList.get(k).getJrow()<30)
                                {
                                    colvalue=itemsList.get(k).getJrow()+9;
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else if(itemsList.get(k).getJrow()>12 && itemsList.get(k).getJrow()<27)
                                {
                                    colvalue=itemsList.get(k).getJrow()+6;
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else{
                                    if(itemsList.get(k).getJrow()>9 && itemsList.get(k).getJrow()<13){
                                        colvalue=3+itemsList.get(k).getJrow();
                                        seat.color = Color.argb(255, 60, 179, 113);
                                        seat.id =itemsList.get(k).getJrow();
                                        seat.selectedSeatMarker = String.valueOf(seat.id);
                                    }
                                    else{
                                        seat.status = HallScheme.SeatStatus.EMPTY;
                                        seat.marker=rowname[rows];
                                    }

                                }
                            }//                   row A ends


//                   row A1 start
                            if(rows ==36)
                            {

                                if(itemsList.get(k).getJrow()>12 && itemsList.get(k).getJrow()<27)
                                {
                                    colvalue=itemsList.get(k).getJrow()+6;
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                                else{
                                    seat.status = HallScheme.SeatStatus.EMPTY;
                                    seat.marker=rowname[rows];
                                }
                            }//                              row A1 ends


                        }//    Q to M Ends


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
                                colvalue=itemsList.get(k).getJrow()+5;
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