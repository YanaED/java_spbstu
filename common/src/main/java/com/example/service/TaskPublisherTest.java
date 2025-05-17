package com.example.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.*;

public class TaskPublisherTest {
    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private TaskPublisher taskPublisher;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendTaskCreatedMessage() {
        Long taskId = 1L;
        taskPublisher.sendTaskCreatedMessage(taskId);

        verify(rabbitTemplate, times(1)).convertAndSend("task.exchange", "task.created", taskId);
    }
}