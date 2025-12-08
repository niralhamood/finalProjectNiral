package com.example.finalprojectniral.data.myTasksTable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity

public class MyTassk {
    @PrimaryKey(autoGenerate = true)
    public long keyId;
    public int importance;
    public String shortTitle;
    public String text;
    public long time;
    public boolean isCompleted;
    public long subjId;
    public long userId;

    @Override
    public String toString() {
        return "MyTassk{" +
                "keyId=" + keyId +
                ", importance=" + importance +
                ", shortTitle='" + shortTitle + '\'' +
                ", text='" + text + '\'' +
                ", time=" + time +
                ", isCompleted=" + isCompleted +
                ", subjId=" + subjId +
                ", userId=" + userId +
                '}';
    }
}

