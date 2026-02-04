package com.example.finalprojectniral.data.myTasksTable;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyAssignmentQuery {

    // 1) جلب كل المهام مرتبة حسب الأهمية
    @Query("SELECT * FROM MyAssignment ORDER BY importance DESC")
    List<MyAssignment> getAllTasks();

    // 2) جلب المهام حسب userId مرتبة حسب الوقت
    @Query("SELECT * FROM MyAssignment WHERE userId = :userId ORDER BY time DESC")
    List<MyAssignment> getAllTasksByUser(long userId);

    // 3) جلب المهام حسب userId + حالة الإنجاز + ترتيب حسب الأهمية
    @Query("SELECT * FROM MyAssignment WHERE userId = :userId AND isCompleted = :isCompleted ORDER BY importance DESC")
    List<MyAssignment> getTasksByUserAndStatus(long userId, boolean isCompleted);

    // 4) إدخال مهمة أو أكثر
    @Insert
    void insertTask(MyAssignment... tasks);

    // 5) تحديث مهمة أو أكثر
    @Update
    void updateTask(MyAssignment... tasks);

    // 6) حذف مهمة أو أكثر
    @Delete
    void deleteTask(MyAssignment... tasks);

    // 7) حذف مهمة حسب المفتاح الأساسي
    @Query("DELETE FROM MyAssignment WHERE keyId = :keyId")
    void deleteTaskByKey(long keyId);
}
