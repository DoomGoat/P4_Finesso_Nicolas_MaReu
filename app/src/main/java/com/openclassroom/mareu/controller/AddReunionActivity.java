package com.openclassroom.mareu.controller;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import com.google.android.material.textfield.TextInputLayout;
import com.openclassroom.mareu.R;
import com.openclassroom.mareu.di.DI;
import com.openclassroom.mareu.model.Participant;
import com.openclassroom.mareu.model.Reunion;
import com.openclassroom.mareu.service.ReunionApiService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class AddReunionActivity extends AppCompatActivity {

    //UI component
    ImageView avatar;
    TextInputLayout nameInput;
    TextView timeInput;
    TextView roomInput;
    MultiAutoCompleteTextView participantsInput;
    TextInputLayout reunionInfoInput;
    Button addButton;
    TimePickerDialog mTimePicker;

    private ReunionApiService mApiService;
    int mReunionColor;
    String[] listMeetingRooms;
    ArrayList<String> participants;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reunion);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mApiService = DI.getReunionApiService();
        avatar = findViewById(R.id.add_avatar);
        nameInput = findViewById(R.id.nameLyt);
        timeInput = findViewById(R.id.add_time);
        roomInput = findViewById(R.id.add_room);
        participantsInput = findViewById(R.id.add_participants);
        reunionInfoInput = findViewById(R.id.reunionAboutLyt);
        addButton = findViewById(R.id.add_button);
        mReunionColor = ((int)(Math.random()*16777215)) | (0xFF << 24);
        listMeetingRooms = getResources().getStringArray(R.array.meetingrooms);
        participants = new ArrayList<>();

        for (int i = 0 ; i < mApiService.getParticipants().size(); i++){
            participants.add(mApiService.getParticipants().get(i).getEmail());
        }
        final String[] arrayParticipants = participants.toArray(new String[participants.size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, arrayParticipants);
        participantsInput.setThreshold(1); //will start working from first character
        participantsInput.setAdapter(adapter);
        participantsInput.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        init();

        /**
        * Meeting room single choice input
        */
        roomInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddReunionActivity.this);
                mBuilder.setTitle("Meeting rooms available");
                mBuilder.setSingleChoiceItems(listMeetingRooms, -1, new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        roomInput.setText("Meeting in the "+listMeetingRooms[which]+" room");

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
            }
        });


        /**
         * Time select input
         */
        timeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                mTimePicker = new TimePickerDialog(AddReunionActivity.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                timeInput.setText("at  " + sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                mTimePicker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                mTimePicker.show();
            }
        });


        /**
         * Create the new reunion
         */
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reunion reunion = new Reunion(
                        System.currentTimeMillis(),
                        nameInput.getEditText().getText().toString(),
                        mReunionColor,
                        roomInput.getText().toString(),
                        timeInput.getText().toString(),
                        new ArrayList<Participant>(),
                        reunionInfoInput.getEditText().getText().toString()
                );
                mApiService.createReunion(reunion);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home : {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        avatar.getDrawable().setColorFilter(mReunionColor, PorterDuff.Mode.MULTIPLY);
        nameInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                addButton.setEnabled(s.length() > 0);
            }
        });

    }

}
