package com.example.justa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener{

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

    private boolean b;

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

            dialog.dismiss();
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

        else if(checkedButtonId == rbVolunteer.getId()){
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

        if(!check(username, phone, pass))
        {
            return;
        }

        if(!existPhone(phone))
        {
            return;
        }

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

    public boolean existPhone(String phone)
    {
        b = true;

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {

               for(DataSnapshot currentSnap: snapshot.getChildren())
               {
                   if(currentSnap.getValue(User.class).getPhone() == phone)
                   {
                       Toast.makeText(Register.this, "This Phone Already Exist", Toast.LENGTH_LONG).show();

                       b = false;
                   }
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

       databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
           @Override
           public void onComplete(@NonNull Task<DataSnapshot> task) {

               Toast.makeText(Register.this, "This Phone Number Already Exists", Toast.LENGTH_LONG).show();

               dialog.dismiss();

               b = false;

               etPhoneR.setText("");
           }
       });

       return b;
    }

    public boolean check(String name, String phone, String password)
    {
        if(name.length() == 0)
        {
            Toast.makeText(Register.this, "ENTER NAME", Toast.LENGTH_LONG).show();

            return false;
        }

        if(password.length() == 0)
        {
            Toast.makeText(Register.this, "ENTER PASSWORD", Toast.LENGTH_LONG).show();

            return false;
        }

        if(phone.length() == 0)
        {
            Toast.makeText(Register.this, "ENTER PHONE NUMBER", Toast.LENGTH_LONG).show();

            return false;
        }

        if(name.length() < 2 || name.length() > 12)
        {
            Toast.makeText(Register.this, "USERNAME BETWEEN 2 AND 15 LETTERS", Toast.LENGTH_LONG).show();

            etUsernameR.setText("");

            return false;
        }

        if(password.length() < 4 || password.length() > 10)
        {
            Toast.makeText(Register.this, "PASSWORD BETWEEN 4 AND 10 NUMBERS", Toast.LENGTH_LONG).show();

            etPasswordR.setText("");

            return false;
        }

        if(phone.length() < 10 || phone.length() > 10)
        {
            Toast.makeText(Register.this, "WRONG PHONE NUMBER", Toast.LENGTH_LONG).show();

            etPhoneR.setText("");

            return false;
        }

        return true;
    }
}