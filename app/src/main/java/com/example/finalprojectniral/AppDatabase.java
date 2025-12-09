package com.example.finalprojectniral;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

public class AppDatabase {
    @Database(entities = {MySubject.class}, version = 1)
    public abstract static class appDatabase extends RoomDatabase {

        private static volatile appDatabase INSTANCE;

        public abstract MySubject.MySubjectDao mySubjectDao();

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
