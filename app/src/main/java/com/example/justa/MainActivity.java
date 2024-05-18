package com.example.justa;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btRegister;
    private Button btLogin;

    private Animation animOffset;

    private ImageView ivOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btRegister = findViewById(R.id.btRegister);
        btLogin = findViewById(R.id.btLogin);

        ivOpen = findViewById(R.id.ivOpen);
        animOffset= AnimationUtils.loadAnimation(this,R.anim.offset_anim);

        btRegister.setOnClickListener(this);
        btLogin.setOnClickListener(this);
    }

    public void onClick(View view) {

        if(view == btRegister)
        {
            ivOpen.startAnimation(animOffset);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent go = new Intent(MainActivity.this, Register.class);

                    startActivity(go);
                }
            },4000);
        }

        if(view == btLogin)
        {
            ivOpen.startAnimation(animOffset);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent go = new Intent(MainActivity.this, Login.class);

                    startActivity(go);
                }
            },4000);
        }
    }
}