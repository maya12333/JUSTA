package com.example.justa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Contact extends AppCompatActivity implements View.OnClickListener {

    private EditText etPhoneNumber;

    private Button btSend;
    private Button btCall;
    private Button btCallYes;
    private Button btCallNo;
    private Button btSendYes;
    private Button btSendNo;

    private ImageView ivBackC;
    private ImageView ivExitCall;
    private ImageView ivExitSend;

    private TextView tvPhoneCall;
    private TextView tvPhoneSend;

    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        etPhoneNumber = findViewById(R.id.etPhoneNumber);

        btSend = findViewById(R.id.btSend);
        btCall = findViewById(R.id.btCall);

        ivBackC = findViewById(R.id.ivBackC);

        btSend.setOnClickListener(this);
        btCall.setOnClickListener(this);

        ivBackC.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(view == btSend)
        {
            openDialogSend();
        }

        if(view == btCall)
        {
            openDialogCall();
        }

        if(view == btCallYes)
        {
            String phone = etPhoneNumber.getText().toString();

            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:"+phone));//change the number
            startActivity(callIntent);
        }

        if(view == btCallNo)
        {
            dialog.dismiss();
        }

        if(view == btSendYes)
        {
            String phone = etPhoneNumber.getText().toString();

            Intent sendIntent = new Intent(Intent.ACTION_VIEW,Uri.fromParts("sms",phone,null));
            startActivity(sendIntent);
        }

        if(view == btSendNo)
        {
            dialog.dismiss();
        }

        if(view == ivExitCall)
        {
            dialog.dismiss();
        }

        if(view == ivExitSend)
        {
            dialog.dismiss();
        }

        if(view == ivBackC)
        {
            Intent go = new Intent(Contact.this, Open_volunteer.class);

            startActivity(go);

            finish();
        }
    }

    public void openDialogCall()
    {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.call_dialog);

        String phone = etPhoneNumber.getText().toString();

        if(!check(phone))
            return;

        tvPhoneCall = dialog.findViewById(R.id.tvPhoneCall);
        tvPhoneCall.setText(phone);

        btCallYes = dialog.findViewById(R.id.btCallYes);
        btCallNo = dialog.findViewById(R.id.btCallNo);

        ivExitCall = dialog.findViewById(R.id.ivExitCall);

        btCallYes.setOnClickListener(this);
        btCallNo.setOnClickListener(this);

        ivExitCall.setOnClickListener(this);

        dialog.show();
    }

    public void openDialogSend()
    {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.send_dialog);

        String phone = etPhoneNumber.getText().toString();

        tvPhoneSend = dialog.findViewById(R.id.tvPhoneSend);

        if(!check(phone))
            return;

        tvPhoneSend.setText(phone);

        btSendYes = dialog.findViewById(R.id.btSendYes);
        btSendNo = dialog.findViewById(R.id.btSendNo);

        ivExitSend = dialog.findViewById(R.id.ivExitSend);

        btSendYes.setOnClickListener(this);
        btSendNo.setOnClickListener(this);

        ivExitSend.setOnClickListener(this);

        dialog.show();
    }

    public boolean check(String phone)
    {
        if(phone.length() == 0)
        {
            Toast.makeText(Contact.this, "ENTER PHONE NUMBER", Toast.LENGTH_LONG).show();

            return false;
        }

        if(phone.length() < 10 || phone.length() > 10)
        {
            Toast.makeText(Contact.this, "WRONG PHONE NUMBER", Toast.LENGTH_LONG).show();
            etPhoneNumber.setText("");

            return false;
        }

        return true;
    }
}