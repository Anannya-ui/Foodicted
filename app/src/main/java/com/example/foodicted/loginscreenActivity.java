package com.example.foodicted;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.HardwarePropertiesManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodicted.common.urls;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class loginscreenActivity extends AppCompatActivity {
    EditText etusername,etpassword;
    CheckBox chshowpassword;
    TextView tvforgottenpassword;
    Button btnlogin,btnsignup;
    AppCompatButton acbgooglelogin;
    ProgressDialog progressDialog;

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginscreen);
        getWindow().setStatusBarColor(ContextCompat.getColor(loginscreenActivity.this,R.color.white));
        getWindow().setNavigationBarColor(ContextCompat.getColor(loginscreenActivity.this,R.color.white));

        etusername = findViewById(R.id.etLoginUsername);
        etpassword = findViewById(R.id.etLoginPassword);
        chshowpassword = findViewById(R.id.cbLoginShowHidePassword);
        tvforgottenpassword= findViewById(R.id.tvloginforgetpassword);
        btnlogin = findViewById(R.id.btnLoginlogin);
        btnsignup = findViewById(R.id.btnsignup);
        acbgooglelogin = findViewById(R.id.btnsigninwithgoogle);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(loginscreenActivity.this,googleSignInOptions);

        acbgooglelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Signin();
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etusername.getText().toString().isEmpty()){
                    etusername.setError("enter the username");
                }else if (etpassword.getText().toString().isEmpty()){
                    etpassword.setError("enter the password");
                }else {
                    progressDialog = new ProgressDialog(loginscreenActivity.this);
                    progressDialog.setTitle("LOG IN");
                    progressDialog.setMessage("please wait for few second");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    Loginuser();
                }
            }
        });

        chshowpassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    etpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    etpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        tvforgottenpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(loginscreenActivity.this,Checkmobileno_forpassword_updateActivity.class);
                startActivity(i);

            }
        });
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(loginscreenActivity.this,SignupActivity.class);
                startActivity(i);
            }
        });


    }

    private void Signin() {
        Intent i = googleSignInClient.getSignInIntent();
        startActivityForResult(i,999);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 999){

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                Intent intent = new Intent(loginscreenActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();


            } catch (ApiException e) {
                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                throw new RuntimeException(e);
            }

        }
    }

    private void Loginuser() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("username",etusername.getText().toString());
        params.put("password",etpassword.getText().toString());

        client.post(urls.userloginn,params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String status = response.getString("success");
                    if (status.equals("1")){
                        progressDialog.dismiss();
                        Intent i = new Intent(loginscreenActivity.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(loginscreenActivity.this, "Unable to login Please Sign up", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
                Toast.makeText(loginscreenActivity.this, "server Error", Toast.LENGTH_SHORT).show();

            }
        });


    }
}