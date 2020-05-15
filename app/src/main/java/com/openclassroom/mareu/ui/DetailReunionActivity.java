package com.openclassroom.mareu.ui;

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
    TextView mTimeTextView;
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
        mTimeTextView = findViewById(R.id.detail_info_time);
        mParticipantsTextView = findViewById(R.id.detail_info_participant);
        mAboutTitleTextView = findViewById(R.id.detail_about_title);
        mAboutDescriptionTextView = findViewById(R.id.detail_about_text);
        // Get all meeting info through Parcelable
        Reunion currentReunion = Objects.requireNonNull(getIntent().getExtras()).getParcelable("REUNION");
        assert currentReunion != null;
        this.displayDetails(currentReunion);
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


    private void displayDetails(final Reunion reunion) {
        mAvatarImageView.setBackgroundColor(reunion.getAvatarColor());
        mSubjectTextView.setText(reunion.getName());
        mDateTextView.setText(DateFormat.format("dd/MM/yyyy", reunion.getBeginTime()));
        mLocationTextView.setText(getString(R.string.meeting_in_the_x_room, reunion.getLocation().getRoom()));
        mTimeTextView.setText(getString(R.string.meeting_from_x_to_x,
                DateFormat.format("HH:mm", reunion.getBeginTime()),
                DateFormat.format("HH:mm", reunion.getEndTime())));
        mParticipantsTextView.setText(MyReunionRecyclerViewAdapter.appendParticipantsInString(reunion.getParticipants()));
        mAboutTitleTextView.setText(R.string.about_the_meeting);
        // Info message displayed if fill by user or not
        if (reunion.getInfo().length() < 2) {
            mAboutDescriptionTextView.setText(R.string.no_more_info);
        } else {
            mAboutDescriptionTextView.setText(reunion.getInfo());
        }
    }

}
