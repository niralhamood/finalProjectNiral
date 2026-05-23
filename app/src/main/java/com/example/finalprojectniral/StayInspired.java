package com.example.finalprojectniral; 
import android.Manifest;    
import android.annotation.SuppressLint; 
import android.app.AlarmManager; // استيراد مدير التنبيهات لجدولة المهام
import android.app.NotificationChannel; // استيراد فئة إنشاء قنوات الإشعارات
import android.app.NotificationManager; // استيراد مدير نظام الإشعارات
import android.app.PendingIntent; // استيراد كائن لتنفيذ إجراء مستقبلي (مثل فتح التطبيق من إشعار)
import android.content.Context; // استيراد سياق التطبيق للوصول للموارد
import android.content.Intent; // استيراد الـ Intent للانتقال بين المكونات
import android.content.SharedPreferences;
import android.content.pm.PackageManager; // استيراد أداة لفحص صلاحيات النظام
import android.os.Build; // استيراد معلومات عن إصدار نظام الأندرويد
import android.os.Bundle; // استيراد كائن لنقل البيانات بين الحالات
import android.os.SystemClock; // استيراد ساعة النظام لقياس الوقت المنقضي
import android.os.SystemClock; // استيراد ساعة النظام لقياس الوقت المنقضي
import android.widget.Button; // استيراد عنصر الزر (Button)
import android.widget.RadioButton; // استيراد زر الاختيار (RadioButton)
import android.widget.RadioGroup; // استيراد مجموعة أزرار الاختيار
import android.widget.TextView; // استيراد عنصر عرض النصوص
import android.widget.Toast; // استيراد أداة عرض رسائل قصيرة للمستخدم

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity; // استيراد فئة الأنشطة المتوافقة مع الإصدارات القديمة
import androidx.core.app.ActivityCompat; // استيراد أدوات المساعدة لطلب الصلاحيات
import androidx.core.content.ContextCompat; // استيراد أدوات المساعدة للتعامل مع موارد النظام

import java.util.Random; // استيراد أداة لتوليد أرقام عشوائية

public class StayInspired extends AppCompatActivity { // تعريف الكلاس الأساسي للشاشة

    RadioGroup modeRadioGroup; // تعريف متغير لمجموعة أزرار الاختيار
    RadioButton rbMotivational, rbGuiltDriven; // تعريف متغيرات لأزرار الاختيار الفردية
    Button btnStart; // تعريف متغير لزر البدء والإيقاف
    TextView tvStatus; // تعريف متغير لعرض حالة الخدمة

    boolean isRunning = false; // متغير منطقي لتتبع ما إذا كانت الإشعارات مفعلة أم لا

    // مصفوفة تحتوي على جمل تحفيزية إيجابية
    String[] motivationalMessages = {
            "You're doing great 💪",
            "Keep going, don't stop!",
            "Every step matters ✨",
            "Believe in yourself ❤️"
    };

    // مصفوفة تحتوي على جمل تأنيب ضمير للتحفيز
    String[] guiltMessages = {
            "Why are you still not studying?",
            "Others are working harder than you!",
            "Your future depends on this...",
            "Stop wasting time. Start NOW."
    };

    /**
     * دالة onCreate: يتم استدعاؤها عند تشغيل الشاشة لأول مرة.
     * تقوم بربط العناصر البرمجية بالتصميم وتجهيز المستمعات (Listeners).
     */

