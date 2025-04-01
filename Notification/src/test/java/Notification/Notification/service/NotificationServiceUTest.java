package Notification.Notification.service;

import Notification.Notification.model.UserInfo;
import Notification.Notification.reposiroty.NotificationRepository;
import Notification.Notification.reposiroty.UserInfoRepository;
import Notification.Notification.web.dto.UserInfoRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSender;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceUTest {
    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserInfoRepository userInfoRepository;

    @Mock
    private MailSender mailSender;


    @Test
    void testAddUser_NewUser() {
        UserInfoRequest userInfoRequest = new UserInfoRequest();
        userInfoRequest.setUserId(UUID.randomUUID());
        userInfoRequest.setContactInfo("testuser@example.com");

        UserInfo newUserInfo = new UserInfo();
        newUserInfo.setUserId(userInfoRequest.getUserId());
        newUserInfo.setContactInfo(userInfoRequest.getContactInfo());

        when(userInfoRepository.findByUserId(userInfoRequest.getUserId())).thenReturn(Optional.empty());
        when(userInfoRepository.save(any(UserInfo.class))).thenReturn(newUserInfo);

        UserInfo result = notificationService.addUser(userInfoRequest);

        assertNotNull(result);
        assertEquals(userInfoRequest.getUserId(), result.getUserId());
        assertEquals(userInfoRequest.getContactInfo(), result.getContactInfo());

        verify(userInfoRepository, times(1)).findByUserId(userInfoRequest.getUserId());
        verify(userInfoRepository, times(1)).save(any(UserInfo.class));
    }

    @Test
    void testGetUserInfoById_UserFound() {

        UUID userId = UUID.randomUUID();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setContactInfo("user@example.com");

        when(userInfoRepository.findByUserId(userId)).thenReturn(Optional.of(userInfo));

        UserInfo result = notificationService.getUserInfoById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals("user@example.com", result.getContactInfo());
        verify(userInfoRepository, times(1)).findByUserId(userId);
    }
}
