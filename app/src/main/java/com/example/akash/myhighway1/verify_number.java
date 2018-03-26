package com.example.akash.myhighway1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class verify_number extends AppCompatActivity {
    FirebaseAuth fbauth;
    PhoneAuthProvider pauth;
    PhoneAuthCredential crd;
    String mobno="",verification_id="";
    TextView mobnotext,codetext;
    Button verifyButton,sendButton,resendButton;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String PREFRENCENAME="AKASHSASH";
    PhoneAuthProvider.ForceResendingToken token1;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationcallback;
    FirebaseAuth.AuthStateListener mauthListener;
    private PhoneAuthCredential pcredential;
    int counter=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences=getSharedPreferences(PREFRENCENAME, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        setContentView(R.layout.activity_verify_number);
        fbauth=FirebaseAuth.getInstance();
        //t1=(TextView) findViewById(R.id.username);
        mobnotext=(TextView) findViewById(R.id.mobnotextverify);
        codetext=(TextView) findViewById(R.id.otpcodetextverify);
        //verify1=(Button)findViewById(R.id.verifybutton);
        sendButton=(Button)findViewById(R.id.sendotpButtonverify);
        verifyButton=(Button)findViewById(R.id.verifyButtonverify);
        mobnotext.setText("+91"+getIntent().getStringExtra("mobile"));
        mobnotext.setEnabled(false);
       // resendButton=(Button)findViewById(R.id.resendbutton);
        //verify1.setOnClickListener(this);
        //t1.setText(getIntent().getStringExtra("email"));
        //Button signout=(Button) findViewById(R.id.signoutbutton);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar2);
        //getActionBar().setCustomView(t);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActivityCompat.requestPermissions(verify_number.this,new String[]{Manifest.permission.SEND_SMS},1);

        setupverificationcallback();
        mauthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("firebase log:", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("firebase log:", "onAuthStateChanged:signed_out");
                }
                // ...
            }

        };
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(counter==0)
                validatePhoneNumber(view);
                else
                    resendVerificationCode(mobno,token1);
            }
        });
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(view);
            }
        });

    }
    private void verifyPhoneNumber(PhoneAuthCredential credential) {
        String code=codetext.getText().toString();
        fbauth.signInWithCredential(credential).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    editor.putString("userMobNumberkey",mobno);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "Verified!!!! Go on..", Toast.LENGTH_LONG).show();
                    FirebaseUser user=task.getResult().getUser();
                    Intent IntentwithData=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(IntentwithData);
                    finish();
                }
                else{
                    if(counter<3) {
                        if(counter<1){
                            sendButton.setEnabled(true);
                            sendButton.setText("ReSend OTP");
                        }
                        Toast.makeText(getApplicationContext(), "Not Verified!!!! "+(3-counter)+"time remain", Toast.LENGTH_LONG).show();
                        counter++;
                    }
                    else {
                        finish();
                    }
                }
            }
        });
    }


    public void validatePhoneNumber(View view){
        mobno=mobnotext.getText().toString();
        Toast.makeText(getApplicationContext(),mobno,Toast.LENGTH_SHORT).show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+mobno,
                60,
                TimeUnit.SECONDS,
                this,
                verificationcallback);
        sendButton.setEnabled(false);
    }
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                verificationcallback,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
        sendButton.setEnabled(false);
    }



    public void setupverificationcallback(){
        verificationcallback=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(getApplicationContext(), "verification completed!!! ", Toast.LENGTH_SHORT).show();
                verifyPhoneNumber(phoneAuthCredential);
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                // s is a token unique for this otp verification
               // Toast.makeText(getApplicationContext(), "Automatically Retrieved...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                verification_id=s;
                token1 =forceResendingToken;
                Toast.makeText(getApplicationContext(), "code has been sent!!!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(getApplicationContext(), "invalid number...", Toast.LENGTH_SHORT).show();
                    if(counter!=0&&counter<4) {
                        sendButton.setText("ReSend");
                        sendButton.setEnabled(true);
                    }
                      counter++;
                }
                if(e instanceof FirebaseTooManyRequestsException){
                    Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        };
    }


    public void resend(View view){
        resendVerificationCode(mobno,token1);
    }
    public void signIn(View view){
        String code=codetext.getText().toString();
        pcredential = pauth.getCredential(verification_id,code);
        verifyPhoneNumber(pcredential);
    }
    public void signoutnow(View view){
        fbauth.signOut();
        Toast.makeText(getApplicationContext(),"signed Out!!!",Toast.LENGTH_LONG).show();
        finish();
    }


    @Override
    public void onStart() {
        super.onStart();
        fbauth.addAuthStateListener(mauthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mauthListener != null) {
            Toast.makeText(getApplicationContext(),"Signed out!!",Toast.LENGTH_SHORT);
            fbauth.removeAuthStateListener(mauthListener);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
