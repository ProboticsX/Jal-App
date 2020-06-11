package com.example.shubham.jal;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class complaint_list extends ArrayAdapter<String> {

    private final Activity context;
String[]abc;

    public complaint_list(Activity context,String[] abc) {
        super(context,R.layout.activity_useractivity,abc);

        // TODO Auto-generated constructor stub

        this.context=context;
        this.abc=abc;

    }


    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.complaint_listitem, null,true);

        ImageView iv_problem=rowView.findViewById((R.id.iv_problem));
        TextView tv_problem=rowView.findViewById((R.id.tv_problem));
        TextView tv_problem_detail=rowView.findViewById((R.id.tv_problem_detail));


        if(position==0){
            iv_problem.setImageDrawable(context.getDrawable(R.drawable.waterbill));
            tv_problem.setText("Wrong Water Bill");
            tv_problem_detail.setText("Upload and Submit Picture of your Water Bill");
        }

        if(position==1){
            iv_problem.setImageDrawable(context.getDrawable(R.drawable.watermeter));
            tv_problem.setText("Error in Water Meter");
            tv_problem_detail.setText("Upload and Submit Picture of your Water Meter");
        }
        if(position==2){
            iv_problem.setImageDrawable(context.getDrawable(R.drawable.dirty));
            tv_problem.setText("Contaminated Water Supply");
            tv_problem_detail.setText("Upload and Submit Picture of the Dirty Water");
        }

        if(position==3){
            iv_problem.setImageDrawable(context.getDrawable(R.drawable.pipe));
            tv_problem.setText("Leakage in Pipeline");
            tv_problem_detail.setText("Upload and Submit Picture of the Pipeline");
        }
        if(position==4){
            iv_problem.setImageDrawable(context.getDrawable(R.drawable.nowater));
            tv_problem.setText("No/Short Water Supply");
            tv_problem_detail.setText("Submit the required details");
        }
        if(position==5){
            iv_problem.setImageDrawable(context.getDrawable(R.drawable.customer));
            tv_problem.setText("Other Problems");
            tv_problem_detail.setText("Feel free to share any other issues");
        }


        return rowView;

    };
}
