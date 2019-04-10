package com.example.recipeapp.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "recipe_type_id")
    public int recipeTypeId;

    @ColumnInfo(name = "recipe_name")
    public String recipeName;

    @ColumnInfo(name = "recipe_pic")
    public String recipePic;

    @ColumnInfo(name = "recipe_ingredients")
    public String recipeIngredients;

    @ColumnInfo(name = "recipe_step")
    public String recipeStep;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecipeTypeId() {
        return recipeTypeId;
    }

    public void setRecipeTypeId(int recipeTypeId) {
        this.recipeTypeId = recipeTypeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipePic() {
        return recipePic;
    }

    public void setRecipePic(String recipePic) {
        this.recipePic = recipePic;
    }

    public String getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(String recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public String getRecipeStep() {
        return recipeStep;
    }

    public void setRecipeStep(String recipeStep) {
        this.recipeStep = recipeStep;
    }
}
