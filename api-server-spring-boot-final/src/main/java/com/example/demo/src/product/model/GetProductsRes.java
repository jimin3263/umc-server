package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class GetProductsRes {
    private int productIdx;
    private String category;
    private String title;
    private String state;
    private List<String> imgUrl;
    private String region;
    private int price;
    private int updateCount;
    private int roomCnt;
    private int likeCnt;
    private String updateDate;
}
