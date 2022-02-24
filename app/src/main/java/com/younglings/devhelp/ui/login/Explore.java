package com.younglings.devhelp.ui.login;

import android.database.DataSetObserver;
import android.os.Bundle;
import java.util.Hashtable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import java.util.ArrayList;

import android.view.View;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;
import com.google.protobuf.Internal;
import com.younglings.devhelp.ui.login.models.Job;
import com.younglings.devhelp.ui.login.models.Post;
import java.util.Arrays;
import androidx.navigation.NavController;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ListAdapter;

import com.younglings.devhelp.R;


public class Explore extends AppCompatActivity {
    private TextView databaseView;
    private static final String TAG = "MainActivity";
    public String valueHolder;
    public String[] name = {};
    public String[] positions = {};
    public int get_count;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Jobs");
    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<String> posList= new ArrayList<>();
    ListView listView;
    public DatabaseReference mDatabase;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.devhelp_explore_page);
        //databaseView = findViewById(R.id.content);
        listView = (ListView) findViewById(R.id.jobList);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                long long_count = dataSnapshot.getChildrenCount();
                Log.d("firebase", String.valueOf(long_count));
                get_count = (int) (long) long_count;
                for (int i = 1; i < get_count; i++) {
                    String urlComp = "Jobs/" + i;
                    DatabaseReference myReference = database.getReference(urlComp);
                    basicRead(myReference);
                    /*String urlPos = "Jobs/" + i + "/Position";
                    myReference = database.getReference(urlPos);
                    basicRead(myReference);*/
                }
                Log.d("firebase", String.valueOf(get_count));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
    public void basicRead(DatabaseReference myRefer){
        myRefer.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName){
                    Hashtable<Integer, String> my_dict = new Hashtable<Integer, String>();
                    for (int i = 1; i < 3; i++){
                        my_dict.put(i, snapshot.getValue(String.class));
                    }
                    /*String plate = snapshot.getValue(String.class);
                    String email = snapshot.child("Position").getValue(String.class);*/
                    nameList = new ArrayList<String>(Arrays.asList(name));
                    nameList.add(my_dict.get(1));
                    name = nameList.toArray(name);
                    posList = new ArrayList<String>(Arrays.asList(positions));
                    posList.add(my_dict.get(2));
                    positions = posList.toArray(positions);
                    ListViewExplore customBaseAdapter = new ListViewExplore(getApplicationContext(), name, positions);
                    listView.setAdapter(customBaseAdapter);
                    Log.d("firebase", my_dict.get(1) + " " + my_dict.get(2));
                    //etc
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String plate = ds.child("Company").getValue(String.class).toString();
                    String email = ds.child("Position").getValue(String.class).toString();
                    nameList = new ArrayList<String>(Arrays.asList(name));
                    nameList.add(plate);
                    name = nameList.toArray(name);
                    posList = new ArrayList<String>(Arrays.asList(positions));
                    posList.add(email);
                    positions = posList.toArray(positions);
                    ListViewExplore customBaseAdapter = new ListViewExplore(getApplicationContext(), name, positions);
                    listView.setAdapter(customBaseAdapter);
                    Log.d("firebase", plate + " " + email);
                    //etc
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String plate = ds.child("Company").getValue(String.class).toString();
                    String email = ds.child("Position").getValue(String.class).toString();
                    nameList = new ArrayList<String>(Arrays.asList(name));
                    nameList.add(plate);
                    name = nameList.toArray(name);
                    posList = new ArrayList<String>(Arrays.asList(positions));
                    posList.add(email);
                    positions = posList.toArray(positions);
                    ListViewExplore customBaseAdapter = new ListViewExplore(getApplicationContext(), name, positions);
                    listView.setAdapter(customBaseAdapter);
                    Log.d("firebase", plate + " " + email);
                    //etc
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String plate = ds.child("Company").getValue(String.class).toString();
                    String email = ds.child("Position").getValue(String.class).toString();
                    nameList = new ArrayList<String>(Arrays.asList(name));
                    nameList.add(plate);
                    name = nameList.toArray(name);
                    posList = new ArrayList<String>(Arrays.asList(positions));
                    posList.add(email);
                    positions = posList.toArray(positions);
                    ListViewExplore customBaseAdapter = new ListViewExplore(getApplicationContext(), name, positions);
                    listView.setAdapter(customBaseAdapter);
                    Log.d("firebase", plate + " " + email);
                    //etc
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void viewJob(){
        setContentView(R.layout.job_viewer);
    };

    public void addJob(View view) {
        setContentView(R.layout.job_adder);
    }

    public void writeNewJob() {
        TextView companyName = findViewById(R.id.CompanyName);
        TextView description = findViewById(R.id.Description);
        Spinner spinnerDev = findViewById(R.id.spinnerDevAdd);
        Spinner spinnerLang = findViewById(R.id.spinnerMainLang);
        String compVar= companyName.getText().toString();
        String descriptVar= description.getText().toString();
        String devVar= spinnerDev.getSelectedItem().toString();
        String langVar= spinnerLang.getSelectedItem().toString();
        mDatabase.child("Jobs").child(String.valueOf(get_count+1)).child("Company").setValue(compVar);
        mDatabase.child("Jobs").child(String.valueOf(get_count+1)).child("Description").setValue(descriptVar);
        mDatabase.child("Jobs").child(String.valueOf(get_count+1)).child("SpecializationNeeded").setValue(devVar);
        mDatabase.child("Jobs").child(String.valueOf(get_count+1)).child("NeededSkills").setValue(langVar);
    }
    public void getJobCount(){
    }

}
