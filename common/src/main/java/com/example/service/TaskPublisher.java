package com.example.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskPublisher {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendTaskCreatedMessage(Long taskId) {
        rabbitTemplate.convertAndSend("task.exchange", "task.created", taskId);
    }
}