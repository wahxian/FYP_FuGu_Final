package com.minimalwaste.fypv2_fragments;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Wah Xian on 27/05/2017.
 */

public class ItemAdapter extends ArrayAdapter<String> {

    //--Instantiate inflator to convert xml to processable data
    LayoutInflater iInflator;

    //Variables to be passed into instantiation
//    List<String> iItem = new ArrayList<>();
//    List<String> iBrand = new ArrayList<>();
//    List<String> iDate = new ArrayList<>();
//    List<String> iPrice = new ArrayList<>();

    String[] iItem;
    String[] iBrand;
    String[] iDate;
    String[] iPrice;

    Context iContext;

    //--Constructor Model
//    public ItemAdapter(@NonNull Context context, List<String> iItem,List<String> iBrand, List<String> iDate, List<String> iPrice) {
    public ItemAdapter(@NonNull Context context, String[] iItem, String[] iBrand, String[] iDate, String[] iPrice) {
        super(context, R.layout.item_list_model);
        this.iContext= context;
        this.iItem = iItem;
        this.iBrand = iBrand;
        this.iDate = iDate;
        this.iPrice = iPrice;

    }

    //--Get the length of the arrays (How many Items in list)

    @Override
    public int getCount() {
//        return iItem.size();
        return iItem.length;
    }


    //--Creating getView to get the views and set the items into the position

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolderItem viewHolderItem = new ViewHolderItem();
        if(convertView==null) {
            iInflator = (LayoutInflater) iContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = iInflator.inflate(R.layout.item_list_model, parent, false);

            //--FIND XML views
            viewHolderItem.TVHItem = (TextView) convertView.findViewById(R.id.TVIMItem);
            viewHolderItem.TVHBrand = (TextView) convertView.findViewById(R.id.TVIMBrand);
            viewHolderItem.TVHDate = (TextView) convertView.findViewById(R.id.TVIMDate);
            viewHolderItem.TVHPrice = (TextView) convertView.findViewById(R.id.TVIMPrice);
            viewHolderItem.IVILMname = (ImageView) convertView.findViewById(R.id.IVILMname);

            convertView.setTag(viewHolderItem);

        }else {

            viewHolderItem = (ViewHolderItem)convertView.getTag();

        }

        //------------Putting Items into Lists-----------------
        //Image
//        try{
//            String temp = iItem[position].toLowerCase();
//        }catch(Exception e){
//            Log.d("", "getView: Faile COnverting to Lower Case");
//        }
        int id = iContext.getResources().getIdentifier("com.minimalwaste.fypv2_fragments:drawable/" + iItem[position].toLowerCase(), null, null);
        viewHolderItem.IVILMname.setImageResource(id);

        viewHolderItem.TVHItem.setText(iItem[position]);
        viewHolderItem.TVHBrand.setText(iBrand[position]);
        if(Integer.parseInt(iDate[position])<0){
            StringBuilder sb=new StringBuilder(iDate[position]);
            sb.deleteCharAt(0);
            String expireDays = sb.toString();
            viewHolderItem.TVHDate.setText("Expired for " + expireDays + " days");
        }else{
            viewHolderItem.TVHDate.setText(iDate[position]);
        }
        viewHolderItem.TVHPrice.setText(iPrice[position]);

        return convertView;
    }

    //--ViewHolder for the Items Layout
    private class ViewHolderItem {
        TextView TVHItem;   //TextView Holder for item
        TextView TVHBrand;
        TextView TVHDate;
        TextView TVHPrice;
        ImageView IVILMname;
    }
}
