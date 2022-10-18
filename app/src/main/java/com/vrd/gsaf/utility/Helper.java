package com.vrd.gsaf.utility;

import static android.text.Html.fromHtml;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageListActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.vrd.gsaf.BuildConfig;
import com.vrd.gsaf.R;
import com.vrd.gsaf.activity.MainActivity;
import com.vrd.gsaf.api.responses.BaseResponse;
import com.vrd.gsaf.app.MainApp;
import com.vrd.gsaf.home.whereBy.WebViewActivity;
import com.vrd.gsaf.singleton.Singleton;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import vimeoextractor.OnVimeoExtractionListener;
import vimeoextractor.VimeoExtractor;
import vimeoextractor.VimeoVideo;


public class Helper {

    static String vimeoUrl;

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            return "IP Not Available";
        } // for now eat exceptions
        return "";
    }


    public static boolean isInternetConnected() {
        ConnectivityManager connectivityMgr = (ConnectivityManager) Singleton.getInstance().getContext().getSystemService(Singleton.getInstance().getContext().CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi != null) {
            if (wifi.isConnected()) {
                return true;
            }
        }
        if (mobile != null) {
            if (mobile.isConnected()) {
                return true;
            }
        }
        openNoInternetDialog();
        return false;
    }

    @SuppressLint("ResourceAsColor")
    public static void openNoInternetDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Singleton.getInstance().getContext());
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = Singleton.getInstance().getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_no_internet, null);
        Button tryAgainBtn = dialogView.findViewById(R.id.tryAgainBtn);

        // tryAgainBtn.setText("OK");
        try {
            Helper.setButtonColorWithDrawable(tryAgainBtn, Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(), Singleton.getInstance().getFairData().getFair().getOptions().getReceptionButtonsInnerTextColor());
        } catch (Exception e) {
            // tryAgainBtn.setTextColor(R.color.white);
            GradientDrawable drawable = (GradientDrawable) tryAgainBtn.getBackground();
            // drawable.setColor(R.color.appColor);
            drawable.setColor(Color.parseColor("#16233e"));

        }

        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        tryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });


    }

    public static void showSnackBar(String message) {
        Snackbar snack = Snackbar.make(Singleton.getInstance().getActivity().getWindow().getDecorView(), message, Snackbar.LENGTH_LONG);
        snack.show();
    }

    public static void showProgressBar(ProgressBar progressBar) {
        progressBar.getIndeterminateDrawable().setColorFilter(Singleton.getTopNavColor(), android.graphics.PorterDuff.Mode.MULTIPLY);
        progressBar.setVisibility(View.VISIBLE);
        disableTouch();

    }

    public static void showProgressBar(ProgressBar progressBar, boolean disableTouch) {
        progressBar.setVisibility(View.VISIBLE);
        if (disableTouch) disableTouch();

    }

    public static void setTextColor(EditText editText) {
        editText.setTextColor(Singleton.getSidebarInnerTextColor());
        editText.setHintTextColor(Color.GRAY);
        editText.getBackground().setColorFilter(Singleton.getSidebarInnerTextColor(),
                PorterDuff.Mode.SRC_ATOP);
    }

    public static void setAlternateTextColor(EditText editText) {
        editText.setTextColor(Singleton.getSideBarBgColor());
        editText.setHintTextColor(Singleton.getSideBarBgColor());
        editText.getBackground().setColorFilter(Singleton.getSidebarInnerTextColor(),
                PorterDuff.Mode.SRC_ATOP);
    }

    public static void setTextColor(TextView textView) {
        textView.setTextColor(Singleton.getSidebarInnerTextColor());
    }

    public static void setBackgroundRelativeGradient(View view) {
        RelativeLayout mainLayout = view.findViewById(R.id.mainLayout);
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{Color.parseColor(BuildConfig.APP_COLOR),
                        Color.parseColor(BuildConfig.APP_MID_COLOR),
                        Color.parseColor(BuildConfig.END_COLOR)});
        gd.setCornerRadius(0f);
        mainLayout.setBackground(gd);
    }
    public static void setBackgroundConstraintGradient(View view) {
        ConstraintLayout mainLayout = view.findViewById(R.id.mainLayout);
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{Color.parseColor(BuildConfig.APP_COLOR),
                        Color.parseColor(BuildConfig.APP_MID_COLOR),
                        Color.parseColor(BuildConfig.END_COLOR)});
        gd.setCornerRadius(0f);
        mainLayout.setBackground(gd);
    }

    public static void setCardTextColor(TextView textView) {
        textView.setTextColor(Singleton.getCardTextColor());
    }

    public static void disableTouch() {
        Singleton.getInstance().setTouchEnabled(false);

        Singleton.getInstance().getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public static void enableTouch() {
        Singleton.getInstance().setTouchEnabled(true);
        Singleton.getInstance().getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    public static void setActiveItemBottomNav(int position) {
        // Singleton.getInstance().getHomeActivity().bottomNavigationView.getCurrentActiveItemPosition()

//        if (Singleton.getInstance().getHomeActivity().bottomNavigationView.getCurrentActiveItemPosition() != position) {
//            Singleton.getInstance().setSuperBackPressed(true);
//
//            Singleton.getInstance().getBottomNavigationView().setCurrentActiveItem(position);
//        }
        switch (position) {
            case R.id.nav_home:
            case 0:
                Singleton.getInstance().setSuperBackPressed(true);
                Singleton.getInstance().getMainActivity().chipNavigationBar.setItemSelected(R.id.nav_home, true, Singleton.getHomeTitle());
//                Singleton.getInstance().getBottomNavigationView().setCurrentActiveItem(0);
                break;

            case R.id.nav_notification:
            case 1:
                Singleton.getInstance().setSuperBackPressed(true);
                Singleton.getInstance().getMainActivity().chipNavigationBar.setItemSelected(R.id.nav_notification, true, Singleton.getNotificationTitle());
//                Singleton.getInstance().getBottomNavigationView().setCurrentActiveItem(1);
                break;
            case R.id.nav_chat:
            case 2:
                Singleton.getInstance().setSuperBackPressed(true);
                Singleton.getInstance().getMainActivity().chipNavigationBar.setItemSelected(R.id.nav_chat, true, Singleton.getMessageTitle());
//                Singleton.getInstance().getBottomNavigationView().setCurrentActiveItem(2);
                break;
        }

    }


    public static void hideProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.GONE);
        enableTouch();

    }

    public static void setYoutubePlayerSettings(YouTubePlayerView youTubePlayerView) {
        youTubePlayerView.getPlayerUiController().showFullscreenButton(false);
//    youTubePlayerView.getPlayerUiController().showSeekBar(false);
//    youTubePlayerView.getPlayerUiController().showCurrentTime(false);
//    youTubePlayerView.getPlayerUiController().showDuration(false);
        youTubePlayerView.getPlayerUiController().showYouTubeButton(false);
    }

    public static void receptionYoutubePlayerSettings(YouTubePlayerView youTubePlayerView) {
        youTubePlayerView.getPlayerUiController().showFullscreenButton(false);
        youTubePlayerView.getPlayerUiController().showSeekBar(false);
        youTubePlayerView.getPlayerUiController().showCurrentTime(false);
        youTubePlayerView.getPlayerUiController().showDuration(false);
        youTubePlayerView.getPlayerUiController().showYouTubeButton(false);
    }

    public static void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = Singleton.getInstance().getActivity().getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }


    public static void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) Singleton.getInstance().getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = Singleton.getInstance().getActivity().getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void menuClick() {
        try {
            Helper.hideKeyboard();
            if (Singleton.getInstance().getDrawerLayout().isOpen()) {
                Singleton.getInstance().getDrawerLayout().closeDrawer(GravityCompat.START);
                //    Singleton.getInstance().getMenuCloseImageView().setVisibility(View.GONE);

            } else if (!Singleton.getInstance().getDrawerLayout().isOpen()) {
                Singleton.getInstance().getDrawerLayout().setDrawerElevation(100);
                Singleton.getInstance().getDrawerLayout().openDrawer(GravityCompat.START);
                //  Singleton.getInstance().getMenuCloseImageView().setVisibility(View.VISIBLE);

            }

//
        } catch (Exception e) {
            Log.e("Error", "menuClick , Helper");
        }
    }


    public static void showKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) Singleton.getInstance().getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        View currentFocusedView = Singleton.getInstance().getActivity().getCurrentFocus();
        if (currentFocusedView != null) {
            inputMethodManager.toggleSoftInputFromWindow(
                    currentFocusedView.getApplicationWindowToken(),
                    InputMethodManager.SHOW_FORCED, 0);
        }
    }


    public static boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(Singleton.getInstance().getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        result = ContextCompat.checkSelfPermission(Singleton.getInstance().getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static String getRealPathFromUri(Uri uri, Activity activity) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(uri, proj, null, null, null);
        if (cursor == null) {
            return uri.getPath();
        } else {
            cursor.moveToFirst();
            int id = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(id);
        }
    }

    public static String getDriveFilePath(Uri uri, Context context) {
        Uri returnUri = uri;
        Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(context.getCacheDir(), name);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();

            //int bufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            Log.e("File Size", "Size " + file.length());
            inputStream.close();
            outputStream.close();
            Log.e("File Path", "Path " + file.getPath());
            Log.e("File Size", "Size " + file.length());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }


    public static int getSizeInSp(int size) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, Singleton.getInstance().getActivity().getResources().getDisplayMetrics());
    }


    public static Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = Singleton.getInstance().getContext().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public static void showToast(String text) {
        if (text != null)
            Toast.makeText(Singleton.getInstance().getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public static void getErrorMessage(ResponseBody response) {
        if (response != null) {
            String jsonString = null;
            try {
                jsonString = response.string();

                BaseResponse baseResponse = new Gson().fromJson(jsonString, BaseResponse.class);
                Singleton.getInstance().getMainActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Helper.showToast(baseResponse.getMsg());
                        } catch (Exception e) {
                            Helper.showToast("Something went wrong!");
                        }
                    }
                });
            } catch (IOException e) {
                Helper.showToast("Something Went Wrong.");
                e.printStackTrace();
            }
        } else Singleton.getInstance().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Helper.showToast("Response is null");
            }
        });
    }

    public static void noInternetDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Singleton.getInstance().getContext());
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = Singleton.getInstance().getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_no_internet, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        Button tryAgainBtn = dialogView.findViewById(R.id.tryAgainBtn);
        tryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }


    public static boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(Singleton.getInstance().getContext().getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(Singleton.getInstance().getContext().getContentResolver(), Settings.Secure.LOCATION_MODE);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public static void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Singleton.getInstance().getActivity());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {

                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        Singleton.getInstance().getActivity().startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 10);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        buildAlertMessageNoGps();
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        try {
            Singleton.getInstance().getFloatingButton().setVisibility(View.GONE);
        } catch (Exception e) {

        }
        alert.show();


    }

    public static void loadHtml(TextView textView, String text) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            textView.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));
