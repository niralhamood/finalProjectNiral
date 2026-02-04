package com.example.finalprojectniral;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Signup extends AppCompatActivity {
    private TextView ed;
    private TextView ed2;
    private TextView Un;
    private TextView Pw;
    private Button SignUp;
    private TextView textView4;
    private Button SignIn;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SignUp.setOnClickListener(v -> {

            String username = ed.getText().toString();
            String password = ed2.getText().toString();

            // أولاً نفحص هل الحقول فارغة
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "الرجاء تعبئة جميع الحقول", Toast.LENGTH_SHORT).show();
                return;
            }

            // ثانياً نفحص هل المستخدم موجود أصلاً
            boolean exists = check(username);

            if (exists) {
                Toast.makeText(this, "هذا الحساب موجود بالفعل", Toast.LENGTH_SHORT).show();
            } else {
                // إذا ليس موجود → نسجل الحساب الجديد
                saveUser(username, password);
                Toast.makeText(this, "تم إنشاء الحساب بنجاح!", Toast.LENGTH_SHORT).show();

                // الانتقال لصفحة تسجيل الدخول
                Intent intent = new Intent(Signup.this, Signin.class);
                startActivity(intent);
                finish();
            }
        });


        ed = findViewById(R.id.ed);
        ed2 = findViewById(R.id.ed2);
        Un = findViewById(R.id.Un);
        Pw = findViewById(R.id.Pw);
        SignUp = findViewById(R.id.SignUp);
        SignIn = findViewById(R.id.SignIn);
        textView4 = findViewById(R.id.textView4);
    }

    /**
     * Called when pointer capture is enabled or disabled for the current window.
     *
     * @param hasCapture True if the window has pointer capture.
     */
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    public void onClickGo(View v) {
        Intent intent = new Intent(Signup.this, Signin.class);
        startActivity(intent);
        finish();

    }

    public void onClickGo2(View v) {
        Intent intent = new Intent(Signup.this, Mainalmain.class);
        startActivity(intent);
        finish();
    }

    private boolean check(String username) {

        boolean userExists = false;   // متغير بولياني مساعد

        // جلب اسم المستخدم المخزن
        String savedUsername = getSharedPreferences("UserData", MODE_PRIVATE)
                .getString("username", null);

        // إذا كان اسم المستخدم موجود في الذاكرة
        if (savedUsername != null && savedUsername.equals(username)) {
            userExists = true;
        }

        return userExists;
    }

    private void saveUser(String username, String password) {
        getSharedPreferences("UserData", MODE_PRIVATE)
                .edit()
                .putString("username", username)
                .putString("password", password)
                .apply();
    }


}