package com.example.demo.src.like.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetLikeRes {
    private int productIdx;
    private String category;
    private String title;
    private String productStatus;
    private List<String> imgUrl;
    private String region;
    private int price;
    private String updateDate;
}
