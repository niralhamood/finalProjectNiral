package com.example.finalprojectniral;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

public class AppDatabase {
    public abstract static class appDatabase extends RoomDatabase {

        private static volatile appDatabase INSTANCE;


        public static appDatabase getDatabase(Context context) {
            if (INSTANCE == null) {
                synchronized (appDatabase.class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                                context.getApplicationContext(),
                                appDatabase.class,
                                "my_database"
                        ).allowMainThreadQueries().build();
                    }
                }
            }
            return INSTANCE;
        }
    }

}
