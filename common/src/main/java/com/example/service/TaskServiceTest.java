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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskPublisher taskPublisher;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTask() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setCompleted(false);
        task.setCreatedAt(LocalDateTime.now());

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.createTask("Test Task", "Test Description");

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertEquals(false, result.isCompleted());
        verify(taskRepository, times(1)).save(any(Task.class));
        verify(taskPublisher, times(1)).sendTaskCreatedMessage(1L);
    }

    @Test
    public void testGetTaskById_CacheHit() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Cached Task");
        task.setDescription("Cached Description");
        task.setCompleted(true);
        task.setCreatedAt(LocalDateTime.now());

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result1 = taskService.getTaskById(1L);
        Task result2 = taskService.getTaskById(1L);

        assertEquals("Cached Task", result1.getTitle());
        assertEquals("Cached Task", result2.getTitle());
        verify(taskRepository, times(1)).findById(1L); // Вызывается только раз из-за кэша
    }
}