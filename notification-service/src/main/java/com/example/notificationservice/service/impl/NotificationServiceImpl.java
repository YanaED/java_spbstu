package com.example.notificationservice.service.impl;

import com.example.common.dto.NotificationDto;
import com.example.common.events.TaskCreatedEvent;
import com.example.common.exceptions.ResourceNotFoundException;
import com.example.notificationservice.model.Notification;
import com.example.notificationservice.repository.NotificationRepository;
import com.example.notificationservice.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    private final NotificationRepository notificationRepository;
    
    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }
    
    @Override
    public List<NotificationDto> getNotificationsByUserId(Long userId) {
        logger.info("Fetching all notifications for user ID: {}", userId);
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<NotificationDto> getUnreadNotificationsByUserId(Long userId) {
        logger.info("Fetching unread notifications for user ID: {}", userId);
        return notificationRepository.findByUserIdAndReadOrderByCreatedAtDesc(userId, false)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public NotificationDto getNotificationById(Long id) {
        logger.info("Fetching notification with ID: {}", id);
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", id));
        return convertToDto(notification);
    }
    
    @Override
    @Transactional
    public NotificationDto markAsRead(Long id) {
        logger.info("Marking notification with ID: {} as read", id);
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", id));
        
        notification.setRead(true);
        Notification updatedNotification = notificationRepository.save(notification);
        return convertToDto(updatedNotification);
    }
    
    @Override
    @Transactional
    public void processTaskCreatedEvent(TaskCreatedEvent event) {
        logger.info("Processing task created event for task ID: {}", event.getTaskId());
        
        String dueDate = event.getDueDate() != null ? 
                event.getDueDate().format(DATE_FORMATTER) : "No due date";
        
        String message = String.format("New task created: '%s'. Due: %s", 
                event.getTitle(), dueDate);
        
        Notification notification = new Notification(
                "TASK_CREATED", 
                message, 
                event.getUserId()
        );
        
        notificationRepository.save(notification);
        logger.info("Notification created for user ID: {}", event.getUserId());
    }
    
    private NotificationDto convertToDto(Notification notification) {
        return new NotificationDto(
            notification.getId(),
            notification.getType(),
            notification.getMessage(),
            notification.getUserId(),
            notification.getRead(),
            notification.getCreatedAt()
        );
    }
}
