package com.wefood.front.order.controller;

import com.wefood.front.global.Message;
import com.wefood.front.order.adaptor.OrderAdaptor;
import com.wefood.front.order.dto.ReviewGetResponse;
import com.wefood.front.order.dto.request.ReviewCreateRequest;
import com.wefood.front.order.dto.response.OrderDetailGetResponse;
import com.wefood.front.product.adaptor.ProductAdaptor;
import com.wefood.front.product.dto.ProductDetailResponse;
import com.wefood.front.product.dto.ProductImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderAdaptor orderAdaptor;

    private final ProductAdaptor productAdaptor;

    @GetMapping("/review-list")
    public String reviewList(@CookieValue Long id, Model model) {
        model.addAttribute("reviewList", orderAdaptor.findOrderReviewList(id));
        return "review-list";
    }

    @GetMapping("/review")
    public String review(@RequestParam Long reviewId, Model model) {

        model.addAttribute("review", orderAdaptor.findOrderReview(reviewId));

        return "review";
    }

    @PostMapping("/review-create")
    public String createReview(@CookieValue Long id, Model model, ReviewCreateRequest createRequest, @RequestParam(required = false) Long orderDetailId) {

        orderAdaptor.createReview(createRequest, orderDetailId);
        model.addAttribute("reviewList", orderAdaptor.findOrderReviewList(id));
        return "redirect:/review-list";
    }

    @GetMapping("/order-list")
    public String orderList(@CookieValue Long id, Model model) {
        model.addAttribute("orderList", orderAdaptor.findOrderList(id));
        return "order-list";
    }

    @GetMapping("/order")
    public String order(@RequestParam Long id, Model model) {

        List<OrderDetailGetResponse> orderDetailGetResponseList = orderAdaptor.findOrder(id);

        model.addAttribute("orderDetailList", orderDetailGetResponseList);

        List<ProductDetailResponse> productDetailResponseList = new ArrayList<>();
        List<ProductImageResponse> productImageResponseList = new ArrayList<>();
        for (OrderDetailGetResponse orderDetailGetResponse : orderDetailGetResponseList) {

            ProductDetailResponse productDetailResponse = productAdaptor.getProductDetail(orderDetailGetResponse.productId()).getData();
            productDetailResponseList.add(productDetailResponse);

            List<ProductImageResponse> img = productDetailResponse.getImg();
            for (ProductImageResponse productImageResponse : img) {
                if (productImageResponse.getIsThumbnail()) {
                    productImageResponseList.add(productImageResponse);
                    break;
                }
            }

        }


        model.addAttribute("productDetailList", productDetailResponseList);
        model.addAttribute("productImageResponseList", productImageResponseList);
        return "order";
    }


}
