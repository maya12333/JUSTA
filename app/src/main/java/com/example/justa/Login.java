package com.example.justa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private EditText etPasswordL;
    private EditText etPhoneL;

    private Button btLoginIn;

    private TextView tvToRegister;

    private FirebaseDatabase firebaseDatabase;

    private DatabaseReference databaseReference;

    private Intent go ;

    private SharedPreferences mPref;

    private User current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etPasswordL = findViewById(R.id.etPasswordL);
        etPhoneL = findViewById(R.id.etPhoneL);

        btLoginIn = findViewById(R.id.btLoginIn);

        tvToRegister = findViewById(R.id.tvToRegister);

        mPref = getPreferences(MODE_PRIVATE);

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();

        btLoginIn.setOnClickListener(this);
        tvToRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(view == btLoginIn)
        {
            String phone = etPhoneL.getText().toString();
            String password = etPasswordL.getText().toString();

            databaseReference=firebaseDatabase.getReference();

            databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.hasChild(phone))
                    {
                        current=snapshot.child(phone).getValue(User.class);

                        String passD = snapshot.child(phone).child("password").getValue(String.class);
                        String typeD = snapshot.child(phone).child("type").getValue(String.class);

                        if(passD.equals(password))
                        {
                            Toast.makeText(Login.this, "Successfully Login", Toast.LENGTH_LONG).show();

                            if(typeD.equals("volunteer"))
                            {
                                 go = new Intent(Login.this, Open_volunteer.class);
                            }

                            else
                            {
                                 go = new Intent(Login.this, Open_needy.class);
                            }

                            SharedPreferences.Editor perfEditor = mPref.edit();
                            perfEditor.putString("phone",current.getPhone());
                            perfEditor.putString("name",current.getUsername());
                            perfEditor.putString("password",current.getPassword());
                            perfEditor.commit();

                            go.putExtra("user", current);
                            startActivity(go);

                            finish();
                        }

                        else {
                            Toast.makeText(Login.this, "Wrong Password", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    Toast.makeText(Login.this, "Wrong Phone Number", Toast.LENGTH_LONG).show();
                }
            });
        }

        if(view == tvToRegister)
        {
            Intent go = new Intent(Login.this, Register.class);

            startActivity(go);

            finish();
        }
    }
}