package com.example.finalprojectniral;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// استيراد كلاس المستخدم وقواعد بيانات Firebase
import com.example.finalprojectniral.data.myUserTable.MyUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * شاشة التسجيل (Signup): المسؤولة عن إنشاء حساب جديد وحفظ البيانات في Firebase
 */
public class Signup extends AppCompatActivity {
    // تعريف عناصر الواجهة (حقول النص والأزرار)
    private EditText edUsername2, edPassW;
    private Button SignUp;
    private Button signIn;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // تفعيل ميزة العرض على كامل الشاشة
        EdgeToEdge.enable(this);
        // تحديد ملف التصميم (Layout) الخاص بهذه الشاشة
        setContentView(R.layout.signup);

        // ربط المتغيرات بالعناصر الموجودة في ملف الـ XML باستخدام الـ ID
        edUsername2 = findViewById(R.id.edUsername2); // حقل اسم المستخدم
        edPassW = findViewById(R.id.edPassW);       // حقل كلمة المرور
        SignUp = findViewById(R.id.SignUp);           // زر التسجيل
        signIn = findViewById(R.id.signIn);           // زر الانتقال لتسجيل الدخول

        // ضبط الحواف لتناسب أبعاد شاشة الجهاز (مثل مكان الساعة والكاميرا)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // عند الضغط على زر "Sign In" يتم الانتقال لشاشة تسجيل الدخول
        signIn.setOnClickListener(v -> {
            Intent intent = new Intent(Signup.this, Signin.class);
            startActivity(intent);
            finish(); // إغلاق الشاشة الحالية
        });

        // عند الضغط على زر "Sign Up" تبدأ عملية فحص البيانات ثم الحفظ
        SignUp.setOnClickListener(v -> {
            // جلب النصوص المدخلة وحذف المسافات الزائدة
            String username = edUsername2.getText().toString().trim();
            String password = edPassW.getText().toString().trim();

            // 1. التأكد من أن البيانات المدخلة صحيحة (ليست فارغة والطول مناسب)
            if (isValid(username, password)) {
                // 2. إنشاء كائن مستخدم جديد وتعبئته بالبيانات
                MyUser newUser = new MyUser();
                newUser.fullName = username;
                newUser.passw = password;
                newUser.email = username; // استخدام اسم المستخدم كإيميل افتراضي حالياً

                // 3. استدعاء دالة حفظ البيانات في قاعدة بيانات Firebase السحابية
                saveUserToFirebase(newUser);
            }
        });
    }

    /**
     * دالة لفحص صحة المعطيات: تتأكد من أن الحقول ليست فارغة وأن كلمة المرور قوية كفاية
     */
    private boolean isValid(String username, String password) {
        boolean valid = true;

        // فحص حقل اسم المستخدم
        if (username.isEmpty()) {
            edUsername2.setError("الرجاء إدخال اسم المستخدم");
            valid = false;
        }
        
        // فحص حقل كلمة المرور
        if (password.isEmpty()) {
            edPassW.setError("الرجاء إدخال كلمة المرور");
            valid = false;
        } else if (password.length() < 5) {
            // كلمة المرور يجب أن لا تقل عن 5 خانات
            edPassW.setError("يجب أن تكون كلمة المرور 5 أحرف على الأقل");
            valid = false;
        }

        return valid;
    }

    /**
     * دالة حفظ المستخدم في Firebase Realtime Database (السحابة)
     */
    private void saveUserToFirebase(MyUser user) {
        // الوصول لمرجع قاعدة البيانات السحابية
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // الوصول للجدول أو العقدة المسماة "users"
        DatabaseReference usersRef = database.getReference("users");
        // إنشاء مكان جديد للمستخدم وتوليد مفتاح فريد (Unique Key) له
        DatabaseReference newUserRef = usersRef.push();
        
        // وضع المفتاح الفريد داخل كائن المستخدم لسهولة الوصول إليه مستقبلاً
        user.setUserId(newUserRef.getKey());

        // البدء بعملية رفع البيانات للسحابة مع إضافة مستمع (Listener) للنتائج
        newUserRef.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // في حال نجاح الحفظ، تظهر رسالة للمستخدم ويتم حفظ البيانات محلياً أيضاً
                    Toast.makeText(Signup.this, "تم إنشاء الحساب وحفظ البيانات بنجاح!", Toast.LENGTH_SHORT).show();
                    saveUserLocally(user.fullName, user.passw);
                    // الانتقال لشاشة تسجيل الدخول
                    Intent intent = new Intent(Signup.this, Signin.class);
                    startActivity(intent);
                    finish();
                } else {
                    // في حال الفشل، يتم عرض سبب الخطأ
                    Toast.makeText(Signup.this, "فشل في حفظ البيانات: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * دالة لحفظ بيانات بسيطة في ذاكرة الهاتف (SharedPreferences)
     */
    private void saveUserLocally(String username, String password) {
        getSharedPreferences("UserData", MODE_PRIVATE)
                .edit()
                .putString("username", username)
                .putString("password", password)
                .apply();
    }
}
