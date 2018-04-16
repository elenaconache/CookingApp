package com.example.elena.shopeasy.ui;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.elena.shopeasy.App;
import com.example.elena.shopeasy.GlideApp;
import com.example.elena.shopeasy.R;
import com.example.elena.shopeasy.adapters.HomeFragmentPagerAdapter;
import com.example.elena.shopeasy.adapters.MainDrawerAdapter;
import com.example.elena.shopeasy.adapters.SuggestionsListAdapter;
import com.example.elena.shopeasy.customViews.CustomEditText;
import com.example.elena.shopeasy.fragments.CategoriesFragment;
import com.example.elena.shopeasy.fragments.FooterFragment;
import com.example.elena.shopeasy.fragments.HomeFragment;
import com.example.elena.shopeasy.model.Ingredient;
import com.example.elena.shopeasy.model.Recipe;
import com.example.elena.shopeasy.realm.RecipeInput;
import com.example.elena.shopeasy.modelview.RecipeViewModel;
import com.example.elena.shopeasy.realm.RealmController;
import com.example.elena.shopeasy.receiver.AlarmReceiver;
import com.example.elena.shopeasy.utils.BitmapUtils;
import com.example.elena.shopeasy.utils.PrefUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.RealmResults;
import retrofit2.Retrofit;

import static com.example.elena.shopeasy.ui.RecipesActivity.EXTRA_RECIPE;
import static com.example.elena.shopeasy.ui.RecipesActivity.EXTRA_RECIPE_PARTIAL_NAME;

