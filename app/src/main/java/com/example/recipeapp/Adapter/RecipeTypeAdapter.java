package com.example.recipeapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.recipeapp.entity.RecipeType;

import java.util.ArrayList;
import java.util.List;

public class RecipeTypeAdapter extends ArrayAdapter<RecipeType> {
    private Context context;
    private List<RecipeType> lstRecipeType;

    public RecipeTypeAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        this.context = context;
        this.lstRecipeType = new ArrayList<>();
    }

    public void setRecipeType(List<RecipeType> lstRecipeType) {
        this.lstRecipeType.clear();
        this.lstRecipeType.addAll(lstRecipeType);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (lstRecipeType == null) return 0;
        return lstRecipeType.size();
    }

    @Nullable
    @Override
    public RecipeType getItem(int position) {
        return lstRecipeType.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(getItem(position).getRecipeType());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(getItem(position).getRecipeType());
        return label;
    }


}
