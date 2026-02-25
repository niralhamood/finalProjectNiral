package com.example.finalprojectniral;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.finalprojectniral.data.myTasksTable.MyAssignment;
import com.example.finalprojectniral.data.myTasksTable.MyAssignmentQuery;
import com.example.finalprojectniral.data.myUserTable.MyUser;
import com.example.finalprojectniral.data.myUserTable.MyUserQuery;

@Database(entities = {MyUser.class, MyAssignment.class}, version = 1, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {
    public abstract MyUserQuery myUserQuery();
    public abstract MyAssignmentQuery myAssignmentQuery();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "my_database"
                    ).allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}