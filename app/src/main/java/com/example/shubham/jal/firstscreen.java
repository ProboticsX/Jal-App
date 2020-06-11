package com.example.shubham.jal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

public class firstscreen extends AppCompatActivity {

    Button login1,login2;
    FirebaseAuth mAuth;
    public static String ID;
    boolean isUserLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstscreen);
        login1=findViewById(R.id.bt_login1);
        login2=findViewById(R.id.bt_login2);
        mAuth = FirebaseAuth.getInstance();

        loginUser();

        login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(firstscreen.this,loginactivity.class);
                startActivity(intent);
                finish();
            }
        });

        login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(firstscreen.this,office_login.class);



                startActivity(intent);
                finish();
            }
        });
    }

    public void loginUser() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseMessaging.getInstance().subscribeToTopic(user.getUid());
                            //Log.d("XALAN_7", "onComplete: "+mAuth.getCurrentUser());
                            Log.d("XALAN_LOGIN_FUN",mAuth.getCurrentUser().getUid());
                            ID=mAuth.getCurrentUser().getUid();
                            isUserLoggedIn = true;
                            Toast.makeText(firstscreen.this, "You have successfully Logged In", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInAnonymously:failure", task.getException());
                            isUserLoggedIn = false;
                            loginUser();
                        }

                        // ...
                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
        try {
            mAuth.getCurrentUser();
            ID = mAuth.getCurrentUser().getUid();
            Log.d("XALAN_ONSTART_MAIN", "onStart: " + mAuth.getCurrentUser().getUid());
        }
        catch (Exception e){
            Log.d("aaaaaa", "onStart: "+e);
        }
    }
}
