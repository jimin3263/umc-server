package com.example.demo.src.product.model;

import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostProductReq {
    private List<String> imgUrl;
    private int userIdx;
    private String title;
    private int categoryIdx;
    private int price;
    private String content;
}
