package com.allar.market.domain.cart.repository;

import com.allar.market.domain.cart.domain.Cart;
import com.allar.market.domain.customer.domain.Customer;
import com.allar.market.domain.customer.repository.CustomerRepository;
import com.allar.market.domain.product.domain.Product;
import com.allar.market.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CartRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartRepository cartRepository;

    @Test
    @DisplayName("카트 자체생성 테스트")
    void findAllCart() {
        // given
        Customer customer = Customer.builder().email("aaa@aaa.com").password("aa").build();
        Cart cart = Cart.builder()
                .customer(customer)
                .build();
        Product product = Product.builder().name("테스트 상품1")
                .price(BigDecimal.valueOf(1000))
                .quantity(10)
                .build();
        // when
        Cart savedCart = cartRepository.save(cart);
        Optional<Cart> foundCart = cartRepository.findById(savedCart.getId());

        // then
        assertTrue(foundCart.isPresent());
        assertEquals(savedCart.getId(), foundCart.get().getId());
    }

    @Test
    @DisplayName("기존에 있던 상품 카트에 담기 테스트")
    void addExistItem() {
        // given
        Customer customer = Customer.builder().email("aaa@aaa.com").password("aa").build();
        Product product = Product.builder().name("테스트 상품1")
                .price(BigDecimal.valueOf(1000))
                .quantity(10)
                .build();
        customerRepository.save(customer);
        productRepository.save(product);

        List<Product> products = productRepository.findAll();
        Product foundProduct = products.stream().findFirst().get();
        Cart cart = customer.getCart();

        // when
        // addItem 메서드 있는 아이템 추가시 더하는걸로 변경 total price 8000원이 정상
        cart.addItem(foundProduct, 5); // 5개 카트에 담기
        cart.addItem(foundProduct, 3); // 3개 더 카트에 담기

        // then
        assertEquals(BigDecimal.valueOf(8000), cart.getTotalPrice()); // 5000+3000
        assertEquals(1, cart.getItems().size()); // 1개 유지
    }

    @Test
    @DisplayName("카트 아이템 수량 변경")
    void updateCartItemQuantity() {
        // given
        Customer customer = Customer.builder().email("aaa@aaa.com").password("aa").build();
        Product product = Product.builder().name("테스트 상품1")
                .price(BigDecimal.valueOf(1000))
                .quantity(100) // 100개 수량 product
                .build();
        customerRepository.save(customer);
        productRepository.save(product);

        List<Product> products = productRepository.findAll();
        Product foundProduct = products.stream().findFirst().get();
        Cart cart = customer.getCart();

        // when
        cart.addItem(foundProduct, 1); // 1개 담기
        cart.updateCartItemQuantity(foundProduct.getId(), 100); // 100개로 변경

        // then
        assertEquals(100, cart.findItem(foundProduct.getId()).getQuantity());
    }

    @Test
    @DisplayName("오류 - 카트 아이템 수량 초과 변경")
    void updateCartItemQuantityException() {
        // given
        Customer customer = Customer.builder().email("aaa@aaa.com").password("aa").build();
        Product product = Product.builder().name("테스트 상품1")
                .price(BigDecimal.valueOf(1000))
                .quantity(100) // 100개 수량 product
                .build();
        customerRepository.save(customer);
        productRepository.save(product);

        List<Product> products = productRepository.findAll();
        Product foundProduct = products.stream().findFirst().get();
        Cart cart = customer.getCart();

        // when
        cart.addItem(foundProduct, 1);

        // then
        assertThrows(IllegalArgumentException.class,
                () -> cart.updateCartItemQuantity(foundProduct.getId(), 200)); // 200개 수량 담기 - 오류
    }

    @Test
    @DisplayName("장바구니 아이템 삭제")
    void removeItem(){
        // given
        Customer customer = Customer.builder().email("aaa@aaa.com").password("aa").build();
        Product product = Product.builder().name("테스트 상품1")
                .price(BigDecimal.valueOf(1000))
                .quantity(100) // 100개 수량 product
                .build();
        customerRepository.save(customer);
        productRepository.save(product);

        List<Product> products = productRepository.findAll();
        Product foundProduct = products.stream().findFirst().get();
        Cart cart = customer.getCart();

        // when
        Long foundProductId = foundProduct.getId();
        cart.addItem(foundProduct, 10);
        cart.removeItem(foundProductId);

        //then
        assertThrows(IllegalArgumentException.class,
                () -> cart.findItem(foundProductId), "카트에 해당 상품이 없습니다.");
    }

    @Test
    @DisplayName("오류 - 없는 아이템 장바구니 삭제 요청")
    void removeItemException(){
        // given, when
        Customer customer = Customer.builder().email("aaa@aaa.com").password("aa").build();
        customerRepository.save(customer);
        Cart cart = customer.getCart();

        // then
        assertThrows(IllegalArgumentException.class, () -> cart.removeItem(999L));
    }
}