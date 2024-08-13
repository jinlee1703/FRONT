package com.wefood.front.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "wefood.config.back")
public class BackAdaptorProperties {

    private String address;

}
