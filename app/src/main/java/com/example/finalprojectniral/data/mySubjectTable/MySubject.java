package com.example.finalprojectniral.data.mySubjectTable;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Update;

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
    }
    @Dao
    public interface UserDao {
        @Insert
        void insertUser(User user);

        @Update
        void updateUser(User user);

        @Delete
        void deleteUser(User user);

        @Query("SELECT * FROM User WHERE user_id = :userId")
        User getUserById(long userId);
    }






}
