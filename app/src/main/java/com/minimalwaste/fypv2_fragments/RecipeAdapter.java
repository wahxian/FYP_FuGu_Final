package com.minimalwaste.fypv2_fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Wah Xian on 28/05/2017.
 */

public class RecipeAdapter extends ArrayAdapter<String> {


    String[] Names;
    String[] Image;
    String[] Rating;
    Context mContext;
    Integer[] Type;
    LayoutInflater mInflater;

//    public RecipeAdapter(@NonNull Context context, String[] recipeImage, String[] recipeNames,String[] recipeID, String[] recipeRating) {

    public RecipeAdapter(@NonNull Context context, String[] recipeImage, String[] recipeNames, String[] recipeRating, Integer[] type) {

        super(context, R.layout.list_format);
        this.Image = recipeImage;
        this.Names = recipeNames;
//        this.ID = recipeID;
        this.Rating = recipeRating;
        this.mContext = context;
        this.Type = type;       //0 = personal recipe, 1 = Yummly recipe
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
            mViewHolder.mRRating = (ImageView) convertView.findViewById(R.id.IVRLRating);
            mViewHolder.mRType = (ImageView) convertView.findViewById(R.id.IVLFtype);

            convertView.setTag(mViewHolder);
        }else {
            mViewHolder = (ViewHolder)convertView.getTag();
        }

        mViewHolder.mRName.setText(Names[position]);

        if(Type[position] == 1){
            Picasso.with(getContext()).load(Image[position]).into(mViewHolder.mRImage);


            mViewHolder.mRType.setImageDrawable(getContext().getResources().getDrawable(R.drawable.yummly));

        }else {
            //insert Image
            String base64Image2 = Image[position];

            //Decode base 64 string Image to bitmap to bitmap image
            byte[] decodedString2 = Base64.decode(base64Image2, Base64.DEFAULT);
            Bitmap base64Bitmap2 = BitmapFactory.decodeByteArray(decodedString2, 0,
                    decodedString2.length);

            mViewHolder.mRImage.setImageBitmap(base64Bitmap2);

            mViewHolder.mRType.setImageDrawable(getContext().getResources().getDrawable(R.drawable.myrecipe2));

        }


        int id = mContext.getResources().getIdentifier("com.minimalwaste.fypv2_fragments:drawable/" + Rating[position], null, null);
        mViewHolder.mRRating.setImageResource(id);

        return convertView;
    }

    private class ViewHolder {
        ImageView mRImage;
        TextView mRName;
        ImageView mRRating;
        ImageView mRType;
    }
}