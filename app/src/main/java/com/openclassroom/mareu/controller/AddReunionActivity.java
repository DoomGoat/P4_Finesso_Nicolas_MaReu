package com.openclassroom.mareu.controller;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.openclassroom.mareu.R;
import com.openclassroom.mareu.di.DI;
import com.openclassroom.mareu.model.Participant;
import com.openclassroom.mareu.model.Reunion;
import com.openclassroom.mareu.model.Room;
import com.openclassroom.mareu.service.ReunionApiService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class AddReunionActivity extends AppCompatActivity implements DialogNumberPicker.DialogNumberPickerListener {

    // UI component
    ImageView avatarImage;
    TextInputLayout nameInput;
    TextView dateInput;
    TextView timeInput;
    TextView durationInput;
    TextView roomInput;
    TextView dashTV;
    MultiAutoCompleteTextView participantsInput;
    TextInputLayout reunionInfoInput;
    Button addButton;
    TimePickerDialog mTimePicker;

    // Meeting variables
    private ReunionApiService mApiService;
    int meetingColor;
    List<Participant> meetingParticipants = new ArrayList<>();
    Room meetingRoom;
    Date meetingBeginTime;
    Date meetingEndTime;
    Date meetingDuration;
    String[] listMeetingRooms;
    ArrayList<String> listOfParticipantsNames = new ArrayList<>();
    Calendar beginCalendar = Calendar.getInstance();
    // Fix GMT offset
    long currentGMT = TimeZone.getDefault().getRawOffset();
    String unavailableMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reunion);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        avatarImage = findViewById(R.id.add_avatar);
        nameInput = findViewById(R.id.nameLyt);
        dateInput = findViewById(R.id.add_date);
        timeInput = findViewById(R.id.add_time);
        durationInput = findViewById(R.id.add_duration);
        roomInput = findViewById(R.id.add_room);
        participantsInput = findViewById(R.id.add_participants);
        reunionInfoInput = findViewById(R.id.reunionAboutLyt);
        addButton = findViewById(R.id.add_button);
        dashTV = findViewById(R.id.dash);

        mApiService = DI.getReunionApiService();
        meetingColor = ((int)(Math.random()*16777215)) | (0xFF << 24);
        listMeetingRooms = listRoomsInStrings();

        // Setup avatar color (for old and new SDK)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            avatarImage.setColorFilter(new BlendModeColorFilter(meetingColor, BlendMode.SRC_IN));
        } else {
            avatarImage.setColorFilter(meetingColor, PorterDuff.Mode.SRC_IN);
        }


        // Date select input
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
                                beginCalendar.set(Calendar.YEAR, year);
                                beginCalendar.set(Calendar.MONTH, month);
                                beginCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                String dateSet = DateFormat.format("dd/MM/yyyy", beginCalendar.getTime()).toString();
                                dateInput.setText(dateSet);
                                enableCreateButtonIfReady();
                            }
                        }, year, month, day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();

            }
        });


        // Meeting Room single choice input
        roomInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddReunionActivity.this);
                mBuilder.setTitle(R.string.meeting_room_available);
                mBuilder.setSingleChoiceItems(listMeetingRooms, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        meetingRoom = mApiService.getRooms().get(which);
                        roomInput.setText(getString(R.string.meeting_in_the_x_room, listMeetingRooms[which]));

                    }
                });
                mBuilder.setPositiveButton(R.string.ok, null);
                mBuilder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
                enableCreateButtonIfReady();
            }
        });


        // Time select input
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
                                beginCalendar.set(Calendar.HOUR_OF_DAY, sHour);
                                beginCalendar.set(Calendar.MINUTE, sMinute);
                                beginCalendar.set(Calendar.SECOND, 0);
                                timeInput.setText(getString(R.string.at_x, DateFormat.format("HH:mm", beginCalendar.getTime()).toString()));
                                enableCreateButtonIfReady();
                                //Enable duration picker
                                durationInput.setAlpha(1);
                                durationInput.setEnabled(true);
                                //Update end time if already set
                                if (durationInput.getText().length()>0){
                                    updateTimes();
                                    durationInput.setText(DateFormat.format("HH:mm", meetingEndTime));
                                }
                            }
                        }, hour, minutes, true);
                Objects.requireNonNull(mTimePicker.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                mTimePicker.show();
            }
        });


        // Duration select input
        durationInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNumberPicker();
            }
        });

        Objects.requireNonNull(nameInput.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) { enableCreateButtonIfReady(); }
        });


        // Setup participants ArrayList with only names
        for (int i = 0 ; i < mApiService.getParticipants().size(); i++){
            String email = mApiService.getParticipants().get(i).getEmail();
            listOfParticipantsNames.add(email.substring(0, email.indexOf("@")));
        }
        final String[] arrayParticipantsNames = listOfParticipantsNames.toArray(new String[0]);

        // Participant multi autocomplete field init
        ArrayAdapter<String> adapter =
                new ArrayAdapter<> (this, android.R.layout.select_dialog_item, arrayParticipantsNames);
        participantsInput.setThreshold(1); //will start working from first character
        participantsInput.setAdapter(adapter);
        participantsInput.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        // Participant select input
        participantsInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemNameDisplayed = participantsInput.getAdapter().getItem(position).toString();
                for (int i = 0; i < arrayParticipantsNames.length ; i++){
                    if (listOfParticipantsNames.get(i).equals(itemNameDisplayed)){
                        meetingParticipants.add(mApiService.getParticipants().get(i));
                        break;
                    }
                }
                enableCreateButtonIfReady();
            }
        });


        // Create the new meeting AND check if other meeting overlap
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reunion reunion = new Reunion(
                        System.currentTimeMillis(),
                        nameInput.getEditText().getText().toString(),
                        meetingColor,
                        meetingRoom,
                        meetingBeginTime,
                        meetingEndTime,
                        meetingParticipants,
                        Objects.requireNonNull(reunionInfoInput.getEditText()).getText().toString() );

                // Create if no other meeting overlap the new one
                if (checkMeetingAvailability()) {
                    mApiService.createReunion(reunion);
                    finish();

                // Else return dialog with the info of the overlap meeting
                } else {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddReunionActivity.this);
                    mBuilder.setCancelable(true);
                    mBuilder.setTitle(R.string.meeting_room_unavailable);
                    mBuilder.setMessage(unavailableMessage);
                    mBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) { }
                    });
                    mBuilder.show();
                }
            }
        });
    }

    // Back button click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Open the duration select dialog
    public void openNumberPicker(){
        DialogNumberPicker dialogNumberPicker = new DialogNumberPicker();
        dialogNumberPicker.show(getSupportFragmentManager(), "NumberPicker");
    }

    // Function to check if meetings overlap
    public boolean checkMeetingAvailability() {
        boolean available = true;

        for (int i = 0; i < mApiService.getReunions().size(); i++){

            long newBeginTime = meetingBeginTime.getTime();
            long newEndTime = meetingEndTime.getTime();
            long existingBeginTime = mApiService.getReunions().get(i).getBeginTime().getTime();
            long existingEndTime = mApiService.getReunions().get(i).getEndTime().getTime();

            if ((newBeginTime <= existingBeginTime && newEndTime >= existingBeginTime) || // If new overlap the begin of existing meeting
                    (newBeginTime <= existingEndTime && newEndTime >= existingEndTime) || // If new overlap between existing meeting
                    (newBeginTime >= existingBeginTime && newEndTime <= existingEndTime)) { // If new overlap the end of existing meeting
                // If it happen in the same room return the error message
                if (meetingRoom.getRoom().equals(mApiService.getReunions().get(i).getLocation().getRoom())){
                    String comparedTimeBegin = DateFormat.format("HH:mm", existingBeginTime).toString();
                    String comparedTimeEnd = DateFormat.format("HH:mm", existingEndTime).toString();
                    unavailableMessage = getString(R.string.unavailable_message, meetingRoom.getRoom(), comparedTimeBegin, comparedTimeEnd);
                    available = false;
                }
            }
        }
        return available;
    }

    // Enable the Create button if all the fields are fill (info is optional)
    public void enableCreateButtonIfReady() {
        boolean isReady = (Objects.requireNonNull(nameInput.getEditText()).getText().length() > 0
                && timeInput.getText().length() > 0
                && dateInput.getText().length() > 0
                && roomInput.getText().length() > 0
                && participantsInput.getText().length() > 0
                && durationInput.getText().length() > 0);

        if (isReady){
            addButton.setEnabled(true);
            addButton.setBackgroundTintList(ColorStateList.valueOf(0xFF004d99));
        }
    }

    // Get the meeting duration set with the DialogNumberPicker
    @Override
    public void durationListener(int hour, int minute) {
        Calendar durationCal = Calendar.getInstance(Locale.getDefault());
        durationCal.setTime(new Date(0));
        durationCal.set(Calendar.HOUR_OF_DAY, hour);
        durationCal.set(Calendar.MINUTE, minute);
        meetingDuration = durationCal.getTime();
        updateTimes();

        durationInput.setText(DateFormat.format("HH:mm", meetingEndTime));
        dashTV.setText(R.string.until);
    }

    // Update meeting times
    public  void updateTimes () {
        meetingBeginTime = beginCalendar.getTime();
        meetingEndTime = new Date(meetingBeginTime.getTime()+ meetingDuration.getTime()+currentGMT);
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
