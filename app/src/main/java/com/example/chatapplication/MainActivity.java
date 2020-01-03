package com.example.chatapplication;


import android.app.AlertDialog;
import android.app.VoiceInteractor;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Toolbar mtoolbar;
    private ViewPager myviewPager;
    private TabLayout mytabLayout;
    private TabsAccessorAdapter mytabsAccessorAdapter;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        RootRef= FirebaseDatabase.getInstance().getReference();

        mtoolbar=findViewById(R.id.main_page_toolbar );
        setSupportActionBar( mtoolbar );
        getSupportActionBar().setTitle( "ChatApplication" );

        myviewPager=(ViewPager)findViewById( R.id.main_tab_pager );
        mytabsAccessorAdapter=new TabsAccessorAdapter( getSupportFragmentManager() );
        myviewPager.setAdapter( mytabsAccessorAdapter );

        mytabLayout=(TabLayout)findViewById( R.id.main_tab );
        mytabLayout.setupWithViewPager(myviewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser==null)
        {
            SendUsertoLoginActivity();
        }
        else
            {
            VerifyUserExistance();
        }
    }

    private void VerifyUserExistance()
    {
        String currentUserId=mAuth.getCurrentUser().getUid();

        RootRef.child( "user" ).child( currentUserId ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
              if ((dataSnapshot.child( "name" ).exists()))
              {
                  Toast.makeText( getApplicationContext(),"Wellcome",Toast.LENGTH_LONG ).show();

              }
              else
                  {
                      SendUsertoSettingActivity();

                  }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
         super.onCreateOptionsMenu( menu );

        getMenuInflater().inflate( R.menu.option_menu,menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
         super.onOptionsItemSelected( item );
         if (item.getItemId() ==R.id.Log_out){
          mAuth.signOut();
          SendUsertoLoginActivity();
         }
        if (item.getItemId() ==R.id.Setting){

            SendUsertoSettingActivity();
        }
        if (item.getItemId() ==R.id.find_friends)
        {
            SendUsertoFindFriendActivity();

        }
        if (item.getItemId() ==R.id.Creat_Group1)
        {

            RequestNewGroup();
        }
        return true;
    }

    private void RequestNewGroup()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder( MainActivity.this,R.style.AlertDialog );
        builder.setTitle( "enter Group Name" );
        final EditText groupNameField =new EditText( MainActivity.this );
        groupNameField.setHint( "   e.g = college Group " );
        builder.setView( groupNameField );
        builder.setPositiveButton( "Creat", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                 String groupName=groupNameField.getText().toString();
                 if(TextUtils.isEmpty(groupName))
                {
                    Toast.makeText( getApplicationContext(),"Please write Group Name",Toast.LENGTH_LONG ).show();

                 }
                 else
                       {
                         CreatGroupName(groupName);
                 }

            }
        } );

        builder.setNegativeButton( "Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
//                String groupName=groupNameField.getText().toString();
                  dialog.cancel();
            }
        } );
        builder.show();
    }

    private void CreatGroupName(final String groupName)
    {
        RootRef.child( "Groups" ).child(groupName).setValue( "" ).addOnCompleteListener( new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    Toast.makeText( getApplicationContext(),groupName+"is created Successfully...",Toast.LENGTH_LONG ).show();

                }
            }
        } );
    }

    private void SendUsertoLoginActivity()
    {
        Intent loginIntent =new Intent( MainActivity.this,LoginActivity.class );
        loginIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK );

        startActivity( loginIntent );
        finish();
    }
    private void SendUsertoSettingActivity()
    {
        Intent SettingIntent =new Intent( MainActivity.this,SettingActivity.class );
        SettingIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK );

        startActivity( SettingIntent );
        finish();
    }
    private void SendUsertoFindFriendActivity()
    {
        Intent FindFriendIntent =new Intent( MainActivity.this,FindFriendsActivity.class );

        startActivity( FindFriendIntent );
        finish();
    }
}
