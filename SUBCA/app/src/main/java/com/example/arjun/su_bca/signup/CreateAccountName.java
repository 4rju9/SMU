package com.example.arjun.su_bca.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.arjun.su_bca.R;

public class CreateAccountName extends AppCompatActivity {

    private EditText nameEditText;
    private Button nextButton;
    private TextView loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_name);

        setupUIViews();
        buttonFunctioning();

    }

    private void setupUIViews() {
        nameEditText = (EditText) findViewById(R.id.createAccountNameEditText);
        nextButton = (Button) findViewById(R.id.createAccountNameButton);
        loginButton = (TextView) findViewById(R.id.tvLoginButtonCreateAccountName);
    }

    private void buttonFunctioning() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                if(name.isEmpty() || name == null) {
                    nameEditText.setError("Name is required.");
                } else {
                    Intent intent = new Intent(CreateAccountName.this, CreateAccountEmail.class);
                    intent.putExtra("name", name);
                    startActivity(intent);
                }
            }
        });
        loginButton.setOnClickListener((v)-> {
            startActivity(new Intent(CreateAccountName.this, LoginActivity.class));
            finish();
        });
    }

}