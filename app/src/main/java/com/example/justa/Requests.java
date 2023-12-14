package com.example.justa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;

public class Requests extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivBackReq;
    private ImageView ivAddRequest;
    private ImageView ivExitReq;

    private ListView lvAddRequest;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        ivBackReq = findViewById(R.id.ivBackReq);
        ivAddRequest = findViewById(R.id.ivAddRequest);

        lvAddRequest =findViewById(R.id.lvAddRequest);

        ivBackReq.setOnClickListener(this);
        ivAddRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(view == ivAddRequest)
        {
            openDialog();
        }

        if(view == ivBackReq)
        {
            Intent go = new Intent(Requests.this, Open_needy.class);

            startActivity(go);

            finish();
        }

        if(view == ivExitReq)
        {
            dialog.dismiss();
        }
    }

    public void openDialog()
    {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_request_dialog);

        ivExitReq = dialog.findViewById(R.id.ivExitReq);


        dialog.show();

    }
}