package com.vrd.gsaf.registration;

import static com.vrd.gsaf.registration.Login.loadHome;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.linkedin.platform.utils.Scope;
import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.http.IHttpRequest;
import com.microsoft.graph.models.extensions.Drive;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.IPublicClientApplication;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.exception.MsalException;
import com.omralcorut.linkedinsignin.Linkedin;
import com.omralcorut.linkedinsignin.LinkedinLoginViewResponseListener;
import com.omralcorut.linkedinsignin.model.LinkedinToken;
import com.vrd.gsaf.BuildConfig;
import com.vrd.gsaf.R;
import com.vrd.gsaf.api.ApiInterface;
import com.vrd.gsaf.api.RetrofitClient;
import com.vrd.gsaf.api.responses.fairDetail.Keywords;
import com.vrd.gsaf.api.responses.login.Login;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.home.dashboard.careeTest.CareerTest;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class RegistrationOptions extends Fragment implements View.OnClickListener {

    private static final String TAG = "Google Sign In";
    private static final long MIN_CLICK_INTERVAL = 600;
    private static final String host = "api.linkedin.com";
    private static final String topCardUrl = "https://" + host + "/v1/people/~:" +
            "(email-address,formatted-name,phone-numbers,public-profile-url,picture-url,picture-urls::(original))";
    public static boolean isViewClicked = false;
    int RC_SIGN_IN = 444;
    private View view;
    private ProgressBar progressBar;
    private ImageView backImageView, passwordVisibleImageView, draggable_view;
    private Button facebookBtn, microsoftBtn, googleBtn, linkedInBtn;
    private long mLastClickTime;
    private GoogleSignInClient mGoogleSignInClient;
    CallbackManager callbackManager;
    private final static String[] SCOPES = {"Files.Read"};
    /* Azure AD v2 Configs */
    final static String AUTHORITY = "https://login.microsoftonline.com/common";
    private ISingleAccountPublicClientApplication mSingleAccountApp;

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Helper.lockOrientation();

        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.fragment_registration_options, container, false);
        Helper.hideKeyboard();
        initializeViews();
        return view;
    }

    private void initializeViews() {
        Helper.setBackgroundRelativeGradient(view);
        progressBar = view.findViewById(R.id.progressBar);
        backImageView = view.findViewById(R.id.backImageView);
        facebookBtn = view.findViewById(R.id.facebookBtn);
        microsoftBtn = view.findViewById(R.id.microsoftBtn);
        googleBtn = view.findViewById(R.id.googleBtn);
        linkedInBtn = view.findViewById(R.id.linkedInBtn);
        ImageView appLogo = view.findViewById(R.id.logo);
        Glide.with(Singleton.getInstance().getContext()).
                load(AppConstants.APP_LOGO)
                .placeholder(R.drawable.rectangluar_placeholder)
                .into(appLogo);
        setLanguage((String) this.getArguments().get("from"));

        //google SSO
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.google_client_id)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(Singleton.getInstance().getContext(), gso);

        PublicClientApplication.createSingleAccountPublicClientApplication(requireActivity(),
                R.raw.auth_config_single_account, new IPublicClientApplication.ISingleAccountApplicationCreatedListener() {
                    @Override
                    public void onCreated(ISingleAccountPublicClientApplication application) {
                        mSingleAccountApp = application;
                        loadAccount();
                    }

                    @Override
                    public void onError(MsalException exception) {
//                        displayError(exception);
                    }
                });
        if(isLoggedInFb()){
            LoginManager.getInstance().logOut();
        }

    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void linkedInLoginHandle() {
        Linkedin.Companion.initialize(getActivity(),
                BuildConfig.linkedIn_client_id,
                BuildConfig.linkedIn_client_secret,
                BuildConfig.linkedIn_redirect_url,
                "RANDOM_STRING",
                Arrays.asList("r_liteprofile", "r_emailaddress", "w_member_social"));

        Linkedin.Companion.login(getActivity(), new LinkedinLoginViewResponseListener() {
            @Override
            public void linkedinDidLoggedIn(@NotNull LinkedinToken linkedinToken) {
                // Success
                if (linkedinToken.getAccessToken() != null)
                    goForLogin("linkedIn", linkedinToken.getAccessToken());
//                getProfile(linkedinToken.getAccessToken());

            }

            @Override
            public void linkedinLoginDidFail(@NotNull String error) {
                // Fail
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task, data);
            return;
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask, Intent data) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            assert result != null;
            @SuppressLint("StaticFieldLeak")
            AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    String token = null;

                    try {
                        token = GoogleAuthUtil.getToken(
                                requireActivity(),
                                account.getAccount(),
                                "oauth2:" + Scopes.PLUS_LOGIN);
                    } catch (IOException transientEx) {
                        // Network or server error, try later
                        Log.e(TAG, transientEx.toString());
                    } catch (UserRecoverableAuthException e) {
                        // Recover (with e.getIntent())
                        Log.e(TAG, e.toString());
                        Intent recover = e.getIntent();
//                        startActivityForResult(recover, REQUEST_CODE_TOKEN_AUTH);
                    } catch (GoogleAuthException authEx) {
                        // The call is not ever expected to succeed
                        // assuming you have already verified that
                        // Google Play services is installed.
                        Log.e(TAG, authEx.toString());
                    }

                    return token;
                }

                @Override
                protected void onPostExecute(String token) {
                    Log.i(TAG, "Access token retrieved:" + token);
                    goForLogin("google", token);
                    mGoogleSignInClient.signOut();
                    LoginManager.getInstance().logOut();
                }

            };
            task.execute();
