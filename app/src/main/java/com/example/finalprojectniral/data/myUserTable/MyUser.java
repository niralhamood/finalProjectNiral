package com.example.finalprojectniral.data.myUserTable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

/**
 * كلاس MyUser: يمثل بيانات المستخدم في قاعدة البيانات المحلية (Room) وفي Firebase.
 * implements Serializable: تسمح بنقل كائن المستخدم بين الشاشات (Activities).
 */
@Entity(tableName = "my_user_table") // تعريف اسم الجدول في قاعدة بيانات Room
public class MyUser implements Serializable { // تسمح بتحويل الكائن إلى سلسلة بيانات (Byte Stream) لنقله بين الشاشات عبر الـ Intent

    // المعرف التلقائي لقاعدة البيانات المحلية (Room)
    @PrimaryKey(autoGenerate = true)
    public long keyid;

    // المعرف الفريد الخاص بـ Firebase (يتم توليده عند الحفظ في السحابة)
    @ColumnInfo(name = "user_id")
    public String userId;

    // حقل الاسم الكامل للمستخدم
    @ColumnInfo(name = "full_name")
    public String fullName;

    // حقل البريد الإلكتروني
    @ColumnInfo(name = "email")
    public String email;

    // حقل رقم الهاتف
    @ColumnInfo(name = "phone")
    public String phone;

    // حقل كلمة المرور
    @ColumnInfo(name = "password")
    public String passw;

    /**
     * المشيد الافتراضي (Empty Constructor):
     * ضروري جداً لعمل مكتبة Firebase لكي تتمكن من قراءة البيانات وتحويلها لكائن.
     */
    public MyUser() {
    }

    /**
     * مشيد (Constructor) لإنشاء مستخدم جديد بسهولة مع تمرير البيانات الأساسية
     */
    public MyUser(String fullName, String email, String phone, String passw) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.passw = passw;
    }

    // --- دالات الـ Getters والـ Setters للوصول للبيانات وتعديلها ---

    public long getKeyid() {
        return keyid;
    }

    public void setKeyid(long keyid) {
        this.keyid = keyid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassw() {
        return passw;
    }

    public void setPassw(String passw) {
        this.passw = passw;
    }

    /**
     * دالة toString: مفيدة للمبرمج لعرض بيانات الكائن بشكل نصي عند فحص الأخطاء.
     */
    @Override
    public String toString() {
        return "MyUser{" +
                "userId='" + userId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
