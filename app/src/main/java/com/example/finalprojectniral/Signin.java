package com.example.finalprojectniral;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Signin extends AppCompatActivity {
    private TextView SignInn;
    private EditText etUserNmaeEmail;     // هنا المستخدم يدخل ال Username أو الايميل
    private EditText etPassword;    // هنا يدخل ال Password
    private TextView Un2;
    private TextView Psw2;
    private Button Enter,forgetyourpw ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.signin);
        etUserNmaeEmail =findViewById(R.id.etUserNmaeEmail);
        etPassword=findViewById(R.id.etPassword);
        Un2=findViewById(R.id.Un2);
        Psw2=findViewById(R.id.Pw2);
        Enter=findViewById(R.id.Enter);
        forgetyourpw=findViewById(R.id.forgetyourpw);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ⚠ معالجة الضغط على زر Enter
        Enter.setOnClickListener(v -> {

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
    public void onClickForget(View v){
        Intent intent = new Intent(Signin.this, ForgetUrPW.class);
        startActivity(intent);
    }


}
