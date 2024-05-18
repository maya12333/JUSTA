package com.example.justa;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

    private ImageView ivBackReg;

    private FirebaseDatabase firebaseDatabase;

    private DatabaseReference databaseReference;

    private String type;

    private Dialog dialog;

    private SharedPreferences mPref;

    private Intent go;

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

        ivBackReg = findViewById(R.id.ivBackReg);

        mPref = getSharedPreferences("Login",MODE_PRIVATE);

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();

        btRegisterIn.setOnClickListener(this);

        rgRegister.setOnCheckedChangeListener(this);

        tvToLogin.setOnClickListener(this);

        ivBackReg.setOnClickListener(this);

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

            boolean[] flag = {false};

            databaseReference = firebaseDatabase.getReference("Users");

            User user = new User(username, phone,pass,type);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for(DataSnapshot currentSnap: snapshot.getChildren())
                    {
                        User u = currentSnap.getValue(User.class);

                        assert u != null;

                        if(phone.equals(u.getPhone()))
                        {
                            flag[0] = true;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    Toast.makeText(Register.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });

            databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {

                    if(!flag[0])
                    {
                        databaseReference.child(phone).setValue(user);

                        Toast.makeText(Register.this, "Added!", Toast.LENGTH_SHORT).show();

                        if(type.equals("volunteer"))
                        {
                            go = new Intent(Register.this, Open_volunteer.class);
                        }

                        else
                        {
                            go = new Intent(Register.this, Open_needy.class);
                        }

                        SharedPreferences.Editor perfEditor = mPref.edit();
                        perfEditor.putString("phone",user.getPhone());
                        perfEditor.putString("name",user.getUsername());
                        perfEditor.putString("password",user.getPassword());
                        perfEditor.putString("image", null);
                        perfEditor.putString("type", user.getType());
                        perfEditor.putInt("counter", 0);

                        perfEditor.commit();

                        go.putExtra("user", user);
                        startActivity(go);

                        finish();
                    }

                    else {

                        Toast.makeText(Register.this, "THIS PHONE NUMBER ALRADY EXIST!", Toast.LENGTH_SHORT).show();

                        etPhoneR.setText("");
                    }
                }
            });

            dialog.dismiss();
        }

        if(view == btRNo)
        {
            dialog.dismiss();
        }

        if(view == ivBackReg)
        {
            Intent go = new Intent(Register.this, MainActivity.class);

            startActivity(go);

            finish();
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