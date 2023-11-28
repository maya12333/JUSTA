package com.example.justa;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AdapterRecommend extends ArrayAdapter<Recommendation> {

    private Context context;
    private ArrayList<Recommendation> arrayList;

    private Dialog dialog;

    public AdapterRecommend(@NonNull Context context, int resource, int textViewResourceId, @NonNull ArrayList<Recommendation> objects) {
        super(context, resource, textViewResourceId, objects);

        this.context = context;
        this.arrayList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater li = ((Activity)context).getLayoutInflater();

        View view = li.inflate(R.layout.recommendation_listview,parent, false);

        TextView tvNameV = view.findViewById(R.id.tvNameV);
        TextView tvPhoneV = view.findViewById(R.id.tvPhoneV);
        TextView tvTextV = view.findViewById(R.id.tvTextV);

        Recommendation r = arrayList.get(position);

        tvNameV.setText(r.getNameV());
        tvPhoneV.setText(r.getPhoneV());
        tvTextV.setText(r.getTextV());


        return view;
    }
}
