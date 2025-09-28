package com.ntp.sasin_be.services;

import com.ntp.sasin_be.auth.repositories.UserDetailRepository;
import com.ntp.sasin_be.dto.CartDTO;
import com.ntp.sasin_be.entities.Cart;
import com.ntp.sasin_be.entities.CartItem;
import com.ntp.sasin_be.entities.Product;
import com.ntp.sasin_be.enums.CartStatus;
import com.ntp.sasin_be.exceptions.ResourceNotFoundException;
import com.ntp.sasin_be.mapper.CartMapper;
import com.ntp.sasin_be.repositories.CartItemRepository;
import com.ntp.sasin_be.repositories.CartRepository;
import com.ntp.sasin_be.repositories.ProductRepository;
import com.ntp.sasin_be.services.impl.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserDetailRepository userDetailRepository;

    private final CartMapper cartMapper;

    @Override
    @Transactional
    public CartDTO getActiveCartForUser(Long userId) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(userDetailRepository.findById(userId).get());
                    newCart.setStatus(CartStatus.ACTIVE);
                    newCart.setTotalPrice(BigDecimal.ZERO);

                    return cartRepository.save(newCart);
                });

        return cartMapper.toCartDto(cart);
    }

    @Override
    @Transactional
    public CartDTO addToCart(Long userId, Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Cart cart = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(userDetailRepository.findById(userId).get());
                    newCart.setStatus(CartStatus.ACTIVE);
                    newCart.setTotalPrice(BigDecimal.ZERO);

                    return cartRepository.save(newCart);
                });

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseGet(() -> {
                    CartItem newCartItem = new CartItem();
                    newCartItem.setCart(cart);
                    newCartItem.setProduct(product);
                    newCartItem.setQuantity(0);
                    newCartItem.setUnitPrice(product.getPrice());

                    return newCartItem;
                });

        cartItem.setQuantity(Math.max(1, cartItem.getQuantity() + quantity));
        cartItem.setUnitPrice(product.getPrice());
        cartItemRepository.save(cartItem);

        List<CartItem> all = cartItemRepository.getAllByCartId(cart.getId());

        BigDecimal total = all.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalPrice(total);
        cartRepository.save(cart);

        return cartMapper.toCartDto(cart);
    }


    @Override
    @Transactional
    public CartDTO updateQuantity(Long cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found: " + cartItemId));

        if (quantity <= 0) {
            Cart cart = cartItem.getCart();
            cartItemRepository.delete(cartItem);

            List<CartItem> all = cartItemRepository.getAllByCartId(cart.getId());

            BigDecimal total = all.stream()
                    .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            cart.setTotalPrice(total);
            cartRepository.save(cart);

            return cartMapper.toCartDto(cart);
        } else {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);

            Cart cart = cartItem.getCart();

            List<CartItem> all = cartItemRepository.getAllByCartId(cart.getId());

            BigDecimal total = all.stream()
                    .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            cart.setTotalPrice(total);
            cartRepository.save(cart);

            return cartMapper.toCartDto(cart);
        }
    }

    @Override
    @Transactional
    public CartDTO removeCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found: " + cartItemId));

        Cart cart = cartItem.getCart();
        cartItemRepository.delete(cartItem);

        // Tính lại tổng giá
        List<CartItem> all = cartItemRepository.getAllByCartId(cart.getId());
        BigDecimal total = all.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalPrice(total);
        cartRepository.save(cart);

        return cartMapper.toCartDto(cart);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user " + userId));

        cartItemRepository.getAllByCartId(cart.getId()).forEach(cartItem -> cartItemRepository.delete(cartItem));
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);
    }
}
