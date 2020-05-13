package com.openclassroom.mareu.controller;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.PorterDuff;
import android.os.Build;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.openclassroom.mareu.R;
import com.openclassroom.mareu.events.ClickReunionEvent;
import com.openclassroom.mareu.events.DeleteReunionEvent;
import com.openclassroom.mareu.model.Participant;
import com.openclassroom.mareu.model.Reunion;

import org.greenrobot.eventbus.EventBus;
import java.util.List;

public class MyReunionRecyclerViewAdapter extends RecyclerView.Adapter<MyReunionRecyclerViewAdapter.ViewHolder> {


    private final List<Reunion> mReunions;
    MyReunionRecyclerViewAdapter(List<Reunion> items) {
        mReunions = items;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_reunion, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Reunion reunion = mReunions.get(position);

        // Setup TextViews
        // Name - Time - Room
        String reunionInfo = reunion.getName()+" - "+ DateFormat.format("HH:mm", reunion.getBeginTime()).toString()+" - "+reunion.getLocation().getRoom();
        holder.mReunionInfo.setText(reunionInfo);
        // Avatar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            holder.mReunionAvatar.setColorFilter(new BlendModeColorFilter(reunion.getAvatarColor(), BlendMode.SRC_IN));
        } else {
            holder.mReunionAvatar.setColorFilter(reunion.getAvatarColor() , PorterDuff.Mode.SRC_IN);
        }
        // Participant
        holder.mParticipantsList.setText(appendParticipantsInString(reunion.getParticipants()));
        // Date in the avatar
        holder.mReunionDate.setText(DateFormat.format("dd/MM", reunion.getBeginTime().getTime()).toString());


        // Delete icon click listener
        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new DeleteReunionEvent(reunion));
            }
        });

        // Meeting click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new ClickReunionEvent(reunion));
            }
        });
    }

    // Make a string of all the meeting participants to display
    static String appendParticipantsInString (List<Participant> meetingParticipants) {
        StringBuilder reunionParticipants = new StringBuilder();
        for (int i = 0; i < meetingParticipants.size(); i++) {
            reunionParticipants.append(meetingParticipants.get(i).getEmail());
            if (i < meetingParticipants.size()-1) reunionParticipants.append(", ");
        }
        reunionParticipants.append(".");
        return reunionParticipants.toString();
    }

    // Function required for proper operation of the RecyclerView
    @Override
    public int getItemCount() {
        return mReunions.size();
    }


   static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mReunionAvatar ;
        TextView mReunionDate;
        TextView mReunionInfo;
        TextView mParticipantsList;
        ImageButton mDeleteButton;

        ViewHolder(View view) {
            super(view);
            mReunionAvatar = view.findViewById(R.id.item_list_avatar);
            mReunionDate = view.findViewById(R.id.item_list_date);
            mReunionInfo = view.findViewById(R.id.item_list_info);
            mParticipantsList = view.findViewById(R.id.item_list_participant);
            mDeleteButton = view.findViewById(R.id.item_list_delete_button);
        }
    }
}
