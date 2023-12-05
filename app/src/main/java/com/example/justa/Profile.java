package com.example.justa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.util.ArrayList;

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

    private ArrayList<Recommendation> arrayList;

    private AdapterRecommend adapterRecommend;

    private FirebaseDatabase firebaseDatabase;

    private DatabaseReference databaseReference;

    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sp = getSharedPreferences("Login", MODE_PRIVATE);

        getMyRecommendationsFromDB();

        tvUsernameP = findViewById(R.id.tvUsernameP);
        tvUsernameP.setText(sp.getString("name", null));

        tvPhone = findViewById(R.id.tvPhone);
        tvPhone.setText(sp.getString("phone", null));

        tvCounter = findViewById(R.id.tvCounter);
        //tvCounter.setText(arrayList.size());

        tvUpdateP = findViewById(R.id.tvUpdateP);

        ivStar1 = findViewById(R.id.ivStar1);
        ivStar2 = findViewById(R.id.ivStar2);
        ivStar3 = findViewById(R.id.ivStar3);
        ivStar4 = findViewById(R.id.ivStar4);
        ivStar5 = findViewById(R.id.ivStar5);
        ivBackP = findViewById(R.id.ivBackP);

        lvRecommendations = findViewById(R.id.lvRecommendations);

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();

        counter = 0;


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

    public void getMyRecommendationsFromDB()
    {
        arrayList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Recommendation");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot currentSnap: snapshot.getChildren())
                {
                    if(currentSnap.getValue(Recommendation.class).getPhoneV() == sp.getString("phone", null))
                    {
                        arrayList.add(currentSnap.getValue(Recommendation.class));
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

                adapterRecommend = new AdapterRecommend(Profile.this, 0, 0 , arrayList);

                lvRecommendations.setAdapter(adapterRecommend);
            }
        });
    }
}