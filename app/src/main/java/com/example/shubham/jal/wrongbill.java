package com.example.shubham.jal;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class wrongbill extends AppCompatActivity {

    private Uri filePath;
    Uri picUri;
    ImageButton ib_upload,ib_camera;
    Button bt_submit;
    private final int PICK_IMAGE_REQUEST = 71;
    private final int REQUEST_PICTURE_CAPTURE = 70;
    FirebaseStorage storage;
    StorageReference storageReference;
    public static final String SHARED_PREFS="shared_prefs";
    public static final String KNO="kno";
    private DatabaseReference mDatabase;
    EditText et_billnumber,et_problem;
    TextView tv_file_status;
    String imageFilePath;
    private String LEGACY_SERVER_KEY = "AIzaSyANaeOkqTcvOh9JICvtRXuzc1uqi42m4ew";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrongbill);
        ib_upload=findViewById(R.id.ib_upload);
        ib_camera=findViewById(R.id.ib_camera);
        bt_submit=findViewById(R.id.bt_submit);
        tv_file_status=findViewById(R.id.tv_file_status);
        et_billnumber=findViewById(R.id.et_billnumber);
        et_problem=findViewById(R.id.et_problem);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        final SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        final String text1 = sharedPreferences.getString(KNO,"-1");


        ib_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        });

        ib_camera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {



                Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(pictureIntent.resolveActivity(getPackageManager()) != null){
                    //Create a file to store the image
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File

                    }
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(wrongbill.this,"com.example.shubham.jal.fileprovider", photoFile);
                        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                photoURI);
                        startActivityForResult(pictureIntent,
                                REQUEST_PICTURE_CAPTURE);
                    }
                }

            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(wrongbill.this)
                        .setTitle("Complaint Submission")
                        .setMessage("Are you sure you want to confirm the complaint submission?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(imageFilePath!=null){
                                    File f = new File(imageFilePath);
                                    picUri = Uri.fromFile(f);
                                }

                                if(filePath != null || picUri!=null)
                                {
                                    if(filePath==null){
                                        filePath=picUri;
                                    }

                                    final ProgressDialog progressDialog = new ProgressDialog(wrongbill.this);
                                    progressDialog.setTitle("Submitting...");
                                    progressDialog.show();
                                    //generate complaintid in incremental order
                                    final String complaintid=text1+"wb1";
                                    StorageReference ref = storageReference.child("images/"+text1+"/wrongbill/"+ complaintid+".jpg");

                                    Date d = new Date();
                                    final CharSequence date  = DateFormat.format("MMMM d, yyyy ", d.getTime());

                                    ref.putFile(filePath)
                                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    progressDialog.dismiss();

                                                    mDatabase.child("knos").child(text1).child("complaints").child("wrongbill").child(complaintid).child("complaintno")
                                                            .setValue(complaintid);
                                                    mDatabase.child("knos").child(text1).child("complaints").child("wrongbill").child(complaintid).child("date")
                                                            .setValue(date.toString());
                                                    mDatabase.child("knos").child(text1).child("complaints").child("wrongbill").child(complaintid).child("category")
                                                            .setValue("wrongbill");
                                                    mDatabase.child("knos").child(text1).child("complaints").child("wrongbill").child(complaintid).child("problem")
                                                            .setValue("Bill number: "+et_billnumber.getText().toString()+"\n"+et_problem.getText().toString());
                                                    mDatabase.child("knos").child(text1).child("complaints").child("wrongbill").child(complaintid).child("officeid")
                                                            .setValue("dl110043");

                                                    //write code that how it judges that complaint will go to particular office only
                                                    String officeid="dl110043";
                                                    mDatabase.child("officeids").child(officeid).child("complaintstack").child("wrongbill").child(complaintid).child("kno")
                                                            .setValue(text1);
                                                    mDatabase.child("officeids").child(officeid).child("complaintstack").child("wrongbill").child(complaintid).child("complaintno")
                                                            .setValue(complaintid);
                                                    mDatabase.child("officeids").child(officeid).child("complaintstack").child("wrongbill").child(complaintid).child("date")
                                                            .setValue(date);
                                                    mDatabase.child("officeids").child(officeid).child("complaintstack").child("wrongbill").child(complaintid).child("category")
                                                            .setValue("wrongbill");
                                                    mDatabase.child("officeids").child(officeid).child("complaintstack").child("wrongbill").child(complaintid).child("problem")
                                                            .setValue("Bill number: "+et_billnumber.getText().toString()+"\n"+et_problem.getText().toString());

                                                    sendNotification("user", "Complaint Number:"+complaintid
                                                            +" has been issued");

                                                    sendNotification("topic_menu", "Complaint Number:"+complaintid
                                                            +" has been issued");

                                                    new AlertDialog.Builder(wrongbill.this)
                                                            .setTitle("Complaint Submission")
                                                            .setMessage("Your complaint has been successfully registered\nYour complaint ID:"+complaintid)
                                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                                    finish();
                                                                }
                                                            })
                                                            .setIcon(R.drawable.waterbill)
                                                            .show();

                                                    Toast.makeText(wrongbill.this, "Submitted successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(wrongbill.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                                            .getTotalByteCount());
                                                    progressDialog.setMessage("Submitting "+(int)progress+"%");
                                                }
                                            });
                                }

                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }


    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            tv_file_status.setTextColor(Color.GREEN);
            tv_file_status.setText("Your image has been uploaded");

        }
        if (requestCode == REQUEST_PICTURE_CAPTURE &&
                resultCode == RESULT_OK) {
            tv_file_status.setTextColor(Color.GREEN);
            tv_file_status.setText("Your image has been uploaded");
            if (data != null && data.getExtras() != null) {
                tv_file_status.setTextColor(Color.GREEN);
                tv_file_status.setText("Your image has been uploaded");


            }
        }
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
                    dataJson.put("title", "Wrong Water Bill");
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


