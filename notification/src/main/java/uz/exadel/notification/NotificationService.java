package uz.exadel.notification;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import uz.exadel.clients.notification.NotificationRequest;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public void send(NotificationRequest notificationRequest){
        notificationRepository.save(
                Notification.builder()
                        .toCustomerId(notificationRequest.getToUserId())
                        .toCustomerEmail(notificationRequest.getToUserName())
                        .sender("KuvonchbekN")
                        .message(notificationRequest.getMessage())
                        .sentAt(LocalDateTime.now())
                        .build()
        );
    }

    //TODO in here we should send an actual notification to just registered user!

}