//
//        } else
        // textView.setText(Html.fromHtml(text));
//        textView.setText(HtmlCompat.fromHtml(text, 0));
        setTextViewHTML(textView, text);

    }

    public static void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span) {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                // Do something with span.getURL() to handle the link click...
                if (span.getURL().contains("http")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(span.getURL()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MainApp.getAppContext().startActivity(intent);
                }
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    public static void setTextViewHTML(TextView text, String html) {
        CharSequence sequence = fromHtml(html);
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for (URLSpan span : urls) {
            makeLinkClickable(strBuilder, span);
        }
        text.setText(strBuilder);
        text.setMovementMethod(LinkMovementMethod.getInstance());


    }

    public static void loadImageWithGlide(ImageView imageView, String url) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterInside(), new RoundedCorners(25));
        Glide.with(Singleton.getInstance().getContext()).
                load(url)
                .placeholder(R.drawable.rectangluar_placeholder)
                .apply(requestOptions)
                .into(imageView);

    }

    public static void loadImageWithGlideNoPlaceHolder(ImageView imageView, String url) {
        if (url != null) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CenterInside(), new RoundedCorners(25));
            Glide.with(Singleton.getInstance().getContext()).
                    load(url)
                    .apply(requestOptions)
                    .transition(DrawableTransitionOptions.withCrossFade(3000))
                    .into(imageView);
        }

    }

    public static void loadRectangleImageFromUrlWithRounded(ImageView imageView, String url, int cornors) {
        if (cornors == 0)
            cornors++;
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterInside(), new RoundedCorners(cornors));
        Glide.with(Singleton.getInstance().getContext()).
                load(url)
                .placeholder(R.drawable.rectangluar_placeholder)
                .apply(requestOptions)
                .into(imageView);

    }

    public static Bitmap retriveThumbnailFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    public static void loadCircularImageFromUrl(ImageView imageView, String url) {
        Glide.with(Singleton.getInstance().getContext())
                .load(url)
                .placeholder(R.drawable.ic_account)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);
    }

    @SuppressLint("ResourceAsColor")
    public static void setbackgroundColor(View view) {
        view.setBackgroundColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getRegisterEnterBackground()));
    }


    public static void setButtonColorWithDrawable(Button btn, String backGroundColor, String fontColor) {
        btn.setTextColor(Color.parseColor(fontColor));
        GradientDrawable drawable = (GradientDrawable) btn.getBackground();
        drawable.setColor(Color.parseColor(backGroundColor));
        //drawable.setColor(Color.parseColor(Singleton.getInstance().getFairData().getFair().getOptions().getRegisterEnterBackground()));
    }

    public static void setButtonColorWithDrawableAndStroke(Button btn, String backGroundColor, String fontColor, int stroke) {
        GradientDrawable drawable = (GradientDrawable) btn.getBackground();
        if (fontColor.equalsIgnoreCase("#fff")) {
            btn.setTextColor(Color.parseColor("#ffffff"));
            drawable.setStroke(stroke, Color.parseColor("#ffffff"));
        } else {
            btn.setTextColor(Color.parseColor(fontColor));
            drawable.setStroke(stroke, Color.parseColor(fontColor));
        }


        drawable.setColor(Color.parseColor(backGroundColor));
    }


