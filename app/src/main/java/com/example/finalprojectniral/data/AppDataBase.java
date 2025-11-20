package com.example.finalprojectniral.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.finalprojectniral.data.mySubjectTable.MySubject;
import com.example.finalprojectniral.data.mySubjectTable.MySubjectQuery;
import com.example.finalprojectniral.data.myTasksTable.MyTaskQuery;
import com.example.finalprojectniral.data.myTasksTable.TasksActivity;
import com.example.finalprojectniral.data.myUserTable.MyUser;
import com.example.finalprojectniral.data.myUserTable.MyUserQuery;

public class AppDataBase {
    @Database(entities = {MyUser.class, MySubject.class, TasksActivity.class}, version = 1)
    /**
     الفنة العمؤولة عن بناء قاعدة البايانات بكل جداولها *
     وتوفر لنا كانن للتعامل مع قاعدة البيانات •
     public abstract class AppDataBase extends RoomDatabase(
     /**
     كائن للتعامل مع قاعدة البيانات *
     /*
     private static AppDataBase db;
     /**
     يعيد كانن لعمليات جدول المستعملين *
     * @return
     */
    public abstract  class AppDataBase extends RoomDatabase {
        /**
         * كائن لتعامل كع قاعدة البينات
         */
        private static AppDataBase db;

        public abstract MyUserQuery getMyUserQuery();

        /**
         * يعيد كائن لعمليات جدول الموضيع *
         * " @return
         */
        public abstract MySubjectQuery getMySubjectQuery();
        /**
         يعيد كانن لعمليات جدول المهمات *
         * @return
         */

        public abstract MyTaskQuery getMyTaskQuery();

        /* بناء قاعدة البيانات واعادة كائن يؤشر عليها *
         "@param context
          * @return
          */
        public static AppDataBase getDB(Context context) {
            if (db == null) {
                db = Room.databaseBuilder(context,
                                AppDataBase.class,
                                "samihDataBase")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build();

            }
            return db;
        }
    }
}





