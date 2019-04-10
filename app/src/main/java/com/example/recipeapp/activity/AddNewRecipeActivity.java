package com.example.recipeapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.recipeapp.Adapter.RecipeTypeAdapter;
import com.example.recipeapp.R;
import com.example.recipeapp.dao.RecipeDao;
import com.example.recipeapp.dao.RecipeTypeDao;
import com.example.recipeapp.database.AppDatabase;
import com.example.recipeapp.entity.Recipe;
import com.example.recipeapp.entity.RecipeType;
import com.example.recipeapp.helper.Utils;

import java.util.List;

public class AddNewRecipeActivity extends AppCompatActivity {

    private static final int REQUEST_GET_SINGLE_FILE = 1;
    public static final String KEY_UPDATE_RECIPE_ID = "UPDATE";
    public static final String KEY_UPDATE_RECIPE_TYPE_ID = "UPDATE_TYPE";

    public static final String KEY_CREATE = "CREATE";

    private ImageView imgRecipe;
    private Uri uriImage;
    private Spinner spinnerRecipeType;

    private EditText edtStep;
    private EditText edtIngredients;
    private EditText edtRecipeName;

    private Button btnAddRecipe;
    private Button btnUpdateRecipe;
    private Button btnDeleteRecipe;

    private int recipeId;
    private int recipeTypeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe);

        imgRecipe = findViewById(R.id.add_new_imageView);
        spinnerRecipeType = findViewById(R.id.add_new_spinner_recipe_type);

        btnAddRecipe = findViewById(R.id.add_button);
        btnUpdateRecipe = findViewById(R.id.update_button);
        btnDeleteRecipe = findViewById(R.id.delete_button);

        edtRecipeName = findViewById(R.id.recipe_et_recipeName);
        edtIngredients = findViewById(R.id.recipe_et_ingredients);
        edtStep = findViewById(R.id.recipe_et_step);

        if (getIntent().hasExtra(KEY_CREATE)) {
            loadDataForCreateForm(this);
        } else if (getIntent().hasExtra(KEY_UPDATE_RECIPE_ID)) {
            recipeId = getIntent().getIntExtra(KEY_UPDATE_RECIPE_ID, -1);
            recipeTypeId = getIntent().getIntExtra(KEY_UPDATE_RECIPE_TYPE_ID, -1);

            loadDataForUpdateForm(this, recipeId, recipeTypeId);
        } else {
            throw new IllegalArgumentException("Activity cannot find your target ");

        }
    }

    // Load Data first time
    public void loadDataForCreateForm(Context context) {
        // Hide update and delete buton
        btnUpdateRecipe.setVisibility(View.GONE);
        btnDeleteRecipe.setVisibility(View.GONE);

        loadSpinnerRecipeType(context, null);
    }

    // Create new recipe action
    public void createNewRecipe(View view) {

        int recipeTypeId = ((RecipeType) spinnerRecipeType.getSelectedItem()).getId();
        String recipeName = edtRecipeName.getText().toString();
        String ingredients = edtIngredients.getText().toString();
        String step = edtStep.getText().toString();

        Utils utils = new Utils();
        if (utils.validateET(edtRecipeName, getResources()) && utils.validateET(edtIngredients, getResources()) && utils.validateET(edtStep, getResources())) {
            Recipe newRecipe = new Recipe();
            newRecipe.setRecipeTypeId(recipeTypeId);
            newRecipe.setRecipeName(recipeName);
            newRecipe.setRecipeIngredients(ingredients);
            newRecipe.setRecipeStep(step);

            String imgBase64 = Utils.uriToBase64(uriImage, this);
            if (!TextUtils.isEmpty(imgBase64)) {
                newRecipe.setRecipePic(imgBase64);
            }

            // Save to DB
            AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());
            RecipeDao recipeDao = database.recipeDao();
            recipeDao.insert(newRecipe);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.add_success), Toast.LENGTH_SHORT).show();

            setResult(RESULT_OK);
            finish();
        }

    }

    public void chooseImage(View view) {
        openGallery();
    }

    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GET_SINGLE_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_GET_SINGLE_FILE) {
                    // Uri selectedImageUri = data.getData();
                    uriImage = data.getData();
                    // Get the path from the Uri

                    // Set the image in ImageView

                    imgRecipe.setImageURI(uriImage);
                }
            }
        } catch (Exception e) {
            Log.e("FileSelectorActivity", "File select error", e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_GET_SINGLE_FILE:
                for (int permissionId : grantResults) {
                    if (permissionId != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getApplicationContext(), getString(R.string.permisiton_denied), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                openGallery();
                break;
        }
    }


    private void loadDataForUpdateForm(AddNewRecipeActivity addNewRecipeActivity, int recipeId, int recipeTypeId) {
        btnAddRecipe.setVisibility(View.GONE);


        // Get recipe
        AppDatabase database = AppDatabase.getAppDatabase(addNewRecipeActivity);
        RecipeDao recipeDao = database.recipeDao();
        RecipeTypeDao recipeTypeDao = database.recipeTypeDao();

        Recipe recipeFromDB = recipeDao.loadById(recipeId);
        RecipeType recipeTypeFromDB = recipeTypeDao.loadById(recipeTypeId);


        loadSpinnerRecipeType(addNewRecipeActivity, recipeTypeFromDB);

        edtStep.setText(recipeFromDB.getRecipeStep());
        edtIngredients.setText(recipeFromDB.getRecipeIngredients());
        edtRecipeName.setText(recipeFromDB.getRecipeName());
        if (TextUtils.isEmpty(recipeFromDB.getRecipePic())) {
            imgRecipe.setImageResource(R.drawable.ic_food_default);
        } else {
            imgRecipe.setImageBitmap(Utils.base64ToBitMap(recipeFromDB.getRecipePic()));
        }
    }

    // Update recipe action
    public void updateRecipe(View view) {

        //Get old object
        AppDatabase database = AppDatabase.getAppDatabase(view.getContext());
        RecipeDao recipeDao = database.recipeDao();
        Recipe recipeFromDB = recipeDao.loadById(recipeId);

        //Get image
        String imgBase64 = Utils.uriToBase64(uriImage, this);
        if (!TextUtils.isEmpty(imgBase64)) {
            recipeFromDB.setRecipePic(imgBase64);
        }

        //Get recipe id
        int recipeTypeId = ((RecipeType) spinnerRecipeType.getSelectedItem()).getId();

        recipeFromDB.setRecipeTypeId(recipeTypeId);
        recipeFromDB.setRecipeStep(edtStep.getText().toString());
        recipeFromDB.setRecipeIngredients(edtIngredients.getText().toString());
        recipeFromDB.setRecipeName(edtRecipeName.getText().toString());
        recipeDao.update(recipeFromDB);

        Toast.makeText(getApplicationContext(), getResources().getString(R.string.update_success), Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    // Delete recipe action
    public void deleteRecipe(View view) {
        AppDatabase database = AppDatabase.getAppDatabase(view.getContext());
        RecipeDao recipeDao = database.recipeDao();
        Recipe recipeFromDB = recipeDao.loadById(recipeId);
        if (recipeFromDB != null) {
            recipeDao.delete(recipeFromDB);
        }

        Toast.makeText(getApplicationContext(), getResources().getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();

    }

    public void loadSpinnerRecipeType(Context context, RecipeType recipeType) {
        AppDatabase database = AppDatabase.getAppDatabase(context);
        RecipeTypeDao recipeTypeDao = database.recipeTypeDao();
        List<RecipeType> listRecipeType = recipeTypeDao.getAll();

        int position = 0;
        if (recipeType != null) {
            for (int i = 0; i < listRecipeType.size(); i++) {
                if (listRecipeType.get(i).getId() == recipeType.getId()) {
                    position = i;
                }
            }
        }

        RecipeTypeAdapter recipeTypeAdapter = new RecipeTypeAdapter(this, R.layout.support_simple_spinner_dropdown_item, android.R.id.text1);
        recipeTypeAdapter.setRecipeType(listRecipeType);


        spinnerRecipeType.setAdapter(recipeTypeAdapter);
        spinnerRecipeType.setSelection(position);
    }


}
