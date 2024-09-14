package com.example.foodicted;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodicted.common.urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Checkmobileno_forpassword_updateActivity extends AppCompatActivity {

    EditText etmobileno;
    Button btnverify;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkmobileno_forpassword_update);
        getWindow().setStatusBarColor(ContextCompat.getColor(Checkmobileno_forpassword_updateActivity.this,R.color.white));
        getWindow().setNavigationBarColor(ContextCompat.getColor(Checkmobileno_forpassword_updateActivity.this,R.color.white));

        etmobileno = findViewById(R.id.etconfirmregistermobileno);
        btnverify= findViewById(R.id.confirmregisterVerify);

        btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etmobileno.getText().toString().isEmpty()){
                    etmobileno.setError("enter mobile no");
                }else{
                    progressDialog= new ProgressDialog(Checkmobileno_forpassword_updateActivity.this);
                    progressDialog.setTitle("Verifying");
                    progressDialog.setMessage("please wait");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    Checkmobileno();
                }
            }
        });

    }

    private void Checkmobileno() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("mobileno",etmobileno.getText().toString());

        client.post(urls.usernumbercheckforpasswordupdate,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String status = response.getString("success");
                    if (status.equals("1")){
                        progressDialog.dismiss();
                     Intent i = new Intent(Checkmobileno_forpassword_updateActivity.this,updatepasswordActivity.class);
                     i.putExtra("mobileno",etmobileno.getText().toString());
                     startActivity(i);
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(Checkmobileno_forpassword_updateActivity.this, "Mobile Number is not Valid", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
                Toast.makeText(Checkmobileno_forpassword_updateActivity.this, "Mobile Number is not Valid", Toast.LENGTH_SHORT).show();

            }
        });
    }
}