public class MainActivity extends AppCompatActivity
        implements FooterFragment.FragmentFooterListener, SearchView.OnQueryTextListener,
        HomeFragment.OnHomeCellInteractionListener{

    private static SearchView mSearchViewProduct;
    private ViewPager mViewPagerHome;
    private TabLayout mTabLayoutHome;

    private RecipeViewModel mRecipeModel;
    @Inject
    Retrofit retrofit;//dagger does not support injection into private fields

    private static ListView listSuggestions;
    private static SuggestionsListAdapter adapterSuggestions;
    private static List<String> searchList=new ArrayList<>();

    private boolean mSearching;

    private String[] mDrawerTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private TextView mDrawerAccountTextView;

    private PrefUtils prefUtils;

    private static CircleImageView mProfileImageView;

    private static List<Recipe> mRecipes;

    private MainDrawerAdapter mDrawerAdapter;

    private List<String> mIngredientsAdded=new ArrayList<>();
    private String mRecipeNameAdded;

    private List<String> mFinalIngredients = new ArrayList<>();

    private FirebaseDatabase mRecipesDatabase;
    private DatabaseReference mRecipesDatabaseReference;

    private List<Recipe> mAddedRecipes;

    static List<String> ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.wtf("mainactivity","oncreate");

        findViews();
        setupViewPagerTabs();

        prefUtils = new PrefUtils(this);

        setupFirebase();

        mRecipeModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        ((App) getApplication()).getNetComponent().injectMain(this);//dagger
        mRecipeModel.setRetrofit(retrofit);
        mRecipeModel.loadRecipes(this);

     //   adapterSuggestions  =
    //            new SuggestionsListAdapter(listSuggestions.getContext(),mRecipes, this);
      //  listSuggestions.setAdapter(adapterSuggestions);

        mSearchViewProduct.setSubmitButtonEnabled(true);
        mSearchViewProduct.setOnQueryTextListener(this);
        mSearchViewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearching = true;
                showSuggestions();
            }
        });

        mProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prefUtils.isAuthenticated()){
                    // custom dialog
                    final Dialog dialog = new Dialog(view.getContext());
                    dialog.setContentView(R.layout.dialog_profile_pic);
                    //dialog.setTitle("");

                    // set the custom dialog components
                    ImageView imageView = dialog.findViewById(R.id.image_profile);
                    String image = prefUtils.getFacebookImage();
                    BitmapUtils.getBitmapFromURL(image);
                    GlideApp.with(view.getContext()).load(mProfileImageView.getDrawable())
                            .placeholder(R.drawable.ic_not_interested_black_24dp).into(imageView);

                    ImageView cancelImage =  dialog.findViewById(R.id.image_close_profile);
                    // if button is clicked, close the custom dialog
                    cancelImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            }
        });

        mDrawerTitles = getResources().getStringArray(R.array.drawer_titles_array);
        // Set the adapter for the list view
        mDrawerAdapter = new MainDrawerAdapter(
                this,
                R.layout.item_drawer_main,
                mDrawerTitles);
        mDrawerList.setAdapter(mDrawerAdapter);

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = view.findViewById(R.id.text_drawer_option);

                Log.wtf("drawer","clicked "+textView.getText());
                if (textView.getText().toString().equals("New recipe idea")){
                    if (prefUtils.isAuthenticated()) showDialog();
                    else showDialogAuth();
                }else if (textView.getText().toString().equals("My ideas")){
                    if (!prefUtils.isAuthenticated()) showDialogAuth();
                    else{
                        Intent openAddedRecipesActivity = new Intent(mDrawerLayout.getContext(),
                                AddedRecipesActivity.class);
                        startActivity(openAddedRecipesActivity);
                    }
                }else if (textView.getText().toString().equals("Most used ingredients")){
                    if (!prefUtils.isAuthenticated()) showDialogAuth();
                    else {
                        Intent openIngredientsChartActivity = new Intent(mDrawerLayout.getContext(),
                                IngredientsChartActivity.class);
                        startActivity(openIngredientsChartActivity);
                    }
                }else if (textView.getText().toString().equals("Settings")){
                    Intent openSettingsIntent = new Intent(mDrawerLayout.getContext(),
                            SettingsActivity.class);
                    startActivity(openSettingsIntent);
                }
            }
        });

            //todo landscape layout [show differently the tabs, the grid, the drawer

        listSuggestions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView name = view.findViewById(R.id.name);
                Log.wtf("selected hint",name.getText().toString()+" text");
                mSearchViewProduct.setQuery(name.getText().toString(),true);
                hideSuggestions();
            }
        });

        setupNotification();


    }



    private void findViews(){
        mSearchViewProduct = findViewById(R.id.search_view_product);
        // Find the view pager that will allow the user to swipe between fragments
        mViewPagerHome = findViewById(R.id.viewpager_home);
        mTabLayoutHome = findViewById(R.id.tabs_home);
        listSuggestions = findViewById(R.id.list_suggestions);
        mDrawerLayout =  findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.list_drawer_personalize);
        mDrawerAccountTextView = findViewById(R.id.text_drawer_account);

        mProfileImageView = findViewById(R.id.image_drawer_account);
    }

    private void setupViewPagerTabs(){
        // Create an adapter that knows which fragment should be shown on each page
        HomeFragmentPagerAdapter adapter = new HomeFragmentPagerAdapter(this,
                getSupportFragmentManager());
        // Set the adapter onto the view pager
        mViewPagerHome.setAdapter(adapter);
        // Give the TabLayout the ViewPager
        mTabLayoutHome.setupWithViewPager(mViewPagerHome);

        hideSuggestions();
    }

    @Override
    public void onHome() {//for footer fragment
    }

    @Override
    public void onAccount() {//for footer fragment
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent openAccountActivity =  new Intent(MainActivity.this, AccountActivity.class);
                startActivity(openAccountActivity);
            }
        },480);//300ms = time of anim/pulse.xml
    }

    @Override
    public void onBook() {//for footer fragment
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent openAccountActivity =  new Intent(MainActivity.this,AddedRecipesActivity.class);
                startActivity(openAccountActivity);


            }
        },480);//300ms = time of anim/pulse.xml
    }

    public void onHomeClick(View view) {
        pulseView(view);
        onHome();
    }

    public void onBookClick(View view) {
        pulseView(view);
        onBook();
    }

    public void onAccountClick(View view) {
        pulseView(view);
        onAccount();
    }

    private void pulseView(View view){
        Animation pulse = AnimationUtils.loadAnimation(this,R.anim.pulse);
        view.startAnimation(pulse);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {//for searchview
        boolean complete = false;
        for (Recipe recipe: mRecipes){
            if (recipe.getName().toLowerCase().equals(query.toLowerCase())){
                Intent intent = new Intent(this,RecipeDetailsActivity.class);
                intent.putExtra(EXTRA_RECIPE,recipe);
                startActivity(intent);
                complete = true;
                break;
            }
        }

        if (!complete){
            Intent intent = new Intent(this,RecipesActivity.class);
            intent.putExtra(EXTRA_RECIPE_PARTIAL_NAME,query.toLowerCase());
            startActivity(intent);
        }

        return false;

    }

    @Override
    public boolean onQueryTextChange(String newText) {//for searchview
       // Log.wtf("search",newText);
        if (newText.length()==0){
           // Log.wtf("if","will not filter");
            hideSuggestions();
        }
        else{
           // Log.wtf("else","will filter");
            Log.wtf("filter-change",newText);
            if (adapterSuggestions!=null)
                adapterSuggestions.filter(newText);
            showSuggestions();
        }
      /*  if (adapterSuggestions!=null)
            for (Recipe recipe: adapterSuggestions.getFilteredRecipes()){
                Log.wtf("recipefilter",recipe.getName());
            }
            for (Recipe recipe: mRecipes){
                Log.wtf("recipenofilter",recipe.getName());
        }*/

        return false;
    }

    public static void setupSuggestions(List<Recipe> recipes){//todo get rid of activity param from all involved methods

        if (mRecipes!=null)mRecipes.clear();
        else mRecipes = new ArrayList<>();
        mRecipes = recipes;
        RealmResults<RecipeInput> addedRecipes = RealmController
                .with((Activity)mSearchViewProduct.getContext()).getRecipeInputs();
        for (RecipeInput input: addedRecipes){
            Recipe recipe= input.toRecipe();
            //  searchList.add(recipe.getName());
            mRecipes.add(recipe);
        }

        adapterSuggestions = new SuggestionsListAdapter(
                listSuggestions.getContext(),
                mRecipes,
                (Activity)mSearchViewProduct.getContext());
        listSuggestions.setAdapter(adapterSuggestions);

    }

    private void showSuggestions(){
        listSuggestions.setVisibility(View.VISIBLE);
    }

    private void hideSuggestions(){
        listSuggestions.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (mSearching){
            hideSuggestions();
            mSearching= false;
        }
        super.onBackPressed();
    }

    @Override
    public void onHomeCellInteraction(String text) {
        if (text.equals(getString(R.string.brands))){
            Intent openBrandsActivity = new Intent(this, BrandsActivity.class);
            startActivity(openBrandsActivity);
        }else if (text.equals(getString(R.string.personalize))){
            mDrawerLayout.openDrawer(Gravity.START);
        }else if (text.equals(getString(R.string.find_ingredients))) {//texts in home grid adapter
            Intent intent = new Intent(this, BestIngredientsActivity.class);
            startActivity(intent);
        }else{//get inspired
            Intent intent = new Intent(this, GetInspiredActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.wtf("mainactivity","onresume");
        hideSuggestions();

        mRecipeModel.loadRecipes(this);

        if (prefUtils.isAuthenticated()){
            mDrawerAccountTextView.setText(prefUtils.getFacebookUsername());
            String image = prefUtils.getFacebookImage();
            if (image.length()==0){
                Glide.with(this).load(R.drawable.ic_account_circle_black_24dp)
                        .into(mProfileImageView);
            }else{
                BitmapUtils.getBitmapFromURL(image);
            }
        }else{
            mDrawerAccountTextView.setText("Not Authenticated");

            Glide.with(this).load(R.drawable.ic_account_circle_black_24dp)
                        .into(mProfileImageView);

        }

        adapterSuggestions  = new SuggestionsListAdapter(
                listSuggestions.getContext(),
                mRecipes,
                this);
        listSuggestions.setAdapter(adapterSuggestions);

    }

    public static List<Recipe> getRecipes(){
        return mRecipes;
    }

    private void showDialog(){
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.dialog_add_recipe, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);

        final EditText recipeNameEdit;
        recipeNameEdit = deleteDialogView.findViewById(R.id.edit_recipe_name);

        recipeNameEdit.setHintTextColor(getResources()
                .getColor(android.R.color.darker_gray));

        deleteDialogView.findViewById(R.id.text_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                boolean valid=true;

                for (int i=0; i<=mIngredientsAdded.size(); i++){
                    CustomEditText et = deleteDialogView.findViewWithTag("ingredient-et-"+i);
                    if (et!=null && et.getText()!=null&&et.getText().toString().length()>0){
                        mFinalIngredients.add(et.getText().toString());
                        Log.wtf("finally-added",et.getText().toString()+" ingredient");
                    }
                }

                if (recipeNameEdit.getText()==null||
                        recipeNameEdit.getText().length()==0){
                    recipeNameEdit.setHintTextColor(getResources()
                            .getColor(android.R.color.holo_red_dark));
                    valid = false;
                }else{
                    if (RealmController.with(MainActivity.this)
                            .getRecipeInputForName(recipeNameEdit.getText().toString())!=null){
                        valid = false;
                        Snackbar.make(v.getRootView(),
                                "A recipe with this name already exists!",
                                Snackbar.LENGTH_SHORT).show();
                    }
                }

                EditText previousEditText  = deleteDialogView.findViewWithTag("ingredient-et-"
                        +mIngredientsAdded.size());
                if (previousEditText!=null && previousEditText.getText()!=null &&
                        previousEditText.getText().toString().length()>0){
                    mIngredientsAdded.add(previousEditText.getText().toString());
                }

                if (mFinalIngredients.size()==0){
                    valid=false;
                    if (previousEditText!=null) previousEditText.setHintTextColor(
                            getResources().getColor(android.R.color.holo_red_dark));
                }

                if (valid) {
                    mRecipeNameAdded = recipeNameEdit.getText().toString();
                    StringBuilder mIngredients= new StringBuilder();
                    for (String ingredient: mFinalIngredients){
                        mIngredients.append(ingredient).append("\n");
                    }
                    deleteDialog.dismiss();
                    RealmController.with(getApplication()).insertRecipeInput(mRecipeNameAdded,
                            mIngredients.toString(), "Not specified");
                    displayData();

                    addToFirebase(mRecipeNameAdded, mIngredients.toString(),"Not specified");
                }

            }
        });
        deleteDialogView.findViewById(R.id.text_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });

        mIngredientsAdded.clear();

        mFinalIngredients.clear();


        deleteDialogView.findViewById(R.id.image_add_ingredient).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        EditText previousEditText  = deleteDialogView
                                .findViewWithTag("ingredient-et-" +mIngredientsAdded.size());
                        if (previousEditText==null || (previousEditText.getText()!=null &&
                                previousEditText.getText().toString().length()>0)){
                            if (previousEditText!=null && previousEditText.getText()!=null
                                    && previousEditText.getText().toString().length()>0) {
                                mIngredientsAdded.add(previousEditText.getText()
                                        .toString());
                            }

                            LinearLayout ll = deleteDialogView.findViewById(R.id.
                                    container_ingredients);
                            ll.setWeightSum(4);

                         //   CustomEditText etQuantity = generateCustomEditText();
                            CustomEditText et = generateCustomEditText();
                            ll.addView(et);

                        }else{
                            previousEditText.setHintTextColor(getResources()
                                    .getColor(android.R.color.holo_red_dark));
                        }

                    }
                });

        deleteDialog.show();
    }

    //todo solve memory leaks due to static view variables/static context variables

    private void showDialogAuth(){

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Authenticate");
        alertDialog.setMessage(getString(R.string.text_auth));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

   private void displayData(){
        StringBuilder mIngredients= new StringBuilder();
        for (String ingredient: mFinalIngredients){
            mIngredients.append(" ").append(ingredient);
        }
        Log.wtf("after-dialog",mRecipeNameAdded+" "+mIngredients);

        RealmResults<RecipeInput> recipeInputs = RealmController.with(getApplication())
                .getRecipeInputs();
        for (RecipeInput input: recipeInputs){
            Log.wtf("in-db-recipe",input.getmName()+" "+input.getmId()+" "
                    +input.getmIngredients()+" "+input.getmServings());
        }

   }

   public CustomEditText generateCustomEditText() {
       LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
               ViewGroup.LayoutParams.MATCH_PARENT,
               ViewGroup.LayoutParams.WRAP_CONTENT);
       //  layoutParams.setMargins(15, 10, 0, 0);
       final CustomEditText et = new CustomEditText(this);
       et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
       // et.setBackgroundResource(R.drawable.login_textfield_background);
       et.setHint("brown sugar");
       et.setInputType(InputType.TYPE_CLASS_TEXT);
       //  et.setPadding(35, 0, 30, 0);
       // et.setId(id);
       et.setTag("ingredient-et-"+(mIngredientsAdded.size()));
       et.setLayoutParams(layoutParams);
       et.requestFocus();
       et.setCompoundDrawablesWithIntrinsicBounds(
               null,
               null,
                getResources().getDrawable(R.drawable.ic_cancel_black_24dp),
               null);
       et.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View view, MotionEvent motionEvent) {
               final int DRAWABLE_LEFT = 0;
               final int DRAWABLE_TOP = 1;
               final int DRAWABLE_RIGHT = 2;
               final int DRAWABLE_BOTTOM = 3;

               if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                   if(motionEvent.getRawX() >= (et.getRight() -
                           et.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                       // your action here
                       ((ViewGroup)et.getParent()).removeView(et);

                       return true;
                   }
               }
               return false;
           }
       });
       return et;
   }
