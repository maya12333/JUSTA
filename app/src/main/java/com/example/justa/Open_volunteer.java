package com.example.justa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Open_volunteer extends AppCompatActivity implements View.OnClickListener {

    private TextView tvUsernameV;
    private TextView tvLogoutV;

    private Button btViewRequests;
    private Button btProfile;
    private Button btContact;

    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_volunteer);

        sp = getSharedPreferences("Login", MODE_PRIVATE);

        tvUsernameV = findViewById(R.id.tvUsernameV);
        tvUsernameV.setText(sp.getString("name", null));

        tvLogoutV = findViewById(R.id.tvLogoutV);

        btViewRequests = findViewById(R.id.btViewRequests);
        btProfile = findViewById(R.id.btProfile);
        btContact = findViewById(R.id.btContact);

        tvLogoutV.setOnClickListener(this);
        btViewRequests.setOnClickListener(this);
        btProfile.setOnClickListener(this);
        btContact.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(view == btViewRequests)
        {
            Intent go = new Intent(Open_volunteer.this, View_Requests.class);

            startActivity(go);

            finish();
        }

        if(view == btProfile)
        {
            Intent go = new Intent(Open_volunteer.this, Profile.class);

            startActivity(go);

            finish();
        }

        if(view == btContact)
        {
            Intent go = new Intent(Open_volunteer.this, Contact.class);

            startActivity(go);

            finish();
        }

        if(view == tvLogoutV)
        {
            Intent go = new Intent(Open_volunteer.this, MainActivity.class);

            startActivity(go);

            finish();
        }
    }
}