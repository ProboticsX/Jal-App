package com.example.shubham.jal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class complaintstack_list extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> complaintno_stack;
    private final ArrayList<String> date_stack;
    private final ArrayList<String> category_stack;
    private final ArrayList<String> problem_stack;
    private final ArrayList<String> officeid_stack;
    private String LEGACY_SERVER_KEY = "AIzaSyANaeOkqTcvOh9JICvtRXuzc1uqi42m4ew";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");


    FirebaseStorage storage;
    StorageReference storageRef;
    public static final String SHARED_PREFS="shared_prefs";
    public static final String KNO="kno";
    private DatabaseReference mDatabase;
    String text1;
    String text2;
    String text3;
    FragmentTransaction ft;

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

    }

    public complaintstack_list(Activity context, ArrayList<String> complaintno_stack, ArrayList<String>date_stack, ArrayList<String>category_stack, ArrayList<String>problem_stack, ArrayList<String> officeid_stack) {
        super(context, R.layout.activity_useractivity,complaintno_stack);

        this.context=context;
        this.complaintno_stack=complaintno_stack;
        this.date_stack=date_stack;
        this.category_stack=category_stack;
        this.problem_stack=problem_stack;
        this.officeid_stack=officeid_stack;

        final SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        text1 = sharedPreferences.getString(KNO,"-1");


    }
    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater=context.getLayoutInflater();
        final View rowView=inflater.inflate(R.layout.complaintstack_listitem, null,true);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final TextView tv_complaintno_stack=rowView.findViewById((R.id.tv_complaintno_stack));
        TextView tv_date_stack=rowView.findViewById((R.id.tv_date_stack));
        TextView tv_category_stack=rowView.findViewById((R.id.tv_category_stack));
        TextView tv_problem_stack=rowView.findViewById((R.id.tv_problem_stack));
        final Switch resolved_switch=rowView.findViewById(R.id.resolved_switch);

        tv_complaintno_stack.setText("Complaint Number:"+complaintno_stack.get(position));
        tv_date_stack.setText("Date Issued:"+date_stack.get(position));
        tv_category_stack.setText("Category:"+category_stack.get(position));
        tv_problem_stack.setText("Problem:\n"+problem_stack.get(position));

        ImageButton ib_download_stack=rowView.findViewById(R.id.ib_download_stack);

        resolved_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                new AlertDialog.Builder(context)
                        .setTitle("Complaint Submission")
                        .setMessage("Are you sure that your Complaint has been successfully resolved ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {




                                storage = FirebaseStorage.getInstance();
                                storageRef = storage.getReference();

                                text2="xyz";

                                if(category_stack.get(position).equals("Wrong Water Bill"))
                                    text2="wrongbill";
                                if(category_stack.get(position).equals("Error in Water Meter"))
                                    text2="watermeter";
                                if(category_stack.get(position).equals("No/Short Water Supply"))
                                    text2="nowater";

                                text3=complaintno_stack.get(position);




                                storageRef.child("images/"+text1+"/"+ text2 +"/"+text3+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        mDatabase.child("knos").child(text1).child("complaintsresolved").child(text2).child(complaintno_stack.get(position)).child("imagelink")
                                                .setValue(uri.toString());
                                        mDatabase.child("knos").child(text1).child("complaintsresolved").child(text2).child(complaintno_stack.get(position)).child("complaintno")
                                                .setValue("Complaint Number:"+complaintno_stack.get(position));
                                        mDatabase.child("knos").child(text1).child("complaintsresolved").child(text2).child(complaintno_stack.get(position)).child("date")
                                                .setValue("Date Issued:"+date_stack.get(position));
                                        mDatabase.child("knos").child(text1).child("complaintsresolved").child(text2).child(complaintno_stack.get(position)).child("category")
                                                .setValue("Category:"+category_stack.get(position));
                                        mDatabase.child("knos").child(text1).child("complaintsresolved").child(text2).child(complaintno_stack.get(position)).child("problem")
                                                .setValue("Problem:\n"+problem_stack.get(position));

                                        Date d = new Date();
                                        final CharSequence date  = DateFormat.format("MMMM d, yyyy ", d.getTime());

                                        mDatabase.child("knos").child(text1).child("complaintsresolved").child(text2).child(complaintno_stack.get(position)).child("dateresolved")
                                                .setValue(date);
                                        mDatabase.child("officeids").child(officeid_stack.get(position)).child("complaintsresolved").child(text2).child(complaintno_stack.get(position)).child("dateresolved")
                                                .setValue(date);
                                        mDatabase.child("officeids").child(officeid_stack.get(position)).child("complaintsresolved").
                                                child(text2).child(complaintno_stack.get(position)).child("imagelink")
                                                .setValue(uri.toString());
                                        mDatabase.child("officeids").child(officeid_stack.get(position)).child("complaintsresolved").child(text2).child(complaintno_stack.get(position)).child("complaintno")
                                                .setValue("Complaint Number:"+complaintno_stack.get(position));
                                        mDatabase.child("officeids").child(officeid_stack.get(position)).child("complaintsresolved").child(text2).child(complaintno_stack.get(position)).child("date")
                                                .setValue("Date Issued:"+date_stack.get(position));
                                        mDatabase.child("officeids").child(officeid_stack.get(position)).child("complaintsresolved").child(text2).child(complaintno_stack.get(position)).child("category")
                                                .setValue("Category:"+category_stack.get(position));
                                        mDatabase.child("officeids").child(officeid_stack.get(position)).child("complaintsresolved").child(text2).child(complaintno_stack.get(position)).child("problem")
                                                .setValue("Problem:\n"+problem_stack.get(position));

                                        mDatabase.child("knos").child(text1).child("complaints").child(text2)
                                                .child(complaintno_stack.get(position)).removeValue();
                                        mDatabase.child("officeids").child(officeid_stack.get(position)).
                                                child("complaintstack").child(text2)
                                                .child(complaintno_stack.get(position)).removeValue();
                                        sendNotification("user", "Complaint Number:"+complaintno_stack.get(position)
                                                +" has been resolved");

                                        sendNotification("topic_menu", "Complaint Number:"+complaintno_stack.get(position)
                                                +" has been resolved");

                                        Log.d("1234", "onSuccess: "+uri.toString());
                                        Intent intent=new Intent(getContext(),useractivity.class);
                                        Log.d("1234", "notifyDataSetChanged: ");
                                        intent.putExtra("reloadcomplaintstack",1);
                                        getContext().startActivity(intent);
                                        ((Activity)getContext()).finish();



                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {

                                        mDatabase.child("knos").child(text1).child("complaintsresolved").child(text2).child(complaintno_stack.get(position)).child("imagelink")
                                                .setValue("link");
                                        mDatabase.child("knos").child(text1).child("complaintsresolved").child(text2).child(complaintno_stack.get(position)).child("complaintno")
                                                .setValue("Complaint Number:"+complaintno_stack.get(position));
                                        mDatabase.child("knos").child(text1).child("complaintsresolved").child(text2).child(complaintno_stack.get(position)).child("date")
                                                .setValue("Date Issued:"+date_stack.get(position));
                                        mDatabase.child("knos").child(text1).child("complaintsresolved").child(text2).child(complaintno_stack.get(position)).child("category")
                                                .setValue("Category:"+category_stack.get(position));
                                        mDatabase.child("knos").child(text1).child("complaintsresolved").child(text2).child(complaintno_stack.get(position)).child("problem")
                                                .setValue("Problem:\n"+problem_stack.get(position));

                                        Date d = new Date();
                                        final CharSequence date  = DateFormat.format("MMMM d, yyyy ", d.getTime());

                                        mDatabase.child("knos").child(text1).child("complaintsresolved").child(text2).child(complaintno_stack.get(position)).child("dateresolved")
                                                .setValue(date);
                                        mDatabase.child("officeids").child(officeid_stack.get(position)).child("complaintsresolved").child(text2).child(complaintno_stack.get(position)).child("dateresolved")
                                                .setValue(date);

                                        mDatabase.child("officeids").child(officeid_stack.get(position)).child("complaintsresolved").
                                                child(text2).child(complaintno_stack.get(position)).child("imagelink")
                                                .setValue("link");
                                        mDatabase.child("officeids").child(officeid_stack.get(position)).child("complaintsresolved").child(text2).child(complaintno_stack.get(position)).child("complaintno")
                                                .setValue("Complaint Number:"+complaintno_stack.get(position));
                                        mDatabase.child("officeids").child(officeid_stack.get(position)).child("complaintsresolved").child(text2).child(complaintno_stack.get(position)).child("date")
                                                .setValue("Date Issued:"+date_stack.get(position));
                                        mDatabase.child("officeids").child(officeid_stack.get(position)).child("complaintsresolved").child(text2).child(complaintno_stack.get(position)).child("category")
                                                .setValue("Category:"+category_stack.get(position));
                                        mDatabase.child("officeids").child(officeid_stack.get(position)).child("complaintsresolved").child(text2).child(complaintno_stack.get(position)).child("problem")
                                                .setValue("Problem:\n"+problem_stack.get(position));

                                        mDatabase.child("knos").child(text1).child("complaints").child(text2)
                                                .child(complaintno_stack.get(position)).removeValue();
                                        mDatabase.child("officeids").child(officeid_stack.get(position)).
                                                child("complaintstack").child(text2)
                                                .child(complaintno_stack.get(position)).removeValue();

                                        sendNotification("user", "Complaint Number:"+complaintno_stack.get(position)
                                                +" has been resolved");

                                        sendNotification("topic_menu", "Complaint Number:"+complaintno_stack.get(position)
                                                +" has been resolved");

                                        Intent intent=new Intent(getContext(),useractivity.class);
                                        Log.d("1234", "notifyDataSetChanged: ");
                                        intent.putExtra("reloadcomplaintstack",1);
                                        getContext().startActivity(intent);
                                        ((Activity)getContext()).finish();

                                    }
                                });






                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                resolved_switch.setChecked(false);

                            }
                        })
                        .setIcon(R.drawable.complaint)
                        .show();
            }
        });

        ib_download_stack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                storage = FirebaseStorage.getInstance();
                storageRef = storage.getReference();



                if(category_stack.get(position).equals("Wrong Water Bill"))
                text2="wrongbill";
                if(category_stack.get(position).equals("Error in Water Meter"))
                    text2="watermeter";
                if(category_stack.get(position).equals("No/Short Water Supply"))
                    text2="nowater";
                text3=complaintno_stack.get(position);




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


        return rowView;
    }

    private void sendNotification(final String topic, final String msg) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();
                    JSONObject dataJson = new JSONObject();
                    //this is for additional data
                    JSONObject payloadJson = new JSONObject();
                    payloadJson.put("table_number", "dsgf");
                    payloadJson.put("user_uid", firstscreen.ID);
                    // till here
                    dataJson.put("body", msg);
                    dataJson.put("title", "Complaint Resolved");
                    json.put("notification", dataJson);
                    json.put("data", payloadJson);
                    json.put("to", "/topics/".concat(topic));
                    json.put("sound", "default");
                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization", "key=" + LEGACY_SERVER_KEY)
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    com.squareup.okhttp.Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                } catch (Exception e) {
                    Log.d("XALAN_2",e.toString());
                }
                return null;
            }
        }.execute();

    }

    }
