package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class GetProductRes{
    private int productIdx;
    private int userIdx;
    private String userName;
    private String title;
    private String content;
    private String state;
    private List<String> imgUrl;
    private String region;
    private int price;
    private int updateCount;
    private int roomCnt;
    private int likeCnt;
    private String updateAt;
}

