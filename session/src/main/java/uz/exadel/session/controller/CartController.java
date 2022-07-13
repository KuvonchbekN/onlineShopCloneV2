package uz.exadel.session.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.exadel.session.entity.CartItem;
import uz.exadel.session.payload.CartItemDto;
import uz.exadel.session.payload.ResponseItem;
import uz.exadel.session.service.CartService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<?> createCart(@RequestBody CartItemDto cartItemDto){
        String id = cartService.create(cartItemDto);
        return ResponseEntity.ok(new ResponseItem("Successfully created!", id));
    }

    @GetMapping
    public ResponseEntity<?> getCartList(){
        List<CartItem> list = cartService.getList();
        return ResponseEntity.ok(new ResponseItem("CartItem List", list));
    }

    @GetMapping("/sessionCarts/{sessionId}")
    public ResponseEntity<?> getSessionCartsList(@PathVariable String sessionId){
        return ResponseEntity.ok(new ResponseItem("Specific Session Id", cartService.getCartItemListBySessionId(sessionId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCartById(@PathVariable String id){
        CartItem byId = cartService.getById(id);
        return ResponseEntity.ok(new ResponseItem("Cart by Id", byId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCart(@RequestBody CartItemDto cartItemDto,@PathVariable String id){
        cartService.update(cartItemDto, id);
        return ResponseEntity.ok(new ResponseItem("Updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable String id){
        cartService.delete(id);
        return ResponseEntity.ok(new ResponseItem("Successfully deleted!"));
    }
}
