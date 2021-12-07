package com.example.demo.src.chat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
@AllArgsConstructor
public class ChatContent {
    private int userIdx;
    private String message;
    private Timestamp createAt;
}
