package com.example.taskservice.service;

import com.example.common.dto.TaskDto;

import java.util.List;

public interface TaskService {
    
    List<TaskDto> getAllTasks();
    
    TaskDto getTaskById(Long id);
    
    List<TaskDto> getTasksByUserId(Long userId);
    
    TaskDto createTask(TaskDto taskDto);
    
    TaskDto updateTask(Long id, TaskDto taskDto);
    
    void deleteTask(Long id);
    
    void checkOverdueTasks();
}
