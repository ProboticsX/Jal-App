package com.example.shubham.jal;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

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
public class office_complaintstack_frag extends Fragment {

    ListView list;
    ArrayList<String> complaintno_stack=new ArrayList<>();
    ArrayList<String> date_stack=new ArrayList<>();
    ArrayList<String> category_stack=new ArrayList<>();
    ArrayList<String> problem_stack=new ArrayList<>();
    ArrayList<String>userkno_office=new ArrayList<>();

    public static final String SHARED_PREFS="shared_prefs";
    public static final String OFFICEID="OFFICEID";
    public static final String CATEGORY="category";
    public static final String COMPLAINTNO="complaintno";

    private DatabaseReference mDatabase;
    final String text1;
    ImageButton ib_download_stack;
    SharedPreferences sharedPreferences;
    TextView tv_appbaruser;



    @SuppressLint("ValidFragment")
    public office_complaintstack_frag(Activity context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        text1 = sharedPreferences.getString(OFFICEID,"-4");
        tv_appbaruser=context.findViewById(R.id.tv_appbaruser_office);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v= inflater.inflate(R.layout.fragment_office_complaintstack_frag, container, false);
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.apply();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("officeids").child(text1).child("complaintstack").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if(dataSnapshot1.getKey().toString().equals("wrongbill")){
                        category_stack.add("Wrong Water Bill");

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
                                if(dataSnapshot3.getKey().toString().equals("kno")){
                                    userkno_office.add(dataSnapshot3.getValue().toString());
                                }

                            }

                            }


                        }
                    if(dataSnapshot1.getKey().toString().equals("watermeter")){
                        category_stack.add("Error in Water Meter");

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
                                if(dataSnapshot3.getKey().toString().equals("kno")){
                                    userkno_office.add(dataSnapshot3.getValue().toString());
                                }

                            }

                        }


                    }
                    if(dataSnapshot1.getKey().toString().equals("nowater")){
                        category_stack.add("No/Short Water Supply");

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
                                if(dataSnapshot3.getKey().toString().equals("kno")){
                                    userkno_office.add(dataSnapshot3.getValue().toString());
                                }

                            }

                        }


                    }
                }

                office_complaintstack_list adapter = new office_complaintstack_list(getActivity(),complaintno_stack,date_stack,category_stack,problem_stack,userkno_office);

                list = v.findViewById(R.id.lv_office_complaintstack);
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return  v;
    }

}
