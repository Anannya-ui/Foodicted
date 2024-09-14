package com.example.foodicted;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodicted.common.urls;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class User_register_verification extends AppCompatActivity {
    TextView tvmobileno,tvresendotp;
    EditText etinput1,etinput2,etinput3,etinput4,etinput5,etinput6;
    Button btnverify;
    String strotp,strname,strmobileno,stremailid,strusername,strpassword;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register_verification);
        getWindow().setStatusBarColor(ContextCompat.getColor(User_register_verification.this,R.color.white));
        getWindow().setNavigationBarColor(ContextCompat.getColor(User_register_verification.this,R.color.white));

        tvmobileno = findViewById(R.id.tvVerifyOTPmobileno);
        tvresendotp = findViewById(R.id.tvResendOTPverify);
        etinput1 = findViewById( R.id.etVerifyOTPInput1);
        etinput2 = findViewById( R.id.etVerifyOTPInput2);
        etinput3 = findViewById( R.id.etVerifyOTPInput3);
        etinput4 = findViewById( R.id.etVerifyOTPInput4);
        etinput5 = findViewById( R.id.etVerifyOTPInput5);
        etinput6 = findViewById( R.id.etVerifyOTPInput6);
        btnverify = findViewById(R.id.btnVerifyOTP);

        strotp = getIntent().getStringExtra("otp");
        strname = getIntent().getStringExtra("name");
        strmobileno = getIntent().getStringExtra("mobileno");
        stremailid = getIntent().getStringExtra("emailid");
        strusername = getIntent().getStringExtra("username");
        strpassword = getIntent().getStringExtra("password");

        tvmobileno.setText(strmobileno);

        btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etinput1.getText().toString().trim().isEmpty() ||
                        etinput2.getText().toString().trim().isEmpty() ||
                        etinput3.getText().toString().trim().isEmpty() ||
                        etinput4.getText().toString().trim().isEmpty() ||
                        etinput5.getText().toString().trim().isEmpty() ||
                        etinput6.getText().toString().trim().isEmpty()){
                    Toast.makeText(User_register_verification.this, "Enetr proper otp", Toast.LENGTH_SHORT).show();
                }

                String otpcode=etinput1.getText().toString()+etinput2.getText().toString()+etinput3.getText().toString()+etinput4.getText().toString()+etinput5.getText().toString()+etinput6.getText().toString();

                if (strotp != null){
                    progressDialog = new ProgressDialog(User_register_verification.this);
                    progressDialog.setTitle("verifying");
                    progressDialog.setMessage("please wait");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(strotp,otpcode);
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                                Userregister();
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(User_register_verification.this, "Verification fail", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });

                }
            }
        });

        tvresendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + strmobileno, 60, TimeUnit.SECONDS, User_register_verification.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                    }

                    @Override
                    public void onCodeSent(@NonNull String newotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(newotp, forceResendingToken);

                        strotp=newotp;

                    }
                });
            }
        });


        InputOtp();


    }

    private void Userregister() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("name",strname);
        params.put("mobileno",strmobileno);
        params.put("emailid",stremailid);
        params.put("username",strusername);
        params.put("password",strpassword);

        client.post(urls.userRegister,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String status = response.getString("success");
                    if (status.equals("1")) {
                        Intent i = new Intent(User_register_verification.this, loginscreenActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        Toast.makeText(User_register_verification.this,"Registration fail",Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(User_register_verification.this,"Server fail",Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void InputOtp() {
        etinput1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()){
                    etinput2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });etinput2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()){
                    etinput3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });etinput3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()){
                    etinput4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });etinput4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()){
                    etinput5.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });etinput5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()){
                    etinput6.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}