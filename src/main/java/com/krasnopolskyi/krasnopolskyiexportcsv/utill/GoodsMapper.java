package com.krasnopolskyi.krasnopolskyiexportcsv.utill;

import com.krasnopolskyi.krasnopolskyiexportcsv.entity.Goods;
import com.krasnopolskyi.krasnopolskyiexportcsv.entity.TypeOfGoods;

import java.math.BigDecimal;

public class GoodsMapper {
    public static Goods map(String[] strings) {
        return Goods.builder()
                .id(Integer.valueOf(strings[0]))
                .name(strings[1])
                .price(BigDecimal.valueOf(Double.parseDouble(strings[2])))
                .typeOfGoods(TypeOfGoods.getTypeByNumber(Integer.parseInt(strings[3])))
                .build();
    }
}
