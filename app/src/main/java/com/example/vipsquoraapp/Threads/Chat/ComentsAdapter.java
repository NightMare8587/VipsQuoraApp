package com.example.vipsquoraapp.Threads.Chat;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vipsquoraapp.Account.MyAccount;
import com.example.vipsquoraapp.ProfileInfo.ShowUserProfile;
import com.example.vipsquoraapp.R;
import com.example.vipsquoraapp.Threads.FollowThreads.FollowThreadAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ComentsAdapter extends RecyclerView.Adapter<ComentsAdapter.Holder> {
    List<String> titleName;
    List<String> createdByName;
    List<String> threadIdName;
    List<String> authIdName;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    public ComentsAdapter(List<String> titleName, List<String> createdByName, List<String> threadIdName, List<String> authIdName) {
        this.titleName = titleName;
        this.createdByName = createdByName;
        this.threadIdName = threadIdName;
        this.authIdName = authIdName;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.comments_adapter,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.title.setText(titleName.get(position));
        holder.name.setText(createdByName.get(position));
        holder.name.setOnClickListener(click -> {
            if(holder.name.getText().equals("anonymous")){
                Toast.makeText(click.getContext(), "Can't open anonymous profile", Toast.LENGTH_SHORT).show();
            }
            else if(!authIdName.get(position).equals(String.valueOf(auth.getUid()))) {
                Intent intent = new Intent(click.getContext(), ShowUserProfile.class);
                intent.putExtra("authID",authIdName.get(position));
                click.getContext().startActivity(intent);
            }else
                click.getContext().startActivity(new Intent(click.getContext(), MyAccount.class));
        });
    }

    @Override
    public int getItemCount() {
        return threadIdName.size();
    }
    public class Holder extends RecyclerView.ViewHolder{
        TextView title,name;
        Button reply,report;
        public Holder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleCommentsCardView);
            name = itemView.findViewById(R.id.createdByCommentsCardView);
            reply = itemView.findViewById(R.id.replyCommentsCardView);
            report = itemView.findViewById(R.id.reportCommentsCardView);
        }
    }
}
