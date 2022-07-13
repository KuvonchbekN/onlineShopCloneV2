package uz.exadel.order.service.impl;

import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import uz.exadel.order.dto.OrderCartDto;
import uz.exadel.order.dto.OrderItemDto;
import uz.exadel.order.entity.OrderDetail;
import uz.exadel.order.entity.OrderItem;
import uz.exadel.order.entity.PaymentDetail;
import uz.exadel.order.productClient.ProductClient;
import uz.exadel.order.repo.OrderDetailRepo;
import uz.exadel.order.repo.OrderItemRepo;
import uz.exadel.order.repo.PaymentDetailRepo;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Transactional
@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderDetailRepo orderDetailRepo;

    @Mock
    private PaymentDetailRepo paymentDetailRepo;

    @Mock
    private OrderItemRepo orderItemRepo;

    @Mock
    private ProductClient productClient;

    private List<OrderItemDto> list = new ArrayList<>();
    private OrderCartDto orderCartDto;
    private OrderDetail orderDetail;
    private PaymentDetail paymentDetail;
    private OrderItem orderItem;
    private OrderItem orderItem2;
    private OrderItemDto orderItemDto;
    private OrderItemDto orderItemDto2;
    private Timestamp now = Timestamp.valueOf(LocalDateTime.now());
    private Set<OrderItem> orderItems = new HashSet<>();
    private List<OrderItemDto> orderItemsDto = new ArrayList<>();


    @BeforeEach
    void setUp() {
        orderItemDto = new OrderItemDto("1", 10, BigDecimal.valueOf(100));
        orderItemDto2 = new OrderItemDto("2", 20, BigDecimal.valueOf(200));
        orderItem = new OrderItem("1",orderDetail,"1",10, BigDecimal.valueOf(100L),now);
        orderItem2 = new OrderItem("2",orderDetail, "2",5,BigDecimal.valueOf(50L),now);
        orderItems.add(orderItem); orderItems.add(orderItem2);
        orderItemsDto.add(orderItemDto); orderItemsDto.add(orderItemDto2);

        list.add(orderItemDto);
        list.add(orderItemDto2);
        orderCartDto = new OrderCartDto(list, "1111");

        orderDetail = new OrderDetail("1", "1",paymentDetail,orderItems,now);
        paymentDetail = new PaymentDetail("1",BigDecimal.valueOf(100L),orderDetail, now);
    }

    @Test
    void buyByCart() {
        when(orderDetailRepo.save(any(OrderDetail.class))).thenReturn(orderDetail);
        doNothing().when(productClient).isThereEnoughProductInWarehouse(orderItemsDto);
        when(paymentDetailRepo.save(any(PaymentDetail.class))).thenReturn(paymentDetail);
        when(orderItemRepo.save(any(OrderItem.class))).thenReturn(orderItem);

        String id = orderService.buyByCart(orderCartDto);

        assertEquals(id, "1","The id of saved order detail should be equal to 1");
    }
}