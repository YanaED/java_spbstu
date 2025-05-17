package com.example.taskservice.service.impl;

import com.example.common.dto.TaskDto;
import com.example.common.events.TaskCreatedEvent;
import com.example.common.exceptions.ResourceNotFoundException;
import com.example.taskservice.model.Task;
import com.example.taskservice.repository.TaskRepository;
import com.example.taskservice.service.TaskService;
import com.example.taskservice.messaging.TaskEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
    
    private final TaskRepository taskRepository;
    private final TaskEventPublisher taskEventPublisher;
    
    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, TaskEventPublisher taskEventPublisher) {
        this.taskRepository = taskRepository;
        this.taskEventPublisher = taskEventPublisher;
    }
    
    @Override
    @Cacheable(value = "tasks")
    public List<TaskDto> getAllTasks() {
        logger.info("Fetching all tasks");
        return taskRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Cacheable(value = "tasks", key = "#id")
    public TaskDto getTaskById(Long id) {
        logger.info("Fetching task with id: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
        return convertToDto(task);
    }
    
    @Override
    @Cacheable(value = "tasksByUser", key = "#userId")
    public List<TaskDto> getTasksByUserId(Long userId) {
        logger.info("Fetching tasks for user id: {}", userId);
        return taskRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "tasks", allEntries = true),
        @CacheEvict(value = "tasksByUser", key = "#taskDto.userId")
    })
    public TaskDto createTask(TaskDto taskDto) {
        logger.info("Creating new task: {}", taskDto.getTitle());
        
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(taskDto.getStatus() != null ? taskDto.getStatus() : "PENDING");
        task.setUserId(taskDto.getUserId());
        task.setDueDate(taskDto.getDueDate());
        
        Task savedTask = taskRepository.save(task);
        
        // Publish task created event asynchronously
        publishTaskCreatedEvent(savedTask);
        
        return convertToDto(savedTask);
    }
    
    @Async
    protected void publishTaskCreatedEvent(Task task) {
        TaskCreatedEvent event = new TaskCreatedEvent(
            task.getId(),
            task.getTitle(),
            task.getUserId(),
            task.getDueDate(),
            task.getCreatedAt()
        );
        
        taskEventPublisher.publishTaskCreated(event);
        logger.info("Published task created event for task id: {}", task.getId());
    }
    
    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "tasks", key = "#id"),
        @CacheEvict(value = "tasks", allEntries = true),
        @CacheEvict(value = "tasksByUser", allEntries = true)
    })
    public TaskDto updateTask(Long id, TaskDto taskDto) {
        logger.info("Updating task with id: {}", id);
        
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
        
        existingTask.setTitle(taskDto.getTitle());
        existingTask.setDescription(taskDto.getDescription());
        existingTask.setStatus(taskDto.getStatus());
        existingTask.setDueDate(taskDto.getDueDate());
        
        Task updatedTask = taskRepository.save(existingTask);
        return convertToDto(updatedTask);
    }
    
    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "tasks", key = "#id"),
        @CacheEvict(value = "tasks", allEntries = true),
        @CacheEvict(value = "tasksByUser", allEntries = true)
    })
    public void deleteTask(Long id) {
        logger.info("Deleting task with id: {}", id);
        
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
        
        taskRepository.delete(task);
    }
    
    @Override
    @Async
    @Scheduled(fixedRate = 3600000) // Check every hour
    public void checkOverdueTasks() {
        logger.info("Checking for overdue tasks");
        List<Task> overdueTasks = taskRepository.findOverdueTasks(LocalDateTime.now());
        
        if (!overdueTasks.isEmpty()) {
            logger.info("Found {} overdue tasks", overdueTasks.size());
            // In a real implementation, you might want to notify users or take other actions
        }
    }
    
    private TaskDto convertToDto(Task task) {
        return new TaskDto(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getStatus(),
            task.getUserId(),
            task.getDueDate(),
            task.getCreatedAt(),
            task.getUpdatedAt()
        );
    }
}