//todo solve warning ontouchlistener above
//todo vocal commands
    //todo quantity for each ingredient, eventually spinner for units
    private void setupNotification(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 16);
        calendar.set(Calendar.MINUTE, 13);
        calendar.set(Calendar.SECOND, 0);

        Intent notifyIntent = new Intent(this,AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        String notificationPref = prefUtils.getNotify();
        switch (notificationPref){
            case "Daily":
                if (alarmManager != null) {
                    alarmManager.setRepeating(
                            AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY,
                            pendingIntent);
                }
                break;
            case "Weekly":
                if (alarmManager != null) {
                    alarmManager.setRepeating(
                            AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY*7,
                            pendingIntent);
                }
        }

        if (alarmManager != null) {
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent);
        }
    }

    public static void setupImageProfile(Bitmap bitmap){
        GlideApp.with(listSuggestions.getContext()).load(bitmap)
                .placeholder(R.drawable.ic_not_interested_black_24dp).into(mProfileImageView);
    }

    private void setupFirebase() {
        try{
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }catch (DatabaseException e){
            e.printStackTrace();
        }

        mRecipesDatabase = FirebaseDatabase.getInstance();
        mRecipesDatabaseReference = mRecipesDatabase.getReference("recipes");
       // mRecipesDatabase.addValueEventListener
        mRecipesDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ingredients= new ArrayList<>();

                List<Recipe> recipes = new ArrayList<>();
                RealmController.with(MainActivity.this).deleteRecipeInputs();
                for (DataSnapshot recipeSnapshot: dataSnapshot.getChildren()){
                    RecipeInput input = recipeSnapshot.getValue(RecipeInput.class);
                    recipes.add(input.toRecipe());
                    if (mRecipes==null)mRecipes = new ArrayList<>();
                   /// if (RealmController.with(MainActivity.this).getRecipeInputForName(input.getmName())==null){//todo author fields name and mail and compare by author as well
                        mRecipes.add(input.toRecipe());

                        for (Ingredient ingr: input.toRecipe().getIngredients()){
                            ingredients.add(ingr.getIngredient());
                        }

                        RealmController.with(MainActivity.this)
                                .insertRecipeInput(input.getmName(), input.getmIngredients(),input.getmServings());

                   // }
                }
               // setupSuggestions(recipes);


                //todo field for isadded [by user] to know if the user should be able to edit servings or add pictures
                adapterSuggestions  = new SuggestionsListAdapter(
                        listSuggestions.getContext(),
                        mRecipes,
                        (Activity)mSearchViewProduct.getContext());
                listSuggestions.setAdapter(adapterSuggestions);

                CategoriesFragment.setupData(MainActivity.this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addToFirebase(String mRecipeNameAdded, String ingredients, String servings) {
        RecipeInput recipeInput = new RecipeInput();
        recipeInput.setmName(mRecipeNameAdded);
        recipeInput.setmIngredients(ingredients);

        recipeInput.setmIngredientsListFromString(ingredients);

        recipeInput.setmServings(servings);
        mRecipesDatabaseReference.child(prefUtils.getEmail().replace(".","")
                .replace("#","")
                .replace("$","")
                .replace("[","")
                .replace("]","")+" "+mRecipeNameAdded)

                .setValue(recipeInput);
    }

    public static List<String> getIngredients() {
        return ingredients;
    }
}

