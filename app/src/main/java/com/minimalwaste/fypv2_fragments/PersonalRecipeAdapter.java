package com.minimalwaste.fypv2_fragments;

import android.support.v7.app.AppCompatActivity;
import android.widget.BaseAdapter;

/**
 * Created by Wah Xian on 17/06/2017.
 */

//public class PersonalRecipeAdapter extends BaseAdapter {
//
//    private Context context;
//    private  int layout;
//    private List<Food> foodsList;
//
//    public PersonalRecipeAdapter() {
//    }
//
//
//
//    public FoodListAdapter(Context context, int layout, ArrayList<Food> foodsList) {
//        this.context = context;
//        this.layout = layout;
//        this.foodsList = foodsList;
//    }
//
//    @Override
//    public int getCount() {
//        return foodsList.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return foodsList.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    private class ViewHolder{
//        ImageView imageView;
//        TextView txtName, txtPrice;
//    }
//
//    @Override
//    public View getView(int position, View view, ViewGroup viewGroup) {
//
//        View row = view;
//        ViewHolder holder = new ViewHolder();
//
//        if(row == null){
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            row = inflater.inflate(layout, null);
//
//            holder.txtName = (TextView) row.findViewById(R.id.txtName);
//            holder.txtPrice = (TextView) row.findViewById(R.id.txtPrice);
//            holder.imageView = (ImageView) row.findViewById(R.id.imgFood);
//            row.setTag(holder);
//        }
//        else {
//            holder = (ViewHolder) row.getTag();
//        }
//
//        Food food = foodsList.get(position);
//
//        holder.txtName.setText(food.getName());
//        holder.txtPrice.setText(food.getPrice());
//
//        byte[] foodImage = food.getImage();
//        Bitmap bitmap = BitmapFactory.decodeByteArray(foodImage, 0, foodImage.length);
//        holder.imageView.setImageBitmap(bitmap);
//
//        return row;
//    }
//}
