package com.openclassroom.mareu.controller;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.openclassroom.mareu.R;
import com.openclassroom.mareu.model.Reunion;

import java.util.Objects;

public class DetailReunionActivity extends AppCompatActivity {

    ConstraintLayout mAvatarImageView;
    TextView mSubjectTextView;
    TextView mDateTextView;
    TextView mLocationTextView;
    TextView mBeginTimeTextView;
    TextView mEndTimeTextView;
    TextView mParticipantsTextView;
    TextView mAboutTitleTextView;
    TextView mAboutDescriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_reunion);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mAvatarImageView = findViewById(R.id.detail_reunion);
        mSubjectTextView = findViewById(R.id.detail_subject);
        mDateTextView = findViewById(R.id.detail_date);
        mLocationTextView = findViewById(R.id.detail_info_location);
        mBeginTimeTextView = findViewById(R.id.detail_info_begin_time);
        mEndTimeTextView = findViewById(R.id.detail_info_end_time);
        mParticipantsTextView = findViewById(R.id.detail_info_participant);
        mAboutTitleTextView = findViewById(R.id.detail_about_title);
        mAboutDescriptionTextView = findViewById(R.id.detail_about_text);

        Reunion currentReunion = Objects.requireNonNull(getIntent().getExtras()).getParcelable("REUNION");
        assert currentReunion != null;
        this.displayDetails(currentReunion);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayDetails(final Reunion reunion) {
        String locationTV = "Meeting in the " + reunion.getLocation().getRoom() +" room.";
        String beginTimeTV= "Meeting from " + DateFormat.format("HH:mm", reunion.getBeginTime())+" to ";
        String endTimeTV = DateFormat.format("HH:mm", reunion.getEndTime())+".";
        String descriptionTV = "No more information.";
        mSubjectTextView.setText(reunion.getName());
        mDateTextView.setText(DateFormat.format("dd/MM/yyyy", reunion.getBeginTime()));
        mLocationTextView.setText(locationTV);
        mBeginTimeTextView.setText(beginTimeTV);
        mEndTimeTextView.setText(endTimeTV);
        mParticipantsTextView.setText(MyReunionRecyclerViewAdapter.reunionParticipants(reunion.getParticipants()));
        mAboutTitleTextView.setText(R.string.about_the_meeting);
        if (reunion.getInfo().length() < 2) {
            mAboutDescriptionTextView.setText(descriptionTV);
        }else{
            mAboutDescriptionTextView.setText(reunion.getInfo());
        }
        mAvatarImageView.setBackgroundColor(reunion.getAvatarColor());
    }

}
