package com.example.finalprojectniral;

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

public class Signin extends AppCompatActivity {
    private TextView SignInn;
    private TextView edt;     // هنا المستخدم يدخل ال Username أو الايميل
    private TextView edt2;    // هنا يدخل ال Password
    private TextView Un2;
    private TextView Psw2;
    private Button Enter;
    private TextView FG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.signin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SignInn= findViewById(R.id.SignInn);
        edt=findViewById(R.id.edt);
        edt2=findViewById(R.id.edt2);
        Un2=findViewById(R.id.Un2);
        Psw2=findViewById(R.id.Pw2);
        Enter=findViewById(R.id.Enter);
        FG=findViewById(R.id.FG);

        // ⚠ معالجة الضغط على زر Enter
        Enter.setOnClickListener(v -> {

            // استخراج الحقول
            String username = edt.getText().toString();
            String password = edt2.getText().toString();

            // فحص القيم
            boolean ok = validateSignIn(username, password);

            if (ok) {
                // إذا كل شيء سليم → انتقال للشاشة التالية
                Intent intent = new Intent(Signin.this, Mainalmain.class);
                startActivity(intent);
                finish();
            } else {
                // إذا في خطأ
                Toast.makeText(Signin.this, "الرجاء تعبئة الحقول بشكل صحيح", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // -----------------------------------------------------------
    //  دالة تفحص القيم بمساعدة متغير بولياني مساعد
    // -----------------------------------------------------------
    private boolean validateSignIn(String username, String password) {

        boolean isValid = true; // المتغير المساعد

        // فحص الاسم
        if (username == null || username.trim().isEmpty()) {
            isValid = false;
        }

        // فحص كلمة السر
        if (password == null || password.length() < 6) {
            isValid = false;
        }

        return isValid;
    }


    public void onClickGo22(View v){
        Intent intent = new Intent(Signin.this, Mainalmain.class);
        startActivity(intent);
        finish();
    }
    private void saveUser(String username, String password){
        getSharedPreferences("UserData", MODE_PRIVATE)
                .edit()
                .putString("username", username)
                .putString("password", password)
                .apply();
    }

}
