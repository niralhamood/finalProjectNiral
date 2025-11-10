package com.example.finalprojectniral;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalprojectniral.Mainalmain;
import com.example.finalprojectniral.R;

public class Signup extends AppCompatActivity {

    // تعريف العناصر اللي موجودة في واجهة XML
    EditText username, password;
    Button signUp, signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup); // ربط الجافا بملف XML

        // ربط المتغيرات بالعناصر من التصميم
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        signUp = findViewById(R.id.signUp);
        signIn = findViewById(R.id.signIn);

        // حدث الضغط على زر Sign Up
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if (user.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(com.example.finalprojectniral.Signup.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(com.example.finalprojectniral.Signup.this, "Account created for " + user, Toast.LENGTH_SHORT).show();
                    // مثال: الانتقال لشاشة رئيسية بعد التسجيل
                    Intent intent = new Intent(com.example.finalprojectniral.Signup.this, Mainalmain.class);
                    startActivity(intent);
                }
            }
        });

        // حدث الضغط على زر Sign In
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if (user.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(com.example.finalprojectniral.Signup.this, "Enter your username and password", Toast.LENGTH_SHORT).show();
                } else {
                    // مثال: الانتقال إلى الشاشة الرئيسية بعد تسجيل الدخول
                    Intent intent = new Intent(com.example.finalprojectniral.Signup.this, Mainalmain.class);
                    startActivity(intent);
                }
            }
        });
    }

}