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
        // تحقق من أن القيمة صحيحة قبل مناقشة اسم المستخدم وكلمة المرور
        if(isValid) {
            // حفظ البيانات المستخدمة في قاعدة البيانات الخاصة بالتطبيق
            // يوجد قاعدة بيانات واحدة فقط تسمى "UserData"
            // يمكن استخدامها لتخزين البيانات المستخدمة في التطبيق
            // يمكن الوصول إليها باستخدام الدالة getSharedPreferences
            // الدالة تستقبل اسم القاعدة المراد الوصول إليها كمعلمة والنوع المستخدم لها كمعلمة اخرى
            // النوع MODE_PRIVATE يعني أن البيانات سرية ولا يمكن الوصول إليها من قبل البرامج الأخرى
            SharedPreferences userData = getSharedPreferences("UserData", MODE_PRIVATE);
            // الدالة getString تستخدم لجلب قيمة نصية من القاعدة المسجلة لها
            // الدالة تستقبل اسم المفتاح الذي تريد الوصول إليه كمعلمة وقيمة الإفتراضية كمعلمة اخرى
            // في حال كان المفتاح غير موجود في القاعدة سيتم عرض القيمة الإفتراضية المحددة
            String username1 = userData.getString("username", "");
            if (username1.equals(username) && password.equals(password)) {

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
}
