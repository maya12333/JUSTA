package com.example.justa;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class Requests extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivBackReq;
    private ImageView ivAddRequest;
    private ImageView ivExitReq;
    private ImageView ivLocation;

    private ListView lvAddRequest;

    private Dialog dialog;

    private EditText etTextReq;

    private Button btDate;
    private Button btSendReq;

    private TextView tvAddress;

    private String date;
    private String address;

    private ArrayList<Request> arrayList;

    private AdapterRequest adapterRequest;

    private FirebaseDatabase firebaseDatabase;

    private DatabaseReference databaseReference;

    private final static int ADDRESS_REQ_CODE = 2000;
    private final static int ADDRESS_REQ_CODE_UPDATE = 1000;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();

        sp = getSharedPreferences("Login", MODE_PRIVATE);

        ivBackReq = findViewById(R.id.ivBackReq);
        ivAddRequest = findViewById(R.id.ivAddRequest);

        lvAddRequest = findViewById(R.id.lvAddRequest);

        arrayList = new ArrayList<>();

        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        date = dateFormat.format(d);

        address = "";

        getMyRequestsFromDB();

        ivBackReq.setOnClickListener(this);
        ivAddRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view == ivAddRequest)
        {
            openDialog();
        }

        if (view == ivBackReq)
        {
            Intent go = new Intent(Requests.this, Open_needy.class);

            startActivity(go);

            finish();
        }

        if (view == ivExitReq)
        {
            dialog.dismiss();
        }

        if (view == btDate)
        {
            Calendar c = Calendar.getInstance();

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.MyDatePickerStyle, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int yearD, int monthD, int dayD) {

                    date = dayD + "." + (monthD + 1) + "." + yearD;

                    btDate.setText(date);
                }
            }, year, month, day);

            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);

            datePickerDialog.show();
        }

        if (view == btSendReq)
        {
            addRequest();
        }

        if (view == ivLocation)
        {
            Intent go = new Intent(Requests.this, SearchPlaces.class);

            startActivityForResult(go, ADDRESS_REQ_CODE);
        }
    }

    public void openDialog() {

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_request_dialog);

        ivExitReq = dialog.findViewById(R.id.ivExitReq);
        ivLocation = dialog.findViewById(R.id.ivLocation);

        tvAddress = dialog.findViewById(R.id.tvAddress);

        btDate = dialog.findViewById(R.id.btDate);
        btSendReq = dialog.findViewById(R.id.btSendReq);

        btDate.setOnClickListener(this);
        btSendReq.setOnClickListener(this);

        ivExitReq.setOnClickListener(this);
        ivLocation.setOnClickListener(this);

        dialog.show();
    }

    public void addRequest()
    {
        etTextReq = dialog.findViewById(R.id.etTextReq);

        String text = etTextReq.getText().toString();

        if (!check(text))
        {
            return;
        }

        databaseReference = firebaseDatabase.getReference("Requests");

        Request request = new Request(sp.getString("phone", null), date, text, address, false);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String key = databaseReference.push().getKey();

                request.setUid(key);

                databaseReference.child(key).setValue(request);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        arrayList.add(request);

        adapterRequest = new AdapterRequest(this, 0, 0, arrayList);

        lvAddRequest.setAdapter(adapterRequest);

        dialog.dismiss();
    }

    public void getMyRequestsFromDB()
    {
        arrayList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Requests");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot currentSnap: snapshot.getChildren()) {

                    if(sp.getString("phone",null).equals(currentSnap.getValue(Request.class).getPhone()))
                    {
                        arrayList.add(currentSnap.getValue(Request.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task)
            {
                adapterRequest = new AdapterRequest(Requests.this, 0, 0 , arrayList);

                Collections.sort(arrayList);
                adapterRequest.notifyDataSetChanged();

                lvAddRequest.setAdapter(adapterRequest);
            }
        });
    }

    public boolean check(String text)
    {

        if (address.length() == 0) {
            Toast.makeText(Requests.this, "ENTER AN ADDRESS", Toast.LENGTH_LONG).show();

            return false;
        }

        if (text.length() == 0) {
            Toast.makeText(Requests.this, "ENTER TEXT", Toast.LENGTH_LONG).show();

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
            Intent go = new Intent(Requests.this, MainActivity.class);

            startActivity(go);

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            if(requestCode == ADDRESS_REQ_CODE)
            {
                String add = data.getStringExtra("address");

                tvAddress.setText(add);

                address = add;
            }

            if(requestCode==ADDRESS_REQ_CODE_UPDATE)
            {
                TextView tv = AdapterRequest.dialog.findViewById(R.id.tvAddressU);

                String add = data.getStringExtra("address");

                tv.setText(add);
            }
        }

    }
}