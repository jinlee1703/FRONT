package com.wefood.front.farm.controller;

import com.wefood.front.farm.dto.FarmListResponse;
import com.wefood.front.farm.service.FarmService;
import com.wefood.front.global.PageRequest;
import com.wefood.front.product.dto.ImageResponse;
import com.wefood.front.product.dto.ProductResponse;
import com.wefood.front.user.adaptor.UserAdaptor;
import com.wefood.front.user.dto.response.FarmResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/farms")
public class FarmController {

    private final FarmService farmService;
    private final UserAdaptor userAdaptor;

    public FarmController(FarmService farmService, UserAdaptor userAdaptor) {
        this.farmService = farmService;
        this.userAdaptor = userAdaptor;
    }

    @GetMapping
    public String getFarms(Model model, @RequestParam(name = "page", required = false) Long page, @RequestParam(name = "size", required = false) Long size) {
        PageRequest<FarmListResponse> farms = farmService.getFarms(page, size);
        model.addAttribute("farms", farms);
        model.addAttribute("page", page);
        return "/farms";
    }

    @GetMapping("/{id}")
    public String getFarm(@PathVariable("id") Long id, Model model, @CookieValue(name = "isSeller", required = false) Boolean isSeller, @RequestParam(name = "page", required = false) Long page, @RequestParam(name = "size", required = false) Long size) {
        FarmResponse farm = farmService.getFarm(id);
        List<ImageResponse> farmImage = userAdaptor.getFarmImage(id);

        model.addAttribute("farm", farm);
        model.addAttribute("farmImage", farmImage);
        model.addAttribute("isSeller", isSeller);
        model.addAttribute("page", page);

        // get product by farmId
        PageRequest<ProductResponse> products = farmService.getProductsByFarm(id, page, size);
        model.addAttribute("products", products);

        return "/farm-detail";
    }
}
