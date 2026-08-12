package com.ecommerce.controller;

import com.ecommerce.model.CartItem;
import com.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/{buyerId}")
    public List<CartItem> getCart(@PathVariable Long buyerId) {
        return cartService.getCartItems(buyerId);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestParam Long buyerId, @RequestParam Long productId, @RequestParam Integer quantity) {
        CartItem item = cartService.addToCart(buyerId, productId, quantity);
        return ResponseEntity.ok(item);
    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<?> updateQuantity(@PathVariable Long cartItemId, @RequestParam Integer quantity) {
        CartItem item = cartService.updateQuantity(cartItemId, quantity);
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<?> removeItem(@PathVariable Long cartItemId) {
        cartService.removeFromCart(cartItemId);
        return ResponseEntity.ok("Item removed from cart");
    }

    @GetMapping("/{buyerId}/total")
    public ResponseEntity<?> getTotal(@PathVariable Long buyerId) {
        Double total = cartService.calculateTotal(buyerId);
        return ResponseEntity.ok("Total Price: " + total);
    }
}
