package com.example.finalprojectniral.data.myTasksTable;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MyTaskQuery {

 قائمة من المهمات * return@*
 @Query("SELECT * FROM MyTask ORDER BY Importance DESC")
 List<MyTask> getAll Tasks(;
 /**
 ارجاع المهمات حسب المستعمل واذا انتهت ام لا ومرتبة تنازليا حسب الاهمية *
 @param userid_p رقم المستعمل
  * @return
 */
    @Query"SELECT * FROM MyTask WHERE userld=:userid_p, ORDER BY time DES")
    List<MyTask> getAl|TaskOrederBy(long userid_p);
    /**
     ارجاع المهمات حسب المستعمل واذا انتهت ام لا ومرتبة تنازليا حسب الاهمية *
     @param userid_p رقم المستعمل
     *@param isCompleted_ * هل تمت ام لا
     * @return * قائمة مهمات
     */
    @Query(SELECT * FROM MyTask WHERE userld=:userid_p AND isCompleted=:isCompleted_p"+
            "ORDER BY importance DESC")
    List<MyTask> getAl|TaskOrederBy(long userid_p,boolean isCompleted_p);