//            String accessToken = GoogleAuthUtil.getToken(requireActivity(), account.getAccount(), "oauth2:" + Scopes.PLUS_LOGIN);

//            goForLogin("google",accessToken);
//            mGoogleSignInClient.signOut();
//            LoginManager.getInstance().logOut();
            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            // updateUI(null);
        }
    }

    private void setClickListernes() {
        facebookBtn.setOnClickListener(this);
        microsoftBtn.setOnClickListener(this);
        googleBtn.setOnClickListener(this);
        linkedInBtn.setOnClickListener(this);
        backImageView.setOnClickListener(this);
    }

    private void startTimer() {
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                isViewClicked = false;
            }
        }, MIN_CLICK_INTERVAL);

    }

    private void setLanguage(String x) {
        int languageIndex = Singleton.getInstance().getLanguageIndex();
        Keywords keyword = Singleton.getInstance().getFairData().getFair().getFairTranslations().get(languageIndex).getKeywords();
        switch (x) {
            case "registeration":
                facebookBtn.setText(keyword.getSignUp() + " with Facebook");
                googleBtn.setText(keyword.getSignUp() + " with Google");
                linkedInBtn.setText(keyword.getSignUp() + " with Linked In");
                microsoftBtn.setText(keyword.getSignUp() + " with Microsoft");
                break;
            case "login":
                facebookBtn.setText(keyword.getLogin() + " with Facebook");
                googleBtn.setText(keyword.getLogin() + " with Google");
                linkedInBtn.setText(keyword.getLogin() + " with Linked In");
                microsoftBtn.setText(keyword.getLogin() + " with Microsoft");

                break;

        }
        setClickListernes();
    }

    @Override
    public void onClick(View view) {
        long currentClickTime = SystemClock.uptimeMillis();
        long elapsedTime = currentClickTime - mLastClickTime;

        mLastClickTime = currentClickTime;

        if (elapsedTime <= MIN_CLICK_INTERVAL)
            return;
        if (!isViewClicked) {
            isViewClicked = true;
            startTimer();
        } else {
            return;
        }

        switch (view.getId()) {
            case R.id.backImageView:
                FragmentManager fm = getParentFragmentManager();
                fm.popBackStackImmediate();
                break;
            case R.id.facebookBtn:
                loginWithFB();
                break;
            case R.id.linkedInBtn:
                linkedInLoginHandle();
                break;
            case R.id.googleBtn:
                googleSignIn();
                break;
            case R.id.microsoftBtn:
                if (mSingleAccountApp == null) {
                    return;
                }
                mSingleAccountApp.signIn(requireActivity(), null, SCOPES, getAuthInteractiveCallback());
                break;
        }
    }
    public boolean isLoggedInFb() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
    private void loginWithFB() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance()
                .logInWithReadPermissions(
                        this,
                        callbackManager,
                        Arrays.asList("email", "public_profile", "user_friends", "user_photos", "user_birthday", "user_location")
                );

        // Callback registration

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
//                        Helper.showToast(loginResult.getAccessToken().getToken());
                        if (loginResult != null && loginResult.getAccessToken() != null) {
                            goForLogin("fb", loginResult.getAccessToken().getToken());
                        }
