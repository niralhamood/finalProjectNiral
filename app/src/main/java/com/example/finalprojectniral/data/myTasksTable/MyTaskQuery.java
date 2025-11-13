package com.example.finalprojectniral.data.myTasksTable;

import static android.icu.text.MessagePattern.ArgType.SELECT;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import kotlinx.coroutines.scheduling.Task;

@Dao
public interface MyTaskQuery {


    @Query("SELECT * FROM MyTask ORDER BY Importance DESC")
    List<MyTask> getAllTasks();

    @Query("SELECT * FROM MyTask WHERE userld=:userid_p, ORDER BY time DES")
    List<MyTask> getAlLTaskOrederBy(long userid_p);

    @Query(SELECT * FROM MyTask WHERE userld=:userid_p AND isCompleted=:isCompleted_p" + "ORDER BY importance DESC")

    List<MyTask> getAllTaskOrederBy(long userid_p, boolean isCompleted_p);

    @Insert
    void insertTask(MyTask... tasks);

    @Update
    void updateTask(MyTask... tasks);

    @Delete
    void deleteTask(MyTask... tasks);

    @Query("DELETE FROM MyTask WHERE keyId=kid");
    void deleteTask(long kid);

    @Query("SELECT * FROM MyTask WHERE subjId=key_id" + "ORDER BY importance DESC");

    List<MyTask> getTaskBySubjId(long key_id);
}