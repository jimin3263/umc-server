package com.example.demo.src.chat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;

@Setter
@Getter
@AllArgsConstructor
public class GetChatListRes {
    private String imgUrl;
    private String nickname;
    private int userIdx;
    private int roomIdx;
    private String message;
    private Timestamp createAt;
}
