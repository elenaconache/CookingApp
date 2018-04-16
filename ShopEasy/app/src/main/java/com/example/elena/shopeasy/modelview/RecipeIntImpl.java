package com.example.elena.shopeasy.modelview;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.elena.shopeasy.RetrofitService;
import com.example.elena.shopeasy.model.Recipe;
import com.example.elena.shopeasy.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by User on 31/12/2017.
 */

public class RecipeIntImpl implements RecipeInt {
    private RetrofitService mRetrofitService;

    public RecipeIntImpl(){
    }

    public RecipeIntImpl(Retrofit retrofit){
        mRetrofitService = retrofit.create(RetrofitService.class);//keep this
    }

    @Override
    public MutableLiveData<List<Recipe>> getRecipes(final Context context) {
        final MutableLiveData<List<Recipe>> liveData = new MutableLiveData<>();
        Call<List<Recipe>> call = mRetrofitService.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call,
                                   @NonNull Response<List<Recipe>> response) {
                List<Recipe> recipesList = new ArrayList<>();
              //  Log.v("body vs err",response.body()+" "+response.errorBody());
                if (response.body() != null){
                    recipesList.addAll(response.body());
                    liveData.setValue(recipesList);
                    RecipeViewModel.handleResponse(recipesList, context, new MainActivity());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                 t.printStackTrace();
                 Log.v("error-getting-categ",t.toString());
            }
        });
        return liveData;
    }
}
