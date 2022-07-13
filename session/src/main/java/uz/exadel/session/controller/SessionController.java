package uz.exadel.session.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.exadel.session.entity.ShoppingSession;
import uz.exadel.session.payload.ResponseItem;
import uz.exadel.session.payload.ShoppingSessionDto;
import uz.exadel.session.service.SessionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/session")
public class SessionController {
    private final SessionService sessionService;

    @PostMapping
    public ResponseEntity<?> createSession(@RequestBody ShoppingSessionDto shoppingSessionDto){
        String id = sessionService.create(shoppingSessionDto);
        return ResponseEntity.ok(new ResponseItem("Successfully created!", id));
    }

    @GetMapping
    public ResponseEntity<?> getSessionList(){
        List<ShoppingSession> list = sessionService.getList();
        return ResponseEntity.ok(new ResponseItem("Session list", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSessionById(@PathVariable String id){
        ShoppingSession byId = sessionService.getById(id);
        return ResponseEntity.ok(new ResponseItem("Single Session", byId));
    }

    @GetMapping("/userId/{userId}")
    public ResponseEntity<?> getSessionByUserId(@PathVariable String userId){
        ShoppingSession shoppingSessionByUserId = sessionService.getShoppingSessionByUserId(userId);
        return ResponseEntity.ok(new ResponseItem("Session of Specific User", shoppingSessionByUserId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSession(@RequestBody ShoppingSessionDto shoppingSessionDto,@PathVariable String id){
        sessionService.update(shoppingSessionDto, id);
        return ResponseEntity.ok(new ResponseItem("Updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSession(@PathVariable String id){
        sessionService.delete(id);
        return ResponseEntity.ok(new ResponseItem("Deleted Successfully"));
    }

    @DeleteMapping( "/userSession/{userId}")
    public ResponseEntity<?> deleteUserSessionWhenUserDeleted(@PathVariable String userId){
        sessionService.deleteSessionWhenUserDeleted(userId);
        return ResponseEntity.ok(new ResponseItem("User's session is deleted!"));
    }
}
