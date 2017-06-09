package com.lognsys.kalrav.schemes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
        Seat seats[][] = new Seat[12][50];
        int k = 0;
        int n=91;
        int charGtoA=71,charZtoA=91;
        for (int i = 0; i < 12; i++)
            for(int j = 0; j < 50; j++) {
                SeatExample seat = new SeatExample();
                seat.id = ++k;
//                seat.selectedSeatMarker = String.valueOf(i+1);
                seat.status = HallScheme.SeatStatus.EMPTY;
                if(i==0){
                    if (j > 0 && j < 49){
                       seat.selectedSeatMarker= String.valueOf(i);
                        seat.status = HallScheme.SeatStatus.FREE;
                    }else {
                        char GG=(char)charGtoA;
                        seat.marker =String.valueOf(GG)+String.valueOf(GG);

                        seat.status = HallScheme.SeatStatus.INFO;
                    }
                }

                if(!(i==1) && !(i==0)){
                    if (j >1 && j < 49){
                        seat.selectedSeatMarker= String.valueOf(i);
                        seat.status = HallScheme.SeatStatus.FREE;
                    }
                    if(!(j==0) &&(!(j >1 && j < 49))){
                        char GG=(char)charGtoA;
                        seat.marker =String.valueOf(GG)+String.valueOf(GG);

                        seat.status = HallScheme.SeatStatus.INFO;
                        charGtoA--;
                    }
                }
              /*  if (j == 0 || j == 52) {
                    seat.status = HallScheme.SeatStatus.EMPTY;
                    if (i > 2 && i < 10) {
                        seat.marker = String.valueOf(i);
                        seat.status = HallScheme.SeatStatus.INFO;
                    }

                }
                if (((j > 0 && j < 3) || (j > 14 && j < 17)) && i == 0) {
                    seat.status = HallScheme.SeatStatus.EMPTY;
                    if (j == 2 || j == 15) {
                        seat.marker = String.valueOf(i+1);
                        seat.status = HallScheme.SeatStatus.INFO;
                    }
                }
                if (((j > 0 && j < 2) || (j > 15 && j < 17)) && i == 1) {
                    seat.status = HallScheme.SeatStatus.EMPTY;
                    if (j == 1 || j == 16) {
                        seat.marker = String.valueOf(i+1);
                        seat.status = HallScheme.SeatStatus.INFO;
                    }
                }
                if (i == 2)
                    seat.status = HallScheme.SeatStatus.EMPTY;
                if (i > 9 && (j == 1 || j == 16)) {
                    seat.status = HallScheme.SeatStatus.INFO;
                    seat.marker = String.valueOf(i);
                }*/
                seats[i][j] = seat;
            }
        return seats;
    }

}