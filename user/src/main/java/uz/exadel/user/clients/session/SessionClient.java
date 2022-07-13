package uz.exadel.user.clients.session;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("session")
public interface SessionClient {
    @PostMapping(path = "/api/session")
    ResponseEntity<?> createSession(@RequestBody ShoppingSessionDto shoppingSessionDto);

    @DeleteMapping(path = "/api/session/userSession/{userId}")
    ResponseEntity<?> deleteSessionWhenUserDeleted(@PathVariable String userId);
}
