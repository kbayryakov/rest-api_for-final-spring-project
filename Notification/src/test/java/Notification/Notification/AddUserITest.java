package Notification.Notification;

import Notification.Notification.model.UserInfo;
import Notification.Notification.reposiroty.UserInfoRepository;
import Notification.Notification.service.NotificationService;
import Notification.Notification.web.dto.UserInfoRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AddUserITest {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Test
    void addUser_happyPath() {
        UserInfoRequest userInfoRequest = new UserInfoRequest();
        userInfoRequest.setUserId(UUID.randomUUID());
        userInfoRequest.setContactInfo("user@example.com");

        notificationService.addUser(userInfoRequest);

        Optional<UserInfo> optionalUserInfo = userInfoRepository.findByUserId(userInfoRequest.getUserId());
        UserInfo userInfo = optionalUserInfo.get();
        assertEquals(userInfoRequest.getUserId(), userInfo.getUserId());
    }
}
