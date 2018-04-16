package com.example.elena.shopeasy.ui;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.elena.shopeasy.BuildConfig;
import com.example.elena.shopeasy.GlideApp;
import com.example.elena.shopeasy.R;
import com.example.elena.shopeasy.customViews.CustomEditText;
import com.example.elena.shopeasy.fragments.FooterFragment;
import com.example.elena.shopeasy.model.Ingredient;
import com.example.elena.shopeasy.model.Recipe;
import com.example.elena.shopeasy.realm.RealmController;
import com.example.elena.shopeasy.realm.RecipeInput;
import com.example.elena.shopeasy.utils.ImageFilePathUtils;
import com.example.elena.shopeasy.utils.PrefUtils;
import com.example.elena.shopeasy.youtubeApi.Config;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;

import static android.support.v4.graphics.TypefaceCompatUtil.getTempFile;

public class RecipeDetailsActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener{

    private static final int RECOVERY_REQUEST = 1;
    private static final String PATH_IMAGES = "shopeasyimages/";
    private static final String STORAGE_PATH = "recipe_images/";
    private YouTubePlayerView youTubeView;

    private FrameLayout mShareVideoContainer;

    private String[] mVideoUrls = {
            "SARReq5smA0",//cheesecake
            "HIonKbKM-tE",//brownie
            "BFPdcRVBj7g",//yellow cake
            "PQXyXruZycc"//nutella pie
    };

    private Recipe mRecipe;
    private TextView mRecipeNameTextView;
    private TextView mIngredientsTextView;
    private TextView mServingsTextView;

    private ImageView mRecipeImageView;
    private CircleImageView mEditImageView;

    private static final String TAG = "RecipeDetailsActivity";
    private static final int CAMERA_REQUEST_CODE = 0;
    private static final int GALLERY_REQUEST_CODE =1 ;
    private static final int READ_WRITE_CAMERA_REQUEST_CODE=10;
    private static final int READ_WRITE_REQUEST_CODE=11;

    private CircleImageView mEditServingsImageView;

    private String mCurrentPhotoPath;
    private Uri picUri;
    private String mCurrentYoutubeLink;

    private PrefUtils mPrefUtils;

    private LinearLayout mContainer;
    private TextView mIngredientsLabelTextView;
    private LinearLayout mServingsContainer;

    private StorageReference mStorageReference;
    // Creating URI.
    private Uri mFilePathUri;

    private static final int READ_EXTERNAL_CODE = 12;

    private ProgressBar progressBar;

    private LinearLayout mVideoContainer;
    private FrameLayout mAddedRecipeContainer;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        findViews();
        youTubeView.initialize(Config.YOUTUBE_API_KEY, this);

        mPrefUtils = new PrefUtils(this);
        setupColors();

        progressBar.setVisibility(View.VISIBLE);

        // Assign FirebaseStorage instance to storageReference.
        mStorageReference = FirebaseStorage.getInstance().getReference();

