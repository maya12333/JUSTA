package com.example.justa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class View_Requests extends AppCompatActivity implements View.OnClickListener {

    private Button btPlace;

    private ListView lvViewR;

    private ImageView ivBackVR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);

        btPlace = findViewById(R.id.btPlace);

        lvViewR = findViewById(R.id.lvViewR);

        ivBackVR = findViewById(R.id.ivBackVR);

        btPlace.setOnClickListener(this);
        ivBackVR.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(view == btPlace)
        {

        }

        if(view == ivBackVR)
        {
            Intent go = new Intent(View_Requests.this, Open_volunteer.class);

            startActivity(go);

            finish();
        }

    }
}