//    public static void setButtonBackgroundAndTextColor(Button btn, String backgroundColor,String fontColor) {
//        RippleDrawable drawable = (RippleDrawable) btn.getBackground();
//        int color = Color.parseColor(backgroundColor);
//        drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
//
//        //drawable.setColor(Color.parseColor(backgroundColor));
//        btn.setTextColor(Color.parseColor(fontColor));
//
//    }

    public static void endShimmer(ShimmerFrameLayout shimmerFrameLayout) {
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
    }


    public static void failureResponse(ProgressBar progressBar, View view) {
        if (progressBar != null)
            hideProgressBar(progressBar);
        somethingWentWrong(view);
    }

    public static void noDataFound(View view) {
        try {
            ConstraintLayout noDataLayout;
            noDataLayout = view.findViewById(R.id.noDataLayout);
            TextView noDataTxt = noDataLayout.findViewById(R.id.noDataTxt);
//            TextView retryTxt = noDataLayout.findViewById(R.id.retryTxt);
            ImageView cast_button_type_empty = noDataLayout.findViewById(R.id.cast_button_type_empty);
            Singleton.changeIconColor(R.drawable.empty_data, cast_button_type_empty);
            setTextColor(noDataTxt);
            noDataLayout.setVisibility(View.VISIBLE);
            noDataTxt.setText(Objects.requireNonNull(Singleton.getKeywords()).getTrans_string_oops_no_data_found());
//            setTextColor(retryTxt);

        } catch (Exception e) {

        }

    }

    public static void somethingWentWrong(View view) {
        try {
            ConstraintLayout noDataLayout;
            noDataLayout = view.findViewById(R.id.somethingWentWrongLayout);
            TextView noDataTxt = noDataLayout.findViewById(R.id.noDataTxt);
            TextView retryTxt = noDataLayout.findViewById(R.id.retryTxt);
            ImageView cast_button_type_empty = noDataLayout.findViewById(R.id.cast_button_type_empty);
            Singleton.changeIconColor(R.drawable.empty_data, cast_button_type_empty);
            setTextColor(noDataTxt);
            setTextColor(retryTxt);
            noDataLayout.setVisibility(View.VISIBLE);
        } catch (Exception e) {

        }
    }

    public static void hideLayouts(View view) {
        ConstraintLayout layout;
        try {
            layout = view.findViewById(R.id.somethingWentWrongLayout);
            layout.setVisibility(View.GONE);
        } catch (Exception e) {

        }
        try {
            layout = view.findViewById(R.id.noDataLayout);
            layout.setVisibility(View.GONE);
        } catch (Exception e) {

        }

    }


    public static void clearUserData() {
        SharedPreferences.Editor editor = Singleton.getInstance().getSharedPreferences().edit();
        Singleton.getInstance().setLoginData(null);
        Singleton.getInstance().setFairData(null);
        editor.clear();
        editor.apply();
        editor.commit();
//        for (int i = 0; i < ((AppCompatActivity) Singleton.getInstance().getContext()).getSupportFragmentManager().getBackStackEntryCount(); i++) {
//            ((AppCompatActivity) Singleton.getInstance().getContext()).getSupportFragmentManager().popBackStack();
//        }

        Singleton.getInstance().setIsLoggedIn(null);
        Singleton.getInstance().setLogout(true);
        Singleton.getInstance().setLoginData(null);
        Singleton.getInstance().setFairData(null);
        Singleton.getInstance().getDrawerLayout().close();
        Singleton.getInstance().getBottomNavigationView().setVisibility(View.GONE);

        // getSupportFragmentManager().popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//        ((AppCompatActivity) Singleton.getInstance().getContext()).getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, new Splash())
//                .setReorderingAllowed(true)
//                .addToBackStack(null)
//                .commit();


        Intent intent = Singleton.getInstance().getActivity().getIntent();
        Singleton.getInstance().getActivity().finish();
        Singleton.getInstance().getActivity().startActivity(intent);

    }


    public static Context adjustFontSize(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        // This will apply to all text like -> Your given text size * fontScale
        configuration.fontScale = 10.0f;

//        Settings.System.putFloat(context.getContentResolver(),
//                Settings.System.FONT_SCALE, (float) 4.0);

        return context.createConfigurationContext(configuration);
    }

    public static boolean isEmailValid(String email) {
        String email2 = "";

        if (email.contains("@")) {
            String[] testStr = email.split("@");
            if (testStr.length > 1) {
                email2 = (testStr[1]);
            } else return false;
        } else {
            return false;
        }
        String expression = "([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email2);
        return matcher.matches();
    }

