package com.example.service;

import com.example.model.Task;
import com.example.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class NotificationListenerTest {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private NotificationListener notificationListener;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandleTaskCreated() {
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setCompleted(false);
        task.setCreatedAt(LocalDateTime.now());

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        notificationListener.handleTaskCreated(taskId);

        verify(taskRepository, times(1)).findById(taskId);
    }
}