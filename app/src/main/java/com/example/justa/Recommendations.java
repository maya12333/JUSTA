package com.example.justa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Recommendations extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivAddRecommend;
    private ImageView ivBackRec;
    private ImageView ivExitReco;

    private ListView lvAddRecommend;

    private EditText etNameV;
    private EditText etPhoneV;
    private EditText etTextReco;

    private Button btSendAReco;

    private Dialog dialog;

    private ArrayList<Recommendation> arrayList;

    private AdapterRecommend adapterRecommend;

    private Recommendation recommendation;

    private FirebaseDatabase firebaseDatabase;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);

        ivAddRecommend = findViewById(R.id.ivAddRecommend);
        ivBackRec = findViewById(R.id.ivBackRec);

        lvAddRecommend = findViewById(R.id.lvAddRecommend);

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();

        arrayList = new ArrayList<>();

        ivAddRecommend.setOnClickListener(this);
        ivBackRec.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(view == ivAddRecommend)
        {
            openDialog();
        }

        if(view == ivBackRec)
        {
            Intent go = new Intent(Recommendations.this, Open_needy.class);

            startActivity(go);

            finish();
        }

        if(view == ivExitReco)
        {
            dialog.dismiss();
        }

        if(view == btSendAReco)
        {
            addRecommend();
        }
    }

    public void openDialog()
    {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_recommendation_dialog);

        ivExitReco = dialog.findViewById(R.id.ivExitReco);
        btSendAReco = dialog.findViewById(R.id.btSendAReco);

        ivExitReco.setOnClickListener(this);
        btSendAReco.setOnClickListener(this);

        dialog.show();
    }

    public void addRecommend()
    {
        etNameV = dialog.findViewById(R.id.etNameV);
        etPhoneV = dialog.findViewById(R.id.etPhoneV);
        etTextReco = dialog.findViewById(R.id.etTextReco);

        String name = etNameV.getText().toString();
        String phone = etPhoneV.getText().toString();
        String text = etTextReco.getText().toString();

        databaseReference = firebaseDatabase.getReference("Recommendation");

        recommendation = new Recommendation(name, phone, text);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                databaseReference.child(phone).setValue(recommendation);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error){

            }
        });

        arrayList.add(recommendation);

        adapterRecommend = new AdapterRecommend(this, 0, 0 , arrayList);

        lvAddRecommend.setAdapter(adapterRecommend);

        dialog.dismiss();
    }
}