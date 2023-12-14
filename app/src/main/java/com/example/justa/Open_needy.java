package com.example.justa;

import androidx.annotation.NavigationRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class Open_needy extends AppCompatActivity implements View.OnClickListener {

    private TextView tvUsernameN;
    private TextView tvLogoutN;
    private Button btRequests;
    private Button btRecommendations;

    private SharedPreferences sp;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_needy);

        sp = getSharedPreferences("Login", MODE_PRIVATE);

        tvUsernameN = findViewById(R.id.tvUsernameN);
        tvUsernameN.setText(sp.getString("name", null));

        tvLogoutN = findViewById(R.id.tvLogoutN);

        btRequests = findViewById(R.id.btRequests);
        btRecommendations = findViewById(R.id.btRecommendations);

        tvLogoutN.setOnClickListener(this);
        btRequests.setOnClickListener(this);
        btRecommendations.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(view==tvLogoutN)
        {
            Intent go = new Intent(Open_needy.this, MainActivity.class);

            startActivity(go);

            finish();
        }

        if(view == btRequests)
        {
            Intent go = new Intent(Open_needy.this, Requests.class);

            startActivity(go);

            finish();
        }

        if(view == btRecommendations)
        {
            Intent go = new Intent(Open_needy.this, Recommendations.class);

            startActivity(go);

            finish();
        }
    }

}