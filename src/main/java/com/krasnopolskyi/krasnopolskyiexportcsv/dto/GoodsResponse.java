package com.krasnopolskyi.krasnopolskyiexportcsv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoodsResponse {
    private Integer rowInserted;
    private String message;
}
