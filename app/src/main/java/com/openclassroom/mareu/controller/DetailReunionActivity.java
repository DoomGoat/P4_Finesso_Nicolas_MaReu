package com.openclassroom.mareu.controller;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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
    TextView mTimeTextView;
    TextView mParticipantsTextView;
    TextView mAboutTitleTextView;
    TextView mAboutDescriptionTextView;
    ImageButton mPreviousButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_reunion);

        mAvatarImageView = findViewById(R.id.detail_reunion);
        mSubjectTextView = findViewById(R.id.detail_subject);
        mDateTextView = findViewById(R.id.detail_date);
        mLocationTextView = findViewById(R.id.detail_info_location);
        mTimeTextView = findViewById(R.id.detail_info_time);
        mParticipantsTextView = findViewById(R.id.detail_info_participant);
        mAboutTitleTextView = findViewById(R.id.detail_about_title);
        mAboutDescriptionTextView = findViewById(R.id.detail_about_text);
        mPreviousButton = findViewById(R.id.detail_previous_button);

        Reunion currentReunion = Objects.requireNonNull(getIntent().getExtras()).getParcelable("REUNION");
        assert currentReunion != null;
        this.displayDetails(currentReunion);

        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @SuppressLint("SetTextI18n")
    private void displayDetails(final Reunion reunion) {

        mSubjectTextView.setText(reunion.getName());
        mDateTextView.setText(reunion.getDate());
        mLocationTextView.setText("Meeting in the " + reunion.getLocation() +" room." );
        mTimeTextView.setText("Meeting at " +reunion.getTime()+".");
        //mParticipantsTextView.setText(MyReunionRecyclerViewAdapter.reunionParticipants(reunion.getParticipants()));
        mAboutTitleTextView.setText(R.string.about_the_meeting);
        if (reunion.getInfo() == "") {
            mAboutDescriptionTextView.setText("No more information.");
        }else{
            mAboutDescriptionTextView.setText(reunion.getInfo());
        }
        mAvatarImageView.setBackgroundColor(reunion.getAvatarColor());
    }

}
