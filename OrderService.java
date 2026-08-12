package com.ecommerce.service;

import com.ecommerce.model.CartItem;
import com.ecommerce.model.Order;
import com.ecommerce.model.Product;
import com.ecommerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    public Order checkout(Long buyerId) {
        List<CartItem> cartItems = cartService.getCartItems(buyerId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty!");
        }

        List<Product> products = new ArrayList<>();
        for (CartItem item : cartItems) {
            for (int i = 0; i < item.getQuantity(); i++) {
                products.add(item.getProduct());
            }
        }

        Double totalPrice = cartService.calculateTotal(buyerId);
        Order order = new Order(buyerId, products, totalPrice, "Confirmed / Pending");
        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(buyerId);
        return savedOrder;
    }

    public List<Order> getBuyerOrders(Long buyerId) {
        return orderRepository.findByBuyerId(buyerId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
