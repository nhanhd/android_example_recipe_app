package com.example.recipeapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recipeapp.R;
import com.example.recipeapp.activity.AddNewRecipeActivity;
import com.example.recipeapp.entity.Recipe;
import com.example.recipeapp.helper.Utils;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder> {
    private List<Recipe> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView txtName;
        TextView txtIngredients;
        ImageView image;

        MyViewHolder(View v) {
            super(v);
            txtName = v.findViewById(R.id.recipe_item_txt_recipe_name);
            txtIngredients = v.findViewById(R.id.recipe_item_txt_ingredients);
            image = v.findViewById(R.id.recipe_item_img_view);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecipeAdapter(List<Recipe> myDataset) {
        mDataset = myDataset;

    }

    public void setData(List<Recipe> data) {
        mDataset.clear();
        mDataset.addAll(data);
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecipeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_item_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final int recipeId = mDataset.get(position).getId();
        final int recipeTypeId = mDataset.get(position).getRecipeTypeId();
        holder.txtName.setText(mDataset.get(position).getRecipeName());
        holder.txtIngredients.setText(mDataset.get(position).getRecipeIngredients());
        Bitmap bitmap = Utils.base64ToBitMap(mDataset.get(position).getRecipePic());

        if (bitmap != null) {
            holder.image.setImageBitmap(bitmap);
        } else {
            holder.image.setImageResource(R.drawable.ic_food_default);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRecipe(v.getContext(), recipeId, recipeTypeId);
            }
        });

        //  Uri.parse(new File(mDataset.get(position).getRecipePic()).toString())

    }


    private void onClickRecipe(Context context, int recipeId, int recipeTypeId) {
        Intent intent = new Intent(context, AddNewRecipeActivity.class);
        intent.putExtra(AddNewRecipeActivity.KEY_UPDATE_RECIPE_ID, recipeId);
        intent.putExtra(AddNewRecipeActivity.KEY_UPDATE_RECIPE_TYPE_ID, recipeTypeId);
//        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, 1);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
