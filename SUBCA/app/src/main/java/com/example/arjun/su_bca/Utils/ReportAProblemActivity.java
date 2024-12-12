package com.example.arjun.su_bca.Utils;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.example.arjun.su_bca.R;

import java.util.HashMap;
import java.util.Map;

public class ReportAProblemActivity extends AppCompatActivity {

    private ImageButton backButton;
    private TextView sendButton;
    private EditText name, email, subject, message;
    private RequestQueue queue;
    private final String TAG = "FormsPree";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_aproblem);

        setupUIViews();
        initButtons();

    }

    private void setupUIViews () {

        backButton = findViewById(R.id.reportAProblemBackButton);
        sendButton = findViewById(R.id.reportAProblemSendButton);
        name = findViewById(R.id.reportAProblemNameText);
        email = findViewById(R.id.reportAProblemEmailText);
        subject = findViewById(R.id.reportAProblemSubjectText);
        message = findViewById(R.id.reportAProblemMessageText);
        queue = VolleyRequestQueue.getInstance(ReportAProblemActivity.this).getQueue();

    }

    private void initButtons () {

        backButton.setOnClickListener( v -> finish());

        sendButton.setOnClickListener( v -> {

            String strName = name.getText().toString();
            String strEmail = email.getText().toString();
            String strSubject = subject.getText().toString();
            String strMessage = message.getText().toString();

            // checking if the fields are not empty.
            boolean notEmpty = checkIfFieldsEmpty(strName, strEmail, strSubject, strMessage);

            final String FORM_URL = "https://formspree.io/f/mpzkjyoq";

            if (notEmpty) {
                StringRequest request = new StringRequest(
                        Request.Method.POST,
                        FORM_URL,
                        response -> {
                            utility.showToast(ReportAProblemActivity.this, "Dispatched!");
                            finish();
                        },
                        error -> utility.showToast(ReportAProblemActivity.this, "Error while processing.\n" + error.getLocalizedMessage())) {
                    @Override
                    protected Map<String, String> getParams () {
                        Map<String, String> params = new HashMap<>();
                        params.put("name", strName);
                        params.put("email", strEmail);
                        params.put("subject", strSubject);
                        params.put("message", strMessage);
                        return params;
                    }
                };
                request.setTag(TAG);
                queue.add(request);
            }

        });

    }

    private boolean checkIfFieldsEmpty (String name, String email, String subject, String message) {

        boolean result = true;

        if (name.isEmpty()) {
            this.name.setError("Name is Required!");
            result = false;
        }
        if (email.isEmpty()) {
            this.email.setError("Email is Required!");
            result = false;
        }
        if (subject.isEmpty()) {
            this.subject.setError("Subject is Required!");
            result = false;
        }
        if (message.isEmpty()) {
            this.message.setError("Email is Required!");
            result = false;
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }
}