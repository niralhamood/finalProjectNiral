package com.example.finalprojectniral; // تعريف الحزمة (Package) الخاصة بالمشروع

// استيراد المكتبات اللازمة للعمل في التطبيق
import android.annotation.SuppressLint; // للتعامل مع تحذيرات الـ Lint (التحقق من الكود)
import android.app.NotificationChannel; // لإنشاء قناة التنبيهات (مطلوب لأندرويد 8 فما فوق)
import android.app.NotificationManager; // لإدارة التنبيهات في نظام أندرويد
import android.content.Context; // للوصول إلى موارد النظام وخدماته
import android.os.Build; // للتحقق من إصدار نظام الأندرويد الحالي
import android.os.Bundle; // لتمرير البيانات وحفظ حالة واجهة المستخدم
import android.os.Handler; // لجدولة المهام وتكرارها (مثل إرسال التنبيهات دورياً)
import android.widget.Button; // لعنصر الزر القابل للنقر في الواجهة
import android.widget.RadioButton; // لعنصر زر الاختيار الواحد
import android.widget.RadioGroup; // لمجموعة أزرار الاختيار (يسمح باختيار واحد فقط)
import android.widget.TextView; // لعنصر عرض النصوص على الشاشة
import android.widget.Toast; // لعرض رسائل قصيرة سريعة تظهر وتختفي
import android.Manifest; // للوصول إلى تعريفات الأذونات (Permissions)
import android.content.pm.PackageManager; // للتحقق من حالة الأذونات في الجهاز

import androidx.appcompat.app.AppCompatActivity; // الفئة الأساسية للأنشطة التي تدعم الإصدارات القديمة
import androidx.core.app.ActivityCompat; // فئة مساعدة لطلب الأذونات من المستخدم
import androidx.core.content.ContextCompat; // فئة مساعدة للتحقق من الأذونات بشكل متوافق
import androidx.core.app.NotificationCompat; // فئة لبناء التنبيهات بطريقة متوافقة مع كل الإصدارات

import java.util.Random; // مكتبة لتوليد أرقام عشوائية (لاختيار رسالة عشوائية)

// تعريف كلاس النشاط (Activity) الذي يتحكم في شاشة "Stay Inspired"
public class StayInspired extends AppCompatActivity {

    // تعريف متغيرات لتمثيل عناصر واجهة المستخدم (UI Components)
    RadioGroup modeRadioGroup; // مجموعة خيارات الأنماط (تحفيزي أو تأنيب ضمير)
    RadioButton rbMotivational, rbGuiltDriven; // الخيار التحفيزي وخيار تأنيب الضمير
    Button btnStart; // الزر الذي يبدأ أو يوقف التنبيهات
    TextView tvStatus; // نص يظهر حالة الخدمة حالياً (نشطة أم لا)

    boolean isRunning = false; // متغير منطقي (true/false) لتتبع هل التنبيهات تعمل الآن أم لا


    Handler handler = new Handler(); // يستخدم لجدولة أو تنفيذ المهام في الوقت المناسب. يمكن استخدامه للتنفيذ الكود في وقت لاحق (مثل عمل تنبيه بعد 3 ثواني) أو لتكرار المهام بشكل متكرر (مثل إرسال تنبيه كل 15 ثانية). هذا الكائن مهم في هذه الصفحة لأنه يستخدم لإدارة تكرار التنبيهات في الوقت المناسب.

    // مصفوفة نصية تحتوي على رسائل تشجيعية
    String[] motivationalMessages = {
            "You're doing great 💪",
            "Keep going, don't stop!",
            "Every step matters ✨",
            "Believe in yourself ❤️"
    };

    // مصفوفة نصية تحتوي على رسائل تهدف لتأنيب الضمير للتحفيز على الدراسة
    String[] guiltMessages = {
            "Why are you still not studying?",
            "Others are working harder than you!",
            "Your future depends on this...",
            "Stop wasting time. Start NOW."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) { // الدالة التي تعمل عند فتح الشاشة مباشرة
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stay_inspired); // ربط الكود بملف التصميم (XML)

