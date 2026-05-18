package com.example.finalprojectniral.data.myUserTable;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * واجهة MyUserQuery تحتوي على جميع الاستعلامات (Queries) الخاصة بجدول المستخدمين.
 * تستخدم مكتبة Room لتحويل هذه الدوال إلى أوامر SQL وتنفذها على قاعدة البيانات.
 */
@Dao
public interface MyUserQuery {
    /**
     * تجلب جميع المستخدمين المسجلين في قاعدة البيانات.
     * @return قائمة (List) بجميع كائنات MyUser.
     */
    @Query("SELECT * FROM MyUser")
    List<MyUser> getAll();

    /**
     * تجلب مجموعة مستخدمين بناءً على مصفوفة من الأرقام التعريفية (IDs).
     * @param userIds مصفوفة أرقام تعريفية.
     * @return قائمة بالمستخدمين المطابقين.
     */
    @Query("SELECT * FROM MyUser WHERE keyid IN (:userIds)")
    List<MyUser> loadAllByIds(int[] userIds);

    /**
     * تتحقق من صحة بيانات تسجيل الدخول (البريد وكلمة المرور).
     * @param myEmail البريد الإلكتروني.
     * @param myPassw كلمة المرور.
     * @return كائن المستخدم إذا كانت البيانات صحيحة، أو null إذا لم يتطابق شيء.
     */
    @Query("SELECT * FROM MyUser WHERE email = :myEmail AND passw = :myPassw LIMIT 1")
    MyUser checkEmailPassw(String myEmail, String myPassw);

    /**
     * تتحقق من وجود بريد إلكتروني معين (يستخدم عادةً عند التسجيل الجديد).
     * @param myEmail البريد المراد فحصه.
     * @return كائن المستخدم إذا وجد.
     */
    @Query("SELECT * FROM MyUser WHERE email = :myEmail LIMIT 1")
    MyUser checkEmail(String myEmail);

    /**
     * إدراج مجموعة من المستخدمين في قاعدة البيانات.
     * @param users كائنات المستخدمين المراد إضافتهم.
     */
    @Insert
    void insertAll(MyUser... users);

    /**
     * حذف مستخدم معين من قاعدة البيانات.
     * @param user كائن المستخدم المراد حذفه.
     */
    @Delete
    void delete(MyUser user);

    /**
     * حذف مستخدم بناءً على رقمه التعريفي.
     * @param id الرقم التعريفي (KeyId).
     */
    @Query("Delete From MyUser WHERE keyid=:id ")
    void delete(int id);

    /**
     * إدراج مستخدم واحد جديد.
     * @param myUser كائن المستخدم.
     */
    @Insert
    void insert(MyUser myUser);

    /**
     * تحديث بيانات مستخدم أو أكثر (مثل تغيير الاسم أو كلمة المرور).
     * @param values المستخدمون المراد تحديث بياناتهم.
     */
    @Update
    void update(MyUser...values);
}




