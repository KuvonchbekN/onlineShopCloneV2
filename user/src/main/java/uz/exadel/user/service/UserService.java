package uz.exadel.user.service;

import org.springframework.stereotype.Service;
import uz.exadel.user.dto.UserDto;
import uz.exadel.user.entity.User;

import java.util.List;

@Service
public interface UserService {
    String registerUser(UserDto userRegistrationRequest);

    List<User> getUserList();

    User getUserById(String userId);

    void deleteUser(String userId);

    String updateUser(String userId, UserDto userDto);
}
