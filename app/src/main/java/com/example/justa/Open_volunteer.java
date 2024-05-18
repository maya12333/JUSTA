package com.example.justa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Open_volunteer extends AppCompatActivity implements View.OnClickListener {

    private TextView tvUsernameV;
    private Button btViewRequests;
    private Button btProfile;
    private Button btContact;

    private SharedPreferences sp;

    private  int ONE_MEGABYTE = 1024 * 1024;

    private ImageView ivProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_volunteer);

        sp = getSharedPreferences("Login", MODE_PRIVATE);

        tvUsernameV = findViewById(R.id.tvUsernameV);
        tvUsernameV.setText(sp.getString("name", null));

        btViewRequests = findViewById(R.id.btViewRequests);
        btProfile = findViewById(R.id.btProfile);
        btContact = findViewById(R.id.btContact);

        ivProfile = findViewById(R.id.ivImage);

        uploadImage();

        btViewRequests.setOnClickListener(this);
        btProfile.setOnClickListener(this);
        btContact.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(view == btViewRequests)
        {
            Intent go = new Intent(Open_volunteer.this, View_Requests.class);

            startActivity(go);

            finish();
        }

        if(view == btProfile)
        {
            Intent go = new Intent(Open_volunteer.this, Profile.class);

            startActivity(go);

            finish();
        }

        if(view == btContact)
        {
            Intent go = new Intent(Open_volunteer.this, Contact.class);

            startActivity(go);

            finish();
        }
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
            Intent go = new Intent(Open_volunteer.this, MainActivity.class);

            startActivity(go);

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void uploadImage()
    {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference picRef = storageReference.child((sp.getString("phone", null)));

        picRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                ivProfile.setImageBitmap(BitmapFactory.decodeByteArray(bytes,0, bytes.length));
            }
        });
    }
}