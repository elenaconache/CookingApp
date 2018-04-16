package com.example.elena.shopeasy;

import com.example.elena.shopeasy.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Absolute on 2/1/2018.
 */

public interface RetrofitService {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes();

}
