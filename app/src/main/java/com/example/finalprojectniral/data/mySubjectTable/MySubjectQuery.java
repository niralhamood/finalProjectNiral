package com.example.finalprojectniral.data.mySubjectTable;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

public interface MySubjectQuery {/** اعادة جميع معيات جدول المواضيع *
 * قائمة من المواضيع *return@
 */

@Dao
public interface MySubjectQuery {

    @Insert
    long insert(MySubject subject);

    @Update
    int update(MySubject subject);

    @Delete
    int delete(MySubject subject);

    @Query("SELECT * FROM MySubject ORDER BY title ASC")
    List<MySubject> getAllSubjects();


