package com.example.recipeapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.recipeapp.Adapter.RecipeAdapter;
import com.example.recipeapp.Adapter.RecipeTypeAdapter;
import com.example.recipeapp.R;
import com.example.recipeapp.dao.RecipeDao;
import com.example.recipeapp.database.AppDatabase;
import com.example.recipeapp.entity.Recipe;
import com.example.recipeapp.entity.RecipeType;
import com.example.recipeapp.helper.XmlParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;

    private Spinner recipeTypeSpinner;
    private RecyclerView recipeRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private RecipeTypeAdapter recipeTypeAdapter;
    private RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpRecycleView();
        setUpSpinner();
    }

    public void setUpRecycleView() {
        recipeRecyclerView = findViewById(R.id.main_recipe_recycler_view);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recipeRecyclerView.setLayoutManager(layoutManager);

        recipeAdapter = new RecipeAdapter(getRecipeData(0));
        recipeRecyclerView.setAdapter(recipeAdapter);

    }

    public void setUpSpinner() {
        recipeTypeSpinner = findViewById(R.id.recipe_spinner_main);
        recipeTypeAdapter = new RecipeTypeAdapter(this, R.layout.support_simple_spinner_dropdown_item, android.R.id.text1);
        recipeTypeAdapter.setRecipeType(readXML());

        // Recipe Type Spinner
        recipeTypeSpinner.setAdapter(recipeTypeAdapter);
        recipeTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RecipeType recipeType = (RecipeType) parent.getItemAtPosition(position);
                recipeAdapter.setData(getRecipeData(recipeType.getId()));

                //recipeRecyclerView.setAdapter(recipeAdapter);
                //recipeTypeId = recipeType.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public List<Recipe> getRecipeData(int recipeTypeId) {
        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());
//        recipeAdapter = new RecipeAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1);
        final RecipeDao recipeDao = database.recipeDao();

        // Create new list when change recipe type
        return recipeDao.loadAllByRecipeTypeIds(recipeTypeId);
    }
    public void addRecipe(View view) {
        Intent intent = new Intent(this, AddNewRecipeActivity.class);
        intent.putExtra(AddNewRecipeActivity.KEY_CREATE, "");
        startActivityForResult(intent, REQUEST_CODE);
    }

    public List<RecipeType> readXML() {
        try {
            XmlParser xmlParser = new XmlParser();
            return xmlParser.parse(getApplicationContext());

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                int position = recipeTypeSpinner.getSelectedItemPosition();
                int id = recipeTypeAdapter.getItem(position).getId();
//                recipeAdapter.setRecipe(getRecipeData(id));
                recipeAdapter.setData(getRecipeData(id));
            }
        }
    }
}
