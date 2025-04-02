package Notification.Notification.service;

import Notification.Notification.model.Notification;
import Notification.Notification.model.NotificationStatus;
import Notification.Notification.model.UserInfo;
import Notification.Notification.reposiroty.NotificationRepository;
import Notification.Notification.reposiroty.UserInfoRepository;
import Notification.Notification.web.dto.NotificationRequest;
import Notification.Notification.web.dto.UserInfoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class NotificationService {
    private final UserInfoRepository userInfoRepository;
    private final MailSender mailSender;
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(UserInfoRepository userInfoRepository, MailSender mailSender, NotificationRepository notificationRepository) {
        this.userInfoRepository = userInfoRepository;
        this.mailSender = mailSender;
        this.notificationRepository = notificationRepository;
    }

    public UserInfo getUserInfoById(UUID userId) {
        return this.userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new NullPointerException("User not found"));
    }

    public UserInfo addUser (UserInfoRequest userInfoRequest) {
        Optional<UserInfo> optionalUserInfo = this.userInfoRepository.findByUserId(userInfoRequest.getUserId());

        if (optionalUserInfo.isPresent()) {
            UserInfo userInfo = optionalUserInfo.get();
            userInfo.setUserId(userInfoRequest.getUserId());
            userInfo.setContactInfo(userInfoRequest.getContactInfo());
            return this.userInfoRepository.save(userInfo);
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userInfoRequest.getUserId());
        userInfo.setContactInfo(userInfoRequest.getContactInfo());
        return this.userInfoRepository.save(userInfo);

    }

    public Notification sendNotification(NotificationRequest notificationRequest) {
        UUID userId = notificationRequest.getUserId();
        UserInfo userInfo = getUserInfoById(userId);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userInfo.getContactInfo());
        message.setSubject(notificationRequest.getSubject());
        message.setText(notificationRequest.getBody());

        Notification notification = new Notification();
        notification.setSubject(notificationRequest.getSubject());
        notification.setBody(notificationRequest.getBody());
        notification.setCreatedOn(LocalDate.now());
        notification.setUserId(userId);

        try {
            mailSender.send(message);
            notification.setNotificationStatus(NotificationStatus.SUCCEEDED);
        } catch (Exception e) {
            notification.setNotificationStatus(NotificationStatus.FAILED);
            System.out.printf("There was an issue sending an email to %s due to %s.%n",
                    userInfo.getContactInfo(), e.getMessage());
        }

        return this.notificationRepository.save(notification);
    }
}
