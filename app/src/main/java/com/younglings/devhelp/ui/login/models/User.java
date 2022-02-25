package com.younglings.devhelp.ui.login.models;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.ValueEventListener;

@IgnoreExtraProperties
public class User {
    public String username;
    public String password;
    private DatabaseReference mDatabase;
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        mDatabase.child("LoggedIn").child("username").setValue(username);
        mDatabase.child("LoggedIn").child("password").setValue(password);
    }

}
