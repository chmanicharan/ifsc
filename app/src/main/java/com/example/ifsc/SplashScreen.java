package com.example.ifsc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {
    private EditText editTextName;
    private EditText editTextPassword;
    private EditText editTextEmail;
    private Button buttonRegister;

    Button loginbtn;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        editTextName = findViewById(R.id.editTextName);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextEmail = findViewById(R.id.ed1);
        buttonRegister = findViewById(R.id.button);
        loginbtn = findViewById(R.id.btn);
        databaseHelper = new DatabaseHelper(this);

        buttonRegister.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            String password = editTextPassword.getText().toString();
            String email = editTextEmail.getText().toString();



            if(name.isEmpty() || password.isEmpty() || email.isEmpty() ){
                Toast.makeText(SplashScreen.this,"Please Enter All Fields",Toast.LENGTH_SHORT).show();
            }else {
                // Insert data into the database
                databaseHelper.insertData(name, password, email);

                // Navigate to HomeScreen activity
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loginbtn.setOnClickListener(v -> {
            Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}