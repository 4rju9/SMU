package com.example.arjun.su_bca.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.arjun.su_bca.R;

public class CreateAccountEmail extends AppCompatActivity {

    private EditText emailEditText;
    private Button nextButton;
    private TextView loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_email);

        setupUIViews();
        buttonFunctioning();

    }

    private void setupUIViews() {
        emailEditText = (EditText) findViewById(R.id.createAccountEmailEditText);
        nextButton = (Button) findViewById(R.id.createAccountEmailButton);
        loginButton = (TextView) findViewById(R.id.tvLoginButtonCreateAccountEmail);
    }

    private void buttonFunctioning() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = getIntent().getStringExtra("name");
                String email = emailEditText.getText().toString();
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEditText.setError("Email is invalid");
                } else {
                    Intent intent = new Intent(CreateAccountEmail.this, CreateAccountPassword.class);
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    startActivity(intent);
                }
            }
        });
        loginButton.setOnClickListener((v)-> {
            startActivity(new Intent(CreateAccountEmail.this, LoginActivity.class));
            finish();
        });
    }

}