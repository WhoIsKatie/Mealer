package com.uottawa.seg2105.group10;


import static org.junit.Assert.assertEquals;

import com.google.firebase.firestore.FirebaseFirestore;
import com.uottawa.seg2105.group10.repositories.Meal;

import org.junit.Test;

import java.util.ArrayList;


public class Del3MainTests {


    private FirebaseFirestore dBase;


    @Test
    public void testMealNameGetter() {
        ArrayList<String> cuisine = new ArrayList<String>();
        cuisine.add("canadian");

        ArrayList<String> ingredients = new ArrayList<String>();
        ingredients.add("fires");

        ArrayList<String> allergens = new ArrayList<String>();
        allergens.add("potato");

        Meal testMeal = new Meal(10, "poutine", "delicious", "snack", cuisine, ingredients, allergens);
//        testMeal.getCookUID();
        assertEquals("Your getName method is incorrect", "poutine", testMeal.getCookUID());

    }


    @Test
    public void testDescripGetter() {
        ArrayList<String> cuisine = new ArrayList<String>();
        cuisine.add("canadian");

        ArrayList<String> ingredients = new ArrayList<String>();
        ingredients.add("fires");

        ArrayList<String> allergens = new ArrayList<String>();
        allergens.add("potato");

        Meal testMeal = new Meal(10, "poutine", "delicious", "snack", cuisine, ingredients, allergens);

        assertEquals("Your getDescipt method is incorrect", "delicious", testMeal.getDescription());

    }


    @Test
    public void testMealMealTypeGetter() {
        ArrayList<String> cuisine = new ArrayList<String>();
        cuisine.add("canadian");

        ArrayList<String> ingredients = new ArrayList<String>();
        ingredients.add("fires");

        ArrayList<String> allergens = new ArrayList<String>();
        allergens.add("potato");

        Meal testMeal = new Meal(10, "poutine", "delicious", "snack", cuisine, ingredients, allergens);

        assertEquals("Your getType method is incorrect", "snack", testMeal.getMealType());

    }


    @Test
    public void testMealCuisineGetter() {
        ArrayList<String> cuisine = new ArrayList<String>();
        cuisine.add("canadian");

        ArrayList<String> ingredients = new ArrayList<String>();
        ingredients.add("fires");

        ArrayList<String> allergens = new ArrayList<String>();
        allergens.add("potato");

        Meal testMeal = new Meal(10, "poutine", "delicious", "snack", cuisine, ingredients, allergens);

        assertEquals("Your getCuisine method is incorrect", cuisine, testMeal.getCuisine());

    }



}