//                        getFBProfile(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Helper.showToast("cancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Helper.showToast("error");
                    }
                });
    }

    private void getFBProfile(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                Log.d("res", object.toString());
                Log.d("res_obj", response.toString());
                try {

                    String id = object.getString("id");
                    try {
                        URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                        Log.i("profile_pic", profile_pic + "");

                        String f_name = object.getString("first_name");
                        String l_name = object.getString("last_name");
                        String name = f_name + " " + l_name;
                        String email = object.getString("email");
                        String image = profile_pic.toString();


                        Log.d("data", email + name + image);
                        String type = "facebook";

                        if (email == null) {
                            Helper.showToast(email);
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void getLinkedInProfile(String token) {
        String PROFILE_URL =
                "https://api.linkedin.com/v2/me?projection=(id,firstName,lastName,profilePicture(displayImage~:playableStreams))";

        Request request = new
                Request.Builder()
                .url(PROFILE_URL)
                .header("Authorization", "Bearer " + token)
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String res = response.body().string();
//                    LinkedInProfile profile = new Gson().fromJson(res,LinkedInProfile.class);

                }

            }

            @Override
            public void onFailure(Call call, IOException e) {

            }


        });

    }

    private void loadAccount() {
        if (mSingleAccountApp == null) {
            return;
        }

        mSingleAccountApp.getCurrentAccountAsync(new ISingleAccountPublicClientApplication.CurrentAccountCallback() {
            @Override
            public void onAccountLoaded(@Nullable IAccount activeAccount) {
                // You can use the account data to update your UI or your app database.
//                updateUI(activeAccount);
                signOutMicrosoft();
            }

            @Override
            public void onAccountChanged(@Nullable IAccount priorAccount, @Nullable IAccount currentAccount) {
                if (currentAccount == null) {
                    // Perform a cleanup task as the signed-in account changed.
//                    performOperationOnSignOut();
                }
            }

            @Override
            public void onError(@NonNull MsalException exception) {
//                displayError(exception);
            }
        });
    }

    private AuthenticationCallback getAuthInteractiveCallback() {
        Helper.showProgressBar(progressBar);
        return new AuthenticationCallback() {
            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                /* Successfully got a token, use it to call a protected resource - MSGraph */
                Helper.hideProgressBar(progressBar);
                Log.d(TAG, "Successfully authenticated");
                /* Update UI */
//                updateUI(authenticationResult.getAccount());
                /* call graph */
                if (authenticationResult.getAccessToken() != null) {
                    goForLogin( "MS",authenticationResult.getAccessToken());
                }
//                callGraphAPI(authenticationResult);
            }

            @Override
            public void onError(MsalException exception) {
                /* Failed to acquireToken */
                Helper.hideProgressBar(progressBar);
                Log.d(TAG, "Authentication failed: " + exception.toString());
//                displayError(exception);
            }

            @Override
            public void onCancel() {
                /* User canceled the authentication */
                Helper.hideProgressBar(progressBar);
                Log.d(TAG, "User cancelled login.");
            }
        };
    }

    private void callGraphAPI(IAuthenticationResult authenticationResult) {
        final String accessToken = authenticationResult.getAccessToken();
        IGraphServiceClient graphClient =
                GraphServiceClient
                        .builder()
                        .authenticationProvider(new IAuthenticationProvider() {
                            @Override
                            public void authenticateRequest(IHttpRequest request) {
                                Log.d(TAG, "Authenticating request," + request.getRequestUrl());
                                request.addHeader("Authorization", "Bearer " + accessToken);
                            }
                        })
                        .buildClient();
        graphClient
                .me()
                .drive()
                .buildRequest()
                .get(new ICallback<Drive>() {
                    @Override
                    public void success(final Drive drive) {
                        Log.d(TAG, "Found Drive " + drive.id);
//                        displayGraphResult(drive.getRawObject());
                    }

                    @Override
                    public void failure(ClientException ex) {
//                        displayError(ex);
                        Helper.showToast(ex.toString());
                    }
                });
    }

    private void signOutMicrosoft() {
        mSingleAccountApp.signOut(new ISingleAccountPublicClientApplication.SignOutCallback() {
            @Override
            public void onSignOut() {
            }

            @Override
            public void onError(@NonNull MsalException exception) {
//                displayError(exception);
            }
        });

    }

    private void goForLogin(String type, String token) {
        String provider = "";
        if (type.equalsIgnoreCase("google")) {
            provider = "VRD_Google";
        } else if (type.equalsIgnoreCase("fb")) {
            provider = "VRD_Facebook";
        } else if (type.equalsIgnoreCase("linkedIn")) {
            provider = "VRD_Linkdin";
        } else if (type.equalsIgnoreCase("MS")) {
            provider = "VRD_Microsoft";
        }
        JsonObject JsonObject = new JsonObject();
        JsonObject.addProperty("fair_id", Singleton.getInstance().getFairId());
        JsonObject.addProperty("timezone", TimeZone.getDefault().getID());
        JsonObject.addProperty("device_type", "android");
        JsonObject.addProperty("token", token);
        JsonObject.addProperty("from", provider);
        JsonObject.addProperty("user_country", Singleton.getInstance().getAddresses().get(0).getCountryName());
        JsonObject.addProperty("user_city", Singleton.getInstance().getAddresses().get(0).getLocality());
        if (Singleton.getInstance().getAddresses().get(0).getPostalCode() == null) {
            JsonObject.addProperty("user_postal_code", "980096");
        } else {
            JsonObject.addProperty("user_postal_code", Singleton.getInstance().getAddresses().get(0).getPostalCode());
        }
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        JsonObject.addProperty("device_id", task.getResult());
                        login(JsonObject);
                    } else Helper.showToast("Firebase token error");
                }
        );

    }

    private void login(JsonObject jsonObject) {
        Helper.showProgressBar(progressBar);
        retrofit2.Call<Login> call = RetrofitClient.getRetrofitInstance().create(ApiInterface.class).socialLogin(Singleton.getInstance().getLanguage(), "application/json", AppConstants.APP_ID, AppConstants.APP_KEY, jsonObject);
        call.enqueue(new retrofit2.Callback<Login>() {
            @Override
            public void onResponse(retrofit2.Call<Login> call, retrofit2.Response<Login> response) {
                Helper.hideProgressBar(progressBar);
                try {
                    if (response.body().getStatus()) {
                        updateSp(response);
                    } else
                        Helper.showToast(response.body().getMsg());
                } catch (Exception e) {
                    ResponseBody response1 = response.errorBody();
                    Helper.getErrorMessage(response1);
                }

            }

            @Override
            public void onFailure(retrofit2.Call<Login> call, Throwable t) {
                Helper.hideProgressBar(progressBar);
                Helper.showToast("Something Went Wrong. Please try again in a while");

            }

        });
    }

    private void updateSp(retrofit2.Response<Login> response) {
        Singleton.getInstance().setLoginData(response.body().getData());
        SharedPreferences.Editor editor = Singleton.getInstance().getSharedPreferences().edit();
        editor.putBoolean("login", true);
        Gson gson = new Gson();
        String json = gson.toJson(Singleton.getInstance().getLoginData());
        editor.putString("loginData", json);
        editor.apply();
        if (Singleton.getInstance().getLoginData().getUser().getTakeTest() == 0 && (Singleton.getInstance().getFairData().getFair().getMatchAlgorithm().equals("1") || Singleton.getInstance().getFairData().getFair().getOptions().getEnable_force_questionnaire_after_registration_front() == 1)) {
            replaceFragment(new CareerTest(), "login");
        } else {
            SharedPreferences.Editor editor2 = Singleton.getInstance().getSharedPreferences().edit();
            editor2.putBoolean("careerTest", true);
            editor2.apply();
            loadHome();
        }
    }

    private void replaceFragment(Fragment fragment, String tag) {

        Bundle args = new Bundle();
        args.putString("from", "login");
        fragment.setArguments(args);
        FragmentManager fm = getParentFragmentManager();
        fm.beginTransaction().replace(R.id.frameLayout, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(tag)
                .commit();
    }


}