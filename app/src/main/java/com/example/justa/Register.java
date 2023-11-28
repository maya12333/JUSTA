package com.example.justa;

import static com.example.justa.R.id.rbNeed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private TextView tvToLogin;
    private TextView tvNameR;
    private TextView tvPasswordR;
    private TextView tvPhoneR;
    private TextView tvTypeR;

    private Button btRegisterIn;
    private Button btRNo;
    private Button btRYes;

    private RadioGroup rgRegister;

    private RadioButton rbVolunteer;
    private RadioButton rbNeed;

    private EditText etUsernameR;
    private EditText etPasswordR;
    private EditText etPhoneR;

    private FirebaseDatabase firebaseDatabase;

    private DatabaseReference databaseReference;

    private String type;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tvToLogin = findViewById(R.id.tvToLogin);

        btRegisterIn = findViewById(R.id.btRegisterIn);

        rgRegister = findViewById(R.id.rgRegister);

        rbNeed = findViewById(R.id.rbNeed);
        rbVolunteer = findViewById(R.id.rbVolunteer);

        etUsernameR = findViewById(R.id.etUsernameR);
        etPasswordR = findViewById(R.id.etPasswordR);
        etPhoneR = findViewById(R.id.etPhoneR);

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();

        btRegisterIn.setOnClickListener(this);

        rgRegister.setOnCheckedChangeListener(this);

        tvToLogin.setOnClickListener(this);
        type = "needy";

    }


    @Override
    public void onClick(View view) {
        if(view == btRegisterIn)
        {
            openDialog();
        }

        if (view == tvToLogin)
        {
            Intent go = new Intent(Register.this, Login.class);

            startActivity(go);

            finish();
        }

        if(view == btRYes)
        {
            String username = etUsernameR.getText().toString();
            String pass = etPasswordR.getText().toString();
            String phone = etPhoneR.getText().toString();

            databaseReference = firebaseDatabase.getReference("Users");

            User user = new User(username, phone,pass,type);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    databaseReference.child(phone).setValue(user);

                    Toast.makeText(Register.this, "Added!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    Toast.makeText(Register.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if(view == btRNo)
        {
            dialog.dismiss();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedButtonId) {

        if (checkedButtonId == rbNeed.getId())
        {
            type = "needy";
        }

        else {
            type = "volunteer";
        }
    }

    public void openDialog()
    {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.register_dialog);

        tvNameR = dialog.findViewById(R.id.tvNameR);
        tvPasswordR = dialog.findViewById(R.id.tvPasswordR);
        tvPhoneR = dialog.findViewById(R.id.tvPhoneR);
        tvTypeR = dialog.findViewById(R.id.tvTypeR);

        btRYes = dialog.findViewById(R.id.btRYes);
        btRNo = dialog.findViewById(R.id.btRNo);

        btRYes.setOnClickListener(this);
        btRNo.setOnClickListener(this);

        String username = etUsernameR.getText().toString();
        String pass = etPasswordR.getText().toString();
        String phone = etPhoneR.getText().toString();
        
        tvNameR.setText(username);
        tvPasswordR.setText(pass);
        tvPhoneR.setText(phone);
        
        if(type == "volunteer")
        {
            tvTypeR.setText("נותן ג'סטה");
        }

        else
        {
            tvTypeR.setText("צריך ג'סטה");
        }

        dialog.show();
    }
}