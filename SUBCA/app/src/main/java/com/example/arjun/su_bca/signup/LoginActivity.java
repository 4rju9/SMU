package com.example.arjun.su_bca.signup;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.arjun.su_bca.R;
import com.example.arjun.su_bca.UserClassSpinner;
import com.example.arjun.su_bca.Utils.UserCredentialsModel;
import com.example.arjun.su_bca.Utils.utility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    
    private EditText emailEditText, passEditText;
    private Button loginButton, createAccountButton;
    private TextView forgotButton;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        setupUIViews();
        allButtonFunctioning();
        
    }
    
    private void setupUIViews() {
        emailEditText = (EditText) findViewById(R.id.loginActivityEmailEditText);
        passEditText = (EditText) findViewById(R.id.loginActivityPassEditText);
        loginButton = (Button) findViewById(R.id.loginActivityLoginButton);
        createAccountButton = (Button) findViewById(R.id.loginActivityCreateAccountButton);
        forgotButton = (TextView) findViewById(R.id.tvForgotPasswordLoginActivity);
        progressBar = (ProgressBar) findViewById(R.id.loginActivityProgressBar);
        sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
    }

    private void allButtonFunctioning() {
        loginButton.setOnClickListener((v)-> loginUser());
        forgotButton.setOnClickListener((v)-> forgotPassword());
        createAccountButton.setOnClickListener((v)-> startActivity(new Intent(LoginActivity.this, CreateAccountName.class)));
    }

    private void loginUser() {

        String email = emailEditText.getText().toString();
        String password = passEditText.getText().toString();

        boolean isValidated = validateCredentials(email, password);

        if(!isValidated) {
            return;
        }

        loginAccountInFirebase(email, password);

    }

    private void loginAccountInFirebase(String email, String password) {
        changeInProgress(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {

                // check if user has verified their mail.
                if(firebaseAuth.getCurrentUser().isEmailVerified()) {

                    DocumentReference db = FirebaseFirestore.getInstance().collection("Users").document(firebaseAuth.getCurrentUser().getUid());
                    db.get().addOnCompleteListener(task1 -> {
                        if(task1.isSuccessful()) {
                            changeInProgress(false);
                            DocumentSnapshot result = task1.getResult();
                            if(result.exists() && result!=null) {
                                UserCredentialsModel model = task1.getResult().toObject(UserCredentialsModel.class);
                                String[] full_name = model.getName().split(" ");
                                sharedPreferences.edit().putString("user_first_name", full_name[0]).apply();
                                sharedPreferences.edit().putBoolean("is_user_admin", model.isAdmin()).apply();
                                if(!password.equals(model.getPassword())) {
                                    model.setPassword(password);
                                    db.set(model);
                                }
                            }

                        } else {
                            changeInProgress(false);
                            utility.showToast(LoginActivity.this, "Failed To Load Details.");
                        }
                    });

                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(LoginActivity.this, UserClassSpinner.class);
                        startActivity(intent);
                        finish();
                    }, 1000);

                } else {
                    changeInProgress(false);
                    utility.showToast(LoginActivity.this, "Please verify your email to activate your account.");
                }

            } else {
                changeInProgress(false);
                utility.showToast(LoginActivity.this, task.getException().getLocalizedMessage());
            }
        });
    }

    private void changeInProgress(boolean inProgress) {
        if(inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        }
    }

    private boolean validateCredentials(String email, String password) {
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email is invalid");
            return false;
        }
        if(password.length()<6) {
            passEditText.setError("Password is too short");
            return false;
        }
        return true;
    }

    private void forgotPassword() {
        
        // Go To Forgot Activity.

        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        
    }
    
    

}