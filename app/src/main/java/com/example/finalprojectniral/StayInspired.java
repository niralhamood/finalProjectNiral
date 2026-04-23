package com.example.finalprojectniral; 
import android.Manifest;    
import android.annotation.SuppressLint; 
import android.app.AlarmManager; // استيراد مدير التنبيهات لجدولة المهام
import android.app.NotificationChannel; // استيراد فئة إنشاء قنوات الإشعارات
import android.app.NotificationManager; // استيراد مدير نظام الإشعارات
import android.app.PendingIntent; // استيراد كائن لتنفيذ إجراء مستقبلي (مثل فتح التطبيق من إشعار)
import android.content.Context; // استيراد سياق التطبيق للوصول للموارد
import android.content.Intent; // استيراد الـ Intent للانتقال بين المكونات
import android.content.pm.PackageManager; // استيراد أداة لفحص صلاحيات النظام
import android.os.Build; // استيراد معلومات عن إصدار نظام الأندرويد
import android.os.Bundle; // استيراد كائن لنقل البيانات بين الحالات
import android.widget.Button; // استيراد عنصر الزر (Button)
import android.widget.RadioButton; // استيراد زر الاختيار (RadioButton)
import android.widget.RadioGroup; // استيراد مجموعة أزرار الاختيار
import android.widget.TextView; // استيراد عنصر عرض النصوص
import android.widget.Toast; // استيراد أداة عرض رسائل قصيرة للمستخدم

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

        createNotificationChannel(); // استدعاء دالة إنشاء قناة الإشعارات

        // إعداد ما سيحدث عند الضغط على زر البدء
        btnStart.setOnClickListener(v -> { // مراقب النقرات على الزر
            if (modeRadioGroup.getCheckedRadioButtonId() == -1) { // التحقق إذا لم يتم اختيار أي نمط
                Toast.makeText(this, "Please choose a mode first", Toast.LENGTH_SHORT).show(); // عرض تنبيه للمستخدم
                return; // التوقف عن التنفيذ
            }

            // طلب إذن الإشعارات إذا كان الإصدار أندرويد 13 (Tiramisu) أو أحدث
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // فحص إصدار النظام
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) { // فحص الإذن
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101); // طلب الإذن من المستخدم
                    return; // التوقف لحين الموافقة
                }
            }

            if (!isRunning) { // إذا كانت الخدمة متوقفة
                startInspiration(); // استدعاء دالة التشغيل
            } else { // إذا كانت الخدمة تعمل
                stopInspiration(); // استدعاء دالة الإيقاف
            }
        });
    }

    /**
     * دالة startInspiration: مسؤولة عن جدولة الإشعارات باستخدام AlarmManager.
     * تضمن وصول الإشعار حتى لو تم إغلاق التطبيق.
     */
    private void startInspiration() { // دالة تشغيل الإلهام
        String message = getRandomMessage(); // الحصول على رسالة عشوائية بناءً على النمط

        // تجهيز الـ Intent الذي سيقوم بتشغيل المستقبل (Receiver) لإظهار الإشعار
        Intent intent = new Intent(this, NotificationReceiver.class); // إنشاء نية للمستقبل
        intent.putExtra("message", message); // وضع الرسالة المختارة داخل النية

        // PendingIntent هو كائن يستخدم للسماح للنظام بتنفيذ كود بعد فتح التطبيق من إشعار أو فتح مسار معين.
        // تتم إنشاء PendingIntent باستخدام الأسلوب PendingIntent.getBroadcast() أو PendingIntent.getActivity() أو PendingIntent.getService().
        // يتم استدعاء هذا الأسلوب مع السياق (Context)، رقم الطلب (requestCode)، النية الأصلية (originalIntent)، والأعلام (flags).
        // في هذا الأمر، يتم إنشاء PendingIntent مع السياق هو النشاط الحالي (this)، رقم الطلب هو 0، النية الأصلية هي intent، والأعلام هي PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE.
        // يتم تعيين الأعلام PendingIntent.FLAG_UPDATE_CURRENT للتأكد من تحديث النية الموجودة في الذاكرة إذا كانت موجودة بالفعل.
        // ويتم تعيين الأعلام PendingIntent.FLAG_IMMUTABLE للتأكد من أن النية لن تتغير حتى يتم تحديثها في الإصدارات الحديثة.
        // إنشاء PendingIntent  للسماح للنظام بتنفيذ الكود لاحقاً
        PendingIntent pendingIntent = PendingIntent.getBroadcast( // إنشاء نية معلقة للبث
                this, // السياق الحالي
                0, // كود الطلب
                intent, // النية الأصلية
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE // الأعلام المطلوبة للإصدارات الحديثة
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE); // الوصول لخدمة المنبه في النظام

        // تحديد وقت ظهور الإشعار (الوقت الحالي + 60 ثانية)
        long triggerTime = System.currentTimeMillis() + 60000; // حساب وقت الانطلاق

        if (alarmManager != null) { // التأكد من توفر مدير المنبهات
            // ضبط منبه دقيق يعمل حتى في وضع السكون
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent); // جدولة المنبه
        }

        isRunning = true; // تغيير الحالة إلى "يعمل"
        btnStart.setText("Stop Notifications"); // تغيير نص الزر
        tvStatus.setText("Status: Active (Next in 60s)"); // تحديث نص الحالة
        Toast.makeText(this, "Inspiration scheduled for 60 seconds from now!", Toast.LENGTH_LONG).show(); // إظهار رسالة تأكيد
    }

    /**
     * دالة stopInspiration: تقوم بإلغاء أي منبهات مجدولة مسبقاً لإيقاف الإشعارات.
     */
    private void stopInspiration() { // دالة إيقاف الإلهام
        Intent intent = new Intent(this, NotificationReceiver.class); // إنشاء نية مطابقة للنية المجدولة
        PendingIntent pendingIntent = PendingIntent.getBroadcast( // الحصول على نفس النية المعلقة
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE); // الوصول لخدمة المنبه
        if (alarmManager != null) { // التأكد من توفر الخدمة
            alarmManager.cancel(pendingIntent); // إلغاء المنبه المجدول المرتبط بهذه النية
        }

        isRunning = false; // تغيير الحالة إلى "متوقف"
        btnStart.setText("Start Staying Inspired"); // إعادة نص الزر للأصلي
        tvStatus.setText("Status: Inactive"); // تحديث نص الحالة
    }

    /**
     * دالة getRandomMessage: تختار رسالة واحدة عشوائية من المصفوفة المختارة (تحفيزي أو تأنيب).
     */
    private String getRandomMessage() { // دالة جلب رسالة عشوائية
        Random random = new Random(); // إنشاء كائن العشوائية
        if (rbMotivational.isChecked()) { // إذا اختار المستخدم النمط التحفيزي
            return motivationalMessages[random.nextInt(motivationalMessages.length)]; // اختيار رسالة عشوائية من مصفوفة التحفيز
        } else { // إذا اختار نمط تأنيب الضمير
            return guiltMessages[random.nextInt(guiltMessages.length)]; // اختيار رسالة عشوائية من مصفوفة التأنيب
        }
    }

    /**
     * دالة createNotificationChannel: تنشئ قناة إشعارات (مطلوب لأندرويد 8 فما فوق).
     * بدون هذه القناة، لن تظهر الإشعارات على الإصدارات الحديثة.
     */
    private void createNotificationChannel() { // دالة إنشاء قناة الإشعار


/* <<<<<<<<<<<<<<  ✨ Windsurf Command 🌟 >>>>>>>>>>>>>>>> */
        // شرح عن SDK_INT

        // تحقق من أن الإصدار الذي يعمل على الجهاز هو أوريو أو أحدث.
        // SDK_INT هو متغير يحتوي على رقم يمثل إصدار النظام الأوبينتي.
        // يتم تعيين هذا الرقم بشكل ثابت في كل إصدار من النظام.
        // يمكن استخدامه للتحقق من إصدار النظام بداخل الكود.



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // التحقق من أن الإصدار أوريو أو أحدث
/* <<<<<<<<<<  7f7184d0-860e-46a2-a0ab-f655b675cac0  >>>>>>>>>>> */
            NotificationChannel channel = new NotificationChannel( // إنشاء كائن القناة
                    "channel_id", // معرف القناة (يجب أن يتطابق مع المستخدم في الـ Receiver)
                    "Stay Inspired Channel", // اسم القناة الذي يظهر في الإعدادات
                    NotificationManager.IMPORTANCE_HIGH // تحديد أهمية القناة (عالية لتظهر كـ Pop-up)
            );
            NotificationManager manager = getSystemService(NotificationManager.class); // الحصول على مدير الإشعارات
            if (manager != null) { // التأكد من توفر المدير
                manager.createNotificationChannel(channel); // تسجيل القناة في النظام
            }
        }
    }
}
