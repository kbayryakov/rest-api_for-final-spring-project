package Notification.Notification.web;

import Notification.Notification.model.Notification;
import Notification.Notification.model.NotificationStatus;
import Notification.Notification.model.UserInfo;
import Notification.Notification.service.NotificationService;
import Notification.Notification.web.dto.NotificationRequest;
import Notification.Notification.web.dto.UserInfoRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
public class NotificationControllerApiTest {
    @MockitoBean
    private NotificationService notificationService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testAddUser_Success() throws Exception {

        UserInfoRequest request = new UserInfoRequest();
        request.setUserId(UUID.randomUUID());
        request.setContactInfo("user@example.com");

        UserInfo userInfo = new UserInfo();
        userInfo.setId(UUID.randomUUID());
        userInfo.setUserId(request.getUserId());
        userInfo.setContactInfo(request.getContactInfo());

        when(notificationService.addUser(any(UserInfoRequest.class))).thenReturn(userInfo);

        mockMvc.perform(post("/api/v1/notifications/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("userId").isNotEmpty())
                .andExpect(jsonPath("contactInfo").isNotEmpty());

        verify(notificationService, times(1)).addUser(any(UserInfoRequest.class));
    }

    @Test
    void testSendNotification_Success() throws Exception {

        NotificationRequest request = new NotificationRequest();
        request.setSubject("Test");
        request.setBody("Test");

        Notification notification = new Notification();
        notification.setSubject("Test");
        notification.setBody("Test");
        notification.setCreatedOn(LocalDate.now());
        notification.setNotificationStatus(NotificationStatus.SUCCEEDED);

        when(notificationService.sendNotification(any(NotificationRequest.class))).thenReturn(notification);

        mockMvc.perform(post("/api/v1/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("subject").isNotEmpty())
                .andExpect(jsonPath("createdOn").isNotEmpty())
                .andExpect(jsonPath("notificationStatus").isNotEmpty());

        verify(notificationService, times(1)).sendNotification(any(NotificationRequest.class));
    }
}
