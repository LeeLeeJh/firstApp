
package com.example.firstapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.MyHolder> {

    List<comment_info> clist = new ArrayList<>();
    ItemClickListener itemClickListener;

    public commentAdapter(List<comment_info> clist){
        this.clist = clist;
    }



    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {






        final comment_info comment_info = clist.get(position);

        holder.comment_name.setText(comment_info.getName());
        holder.comment_insert.setText(comment_info.getInsert());



     //   holder.comment_profile.setImageURI(comment_info.getProfile());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.OnItemClick(position,comment_info);
            }
        });

        /*holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clist.remove(position);
                notifyDataSetChanged();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return clist.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder{

        TextView comment_name,comment_insert,comment_profile;

        public MyHolder(View itemView) {
            super(itemView);

            comment_name = itemView.findViewById(R.id.comment_name);
            comment_insert = itemView.findViewById(R.id.textView_comment);
            comment_profile = itemView.findViewById(R.id.myprofilep);
        }
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public void UpdateData(int position,comment_info comment_info){

        clist.remove(position);
        clist.add(comment_info);
        notifyItemChanged(position);
        notifyDataSetChanged();
    }

}

