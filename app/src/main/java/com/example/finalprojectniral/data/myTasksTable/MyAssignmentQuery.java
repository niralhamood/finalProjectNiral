package com.example.finalprojectniral.data.myTasksTable;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyAssignmentQuery {

    /**
     * تجلب جميع المهام من قاعدة البيانات مرتبة تنازلياً حسب الأهمية.
     * 
     * @return قائمة بجميع المهام.
     */
    @Query("SELECT * FROM MyAssignment ORDER BY importance DESC")
    List<MyAssignment> getAllTasks();

    /**
     * تجلب جميع المهام الخاصة بمستخدم معين مرتبة تنازلياً حسب الوقت.
     * 
     * @param userId الرقم التعريفي للمستخدم.
     * @return قائمة بمهام المستخدم المحدد.
     */
    @Query("SELECT * FROM MyAssignment WHERE userId = :userId ORDER BY time DESC")
    List<MyAssignment> getAllTasksByUser(long userId);

    /**
     * تجلب المهام الخاصة بمستخدم معين بناءً على حالة الإنجاز، مرتبة حسب الأهمية.
     * 
     * @param userId الرقم التعريفي للمستخدم.
     * @param isCompleted حالة إنجاز المهمة (تمت أو لم تتم).
     * @return قائمة بالمهام المفلترة.
     */
    @Query("SELECT * FROM MyAssignment WHERE userId = :userId AND isCompleted = :isCompleted ORDER BY importance DESC")
    List<MyAssignment> getTasksByUserAndStatus(long userId, boolean isCompleted);

    /**
     * تقوم بإدراج مهمة أو أكثر في قاعدة البيانات.
     * 
     * @param tasks المهام المراد إدراجها.
     */
    @Insert
    void insertTask(MyAssignment... tasks);

    /**
     * تقوم بتحديث بيانات مهمة أو أكثر موجودة مسبقاً في قاعدة البيانات.
     * 
     * @param tasks المهام المراد تحديثها.
     */
    @Update
    void updateTask(MyAssignment... tasks);

    /**
     * تقوم بحذف مهمة أو أكثر من قاعدة البيانات.
     * 
     * @param tasks المهام المراد حذفها.
     */
    @Delete
    void deleteTask(MyAssignment... tasks);

    /**
     * تقوم بحذف مهمة معينة بناءً على رقمها التعريفي (المفتاح الأساسي).
     * 
     * @param keyId الرقم التعريفي للمهمة.
     */
    @Query("DELETE FROM MyAssignment WHERE keyId = :keyId")
    void deleteTaskByKey(long keyId);
}
