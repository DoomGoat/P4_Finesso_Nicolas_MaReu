package com.openclassroom.mareu.controller;

import android.annotation.SuppressLint;
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
import com.openclassroom.mareu.service.ReunionApiService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class AddReunionActivity extends AppCompatActivity implements DialogNumberPicker.DialogNumberPickerListener {

    //UI component
    ImageView avatar;
    TextInputLayout nameInput;
    TextView dateInput;
    TextView timeInput;
    TextView durationInput;
    TextView roomInput;
    TextView dash;
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
    Date beginTime;
    Date endTime;
    Date duration;
    String timeSet;
    String dateSet = DateFormat.format("dd/MM/yyyy", Calendar.getInstance().getTime()).toString();
    String unavailableMessage;


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
        durationInput = findViewById(R.id.add_duration);
        roomInput = findViewById(R.id.add_room);
        participantsInput = findViewById(R.id.add_participants);
        reunionInfoInput = findViewById(R.id.reunionAboutLyt);
        addButton = findViewById(R.id.add_button);
        dash = findViewById(R.id.dash);
        mReunionColor = ((int)(Math.random()*16777215)) | (0xFF << 24);
        listMeetingRooms = getResources().getStringArray(R.array.meetingrooms);
        participantsList = new ArrayList<>();

        //Setup avatar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            avatar.getDrawable().setColorFilter(new BlendModeColorFilter(mReunionColor, BlendMode.SRC_IN));
        } else {
            //noinspection deprecation
            avatar.getDrawable().setColorFilter(mReunionColor , PorterDuff.Mode.SRC_IN);
        }

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
                                dateSet = DateFormat.format("dd/MM/yyyy", calendar.getTime()).toString();
                                dateInput.setText(dateSet);
                                System.out.println("DATE SET : "+calendar.getTime());
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
                            @SuppressLint("SetTextI18n")//TODO
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                timeSet = checkDigit(sHour)+":"+checkDigit(sMinute);
                                timeInput.setText("At "+timeSet);
                                System.out.println("TIME SET : "+timeSet);
                                enableButtonIfReady();
                                durationInput.setAlpha(1);
                                durationInput.setEnabled(true);
                            }
                        }, hour, minutes, true);
                Objects.requireNonNull(mTimePicker.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                mTimePicker.show();
            }
        });

        //Duration select input
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
            public void afterTextChanged(Editable s) {
                enableButtonIfReady();
            }
        });



        //Create the new reunion
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    beginTime = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).parse(dateSet+" "+timeSet);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Reunion reunion = new Reunion(
                        System.currentTimeMillis(),
                        Objects.requireNonNull(nameInput.getEditText()).getText().toString(),
                        mReunionColor,
                        room,
                        beginTime,
                        endTime,
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
                            mBuilder.setMessage(unavailableMessage);
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

    public void openNumberPicker(){
        DialogNumberPicker dialogNumberPicker = new DialogNumberPicker();
        dialogNumberPicker.show(getSupportFragmentManager(), "NumberPicker");
    }
    
    public boolean checkMeetingAvailability() {
        boolean available = true;
        String comparedMeetingTimeBegin = "";
        String comparedMeetingTimeEnd = "";
        for (int i = 0; i < mApiService.getReunions().size(); i++){
            System.out.println("SELECTED DATE : "+ beginTime);
            System.out.println("SELECTED DATE LONG: "+ beginTime.getTime());
            System.out.println("COMPARING DATE : "+ mApiService.getReunions().get(i).getBeginTime());
            System.out.println("COMPARING DATE LONG: "+ mApiService.getReunions().get(i).getBeginTime().getTime());

            //Compare times with an interval
            long time1 = beginTime.getTime();
            long time2 = endTime.getTime();
            long time3 = mApiService.getReunions().get(i).getBeginTime().getTime();
            long time4 = mApiService.getReunions().get(i).getEndTime().getTime();

            if ((time1 <= time3 && time2 >= time3) || (time1 <= time4 && time2 >= time4)||(time1 >= time3 && time2 <= time4)){
                if (room.equals(mApiService.getReunions().get(i).getLocation())){
                    comparedMeetingTimeBegin = DateFormat.format("HH:mm", time3).toString();
                    comparedMeetingTimeEnd = DateFormat.format("HH:mm", time4).toString();
                    available = false;
                }
            }
        }
        unavailableMessage = "The "+room+" meeting room is not available between "+ comparedMeetingTimeBegin +" and "+comparedMeetingTimeEnd+"."+"\n"+"\n"+"Please select another meeting room or try for another time slot.";
        return available;
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

    @Override
    public void durationListener(int hour, int minute) {
        try {
            duration = new SimpleDateFormat("HH:mm", Locale.getDefault()).parse(hour+":"+minute);
            beginTime = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault()).parse(dateSet+" "+timeSet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Fix GMT offset
        long currentGMT = TimeZone.getDefault().getRawOffset();
        duration = new Date(duration.getTime()+currentGMT);

        if (beginTime != null)
        endTime = new Date(beginTime.getTime()+duration.getTime());

        System.out.println("BEGIN TIME : "+beginTime);
        System.out.println("BEGIN TIME LONG : "+beginTime.getTime());
        System.out.println("COMPARING DATE : "+ mApiService.getReunions().get(4).getBeginTime());
        System.out.println("COMPARING DATE LONG: "+ mApiService.getReunions().get(4).getBeginTime().getTime());
        System.out.println("DURATION TIME : "+duration);
        System.out.println("DURATION TIME LONG: "+duration.getTime());
        System.out.println("END TIME : "+endTime);
        System.out.println("END TIME LONG : "+endTime.getTime());
        System.out.println("CURRENT TIME ZONE : "+currentGMT);

        durationInput.setText(DateFormat.format("HH:mm", endTime));
        dash.setText(R.string.until);
    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }
}
