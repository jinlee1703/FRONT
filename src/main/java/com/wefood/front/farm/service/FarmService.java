package com.wefood.front.farm.service;

import com.wefood.front.farm.adaptor.FarmAdaptor;
import com.wefood.front.farm.dto.FarmListResponse;
import com.wefood.front.global.PageRequest;
import com.wefood.front.product.dto.ProductResponse;
import com.wefood.front.user.dto.response.FarmResponse;
import org.springframework.stereotype.Service;

@Service
public class FarmService {

    private final FarmAdaptor farmAdaptor;

    public FarmService(FarmAdaptor farmAdaptor) {
        this.farmAdaptor = farmAdaptor;
    }

    public PageRequest<FarmListResponse> getFarms(Long page, Long size) {
        return farmAdaptor.getFarms(page, size).getData();
    }

    public FarmResponse getFarm(Long id) {
        return farmAdaptor.getFarm(id).getData();
    }

    public PageRequest<ProductResponse> getProductsByFarm(Long id, Long page, Long size) {
        return farmAdaptor.getProductsByFarm(id, page, size).getData();
    }
}
