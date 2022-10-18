package com.vrd.gsaf.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.google.gson.Gson;
import com.vrd.gsaf.R;
import com.vrd.gsaf.api.responses.login.Login;
import com.vrd.gsaf.constants.AppConstants;
import com.vrd.gsaf.databinding.ActivityPdfviewerBinding;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class PDFViewerActivity extends AppCompatActivity{

    ActivityPdfviewerBinding binding;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfviewerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        url=getIntent().getStringExtra("url");
        binding.pbLoadingpdf.setVisibility(View.VISIBLE);
        binding.toolbar.setBackgroundColor(Singleton.getTopNavColor());
        binding.title.setTextColor(Singleton.getTopNavInnerTextColor());
        binding.mIvBack.setOnClickListener(v->{
            onBackPressed();
        });
        new RetrieveFeedTask().execute(url);


    }
    


    class RetrieveFeedTask extends AsyncTask<String, Void, InputStream> {

        private Exception exception;

        protected InputStream doInBackground(String... urls) {
//            binding.pbLoadingpdf.setVisibility(View.VISIBLE);
            InputStream inputStream = null;
            try {
                URL url = new URL(urls[0]);
                // below is the step where we are creating our connection.
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    //response is success.
                    //we are getting input stream from url and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (Exception e) {
                //this is the method to handle errors.
                e.printStackTrace();


            }
            return  inputStream;
        }

        protected void onPostExecute(InputStream inputStream) {
            binding.PDFView.fromStream(inputStream).onLoad(new OnLoadCompleteListener() {
                @Override
                public void loadComplete(int nbPages) {
                    binding.pbLoadingpdf.setVisibility(View.GONE);
                }
            }).load();

        }
    }

}

