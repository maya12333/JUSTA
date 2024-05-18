package com.example.justa;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Profile extends AppCompatActivity implements View.OnClickListener {
    private TextView tvUsernameP;
    private TextView tvPhone;
    private TextView tvCounterV;

    private ImageView ivStar1;
    private ImageView ivStar2;
    private ImageView ivStar3;
    private ImageView ivStar4;
    private ImageView ivStar5;
    private ImageView ivBackP;
    private ImageView ivProfile;
    private ImageView ivExitCG;

    private Button btCamera;
    private Button btGallery;

    private ListView lvRecommendations;

    private SharedPreferences sp;

    private ArrayList<Recommendation> arrayList;

    private AdapterRecommend adapterRecommend;

    private FirebaseDatabase firebaseDatabase;

    private DatabaseReference databaseReference;

    private int counterS;

    private int GALLERY_REQ_CODE = 1000;

    private int CAMERA_REQ_CODE = 2000;

    private int ONE_MEGABYTE = 1024 * 1024;

    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sp = getSharedPreferences("Login", MODE_PRIVATE);

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();

        arrayList = new ArrayList<>();

        counterS = 0;

        getMyRecommendationsFromDB();

        tvUsernameP = findViewById(R.id.tvUsernameP);
        tvUsernameP.setText(sp.getString("name", null));

        tvPhone = findViewById(R.id.tvPhone);
        tvPhone.setText(sp.getString("phone", null));

        tvCounterV = findViewById(R.id.tvCounter);
        tvCounterV.setText(String.valueOf(sp.getInt("counter", 0)));

        ivStar1 = findViewById(R.id.ivStar1);
        ivStar2 = findViewById(R.id.ivStar2);
        ivStar3 = findViewById(R.id.ivStar3);
        ivStar4 = findViewById(R.id.ivStar4);
        ivStar5 = findViewById(R.id.ivStar5);
        ivBackP = findViewById(R.id.ivBackP);
        ivProfile = findViewById(R.id.ivProfile);

        lvRecommendations = findViewById(R.id.lvRecommendations);

        databaseReference = firebaseDatabase.getReference();

        if(sp.getString("image", null) == null)
        {
            ivProfile.setImageResource(R.drawable.ic_person);
        }

        else
        {
            uploadImage();
        }

        ivBackP.setOnClickListener(this);
        ivProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(view == ivBackP)
        {
            Intent go = new Intent(Profile.this, Open_volunteer.class);

            startActivity(go);

            finish();
        }

        if(view == ivProfile)
        {
            openDialog();
        }

        if(view == btGallery)
        {
            Intent iGallery = new Intent();
            iGallery.setAction(Intent.ACTION_GET_CONTENT);
            iGallery.setType("image/*");

            startActivityForResult(iGallery, GALLERY_REQ_CODE);
        }

        if(view == btCamera)
        {
            Intent iCamera = new Intent();
            iCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

            startActivityForResult(iCamera, CAMERA_REQ_CODE);
        }

        if(view == ivExitCG)
        {
            dialog.dismiss();
        }
    }

    public void changeStars()
    {
        if(counterS >= 2 && counterS < 4)
        {
            ivStar1.setImageResource(R.drawable.ic_star_lightblue);
        }

        if(counterS >= 4 && counterS < 6)
        {
            ivStar1.setImageResource(R.drawable.ic_star_lightblue);
            ivStar2.setImageResource(R.drawable.ic_star_lightblue);
        }

        if(counterS >= 6 && counterS < 8)
        {
            ivStar1.setImageResource(R.drawable.ic_star_lightblue);
            ivStar2.setImageResource(R.drawable.ic_star_lightblue);
            ivStar3.setImageResource(R.drawable.ic_star_lightblue);
        }

        if(counterS >= 8 && counterS < 10)
        {
            ivStar1.setImageResource(R.drawable.ic_star_lightblue);
            ivStar2.setImageResource(R.drawable.ic_star_lightblue);
            ivStar3.setImageResource(R.drawable.ic_star_lightblue);
            ivStar4.setImageResource(R.drawable.ic_star_lightblue);
        }

        if(counterS >= 10)
        {
            ivStar1.setImageResource(R.drawable.ic_star_lightblue);
            ivStar2.setImageResource(R.drawable.ic_star_lightblue);
            ivStar3.setImageResource(R.drawable.ic_star_lightblue);
            ivStar4.setImageResource(R.drawable.ic_star_lightblue);
            ivStar5.setImageResource(R.drawable.ic_star_lightblue);
        }
    }

    public void getMyRecommendationsFromDB()
    {
        databaseReference = firebaseDatabase.getReference().child("Recommendation").child(sp.getString("phone", null));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot currentSnap: snapshot.getChildren())
                {
                    arrayList.add(currentSnap.getValue(Recommendation.class));

                    counterS++;
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

                changeStars();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_CANCELED)
        {
            if (requestCode == GALLERY_REQ_CODE)
            {
                ivProfile.setImageURI(data.getData());

                String uri = data.getData().toString();

                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference picRef = storageReference.child((sp.getString("phone", null)));

                picRef.putFile(data.getData());

                databaseReference = firebaseDatabase.getReference();
                databaseReference.child("Users").child(sp.getString("phone", null)).child("uri").setValue(uri);

                SharedPreferences.Editor perfEditor = sp.edit();
                perfEditor.putString("image", String.valueOf(data.getData()));
                perfEditor.commit();
            }

            if(requestCode == CAMERA_REQ_CODE)
            {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                ivProfile.setImageBitmap(bitmap);

                Uri uri = BitmapToURI(bitmap);

                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference picRef = storageReference.child((sp.getString("phone", null)));

                picRef.putFile(uri);

                databaseReference = firebaseDatabase.getReference();
                databaseReference.child("Users").child(sp.getString("phone", null)).child("uri").setValue(uri.toString());

                SharedPreferences.Editor perfEditor = sp.edit();
                perfEditor.putString("image", String.valueOf(data.getData()));
                perfEditor.commit();
            }
        }
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

    public void openDialog()
    {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.camera_gallery_dialog);

        btCamera = dialog.findViewById(R.id.btCamera);
        btGallery = dialog.findViewById(R.id.btGallery);

        ivExitCG = dialog.findViewById(R.id.ivExitCG);

        btCamera.setOnClickListener(this);
        btGallery.setOnClickListener(this);

        ivExitCG.setOnClickListener(this);

        dialog.show();
    }

    public Uri BitmapToURI(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return Uri.parse(MediaStore.Images.Media.insertImage(this.getContentResolver(),image,"Title",null));
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
            Intent go = new Intent(Profile.this, MainActivity.class);

            startActivity(go);

            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}