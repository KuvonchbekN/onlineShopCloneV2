package uz.exadel.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uz.exadel.user.clients.session.ResponseItem;
import uz.exadel.user.dto.UserDto;
import uz.exadel.user.entity.User;
import uz.exadel.user.service.UserService;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto userRegistrationRequest){
        String userId = userService.registerUser(userRegistrationRequest);

        return ResponseEntity.ok(new ResponseItem("User Id", userId));
    }

    @GetMapping
    public ResponseEntity<?> getUserList(){
        return ResponseEntity.ok(new ResponseItem("User List", userService.getUserList()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable String userId){
        User userById = userService.getUserById(userId);
        return ResponseEntity.ok(new ResponseItem("User", userById));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody UserDto userDto){
        String id = userService.updateUser(userId, userDto);
        return ResponseEntity.ok(new ResponseItem("User id", id));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        return ResponseEntity.ok(new ResponseItem("User deleted successfully!"));
    }
}
