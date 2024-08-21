package com.wefood.front.product.service;

import com.wefood.front.global.PageRequest;
import com.wefood.front.product.adaptor.ProductAdaptor;
import com.wefood.front.product.dto.ProductDetailResponse;
import com.wefood.front.product.dto.ImageResponse;
import com.wefood.front.product.dto.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ProductService {

    private final ProductAdaptor productAdaptor;

    public ProductService(ProductAdaptor productAdaptor) {
        this.productAdaptor = productAdaptor;
    }

    public ProductDetailResponse getProductDetail(Long productId) {
        ProductDetailResponse productDetail = productAdaptor.getProductDetail(productId).getData();
        productDetail.getProductImg().sort(Comparator.comparingInt(ImageResponse::getSequence).thenComparing(ImageResponse::getIsThumbnail));
        productDetail.getFarmImg().sort(Comparator.comparingInt(ImageResponse::getSequence).thenComparing(ImageResponse::getIsThumbnail));
        return productDetail;
    }

    public List<ProductResponse> getProducts() {
        return productAdaptor.getProducts().getData();
    }

    public PageRequest<ProductResponse> getProductsByCategory(Long categoryId, Long page, Long size) {
        return productAdaptor.getProductsByCategory(categoryId, page, size).getData();
    }

    public PageRequest<ProductResponse> getProductsBySearch(String searchWord, Long page, Long size) {
        return productAdaptor.getProductsBySearch(searchWord, page, size).getData();
    }
}
