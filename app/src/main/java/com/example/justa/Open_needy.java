package com.example.justa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Open_needy extends AppCompatActivity implements View.OnClickListener {

    private TextView tvUsernameN;
    private Button btRequests;
    private Button btRecommendations;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_needy);

        sp = getSharedPreferences("Login", MODE_PRIVATE);

        tvUsernameN = findViewById(R.id.tvUsernameN);
        tvUsernameN.setText(sp.getString("name", null));

        btRequests = findViewById(R.id.btRequests);
        btRecommendations = findViewById(R.id.btRecommendations);

        btRequests.setOnClickListener(this);
        btRecommendations.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.menu_police)
        {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + 100));
            startActivity(callIntent);
        }

        if(item.getItemId() == R.id.menu_mada)
        {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + 101));
            startActivity(callIntent);
        }

        if(item.getItemId() == R.id.menu_fire)
        {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + 102));
            startActivity(callIntent);
        }

        if(item.getItemId() == R.id.logout)
        {
            Intent go = new Intent(Open_needy.this, MainActivity.class);

            startActivity(go);

            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}