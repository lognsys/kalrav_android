package com.lognsys.kalrav.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import  com.lognsys.kalrav.R;



/**
 * Created by pdoshi on 22/02/17.
 */

public class NetworkStatusDialog extends DialogFragment {
    public static final String ARG_TITLE = "title";
    public static final String ARG_MSG = "message";
    public static final String ARG_INTENT = "intent";


    public NetworkStatusDialog() {

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        String title = args.getString(ARG_TITLE, "");
        String message = args.getString(ARG_MSG, "");
        final Intent i = args.getParcelable(ARG_INTENT);
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.viewname, null);
//        builder.setView(view);
//        TextView tv = new TextView(getActivity());
//        tv.setText(title);
//        tv.setBackgroundColor(Color.DKGRAY);
//        tv.setPadding(10, 10, 10, 10);
//        tv.setGravity(Gravity.CENTER);
//        tv.setTextColor(Color.WHITE);
//        tv.setTextSize(20);
//        builder.setCustomTitle(tv);

        return new AlertDialog.Builder(getActivity(), R.style.AppTheme)
                .setTitle(title)
                .setMessage(message)

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                        dialog.dismiss();
                    }
                }).create();


    }
}
