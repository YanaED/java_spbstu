package com.example.repository;

import com.example.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class TaskRepositoryTest {
    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void testSaveTask() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setCompleted(false);
        task.setCreatedAt(LocalDateTime.now());

        Task savedTask = taskRepository.save(task);

        assertNotNull(savedTask.getId());
        assertEquals("Test Task", savedTask.getTitle());
        assertEquals("Test Description", savedTask.getDescription());
        assertEquals(false, savedTask.isCompleted());
        assertNotNull(savedTask.getCreatedAt());
    }

    @Test
    public void testFindTaskById() {
        Task task = new Task();
        task.setTitle("Find Task");
        task.setDescription("Find Description");
        task.setCompleted(true);
        task.setCreatedAt(LocalDateTime.now());

        Task savedTask = taskRepository.save(task);
        Task foundTask = taskRepository.findById(savedTask.getId()).orElse(null);

        assertNotNull(foundTask);
        assertEquals(savedTask.getId(), foundTask.getId());
        assertEquals("Find Task", foundTask.getTitle());
        assertEquals("Find Description", foundTask.getDescription());
        assertEquals(true, foundTask.getCompleted());
    }
}