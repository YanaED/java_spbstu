package com.example.notificationservice.messaging;

import com.example.common.events.TaskCreatedEvent;
import com.example.notificationservice.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskEventListener {

    private static final Logger logger = LoggerFactory.getLogger(TaskEventListener.class);
    
    private final NotificationService notificationService;
    
    @Autowired
    public TaskEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    @RabbitListener(queues = "#{taskCreatedQueue.name}")
    public void handleTaskCreatedEvent(TaskCreatedEvent event) {
        logger.info("Received task created event for task ID: {}", event.getTaskId());
        try {
            notificationService.processTaskCreatedEvent(event);
            logger.info("Successfully processed task created event");
        } catch (Exception e) {
            logger.error("Error processing task created event: {}", e.getMessage(), e);
        }
    }
}
