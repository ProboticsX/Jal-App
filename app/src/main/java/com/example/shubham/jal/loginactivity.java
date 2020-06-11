package com.example.shubham.jal;

import android.app.ProgressDialog;
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

public class loginactivity extends AppCompatActivity {
    Button bt_userlogin;
    EditText jalpassword,kno;
    private DatabaseReference mDatabase;
    public static final String SHARED_PREFS="shared_prefs";
    public static final String KNO="kno";
    ArrayList<String> userdetails=new ArrayList<>();
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);

        bt_userlogin=findViewById(R.id.bt_userlogin);
        kno=findViewById(R.id.kno);
        jalpassword=findViewById(R.id.jalpassword);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text1 = sharedPreferences.getString(KNO,"-1");


        if (!text1.equals("-1")){
            FirebaseMessaging.getInstance().subscribeToTopic("user");
            userdetails.add(0,text1);
            Toast.makeText(this, "logged in with KNO: "+text1, Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(loginactivity.this,useractivity.class);
            intent.putStringArrayListExtra("userdetails",userdetails);
            startActivity(intent);
            finish();
        }



        bt_userlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //abhi firebase se match krna hai kno aur password ko

                //0:kno
                FirebaseMessaging.getInstance().subscribeToTopic("user");

                userdetails.add(0,kno.getText().toString());
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KNO, userdetails.get(0));
                editor.apply();

                Toast.makeText(loginactivity.this, "logged in with KNO: "+kno.getText().toString(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(loginactivity.this,useractivity.class);
                intent.putStringArrayListExtra("fromloginactivity",userdetails);
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
