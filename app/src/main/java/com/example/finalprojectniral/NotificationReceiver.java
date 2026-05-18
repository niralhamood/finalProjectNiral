package com.example.finalprojectniral;
import static androidx.core.content.ContextCompat.getSystemService;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import android.app.NotificationManager;
import android.os.Build;
import android.os.SystemClock;

import java.util.Random;
/**
 * ملخص الكلاس: NotificationReceiver يعمل كمستقبل (Receiver) ينتظر إشارة من النظام
 * (المنبه) ليقوم ببناء الإشعار وعرضه للمستخدم في الوقت المحدد.
 */
public class NotificationReceiver extends BroadcastReceiver { // تعريف الكلاس كمستقبل بث

    /**
     * دالة onReceive: يتم استدعاؤها عند انطلاق المنبه المجدول.
     * تقوم باستخراج الرسالة وبناء الإشعار وعرضه.
     */
    /**
     * * وصف قصير: يتم استدعاؤها تلقائياً بواسطة النظام عند انطلاق المنبه المجدول.
     * * الهدف منها: استخراج الرسالة التحفيزية وبناء الإشعار وعرضه للمستخدم.
     * * البارامترات (@param context): سياق التطبيق الذي يعمل فيه المستقبل.
     * * البارامترات (@param intent): النية التي تحتوي على الرسالة المرسلة من شاشة StayInspired.
     *
     */
    @Override
    public void onReceive(Context context, Intent intent) { // نقطة انطلاق استقبال الحدث
        // استلام الرسالة التحفيزية التي تم تمريرها من شاشة StayInspired عبر الـ Intent

        String message = intent.getStringExtra("message"); // استخراج نص الرسالة
        createNotificationChannel(context); // استدعاء دالة إنشاء قناة الإشعارات
        Intent resultIntent = new Intent(context, StayInspired.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        // بناء محتوى وشكل الإشعار (العنوان، الأيقونة، النص)
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id") // استخدام معرف القناة
                .setSmallIcon(android.R.drawable.ic_dialog_info) // ضبط الأيقونة التي تظهر في شريط الحالة
                .setContentTitle("Stay Inspired 💡") // ضبط عنوان الإشعار
                .setContentText(message) // ضبط نص الرسالة المستلمة
                .setPriority(NotificationCompat.PRIORITY_HIGH) // جعل الإشعار يظهر بوضوح (أولوية عالية)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true); // مسح الإشعار تلقائياً عند قيام المستخدم بالضغط عليه

        // الوصول لخدمة إدارة الإشعارات في نظام الأندرويد
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); // جلب مدير النظام

        // إرسال الإشعار للنظام لعرضه؛ نستخدم رقم عشوائي كـ ID للسماح بتراكم أكثر من إشعار
        manager.notify(new Random().nextInt(), builder.build()); // تنفيذ عملية إظهار الإشعار
        scheduleNextAlarm(context, message);
    }



    private void scheduleNextAlarm(Context context, String message) {

        // فحص هل الإشعارات ما زالت مفعلة
        boolean isRunning = context
                .getSharedPreferences("InspirationPrefs", Context.MODE_PRIVATE)
                .getBoolean("isRunning", false);

        // إذا المستخدم ضغط Stop لا يتم جدولة إشعار جديد
        if (!isRunning) {
            return;
        }

        // الوصول لخدمة AlarmManager المسؤولة عن جدولة المهام
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // إنشاء Intent جديد لإعادة تشغيل NotificationReceiver
        Intent nextIntent = new Intent(context, NotificationReceiver.class);

        // تمرير الرسالة مرة أخرى للإشعار القادم
        nextIntent.putExtra("message", message);

        // إنشاء PendingIntent ليتم تنفيذه لاحقاً بواسطة النظام
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, nextIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        // تحديد وقت الإشعار القادم بعد 60 ثانية
        long triggerTime = SystemClock.elapsedRealtime() + (30 * 60 * 1000);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                alarmManager.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent);
            } else {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent);
            }
        }


    }
    /**
     * وصف قصير: تقوم هذه الدالة بإنشاء قناة للإشعارات في نظام الأندرويد.
     * الهدف منها: تعريف القناة لنظام التشغيل (مطلوب من إصدار أندرويد 8.0 فما فوق) لضمان ظهور الإشعارات وتحديد مستواها.
     *
     * شرح إضافي: تحقق من أن الإصدار هو أوريو (Oreo) أو أحدث باستخدام SDK_INT، ثم تضبط اسم القناة وأهميتها وتعرفها في مدير الإشعارات.
     *
     * @return لا تُرجع شيئاً.
     */


    private void createNotificationChannel(Context context) { // دالة إنشاء قناة الإشعار
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // التحقق من أن الإصدار أوريو أو أحدث

            NotificationChannel channel = new NotificationChannel( // إنشاء كائن القناة
                    "channel_id", // معرف القناة (يجب أن يتطابق مع المستخدم في الـ Receiver)
                    "Stay Inspired Channel", // اسم القناة الذي يظهر في الإعدادات
                    NotificationManager.IMPORTANCE_HIGH // تحديد أهمية القناة (عالية لتظهر كـ Pop-up)
            );
            NotificationManager manager = context.getSystemService(NotificationManager.class); // الحصول على مدير الإشعارات
            if (manager != null) { // التأكد من توفر المدير
                manager.createNotificationChannel(channel); // تسجيل القناة في النظام
            }
        }
    }


}
