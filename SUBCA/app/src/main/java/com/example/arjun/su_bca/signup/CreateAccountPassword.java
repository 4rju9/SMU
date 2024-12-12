package com.example.arjun.su_bca.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.example.arjun.su_bca.R;
import com.example.arjun.su_bca.Utils.UserCredentialsModel;
import com.example.arjun.su_bca.Utils.utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateAccountPassword extends AppCompatActivity {

    private EditText passEdit, cPassEdit;
    private Button nextButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_password);

        setupUIViews();
        buttonFunctioning();

    }

    private void setupUIViews() {
        passEdit = (EditText) findViewById(R.id.createAccountPasswordEditText);
        cPassEdit = (EditText) findViewById(R.id.createAccountConfirmPasswordEditText);
        nextButton = (Button) findViewById(R.id.createAccountPasswordButton);
        progressBar = (ProgressBar) findViewById(R.id.createAccountPasswordProgressBar);
    }

    private void buttonFunctioning() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = getIntent().getStringExtra("name");
                String email = getIntent().getStringExtra("email");
                String password = passEdit.getText().toString();
                String confirmPassword = cPassEdit.getText().toString();
                if(password.length()<6) {
                    passEdit.setError("Password is too short");
                }
                if(!password.equals(confirmPassword)) {
                    cPassEdit.setError("Password doesn't match");
                } else {
                    createAccountInFirebase(name, email, password);
                }
            }
        });
    }

    private void createAccountInFirebase(String name, String email, String password) {
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    firebaseAuth.getCurrentUser().sendEmailVerification();

                    // save credentials.

                    DocumentReference db = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid());
                    db.set(new UserCredentialsModel(name, email, password, false)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            changeInProgress(false);
                            if(task.isSuccessful()) {
                                firebaseAuth.signOut();
                                utility.showToast(CreateAccountPassword.this, "Created account successfully, check your mail to verify your account");
                                startActivity(new Intent(CreateAccountPassword.this, LoginActivity.class));
                                finish();
                            }
                        }
                    });
                } else {
                    changeInProgress(false);
                    utility.showToast(CreateAccountPassword.this, task.getException().getLocalizedMessage());

                }
            }
        });

    }

    private void changeInProgress(boolean inProgress) {
        if(inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            nextButton.setVisibility(View.VISIBLE);
        }
    }

}