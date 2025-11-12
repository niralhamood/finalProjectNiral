package com.example.finalprojectniral.data.myUserTable;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

public interface MyUserQuery {
    @Dao//لتحديد ان الواجهة تحتوي استعلامات على قاعدة بيانات
    public interface MyUserQuery
    {   //استخراج جميع المستعملين

        List<MyUser> getAll();
        // استخراج مستعمل حسب رقم المميز لهid

        List<MyUser> loadAllByIds(int[] userIds);
        //هل المستعمل موجود حسب الايميل وكلمة السر

        MyUser checkEmailPassw(String myEmail, String myPassw);
        //فحص هل الايميل موجود من قبل

        MyUser checkEmail(String myEmail);
        // اضافة مستعمل او مجموعة مستعملين

        void insertAll(MyUser... users);
        // حذف
        void delete(MyUser user);
        //حذف حسب الرقم المميز id

        void delete(int id);
        //اضافة مستعمل واحد

        void insert(MyUser myUser);
        //تعديل مستعمل او قائمة مستعملين

        void update(MyUser...values);

    }
    @Dao//لتحديد ان الواجهخة تحوي استعلامات على قاعدة بايانات
    public interface MyUserQuery
      //استخراج جميع المستعملين
        @Query("SELECT * FROM MyUser")
        List<MyUser> getAll();
        // استخراج مستعمل حسب رقم المميز لهid
        @Query("SELECT * FROM MyUser WHERE keyid IN (:userIds)")
        List<MyUser> loadAllByIds(int[] userIds);
        //هل المستعمل موجود حسب الايميل وكلمة السر
        @Query("SELECT * FROM MyUser WHERE email = :myEmail AND passw = :myPassw LIMIT 1")
        MyUser checkEmailPassw(String myEmail, String myPassw);
        //فحص هل الايميل موجود من قبل
        @Query("SELECT * FROM MyUser WHERE email = :myEmail LIMIT 1")
        MyUser checkEmail(String myEmail);
        @Insert
// اضافة مستعمل او مجموعة مستعملين
        void insertAll(MyUser... users);
        @Delete
// حذف
        void delete(MyUser user);
        //حذف حسب الرقم المميز id
        @Query("Delete From MyUser WHERE keyid=:id ")
        void delete(int id);
        @Insert//اضافة مستعمل واحد
        void insert(MyUser myUser);
        @Update
//تعديل مستعمل او قائمة مستعملين
        void update(MyUser...values);
    }

