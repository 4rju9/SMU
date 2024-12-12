package com.example.arjun.su_bca.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.arjun.su_bca.R;
import com.example.arjun.su_bca.Utils.utility;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        setupUIViews();
        allButtonFunctioning();

    }

    private void setupUIViews() {
        emailEditText = (EditText) findViewById(R.id.forgotPassEmailEditText);
        continueButton = (Button) findViewById(R.id.forgotActivityForgotButton);
    }

    private void allButtonFunctioning() {
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                if(email.isEmpty() || email.equals(null)) {
                    emailEditText.setError("Enter Email Address!");
                } else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email);
                    utility.showToast(ForgotPasswordActivity.this, "Email has been successfully.");
                    startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });
    }

}