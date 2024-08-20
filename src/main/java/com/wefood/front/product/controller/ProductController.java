package com.wefood.front.product.controller;

import com.wefood.front.global.PageRequest;
import com.wefood.front.product.dto.*;
import com.wefood.front.product.service.MarketPriceService;
import com.wefood.front.product.service.ProductService;
import com.wefood.front.user.adaptor.UserAdaptor;
import com.wefood.front.user.dto.response.AddressResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final MarketPriceService marketPriceService;

    private final UserAdaptor userAdaptor;

    @GetMapping
    public String index(Model model) {
        List<ProductResponse> products = productService.getProducts();
        model.addAttribute("products", products);
        return "/index";
    }

    @GetMapping("/search/category/{categoryId}")
    public String searchCategory(@PathVariable(name = "categoryId") Long categoryId, @RequestParam(defaultValue = "0") Long page, @RequestParam(defaultValue = "10") Long size, Model model) {
        PageRequest<ProductResponse> productsByCategory = productService.getProductsByCategory(categoryId, page, size);
        model.addAttribute("products", productsByCategory);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("page", page);

        return "/shop";
    }

    @GetMapping("/{productId}")
    public String productDetail(@PathVariable(name = "productId") Long productId, Model model, @CookieValue(name = "name", required = false) String name, @CookieValue(name = "phoneNumber", required = false) String phoneNumber, @CookieValue(name = "id", required = false) Long id, HttpServletRequest request, HttpServletResponse response, @CookieValue(name = "price0", required = false) String item) {
        List<String> img = new ArrayList<>();
        ProductDetailResponse productDetail = productService.getProductDetail(productId);

        // sequence대로 정렬
        productDetail.getImg().sort(Comparator.comparingInt(ProductImageResponse::getSequence));
        for (ProductImageResponse image : productDetail.getImg()) {
            if (image.getIsThumbnail()) {
                model.addAttribute("thumbnail", image.getImg());
            } else {
                img.add(image.getImg());
            }
        }

        if (item == null) {
            marketPriceService.saveMarketPrice(response, "price");
        }
        List<MarketPriceItemResponse> marketPrice = marketPriceService.getMarketPriceCookie(productDetail.getItemId(), request.getCookies());
        model.addAttribute("marketPrices", marketPrice);


        model.addAttribute("img", img);
        model.addAttribute("product", productDetail);

        if (name != null) {
            model.addAttribute("userName", name);
            model.addAttribute("phoneNumber", phoneNumber);

            AddressResponse addressResponse = userAdaptor.findAddress(id);
            model.addAttribute("addressResponse", addressResponse);
        }
        return "/product-single";
    }

    @GetMapping("/search")
    public String search(@ModelAttribute(name = "search_word") String searchWord, Model model, @RequestParam(defaultValue = "0") Long page, @RequestParam(defaultValue = "10") Long size) {
        PageRequest<ProductResponse> productsBySearch = productService.getProductsBySearch(searchWord, page, size);

        model.addAttribute("search", searchWord);
        model.addAttribute("products", productsBySearch);
        model.addAttribute("page", page);
        return "/shop";
    }
}
