package com.example.justa;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);

        sp = getSharedPreferences("Login", MODE_PRIVATE);

        ivAddRecommend = findViewById(R.id.ivAddRecommend);
        ivBackRec = findViewById(R.id.ivBackRec);

        lvAddRecommend = findViewById(R.id.lvAddRecommend);

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();

        getMyRecommendationsFromDB();

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

        if(!check(name, phoneV, text))
        {
            return;
        }

        databaseReference = firebaseDatabase.getReference("Recommendation");

        recommendation = new Recommendation(name, phoneV, text, sp.getString("phone", null));

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){

                databaseReference.child(phoneV).push().setValue(recommendation);
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

    public void getMyRecommendationsFromDB()
    {
        arrayList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Recommendation");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot currentSnap: snapshot.getChildren()) {

                    for (DataSnapshot keys: currentSnap.getChildren()) {

                        Recommendation u = keys.getValue(Recommendation.class);

                        if (u.getPhoneN().equals(sp.getString("phone", null))) {
                           arrayList.add(keys.getValue(Recommendation.class));
                        }
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
                adapterRecommend = new AdapterRecommend(Recommendations.this, 0, 0 , arrayList);

                lvAddRecommend.setAdapter(adapterRecommend);
            }
        });
    }

    public boolean check(String name, String phone, String text)
    {
        if(name.length() == 0)
        {
            Toast.makeText(Recommendations.this, "ENTER NAME", Toast.LENGTH_LONG).show();

            return false;
        }

        if(phone.length() == 0)
        {
            Toast.makeText(Recommendations.this, "ENTER PHONE NUMBER", Toast.LENGTH_LONG).show();

            return false;
        }

        if(text.length() == 0)
        {
            Toast.makeText(Recommendations.this, "ENTER Text", Toast.LENGTH_LONG).show();

            return false;
        }

        if(name.length() < 2 || name.length() > 12)
        {
            Toast.makeText(Recommendations.this, "USERNAME BETWEEN 2 AND 15 LETTERS", Toast.LENGTH_LONG).show();

            etNameV.setText("");

            return false;
        }

        if(phone.length() < 10 || phone.length() > 10)
        {
            Toast.makeText(Recommendations.this, "WRONG PHONE NUMBER", Toast.LENGTH_LONG).show();

            etPhoneV.setText("");

            return false;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.menu_police)
        {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + 100));
            startActivity(callIntent);
        }

        if(item.getItemId() == R.id.menu_mada)
        {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + 101));
            startActivity(callIntent);
        }

        if(item.getItemId() == R.id.menu_fire)
        {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + 102));
            startActivity(callIntent);
        }

        if(item.getItemId() == R.id.logout)
        {
            Intent go = new Intent(Recommendations.this, MainActivity.class);

            startActivity(go);

            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}