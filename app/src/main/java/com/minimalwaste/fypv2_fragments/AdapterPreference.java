package com.minimalwaste.fypv2_fragments;

import android.content.Context;
import android.media.Image;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.zip.Inflater;

import static android.R.attr.resource;

/**
 * Created by Wah Xian on 15/06/2017.
 */

public class AdapterPreference extends ArrayAdapter<String> {

    //-----Declare Variables to build Grid View of Preferences (Images and Text)-----
    String[] Cuisine;
    Context Context;

    //-----Layout Inflater to convert XML layout into editable Layout-----
    LayoutInflater mInflater;

    public AdapterPreference(@NonNull Context context, String[] cuisine) {
        //-----Define XML Layout model to this adapter-----
        super(context, R.layout.model_preference);

        //-----Constructor methods-----
        this.Context = context;
        this.Cuisine = cuisine;

    }

    @Override
    public int getCount() {
        return Cuisine.length;
    }

    //-----Create Viewholder to hold view of XML Model
    private class ViewHolderAP {
        TextView TVMP;
        ImageView IVMP;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //----------------------------------------------------------------------------------------
        //          Use ViewHolder to hold Text and Image Views of cuisine
        //----------------------------------------------------------------------------------------

        ViewHolderAP mViewholder = new ViewHolderAP();  //Instantiated class has m

        //-----Use Layout Inflater to get layout from XML model and put into convertView
        if(convertView==null){
            mInflater = (LayoutInflater) Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.model_preference, parent, false);

            //-----Find the views in Model XML------
            mViewholder.TVMP = (TextView) convertView.findViewById(R.id.TVMP);
            mViewholder.IVMP = (ImageView) convertView.findViewById(R.id.IVMP);

            //-----Set tag as these views-----
            convertView.setTag(mViewholder);

        }else{
            mViewholder = (ViewHolderAP)convertView.getTag();
        }

        //----------------------------------------------------------------------------------------
        //          Make Changes to Views
        //----------------------------------------------------------------------------------------

        mViewholder.TVMP.setText(Cuisine[position]);
        //Set Image
        int id = Context.getResources().getIdentifier("com.minimalwaste.fypv2_fragments:drawable/" + Cuisine[position], null, null);
        mViewholder.IVMP.setImageResource(id);

        return convertView;

    }
}