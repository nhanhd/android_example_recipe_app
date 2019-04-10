package com.example.recipeapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.recipeapp.dao.RecipeDao;
import com.example.recipeapp.dao.RecipeTypeDao;
import com.example.recipeapp.entity.Recipe;
import com.example.recipeapp.entity.RecipeType;

@Database(entities = {Recipe.class, RecipeType.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract RecipeDao recipeDao();
    public abstract RecipeTypeDao recipeTypeDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "recipe-database")
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
