package com.minimalwaste.fypv2_fragments;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Wah Xian on 28/05/2017.
 */

class HRecipeAdapter extends RecyclerView.Adapter<HRecipeAdapter.ViewHolder> {
    private ArrayList<Integer> mDataset;

    public HRecipeAdapter(ArrayList<Integer> mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_horizontal_model,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTitle.setImageResource(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView mTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitle = (ImageView) itemView.findViewById(R.id.IVRHZ);
        }
    }
}
