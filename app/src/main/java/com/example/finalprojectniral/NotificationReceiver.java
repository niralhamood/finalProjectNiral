package com.example.finalprojectniral;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import android.app.NotificationManager;
import java.util.Random;
/**
 * ملخص الكلاس: NotificationReceiver يعمل كمستقبل (Receiver) ينتظر إشارة من النظام
 * (المنبه) ليقوم ببناء الإشعار وعرضه للمستخدم في الوقت المحدد.
 */
public class NotificationReceiver extends BroadcastReceiver { // تعريف الكلاس كمستقبل بث

    /**
     * وصف قصير: يتم استدعاؤها تلقائياً بواسطة النظام عند انطلاق المنبه المجدول.
     * الهدف منها: استخراج الرسالة التحفيزية وبناء الإشعار وعرضه للمستخدم.
     * البارامترات (@param context): سياق التطبيق الذي يعمل فيه المستقبل.
     * البارامترات (@param intent): النية التي تحتوي على الرسالة المرسلة من شاشة StayInspired.
     */
    @Override
    public void onReceive(Context context, Intent intent) { // نقطة انطلاق استقبال الحدث
        // استلام الرسالة التحفيزية التي تم تمريرها من شاشة StayInspired عبر الـ Intent
        String message = intent.getStringExtra("message"); // استخراج نص الرسالة

        // بناء محتوى وشكل الإشعار (العنوان، الأيقونة، النص)
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id") // استخدام معرف القناة
                .setSmallIcon(android.R.drawable.ic_dialog_info) // ضبط الأيقونة التي تظهر في شريط الحالة
                .setContentTitle("Stay Inspired 💡") // ضبط عنوان الإشعار
                .setContentText(message) // ضبط نص الرسالة المستلمة
                .setPriority(NotificationCompat.PRIORITY_HIGH) // جعل الإشعار يظهر بوضوح (أولوية عالية)
                .setAutoCancel(true); // مسح الإشعار تلقائياً عند قيام المستخدم بالضغط عليه

        // الوصول لخدمة إدارة الإشعارات في نظام الأندرويد
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); // جلب مدير النظام

        // إرسال الإشعار للنظام لعرضه؛ نستخدم رقم عشوائي كـ ID للسماح بتراكم أكثر من إشعار
        manager.notify(new Random().nextInt(), builder.build()); // تنفيذ عملية إظهار الإشعار
    }
}
