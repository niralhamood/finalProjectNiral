package com.example.finalprojectniral;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgetUrPW extends AppCompatActivity {

    private EditText etEmail;
    private Button btnSend;
    private TextView tvBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_ur_pw);

        // ربط العناصر
        etEmail = findViewById(R.id.etEmail);
        btnSend = findViewById(R.id.btnSend);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        // زر الإرسال
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    etEmail.setError("Please enter your email");
                    etEmail.requestFocus();
                } else {
                    // هنا تقدر تربطه بـ Firebase أو API
                    Toast.makeText(
                            ForgetUrPW.this,
                            "Reset link sent to " + email,
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        });

        // الرجوع لشاشة تسجيل الدخول
        tvBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // يرجع للشاشة السابقة
            }
        });
    }
}
