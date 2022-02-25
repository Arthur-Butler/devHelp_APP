package com.younglings.devhelp.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.younglings.devhelp.R;

public class JobAdder extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_adder);
    }
    public void viewProfile(){
        Intent intent = new Intent(getApplicationContext(), LoggedInUserView.class);
        startActivity(intent);
    }
}
