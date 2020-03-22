package com.example.roadcross;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase.getInstance().getReference().child("Ayyy").child("hell").setValue("yeah");

        HashMap<String, Object> map=new HashMap<>();
        map.put("Lat",32.145);
        map.put("Lng",34.145);

        FirebaseDatabase.getInstance().getReference().child("Users").child("User0").setValue(map);
    }

}
