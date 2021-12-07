package com.example.demo.src.product.model;


import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PatchProductReq {
    //수정할 내용 -> 상태, 사진, 타이틀, 카테고리, 가격, 내용
    private String status;
    private List<String> imgUrl;
    private String title;
    private int categoryIdx;
    private int price;
    private String content;
}
