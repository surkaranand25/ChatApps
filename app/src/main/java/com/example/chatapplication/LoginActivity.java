package com.example.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.mbms.MbmsErrors;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
//    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private Button LoginButton, PhoneLoginBotton;
    private EditText UserEmail,UserPassword;
    private TextView NeednewAccount,ForgetPassword;
    private ProgressDialog loadingbar;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        mAuth=FirebaseAuth.getInstance();
//        currentUser=mAuth.getCurrentUser();

        Initializefields();


        NeednewAccount.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {

                    SendUsertoRegisterActivity();
                }
            }
        } );
        LoginButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
            AllowUserToLogin();
            }
        } );


        PhoneLoginBotton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
             Intent PhoneLoginIntent=new Intent( LoginActivity.this,PhoneLoginActivity.class);
             startActivity( PhoneLoginIntent );
            }
        } );
    }

    private void AllowUserToLogin()
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

            loadingbar.setTitle( "Sign In" );
            loadingbar.setMessage( "plz wait,we are creating new Account for you..." );
            loadingbar.setCanceledOnTouchOutside( true );
            loadingbar.show();
          mAuth.signInWithEmailAndPassword( Email,Password ).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task)
              {
                  if(task.isSuccessful()){
                      SendUsertoMainActivity();
                      Toast.makeText( getApplicationContext(),"Logged in successfully...",Toast.LENGTH_LONG ).show();
                      loadingbar.dismiss();

                  }
                  else {

                          String massage=task.getException().toString();
                          Toast.makeText( getApplicationContext(),"Error"+massage,Toast.LENGTH_LONG ).show();
                          loadingbar.dismiss();

                  }

              }
          } );
        }
    }

    private void Initializefields()


    {
        LoginButton=(Button)findViewById( R.id.login_button );
        PhoneLoginBotton=(Button)findViewById( R.id.phone_login_button );
        UserEmail=(EditText) findViewById( R.id.login_email );
        UserPassword=(EditText) findViewById( R.id.login_password );
        NeednewAccount=(TextView) findViewById( R.id.need_new_Account );
        ForgetPassword=(TextView) findViewById( R.id.forgot_password );
        loadingbar=new ProgressDialog( this );


    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        if (currentUser!=null)
//        {
//            SendUsertoMainActivity();
//        }
//    }

    private void SendUsertoMainActivity()
    {
        Intent MainIntent = new Intent( LoginActivity.this, MainActivity.class );
        MainIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity( MainIntent );
        finish();
    }


    private void SendUsertoRegisterActivity()
    {
        Intent RegisterIntent =new Intent( LoginActivity.this,RegisterActivity.class );
        startActivity( RegisterIntent );
    }
}


