package com.example.recipeapp.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity
public class RecipeType {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "recipe_type")
    public String recipeType;

    public RecipeType(int id, String recipeType) {
        this.id = id;
        this.recipeType = recipeType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecipeType() {
        return recipeType;
    }

    public void setRecipeType(String recipeType) {
        this.recipeType = recipeType;
    }


}
