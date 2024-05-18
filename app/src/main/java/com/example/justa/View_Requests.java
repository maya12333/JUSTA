package com.example.justa;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class View_Requests extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private ImageView ivBackVR;

    private CheckBox cbSort;

    private ArrayList<Request> arrayList;
    private DatabaseReference databaseReference;

    private RecyclerView recyclerView;

    private Adapter myAdapter;

    private ArrayList<Request> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);

        FirebaseApp.initializeApp(this);

        cbSort = findViewById(R.id.cbSort);
        cbSort.setOnCheckedChangeListener(this);

        ivBackVR = findViewById(R.id.ivBackVR);

        recyclerView = findViewById(R.id.rv);

        ivBackVR.setOnClickListener(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Requests");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    Request r = dataSnapshot.getValue(Request.class);

                    list.add(r);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                myAdapter = new Adapter(View_Requests.this, list);
                recyclerView.setAdapter(myAdapter);
            }
        });
    }

    @Override
    public void onClick(View view) {

        if(view == ivBackVR)
        {
            Intent go = new Intent(View_Requests.this, Open_volunteer.class);

            startActivity(go);

            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.search);

        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                txtSearch(s);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void txtSearch(String str)
    {
        arrayList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        Query q = databaseReference.child("Requests").orderByChild("address").startAt(str).endAt(str + "~");

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot currentSnap : snapshot.getChildren()) {

                    arrayList.add(currentSnap.getValue(Request.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myAdapter = new Adapter(this, arrayList);
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        if(b)
        {
            Collections.sort(list);

            myAdapter.notifyDataSetChanged();
        }
    }
}