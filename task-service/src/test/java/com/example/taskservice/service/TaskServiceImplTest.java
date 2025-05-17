package com.example.taskservice.service;

import com.example.common.dto.TaskDto;
import com.example.common.events.TaskCreatedEvent;
import com.example.common.exceptions.ResourceNotFoundException;
import com.example.taskservice.model.Task;
import com.example.taskservice.repository.TaskRepository;
import com.example.taskservice.service.impl.TaskServiceImpl;
import com.example.taskservice.messaging.TaskEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskEventPublisher taskEventPublisher;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;
    private TaskDto taskDto;

    @BeforeEach
    public void setup() {
        LocalDateTime now = LocalDateTime.now();
        
        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus("PENDING");
        task.setUserId(1L);
        task.setDueDate(now.plusDays(1));
        task.setCreatedAt(now);
        task.setUpdatedAt(now);
        
        taskDto = new TaskDto(
                1L,
                "Test Task",
                "Test Description",
                "PENDING",
                1L,
                now.plusDays(1),
                now,
                now
        );
    }

    @Test
    public void getAllTasks_ShouldReturnAllTasks() {
        // Arrange
        when(taskRepository.findAll()).thenReturn(Arrays.asList(task));

        // Act
        List<TaskDto> result = taskService.getAllTasks();

        // Assert
        assertEquals(1, result.size());
        assertEquals(task.getId(), result.get(0).getId());
        assertEquals(task.getTitle(), result.get(0).getTitle());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void getTaskById_ShouldReturnTask_WhenTaskExists() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Act
        TaskDto result = taskService.getTaskById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(task.getId(), result.getId());
        assertEquals(task.getTitle(), result.getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    public void getTaskById_ShouldThrowException_WhenTaskDoesNotExist() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> taskService.getTaskById(1L));
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    public void createTask_ShouldCreateAndReturnTask() {
        // Arrange
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // Act
        TaskDto result = taskService.createTask(taskDto);

        // Assert
        assertNotNull(result);
        assertEquals(task.getId(), result.getId());
        assertEquals(task.getTitle(), result.getTitle());
        
        // Verify that the task was saved
        verify(taskRepository, times(1)).save(any(Task.class));
        
        // Verify that the event publisher was called with correct task data
        verify(taskEventPublisher, times(1)).publishTaskCreated(any(TaskCreatedEvent.class));
    }

    @Test
    public void updateTask_ShouldUpdateAndReturnTask_WhenTaskExists() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        taskDto.setTitle("Updated Task");
        taskDto.setDescription("Updated Description");

        // Act
        TaskDto result = taskService.updateTask(1L, taskDto);

        // Assert
        assertNotNull(result);
        assertEquals(task.getId(), result.getId());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void updateTask_ShouldThrowException_WhenTaskDoesNotExist() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> taskService.updateTask(1L, taskDto));
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(0)).save(any(Task.class));
    }

    @Test
    public void deleteTask_ShouldDeleteTask_WhenTaskExists() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Act
        taskService.deleteTask(1L);

        // Assert
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    public void deleteTask_ShouldThrowException_WhenTaskDoesNotExist() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> taskService.deleteTask(1L));
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(0)).delete(any(Task.class));
    }
}
