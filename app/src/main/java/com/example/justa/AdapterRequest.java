package com.example.justa;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

public class AdapterRequest extends ArrayAdapter<Request> {

    private String phone;


    public AdapterRequest(@NonNull Context context, int resource, String phone) {
        super(context, resource);
        this.phone = phone;
    }
}
