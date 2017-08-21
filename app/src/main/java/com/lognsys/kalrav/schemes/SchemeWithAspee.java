package com.lognsys.kalrav.schemes;

        import android.app.ProgressDialog;
        import android.graphics.Color;
        import android.os.AsyncTask;
        import android.os.Build;
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
        import android.widget.ListAdapter;
        import android.widget.ListView;
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
 * Created by Nublo on 05.12.2015.
 * Copyright Nublo
 */
public class SchemeWithAspee extends Fragment {
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
    String rowNumber[]={"","Z","Y","X","W","V","U","T","S","R","Q","P","O","N","M","L","K","J","H","G","F","E","D","C","B","A"};

    int n =91;
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
        if(auditoriumPriceRangeList!=null && auditoriumPriceRangeList.size()>0)
        adapter =new MyAdapter(auditoriumPriceRangeList);

        listViewPrices.setAdapter( adapter);

        scheme = new HallScheme(imageView, schemeWithScene(), getActivity());
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

                }
                ArrayList<Seat> items=(ArrayList<Seat>)seatList;
                Fragment fff=new ConfirmFragment();
                Bundle args = new Bundle();
                args.putSerializable("auditorium",auditorium);
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

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<AuditoriumPriceRange> list;

        public MyAdapter(List<AuditoriumPriceRange> Data) {
            list = Data;
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
            String strIvalue =rowNumber[iValue];
            Log.d("", "MyAdapter  strIvalue==="+strIvalue);

            int iEndValue=auditorium.getIend();
            String strIEndvalue =rowNumber[iEndValue];
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

    private void switchFragment(Fragment fff) {
        if (getActivity() == null)
            return;
        if (getActivity() instanceof HomeActivity) {
            HomeActivity feeds = (HomeActivity) getActivity();
            feeds.switchContent(fff);
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

    public Seat[][] schemeWithScene() {
        Seat seats[][] = new Seat[35][33];
        int n=91;
//      middle seat last row : Z
        int midSeatsZrow=15;
        int midSeatsGrow=15;

//      middle seat row from Y to M
        int midSeatsYtoMrow=30;

//      middle seat row from L to G
        int midSeatsLtoGrow=26;

//      LEFT - TOP seats from row Y to M
        int colLeftTopSeatsYtoMrow=27;

//     SPACE LEFT - TOP seats from row L to H
        int colSpaceLeftTopSeatsLtoHrow=23;
//        int jrow=11;
//        int irow=30;

//      SPACE RIGHT - TOP seats from row Y to M
        int colSpaceRightTopSeatsYtoMrow=33;

//      SPACE - RIGHT - SPACE - TOP seats from row L to H
        int colSpaceRightSpaceTopSeatsLtoHrow=29;


//      BOTTOM LEFT seats from row F to C
        int bottomLeftSeatsFtoCrow=28;

//      BOTTOM LEFT seats row B
        int bottomLeftSeatsBrow=27;

//      BOTTOM LEFT seats row A
        int bottomLeftSeatsArow=25;



//      BOTTOM RIGHT seats from row F to C
        int bottomRightSeatsFtoCrow=31;

//      BOTTOM RIGHT seats row B
        int bottomRightSeatsBrow=30;

//      BOTTOM RIGHT seats row A
        int bottomRightSeatsArow=28;


        for (int i = 0; i < 35; i++) {
            for (int j = 0; j < 33; j++) {
                SeatExample seat = new SeatExample();
                seat.status = HallScheme.SeatStatus.EMPTY;
                seats[i][j] = seat;

                if (i == 0) {
//                    Row P to  Z 250
//                    if (j == 17) {
//                        seat.marker = " Row P to Z-RS ." + PtoZ;
//                        seat.status = HallScheme.SeatStatus.INFO;
//                    }
                } else if (i > 0) {

                    if (i < 12) {//                row name

                        if (j > 0 || j < 33) {

                            int values = (n - (i));
                            char character = (char) values;

                            seat.marker = String.valueOf(character);
                            seat.status = HallScheme.SeatStatus.INFO;
                        }
                    } else if (i > 13 && i < 22) {//   i < 23)             row name
                        if (j > 0 || j < 33) {
                            int values = (n - (i - 2));
                            if(values<74){
                                values=values-1;
                            }
                            char character = (char) values;

                            seat.marker = String.valueOf(character);
                            seat.status = HallScheme.SeatStatus.INFO;
                        }
                    } else if (i > 23 && i < 30) {// i > 24                row name

                        if (j > 0 || j < 33) {
                            int values = (n - (i - 3));
                            char character = (char) values;

                            seat.marker = String.valueOf(character);
                            seat.status = HallScheme.SeatStatus.INFO;
                        }
                    } else if (i == 13) {
//                    Row G to  O 350
                        if (j == 17) {/*
                            seat.marker = " Row G to O-RS ." + GtoO;
                            seat.status = HallScheme.SeatStatus.INFO;*/
                        }
                    } else if (i == 23) {
//                    Row A to F-RS .500
                        if (j == 17) {
                          /*  seat.marker = " Row A to F-RS ." + AtoF;
                            seat.status = HallScheme.SeatStatus.INFO;*/
                        }
                    }

                    //    space & seats left-top
                    if (j > 0 && j < 10) {

//                        seats left-top
                        if (i > 1 && i < 12 && j < 7) {
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.color = Color.argb(255, 60, 179, 113);
                            seat.id=colLeftTopSeatsYtoMrow-(1*j);

                            seat.selectedSeatMarker = String.valueOf(colLeftTopSeatsYtoMrow-(1*j));
                        } else if (i > 13 && i <= 16 && j < 7) {

                            seat.id = colLeftTopSeatsYtoMrow - (1 * j);
//                       top space + below seats left-top
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.color = Color.argb(255, 60, 179, 113);
                            seat.selectedSeatMarker = String.valueOf(colLeftTopSeatsYtoMrow - (1 * j));
                        } else if (i > 16 && i < 21 && j > 4 && j < 7) {//i < 22
                            seat.id = colSpaceLeftTopSeatsLtoHrow - (1 * j);
//                           space + seats left-top
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.color = Color.argb(255, 60, 179, 113);
                            seat.selectedSeatMarker = String.valueOf(colSpaceLeftTopSeatsLtoHrow - (1 * j));
                        } else {
                            //space after seats
                            seat.status = HallScheme.SeatStatus.EMPTY;
                        }
                    }

//                F to A with  3 space before 12 column(seats)
                    if (i > 23 && i < 30) {//i > 24 && i < 31

//                F to C with  3 space before 12 column(seats)
                        if (i < 28) { //i < 29)
                            if (j > 3 && j < 16) {
                                seat.id = bottomLeftSeatsFtoCrow - (1 * j);
//                       space + seats left
                                seat.status = HallScheme.SeatStatus.FREE;
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.selectedSeatMarker = String.valueOf(bottomLeftSeatsFtoCrow - (1 * j));
                            }
                        } else if (i == 28) { //i == 29
//                B row with 4 space before 11 column(seats)
                            if (j > 4 && j < 16) {
                                seat.id = bottomLeftSeatsBrow - (1 * j);
//                       space + seats left
                                seat.status = HallScheme.SeatStatus.FREE;
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.selectedSeatMarker = String.valueOf(bottomLeftSeatsBrow - (1 * j));

                            }
                        } else {
//                A row with 4 space before 9 column(seats)
                            if (j > 6 && j < 16) {
                                seat.id = bottomLeftSeatsArow - (1 * j);

//                       space + seats left
                                seat.status = HallScheme.SeatStatus.FREE;
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.selectedSeatMarker = String.valueOf(bottomLeftSeatsArow - (1 * j));
                            }
                        }
                        if (j > 15 && j < 19) {
                            seat.status = HallScheme.SeatStatus.EMPTY;
                        }
                    }

                    if (i >= 1) {

//                      seats middle row
                        if (i > 0 && j > 9 && j < 24) {
                            if (i == 1) {

//                                midSeats from  14 to 1
                                seat.id = --midSeatsZrow;
                                seat.status = HallScheme.SeatStatus.FREE;
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.selectedSeatMarker = String.valueOf(seat.id);

                            }
                            if (i > 1 && i < 12) {
                                seat.status = HallScheme.SeatStatus.FREE;
                                seat.color = Color.argb(255, 60, 179, 113);

                                seat.id = midSeatsYtoMrow - (1 * j);
                                seat.selectedSeatMarker = String.valueOf(midSeatsYtoMrow - (1 * j));

                            } else if (i > 13 && i < 21) { //&& i < 22
//                               seats  middle row

                                if (i > 13 && i < 17) {
                                    seat.id = midSeatsYtoMrow - (1 * j);

                                } else if (i > 17 && i < 21) {//&& i < 22
                                    seat.id = midSeatsLtoGrow - (1 * j);
                                }
                                seat.status = HallScheme.SeatStatus.FREE;
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.selectedSeatMarker = String.valueOf(seat.id);
                            }
                            else  if (i == 21) { //i == 22
//                                midSeats from  14 to 1
                                seat.id = --midSeatsGrow;
                                seat.status = HallScheme.SeatStatus.FREE;
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.selectedSeatMarker = String.valueOf(seat.id);

                            }
                        }
                    }

//                  space & seats right-top
                    if (j > 23 && j < 33) {

//                        seats right-top
                        if (i > 1 && i < 12 && j > 26) {
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.color = Color.argb(255, 60, 179, 113);
                            seat.id = colSpaceRightTopSeatsYtoMrow - (1 * j);
                            seat.selectedSeatMarker = String.valueOf(colSpaceRightTopSeatsYtoMrow - (1 * j));
                        } else if (i > 13 && i < 17 && j > 26) {

                            seat.id = colSpaceRightTopSeatsYtoMrow - (1 * j);
//                        seats right-top
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.color = Color.argb(255, 60, 179, 113);

                            seat.selectedSeatMarker = String.valueOf(colSpaceRightTopSeatsYtoMrow - (1 * j));
                        } else if (i > 16 && i < 21 && j > 26 && j < 29) { //&& i < 22
//                       space + seats right-top
                            seat.status = HallScheme.SeatStatus.FREE;
                            seat.color = Color.argb(255, 60, 179, 113);
                            seat.id = colSpaceRightSpaceTopSeatsLtoHrow - (1 * j);

                            seat.selectedSeatMarker = String.valueOf(colSpaceRightSpaceTopSeatsLtoHrow - (1 * j));
                        } else {
                            //space before seats
                            seat.status = HallScheme.SeatStatus.EMPTY;
                        }
                    }

//                          space + seats right+space  from  F to  A row
                    if (i > 23 && i < 30) { //i > 24 && i < 31

//                F to C with  3 space before 12 column(seats)
                        if (i < 28) { //i < 29
//                            space + seats right+space
                            if (j > 18 && j < 31) {
                                seat.id = bottomRightSeatsFtoCrow - (1 * j);
                                seat.status = HallScheme.SeatStatus.FREE;
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.selectedSeatMarker = String.valueOf(bottomRightSeatsFtoCrow - (1 * j));
                            }
                        } else if (i == 28) { //i == 29

//                        B row with 4 space before 11 column(seats)
                            if (j > 18 && j < 30) {
                                seat.id = bottomRightSeatsBrow - (1 * j);

//                       space + seats right+space
                                seat.status = HallScheme.SeatStatus.FREE;
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.selectedSeatMarker = String.valueOf(bottomRightSeatsBrow - (1 * j));
                            }
                        } else {

//                      A row with 4 space before 9 column(seats)
                            if (j > 18 && j < 28) {
                                seat.id = bottomRightSeatsArow - (1 * j);

//                          space + seats left
                                seat.status = HallScheme.SeatStatus.FREE;
                                seat.color = Color.argb(255, 60, 179, 113);
                                seat.selectedSeatMarker = String.valueOf(bottomRightSeatsArow - (1 * j));
                            }
                        }
                    }

                }
                if(i>0){
                    for(int p=0;p<auditoriumPriceRangeList.size();p++){
                        if(j>0 && j<33){
                            if(i>=auditoriumPriceRangeList.get(p).getIstart() && i<=auditoriumPriceRangeList.get(p).getIend()){
                                seat.price=auditoriumPriceRangeList.get(p).getPrice();
                            }
                        }

                    }

                }
            }

//            incrementing by  2 rows range of rows 1 to 28
            int rows=0;
            if(itemsList!=null && itemsList.size()>0){

                for(int k=0;k<itemsList.size();k++){
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
                            }//                   row A ends

                            if(rows>29)
                                seat.status = HallScheme.SeatStatus.EMPTY;

                        }else{

//                   row (P to Y) start
                            if(itemsList.get(k).getIrow()<=11 && itemsList.get(k).getIrow()>1)
                            {
                                rows= itemsList.get(k).getIrow();
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
                            }//                   row P to Y ends


//                   row Z start
                            if(itemsList.get(k).getIrow()==1)
                            {
                                rows= itemsList.get(k).getIrow();

                                if(itemsList.get(k).getJrow()>0 && itemsList.get(k).getJrow()<15)
                                {
                                    colvalue=26-itemsList.get(k).getJrow()-2;
                                    seat.color = Color.argb(255, 60, 179, 113);
                                    seat.id =itemsList.get(k).getJrow();
                                    seat.selectedSeatMarker = String.valueOf(seat.id);
                                }
                            }//                   row Z ends

                        }
                        seats[rows][colvalue]=seat;

                    }
                }
            }
        }

        return seats;
    }



    public static boolean rowEquals(int one, int two) {
        if(one==two)
            return true;
        else
            return false;
    }
}