//    public static void getConfigurationForUser(ContentResolver cr,
//                                               Configuration outConfig, int userHandle) {
//        outConfig.fontScale = Settings.System.getFloatForUser(
//                cr, FONT_SCALE, outConfig.fontScale, userHandle);
//        if (outConfig.fontScale < 0) {
//            outConfig.fontScale = 1;
//        }
//    }


    public static void HomeAct(MainActivity act) {
        act.getLocation();
    }


    public static void openEmail(String email) {
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
//                email.putExtra(Intent.EXTRA_SUBJECT, "Waqas");
//                email.putExtra(Intent.EXTRA_TEXT, "Afzal");

            emailIntent.setType("message/rfc822");
            Singleton.getInstance().getContext().startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
        } catch (android.content.ActivityNotFoundException e) {
            Helper.showToast("There is no email client installed.");
            // Toast.makeText(Singleton.getInstance().getContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void startZoom(String meetingNumber, String password, String userName) {
//        MeetingService meetingService = ZoomSDK.getInstance().getMeetingService();
//        JoinMeetingOptions joinMeetingOptions = new JoinMeetingOptions();
//        JoinMeetingParams params = new JoinMeetingParams();
//        params.displayName = userName;
//        params.password = password;
//        params.meetingNo = meetingNumber;
//        meetingService.joinMeetingWithParams(Singleton.getInstance().getContext(), params, joinMeetingOptions);
//        meetingService.addListener(new MeetingServiceListener() {
//            @Override
//            public void onMeetingStatusChanged(MeetingStatus meetingStatus, int i, int i1) {
//                //  Helper.showToast("Wasd");
//
//            }
//        });
//        meetingService.addDialOutListener(new DialOutStatusListener() {
//            @Override
//            public void onDialOutStatusChanged(int i) {
//                // Helper.showToast("Wasd");
//            }
//        });
    }

    public static void goToUrl(String url) {
        if (url != null && !url.equals("")) {
            try {
                Uri uriUrl = Uri.parse(url);
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                Singleton.getInstance().getContext().startActivity(launchBrowser);
            } catch (Exception e) {

            }
        } else
            Helper.showToast("Not Available");
    }

    public static void lockOrientation() {
        Singleton.getInstance().getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    public static void underLineText(TextView textView, String text) {

        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);

    }


    public static void startWhereBy() {
        Singleton.getInstance().getActivity().startActivity(new Intent(Singleton.getInstance().getContext(), WebViewActivity.class));
    }

    public static void startMsTeams(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        Singleton.getInstance().getContext().startActivity(browserIntent);
    }

    public static void startLiveWebinar(String url) {
        Helper.goToUrl(url);

    }

    public static void startUserChat(String name, String id, String type) {
        Intent messageIntent = new Intent(Singleton.getInstance().getContext(), CometChatMessageListActivity.class);
        messageIntent.putExtra("name", name);
        messageIntent.putExtra("uid", id);
        messageIntent.putExtra("type", type);
        Singleton.getInstance().getContext().startActivity(messageIntent);
    }

    public static void viewDocument(String documentLink, boolean download) {
        if (download) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(documentLink)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Singleton.getInstance().getContext().startActivity(browserIntent);
        } else {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setDataAndType(Uri.parse(documentLink), "application/pdf");

            Intent chooser = Intent.createChooser(browserIntent, "Choose");
            chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // optional

            Singleton.getInstance().getActivity().startActivity(chooser);
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.parse( "http://docs.google.com/viewer?url=" + documentLink), "text/html");
//
//        Singleton.getInstance().getContext().startActivity(intent);
        }
