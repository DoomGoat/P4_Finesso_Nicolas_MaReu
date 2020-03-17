package com.openclassroom.mareu.controller;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.openclassroom.mareu.R;
import com.openclassroom.mareu.model.Reunion;

import java.util.List;

public class MyReunionRecyclerViewAdapter extends RecyclerView.Adapter<MyReunionRecyclerViewAdapter.ViewHolder> {

    private final List<Reunion> mReunions;
    RoundImage roundedImage;

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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Reunion reunion = mReunions.get(position);

        holder.mReunionInfo.setText(reunion.getSubject());
        Bitmap bm = BitmapFactory.decodeResource(holder.mReunionAvatar.getResources(), R.drawable.ic_launcher_background); //TODO
        roundedImage = new RoundImage(bm);
        holder.mReunionAvatar.setImageDrawable(roundedImage);

        /**holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new DeleteNeighbourEvent(neighbour));
            }
        });*/

        /**holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new ClickNeighbourEvent(neighbour));
            }
        });*/
    }


    @Override
    public int getItemCount() {
        return mReunions.size();
    }

   public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_list_avatar)
        public ImageView mReunionAvatar;
        @BindView(R.id.item_list_reunion)
        public TextView mReunionInfo;
        @BindView(R.id.item_list_participant)
        public TextView mParticipantsList;
        @BindView(R.id.item_list_delete_button)
        public ImageButton mDeleteButton;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
