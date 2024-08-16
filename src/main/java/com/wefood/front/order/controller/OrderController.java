package com.wefood.front.order.controller;

import com.wefood.front.order.adaptor.OrderAdaptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderAdaptor orderAdaptor;


    @GetMapping("/order-list")
    public String orderList(@CookieValue Long id, Model model) {
        model.addAttribute("orderList", orderAdaptor.findOrderList(id));
        return "orderList";
    }

    @GetMapping("/order")
    public String order(@RequestParam Long id,Model model){
        model.addAttribute("orderDetailList", orderAdaptor.findOrder(id));
        return "order";
    }


}
