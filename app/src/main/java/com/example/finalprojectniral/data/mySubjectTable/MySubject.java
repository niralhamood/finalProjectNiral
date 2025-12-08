package com.example.finalprojectniral.data.mySubjectTable;

import android.content.Context;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Update;

import java.util.List;

@Entity
public class MySubject {
    @PrimaryKey(autoGenerate = true)
    public long key_id;
    public String title;

    public long getKey_id()
    {
        return key_id;
    }
    @Entity
    public class User {
        @PrimaryKey(autoGenerate = true)
        public long user_id;

        public String user_name;
        public String last_login;
        public String preferences;  // تفضيلات المستخدم مثل اللغة أو الإعدادات المفضلة
    }
    @Entity
    public class Task {
        @PrimaryKey(autoGenerate = true)
        public long task_id;

        public String task_name;
        public String task_description;
        public String due_date;
        public String status;  // حالة المهمة مثل "قيد التنفيذ" أو "مكتملة"
    }
    @Entity
    public class Quote {
        @PrimaryKey(autoGenerate = true)
        public long quote_id;

        public String quote_text;
        public String author;
        public String category;  // فئة الاقتباس مثل "تحفيز"، "فلسفة"، إلخ
    }
    @Entity
    public class Chat {
        @PrimaryKey(autoGenerate = true)
        public long chat_id;

        public String user_message;
        public String ai_response;
        public String timestamp;  // الوقت الذي تم فيه إرسال الرسالة

        @Override
        public String toString() {
            return "Chat{" +
                    "chat_id=" + chat_id +
                    ", user_message='" + user_message + '\'' +
                    ", ai_response='" + ai_response + '\'' +
                    ", timestamp='" + timestamp + '\'' +
                    '}';
        }
    }
        @Dao
        public interface MySubjectDao {

            @Insert
            long insert(MySubject subject);

            @Update
            int update(MySubject subject);

            @Delete
            int delete(MySubject subject);

            @Query("SELECT * FROM MySubject ORDER BY title ASC")
            List<MySubject> getAllSubjects();
        }
    @Database(entities = {MySubject.class}, version = 1)
    public abstract static class AppDatabase extends RoomDatabase {

        private static volatile AppDatabase INSTANCE;

        public abstract MySubjectDao mySubjectDao();

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


}







