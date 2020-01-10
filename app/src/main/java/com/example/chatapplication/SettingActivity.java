package com.example.chatapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {

    private Button UpdateAccountSetting;
    private EditText username, userstatus;
    private CircleImageView userprofileImage;
    private String cuttrntUserId;
    private FirebaseAuth mAuth;
    private StorageReference UserProfileImageRef;
    private DatabaseReference RootRef;
    private static final int GallaryPick = 1;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_setting );
        mAuth = FirebaseAuth.getInstance();
        cuttrntUserId = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child( "Profile Images" );


        Initializefilds();

        username.setVisibility( View.VISIBLE );


        UpdateAccountSetting.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateSetting();
            }
        } );
        RetriveUserInfo();

        userprofileImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction( Intent.ACTION_GET_CONTENT );
                galleryIntent.setType( "image/*" );
                startActivityForResult( galleryIntent, GallaryPick );
            }
        } );
    }


    private void UpdateSetting() {
        String setUsername = username.getText().toString();
        String setStatus = userstatus.getText().toString();
        if (TextUtils.isEmpty( setUsername )) {
            Toast.makeText( getApplicationContext(), "Plz.. wreite ur username first... ", Toast.LENGTH_LONG ).show();
        }
        if (TextUtils.isEmpty( setStatus )) {
            Toast.makeText( getApplicationContext(), "Plz.. wreite ur Stastus first...", Toast.LENGTH_LONG ).show();
        } else {
            HashMap<String, String> profilemap = new HashMap<>();
            profilemap.put( "uid", cuttrntUserId );
            profilemap.put( "name", setUsername );
            profilemap.put( "status", setStatus );
            RootRef.child( "user" ).child( cuttrntUserId ).setValue( profilemap ).addOnCompleteListener( new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        SendUsertoMainActivity();
                        Toast.makeText( getApplicationContext(), "Profile updated Succesfully...", Toast.LENGTH_LONG ).show();

                    } else {
                        String massage = task.getException().toString();
                        Toast.makeText( getApplicationContext(), "Error" + massage, Toast.LENGTH_LONG ).show();

                    }
                }
            } );

        }

    }

    private void Initializefilds() {
        userprofileImage = findViewById( R.id.profile_image );
        UpdateAccountSetting = findViewById( R.id.Update_setting_button );
        username = findViewById( R.id.User_name );
        userstatus = findViewById( R.id.Profile );
        loadingBar = new ProgressDialog( this );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if (requestCode == GallaryPick && resultCode == RESULT_OK && data != null) {
            Uri ImageUri = data.getData();
            CropImage.activity()
                    .setGuidelines( CropImageView.Guidelines.ON )
                    .setAspectRatio( 1, 1 )
                    .start( this );
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult( data );
            if (resultCode == RESULT_OK) {
                loadingBar.setTitle( "Set Profile Image" );
                loadingBar.setMessage( "plz wait,Profile Image uploading..." );
                loadingBar.setCanceledOnTouchOutside( false );
                loadingBar.show();
                Uri resultUri = result.getUri();

                final StorageReference filePath = UserProfileImageRef.child( cuttrntUserId + "jpg" );
                filePath.putFile( resultUri ).addOnCompleteListener( new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText( getApplicationContext(), "Profile Image uploaded Successfully", Toast.LENGTH_LONG ).show();

//
//
                            filePath.getDownloadUrl().addOnCompleteListener( new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String downloadUrl = task.getResult().toString();
                                    Log.d( "", "onComplete: " + downloadUrl );
                                    RootRef.child( "user" ).child( cuttrntUserId ).child( "image" )
                                            .setValue( downloadUrl ).addOnCompleteListener( new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                Toast.makeText( getApplicationContext(), "Image downloaded Successfully", Toast.LENGTH_LONG ).show();
                                                loadingBar.dismiss();
                                            } else {
                                                String massage = task.getException().toString();
                                                Toast.makeText( getApplicationContext(), "Error" + massage, Toast.LENGTH_LONG ).show();
                                                loadingBar.dismiss();

                                            }

                                        }
                                    } );

                                }
                            } );


                        } else {
                            String massage = task.getException().toString();
                            Toast.makeText( getApplicationContext(), "Error" + massage, Toast.LENGTH_LONG ).show();
                            loadingBar.dismiss();

                        }

                    }
                } );
            }
        }
    }

    private void RetriveUserInfo() {
        RootRef.child( "user" ).child( cuttrntUserId )
                .addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name")&&(dataSnapshot.hasChild( "image" )))) {
                            String retriveUserName = dataSnapshot.child( "name" ).getValue().toString();
                            String retriveStatus = dataSnapshot.child( "status" ).getValue().toString();
                            String retriveProfileImage = dataSnapshot.child( "image" ).getValue().toString();
                            username.setText( retriveUserName );
                            userstatus.setText( retriveStatus );
                            Picasso.get().load( retriveProfileImage ).into( userprofileImage );


                        } else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild( "name" ))) {
                            String retriveUserName = dataSnapshot.child( "name" ).getValue().toString();
                            String retriveStatus = dataSnapshot.child( "status" ).getValue().toString();
//                       String retriveProfileImage = dataSnapshot.child("image").getValue().toString();
                            username.setText( retriveUserName );
                            userstatus.setText( retriveStatus );


                        } else {
                            username.setVisibility( View.VISIBLE );
                            Toast.makeText( getApplicationContext(), "Please set and Update yous Profile", Toast.LENGTH_LONG ).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );
    }

    private void SendUsertoMainActivity() {
        Intent MainIntent = new Intent( SettingActivity.this, MainActivity.class );
        MainIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity( MainIntent );
        finish();
    }
}
