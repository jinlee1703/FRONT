package com.wefood.front.product.controller;

import com.wefood.front.global.PageRequest;
import com.wefood.front.product.dto.ProductDetailResponse;
import com.wefood.front.product.dto.ProductImageResponse;
import com.wefood.front.product.dto.ProductResponse;
import com.wefood.front.product.service.ProductService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String index(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies.length > 1) {
            model.addAttribute("login", "Y");
        } else {
            model.addAttribute("login", "N");
        }
        List<ProductResponse> products = productService.getProducts();
        model.addAttribute("products", products);
        return "/index";
    }

    @GetMapping("/search/category/{categoryId}")
    public String searchCategory(@PathVariable(name = "categoryId") Long categoryId, @RequestParam(defaultValue = "0") Long page, @RequestParam(defaultValue = "10") Long size, Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies.length > 1) {
            model.addAttribute("login", "Y");
        } else {
            model.addAttribute("login", "N");
        }

        PageRequest<ProductResponse> productsByCategory = productService.getProductsByCategory(categoryId, page, size);
        model.addAttribute("products", productsByCategory);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("page", page);
        System.out.println(page);
        return "/shop";
    }

    @GetMapping("/{productId}")
    public String productDetail(@PathVariable(name = "productId") Long productId, Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies.length > 1) {
            model.addAttribute("login", "Y");
        } else {
            model.addAttribute("login", "N");
        }

        ProductDetailResponse productDetail = productService.getProductDetail(productId);
        List<String> img = new ArrayList<>();
        for (ProductImageResponse image : productDetail.getImg()) {
            if (image.getIsThumbnail()) {
                model.addAttribute("thumbnail", image.getName());
            } else {
                img.add(image.getName());
            }
        }
        model.addAttribute("img", img);
        model.addAttribute("product", productDetail);
        return "/product-single";
    }

    @GetMapping("/search")
    public String search(@ModelAttribute(name = "searchWord") String searchWord, @RequestParam(defaultValue = "0") Long page, @RequestParam(defaultValue = "10") Long size) {
        productService.getProductsBySearch(searchWord, page, size);
        return "/shop";
    }

    @GetMapping("/cart")
    public String cart() {
        return "/cart";
    }
}
