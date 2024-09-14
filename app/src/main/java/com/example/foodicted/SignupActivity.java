package com.example.foodicted;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SignupActivity extends AppCompatActivity {
    EditText etName,etMobileno,etEmailId,etUsername,etPassword;
    Button btnsignin;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWindow().setNavigationBarColor(ContextCompat.getColor(SignupActivity.this,R.color.pink));
        getWindow().setStatusBarColor(ContextCompat.getColor(SignupActivity.this,R.color.pink));
        
        etName = findViewById(R.id.etRegisterName);
        etMobileno = findViewById(R.id.etRegisterMobileNo);
        etEmailId = findViewById(R.id.etRegisterEmailId);
        etUsername = findViewById(R.id.etRegisterUsername);
        etPassword = findViewById(R.id.etRegisterPassword);
        btnsignin = findViewById(R.id.btnsignin);
        
        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etName.getText().toString().isEmpty()) {
                    etName.setError("Please enter Username");
                } else if (etMobileno.getText().toString().isEmpty()) {
                    etMobileno.setError("Enetr the Mobile Number");
                } else if (etMobileno.getText().toString().length() != 10) {
                    etMobileno.setError("Enter  the proper Mobile Number");
                } else if (!etEmailId.getText().toString().contains("@") && !etEmailId.getText().toString().contains(".com")) {
                    etEmailId.setError("enter Proper Emaiil Id");
                } else if (etUsername.getText().toString().isEmpty()) {
                    etUsername.setError("Please enter username");
                } else if (etUsername.getText().toString().length() < 8) {
                    etUsername.setError("username must be atlest 8 character");
                } else if (!etUsername.getText().toString().matches(".*[A-Z].*")) {
                    etUsername.setError("Please enter atlest One upper case letter");
                } else if (!etUsername.getText().toString().matches(".*[0-9].*")) {
                    etUsername.setError("Please enter atlest One number");
                } else if (!etUsername.getText().toString().matches(".*[!,@,#,$,%,&,].*")) {
                    etUsername.setError("Please enter atlest One special symbol");
                } else if (etPassword.getText().toString().isEmpty()) {
                    etPassword.setError("Please enter username");
                } else if (etPassword.getText().toString().length() < 8) {
                    etPassword.setError("username must be atlest 8  character");
                } else if (!etPassword.getText().toString().matches(".*[A-Z].*")) {
                    etPassword.setError("Please enter atlest One upper case letter");
                } else if (!etPassword.getText().toString().matches(".*[a-z].*")) {
                    etPassword.setError("Please enter atlest One lower case letter");
                } else if (!etPassword.getText().toString().matches(".*[0-9].*")) {
                    etPassword.setError("Please enter atlest One number");
                } else if (!etPassword.getText().toString().matches(".*[!,@,#,$,%,&,].*")) {
                    etPassword.setError("Please enter atlest One special symbol");

                }else {
                    progressDialog = new ProgressDialog(SignupActivity.this);
                    progressDialog.setTitle("Loading");
                    progressDialog.setMessage("Please Wait");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + etMobileno.getText().toString(),
                            60, TimeUnit.SECONDS, SignupActivity.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            Toast.makeText(SignupActivity.this, "Verification Done", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            Toast.makeText(SignupActivity.this, "Verification Fail", Toast.LENGTH_SHORT).show();

                        }

                                @Override
                                public void onCodeSent(@NonNull String otp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    super.onCodeSent(otp, forceResendingToken);
                                    Intent i = new Intent(SignupActivity.this,User_register_verification.class);
                                    i.putExtra("otp",otp);
                                    i.putExtra("name",etName.getText().toString());
                                    i.putExtra("mobileno",etMobileno.getText().toString());
                                    i.putExtra("emailid",etEmailId.getText().toString());
                                    i.putExtra("username",etUsername.getText().toString());
                                    i.putExtra("password",etPassword.getText().toString());
                                    startActivity(i);



                                }
                            });



                }
            }
        });
        
    }
}