//        Fragment fragment = new ViewDocumentFragment();
//            Bundle args = new Bundle();
//            args.putString("link", documentLink);
//            fragment.setArguments(args);
//            Singleton.getInstance().getHomeActivity().getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, fragment)
//                    .setReorderingAllowed(true)
//                    .addToBackStack("document")
//                    .commit();


    }

    public static void downloadImage(String documentLink, boolean download) {
        if (download) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(documentLink));
            Singleton.getInstance().getContext().startActivity(browserIntent);
        } else {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setDataAndType(Uri.parse(documentLink), "image/*");

            Intent chooser = Intent.createChooser(browserIntent, "Choose");
            chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // optional

            Singleton.getInstance().getActivity().startActivity(chooser);

        }


    }

    public static void viewDocument(String documentLink) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(documentLink)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Singleton.getInstance().getContext().startActivity(browserIntent);

    }

    public static void goToLoginPage() {
//        Singleton.getInstance().setLoginFragmentFlag(true);
//        FragmentTransaction fragmentTransaction = Singleton.getInstance().getActivity().getgetChildFragmentManager().beginTransaction();
//        FragmentManager fm = getChildFragmentManager();
//        for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
//            getChildFragmentManager().popBackStackImmediate();
//        }
//        Singleton.getInstance().setUserLogin(null);
//        fm.popBackStackImmediate();
//        Singleton.getInstance().setFlag(true);
//        fragmentTransaction.replace(R.id.frameLayout, new Login());
//        fragmentTransaction.commit();
//        Singleton.getInstance().getBottomNavigationView().setVisibility(View.GONE);
//        Singleton.getInstance().getNav_bottom_home().setVisibility(View.GONE);

    }


    //    public static boolean initializeTeams() {
