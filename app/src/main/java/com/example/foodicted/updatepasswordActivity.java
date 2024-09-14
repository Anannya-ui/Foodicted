package com.example.foodicted;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
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

public class updatepasswordActivity extends AppCompatActivity {
    EditText etPassword,etconfirmpassword;
    Button btnvarify;
    String mobileno;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepassword);
        getWindow().setStatusBarColor(ContextCompat.getColor(updatepasswordActivity.this,R.color.white));
        getWindow().setNavigationBarColor(ContextCompat.getColor(updatepasswordActivity.this,R.color.white));

        etPassword = findViewById(R.id.etupdatenewpassword);
        etconfirmpassword=findViewById(R.id.etupdateconfirmpassword);
        btnvarify=findViewById(R.id.btnupdatepasswordverify);

        mobileno=getIntent().getStringExtra("mobileno");

        btnvarify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etPassword.getText().toString().isEmpty()) {
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

                }else if(!etPassword.getText().toString().equals(etconfirmpassword.getText().toString())){
                    etconfirmpassword.setError("Password do not match");
                }else {
                    progressDialog = new ProgressDialog(updatepasswordActivity.this);
                    progressDialog.setTitle("Updating");
                    progressDialog.setMessage("please wait");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + mobileno, 60, TimeUnit.SECONDS, updatepasswordActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            Toast.makeText(updatepasswordActivity.this, "Verification done", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            Toast.makeText(updatepasswordActivity.this, "vrification Fail", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCodeSent(@NonNull String otp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            super.onCodeSent(otp, forceResendingToken);
                            Intent i = new Intent(updatepasswordActivity.this,password_update_vaerification_Activity.class);
                            i.putExtra("otp",otp);
                            i.putExtra("mobileno",mobileno);
                            i.putExtra("password",etPassword.getText().toString());
                            startActivity(i);
                        }
                    });


                }
            }
        });


    }
}