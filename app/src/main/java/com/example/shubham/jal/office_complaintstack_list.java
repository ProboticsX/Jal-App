package com.example.shubham.jal;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class office_complaintstack_list extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> complaintno_stack;
    private final ArrayList<String> date_stack;
    private final ArrayList<String> category_stack;
    private final ArrayList<String> problem_stack;
    private final ArrayList<String>userkno_office;

    FirebaseStorage storage;
    StorageReference storageRef;
    public static final String SHARED_PREFS="shared_prefs";
    public static final String OFFICEID="OFFICEID";
    String text1;
    String text2;
    String text3;

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        Log.d("shubham", "notifyDataSetChanged: ");
    }

    public office_complaintstack_list(Activity context, ArrayList<String> complaintno_stack, ArrayList<String>date_stack, ArrayList<String>category_stack, ArrayList<String>problem_stack, ArrayList<String>userkno_office) {
        super(context, R.layout.activity_office_useractivity,complaintno_stack);

        this.context=context;
        this.complaintno_stack=complaintno_stack;
        this.date_stack=date_stack;
        this.category_stack=category_stack;
        this.problem_stack=problem_stack;
        this.userkno_office=userkno_office;
    }

    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.office_complaintstack_listitem, null,true);

        TextView tv_complaintno_stack_office=rowView.findViewById((R.id.tv_complaintno_stack_office));
        TextView tv_date_stack_office=rowView.findViewById((R.id.tv_date_stack_office));
        TextView tv_category_stack_office=rowView.findViewById((R.id.tv_category_stack_office));
        TextView tv_problem_stack_office=rowView.findViewById((R.id.tv_problem_stack_office));
        TextView tv_userkno_office=rowView.findViewById((R.id.tv_userkno_office));

        tv_complaintno_stack_office.setText("Complaint Number:"+complaintno_stack.get(position));
        tv_date_stack_office.setText("Date Issued:"+date_stack.get(position));
        tv_category_stack_office.setText("Category:"+category_stack.get(position));
        tv_problem_stack_office.setText("Problem:\n"+problem_stack.get(position));
        tv_userkno_office.setText("Complainer's KNO: "+userkno_office.get(position));

        ImageButton ib_download_stack_office=rowView.findViewById(R.id.ib_download_stack_office);
        ib_download_stack_office.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                storage = FirebaseStorage.getInstance();
                storageRef = storage.getReference();


                final SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                text1 = userkno_office.get(position);
                if(category_stack.get(position).equals("Wrong Water Bill"))
                    text2="wrongbill";
                if(category_stack.get(position).equals("Error in Water Meter"))
                    text2="watermeter";
                if(category_stack.get(position).equals("No/Short Water Supply"))
                    text2="nowater";
                text3=complaintno_stack.get(position);


                Log.d("1234", "onClick: "+"images/"+text1+"/"+ text2 +"/"+text3+".jpg");

                storageRef.child("images/"+text1+"/"+ text2 +"/"+text3+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("1234", "onSuccess: "+uri.toString());

                        LayoutInflater inflater = context.getLayoutInflater();
                        View imageDialog = inflater.inflate(R.layout.image_fragment, null);
                        Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(imageDialog);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        ImageView imgImage = (ImageView) imageDialog.findViewById(R.id.image_frag);
                        String url=uri.toString();
                        Picasso.with(context).load(url).into(imgImage);
                        dialog.show();





                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(context, "Image doesn't exist for this Complaint", Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });
return  rowView;

    }

    }
