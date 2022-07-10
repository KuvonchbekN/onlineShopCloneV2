package uz.exadel.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.exadel.amqp.RabbitMQMessageProducer;
import uz.exadel.user.clients.notification.NotificationRequest;
import uz.exadel.user.clients.session.SessionClient;
import uz.exadel.user.clients.session.ShoppingSessionDto;
import uz.exadel.user.dto.UserDto;
import uz.exadel.user.entity.User;
import uz.exadel.user.exception.UserAlreadyExistsException;
import uz.exadel.user.exception.UserNotFoundException;
import uz.exadel.user.mail.EmailServiceImpl;
import uz.exadel.user.mapper.UserMapper;
import uz.exadel.user.repository.UserRepository;
import uz.exadel.user.service.UserService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;
    private final UserMapper userMapper;
    private final EmailServiceImpl emailService;
    private final SessionClient sessionClient;


    @Override
    public String registerUser(UserDto userRegistrationRequest) {
        User user = userMapper.dtoToUser(userRegistrationRequest);

        String userId = checkUserEmail(user);
        user.setId(userId);

        NotificationRequest notificationRequest = new NotificationRequest(
                user.getId(),
                user.getEmail(),
                String.format("Hi %s, welcome to online shop website... ", user.getFullName())
        );


        rabbitMQMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );

        ShoppingSessionDto shoppingSessionDto =
                new ShoppingSessionDto(userId, BigDecimal.valueOf(0));
        sessionClient.createSession(shoppingSessionDto);

        return user.getId();
    }

    @Override
    public List<User> getUserList() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(String userId) {
        Optional<User> byId = userRepository.findById(userId);
        if (byId.isEmpty()) throw new UserNotFoundException("User with this id is not found!");

        return byId.get();
    }

    @Override
    public void deleteUser(String userId) {
        existsById(userId);
        userRepository.deleteById(userId);
        sessionClient.deleteSessionWhenUserDeleted(userId);
    }

    @Override
    public String updateUser(String userId, UserDto userDto) {
        existsById(userId);

        User user = userMapper.dtoToUser(userDto);
        user.setId(userId);
        userRepository.save(user);

        return userId;
    }



    private String checkUserEmail(User user){
        boolean exists = userRepository.existsByEmail(user.getEmail());
        if (exists) throw new UserAlreadyExistsException("User with this email already exists!");

        User savedUser = userRepository.saveAndFlush(user);
        return savedUser.getId();
    }

    private void existsById(String id){
        boolean exists = userRepository.existsById(id);
        if (!exists) throw new UserNotFoundException("User with this id is not found!");
    }

}