        if (getIntent().hasExtra(RecipesActivity.EXTRA_RECIPE)){
            mRecipe = getIntent().getParcelableExtra(RecipesActivity.EXTRA_RECIPE);

            mServingsTextView.setText(mRecipe.getServings()+"");

            Log.wtf(TAG, mRecipe.getPhoto()+" photo");

            if (mRecipe.getPhoto()!=null && mRecipe.getPhoto().length()>0){

                Log.wtf("RecipeDetailsActivity","no firebase");
                Glide.with(this).load(mRecipe.getPhoto()).into(mRecipeImageView);
                progressBar.setVisibility(View.GONE);

            }else if (mStorageReference.child( mPrefUtils.getEmail().replace(".","")
                    .replace("#","")
                    .replace("$","")
                    .replace("[","")
                    .replace("]","")+" "+mRecipe.getName()) != null){
                Log.wtf("RecipeDetailsActivity"," firebase");
                StorageReference storageReference2nd = mStorageReference.child(STORAGE_PATH +
                        mPrefUtils.getEmail().replace(".","")
                                .replace("#","")
                                .replace("$","")
                                .replace("[","")
                                .replace("]","")+" "+mRecipe.getName());

                GlideApp.with(this /* context */)//glideapp is generated after writing glidemodule and project build
                      //  .using(new FirebaseImageLoader())
                        .load(storageReference2nd)
                       // .placeholder(R.drawable.ic_not_interested_black_24dp)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                Handler handler = new Handler();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        GlideApp.with(RecipeDetailsActivity.this)
                                                .load(R.drawable.background_recipe)
                                               // .placeholder(R.drawable.ic_not_interested_black_24dp)
                                                .into(mRecipeImageView);
                                    }
                                });

                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(mRecipeImageView);


            }else{
                progressBar.setVisibility(View.GONE);
                GlideApp.with(this).load(R.drawable.background_recipe)
                       // .placeholder(R.drawable.ic_not_interested_black_24dp)
                        .into(mRecipeImageView);
            }

        }

        // Creating and load a  new InterstitialAd .
        mInterstitialAd = createNewIntAd();
        loadIntAdd();
     //   showIntAdd();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {//wasnt restored
          //  youTubePlayer.cueVideo("fhWaJi1Hsfo"); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
            if (mRecipe!=null){
                switch (mRecipe.getName().toLowerCase()) {
                    case "nutella pie":
                        mCurrentYoutubeLink = "https://www.youtube.com/watch?v="+mVideoUrls[3];
                        youTubePlayer.cueVideo(mVideoUrls[3]);
                        //youTubeView.setVisibility(View.VISIBLE);
                       // mRecipeImageView.setVisibility(View.GONE);
                      //  mEditImageView.setVisibility(View.GONE);
                        mEditServingsImageView.setVisibility(View.GONE);
                       // progressBar.setVisibility(View.GONE);
                        youTubePlayer.play();

                       // mShareVideoContainer.setVisibility(View.VISIBLE);

                        mVideoContainer.setVisibility(View.VISIBLE);
                        mAddedRecipeContainer.setVisibility(View.GONE);


                        break;
                    case "brownies":
                        mCurrentYoutubeLink = "https://www.youtube.com/watch?v="+mVideoUrls[1];
                        youTubePlayer.cueVideo(mVideoUrls[1]);
                        //youTubeView.setVisibility(View.VISIBLE);
                      //  mRecipeImageView.setVisibility(View.GONE);
                     //   mEditImageView.setVisibility(View.GONE);
                        mEditServingsImageView.setVisibility(View.GONE);
                       // progressBar.setVisibility(View.GONE);

                      //  mShareVideoContainer.setVisibility(View.VISIBLE);
                        mVideoContainer.setVisibility(View.VISIBLE);
                        mAddedRecipeContainer.setVisibility(View.GONE);

                        youTubePlayer.play();
                        break;
                    case "yellow cake":
                        mCurrentYoutubeLink = "https://www.youtube.com/watch?v="+mVideoUrls[2];
                        youTubePlayer.cueVideo(mVideoUrls[2]);
                     //   youTubeView.setVisibility(View.VISIBLE);
                    //    mRecipeImageView.setVisibility(View.GONE);
                      //  mEditImageView.setVisibility(View.GONE);
                        mEditServingsImageView.setVisibility(View.GONE);
                      //  progressBar.setVisibility(View.GONE);
                        mVideoContainer.setVisibility(View.VISIBLE);
                        mAddedRecipeContainer.setVisibility(View.GONE);

                        mShareVideoContainer.setVisibility(View.VISIBLE);

                        youTubePlayer.play();
                        break;
                    case "cheesecake":
                        mCurrentYoutubeLink = "https://www.youtube.com/watch?v="+mVideoUrls[0];
                        youTubePlayer.cueVideo(mVideoUrls[0]);
                       // youTubeView.setVisibility(View.VISIBLE);
                      //  mRecipeImageView.setVisibility(View.GONE);
                      //  mEditImageView.setVisibility(View.GONE);
                      //  progressBar.setVisibility(View.GONE);
                        mEditServingsImageView.setVisibility(View.GONE);
                        mVideoContainer.setVisibility(View.VISIBLE);
                        mAddedRecipeContainer.setVisibility(View.GONE);

                       // mShareVideoContainer.setVisibility(View.VISIBLE);

                        youTubePlayer.play();
                        break;
                    default:
                        mCurrentYoutubeLink = null;
                      //  youTubeView.setVisibility(View.GONE);
                      //  mRecipeImageView.setVisibility(View.VISIBLE);
                      //  mEditImageView.setVisibility(View.VISIBLE);
                        mEditServingsImageView.setVisibility(View.VISIBLE);

                       // mShareVideoContainer.setVisibility(View.GONE);
                       // youTubePlayer.play();
                        mVideoContainer.setVisibility(View.GONE);
                        mAddedRecipeContainer.setVisibility(View.VISIBLE);
                }

                mRecipeNameTextView.setText(mRecipe.getName());
                for (Ingredient ingredient: mRecipe.getIngredients()){
                    mIngredientsTextView.append(ingredient.getIngredient()+'\n');
                }

                mServingsTextView.setText(mRecipe.getServings()+"");
            }
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error),
                    youTubeInitializationResult.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    private void findViews(){
        youTubeView = findViewById(R.id.youtube_view);
        mRecipeNameTextView = findViewById(R.id.recipe_name_title);
        mIngredientsTextView = findViewById(R.id.ingredients_list_text);
        mServingsTextView = findViewById(R.id.servings_text);

        mRecipeImageView = findViewById(R.id.image_recipe_pic);
        mEditImageView = findViewById(R.id.image_recipe_pic_edit);

        mEditServingsImageView = findViewById(R.id.image_servings_edit);

        mContainer = findViewById(R.id.container_recipe_details);
        mIngredientsLabelTextView = findViewById(R.id.text_ingredients_label);
        mServingsContainer = findViewById(R.id.container_servings);

        progressBar = findViewById(R.id.pb_loading_recipe_image);

        mShareVideoContainer = findViewById(R.id.container_share_video);

        mVideoContainer = findViewById(R.id.container_default_recipe_video);
        mAddedRecipeContainer = findViewById(R.id.added_recipe_container_image);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoContainer.getVisibility()==View.VISIBLE) progressBar.setVisibility(View.GONE);
    }

    public void onEditRecipePic(View view) {
        displayPopupChooseImage();
        // custom dialog
    }

    void displayPopupChooseImage() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_choose_recipe_pic);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        LinearLayout mCamerabtn =  dialog.findViewById(R.id.cameradialogbtn);
        LinearLayout mGallerybtn = dialog.findViewById(R.id.gallerydialogbtn);
        TextView btnCancel = dialog.findViewById(R.id.canceldialogbtn);

        mCamerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(v.getContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(v.getContext(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                !=PackageManager.PERMISSION_GRANTED||
                        ContextCompat.checkSelfPermission(v.getContext(),
                                Manifest.permission.CAMERA)
                                !=PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    requestPermissions(
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA},
                            READ_WRITE_CAMERA_REQUEST_CODE);
                }else{
                    afterCameraPermission();
                }

                dialog.cancel();
            }
        });

        mGallerybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(v.getContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(v.getContext(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                !=PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    requestPermissions(
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    },
                            READ_WRITE_REQUEST_CODE);
                }else{
                    afterGalleryPermissions();
                }
                dialog.cancel();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel(); // dismissing the popup
            }
        });

        dialog.show();
    }

    private void afterGalleryPermissions() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Log.wtf(TAG,picUri+" uri");
                   // File file = new File(picUri.toString());
                   // mCurrentPhotoPath = picUri.get;
                    Log.wtf(TAG, "absolute path "+picUri.getPath()+" "+picUri.getEncodedPath());
                    mCurrentPhotoPath = picUri.getPath();
                    mRecipe.setPhoto(mCurrentPhotoPath);
                    RealmController.with(this).updateRecipeInputServings(mRecipe);
                    GlideApp.with(this).load(mCurrentPhotoPath)
                           // .placeholder(R.drawable.ic_not_interested_black_24dp)
                            .into(mRecipeImageView);

                    mFilePathUri = Uri.fromFile(new File(mRecipe.getPhoto()));
                    uploadImageFileToFirebaseStorage();
                }
                break;
            case GALLERY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    String realPath = ImageFilePathUtils.getPath(this, data.getData());
                    Log.i(TAG, "onActivityResult: file path : " + realPath);
                    mCurrentPhotoPath = realPath;
                    mRecipe.setPhoto(mCurrentPhotoPath);
                    RealmController.with(this).updateRecipeInputServings(mRecipe);
                    GlideApp.with(this).load(mCurrentPhotoPath)
                         //   .placeholder(R.drawable.ic_not_interested_black_24dp)
                            .into(mRecipeImageView);

                    mFilePathUri =Uri.fromFile(new File(mRecipe.getPhoto()));
                    uploadImageFileToFirebaseStorage();

                }
                break;
        }
    }
    private void afterCameraPermission(){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file=getOutputMediaFile(1);
        picUri = Uri.fromFile(file); // create
        intent.putExtra(MediaStore.EXTRA_OUTPUT,picUri); // set the image file

        startActivityForResult(intent, CAMERA_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode){
            case READ_WRITE_CAMERA_REQUEST_CODE:{
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2]==PackageManager.PERMISSION_GRANTED){
                    afterCameraPermission();
                }
                break;
            }
            case READ_WRITE_REQUEST_CODE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    afterGalleryPermissions();
                }
                break;


        }
    }

    public void onEditServingsText(View view) {

        Log.wtf(TAG,"called edit servings");
        LayoutInflater factory = LayoutInflater.from(RecipeDetailsActivity.this);
        final View deleteDialogView = factory.inflate(R.layout.dialog_servings, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(RecipeDetailsActivity.this)
                .create();
        deleteDialog.setView(deleteDialogView);

        final EditText servingsEdit;
        servingsEdit = deleteDialogView.findViewById(R.id.edit_servings);

        servingsEdit.setHintTextColor(getResources()
                .getColor(android.R.color.darker_gray));

        deleteDialogView.findViewById(R.id.text_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                if (servingsEdit.getText()!=null && servingsEdit.getText().toString().length()>0){
                    mRecipe.setServings(Integer.valueOf(servingsEdit.getText().toString()));
                    mServingsTextView.setText(mRecipe.getServings()+"");
                    RealmController.with(RecipeDetailsActivity.this)
                            .updateRecipeInputServings(mRecipe);

                    updateRecipe();

                    Log.wtf(TAG,"updating "+Integer.valueOf(servingsEdit.getText().toString()));

                }else {
                    Toast.makeText(RecipeDetailsActivity.this,"Data not changed",
                            Toast.LENGTH_SHORT).show();
                    Log.wtf(TAG,"not updating");
                }

                deleteDialog.dismiss();
            }
        });
        deleteDialogView.findViewById(R.id.text_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });
        deleteDialog.show();
    }

    /** Create a File for saving an image */
    private  File getOutputMediaFile(int type){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "ShopEasy");

        /**Create the storage directory if it does not exist*/
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        /**Create a media file name*/
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        File mediaFile;
        if (type == 1){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".png");
        } else {
            return null;
        }

        return mediaFile;
    }

    public void onShareRecipe(View view) {

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);

        StringBuilder shareBody = new StringBuilder(mRecipe.getName().toUpperCase() + "\n\nIngredients:\n");
        for (Ingredient ingredient: mRecipe.getIngredients()){
            shareBody.append(ingredient.getIngredient()).append("\n");
        }

        if (mRecipe.getPhoto()!=null){
            File file = new File(mRecipe.getPhoto());
            Uri imageUri = Uri.fromFile(file);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        }else {
            if (mCurrentYoutubeLink!=null) shareBody.append("\n"+mCurrentYoutubeLink);
        }
        shareBody.append("\n\nShared from ShopEasy app");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody.toString());
        shareIntent.setType("*/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "send"));

    }

    private void setupColors(){
        int fontColor = mPrefUtils.getFontColor();
        Log.wtf("fontcolor",fontColor+"");
        int backgroundColor = mPrefUtils.getBackgroundColor();
        if (fontColor!=0){
            mRecipeNameTextView
                    .setTextColor(Color.parseColor("#"+Integer.toHexString(fontColor)));
            mServingsTextView
                    .setTextColor(Color.parseColor("#"+Integer.toHexString(fontColor)));
            mIngredientsTextView
                    .setTextColor(Color.parseColor("#"+Integer.toHexString(fontColor)));
            mIngredientsLabelTextView.setBackgroundColor(
                    Color.parseColor("#"+Integer.toHexString(fontColor)));
            mServingsContainer.setBackgroundColor(
                    Color.parseColor("#"+Integer.toHexString(fontColor)));

        }
        if (backgroundColor!=0){
            mContainer.setBackgroundColor(
                    Color.parseColor("#"+Integer.toHexString(backgroundColor)));
        }
    }

    private void updateRecipe() {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("recipes")
                .child(mPrefUtils.getEmail().replace(".","")
                .replace("#","")
                .replace("$","")
                .replace("[","")
                .replace("]","")+" "+mRecipe.getName());

        //updating artist
        RecipeInput input = new RecipeInput();
        StringBuilder mIngredients= new StringBuilder();
        for (Ingredient ingredient: mRecipe.getIngredients()){
            mIngredients.append(ingredient.getIngredient()).append("\n");
        }
        input.setmIngredients(mIngredients.toString());

        input.setmIngredientsListFromString(mIngredients.toString());

        input.setmName(mRecipe.getName());
        input.setmServings(mRecipe.getServings()+"");
        dR.setValue(input);

    }

    // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
    public void uploadImageFileToFirebaseStorage() {

        // Checking whether FilePathUri Is empty or not.
        if (mFilePathUri != null) {

            // Setting progressDialog Title.
          //  progressDialog.setTitle("Image is Uploading...");

            // Showing progressDialog.
           // progressDialog.show();

            // Creating second StorageReference.
            StorageReference storageReference2nd = mStorageReference.child(STORAGE_PATH +
                    mPrefUtils.getEmail().replace(".","")
                            .replace("#","")
                            .replace("$","")
                            .replace("[","")
                            .replace("]","")+" "+mRecipe.getName());

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(mFilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Getting image name from EditText and store into string variable.
                            String TempImageName = mPrefUtils.getEmail().replace(".","")
                                    .replace("#","")
                                    .replace("$","")
                                    .replace("[","")
                                    .replace("]","")+" "+mRecipe.getName();


                            // Showing toast message after done uploading.
                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();

                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Showing exception erro message.
                            Toast.makeText(RecipeDetailsActivity.this,
                                    exception.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private InterstitialAd createNewIntAd() {
        InterstitialAd intAd = new InterstitialAd(this);
        // set the adUnitId (defined in values/strings.xml)
       // intAd.setAdUnitId(getString(R.string.ad_id_interstitial));
        intAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");//test ad
        intAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
               // mLevelTwoButton.setEnabled(true);
                showIntAdd();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
              //  mLevelTwoButton.setEnabled(true);
            }

            @Override
            public void onAdClosed() {
                // Proceed to the next level.
             //   levelTwo();
            }
        });
        return intAd;
    }

    private void showIntAdd() {

// Show the ad if it's ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
           // levelTwo();
        }
    }
    private void loadIntAdd() {
        // Disable the  level two button and load the ad.
       // mLevelTwoButton.setEnabled(false);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("11FC98BB86472EE536487E740D4FDF25")
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    public void onRecipeRemoved(View view) {
                if (mPrefUtils.getConfirm()){
                    showDialogConfirm();
                }else {
                    RealmController.with(this).deleteRecipeInput(mRecipe.getName());

                }


    }

    private void showDialogConfirm(){
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.dialog_confirm_deletion, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);

        deleteDialogView.findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                RealmController.with(RecipeDetailsActivity.this)
                        .deleteRecipeInput(mRecipe.getName());
                deleteFromFirebase(mRecipe.getName());
                finish();
                deleteDialog.dismiss();
            }
        });
        deleteDialogView.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });

        deleteDialog.show();
    }

    private void deleteFromFirebase(String name) {
        PrefUtils prefUtils = new PrefUtils(this);
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("recipes")
                .child(prefUtils.getEmail().replace(".","")
                        .replace("#","")
                        .replace("$","")
                        .replace("[","")
                        .replace("]","")+" "+name);

        //removing
        dR.removeValue();
    }
/*
    @Override
    public void onHome() {//for footer fragment
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent openActivity =  new Intent(RecipeDetailsActivity.this, MainActivity.class);
                startActivity(openActivity);
            }
        },480);//300ms = time of anim/pulse.xml
    }

    @Override
    public void onAccount() {//for footer fragment
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent openAccountActivity =  new Intent(RecipeDetailsActivity.this, AccountActivity.class);
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
                Intent openAccountActivity =  new Intent(RecipeDetailsActivity.this,AddedRecipesActivity.class);
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
    }*/
}
