package com.example.finalprojectniral.data.myUserTable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity



    public class MyUser {
    @PrimaryKey(autoGenerate = true)//تحديد الصفة كمفتاح رئيسي والذي يُنتجح بشكل تلقائي
    public long keyid;
    @ColumnInfo(name = "full_Name")//اعطاء اسم جديد للعامود-الصفة في الجدول
    public String fullName;
    public String email;//بحالة لم يتم اعطاء اسم للعامود يكون اسم الصفه هو اسم العامود
    public String phone;
    public String passw;

    // هذا هو طريقة لإعطاء نسخة من الكائن MyUser بصيغة نصية تعرض جميع خصائص الكائن
    // المعطية في الطريقة toString() يتم استخدامها عادة لعرض البيانات الخاصة بكائن Java
    // في نطاق محدد مثل محطات التحكم أو المحلاعات الخطأ أو المخرجات الخاصة بالبرنامج
    @Override
    public String toString() {
        return "MyUser{" +
                "keyid=" + keyid +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", passw='" + passw + '\'' +
                '}';
    }

    // دالة للحصول على قيمة الصفة fullName
    public String getFullName() {
        return fullName;
    }

    // دالة لتعيين قيمة الصفة fullName
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}


