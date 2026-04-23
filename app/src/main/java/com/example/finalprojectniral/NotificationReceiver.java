package com.example.finalprojectniral; // اسم الحزمة التي ينتمي إليها الكلاس

// استيراد المكتبات اللازمة للتعامل مع البث (Broadcast) والإشعارات
import android.content.BroadcastReceiver; // فئة لاستقبال رسائل البث من النظام
import android.content.Context; // سياق التطبيق للوصول للخدمات والموارد
import android.content.Intent; // كائن لنقل البيانات بين المكونات
import androidx.core.app.NotificationCompat; // أداة لبناء الإشعارات بشكل متوافق مع الإصدارات
import android.app.NotificationManager; // مدير نظام الإشعارات لإظهارها للمستخدم
import java.util.Random; // أداة لتوليد أرقام عشوائية لمعرف الإشعار

/**
 * كلاس NotificationReceiver: هو عبارة عن "مستقبل بث".
 * وظيفته الانتظار حتى يحين الوقت الذي حدده الـ AlarmManager، ثم يقوم بإنشاء وإظهار الإشعار.
 */
public class NotificationReceiver extends BroadcastReceiver { // تعريف الكلاس كمستقبل بث

    /**
     * دالة onReceive: يتم استدعاؤها تلقائياً عندما يحين وقت المنبه المجدول.
     */
    @Override
    public void onReceive(Context context, Intent intent) { // بداية تنفيذ كود الاستقبال

        // جلب نص الرسالة التحفيزية التي تم إرسالها من شاشة StayInspired
        String message = intent.getStringExtra("message"); // استخراج النص المخزن في الـ Intent

        // بناء شكل ومحتوى الإشعار
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id") // البدء ببناء الإشعار على القناة المحددة
                .setSmallIcon(android.R.drawable.ic_dialog_info) // تحديد الأيقونة الصغيرة التي تظهر في شريط الحالة
                .setContentTitle("Stay Inspired 💡") // تحديد عنوان الإشعار الثابت
                .setContentText(message) // وضع الرسالة العشوائية التي استلمناها كـ نص للإشعار
                .setPriority(NotificationCompat.PRIORITY_HIGH) // ضبط الأهمية كـ "عالية" ليظهر الإشعار بوضوح فوق الشاشة
                .setAutoCancel(true); // جعل الإشعار يختفي تلقائياً بمجرد أن يضغط عليه المستخدم

        // الوصول إلى خدمة مدير الإشعارات في نظام الأندرويد
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); // جلب مدير الإشعارات

        // إرسال الإشعار للنظام ليقوم بعرضه للمستخدم
        // نستخدم Random().nextInt() لإعطاء كل إشعار رقم فريد، مما يسمح بظهور أكثر من إشعار في نفس الوقت
        manager.notify(new Random().nextInt(), builder.build()); // عرض الإشعار فعلياً على شاشة الهاتف
    }
}



