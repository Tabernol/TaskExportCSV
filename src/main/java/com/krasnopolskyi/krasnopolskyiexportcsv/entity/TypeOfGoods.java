package com.krasnopolskyi.krasnopolskyiexportcsv.entity;

public enum TypeOfGoods {
    WEIGHT(0),
    QUANTITY(1);

    public final int number;

    TypeOfGoods(int number) {
        this.number = number;
    }

    public static TypeOfGoods getTypeByNumber(int number) {
        for (TypeOfGoods type : TypeOfGoods.values()) {
            if (type.number == number) {
                return type;
            }
        }
        return null;
    }
}
