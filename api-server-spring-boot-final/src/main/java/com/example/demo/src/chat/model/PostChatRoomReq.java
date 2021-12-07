package com.example.demo.src.chat.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostChatRoomReq {
    private int sellerIdx;
    private int buyerIdx;
}
