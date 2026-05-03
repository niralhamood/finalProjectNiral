package com.example.finalprojectniral.data.myUserTable;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

    @Dao//لتحديد ان الواجهخة تحوي استعلامات على قاعدة بايانات
   public interface MyUserQuery{
    /**
     * تجلب جميع المستخدمين من قاعدة البيانات.
     * 
     * @return قائمة بجميع المستخدمين.
     */
    @Query("SELECT * FROM MyUser")
    List<MyUser> getAll();
    /**
     * تجلب مستخدمين محددين بناءً على قائمة الأرقام التعريفية الخاصة بهم.
     * 
     * @param userIds مصفوفة الأرقام التعريفية للمستخدمين.
     * @return قائمة بالمستخدمين الذين تم العثور عليهم.
     */
    @Query("SELECT * FROM MyUser WHERE keyid IN (:userIds)")
    List<MyUser> loadAllByIds(int[] userIds);
    /**
     * تتحقق من وجود مستخدم بناءً على البريد الإلكتروني وكلمة المرور.
     * تستخدم للتحقق من بيانات تسجيل الدخول.
     * 
     * @param myEmail البريد الإلكتروني للمستخدم.
     * @param myPassw كلمة المرور.
     * @return كائن المستخدم إذا كانت البيانات صحيحة، وإلا تعيد null.
     */
    @Query("SELECT * FROM MyUser WHERE email = :myEmail AND passw = :myPassw LIMIT 1")
    MyUser checkEmailPassw(String myEmail, String myPassw);
    /**
     * تتحقق مما إذا كان البريد الإلكتروني موجوداً مسبقاً في قاعدة البيانات.
     * 
     * @param myEmail البريد الإلكتروني المراد فحصه.
     * @return كائن المستخدم إذا وجد، وإلا تعيد null.
     */
    @Query("SELECT * FROM MyUser WHERE email = :myEmail LIMIT 1")
    MyUser checkEmail(String myEmail);
    /**
     * تقوم بإدراج مجموعة من المستخدمين في قاعدة البيانات.
     * 
     * @param users قائمة المستخدمين المراد إدراجهم.
     */
    @Insert
    void insertAll(MyUser... users);
    /**
     * تقوم بحذف مستخدم معين من قاعدة البيانات.
     * 
     * @param user كائن المستخدم المراد حذفه.
     */
    @Delete
    void delete(MyUser user);
    /**
     * تقوم بحذف مستخدم بناءً على رقمه التعريفي.
     * 
     * @param id الرقم التعريفي للمستخدم المراد حذفه.
     */
    @Query("Delete From MyUser WHERE keyid=:id ")
    void delete(int id);
    /**
     * تقوم بإدراج مستخدم واحد في قاعدة البيانات.
     * 
     * @param myUser كائن المستخدم المراد إدراجه.
     */
    @Insert
    void insert(MyUser myUser);
    /**
     * تقوم بتحديث بيانات مستخدم أو أكثر في قاعدة البيانات.
     * 
     * @param values المستخدمون المراد تحديث بياناتهم.
     */
    @Update
    void update(MyUser...values);

    }
      //استخراج جميع المستعملين



