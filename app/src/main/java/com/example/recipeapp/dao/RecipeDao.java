package com.example.recipeapp.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.recipeapp.entity.Recipe;

import java.util.List;


@Dao
public interface RecipeDao {
    @Query("SELECT * FROM recipe")
    List<Recipe> getAll();

    @Query("SELECT * FROM recipe WHERE id == (:recipeId)")
    Recipe loadById(int recipeId);

    @Query("SELECT * FROM recipe WHERE recipe_type_id == (:recipeTypeId)")
    List<Recipe> loadAllByRecipeTypeIds(int recipeTypeId);


    @Insert
    void insertAll(List<Recipe> recipes);

    @Insert
    void insert(Recipe recipe);

    @Delete
    void delete(Recipe recipe);

    @Update
    void update(Recipe recipe);
}