        // ربط المتغيرات المعرفة بالأعلى بالعناصر الموجودة في التصميم باستخدام ID الخاص بكل عنصر
        modeRadioGroup = findViewById(R.id.modeRadioGroup);
        rbMotivational = findViewById(R.id.rbMotivational);
        rbGuiltDriven = findViewById(R.id.rbGuiltDriven);
        btnStart = findViewById(R.id.btnStart);
        tvStatus = findViewById(R.id.tvStatus);

        createNotificationChannel(); // استدعاء دالة لإنشاء قناة التنبيهات (ضروري للأجهزة الحديثة)

        // ضبط ما سيحدث عند الضغط على زر البدء/الإيقاف
        btnStart.setOnClickListener(v -> {

            // التحقق مما إذا كان المستخدم قد اختار أي نمط (تحفيزي أو تأنيب) قبل البدء
            if (modeRadioGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Please choose a mode first", Toast.LENGTH_SHORT).show();
                return; // التوقف عن التنفيذ إذا لم يتم الاختيار
            }

            // التحقق من إذن التنبيهات (مطلوب في أندرويد 13 "Tiramisu" وما فوق)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    // إذا لم يكن الإذن ممنوحاً، نقوم بطلبه من المستخدم
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
                    return;
                }
            }


        });
    }

    // دالة لتهيئة وبدء تكرار التنبيهات




    // دالة تختار رسالة عشوائية من المصفوفات المناسبة
    private String getRandomMessage() {
        Random random = new Random();

        if (rbMotivational.isChecked()) { // إذا كان خيار "تحفيزي" هو المحدد
            return motivationalMessages[random.nextInt(motivationalMessages.length)]; // إرجاع رسالة عشوائية من مصفوفة التحفيز
        } else { // إذا كان خيار "تأنيب الضمير" هو المحدد
            return guiltMessages[random.nextInt(guiltMessages.length)]; // إرجاع رسالة عشوائية من مصفوفة التأنيب
        }
    }

    // دالة لبناء التنبيه الفعلي وإظهاره في شريط التنبيهات
    @SuppressLint("NotificationPermission")
    private void sendNotification(String message) {

        // إعداد مواصفات التنبيه (الأيقونة، العنوان، النص، الأولوية)
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(android.R.drawable.ic_dialog_info) // أيقونة صغيرة تظهر في الشريط العلوي
                .setContentTitle("Stay Inspired 💡") // عنوان التنبيه
                .setContentText(message) // نص الرسالة الذي سيظهر للمستخدم
                .setPriority(NotificationCompat.PRIORITY_HIGH); // جعل التنبيه يظهر في الأعلى بوضوح

        // الحصول على خدمة إدارة التنبيهات من النظام
        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // إرسال التنبيه للنظام ليقوم بعرضه (استخدام رقم عشوائي كـ ID لكي تظهر التنبيهات فوق بعضها ولا تختفي)
        manager.notify(new Random().nextInt(), builder.build());
    }

    // دالة لإنشاء "قناة تنبيهات" (مطلوب ابتداءً من إصدار Android 8.0 Oreo لكي تظهر التنبيهات)
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // التحقق من أن الإصدار هو 8.0 أو أحدث

            NotificationChannel channel = new NotificationChannel(
                    "channel_id", // معرف القناة (يجب أن يتطابق مع المعرف المستخدم في Builder)
                    "Stay Inspired Channel", // اسم القناة الذي يظهر للمستخدم في إعدادات النظام
                    NotificationManager.IMPORTANCE_HIGH // درجة الأهمية (عالية تجعل التنبيه يصدر صوتاً ويظهر فجأة)
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel); // تسجيل القناة رسمياً في النظام
        }
    }
}
