
package com.example.recipeapp.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.recipeapp.entity.Recipe;
import com.example.recipeapp.entity.RecipeType;

import java.util.List;

@Dao
public interface RecipeTypeDao {
    @Query("SELECT * FROM recipetype")
    List<RecipeType> getAll();

    @Query("SELECT * FROM recipetype WHERE id == (:recipeTypeId)")
    RecipeType loadById(int recipeTypeId);



    @Insert
    void insertAll(List<RecipeType> recipeTypes);

    @Delete
    void delete(RecipeType recipeType);
}

