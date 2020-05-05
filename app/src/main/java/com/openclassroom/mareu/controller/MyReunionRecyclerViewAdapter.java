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
        String reunionInfo = reunion.getName()+" - "+ DateFormat.format("HH:mm", reunion.getBeginTime()).toString()+" - "+reunion.getLocation().getRoom();
        holder.mReunionInfo.setText(reunionInfo);
        //Setup avatar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            holder.mReunionAvatar.getDrawable().setColorFilter(new BlendModeColorFilter(reunion.getAvatarColor(), BlendMode.SRC_IN));
        } else {
            //noinspection deprecation
            holder.mReunionAvatar.getDrawable().setColorFilter(reunion.getAvatarColor() , PorterDuff.Mode.SRC_IN);
        }
        holder.mParticipantsList.setText(reunionParticipants(reunion.getParticipants()));


        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new DeleteReunionEvent(reunion));
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new ClickReunionEvent(reunion));
            }
        });
    }

    public static String reunionParticipants(List<Participant> participants) {
        StringBuilder reunionParticipants = new StringBuilder();
        for (int i = 0; i<participants.size(); i++){
            reunionParticipants.append(participants.get(i).getEmail());
            if (i<participants.size()-1){
                reunionParticipants.append(", ");
            }else {
                reunionParticipants.append(".");
            }
        }
        return reunionParticipants.toString();
    }

    @Override
    public int getItemCount() {
        return mReunions.size();
    }

   static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mReunionAvatar ;
        TextView mReunionInfo;
        TextView mParticipantsList;
        ImageButton mDeleteButton;

        ViewHolder(View view) {
            super(view);
            mReunionAvatar = view.findViewById(R.id.item_list_avatar);
            mReunionInfo = view.findViewById(R.id.item_list_info);
            mParticipantsList = view.findViewById(R.id.item_list_participant);
            mDeleteButton = view.findViewById(R.id.item_list_delete_button);
        }
    }
}
