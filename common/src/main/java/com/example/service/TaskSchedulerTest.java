package com.example.service;

import com.example.model.Task;
import com.example.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

public class TaskSchedulerTest {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskPublisher taskPublisher;

    @InjectMocks
    private TaskScheduler taskScheduler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCheckIncompleteTasks() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Incomplete Task");
        task1.setDescription("Description");
        task1.setCompleted(false);
        task1.setCreatedAt(LocalDateTime.now());

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Completed Task");
        task2.setDescription("Description");
        task2.setCompleted(true);
        task2.setCreatedAt(LocalDateTime.now());

        when(taskRepository.findAll()).thenReturn(List.of(task1, task2));

        taskScheduler.checkIncompleteTasks();

        verify(taskRepository, times(1)).findAll();
        verify(taskPublisher, times(1)).sendTaskCreatedMessage(1L);
        verify(taskPublisher, never()).sendTaskCreatedMessage(2L);
    }
}