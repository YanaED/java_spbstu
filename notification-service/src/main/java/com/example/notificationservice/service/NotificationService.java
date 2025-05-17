package com.example.notificationservice.service;

import com.example.common.dto.NotificationDto;
import com.example.common.events.TaskCreatedEvent;

import java.util.List;

public interface NotificationService {
    
    List<NotificationDto> getNotificationsByUserId(Long userId);
    
    List<NotificationDto> getUnreadNotificationsByUserId(Long userId);
    
    NotificationDto getNotificationById(Long id);
    
    NotificationDto markAsRead(Long id);
    
    void processTaskCreatedEvent(TaskCreatedEvent event);
}
