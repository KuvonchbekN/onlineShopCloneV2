package uz.exadel.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.exadel.order.dto.OrderCartDto;
import uz.exadel.order.dto.ResponseItem;
import uz.exadel.order.service.OrderService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> buyByCart(@RequestBody OrderCartDto orderCartDto){
        String orderId = orderService.buyByCart(orderCartDto);
        return ResponseEntity.ok(new ResponseItem("Order Detail Id", orderId));
    }
}
