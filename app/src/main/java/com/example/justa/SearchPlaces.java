package com.example.justa;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

public class SearchPlaces extends AppCompatActivity {

    EditText etSearch;

    RecyclerView rvS;
    
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_places);
        
        etSearch = findViewById(R.id.etSearch);

        rvS = findViewById(R.id.rvS);

        relativeLayout = findViewById(R.id.notdata_found);

        relativeLayout.setVisibility(View.VISIBLE);
        
        rvS.setVisibility(View.GONE);

        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.toString().length() > 0)
                {
                    getAddress(editable.toString());
                }
            }
        };

        etSearch.addTextChangedListener(tw);
        etSearch.removeTextChangedListener(tw);
        etSearch.setText("");
        etSearch.addTextChangedListener(tw);
    }

    public void getAddress(String locationName)
    {
        Geocoder gc = new Geocoder(SearchPlaces.this);

        try{

            List<Address> addressList = gc.getFromLocationName(locationName, 5);

            relativeLayout.setVisibility(View.GONE);

            rvS.setVisibility(View.VISIBLE);

            RecyclerviewAdapter recyclerviewAdapter = new RecyclerviewAdapter(addressList);

            rvS.setAdapter(recyclerviewAdapter);

            recyclerviewAdapter.setOnClickListener(new RecyclerviewAdapter.OnClickListener() {
                @Override
                public void OnClick(int position, Address address) {

                    Intent returnIntent = new Intent();

                    returnIntent.putExtra("address", address.getAddressLine(position));

                    setResult(Activity.RESULT_OK, returnIntent);

                    finish();
                }
            });
        }

        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}