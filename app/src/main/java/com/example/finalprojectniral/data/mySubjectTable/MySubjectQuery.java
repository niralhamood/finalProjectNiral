package com.example.finalprojectniral.data.mySubjectTable;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

public class MySubjectQuery {
    /** اعادة جميع معيات جدول المواضيع *
     * قائمة من المواضيع *return@
     */

        @Query("SELECT * FROM MySubject")
        List<MySubject> getAllSubjects);

    /**
     * تعديل المهمات
     * مجموعة المهمات *param s@
     */
    @Insert
    void insert(MySubject... s)//ثلاثة نقاط تعني مجموعه

    /**
     * تعديل المهمات
     *
     * @param s
     * @Update void update(MySubject... s);
     * /**
     * حذف مهمه او مهمات
     * حذف المهمات ( حسب المفتاح الرئيسي) * param s@*
     */


    @Delete
    void deleteTask(MySubjec...s);
    @Query("DELETE FROM MySubject WHERE key_id=:keyid")
    void delete(long keyid);
    @Query("SELECT * From MySubject WHERE title=:sub")
    MySubject checkSubject(String sup);
    }

}
