package Notification.Notification.web.dto;

import Notification.Notification.model.NotificationStatus;

import java.time.LocalDate;

public class NotificationResponse {

    private String subject;

    private LocalDate createdOn;

    private NotificationStatus notificationStatus;

    public NotificationResponse() {
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public NotificationStatus getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(NotificationStatus notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }
}
