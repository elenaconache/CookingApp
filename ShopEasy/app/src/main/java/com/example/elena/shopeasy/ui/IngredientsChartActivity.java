package com.example.elena.shopeasy.ui;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.elena.shopeasy.R;
import com.example.elena.shopeasy.model.IngredientInput;
import com.example.elena.shopeasy.realm.RecipeInput;
import com.example.elena.shopeasy.realm.RealmController;
import com.example.elena.shopeasy.utils.PrefUtils;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Collections;

import io.realm.RealmResults;

public class IngredientsChartActivity extends AppCompatActivity {

    private TextView mTopTextView;

    private static final int[] MY_COLORS = {
            Color.rgb(0,0,255),
            //Color.rgb(128,128,128),
            Color.rgb(128,0,0),
            Color.rgb(0,128,0),
            Color.rgb(128,0,128),
            Color.rgb(220,20,60),
            Color.rgb(205,92,92),
            Color.rgb(255,140,0),
            Color.rgb(218,165,32),
            Color.rgb(154,205,50),
            Color.rgb(143,188,143),
            Color.rgb(199,21,133)
    };

    private PieChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_chart);

        mTopTextView = findViewById(R.id.text_top_ingredients);
        chart = findViewById(R.id.chart);

        setupChart();
    }

    private void setupChart(){

        final RealmResults<RecipeInput> recipeInputs = RealmController.with(this).getRecipeInputs();
        final ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<IngredientInput> ingredientInputs = getIngredientInputs(recipeInputs);
        ingredientInputs = orderIngredients(ingredientInputs);

        for (IngredientInput input: ingredientInputs){
            entries.add(new PieEntry(input.getmFrequency(), input.getmName()));
        }

        final PieDataSet pieDataSet = new PieDataSet(entries,"");

        pieDataSet.setSelectionShift(10f);

        ArrayList<Integer> colors = new ArrayList<Integer>();
        for(int c: MY_COLORS) colors.add(c);
        pieDataSet.setColors(colors);

        PieData data = new PieData(pieDataSet);
        data.setValueTextSize(18);
        data.setValueTextColor(Color.WHITE);
        data.setValueFormatter(new PercentFormatter());
        chart.setData(data);
        chart.setHoleRadius(30f);
        chart.setTransparentCircleRadius(45f);
        chart.setTransparentCircleAlpha(95);
        chart.setCenterText("Added\nIngredients");
        chart.setCenterTextSize(14f);
        chart.setEntryLabelColor(0);//labels disappear from chart

        Description description = new Description();
        description.setText("");
        chart.setDescription(description);

        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setDragDecelerationFrictionCoef(0.9f);
        chart.setDrawHoleEnabled(true);
        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        chart.animateY(1200, Easing.EasingOption.EaseInOutQuad);

        Legend l = chart.getLegend();
        l.setTextSize(14f);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setWordWrapEnabled(true);
    }

    private int isExistingIngredient(ArrayList<IngredientInput> list, String name){
        for (int i=0; i<list.size(); i++){
            if (list.get(i).getmName().equals(name)) return i;
        }
        return  -1;
    }

    private ArrayList<IngredientInput> orderIngredients(ArrayList<IngredientInput> ingredientInputs) {

        for (int i=0; i<ingredientInputs.size()-1;i++){
            for (int j=i+1; j<ingredientInputs.size(); j++){
                if (ingredientInputs.get(i).getmFrequency()<ingredientInputs.get(j).getmFrequency()){
                    Collections.swap(ingredientInputs, i, j);
                }
            }
        }

        int maxTopCount= new PrefUtils(this).getTopCount();

        ArrayList<IngredientInput> returnedList = new ArrayList<>();
        int min= Math.min(maxTopCount,ingredientInputs.size());
        for (int i=0; i<min; i++) returnedList.add(ingredientInputs.get(i));
        if (min>0){
            mTopTextView.setText("Top "+min+" ingredients");
            mTopTextView.setCompoundDrawables(null, null, null, null);
        //    mTopTextView.setGravity(Gravity.TOP);
            chart.setVisibility(View.VISIBLE);
        }else{
            chart.setVisibility(View.GONE);
          //  mTopTextView.setGravity(Gravity.CENTER);
            mTopTextView.setText(R.string.text_add_recipes_message);
            mTopTextView.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    getResources().getDrawable(R.drawable.cakenodata),
                    null,
                    null);
            mTopTextView.setCompoundDrawablePadding(5);
        }

        return returnedList;
    }

    private ArrayList<IngredientInput> getIngredientInputs(RealmResults<RecipeInput> recipeInputs){
        ArrayList<IngredientInput> ingredientInputs = new ArrayList<>();
        for (RecipeInput input: recipeInputs){
            String[] ingredients = input.getmIngredients().split("\n");
            for (String ingredient : ingredients) {
                if (isExistingIngredient(ingredientInputs, ingredient) != -1) {
                    //if already added, increase frequency
                    int index = isExistingIngredient(ingredientInputs, ingredient);
                    int oldFrequency = ingredientInputs.get(index).getmFrequency();
                    ingredientInputs.get(index).setmFrequency(oldFrequency + 1);
                } else if (ingredient.length() > 0) {
                    ingredientInputs.add(new IngredientInput(ingredient, 1));
                }
            }
        }
        return  ingredientInputs;
    }

}
