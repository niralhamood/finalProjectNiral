package com.example.finalprojectniral.data.myUserTable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
/**
 * كلاس MyUser يمثل جدول المستخدمين في قاعدة بيانات Room.
 * يحتوي على البيانات الشخصية للمستخدم مثل الاسم، البريد الإلكتروني، الهاتف، وكلمة المرور.
 */
@Entity
public class MyUser {
    /**
     * المفتاح الرئيسي للجدول، يتم إنتاجه تلقائياً.
     */
    @PrimaryKey(autoGenerate = true)
    public long keyid;

    /**
     * اسم المستخدم الكامل.
     */
    @ColumnInfo(name = "full_Name")
    public String fullName;

    /**
     * البريد الإلكتروني للمستخدم.
     */
    public String email;

    /**
     * رقم هاتف المستخدم.
     */
    public String phone;

    /**
     * كلمة مرور المستخدم.
     */
    public String passw;

    /**
     * معرف المستخدم الفريد (يمكن استخدامه للربط مع Firebase).
     */
    public String userId;

    /**
     * تحويل بيانات الكائن إلى نص لعرضها (مفيد لعمليات التصحيح Debugging).
     * @return نص يحتوي على كافة خصائص المستخدم.
     */
    @Override
    public String toString() {
        return "MyUser{" +
                "keyid=" + keyid +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", passw='" + passw + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

    /**
     * الحصول على الاسم الكامل للمستخدم.
     * @return الاسم الكامل.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * تعيين الاسم الكامل للمستخدم.
     * @param fullName الاسم الجديد.
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * تعيين المعرف الفريد للمستخدم (UserId).
     * @param userId المعرف الجديد.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
}


