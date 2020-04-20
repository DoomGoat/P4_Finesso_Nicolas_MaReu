package com.openclassroom.mareu.controller;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.openclassroom.mareu.R;
import com.openclassroom.mareu.di.DI;
import com.openclassroom.mareu.model.Participant;
import com.openclassroom.mareu.model.Reunion;
import com.openclassroom.mareu.service.ReunionApiService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AddReunionActivity extends AppCompatActivity {

    //UI component
    ImageView avatar;
    TextInputLayout nameInput;
    TextView dateInput;
    TextView timeInput;
    TextView roomInput;
    MultiAutoCompleteTextView participantsInput;
    TextInputLayout reunionInfoInput;
    Button addButton;
    TimePickerDialog mTimePicker;


    private ReunionApiService mApiService;
    int mReunionColor;
    String[] listMeetingRooms;
    ArrayList<String> participantsList;
    List<Participant> meetingParticipants = new ArrayList<>();
    String room;
    String time;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reunion);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mApiService = DI.getReunionApiService();
        avatar = findViewById(R.id.add_avatar);
        nameInput = findViewById(R.id.nameLyt);
        dateInput = findViewById(R.id.add_date);
        timeInput = findViewById(R.id.add_time);
        roomInput = findViewById(R.id.add_room);
        participantsInput = findViewById(R.id.add_participants);
        reunionInfoInput = findViewById(R.id.reunionAboutLyt);
        addButton = findViewById(R.id.add_button);
        mReunionColor = ((int)(Math.random()*16777215)) | (0xFF << 24);
        listMeetingRooms = getResources().getStringArray(R.array.meetingrooms);
        participantsList = new ArrayList<>();

        //Setup avatar color
        avatar.getDrawable().setColorFilter(mReunionColor, PorterDuff.Mode.MULTIPLY);

        //Setup participants ArrayList with only names
        for (int i = 0 ; i < mApiService.getParticipants().size(); i++){
            String email = mApiService.getParticipants().get(i).getEmail();
            participantsList.add(email.substring(0, email.indexOf("@")));
        }
        final String[] arrayParticipants = participantsList.toArray(new String[0]);

        //Participant autocomplete field
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this, android.R.layout.select_dialog_item, arrayParticipants);
        participantsInput.setThreshold(1); //will start working from first character
        participantsInput.setAdapter(adapter);
        participantsInput.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        //Get the Participant Object onClick
        participantsInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemString = participantsInput.getAdapter().getItem(position).toString();
                for (int i = 0; i < arrayParticipants.length ; i++){
                    if (participantsList.get(i).equals(itemString)){
                        meetingParticipants.add(mApiService.getParticipants().get(i));
                        break;
                    }
                }
                enableButtonIfReady();
            }
        });

        //Date select input
        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int year = cldr.get(Calendar.YEAR);
                int month = cldr.get(Calendar.MONTH);
                int day = cldr.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddReunionActivity.this,
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
                                String dateLabel = DateFormat.format("dd/MM/yyyy", date).toString();
                                dateInput.setText(dateLabel);
                                enableButtonIfReady();
                            }
                        }, year, month, day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();

            }
        });


        //Meeting room single choice input
        roomInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddReunionActivity.this);
                mBuilder.setTitle("Meeting rooms available");
                mBuilder.setSingleChoiceItems(listMeetingRooms, -1, new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        room = listMeetingRooms[which];
                        roomInput.setText("Meeting in the "+room+" room");

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
                enableButtonIfReady();
            }
        });


        //Time select input
        timeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                mTimePicker = new TimePickerDialog(AddReunionActivity.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        new TimePickerDialog.OnTimeSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                time = checkDigit(sHour) + ":" + checkDigit(sMinute);
                                timeInput.setText("At "+time);
                                enableButtonIfReady();
                            }
                        }, hour, minutes, true);
                Objects.requireNonNull(mTimePicker.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                mTimePicker.show();
            }
        });

        Objects.requireNonNull(nameInput.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                enableButtonIfReady();
            }
        });



        //Create the new reunion
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reunion reunion = new Reunion(
                        System.currentTimeMillis(),
                        Objects.requireNonNull(nameInput.getEditText()).getText().toString(),
                        mReunionColor,
                        room,
                        time,
                        dateInput.getText().toString(),
                        meetingParticipants,
                        Objects.requireNonNull(reunionInfoInput.getEditText()).getText().toString()
                );
                if (checkMeetingAvailability()) {
                    mApiService.createReunion(reunion);
                    finish();
                }else {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddReunionActivity.this);
                            mBuilder.setCancelable(true);
                            mBuilder.setTitle("Room unavailable");
                            mBuilder.setMessage("The "+room+" meeting room is not available at "+time+", please select another meeting room or try for another time slot.");
                            mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            mBuilder.show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public boolean checkMeetingAvailability() {
        boolean available = true;
        for (int i = 0; i < mApiService.getReunions().size(); i++){

            if (dateInput.getText().toString().equals(mApiService.getReunions().get(i).getDate())){
                if (room.equals(mApiService.getReunions().get(i).getLocation())){

                    //Compare times with an interval
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                    Date time1 = null, time2 = null, interval = null;
                    try {
                        time1 = timeFormat.parse(time);
                        time2 = timeFormat.parse(mApiService.getReunions().get(i).getTime());
                        interval = timeFormat.parse("00:45");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    assert time1 != null;
                    assert time2 != null;
                    assert interval != null;

                    if (time1.getTime() <= time2.getTime() + interval.getTime() && time1.getTime() >= time2.getTime() - interval.getTime()){
                        available = false;
                    }
                }
            }
        }
        return available;
    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    public void enableButtonIfReady () {
        boolean isReady = (Objects.requireNonNull(nameInput.getEditText()).getText().length() > 0
                && timeInput.getText().length() > 0
                && dateInput.getText().length() > 0
                && roomInput.getText().length() > 0
                && participantsInput.getText().length() > 0);

        if (isReady){
            addButton.setEnabled(true);
            addButton.setBackgroundTintList(ColorStateList.valueOf(0xFF004d99));
        }
    }

}
