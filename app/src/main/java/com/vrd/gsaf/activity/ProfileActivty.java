package com.vrd.gsaf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.vrd.gsaf.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivty extends AppCompatActivity {

    TextView user_detail;
    String firstName, lastName, userEmail;
    Button logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_activty);
        user_detail = findViewById(R.id.userDetail);
        logout = findViewById(R.id.logout_button);

        fetchuserData();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LISessionManager.getInstance(getApplicationContext()).clearSession();
                Toast.makeText(getApplicationContext(), "Logout Successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ProfileActivty.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void fetchuserData() {
        String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,email-address)";

        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                // Success!
                try {
                    JSONObject jsonObject = apiResponse.getResponseDataAsJson();
                    firstName = jsonObject.getString("firstName");
                    lastName = jsonObject.getString("lastName");
                    userEmail = jsonObject.getString("emailAddress");

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("First Name " + firstName + "\n\n");
                    stringBuilder.append("Last Name " + lastName + "\n\n");
                    stringBuilder.append("Email " + userEmail);

                    user_detail.setText(stringBuilder);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onApiError(LIApiError liApiError) {
                // Error making GET request!
                Toast.makeText(getApplicationContext(), "API Error" + liApiError.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

}