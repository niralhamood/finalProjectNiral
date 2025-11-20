package com.example.finalprojectniral.data.mySubjectTable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class MySubject {
    @PrimaryKey(autoGenerate = true)
    public long key_id;
    public String title;
}
