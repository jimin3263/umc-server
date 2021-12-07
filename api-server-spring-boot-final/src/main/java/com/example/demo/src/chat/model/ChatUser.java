package com.example.demo.src.chat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ChatUser {
    private int sellerIdx;
    private int buyerIdx;
}
