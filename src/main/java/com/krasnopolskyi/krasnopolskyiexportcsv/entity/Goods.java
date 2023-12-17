package com.krasnopolskyi.krasnopolskyiexportcsv.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class Goods {
    private Integer id;
    private String name;
    private BigDecimal price;
    private TypeOfGoods typeOfGoods;
}


