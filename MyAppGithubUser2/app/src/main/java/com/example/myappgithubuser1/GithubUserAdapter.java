package com.example.myappgithubuser1;

import android.util.Log;
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

public class GithubUserAdapter extends RecyclerView.Adapter<GithubUserAdapter.ListViewHolder> {

    private ArrayList<GithubUser> listGithubUser;
    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
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

    public GithubUserAdapter(ArrayList<GithubUser> list) {
        this.listGithubUser = list;
    }

    @NonNull
    @Override
    public GithubUserAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_githubuser, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GithubUserAdapter.ListViewHolder holder, int position) {
        GithubUser githubUser = listGithubUser.get(position);
        Glide.with(holder.itemView.getContext())
                .load(githubUser.getAvatar())
                .apply(new RequestOptions().override(55, 55))
                .into(holder.imgPhoto);
        holder.txtName.setText(githubUser.getName());
        holder.txtCompany.setText(githubUser.getCompany());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listGithubUser.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listGithubUser.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(GithubUser data);
    }

}
