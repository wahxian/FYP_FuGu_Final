package com.minimalwaste.fypv2_fragments;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Wah Xian on 27/05/2017.
 */

public class MyAdapter extends ArrayAdapter<String>{
    String[] Names;
    int[] Index;
    Context mContext;
    LayoutInflater mInflater;

    public MyAdapter(@NonNull Context context, String[] recipeNames, int[] recipeIndex) {
        super(context, R.layout.list_format);
        this.Names = recipeNames;
        this.Index = recipeIndex;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return Names.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if(convertView==null) {
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_format, parent, false);
            mViewHolder.mRImage = (ImageView) convertView.findViewById(R.id.IVListFormat);
            mViewHolder.mRName = (TextView) convertView.findViewById(R.id.TVRLName);
            convertView.setTag(mViewHolder);
        }else {
            mViewHolder = (ViewHolder)convertView.getTag();
        }
        mViewHolder.mRImage.setImageResource(Index[position]);
        mViewHolder.mRName.setText(Names[position]);
        return convertView;
    }

    private class ViewHolder {
        ImageView mRImage;
        TextView mRName;
    }
}
