package com.example.myappgithubuser1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.ListViewHolder> {
    private ArrayList<GithubUser> listFollowing;
    private GithubUserAdapter.OnItemClickCallback onItemClickCallback;

    public FollowingAdapter(ArrayList<GithubUser> list) {
        this.listFollowing = list;
    }

    public void setOnItemClickCallback(GithubUserAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public FollowingAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_githubuser, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowingAdapter.ListViewHolder holder, int position) {
        GithubUser githubUser = listFollowing.get(position);
        Glide.with(holder.itemView.getContext())
                .load(githubUser.getAvatar())
                .apply(new RequestOptions().override(55, 55))
                .into(holder.imgPhoto);
        holder.txtName.setText(githubUser.getName());
        holder.txtCompany.setText(githubUser.getCompany());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //NO ACTION
            }
        });
    }

    @Override
    public int getItemCount() {
        return listFollowing.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView txtName, txtCompany;

        ListViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.name);
            txtCompany = itemView.findViewById(R.id.company);
            imgPhoto = itemView.findViewById(R.id.photo);
        }
    }
    
    public interface OnItemClickCallback {
        void onItemClicked(GithubUser data);
    }
}
