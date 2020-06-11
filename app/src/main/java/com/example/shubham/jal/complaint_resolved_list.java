package com.example.shubham.jal;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class complaint_resolved_list extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> complaintno_stack;
    private final ArrayList<String> date_stack;
    private final ArrayList<String> category_stack;
    private final ArrayList<String> problem_stack;
    private final ArrayList<String> date_resolved;
    private final ArrayList<String> imagelink;

    FirebaseStorage storage;
    StorageReference storageRef;
    public static final String SHARED_PREFS="shared_prefs";
    public static final String KNO="kno";
    private DatabaseReference mDatabase;
    String text1;
    String text2;
    String text3;

    public complaint_resolved_list(Activity context, ArrayList<String> complaintno_stack, ArrayList<String>date_stack, ArrayList<String>category_stack, ArrayList<String>problem_stack,
                                   ArrayList<String> date_resolved,ArrayList<String>imagelink) {
        super(context, R.layout.activity_useractivity,complaintno_stack);

        this.context=context;
        this.complaintno_stack=complaintno_stack;
        this.date_stack=date_stack;
        this.category_stack=category_stack;
        this.problem_stack=problem_stack;
        this.date_resolved=date_resolved;
        this.imagelink=imagelink;

        final SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        text1 = sharedPreferences.getString(KNO,"-1");
    }

    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater=context.getLayoutInflater();
        final View rowView=inflater.inflate(R.layout.complaint_resolved_listitem, null,true);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final TextView tv_complaintno_stack=rowView.findViewById((R.id.tv_complaintno_resolved));
        TextView tv_date_stack=rowView.findViewById((R.id.tv_date_resolved));
        TextView tv_category_stack=rowView.findViewById((R.id.tv_category_resolved));
        TextView tv_problem_stack=rowView.findViewById((R.id.tv_problem_resolved));
        TextView tv_dateresolved=rowView.findViewById((R.id.tv_dateresolved_resolved));

        tv_complaintno_stack.setText(complaintno_stack.get(position));
        tv_date_stack.setText(date_stack.get(position));
        tv_category_stack.setText(category_stack.get(position));
        tv_problem_stack.setText(problem_stack.get(position));
        tv_dateresolved.setText(date_resolved.get(position));

        ImageButton ib_download_stack=rowView.findViewById(R.id.ib_download_resolved);

        ib_download_stack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url=imagelink.get(position);
                if(!url.equals("link")){
                    LayoutInflater inflater = context.getLayoutInflater();
                    View imageDialog = inflater.inflate(R.layout.image_fragment, null);
                    Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(imageDialog);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    ImageView imgImage = (ImageView) imageDialog.findViewById(R.id.image_frag);
                    Picasso.with(context).load(url).into(imgImage);
                    dialog.show();
                }
                else{
                    Toast.makeText(context, "Image Doesn't Exist for this Complaint", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return  rowView;
    }


    }
