package com.example.finalprojectniral; // تعريف حزمة المشروع الحالي

import android.content.Intent; // استيراد كلاس Intent (غير مستخدم حالياً في هذا الملف)

public class task_item_layout { // تعريف الكلاس الذي يمثل هيكل بيانات عنصر المهمة

    private String title; // متغير خاص لتخزين عنوان المهمة
    private String priority; // متغير خاص لتخزين مستوى أولوية المهمة

    public task_item_layout(String title, String priority) { // منشئ الكلاس (Constructor) لتعيين القيم عند إنشاء كائن جديد
        this.title = title; // إسناد قيمة العنوان الممررة إلى المتغير الخاص بالكلاس
        this.priority = priority; // إسناد قيمة الأولوية الممررة إلى المتغير الخاص بالكلاس
    }

    public String getTitle() { // دالة (Getter) لاسترجاع عنوان المهمة
        return title; // إرجاع قيمة العنوان
    }

    public String getPriority() { // دالة (Getter) لاسترجاع أولوية المهمة
        return priority; // إرجاع قيمة الأولوية
    }
}

