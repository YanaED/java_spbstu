package com.example.service;

import com.example.model.Task;
import com.example.repository.TaskRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {
    @Autowired
    private TaskRepository taskRepository;

    @RabbitListener(queues = "task.notification.queue")
    public void handleTaskCreated(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        System.out.println("New task created: ID=" + taskId + ", Title=" + task.getTitle());
    }
}