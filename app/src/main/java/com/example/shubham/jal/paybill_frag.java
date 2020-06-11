package com.example.shubham.jal;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class paybill_frag extends Fragment {
    ImageButton ib_currentbill;


    @SuppressLint("ValidFragment")
    public paybill_frag(Activity context) {
        // Required empty public constructor
        ib_currentbill=context.findViewById(R.id.ib_currentbill);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_paybill_frag, container, false);
        //ib_currentbill=getActivity().findViewById(R.id.ib_currentbill);

/*
        ib_currentbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View imageDialog = inflater.inflate(R.layout.image_fragment, null);
                Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(imageDialog);
                dialog.setCanceledOnTouchOutside(true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                ImageView imgImage = (ImageView) imageDialog.findViewById(R.id.image_frag);
                imgImage.setImageResource(R.drawable.currentbill);
                dialog.show();
            }
        });
*/
        return v;
    }

}
