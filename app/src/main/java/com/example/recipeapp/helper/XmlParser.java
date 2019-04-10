package com.example.recipeapp.helper;

import android.content.Context;
import android.util.Log;

import com.example.recipeapp.dao.RecipeDao;
import com.example.recipeapp.dao.RecipeTypeDao;
import com.example.recipeapp.database.AppDatabase;
import com.example.recipeapp.entity.Recipe;
import com.example.recipeapp.entity.RecipeType;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlParser {
    private static final String NS = null;

    private static final String START_TAG_VALUE = "recipetypes";
    private static final String TAGET_TAG_VALUE = "recipetype";


    public List<RecipeType> parse(Context context) throws XmlPullParserException, IOException {

        List<RecipeType> listRecipeTypeFromDB = getRecipeTypeFromDB(context);
        if (listRecipeTypeFromDB != null && listRecipeTypeFromDB.size() > 0) {
            // If data availabe on DB, get data from DB
            return listRecipeTypeFromDB;
        } else {
            InputStream inputStream = null;
            try {
                inputStream = context.getAssets().open("recipetypes.xml");
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(inputStream, null);
                parser.nextTag();

                return readRecipes(parser, context);

            } finally {
                inputStream.close();
            }
        }
    }

    private List readRecipes(XmlPullParser parser, Context context) throws XmlPullParserException, IOException {
        List recipes = new ArrayList();

        parser.require(XmlPullParser.START_TAG, NS, START_TAG_VALUE);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            int id = Integer.parseInt(parser.getAttributeValue(null, "id"));
            String name = parser.getName();
            if (name.equals(TAGET_TAG_VALUE)) {
                recipes.add(new RecipeType(id, readName(parser)));
            } else {
                skip(parser);
            }
        }
        storeDataToDB(recipes, context);
        return recipes;
    }

    private List<RecipeType> getRecipeTypeFromDB(Context context) {
        AppDatabase database = AppDatabase.getAppDatabase(context);
        RecipeTypeDao recipeTypeDao = database.recipeTypeDao();
        return recipeTypeDao.getAll();
    }

    private void storeDataToDB(List<RecipeType> recipeTypes, Context context) {
        AppDatabase database = AppDatabase.getAppDatabase(context);
        RecipeTypeDao recipeTypeDao = database.recipeTypeDao();
        RecipeDao recipeDao = database.recipeDao();
        recipeTypeDao.insertAll(recipeTypes);
        recipeDao.insertAll(createSampleRecipesData(recipeTypes));
    }

    private List<Recipe> createSampleRecipesData(List<RecipeType> recipeTypes) {
        List<Recipe> listRecipe = new ArrayList<Recipe>();
        for (int i = 0; i < recipeTypes.size(); i++) {
            if (i == 0) {
                Recipe sampleRecipe = new Recipe();
                sampleRecipe.setRecipeName("Banana");
                sampleRecipe.setRecipeStep("Cut then eat");
                sampleRecipe.setRecipeIngredients("Banana, walter");
                sampleRecipe.setRecipePic("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");
                sampleRecipe.setRecipeTypeId(recipeTypes.get(i).getId());

                Recipe sampleRecipe2 = new Recipe();
                sampleRecipe2.setRecipeName("Salt");
                sampleRecipe2.setRecipeStep("eat");
                sampleRecipe2.setRecipeIngredients("Salt");
                sampleRecipe2.setRecipeTypeId(recipeTypes.get(i).getId());

                Recipe sampleRecipe3 = new Recipe();
                sampleRecipe3.setRecipeName("Sugar");
                sampleRecipe3.setRecipeStep("eat");
                sampleRecipe3.setRecipeName("Sugar");
                sampleRecipe3.setRecipeTypeId(recipeTypes.get(i).getId());

                listRecipe.add(sampleRecipe);
                listRecipe.add(sampleRecipe2);
                listRecipe.add(sampleRecipe3);
            } else {
                Recipe sampleRecipe = new Recipe();
                sampleRecipe.setRecipeName("Food");
                sampleRecipe.setRecipeStep("Cook then eat");
                sampleRecipe.setRecipeIngredients("food, walter");
                sampleRecipe.setRecipePic("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");
                sampleRecipe.setRecipeTypeId(recipeTypes.get(i).getId());

                Recipe sampleRecipe2 = new Recipe();
                sampleRecipe2.setRecipeName("Salt");
                sampleRecipe2.setRecipeStep("eat");
                sampleRecipe2.setRecipeIngredients("Salt");
                sampleRecipe2.setRecipeTypeId(recipeTypes.get(i).getId());

                Recipe sampleRecipe3 = new Recipe();
                sampleRecipe3.setRecipeName("Sugar");
                sampleRecipe3.setRecipeStep("eat");
                sampleRecipe3.setRecipeName("Sugar");
                sampleRecipe3.setRecipeTypeId(recipeTypes.get(i).getId());

                listRecipe.add(sampleRecipe);
                listRecipe.add(sampleRecipe2);
                listRecipe.add(sampleRecipe3);
            }

        }
        return listRecipe;
    }




    private String readName(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NS, TAGET_TAG_VALUE);
        String name = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String elName = parser.getName();
            if ("name".equals(elName)) {
                name = parser.nextText();
            }
        }
        return name;
    }


    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}

