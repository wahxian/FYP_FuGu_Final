package com.minimalwaste.fypv2_fragments;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wah Xian on 16/06/2017.
 */

public class PersonalRecipeC {

    //Declare Variables inside Structure/Class
    private String _RecipeName;
    private List<String> _Ingredients;
    private String _Image;   //Need blobs to store image from gallery
    private String _Rating;

    //Constructor if we want to create object and assign values later
    public PersonalRecipeC() {
    }

    //Constructor to create object with assignment
    public PersonalRecipeC(String recipeName, List<String> ingredients, String image, String rating) {
        this._RecipeName = recipeName;
        this._Ingredients = ingredients;
        this._Image = image;
        this._Rating = rating;
    }

    //*********************************************************************************************
    //                                      Setters
    //*********************************************************************************************

    public void setRecipeName(String recipeName) {
        _RecipeName = recipeName;
    }

    public void setIngredients(List<String> ingredients) {
        _Ingredients = ingredients;
    }

    public void setImage(String image) {

        _Image = image;
    }

    public void setRating(String rating) {
        _Rating = rating;
    }

    //*********************************************************************************************
    //                                      Getters
    //*********************************************************************************************

    public String getRecipeName() {
        return _RecipeName;
    }

    public List<String> getIngredients() {
        return _Ingredients;
    }

    public String getImage() {
        return _Image;
    }

    public String getRating() {
        return _Rating;
    }
}

//*********************************************************************************************
//                                      Comments
//*********************************************************************************************
//1. Declare private variables used in class
//2. Create empty and assigned constructors for creating class objects
//3. Getters and Setters Function to change and obtain values in Class