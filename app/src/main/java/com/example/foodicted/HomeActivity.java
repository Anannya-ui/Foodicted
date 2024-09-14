package com.example.foodicted;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.pink)));
        getWindow().setStatusBarColor(ContextCompat.getColor(HomeActivity.this,R.color.pink));

        bottomNavigationView = findViewById(R.id.bottomnevigationhome);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.homebottomnevigqationHome);

    }
    homeFragment homeFragment = new homeFragment();
    ReorderFragment reorderFragmentn = new ReorderFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.homebottomnevigqationHome){
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayouthome,homeFragment).commit();
        } else if (item.getItemId()==R.id.homebottomnevigqationreorder){
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayouthome,reorderFragmentn).commit();
        }
        return true;
    }
}