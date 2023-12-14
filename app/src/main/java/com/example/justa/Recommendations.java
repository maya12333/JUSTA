package com.example.justa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    private SharedPreferences sp;

    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);

        sp = getSharedPreferences("Login", MODE_PRIVATE);

        pb=findViewById(R.id.pb);

        ivAddRecommend = findViewById(R.id.ivAddRecommend);
        ivBackRec = findViewById(R.id.ivBackRec);

        lvAddRecommend = findViewById(R.id.lvAddRecommend);

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();

        getRecommendationsFromDB();

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
        String phoneV = etPhoneV.getText().toString();
        String text = etTextReco.getText().toString();

        databaseReference = firebaseDatabase.getReference("Recommendation");

        recommendation = new Recommendation(name, phoneV, text, sp.getString("phone", null));

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){

                databaseReference.child(sp.getString("phone", null)).push().child(phoneV).setValue(recommendation);
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

    public void getRecommendationsFromDB()
    {
        arrayList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Recommendation").child(sp.getString("phone", null));

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot currentSnap: snapshot.getChildren())
                {
                    arrayList.add(currentSnap.getValue(Recommendation.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                pb.setVisibility(View.INVISIBLE);
                adapterRecommend = new AdapterRecommend(Recommendations.this, 0, 0 , arrayList);

                lvAddRecommend.setAdapter(adapterRecommend);

            }
        });
    }
}