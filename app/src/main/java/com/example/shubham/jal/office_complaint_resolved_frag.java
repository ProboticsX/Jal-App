package com.example.shubham.jal;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class office_complaint_resolved_frag extends Fragment {

    ListView list;
    ArrayList<String> complaintno_stack=new ArrayList<>();
    ArrayList<String> date_stack=new ArrayList<>();
    ArrayList<String> category_stack=new ArrayList<>();
    ArrayList<String> problem_stack=new ArrayList<>();
    ArrayList<String> date_resolved=new ArrayList<>();
    ArrayList<String> image_link=new ArrayList<>();

    public static final String SHARED_PREFS="shared_prefs";
    public static final String OFFICEID="OFFICEID";

    private DatabaseReference mDatabase;
    final String text1;
    ImageButton ib_download_stack;
    SharedPreferences sharedPreferences;



    @SuppressLint("ValidFragment")
    public office_complaint_resolved_frag(Activity context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        text1 = sharedPreferences.getString(OFFICEID,"-4");    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v= inflater.inflate(R.layout.fragment_office_complaint_resolved_frag, container, false);
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("officeids").child(text1).child("complaintsresolved").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                            for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                if(dataSnapshot3.getKey().toString().equals("complaintno")){
                                    complaintno_stack.add(dataSnapshot3.getValue().toString());

                                }
                                if(dataSnapshot3.getKey().toString().equals("date")){
                                    date_stack.add(dataSnapshot3.getValue().toString());
                                }
                                if(dataSnapshot3.getKey().toString().equals("problem")){
                                    problem_stack.add(dataSnapshot3.getValue().toString());
                                }
                                if(dataSnapshot3.getKey().toString().equals("category")){
                                    category_stack.add(dataSnapshot3.getValue().toString());
                                }
                                if(dataSnapshot3.getKey().toString().equals("dateresolved")){
                                    date_resolved.add(dataSnapshot3.getValue().toString());
                                }
                                if(dataSnapshot3.getKey().toString().equals("imagelink")){
                                    image_link.add(dataSnapshot3.getValue().toString());
                                }
                            }





                    }

                    office_complaint_resolved_list adapter = new
                            office_complaint_resolved_list(getActivity(),complaintno_stack,date_stack,category_stack,
                            problem_stack,date_resolved,image_link);
                    list = v.findViewById(R.id.lv_office_complaint_resolved);
                    list.setAdapter(adapter);

                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                new AlertDialog.Builder(getContext())
                                        .setTitle("View Profile")
                                        .setMessage("Do you want to view Comlpainer's Profile?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent=new Intent(getContext(),userprofile.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        })
                                        .setIcon(R.drawable.profile)
                                        .show();
                            }
                        });


                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return v;
    }

}
