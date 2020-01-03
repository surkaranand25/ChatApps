package com.example.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static android.os.Build.VERSION_CODES.P;

public class PhoneLoginActivity extends AppCompatActivity {


    private Button sendVarificationCodeButton,VerifyButton;
    private EditText inputePhoneNumber,inputVarificationCode;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_phone_login );

        mAuth=FirebaseAuth.getInstance();
        loadingBar=new ProgressDialog(this);
        sendVarificationCodeButton=findViewById( R.id.send_ver_code_Button );
                VerifyButton=findViewById( R.id.verify_button );
        inputePhoneNumber=findViewById( R.id.Phone_number_input );
                inputVarificationCode=findViewById( R.id.Varification_Code_input );



                sendVarificationCodeButton.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {


                          String phoneNumber = inputePhoneNumber.getText().toString();
                          if(TextUtils.isEmpty( phoneNumber ))
                          {
                              Toast.makeText( getApplicationContext(),"Please Enter your Phone Number ",Toast.LENGTH_LONG ).show();

                          }
                          else
                              {


                                  loadingBar.setTitle( "Phone Varification" );
                                  loadingBar.setMessage( "plz wait,we are Authenticating your phone..." );
                                  loadingBar.setCanceledOnTouchOutside( false );
                                  loadingBar.show();
                                  PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                          phoneNumber,        // Phone number to verify
                                          60,                 // Timeout duration
                                          TimeUnit.SECONDS,   // Unit of timeout
                                          PhoneLoginActivity.this,               // Activity (for callback binding)
                                          callbacks);        // OnVerificationStateChangedCallbacks


                              }

                    }
                } );

                VerifyButton.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        sendVarificationCodeButton.setVisibility( View.INVISIBLE );
                        inputePhoneNumber.setVisibility( View.INVISIBLE );
                        String varificationCode=inputVarificationCode.getText().toString();
                        if(TextUtils.isEmpty(varificationCode))
                        {
                            Toast.makeText( getApplicationContext(),",Please write varification Code First..",Toast.LENGTH_LONG ).show();

                        }
                        else {


                            loadingBar.setTitle( "Varification code" );
                            loadingBar.setMessage( "plz wait,while we are Varifing Varification code..." );
                            loadingBar.setCanceledOnTouchOutside( false );
                            loadingBar.show();
                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, varificationCode);
                          signInWithPhoneAuthCredential( credential );
                        }
                    }
                } );

                callbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
                    {
                        signInWithPhoneAuthCredential( phoneAuthCredential );

                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e)
                    {
                        loadingBar.dismiss();
                        Toast.makeText( getApplicationContext(),"Invalid Phone Number,Please enter your Phone Number with Country Code ..",Toast.LENGTH_LONG ).show();

                        sendVarificationCodeButton.setVisibility( View.VISIBLE );
                        inputePhoneNumber.setVisibility( View.VISIBLE );
                        VerifyButton.setVisibility( View.INVISIBLE );
                        inputVarificationCode.setVisibility( View.INVISIBLE );
                    }

                    public void onCodeSent(@NonNull String verificationId,
                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {

                        // Save verification ID and resending token so we can use them later
                        mVerificationId = verificationId;
                        mResendToken = token;
                        loadingBar.dismiss();
                          Toast.makeText( getApplicationContext(),"Code has been sent ,Please verify",Toast.LENGTH_LONG ).show();

                        sendVarificationCodeButton.setVisibility( View.INVISIBLE );
                        inputePhoneNumber.setVisibility( View.INVISIBLE );
                        VerifyButton.setVisibility( View.VISIBLE );
                        inputVarificationCode.setVisibility( View.VISIBLE );
                    }
                };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                         loadingBar.dismiss();
                         Toast.makeText( getApplicationContext(),"Congratulation, you're Logged in successfully",Toast.LENGTH_LONG ).show();
                         sendUserToMainActivity();
                        }
                        else
                            {
                           String message= task.getException().toString();
                                Toast.makeText( getApplicationContext(),"Error:"+ message,Toast.LENGTH_LONG ).show();


                            }
                    }
                });
    }

    private void sendUserToMainActivity()
    {
        Intent mainIntent=new Intent( PhoneLoginActivity.this,MainActivity.class );
        startActivity( mainIntent );

    }

}
