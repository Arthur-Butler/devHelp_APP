package com.younglings.devhelp.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.younglings.devhelp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity{

    FirebaseAuth mAuth;
    FirebaseUser currentUser ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">"+getString(R.string.profile)+"</font>"));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // ini
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        userData();
    }

    public void userData() {
        CircleImageView userImage = findViewById(R.id.regUserPhoto);
        TextView userName = findViewById(R.id.username);

        userName.setText(currentUser.getDisplayName());

        if (currentUser.getPhotoUrl() != null){
            Glide.with(this).load(currentUser.getPhotoUrl()).into(userImage);
        }
        else {
            Glide.with(this).load(R.drawable.avatarr).into(userImage);
        }
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(),FirstActivity.class);
        startActivity(intent);
        finish();
    }
}