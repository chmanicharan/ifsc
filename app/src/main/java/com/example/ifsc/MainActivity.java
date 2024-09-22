package com.example.ifsc;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText ifscCodeEditText;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ifscCodeEditText = findViewById(R.id.ifscCodeEditText);
        resultTextView = findViewById(R.id.resultTextView);
    }

    public void validateIFSC(View view) {
        String ifscCode = ifscCodeEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(ifscCode)) {
            new ValidateIFSCAsyncTask().execute(ifscCode);
        } else {
            resultTextView.setText("Invalid IFSC code.");
        }
    }

    private class ValidateIFSCAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String ifscCode = params[0];
            String apiUrl = "https://ifsc.razorpay.com/" + ifscCode;

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    reader.close();
                    inputStream.close();
                    return stringBuilder.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject response = new JSONObject(result);
                    String bankName = response.optString("BANK");
                    String branchName = response.optString("BRANCH");
                    String address = response.optString("ADDRESS");
                    String city = response.optString("CITY");
                    String state = response.optString("STATE");

                    String validationMessage = "Bank: " + bankName + "\n" +
                            "Branch: " + branchName + "\n" +
                            "Address: " + address + "\n" +
                            "City: " + city + "\n" +
                            "State: " + state;

                    resultTextView.setText(validationMessage);
                } catch (JSONException e) {
                    e.printStackTrace();
                    resultTextView.setText("Invalid IFSC code.");
                }
            } else {
                resultTextView.setText("Invalid IFSC code.");
            }
        }
    }
}
