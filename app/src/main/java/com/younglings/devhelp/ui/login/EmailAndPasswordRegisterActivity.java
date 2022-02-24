package com.younglings.devhelp.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
//import android.view.View.getRootView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.younglings.devhelp.R;
import com.younglings.devhelp.ui.login.models.Item;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmailAndPasswordRegisterActivity extends AppCompatActivity{
    ImageView userPhoto;
    static int PreReqCode = 1;
    static int REQUESCODE = 1;
    Uri pickedImgUri;
    private DatabaseReference mDatabase;
    private String TAG;
    private TextInputEditText userEmail, userPassword, userPassword2, userName;
    private ProgressBar loadingProgress;
    private Button regBtn, signBtn;
    FirebaseFirestore fStore;
    private FirebaseAuth mAuth;
    private Spinner spinnerFields;
    private List<String> items;
    private TextView multiLangs;
    private boolean[] selectedLang;
    String[] langArray = {"Python","C#", "EXPRESS JS", "REACT JS","NODE JS", "JAVA", "PHP", "DART", "FLUTTER", "SQL", "DELPHI", "KOTLIN", "C++", "C", "SAAS", "DJANGO", "MACHINE LEARNING", "MATPLOTLIB PY"};
    ArrayList<Integer> langList = new ArrayList<>();
    //private MultiSpinner spinner;
    private ArrayAdapter<String> adapter;
    private boolean[] selectedItems;
    String userID;
    //MultiSpinner mySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activity_email_and_password_register);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userEmail = findViewById(R.id.regMail);
        userPassword = findViewById(R.id.regPassword);
        userPassword2 = findViewById(R.id.regPassword2);
        userName = findViewById(R.id.regName);
        loadingProgress = findViewById(R.id.regProgressBar);
        regBtn = findViewById(R.id.regBtn);
        spinnerFields=findViewById(R.id.spinnerField);
        multiLangs=findViewById(R.id.multiLang);
        selectedLang =new boolean[langArray.length];
        multiLangs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        EmailAndPasswordRegisterActivity.this
                );
                builder.setTitle("Select Skills");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(langArray, selectedLang, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i, boolean isChecked) {
                        if (isChecked){
                            langList.add(i);
                            Collections.sort(langList);
                        } else {
                            langList.remove(i);
                        }
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int j=0; j<langList.size(); j++){
                            stringBuilder.append(langArray[langList.get(j)]);
                            if (j != langList.size()-1){
                                stringBuilder.append(", ");
                            }
                        }
                        multiLangs.setText(stringBuilder.toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int j=0;j<selectedLang.length; j++){
                            selectedLang[j]=false;
                            langList.clear();
                            multiLangs.setText("");
                        }
                    }
                });
                builder.show();
            }
        });
        /*ArrayList<Item> items = new ArrayList<>();
        items.add(Item.builder().name('Item 1').value('item-1').build());
        items.add(Item.builder().name('Item 2').value('item-2').build());
        items.add(Item.builder().name('Item 3').value('item-3').build());

        final View mySpinner = (MultiSpinner) findViewById(R.id.spinnerLang);
        mySpinner.setItems(items);

        // get spinner and set adapter

        // set initial selection
        selectedItems = new boolean[adapter.getCount()];
        selectedItems[1] = true; // select second item
        spinner.setSelected(selectedItems);*/


        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this, R.array.field_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        //spinnerFields.setAdapter(adapter);

        ArrayAdapter<CharSequence>adapter2=ArrayAdapter.createFromResource(this, R.array.lang_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        //spinnerLangs.setAdapter(adapter2);
        //       signBtn = findViewById(R.id.sigBtn);
//        signBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(EmailAndPasswordRegisterActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });

        loadingProgress.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                regBtn.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);
                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();
                final String password2 = userPassword2.getText().toString();
                final String name = userName.getText().toString();
                final String image = userPhoto.getDrawable().toString();
                final String field = spinnerFields.getSelectedItem().toString();
                final String languages = multiLangs.getText().toString();


                if (image.isEmpty() || email.isEmpty() || name.isEmpty() || password.isEmpty() || !password.equals(password2)) {


                    // something goes wrong : all fields must be filled
                    // we need to display an error message
                    showMessage("Please Verify all fields");
                    regBtn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);


                } else {
                    // everything is ok and all fields are filled now we can start creating user account
                    // CreateUserAccount method will try to create the user if the email is valid

                    writeNewUser(email, name, password, field, languages);
                }
            }
        });
        userPhoto = findViewById(R.id.regUserPhoto);
        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 22) {
                    checkAndReqForPermission();
                } else {
                    openGalery();
                }
            }
        });
    }

    /*private MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] selected) {
            // Do something here with the selected items
        }
    };*/

    public void writeNewUser(String email, String name, String password, String field, String languages) {
        mDatabase.child("Users").child(name).child("Password").setValue(password);
        mDatabase.child("Users").child(name).child("Email").setValue(email);
        mDatabase.child("Users").child(name).child("Specialization").setValue(field);
        mDatabase.child("Users").child(name).child("Skills").setValue(languages);
        /*mDatabase.child(name).setValue("Password");
        mDatabase.child("Password").setValue(password);
        mDatabase.child(name).setValue("Email");
        mDatabase.child("Email").setValue(email);*/
    }

    private void updateUserInfo(String name, Uri pickedImgUri, FirebaseUser currentUser) {
        // first we need to upload user photo to firebase storage and get url

        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // image uploaded succesfully
                // now we can get our image url

                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        // uri contain user image url


                        UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();


                        currentUser.updateProfile(profleUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            // user info updated successfully
                                            showMessage("Register Complete");
                                            updateUI();
                                        }

                                    }
                                });

                    }
                });
            }
        });
    }

    //without image

    private void updateUserInfoWithoutPhoto(String name, FirebaseUser currentUser) {
        // first we need to upload user photo to firebase storage and get url


        UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();


        currentUser.updateProfile(profleUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            // user info updated successfully
                            showMessage("Register Complete");
                            updateUI();
                        }

                    }
                });


    }


    private void updateUI() {
        Intent homeActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(homeActivity);
        finish();
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void openGalery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);

    }

    private void checkAndReqForPermission() {
        if (ContextCompat.checkSelfPermission(EmailAndPasswordRegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(EmailAndPasswordRegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(EmailAndPasswordRegisterActivity.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(EmailAndPasswordRegisterActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PreReqCode);
            }
        } else {
            openGalery();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData();
            userPhoto.setImageURI(pickedImgUri);


        }


    }
    public void backsignup(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

}
