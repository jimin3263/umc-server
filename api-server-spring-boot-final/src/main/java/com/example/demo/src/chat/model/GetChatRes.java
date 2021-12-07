package com.example.demo.src.chat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class GetChatRes {
    private String title;
    private String productStatus;
    private String category;
    private int price;
    private String nickname;
    private float mannerTemp;
    private List<ChatContent> chat;
}
