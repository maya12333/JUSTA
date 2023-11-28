package com.example.justa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btRegister;
    private Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btRegister = findViewById(R.id.btRegister);
        btLogin = findViewById(R.id.btLogin);

        btRegister.setOnClickListener(this);
        btLogin.setOnClickListener(this);
    }

    public void onClick(View view) {

        if(view == btRegister)
        {
            Intent go = new Intent(MainActivity.this, Register.class);

            startActivity(go);
        }

        if(view == btLogin)
        {
            Intent go = new Intent(MainActivity.this, Login.class);

            startActivity(go);
        }
    }
}