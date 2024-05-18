package com.example.justa;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class AdapterRequest extends ArrayAdapter<Request> implements View.OnClickListener {

    private Context context;
    private ArrayList<Request> arrayList;

    private SharedPreferences sp;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public static Dialog dialog;

    private ImageView ivExitReqU;
    private ImageView ivLocationU;

    private EditText etTextReqU;

    private Button btUpdateReq;
    private Button btDateU;

    private TextView tvAddressU;

    Request r;

    private final static int ADDRESS_REQ_CODE = 1000;

    public AdapterRequest(@NonNull Context context, int resource, int textViewResourceId, @NonNull ArrayList<Request> objects) {
        super(context, resource, textViewResourceId, objects);

        this.context = context;
        this.arrayList = objects;
        this.sp = context.getSharedPreferences("Login", MODE_PRIVATE);
        FirebaseApp.initializeApp(context);
        this.firebaseDatabase = FirebaseDatabase.getInstance();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater li = ((Activity) context).getLayoutInflater();

        View view = li.inflate(R.layout.request_listview,parent, false);

        TextView tvPhoneLvR = view.findViewById(R.id.tvPhoneLvR);
        TextView tvDateLvR = view.findViewById(R.id.tvDateLvR);
        TextView tvTextLvR = view.findViewById(R.id.tvTextLvR);
        TextView tvAddressLvR = view.findViewById(R.id.tvAddressLvR);
        TextView tvUpdateLvR = view.findViewById(R.id.tvUpdateLvR);

        CheckBox cb = view.findViewById(R.id.cbConfirm);
        ImageView iv = view.findViewById(R.id.ivDelete);

        r = arrayList.get(position);

        tvPhoneLvR.setText(r.getPhone());
        tvDateLvR.setText(r.getDate());
        tvTextLvR.setText(r.getText());
        tvAddressLvR.setText(r.getAddress());

        boolean taken = r.isTaken();

        System.out.println(taken);
        System.out.println(sp.getString("type",null));

        tvUpdateLvR.setVisibility(view.INVISIBLE);

        if(sp.getString("type",null) == "volunteer")
        {
            Toast.makeText(view.getContext(), "r", Toast.LENGTH_LONG).show();

            iv.setVisibility(view.INVISIBLE);
            tvUpdateLvR.setVisibility(view.INVISIBLE);
            cb.setVisibility(view.VISIBLE);

            if(taken)
            {
                cb.setVisibility(view.INVISIBLE);
            }
        }

        else
        {
            cb.setVisibility(view.INVISIBLE);
            iv.setVisibility(View.INVISIBLE);
            tvUpdateLvR.setVisibility(view.VISIBLE);

            if(taken)
            {
                iv.setVisibility(View.VISIBLE);
            }
        }

        tvUpdateLvR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(view.getContext(), "update", Toast.LENGTH_SHORT).show();

                openUpdateDialog();
            }
        });

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if(b)
                {
                    r.setTaken(true);
                }
            }
        });

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(view.getContext(),"delete", Toast.LENGTH_SHORT).show();

                databaseReference = firebaseDatabase.getReference("Requests").child(r.getUid());

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        databaseReference.setValue(null);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        arrayList.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }
        });

        return view;
    }

    public void openUpdateDialog()
    {
        dialog = new Dialog(this.getContext());
        dialog.setContentView(R.layout.update_request_dialog);

        ivExitReqU = dialog.findViewById(R.id.ivExitReqU);
        ivLocationU = dialog.findViewById(R.id.ivLocationU);

        tvAddressU = dialog.findViewById(R.id.tvAddressU);

        btDateU = dialog.findViewById(R.id.btDateU);
        btUpdateReq = dialog.findViewById(R.id.btUpdateReq);

        etTextReqU = dialog.findViewById(R.id.etTextReqU);

        retrieveData();

        btDateU.setOnClickListener(this);
        btUpdateReq.setOnClickListener(this);

        ivExitReqU.setOnClickListener(this);
        ivLocationU.setOnClickListener(this);

        dialog.show();
    }

    @Override
    public void onClick(View view) {

        if (view == btDateU)
        {
            Calendar c = Calendar.getInstance();

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), R.style.MyDatePickerStyle, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int yearD, int monthD, int dayD) {

                    String date = dayD + "." + (monthD + 1) + "." + yearD;

                    btDateU.setText(date);
                }
            }, year, month, day);

            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);

            datePickerDialog.show();
        }

        if (view == ivLocationU)
        {
            Intent go = new Intent(view.getContext(), SearchPlaces.class);

            ((Activity) context).startActivityForResult(go,ADDRESS_REQ_CODE);
        }

        if(view == ivExitReqU)
        {
            dialog.dismiss();
        }

        if(view == btUpdateReq)
        {
            databaseReference = firebaseDatabase.getReference("Requests").child(r.getUid());
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (!(btDateU.getText().toString()).equals(r.getDate()))
                    {
                        r.setDate(btDateU.getText().toString());
                    }

                    if(!(tvAddressU.getText()).equals(r.getAddress()))
                    {
                        r.setAddress(tvAddressU.getText().toString());
                    }

                    String text = etTextReqU.getText().toString();

                    if(!(etTextReqU.getText().toString()).equals(r.getText()))
                    {
                        r.setText(text);
                    }

                    databaseReference.setValue(r);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {

                    notifyDataSetChanged();
                    dialog.dismiss();
                }
            });
        }
    }

    public void retrieveData()
    {
        databaseReference = firebaseDatabase.getReference("Requests").child(r.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Request r1 = snapshot.getValue(Request.class);

                etTextReqU.setText(r1.getText());

                btDateU.setText(r1.getDate().toString());

                tvAddressU.setText(r1.getAddress().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}





