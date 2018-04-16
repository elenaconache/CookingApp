package com.example.elena.shopeasy.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.elena.shopeasy.App;
import com.example.elena.shopeasy.R;
import com.example.elena.shopeasy.fragments.FooterFragment;
import com.example.elena.shopeasy.utils.MyAnimationUtils;

import com.example.elena.shopeasy.utils.PrefUtils;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class AccountActivity extends AppCompatActivity implements
        FooterFragment.FragmentFooterListener{

    private CallbackManager callbackManager;
    private LoginButton mLoginButton;
    private Dialog mLoginDialog;
    private TextView mLoginLogoutTextView;

    private PrefUtils prefUtils;
    private ProgressBar mProgressBar;

    private static final String PERMISSION_PROFILE= "public_profile";
    private static final String PERMISSION_EMAIL = "email";
    private static final String BUNDLE_KEY_FIELDS = "fields";
    private static final String BUNDLE_FIELDS = "id,first_name,last_name,email,gender";

    private TextView mAccountInfoTextView;
    private TextView mUserRightsTextView;

    private PrefUtils mPrefUtils;

    private ConstraintLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //FacebookSdk.sdkInitialize(getApplicationContext());//not needed anymore, gets initialized
        // by itself
        AppEventsLogger.activateApp(getApplication());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_account);

        findViews();

        mPrefUtils = new PrefUtils(this);
        setupColors();

        mProgressBar.setVisibility(View.GONE);

        mLoginLogoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupLoginDialog();
            }
        });
        // If you are using in a fragment, call loginButton.setFragment(this);
        mAccountInfoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openAccountInfo = new Intent(AccountActivity.this,
                        AccountInfoActivity.class);
                startActivity(openAccountInfo);
            }
        });
        mUserRightsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openUserRights = new Intent(AccountActivity.this,
                        UserRightsActivity.class);
                startActivity(openUserRights);
            }
        });

        prefUtils = new PrefUtils(this);

    }

    @Override
    public void onHome() {//for footer fragment
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent openActivity =  new Intent(AccountActivity.this, MainActivity.class);
                startActivity(openActivity);
            }
        },480);//300ms = time of anim/pulse.xml
    }

    @Override
    public void onAccount() {//for footer fragment
      /*  Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent openAccountActivity =  new Intent(GetInspiredActivity.this, AccountActivity.class);
                startActivity(openAccountActivity);
            }
        },480);//300ms = time of anim/pulse.xml*/
    }

    @Override
    public void onBook() {//for footer fragment
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent openAccountActivity =  new Intent(AccountActivity.this,AddedRecipesActivity.class);
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

    private void setupLoginDialog(){
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.login_dialog, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);

        mLoginButton = deleteDialogView.findViewById(R.id.login_button);
        mLoginButton.setReadPermissions(Arrays.asList(PERMISSION_PROFILE,PERMISSION_EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);
        // Callback registration

       mLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                   @Override
                   public void onSuccess(LoginResult loginResult) {


                       String accessToken = loginResult.getAccessToken().getToken();

                       // save accessToken to SharedPreference
                       prefUtils.saveAccessToken(accessToken);

                       GraphRequest request = GraphRequest.newMeRequest(
                               loginResult.getAccessToken(),
                               new GraphRequest.GraphJSONObjectCallback() {
                                   @Override
                                   public void onCompleted(JSONObject jsonObject,
                                                           GraphResponse response) {
                                       // Getting FB User Data
                                       //Bundle facebookData = getFacebookData(jsonObject);
                                       saveProfileToPrefs(jsonObject);

                                       Log.wtf("accountactivity","facebook data : "+
                                              jsonObject);


                                   }
                               });

                       Bundle parameters = new Bundle();
                       parameters.putString(BUNDLE_KEY_FIELDS, BUNDLE_FIELDS);
                       request.setParameters(parameters);
                       request.executeAsync();
                   }


                   @Override
                   public void onCancel () {
                       Log.wtf("facebook", "Login attempt cancelled.");
                   }

                   @Override
                   public void onError (FacebookException e){
                       e.printStackTrace();
                       Log.wtf("facebook", "Login attempt failed.");
                       deleteAccessToken();
                   }
               }
       );
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
              //  LoginManager.getInstance().logInWithReadPermissions(AccountActivity.this,
               //         Arrays.asList("public_profile","email"));//actual login - not needed for
                // the implicit button, just for the other cases
                deleteDialog.dismiss();
                if (prefUtils.isAuthenticated()){
                    deleteAccessToken();
                }
            }
        });

        deleteDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void deleteAccessToken() {
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                Log.wtf("accountactivity",currentAccessToken+" token");

                if (currentAccessToken == null){
                    //User logged out
                    prefUtils.clearToken();
                    LoginManager.getInstance().logOut();
                }
            }
        };
    }

    private void saveProfileToPrefs(JSONObject jsonObject){
        try {
            URL profile_pic;
            String id = jsonObject.getString("id");
            profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?type=large");
            prefUtils.saveFacebookUserInfo(jsonObject.getString("first_name"),
                    jsonObject.getString("last_name"),jsonObject.getString("email"),
                    jsonObject.getString("gender"), profile_pic.toString());
            Log.wtf("ui.accountactivity",jsonObject.toString()+" json");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void findViews(){
        mLoginLogoutTextView= findViewById(R.id.account_loginlogout_text);
        mProgressBar = findViewById(R.id.pb_loading_auth);
        mAccountInfoTextView = findViewById(R.id.account_info_text);
        mUserRightsTextView = findViewById(R.id.account_rights_text);

        mContainer = findViewById(R.id.container_account);
    }

    private void setupColors(){
        int fontColor = mPrefUtils.getFontColor();
        Log.wtf("fontcolor",fontColor+"");
        int backgroundColor = mPrefUtils.getBackgroundColor();
        if (fontColor!=0){
            mUserRightsTextView
                    .setTextColor(Color.parseColor("#"+Integer.toHexString(fontColor)));
            mAccountInfoTextView
                    .setTextColor(Color.parseColor("#"+Integer.toHexString(fontColor)));
            mLoginLogoutTextView
                    .setTextColor(Color.parseColor("#"+Integer.toHexString(fontColor)));

            Drawable drawable =  mUserRightsTextView.getCompoundDrawables()[0];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable.setTint(Color.parseColor("#"+Integer.toHexString(fontColor)));
            }
            mUserRightsTextView.setCompoundDrawablesWithIntrinsicBounds(
                    drawable,
                    null,
                    null,
                    null);

            drawable =  mAccountInfoTextView.getCompoundDrawables()[0];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable.setTint(Color.parseColor("#"+Integer.toHexString(fontColor)));
            }
            mAccountInfoTextView.setCompoundDrawablesWithIntrinsicBounds(
                    drawable,
                    null,
                    null,
                    null);

            drawable =  mLoginLogoutTextView.getCompoundDrawables()[0];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable.setTint(Color.parseColor("#"+Integer.toHexString(fontColor)));
            }
            mLoginLogoutTextView.setCompoundDrawablesWithIntrinsicBounds(
                    drawable,
                    null,
                    null,
                    null);
        }
        if (backgroundColor!=0){
            mContainer.setBackgroundColor(
                    Color.parseColor("#"+Integer.toHexString(backgroundColor)));
        }
    }
}
