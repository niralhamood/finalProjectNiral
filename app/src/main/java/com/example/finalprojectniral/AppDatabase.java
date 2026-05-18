package com.example.finalprojectniral;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.finalprojectniral.data.myTasksTable.MyAssignment;
import com.example.finalprojectniral.data.myTasksTable.MyAssignmentQuery;
import com.example.finalprojectniral.data.myUserTable.MyUser;
import com.example.finalprojectniral.data.myUserTable.MyUserQuery;

/**
 * كلاس AppDatabase يمثل قاعدة بيانات التطبيق باستخدام مكتبة Room.
 * يعمل هذا الكلاس كنقطة الوصول الرئيسية للاتصال ببيانات التطبيق المخزنة محلياً.
 * يتضمن الجداول (Entities) والعمليات (DAOs) المتاحة.
 */
@Database(entities = {MyUser.class, MyAssignment.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    /**
     * دالة مجردة للوصول إلى عمليات جدول المستخدمين.
     * @return كائن من نوع MyUserQuery.
     */
    public abstract MyUserQuery myUserQuery();

    /**
     * دالة مجردة للوصول إلى عمليات جدول المهام.
     * @return كائن من نوع MyAssignmentQuery.
     */
    public abstract MyAssignmentQuery myAssignmentQuery();

    private static volatile AppDatabase INSTANCE;

    /**
     * دالة نمط Singleton للحصول على نسخة واحدة فقط من قاعدة البيانات في كامل التطبيق.
     * تضمن هذه الدالة عدم إنشاء أكثر من مثيل لقاعدة البيانات لتوفير موارد النظام.
     * 
     * @param context سياق التطبيق (Context).
     * @return نسخة AppDatabase النشطة.
     */
    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "my_database"
                    )
                    .allowMainThreadQueries() // يسمح بالعمليات على الخيط الرئيسي (يفضل تجنبها في التطبيقات الكبيرة)
                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
