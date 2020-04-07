package com.openclassroom.mareu.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.openclassroom.mareu.R;
import com.openclassroom.mareu.di.DI;
import com.openclassroom.mareu.service.ReunionApiService;

import org.greenrobot.eventbus.EventBus;


public class ListReunionActivity extends AppCompatActivity {

    // UI Components
    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    FloatingActionButton mAddReunionButton;
    MyReunionRecyclerViewAdapter mRecyclerViewAdapter;
    ReunionApiService mApiService = DI.getReunionApiService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_reunion);

        mToolbar = findViewById(R.id.toolbar);
        mAddReunionButton = findViewById(R.id.add_reunion);
        mRecyclerView = findViewById(R.id.list);

        setSupportActionBar(mToolbar);
        mRecyclerViewAdapter = new  MyReunionRecyclerViewAdapter(mApiService.getReunions());
        mRecyclerView.setAdapter(mRecyclerViewAdapter);


        mAddReunionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addActivityIntent = new Intent(ListReunionActivity.this, AddReunionActivity.class);
                startActivity(addActivityIntent);
            }
        });
    }


}
