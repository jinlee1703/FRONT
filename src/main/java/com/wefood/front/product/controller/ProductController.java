package com.wefood.front.product.controller;

import com.wefood.front.global.PageRequest;
import com.wefood.front.order.adaptor.OrderAdaptor;
import com.wefood.front.order.dto.ReviewGetResponse;
import com.wefood.front.product.dto.ProductDetailResponse;
import com.wefood.front.product.dto.ProductImageResponse;
import com.wefood.front.product.dto.ProductResponse;
import com.wefood.front.product.service.ProductService;
import com.wefood.front.user.adaptor.UserAdaptor;
import com.wefood.front.user.dto.response.AddressResponse;
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

    private final UserAdaptor userAdaptor;

    private final OrderAdaptor orderAdaptor;

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
    public String productDetail(@PathVariable(name = "productId") Long productId, Model model, @CookieValue(name = "name", required = false) String name, @CookieValue(name = "phoneNumber", required = false) String phoneNumber, @CookieValue(name = "id", required = false) Long id) {
        List<String> img = new ArrayList<>();
        ProductDetailResponse productDetail = productService.getProductDetail(productId);
        // sequence대로 정렬
        productDetail.getImg().sort(Comparator.comparingInt(ProductImageResponse::getSequence));


        List<ReviewGetResponse> list = orderAdaptor.findProductReview(productId);

        for (ProductImageResponse image : productDetail.getImg()) {
            if (image.getIsThumbnail()) {
                model.addAttribute("thumbnail", image.getImg());
            } else {
                img.add(image.getImg());
            }
        }
        model.addAttribute("img", img);
        model.addAttribute("product", productDetail);
        model.addAttribute("reviewList", list);


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
