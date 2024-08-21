package com.wefood.front.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "wefood.market.price")
public class MarketPriceProperties {

    private String url;

    private String key;

    private String id;
}
