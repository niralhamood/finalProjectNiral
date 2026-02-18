package com.example.finalprojectniral;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private Button btnSignUp,forgetyourpw ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.signin);
        etUserNmaeEmail =findViewById(R.id.etUserNmaeEmail);
        etPassword=findViewById(R.id.etPassword);
        Un2=findViewById(R.id.Un2);
        Psw2=findViewById(R.id.Pw2);
        btnSignUp =findViewById(R.id.Enter);
        forgetyourpw=findViewById(R.id.forgetyourpw);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ⚠ معالجة الضغط على زر Enter
        btnSignUp.setOnClickListener(v -> {
            validateSignIn();
                    });
    }

    // -----------------------------------------------------------
    //  دالة تفحص القيم بمساعدة متغير بولياني مساعد
    // -----------------------------------------------------------

    //  دالة تفحص القيم بمساعدة متغير بولياني مساعد
    // -----------------------------------------------------------
    private boolean validateSignIn() {
        boolean isValid = true;

        String username = etUserNmaeEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "الرجاء تعبئة جميع الحقول", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if(isValid)
        {
            SharedPreferences userData = getSharedPreferences("UserData", MODE_PRIVATE);
            String username1 = userData.getString("username", "");
            if (username.equals(username) && password.equals(password)) {

                // ✅ الانتقال للشاشة الرئيسية
                Intent intent = new Intent(Signin.this, Mainalmain.class);
                startActivity(intent);
                finish(); // يمنع الرجوع لشاشة تسجيل الدخول

            } else {
                Toast.makeText(this, "اسم المستخدم أو كلمة المرور غير صحيحة", Toast.LENGTH_SHORT).show();
                isValid = false;
            }
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
