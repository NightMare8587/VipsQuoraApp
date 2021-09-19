package com.example.vipsquoraapp.HomeScreen.Fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vipsquoraapp.R;

import java.util.ArrayList;
import java.util.List;

public class ThreadCardView extends RecyclerView.Adapter<ThreadCardView.Holder> {
    List<String> title = new ArrayList<>();
    List<String> likesCount = new ArrayList<>();
    List<String> totalComments = new ArrayList<>();
    List<String> createdBy = new ArrayList<>();
    List<String> authID = new ArrayList<>();
    List<String> threadID = new ArrayList<>();

    public ThreadCardView(List<String> title, List<String> likesCount, List<String> totalComments, List<String> createdBy, List<String> authID, List<String> threadID) {
        this.title = title;
        this.likesCount = likesCount;
        this.totalComments = totalComments;
        this.createdBy = createdBy;
        this.authID = authID;
        this.threadID = threadID;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.thread_card_view,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.title.setText(title.get(position));
        holder.createdByText.setText(createdBy.get(position));
    }

    @Override
    public int getItemCount() {
        return threadID.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView title,createdByText;
        public Holder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleCardView);
            cardView = itemView.findViewById(R.id.threadCardView);
            createdByText = itemView.findViewById(R.id.createdByTestVieewCard);
        }
    }
}
