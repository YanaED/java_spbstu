package com.example.common.events;

import java.time.LocalDateTime;

public class TaskCreatedEvent {
    private Long taskId;
    private String title;
    private Long userId;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;

    public TaskCreatedEvent() {
    }

    public TaskCreatedEvent(Long taskId, String title, Long userId, LocalDateTime dueDate, LocalDateTime createdAt) {
        this.taskId = taskId;
        this.title = title;
        this.userId = userId;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
