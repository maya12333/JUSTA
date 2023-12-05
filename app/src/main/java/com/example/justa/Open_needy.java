package com.example.justa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class Open_needy extends AppCompatActivity implements View.OnClickListener {

    private TextView tvUsernameN;
    private TextView tvLogoutN;
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

        tvLogoutN = findViewById(R.id.tvLogoutN);

        btRequests = findViewById(R.id.btRequests);
        btRecommendations = findViewById(R.id.btRecommendations);

        tvLogoutN.setOnClickListener(this);
        btRequests.setOnClickListener(this);
        btRecommendations.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.menu_police)
        {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+100));//change the number.
            startActivity(callIntent);

        }



        return super.onOptionsItemSelected(item);
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