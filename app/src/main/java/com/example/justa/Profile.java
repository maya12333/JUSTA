package com.example.justa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    private TextView tvUsernameP;
    private TextView tvPhone;
    private TextView tvCounter;
    private TextView tvUpdateP;

    private ImageView ivStar1;
    private ImageView ivStar2;
    private ImageView ivStar3;
    private ImageView ivStar4;
    private ImageView ivStar5;
    private ImageView ivBackP;

    private ListView lvRecommendations;

    private SharedPreferences sp;

    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sp = getSharedPreferences("Login", MODE_PRIVATE);

        tvUsernameP = findViewById(R.id.tvUsernameP);
        tvUsernameP.setText(sp.getString("name", null));

        tvPhone = findViewById(R.id.tvPhone);
        tvPhone.setText(sp.getString("phone", null));

        tvCounter = findViewById(R.id.tvCounter);
        tvUpdateP = findViewById(R.id.tvUpdateP);

        ivStar1 = findViewById(R.id.ivStar1);
        ivStar2 = findViewById(R.id.ivStar2);
        ivStar3 = findViewById(R.id.ivStar3);
        ivStar4 = findViewById(R.id.ivStar4);
        ivStar5 = findViewById(R.id.ivStar5);
        ivBackP = findViewById(R.id.ivBackP);

        lvRecommendations = findViewById(R.id.lvRecommendations);

        counter = 1;
        changeStars();

        ivBackP.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(view == ivBackP)
        {
            Intent go = new Intent(Profile.this, Open_volunteer.class);

            startActivity(go);

            finish();
        }
    }

    public void changeStars()
    {
        if(counter == 1)
        {
            ivStar1.setImageResource(R.drawable.ic_star_lightblue);
        }
    }
}