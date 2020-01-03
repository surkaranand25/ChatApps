package com.example.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {
    private Button CreateAccountButton;
    private EditText UserEmail,UserPassword;
    private TextView AlreadyHaveAccount;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    private ProgressDialog loadingBar;



    @Override  
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );



        mAuth=FirebaseAuth.getInstance();
        RootRef= FirebaseDatabase.getInstance().getReference();

       Initializefields();

        AlreadyHaveAccount.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUsertoLoginActivity();
            }
        } );
        CreateAccountButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 CreateNewAccount();


            }
        } );



    }

    private void CreateNewAccount()
    {

        String Email=UserEmail.getText().toString();
        String Password=UserPassword.getText().toString();


        if (Email.isEmpty()) {
            UserEmail.setError("Email required");
            UserEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            UserEmail.setError("Please enter valid email");
            UserEmail.requestFocus();
            return;
        }

        if (Password.isEmpty()) {
            UserPassword.setError("Password required");
            UserPassword.requestFocus();
            return;
        }

        if (Password.length() < 6) {
            UserPassword.setError("Minimum length 6");
            UserPassword.requestFocus();
            return;
        }
        if (Email.isEmpty(  ))
        {
            Toast.makeText( getApplicationContext(),"Plz Enter Email...",Toast.LENGTH_LONG ).show();
        }
        if (Password.isEmpty(  ))
        {
            Toast.makeText( getApplicationContext(),"Plz Enter Password",Toast.LENGTH_LONG ).show();
        }
         else
        {
            loadingBar.setTitle( "Creating New Account" );
            loadingBar.setMessage( "plz wait,we are creating new Account for you..." );
            loadingBar.setCanceledOnTouchOutside( true );
            loadingBar.show();
            mAuth.createUserWithEmailAndPassword( Email,Password ).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if(task.isSuccessful())
                    {

                        String currentUserId=mAuth.getCurrentUser().getUid();
                        RootRef.child( "user" ).child( currentUserId ).setValue( "" );
                        
                        SendUsertoMainActivity();
                        Toast.makeText( getApplicationContext(),"Account Created Successfully",Toast.LENGTH_LONG ).show();
                        loadingBar.dismiss();
                    }
                    else
                    {
                        String massage=task.getException().toString();
                        Toast.makeText( getApplicationContext(),"Error"+massage,Toast.LENGTH_LONG ).show();
                        loadingBar.dismiss();

                    }

                }
            } ).addOnFailureListener( new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

                }
            } );
        }
    }

    private void SendUsertoMainActivity()
    {
        Intent MainIntent = new Intent( RegisterActivity.this, MainActivity.class );
        MainIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity( MainIntent );
        finish();
    }

    private void SendUsertoLoginActivity() {
        Intent loginIntent = new Intent( RegisterActivity.this, LoginActivity.class );
      startActivity( loginIntent );
    }


    private void Initializefields() {
        CreateAccountButton=(Button)findViewById( R.id.register_button );
        UserEmail=(EditText) findViewById( R.id.register_email );
        UserPassword=(EditText)findViewById( R.id.register_password );
        AlreadyHaveAccount=(TextView) findViewById( R.id.allready_have_account );
        loadingBar=new ProgressDialog( this );
    }
}
