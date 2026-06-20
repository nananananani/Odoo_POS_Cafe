package com.cafepos;

import com.cafepos.entity.*;
import com.cafepos.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CafePosApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FloorRepository floorRepository;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PosSessionRepository posSessionRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private SelfOrderConfigRepository selfOrderConfigRepository;

    @Autowired
    private TableQrTokenRepository tableQrTokenRepository;

    @Test
    void contextLoads() {
        // Simple context check
    }

    @Test
    void testUserRepository() {
        User user = User.builder()
                .name("Test User")
                .email("test@cafepos.com")
                .passwordHash("hashedpassword")
                .role(UserRole.ADMIN)
                .status(UserStatus.ACTIVE)
                .build();
        userRepository.save(user);

        Optional<User> found = userRepository.findByEmail("test@cafepos.com");
        assertTrue(found.isPresent());
        assertEquals("Test User", found.get().getName());
    }

    @Test
    void testCouponRepository() {
        Coupon coupon = Coupon.builder()
                .name("10% Off")
                .code("DISCOUNT10")
                .discountType(DiscountType.PERCENT)
                .value(new BigDecimal("10.00"))
                .active(true)
                .build();
        couponRepository.save(coupon);

        Optional<Coupon> found = couponRepository.findByCodeAndActiveTrue("DISCOUNT10");
        assertTrue(found.isPresent());
        assertEquals("10% Off", found.get().getName());

        Optional<Coupon> notFound = couponRepository.findByCodeAndActiveTrue("NONEXISTENT");
        assertFalse(notFound.isPresent());
    }

    @Test
    void testPromotionRepository() {
        Category category = Category.builder()
                .name("Beverages")
                .colors(List.of("#FF0000", "#00FF00"))
                .build();
        categoryRepository.save(category);

        Product product = Product.builder()
                .name("Coffee")
                .category(category)
                .price(new BigDecimal("3.50"))
                .uom("Cup")
                .taxPct(new BigDecimal("5.00"))
                .showOnKds(true)
                .build();
        productRepository.save(product);

        Promotion promo = Promotion.builder()
                .name("Happy Hour")
                .applyScope(ApplyScope.PRODUCT)
                .product(product)
                .discountType(DiscountType.FIXED)
                .value(new BigDecimal("1.00"))
                .active(true)
                .build();
        promotionRepository.save(promo);

        List<Promotion> activePromos = promotionRepository.findByActiveTrue();
        assertFalse(activePromos.isEmpty());
        assertEquals("Happy Hour", activePromos.get(0).getName());
    }

    @Test
    void testTableAndQRTokenRepository() {
        Floor floor = Floor.builder().name("Main Floor").build();
        floorRepository.save(floor);

        Table table = Table.builder()
                .floor(floor)
                .number(5)
                .seats(4)
                .active(true)
                .build();
        tableRepository.save(table);

        TableQrToken qrToken = TableQrToken.builder()
                .table(table)
                .token("secret-token-123")
                .build();
        tableQrTokenRepository.save(qrToken);

        Optional<TableQrToken> found = tableQrTokenRepository.findByToken("secret-token-123");
        assertTrue(found.isPresent());
        assertEquals(5, found.get().getTable().getNumber());
    }

    @Test
    void testOrderRepository() {
        User user = User.builder()
                .name("Cashier")
                .email("cashier@cafepos.com")
                .passwordHash("pwd")
                .role(UserRole.EMPLOYEE)
                .status(UserStatus.ACTIVE)
                .build();
        userRepository.save(user);

        PosSession session = PosSession.builder()
                .employee(user)
                .openedAt(LocalDateTime.now())
                .build();
        posSessionRepository.save(session);

        Floor floor = Floor.builder().name("Second Floor").build();
        floorRepository.save(floor);

        Table table = Table.builder()
                .floor(floor)
                .number(12)
                .seats(2)
                .active(true)
                .build();
        tableRepository.save(table);

        Order order = Order.builder()
                .session(session)
                .table(table)
                .orderNumber(1001)
                .status(OrderStatus.DRAFT)
                .source(OrderSource.POS)
                .subtotal(new BigDecimal("15.00"))
                .taxTotal(new BigDecimal("1.50"))
                .discountTotal(BigDecimal.ZERO)
                .total(new BigDecimal("16.50"))
                .build();
        orderRepository.save(order);

        List<Order> sessionOrders = orderRepository.findBySessionId(session.getId());
        assertEquals(1, sessionOrders.size());

        List<Order> tableOrders = orderRepository.findByTableIdAndStatus(table.getId(), OrderStatus.DRAFT);
        assertEquals(1, tableOrders.size());
    }
}
