package com.wlz.asset.dto.req;

import lombok.Data;

@Data
public class EsPhonedReq {

    String id;
    String name;
    Integer price;
    String brand;

    Integer minPrice;
    Integer maxPrice;

}
