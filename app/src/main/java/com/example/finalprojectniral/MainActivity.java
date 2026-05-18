package com.example.finalprojectniral;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    /**
     * TextView لعرض نص التحميل أو اسم التطبيق أثناء شاشة الترحيب.
     * يتم تعريفه هنا كمتغير على مستوى الفئة (Class Level) ليتم الوصول إليه في أي مكان داخل الأكتيفيتي.
     */
    private TextView tvLoading;

    /**
     * دالة onPointerCaptureChanged:
     * تُستدعى هذه الدالة عندما تتغير حالة "التقاط المؤشر" (Pointer Capture).
     * تُستخدم غالباً في أجهزة التحكم التي تتطلب دقة عالية مثل الماوس في الألعاب.
     * في أغلب التطبيقات العادية، لا نحتاج لتعديلها ولكنها موجودة كجزء من دورة حياة الواجهة.
     */
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @SuppressLint({"MissingInflatedId", "MissingSuperCall"})
    /**
     * دالة onCreate:
     * هي الدالة الأهم، حيث يبدأ نظام أندرويد بتشغيل الأكتيفيتي من هنا.
     * وظائفها الأساسية:
     * 1. إعداد الواجهة الرسومية (Layout).
     * 2. ربط المتغيرات البرمجية بالعناصر في ملف XML.
     * 3. ضبط خصائص الشاشة مثل الـ EdgeToEdge.
     * 4. بدء المؤقت للانتقال للشاشة التالية.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // تفعيل خاصية الشاشة الكاملة (Edge-to-Edge) لجعل محتوى التطبيق يمتد خلف شريط الحالة وشريط التنقل السفلي.
        EdgeToEdge.enable(this);

        // استخدام ViewCompat لضبط "الهوامش" (Padding) بشكل ديناميكي.
        // الهدف: التأكد من أن عناصر الواجهة لا تختفي خلف أشرطة النظام (ساعة الهاتف أو أزرار الرجوع).
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ربط متغير tvLoading بالعنصر الموجود في ملف activity_main.xml عبر المعرف (ID).
        tvLoading = findViewById(R.id.tvLoading);


        /**
         * Handler و Looper:
         * يستخدم الـ Handler لإرسال ومعالجة الرسائل و "Runnable" (أوامر قابلة للتنفيذ) في طابور الرسائل.
         * Looper.getMainLooper(): يضمن تنفيذ الكود على "الخيط الرئيسي" (Main Thread) المسؤول عن تحديث واجهة المستخدم.
         * postDelayed: دالة تقوم بتأخير تنفيذ الكود الموجود بداخلها لفترة زمنية محددة.
         */
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Intent: هو "رسالة نية" تخبر النظام بالانتقال من النشاط الحالي (MainActivity) إلى النشاط المستهدف (Signup).
                Intent intent = new Intent(MainActivity.this, Signup.class);

                // بدء الأكتيفيتي الجديدة.
                startActivity(intent);

                // إنهاء الأكتيفيتي الحالية (Splash Screen) حتى لا يعود المستخدم إليها عند ضغط زر الرجوع.
                finish();
            }
        }, 3000); // 3000 ميلي ثانية تعني الانتظار لمدة 3 ثوانٍ قبل الانتقال.
    }
}




