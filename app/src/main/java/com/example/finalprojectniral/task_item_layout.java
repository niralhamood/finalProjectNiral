package com.example.finalprojectniral;

public class task_item_layout {

    private String title;
    private String priority;

    public task_item_layout(String title, String priority) {
        this.title = title;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public String getPriority() {
        return priority;
    }
}
