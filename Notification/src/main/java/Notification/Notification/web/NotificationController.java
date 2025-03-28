package Notification.Notification.web;

import Notification.Notification.model.Notification;
import Notification.Notification.model.UserInfo;
import Notification.Notification.service.NotificationService;
import Notification.Notification.web.dto.NotificationRequest;
import Notification.Notification.web.dto.NotificationResponse;
import Notification.Notification.web.dto.UserInfoRequest;
import Notification.Notification.web.dto.UserInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/users")
    public ResponseEntity<UserInfoResponse> addUser(@RequestBody UserInfoRequest userInfoRequest){
        UserInfo userInfo = this.notificationService.addUser(userInfoRequest);

        UserInfoResponse userInfoResponse = new UserInfoResponse();
        userInfoResponse.setId(userInfo.getId());
        userInfoResponse.setUserId(userInfo.getUserId());
        userInfoResponse.setContactInfo(userInfo.getContactInfo());

        return ResponseEntity.status(HttpStatus.CREATED).body(userInfoResponse);
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> sendNotification(
            @RequestBody NotificationRequest notificationRequest) {

        Notification notification = this.notificationService.sendNotification(notificationRequest);

        NotificationResponse response = new NotificationResponse();
        response.setSubject(notification.getSubject());
        response.setCreatedOn(notification.getCreatedOn());
        response.setNotificationStatus(notification.getNotificationStatus());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
