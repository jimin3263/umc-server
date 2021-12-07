package com.example.demo.src.chat.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostChatReq {
    private String message;
    private int userIdx;
}
