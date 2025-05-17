package com.example.taskservice.messaging;

import com.example.common.events.TaskCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TaskEventPublisher {
    
    private static final Logger logger = LoggerFactory.getLogger(TaskEventPublisher.class);
    
    private final RabbitTemplate rabbitTemplate;
    
    @Value("${spring.rabbitmq.exchange}")
    private String exchange;
    
    @Value("${spring.rabbitmq.routingkey.task-created}")
    private String taskCreatedRoutingKey;
    
    @Autowired
    public TaskEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    
    public void publishTaskCreated(TaskCreatedEvent event) {
        try {
            logger.info("Publishing task created event for task ID: {}", event.getTaskId());
            rabbitTemplate.convertAndSend(exchange, taskCreatedRoutingKey, event);
            logger.info("Task created event published successfully");
        } catch (Exception e) {
            logger.error("Error publishing task created event: {}", e.getMessage(), e);
        }
    }
}