    private final ActivityResultLauncher<String> requestNotificationPermissionLauncher =
            registerForActivityResult( new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    startInspiration();
                } else {
                    Toast.makeText(this, "Notification permission denied. Cannot start service.", Toast.LENGTH_SHORT).show();
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) { // بداية دورة حياة النشاط
        super.onCreate(savedInstanceState); // استدعاء دالة الحالة الأصلية
        setContentView(R.layout.activity_stay_inspired); // ربط الكود بملف التصميم XML

        // ربط المتغيرات البرمجية بالعناصر الموجودة في التصميم عبر الـ ID
        modeRadioGroup = findViewById(R.id.modeRadioGroup); // ربط مجموعة الراديو
        rbMotivational = findViewById(R.id.rbMotivational); // ربط زر "تحفيزي"
        rbGuiltDriven = findViewById(R.id.rbGuiltDriven); // ربط زر "تأنيب"
        btnStart = findViewById(R.id.btnStart); // ربط زر البدء
        tvStatus = findViewById(R.id.tvStatus); // ربط نص الحالة

        SharedPreferences prefs = getSharedPreferences("InspirationPrefs", MODE_PRIVATE);
        isRunning = prefs.getBoolean("isRunning", false);
        boolean isMotivational = prefs.getBoolean("isMotivational", true);

        if (isRunning) {
            btnStart.setText("Stop Notifications");
            tvStatus.setText("Status: Active (Next in 30min)");
            if (isMotivational) rbMotivational.setChecked(true);
            else rbGuiltDriven.setChecked(true);
        } else {
            btnStart.setText("Start Staying Inspired");
            tvStatus.setText("Status: Inactive");
        }


        // إعداد ما سيحدث عند الضغط على زر البدء
        btnStart.setOnClickListener(v -> { // مراقب النقرات على الزر
            if (!isRunning) {
                if (modeRadioGroup.getCheckedRadioButtonId() == -1) { // التحقق إذا لم يتم اختيار أي نمط
                    Toast.makeText(this, "Please choose a mode first", Toast.LENGTH_SHORT).show(); // عرض تنبيه للمستخدم
                    return; // التوقف عن التنفيذ
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // التحقق مما إذا كان إصدار الأندرويد 13 أو أعلى
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                            != PackageManager.PERMISSION_GRANTED) { // التأكد مما إذا كانت صلاحية الإشعارات غير ممنوحة
                        requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                        return;
                    }
                }
                startInspiration(); // استدعاء دالة التشغيل
            } else { // إذا كانت الخدمة تعمل
                stopInspiration(); // استدعاء دالة الإيقاف
            }
        });
    }

    /**

     * دالة startInspiration: مسؤولة عن جدولة الإشعارات باستخدام AlarmManager.
     * شرح عن الـ AlarmManager:
     * هو خدمة من نظام أندرويد تسمح بجدولة المهام لتنفيذها في وقت محدد مستقبلاً.
     * 1. يعمل حتى لو كان التطبيق مغلقاً (Background Task).
     * 2. نستخدم فيه PendingIntent لإخبار النظام بما يجب فعله عند حلول الوقت.
     * 3. في هذا الكود، نستخدمه لإرسال إشارة للـ Receiver ليقوم بإظهار الإشعار بعد 60 ثانية.

     * تضمن وصول الإشعار حتى لو تم إغلاق التطبيق.
     */

    /**
     * وصف قصير: تقوم هذه الدالة بتشغيل عملية الإلهام عبر اختيار رسالة عشوائية وجدولة إشعار ليظهر بعد دقيقة واحدة.
     * الهدف منها: ضمان وصول رسائل تحفيزية للمستخدم حتى لو كان التطبيق مغلقاً باستخدام مدير التنبيهات (AlarmManager).
     */
    private void startInspiration() { // دالة تشغيل الإلهام
        isRunning = true;
        String message = getRandomMessage(); // الحصول على رسالة عشوائية بناءً على النمط

        // تجهيز الـ Intent الذي سيقوم بتشغيل المستقبل (Receiver) لإظهار الإشعار
        Intent intent = new Intent(this, NotificationReceiver.class); // إنشاء نية للمستقبل
        intent.putExtra("message", message); // وضع الرسالة المختارة داخل النية
        PendingIntent pendingIntent = PendingIntent.getBroadcast( // إنشاء نية معلقة للبث
                this, //  السياق الحالي(اي هاي الشاشة " ستاي انسبيرد")
                0, // كود البيندينج انتن عشان يميز الريسيفر اي بندينج انتن يشغل وانا عندي بس واحد
                intent, // النية الاصليه فيها اسم الريسيفر والرسالة والبيانات
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE //,مشروح بالدفتر ب 3 ستاي انسبيرد
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE); // الوصول لخدمة المنبه في النظام

        long triggerTime = SystemClock.elapsedRealtime() + (30 * 60 * 1000); // حساب وقت الانطلاق (بعد 30 دقيقة)
        if (alarmManager != null) { // التأكد من توفر مدير المنبهات
            // فحص الصلاحية للأجهزة التي تعمل بنظام أندرويد 12 (API 31) أو أحدث
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                // إذا لم تتوفر صلاحية المنبه الدقيق، نستخدم المنبه العادي بديل آمن لتجنب الانهيار (Crash)
                alarmManager.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent);
            } else {
                // ضبط منبه دقيق يعمل حتى في وضع السكون
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent);
            }
        }
        // الأندرويد ينشئ ملف داخلي صغير داخل التطبيق. باسم " انسيبرايشن"
        getSharedPreferences("InspirationPrefs", MODE_PRIVATE)
                .edit()
                .putBoolean("isRunning", true)
                .putBoolean("isMotivational", rbMotivational.isChecked())
                .apply();

        isRunning = true; // تغيير الحالة إلى "يعمل"
        btnStart.setText("Stop Notifications"); // تغيير نص الزر
        tvStatus.setText("Status: Active (Next in 30min)");
        Toast.makeText(this, "Inspiration scheduled for 30min from now!", Toast.LENGTH_LONG).show(); // إظهار رسالة تأكيد
    }

    /**
     * تقوم هذه الدالة بإيقاف عملية الإلهام وإلغاء أي إشعارات مجدولة.
     */
    private void stopInspiration() { // دالة إيقاف الإلهام

        // إنشاء نية مطابقة للنية المجدولة
        // النية هي كائن يحتوي على معلومات حول العملية التي تريد تنفيذها
        // في هذه الحالة، يتم استخدامها لإيقاف عملية الإلهام وإلغاء أي إشعارات مجدولة
        Intent intent = new Intent(this, NotificationReceiver.class);

        // الحصول على نفس النية المعلقة
        // النيات المعلقة هي نوع من النيات التي يتم تخزينها للمستخدم حتى يتم تنفيذها في وقت لاحق
        // في هذه الحالة، نقوم بإنشاء نية مستقلة تحتوي على معلومات حول النية التي تم تخزينها سابقاً
        // ويتم تخزينها في النيات المعلقة من قبل النظام
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        // السطر أدناه: الحصول على نسخة من "مدير التنبيهات" (AlarmManager) من نظام أندرويد عبر استدعاء خدمة النظام المخصصة لذلك.
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // السطر أدناه: التحقق من أن كائن alarmManager ليس فارغاً (null) لضمان عدم حدوث انهيار للتطبيق في حال لم تتوفر الخدمة.
        if (alarmManager != null) {
            // السطر أدناه: استخدام دالة .cancel لإيقاف أي منبه مجدول مسبقاً يطابق "pendingIntent" (أي له نفس المعرف والوجهة).
            alarmManager.cancel(pendingIntent);
        }

        // السطر أدناه: الوصول لملف تخزين البيانات الصغير (SharedPreferences) المسمى "InspirationPrefs".
        getSharedPreferences("InspirationPrefs", MODE_PRIVATE)
                // السطر أدناه: الدخول في وضع التعديل (Edit) لإضافة أو تغيير البيانات داخل الملف.
                .edit()
                // السطر أدناه: تخزين قيمة منطقية (false) تحت مفتاح "isRunning" للإشارة إلى أن الخدمة توقفت فعلياً حتى بعد إغلاق التطبيق.
                .putBoolean("isRunning", false)
                // السطر أدناه: تطبيق وحفظ التغييرات في الخلفية (Asynchronously) لضمان عدم تعليق واجهة المستخدم.
                .apply();
        isRunning = false; // تغيير الحالة إلى "متوقف"
        btnStart.setText("Start Staying Inspired"); // إعادة نص الزر للأصلي
        tvStatus.setText("Status: Inactive"); // تحديث نص الحالة
    }
    /**
     * وصف قصير: تختار هذه الدالة رسالة واحدة بشكل عشوائي من مصفوفات النصوص المتاحة.
     * الهدف منها: جلب نص عشوائي بناءً على النمط الذي اختاره المستخدم (تحفيزي أو تأنيب ضمير) لعرضه في الإشعار.
     * @return ترجع نص الرسالة (String) التي تم اختيارها عشوائياً.
     */
    private String getRandomMessage() { // دالة جلب رسالة عشوائية
        Random random = new Random(); // إنشاء كائن العشوائية
        if (rbMotivational.isChecked()) { // إذا اختار المستخدم النمط التحفيزي
            return motivationalMessages[random.nextInt(motivationalMessages.length)]; // اختيار رسالة عشوائية من مصفوفة التحفيز
        } else { // إذا اختار نمط تأنيب الضمير
            return guiltMessages[random.nextInt(guiltMessages.length)]; // اختيار رسالة عشوائية من مصفوفة التأنيب
        }
    }


}
