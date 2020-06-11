package com.example.shubham.jal;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONObject;

import java.io.File;
import java.util.Date;

public class nowater extends AppCompatActivity {



    Button bt_submit;
    FirebaseStorage storage;
    StorageReference storageReference;
    public static final String SHARED_PREFS="shared_prefs";
    public static final String KNO="kno";
    private DatabaseReference mDatabase;
    EditText et_oldwcn_number_nowater,et_problem;

    private String LEGACY_SERVER_KEY = "AIzaSyANaeOkqTcvOh9JICvtRXuzc1uqi42m4ew";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nowater);

        bt_submit=findViewById(R.id.bt_submit_nowater);
        et_oldwcn_number_nowater=findViewById(R.id.et_oldwcn_number_nowater);
        et_problem=findViewById(R.id.et_problem_nowater);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        final SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        final String text1 = sharedPreferences.getString(KNO,"-1");

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(nowater.this)
                        .setTitle("Complaint Submission")
                        .setMessage("Are you sure you want to confirm the complaint submission?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                    //generate complaintid in incremental order
                                    final String complaintid=text1+"nw1";

                                Date d = new Date();
                                final CharSequence date  = DateFormat.format("MMMM d, yyyy ", d.getTime());


                                mDatabase.child("knos").child(text1).child("complaints").child("nowater").child(complaintid).child("complaintno")
                                        .setValue(complaintid);
                                mDatabase.child("knos").child(text1).child("complaints").child("nowater").child(complaintid).child("date")
                                        .setValue(date.toString());
                                mDatabase.child("knos").child(text1).child("complaints").child("nowater").child(complaintid).child("category")
                                        .setValue("nowater");
                                mDatabase.child("knos").child(text1).child("complaints").child("nowater").child(complaintid).child("problem")
                                        .setValue("Old WCN number: "+et_oldwcn_number_nowater.getText().toString()+"\n"+et_problem.getText().toString());
                                mDatabase.child("knos").child(text1).child("complaints").child("nowater").child(complaintid).child("officeid")
                                        .setValue("dl110043");

                                //write code that how it judges that complaint will go to particular office only
                                String officeid="dl110043";
                                mDatabase.child("officeids").child(officeid).child("complaintstack").child("nowater").child(complaintid).child("kno")
                                        .setValue(text1);
                                mDatabase.child("officeids").child(officeid).child("complaintstack").child("nowater").child(complaintid).child("complaintno")
                                        .setValue(complaintid);
                                mDatabase.child("officeids").child(officeid).child("complaintstack").child("nowater").child(complaintid).child("date")
                                        .setValue(date);
                                mDatabase.child("officeids").child(officeid).child("complaintstack").child("nowater").child(complaintid).child("category")
                                        .setValue("nowater");
                                mDatabase.child("officeids").child(officeid).child("complaintstack").child("nowater").child(complaintid).child("problem")
                                        .setValue("Old WCN number: "+et_oldwcn_number_nowater.getText().toString()+"\n"+et_problem.getText().toString());



                                sendNotification("topic_menu", "Complaint Number:"+complaintid
                                        +" has been issued");
                                sendNotification("user", "Complaint Number:"+complaintid
                                        +" has been issued");

                                new AlertDialog.Builder(nowater.this)
                                        .setTitle("Complaint Submission")
                                        .setMessage("Your complaint has been successfully registered\nYour complaint ID:"+complaintid)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                finish();

                                            }
                                        })
                                        .setIcon(R.drawable.nowater)
                                        .show();

                                Toast.makeText(nowater.this, "Submitted successfully", Toast.LENGTH_SHORT);

                            }
                        })

                        .setNegativeButton("No", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });


    }
    private void sendNotification(final String topic, final String msg)
    {
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
                    dataJson.put("title", "No/Short Water Supply");
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
