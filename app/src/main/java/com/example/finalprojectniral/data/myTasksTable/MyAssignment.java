package com.example.finalprojectniral.data.myTasksTable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity

public class MyAssignment {
    @PrimaryKey(autoGenerate = true)
    public long keyId;
    public  long userId;
    public int importance;
    public String shortTitle;
    public String text;
    public long time;
    public boolean isCompleted;
    public String file;

    public MyAssignment() {
    }

    public MyAssignment(String title, String priority) {
        this.shortTitle = title;
        this.text = title;
        this.importance = convertPriorityToInt(priority);
        this.time = System.currentTimeMillis();
        this.isCompleted = false;
    }

    // هذا الدالة تقوم بتحويل المستوي الخاص بالمهمة من نوع سلسلة نصية إلى مستوى صحيح من نوع رقم صحيح.
    // المستوي الخاص بالمهمة يمكن أن يكون "عالي" أو "متوسط" أو "منخفض".
    // إذا كان مستوى المهمة غير معروف، فسيتم استخدام المستوى الافتراضي "متوسط".
    private int convertPriorityToInt(String priority) {
        switch (priority.toLowerCase()) {
            case "high":
                return 3;
            case "medium":
                return 2;
            case "low":
                return 1;
            default:
                return 2;
        }
    }
    public long getKeyId() {
        return keyId;
    }

    public void setKeyId(long keyId) {
        this.keyId = keyId;
    }

/* <<<<<<<<<<<<<<  ✨ Windsurf Command 🌟 >>>>>>>>>>>>>>>> */
    /**
     * Get the importance of the task.
     *
     * @return the importance of the task as an integer between 1 and 3
     */
    public int getImportance() {
        return importance;
/* <<<<<<<<<<  1401eb70-0cec-40e9-be8e-4cc85e38e76d  >>>>>>>>>>> */
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}

