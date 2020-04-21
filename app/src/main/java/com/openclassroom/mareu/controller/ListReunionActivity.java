package com.openclassroom.mareu.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.openclassroom.mareu.R;
import com.openclassroom.mareu.di.DI;
import com.openclassroom.mareu.events.ClickReunionEvent;
import com.openclassroom.mareu.events.DeleteReunionEvent;
import com.openclassroom.mareu.model.Reunion;
import com.openclassroom.mareu.service.ReunionApiService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class ListReunionActivity extends AppCompatActivity {

    // UI Components
    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    FloatingActionButton mAddReunionButton;
    MyReunionRecyclerViewAdapter mRecyclerViewAdapter;
    ReunionApiService mApiService = DI.getReunionApiService();
    List<Reunion> mReunions = new ArrayList<>();
    Boolean isDateFiltered = false;
    Boolean isLocationFiltered = false;
    String dateFilter = "";
    String [] listMeetingRooms;
    String locationFilter = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_reunion);

        mToolbar = findViewById(R.id.toolbar);
        mAddReunionButton = findViewById(R.id.add_reunion);
        mRecyclerView = findViewById(R.id.list);
        listMeetingRooms = getResources().getStringArray(R.array.meetingrooms);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.filter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuItem1:
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ListReunionActivity.this);
                mBuilder.setTitle("Meeting rooms filter");
                mBuilder.setSingleChoiceItems(listMeetingRooms, -1, new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        locationFilter = listMeetingRooms[which];
                        initList();
                    }
                });
                mBuilder.setPositiveButton("OK", null);
                mBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
                isLocationFiltered = true;
                return true;

            case R.id.menuItem2:
                final Calendar cldr = Calendar.getInstance();
                int year = cldr.get(Calendar.YEAR);
                int month = cldr.get(Calendar.MONTH);
                int day = cldr.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(ListReunionActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, month);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                Date date = calendar.getTime();
                                DateFormat.format("dd/MM/yyyy", date);
                                dateFilter = DateFormat.format("dd/MM/yyyy", date).toString();
                                initList();

                            }
                        }, year, month, day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
                isDateFiltered = true;
                return true;

            case R.id.menuItem3:
                isDateFiltered = false;
                isLocationFiltered = false;
                initList();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Init the List of reunions
     */
    private void initList() {
        if (isDateFiltered){
            mReunions = new ArrayList<>();
            for (int i = 0; i < mApiService.getReunions().size(); i++){
                if (mApiService.getReunions().get(i).getDate().equals(dateFilter)){
                    mReunions.add(mApiService.getReunions().get(i));
                }
            }

        }else if (isLocationFiltered){
            mReunions = new ArrayList<>();
            for (int i = 0; i < mApiService.getReunions().size(); i++){
                if (mApiService.getReunions().get(i).getLocation().equals(locationFilter)){
                    mReunions.add(mApiService.getReunions().get(i));
                }
            }

        }else {
            mReunions = mApiService.getReunions();
        }

        mRecyclerView.setAdapter(new MyReunionRecyclerViewAdapter(mReunions));
    }

    @Override
    public void onResume() {
        super.onResume();

        initList();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * Fired if the user clicks on a delete button
     * @param event
     */
    @Subscribe
    public void onDeleteReunion(DeleteReunionEvent event) {
        mApiService.deleteReunion(event.reunion);
        initList();
    }

    /**
     * Fired if the user clicks on a neighbour fragment
     * @param event
     */
    @Subscribe
    public void onClickReunion(ClickReunionEvent event) {

        Intent detailActivityIntent = new Intent(ListReunionActivity.this, DetailReunionActivity.class);
        detailActivityIntent.putExtra("REUNION", event.reunion);
        startActivity(detailActivityIntent);
    }


}
