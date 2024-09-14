package com.example.foodicted;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodicted.common.NetworkChangeListner;

public class Splashscreen extends AppCompatActivity {
    NetworkChangeListner networkChangeListener = new NetworkChangeListner();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        getWindow().setStatusBarColor(ContextCompat.getColor(Splashscreen.this,R.color.white));
        getWindow().setNavigationBarColor(ContextCompat.getColor(Splashscreen.this,R.color.white));
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Splashscreen.this,loginscreenActivity.class);
                startActivity(i);

            }
        },5000);

    }
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeListener);
    }
}