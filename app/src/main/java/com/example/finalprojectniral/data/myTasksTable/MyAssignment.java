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

/* <<<<<<<<<<<<<<  ✨ Windsurf Command 🌟 >>>>>>>>>>>>>>>> */
    /**
     * This method converts the priority string given as an argument to an integer value.
     * It takes a string argument called "priority" which represents the priority of a task.
     * The priority can be "high", "medium", or "low".
     * The method returns an integer value representing the priority level.
     * If the priority string is not one of the three valid values, the method defaults to "medium" priority level.
     * The method is used to convert the priority string retrieved from the database to an integer value
     * that can be used in the application for sorting and filtering tasks based on their priority.
     *
     * @param priority The priority of a task as a string.
     * @return An integer value representing the priority level of the task.
     */
    private int convertPriorityToInt(String priority) {
/* <<<<<<<<<<  6db93eed-b0e9-48d7-8bc1-3208c5154f79  >>>>>>>>>>> */
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

    public int getImportance() {
        return importance;
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