//        getAllPermissions();
//        return createAgent();
//
//    }
//
//   public static CallAgent callAgent;
//
//    public static boolean createAgent() {
//
//        String userToken = "<User_Access_Token>";
//
//        try {
//            CommunicationTokenCredential credential = new CommunicationTokenCredential(userToken);
//            callAgent = new CallClient().createCallAgent(Singleton.getInstance().getContext(), credential).get();
//            return true;
//        } catch (Exception ex) {
//            Toast.makeText(Singleton.getInstance().getContext(), "Failed to create call agent.", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//    }
//
//    public static void getAllPermissions() {
//        String[] requiredPermissions = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};
//        ArrayList<String> permissionsToAskFor = new ArrayList<>();
//        for (String permission : requiredPermissions) {
//            if (ActivityCompat.checkSelfPermission(Singleton.getInstance().getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
//                permissionsToAskFor.add(permission);
//            }
//        }
//        if (!permissionsToAskFor.isEmpty()) {
//            ActivityCompat.requestPermissions(Singleton.getInstance().getActivity(), permissionsToAskFor.toArray(new String[0]), 1);
//        }
//    }
//
//    public static void startCall() {
//        String calleeId = "asd";
//
//        StartCallOptions options = new StartCallOptions();
//
//        callAgent.startCall(
//                Singleton.getInstance().getContext(),
//                new CommunicationUserIdentifier[]{new CommunicationUserIdentifier(calleeId)},
//                options);
//    }
    public static void launchZoomUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MainApp.getAppContext().startActivity(intent);
    }

    public static String getVimeoVideoLink(String videoURL) {
        vimeoUrl = null;
        VimeoExtractor.getInstance().fetchVideoWithURL(videoURL, null, new OnVimeoExtractionListener() {
            @Override
            public void onSuccess(VimeoVideo video) {
                vimeoUrl = video.getStreams().get("720p");
                //...
            }

            @Override
            public void onFailure(Throwable throwable) {
                //Error handling here
                vimeoUrl = null;
            }
        });
        return vimeoUrl;
    }
}
