package com.sorezel.burritos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class AdaptadorGrid extends RecyclerView.Adapter<AdaptadorGrid.ViewH>{
    
    ArrayList<String> list;


    public AdaptadorGrid(ArrayList<String> list) {
        this.list = list;
    }

    @Override
    public ViewH onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        ViewH vh = new ViewH(v);
        return vh;
    }

    @Override
    public void onBindViewHolder( ViewH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewH extends RecyclerView.ViewHolder{

        public ViewH(View itemView) {
            super(itemView);
        }
    }
}
