package com.example.service;

import com.example.model.Task;
import com.example.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskScheduler {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskPublisher taskPublisher;

    @Scheduled(fixedRate = 60000)
    public void checkIncompleteTasks() {
        List<Task> incompleteTasks = taskRepository.findAll().stream()
                .filter(task -> !task.isCompleted())
                .toList();
        for (Task task : incompleteTasks) {
            System.out.println("Reminder: Task '" + task.getTitle() + "' is incomplete!");
            taskPublisher.sendTaskCreatedMessage(task.getId());
        }
    }
}