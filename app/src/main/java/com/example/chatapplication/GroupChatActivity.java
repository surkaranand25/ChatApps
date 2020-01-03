package com.example.chatapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.telephony.mbms.MbmsErrors;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class GroupChatActivity extends AppCompatActivity {
    private Toolbar mtoolbar;
    private ImageButton SendmassageButton;
    private EditText UseMassageInput;
    private ScrollView mscrollView;
    private TextView displaytextMassage;
    private String currentGroupName, currentUserID, CurrentUserName, Currentdate, Currenttime;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef, GroupNameRef, GroupMassageKeyRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_group_chat );


        currentGroupName = getIntent().getExtras().get( "GroupName"  ).toString();
        Toast.makeText( getApplicationContext(), currentGroupName, Toast.LENGTH_LONG ).show();

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child( "user" );
        GroupNameRef = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child( currentGroupName );


        Initializefields();

        GetUserInfo();

        SendmassageButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveMassageInfoToDataBase();

                UseMassageInput.setText( "" );

                mscrollView.fullScroll(ScrollView.FOCUS_DOWN  );

            }
        } );

    }

    @Override
    protected void onStart() {
        super.onStart();

        GroupNameRef.addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    DisplayMessages( dataSnapshot );

                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    DisplayMessages( dataSnapshot );

                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }


    private void SaveMassageInfoToDataBase() {
        String massage = UseMassageInput.getText().toString();
        String massagekey = GroupNameRef.push().getKey();
        if (TextUtils.isEmpty( massage )) {
            Toast.makeText( getApplicationContext(), "Please Write Massage First ", Toast.LENGTH_LONG ).show();
        } else {
            Calendar calForData = Calendar.getInstance();
            SimpleDateFormat currentDateFormat = new SimpleDateFormat( "MMM dd,yyyy  " );
            Currentdate = currentDateFormat.format( calForData.getTime() );

            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currentTimeFormat = new SimpleDateFormat( "hh:mm a" );
            Currenttime = currentTimeFormat.format( calForTime.getTime() );

            HashMap<String, Object> groupMassageKey = new HashMap<>();
            GroupNameRef.updateChildren( groupMassageKey );

            GroupMassageKeyRef = GroupNameRef.child( massagekey );
            HashMap<String, Object> massageInfoMap = new HashMap<>();
            massageInfoMap.put( "name", CurrentUserName );
            massageInfoMap.put( "date", Currentdate );
            massageInfoMap.put( "time", Currenttime );
            massageInfoMap.put( "massage", massage );

            GroupMassageKeyRef.updateChildren( massageInfoMap );

        }
    }

    private void Initializefields() {
        mtoolbar = (Toolbar) findViewById( R.id.group_Chat_Bar_layout1 );
        setSupportActionBar( mtoolbar );
        getSupportActionBar().setTitle( currentGroupName );

        SendmassageButton = findViewById( R.id.send_massage_button );
        UseMassageInput = findViewById( R.id.input_group_massage );
        displaytextMassage = findViewById( R.id.group_chat_text_display );
        mscrollView = findViewById( R.id.My_Scroll_view );
    }

    private void GetUserInfo() {
        UserRef.child( currentUserID ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) ;
                CurrentUserName = dataSnapshot.child( "name" ).getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    private void DisplayMessages(DataSnapshot dataSnapshot) {
        Iterator iterator = dataSnapshot.getChildren().iterator();
        while (iterator.hasNext()) {
            String chatDate = (String) ((DataSnapshot) iterator.next()).getValue();
            String chatMessage = (String) ((DataSnapshot) iterator.next()).getValue();
            String chatName = (String) ((DataSnapshot) iterator.next()).getValue();
            String chatTime = (String) ((DataSnapshot) iterator.next()).getValue();

            displaytextMassage.append( chatName + ":\n" + chatMessage + "\n" + chatTime + " " + chatDate + "\n\n\n" );

            mscrollView.fullScroll(ScrollView.FOCUS_DOWN  );
        }

    }


}