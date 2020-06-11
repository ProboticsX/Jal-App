package com.example.shubham.jal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class office_login extends AppCompatActivity {

    Button bt_officelogin;
    EditText office_id,office_password;
    private DatabaseReference mDatabase;
    public static final String SHARED_PREFS="shared_prefs";
    public static final String OFFICEID="OFFICEID";
    ArrayList<String> userdetails=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_login);

        bt_officelogin=findViewById(R.id.bt_officelogin);
        office_id=findViewById(R.id.office_id);
        office_password=findViewById(R.id.office_password);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text1 = sharedPreferences.getString(OFFICEID,"-4");

        if (!text1.equals("-4")){
            FirebaseMessaging.getInstance().subscribeToTopic("topic_menu");
            FirebaseMessaging.getInstance().subscribeToTopic("homedelivery");
            //userdetails.add(0,text1);
            Toast.makeText(this, "logged in with Office ID: "+text1, Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(office_login.this,office_useractivity.class);
            //intent.putStringArrayListExtra("userdetails",userdetails);
            startActivity(intent);
            finish();
        }

        bt_officelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //abhi firebase se match krna hai kno aur password ko

                FirebaseMessaging.getInstance().subscribeToTopic("topic_menu");
                FirebaseMessaging.getInstance().subscribeToTopic("homedelivery");
               // userdetails.add(0,kno.getText().toString());
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(OFFICEID, office_id.getText().toString());
                editor.apply();
                Toast.makeText(office_login.this, "logged in with Office ID: "+office_id.getText().toString(), Toast.LENGTH_SHORT).show();


                Intent intent=new Intent(office_login.this,office_useractivity.class);
                //intent.putStringArrayListExtra("fromloginactivity",userdetails);
                startActivity(intent);



            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar_login, menu);
        return true;
    }
}
