package com.example.finalprojectniral.data.myTasksTable; // تعريف الحزمة الخاصة بالكلاس

import androidx.room.Entity; // استيراد مكتبة Room لتعريف الجداول
import androidx.room.PrimaryKey; // استيراد مكتبة Room لتعريف المفتاح الأساسي

import java.io.Serializable; // استيراد واجهة Serializable للسماح بنقل الكائن بين الشاشات

@Entity // وسم الكلاس كجدول في قاعدة بيانات Room

public class MyAssignment { // تعريف كلاس المهمة
    @PrimaryKey(autoGenerate = true) // تعريف المفتاح الأساسي مع خاصية الترقيم التلقائي
    public long keyId; // متغير لتخزين معرف المهمة في قاعدة البيانات المحلية
    public  long userId; // متغير لتخزين معرف المستخدم صاحب المهمة
    public int importance; // متغير لتخزين درجة أهمية المهمة (1-3)
    public String shortTitle; // متغير لتخزين العنوان القصير للمهمة
    public String text; // متغير لتخزين وصف المهمة أو نصها الكامل
    public long time; // متغير لتخزين وقت المهمة بصيغة Timestamp
    public boolean isCompleted; // متغير يحدد ما إذا كانت المهمة قد اكتملت أم لا
    public String file; // متغير لتخزين مسار الملف أو الصورة المرفقة بالمهمة
    private String key; // متغير لتخزين المفتاح الفريد الخاص بالمهمة في Firebase


    public MyAssignment(String title, String priority) { // منشئ (Constructor) للمهمة يقبل العنوان والأولوية
        this.shortTitle = title; // تعيين العنوان
        this.text = title; // تعيين النص (افتراضياً نفس العنوان هنا)
        this.importance = convertPriorityToInt(priority); // تحويل نص الأولوية إلى رقم
        this.time = System.currentTimeMillis(); // تعيين الوقت الحالي للنظام
        this.isCompleted = false; // تعيين الحالة كغير مكتملة افتراضياً
    }

    public MyAssignment() { // منشئ فارغ مطلوب من قبل Firebase و Room

    }

    /* <<<<<<<<<<<<<<  ✨ Windsurf Command 🌟 >>>>>>>>>>>>>>>> */
    /**
     * دالة لتحويل نص الأولوية (High, Medium, Low) إلى قيمة عددية
     *
     * @param priority نص الأولوية للمهمة
     * @return قيمة عددية تمثل مستوى الأولوية
     */
    private int convertPriorityToInt(String priority) {
/* <<<<<<<<<<  6db93eed-b0e9-48d7-8bc1-3208c5154f79  >>>>>>>>>>> */
        switch (priority.toLowerCase()) { // فحص النص بعد تحويله لحروف صغيرة
            case "high":
                return 3; // عالية تأخذ القيمة 3
            case "medium":
                return 2; // متوسطة تأخذ القيمة 2
            case "low":
                return 1; // منخفضة تأخذ القيمة 1
            default:
                return 2; // القيمة الافتراضية هي 2 (متوسطة)
        }
    }

    // دوال الحصول والتعيين (Getters and Setters) لكل المتغيرات

    public long getKeyId() { // دالة للحصول على معرف قاعدة البيانات المحلي
        return keyId;
    }

    public void setKeyId(long keyId) { // دالة لتعيين معرف قاعدة البيانات المحلي
        this.keyId = keyId;
    }

    public int getImportance() { // دالة للحصول على درجة الأهمية
        return importance;
    }

    public void setImportance(int importance) { // دالة لتعيين درجة الأهمية
        this.importance = importance;
    }

    public String getShortTitle() { // دالة للحصول على العنوان القصير
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) { // دالة لتعيين العنوان القصير
        this.shortTitle = shortTitle;
    }

    public String getText() { // دالة للحصول على نص المهمة
        return text;
    }

    public void setText(String text) { // دالة لتعيين نص المهمة
        this.text = text;
    }

    public long getTime() { // دالة للحصول على وقت المهمة
        return time;
    }

    public void setTime(long time) { // دالة لتعيين وقت المهمة
        this.time = time;
    }

    public boolean isCompleted() { // دالة لفحص ما إذا كانت المهمة مكتملة
        return isCompleted;
    }

    public void setCompleted(boolean completed) { // دالة لتعيين حالة اكتمال المهمة
        isCompleted = completed;
    }
    public String getFile() { // دالة للحصول على مسار الملف أو الصورة
        return file;
    }

    public void setFile(String file) { // دالة لتعيين مسار الملف أو الصورة
        this.file = file;
    }

    public void setKey(String key) { // دالة لتعيين مفتاح Firebase الفريد
        this.key = key;
    }

    public String getKey() { // دالة للحصول على مفتاح Firebase الفريد
        return key;
    }
}
