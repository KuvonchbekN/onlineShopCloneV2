package uz.exadel.order.service;

import org.springframework.stereotype.Service;
import uz.exadel.order.dto.OrderCartDto;

@Service
public interface OrderService {
    String buyByCart(OrderCartDto orderCartDto);
}
