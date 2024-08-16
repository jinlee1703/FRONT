package com.wefood.front.order.service;

import com.wefood.front.order.adaptor.CartAdaptor;
import com.wefood.front.order.dto.CartProductRequest;
import com.wefood.front.order.dto.CartProductResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartAdaptor cartAdaptor;

    public CartService(CartAdaptor cartAdaptor) {
        this.cartAdaptor = cartAdaptor;
    }

    public void saveCart(CartProductRequest request) {
        cartAdaptor.saveCartProduct(request);
    }

    public List<CartProductResponse> getCartProductByUserId(Long userId) {
        return cartAdaptor.getCartProduct(userId).getData();
    }
}
