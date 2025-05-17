package com.example.taskservice.controller;

import com.example.common.dto.TaskDto;
import com.example.taskservice.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private TaskDto taskDto;
    private List<TaskDto> taskDtoList;

    @BeforeEach
    public void setup() {
        LocalDateTime now = LocalDateTime.now();
        
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
        
        taskDtoList = Arrays.asList(taskDto);
    }

    @Test
    public void getAllTasks_ShouldReturnAllTasks() throws Exception {
        // Arrange
        when(taskService.getAllTasks()).thenReturn(taskDtoList);

        // Act & Assert
        mockMvc.perform(get("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(taskDto.getId()))
                .andExpect(jsonPath("$[0].title").value(taskDto.getTitle()))
                .andExpect(jsonPath("$[0].status").value(taskDto.getStatus()));
    }

    @Test
    public void getTaskById_ShouldReturnTask() throws Exception {
        // Arrange
        when(taskService.getTaskById(1L)).thenReturn(taskDto);

        // Act & Assert
        mockMvc.perform(get("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskDto.getId()))
                .andExpect(jsonPath("$.title").value(taskDto.getTitle()))
                .andExpect(jsonPath("$.status").value(taskDto.getStatus()));
    }

    @Test
    public void getTasksByUserId_ShouldReturnTasks() throws Exception {
        // Arrange
        when(taskService.getTasksByUserId(1L)).thenReturn(taskDtoList);

        // Act & Assert
        mockMvc.perform(get("/api/tasks/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(taskDto.getId()))
                .andExpect(jsonPath("$[0].title").value(taskDto.getTitle()))
                .andExpect(jsonPath("$[0].userId").value(taskDto.getUserId()));
    }

    @Test
    public void createTask_ShouldCreateAndReturnTask() throws Exception {
        // Arrange
        when(taskService.createTask(any(TaskDto.class))).thenReturn(taskDto);

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(taskDto.getId()))
                .andExpect(jsonPath("$.title").value(taskDto.getTitle()))
                .andExpect(jsonPath("$.status").value(taskDto.getStatus()));
    }

    @Test
    public void updateTask_ShouldUpdateAndReturnTask() throws Exception {
        // Arrange
        when(taskService.updateTask(eq(1L), any(TaskDto.class))).thenReturn(taskDto);

        // Act & Assert
        mockMvc.perform(put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskDto.getId()))
                .andExpect(jsonPath("$.title").value(taskDto.getTitle()))
                .andExpect(jsonPath("$.status").value(taskDto.getStatus()));
    }

    @Test
    public void deleteTask_ShouldReturnNoContent() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
