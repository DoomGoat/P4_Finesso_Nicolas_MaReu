package com.openclassroom.mareu.ui;

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
    List<Reunion> mReunionsArrayList = new ArrayList<>();
    Boolean isDateFiltered = false;
    Boolean isLocationFiltered = false;
    Date dateFilterSelected;
    String roomFilterSelected = "";
    String [] listOfMeetingRooms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_reunion);

        mToolbar = findViewById(R.id.toolbar);
        mAddReunionButton = findViewById(R.id.add_reunion);
        mRecyclerView = findViewById(R.id.list_reunion);
        listOfMeetingRooms = listRoomsInStrings();

        setSupportActionBar(mToolbar);

        mRecyclerViewAdapter = new  MyReunionRecyclerViewAdapter(mApiService.getReunions());
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        // Add meeting button listener
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

    /**
     * Menu Filter selection
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            // Room filter
            case R.id.menuItem1:
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ListReunionActivity.this);
                mBuilder.setTitle(R.string.meeting_room_filter);
                mBuilder.setSingleChoiceItems(listOfMeetingRooms, -1, new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println(which);
                        roomFilterSelected = listOfMeetingRooms[which];
                        initList();
                    }
                });
                mBuilder.setPositiveButton(R.string.ok, null);
                mBuilder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isLocationFiltered = false;
                        initList();
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
                isLocationFiltered = true;
                return true;

            // Date filter
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
                                dateFilterSelected = calendar.getTime();
                                initList();

                            }
                        }, year, month, day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
                isDateFiltered = true;
                return true;

            // Clear filter
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
        // Filter the list if one is set
        mReunionsArrayList = mApiService.reunionListFilter(isDateFiltered, isLocationFiltered, roomFilterSelected, dateFilterSelected);
        // Set the RecyclerViewAdapter
        mRecyclerView.setAdapter(new MyReunionRecyclerViewAdapter(mReunionsArrayList));
    }

    // App life cycles
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

    // Delete the meeting from the list when trash icon is clicked
    @Subscribe
    public void onDeleteReunion(DeleteReunionEvent event) {
        mApiService.deleteReunion(event.reunion);
        initList();
    }

    // Go to the DetailActivity when a meeting cell is clicked
    @Subscribe
    public void onClickReunion(ClickReunionEvent event) {
        Intent detailActivityIntent = new Intent(ListReunionActivity.this, DetailReunionActivity.class);
        detailActivityIntent.putExtra("REUNION", event.reunion);
        startActivity(detailActivityIntent);
    }

    // List the rooms in strings for the singleChoicePicker
    public String [] listRoomsInStrings() {
        String [] list = new String [mApiService.getRooms().size()];
        for (int i = 0 ; i < mApiService.getRooms().size(); i++){
            list[i] = mApiService.getRooms().get(i).getRoom();
        }
        return list;
    }
}
