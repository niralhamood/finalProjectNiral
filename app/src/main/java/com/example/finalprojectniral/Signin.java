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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signin extends AppCompatActivity {
    private TextView SignInn;
    private EditText etUserNmaeEmail;     // هنا المستخدم يدخل ال Username أو الايميل
    private EditText etPassword;    // هنا يدخل ال Password
    private TextView Un2;
    private TextView Psw2;
    private Button btnSignUp, forgetyourpw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.signin);

        // 1. السطر التالي يتحقق مما إذا كان هناك مستخدم مسجل دخوله حالياً في التطبيق.
        // FirebaseAuth.getInstance() تجلب الكائن المسؤول عن المصادقة.
        // getCurrentUser() تعيد كائن المستخدم الحالي، فإذا كانت لا تساوي null، فهذا يعني أن المستخدم دخل مسبقاً ولم يقم بتسجيل الخروج.
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            // 2. إذا كان المستخدم مسجلاً بالفعل، نقوم بإنشاء "Intent" (نية انتقال).
            // المعامل الأول (Signin.this) هو السياق الحالي، والمعامل الثاني (Mainalmain.class) هو النشاط الهدف المراد الانتقال إليه.
            Intent intent = new Intent(Signin.this, Mainalmain.class);

            // 3. نقوم ببدء النشاط الجديد (الشاشة الرئيسية للبرنامج).
            // هذا يضمن أن المستخدم لا يضطر لرؤية شاشة تسجيل الدخول في كل مرة يفتح فيها التطبيق.
            startActivity(intent);

            // 4. دالة finish() مهمة جداً هنا؛ فهي تقوم بإغلاق نشاط التسجيل الحالي (Signin) وإزالته من الـ Back Stack.
            // الفائدة: إذا ضغط المستخدم على زر "الرجوع" من الشاشة الرئيسية، فلن يعود لشاشة تسجيل الدخول مرة أخرى بل سيغلق التطبيق.
            finish();
        }

        etUserNmaeEmail = findViewById(R.id.etUserNmaeEmail);
        etPassword = findViewById(R.id.etPassword);
        Un2 = findViewById(R.id.Un2);
        Psw2 = findViewById(R.id.Pw2);
        btnSignUp = findViewById(R.id.Enter);
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

    /**
     * وصف قصير: تقوم بالتحقق من صحة البيانات المدخلة (البريد الإلكتروني وكلمة المرور) وبدء عملية تسجيل الدخول عبر Firebase.
     * الهدف منها: التأكد من أن المستخدم أدخل بياناته بشكل صحيح قبل محاولة الاتصال بالخادم.
     * القيمة المُرجعة (@return): تُرجع true إذا كانت الحقول غير فارغة، و false خلاف ذلك.
     */
    private boolean validateSignIn() {
        boolean isValid = true;

        String username = etUserNmaeEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        // تحقق من أن القيمة صحيحة قبل مناقشة اسم المستخدم وكلمة المرور
        if (isValid) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Signin.this, "Signing in Succeeded", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Signin.this, Mainalmain.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(Signin.this, "Signing in Failed", Toast.LENGTH_SHORT).show();
                        etUserNmaeEmail.setError(task.getException().getMessage());
                    }
                }
            });
        }
        return isValid;
    }
}