package com.younglings.devhelp.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.younglings.devhelp.R;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import com.younglings.devhelp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;

public class FirstActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser currentUser ;
    private EditText userMail,userPassword;
    private String fire_value;
    private String pswd_input;
    private DatabaseReference mDatabase;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


    }

    public void gotosignuppage(View view) {
        Intent intent = new Intent(getApplicationContext(), EmailAndPasswordRegisterActivity.class);
        startActivity(intent);
    }

    public void gotosigninpage(View view) {
        userMail = findViewById(R.id.username);
        userPassword = findViewById(R.id.password);
        mDatabase.child("Users").child(userMail.getText().toString()).child("Password").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    showMessage("Network issues when signing in");
                    setContentView(R.layout.activity_login);
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    fire_value = String.valueOf(task.getResult().getValue());
                    String pswd = userPassword.getText().toString();
                    if(fire_value == pswd) {
                        Log.d("firebase","Password or Username Incorrect");
                        showMessage("Password or Username Incorrect");
                        setContentView(R.layout.activity_login);
                    }
                    else{
                        Log.d("firebase","Password or Username Correct");
                        Intent intent = new Intent(getApplicationContext(), Explore.class);
                        startActivity(intent);
                    }

                }
            }
        });
    }
    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(),text, Toast.LENGTH_LONG).show();
    }

}
