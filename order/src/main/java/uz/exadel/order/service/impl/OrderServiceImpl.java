package uz.exadel.order.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.exadel.clients.product.OrderItemDto;
import uz.exadel.clients.product.ProductClient;
import uz.exadel.order.dto.OrderCartDto;
import uz.exadel.order.entity.OrderDetail;
import uz.exadel.order.entity.OrderItem;
import uz.exadel.order.entity.PaymentDetail;
import uz.exadel.order.repo.OrderDetailRepo;
import uz.exadel.order.repo.OrderItemRepo;
import uz.exadel.order.repo.PaymentDetailRepo;
import uz.exadel.order.service.OrderService;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderDetailRepo orderDetailRepo;
    private final PaymentDetailRepo paymentDetailRepo;
    private final OrderItemRepo orderItemRepo;

    private final ProductClient productClient;

    @Override
    public String buyByCart(OrderCartDto orderCartDto) {
        List<OrderItemDto> orderedItemsDto =
                orderCartDto.getOrderedItemsDto();

        String userId = orderCartDto.getUserId();

        productClient.isThereEnoughProductInWarehouse(orderedItemsDto); //this updates product table


        BigDecimal totalPrice = calculateTotalPrice(orderedItemsDto);
        PaymentDetail paymentDetail= savePaymentDetail(totalPrice);

        OrderDetail orderDetail = saveOrderDetail(userId, paymentDetail);

        saveOrderItem(orderedItemsDto, orderDetail);

        return orderDetail.getId();
    }

    private void saveOrderItem(List<OrderItemDto> orderItemDtoList, OrderDetail orderDetail){
        for (OrderItemDto orderItemDto : orderItemDtoList) {
            OrderItem orderItem = OrderItem.builder()
                    .productId(orderItemDto.getProductId())
                    .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                    .price(orderItemDto.getPrice())
                    .orderDetail(orderDetail)
                    .productQuantity(orderItemDto.getProductQuantity())
                    .build();
            orderItemRepo.save(orderItem);
        }
    }

    private OrderDetail saveOrderDetail(String userId, PaymentDetail paymentDetail){
        OrderDetail orderDetail = OrderDetail.builder()
                .paymentDetail(paymentDetail)
                .userId(userId)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        return orderDetailRepo.save(orderDetail);
    }


    private PaymentDetail savePaymentDetail(BigDecimal totalAmount){
        PaymentDetail paymentDetail = PaymentDetail.builder()
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .totalPrice(totalAmount)
                .build();
        return paymentDetailRepo.save(paymentDetail);
    }

    private BigDecimal calculateTotalPrice( List<OrderItemDto> orderedItemsDto){
        AtomicReference<BigDecimal> totalAmount = new AtomicReference<>(BigDecimal.valueOf(0));
        orderedItemsDto.forEach(orderItemDto -> {
            BigDecimal value = orderItemDto.getPrice().multiply(BigDecimal.valueOf(orderItemDto.getProductQuantity()));
            totalAmount.updateAndGet(v -> v.add(value));
        });
        return totalAmount.get();
    }
}

