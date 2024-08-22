package com.wefood.front.farm.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FarmListResponse {

    private Long id;
    private String name;
    private String img;

    public void setImg(String img) {
        this.img = img;
    }
}
