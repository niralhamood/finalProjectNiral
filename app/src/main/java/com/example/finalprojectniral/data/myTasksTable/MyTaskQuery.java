package com.example.finalprojectniral.data.myTasksTable;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyTaskQuery {

    // 1) كل التسك حسب الأهمية
    @Query("SELECT * FROM TasksActivity ORDER BY importance DESC")
    List<TasksActivity> getAllTasks();

    // 2) كل التسك حسب userId ومرتبة حسب الوقت
    @Query("SELECT * FROM TasksActivity WHERE userId = :userid_p ORDER BY time DESC")
    List<TasksActivity> getAllTaskOrderBy(long userid_p);

    // 3) حسب userId + هل مكتملة + ترتيب بالأهمية
    @Query("SELECT * FROM TasksActivity WHERE userId = :userid_p AND isCompleted = :isCompleted_p ORDER BY importance DESC")
    List<TasksActivity> getAllTaskOrderBy(long userid_p, boolean isCompleted_p);

    // 4) إدخال مهمة
    @Insert
    void insertTask(TasksActivity... tasks);

    // 5) تحديث مهمة
    @Update
    void updateTask(TasksActivity... tasks);

    // 6) حذف مهمة
    @Delete
    void deleteTask(TasksActivity... tasks);

    // 7) حذف حسب المفتاح
    @Query("DELETE FROM TasksActivity WHERE keyId = :kid")
    void deleteTask(long kid);

    // 8) جلب تسكات حسب subject id
    @Query("SELECT * FROM TasksActivity WHERE subjId = :key_id ORDER BY importance DESC")
    List<TasksActivity> getTaskBySubjId(long key_id);
}
