package com.example.demo.src.chat;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.chat.model.GetChatListRes;
import com.example.demo.src.chat.model.GetChatRes;
import com.example.demo.src.chat.model.PostChatReq;
import com.example.demo.src.chat.model.PostChatRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.POST_CHAT_EMPTY;


@RestController
@RequestMapping("/app/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatProvider chatProvider;
    private final ChatService chatService;
    private final JwtService jwtService;

    //채팅방 내용 조회
    @GetMapping("/{roomIdx}")
    public BaseResponse<GetChatRes> getProduct(@PathVariable("roomIdx") int roomIdx){
        try {
            GetChatRes chat = chatProvider.getchat(roomIdx);
            return new BaseResponse<>(chat);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @PostMapping("/{roomId}")
    public BaseResponse<PostChatRes> createChat(@RequestBody PostChatReq postChatReq, @PathVariable("roomId") int roomId){
        if(postChatReq.getMessage() == null){
            return new BaseResponse<>(POST_CHAT_EMPTY);
        }
        try {
            PostChatRes chat = chatService.createChat(postChatReq, roomId);
            return new BaseResponse<>(chat);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @GetMapping()
    public BaseResponse<List<GetChatListRes>> chatList(){
        try {
            int userIdx= jwtService.getUserIdx();
            List<GetChatListRes> chatList = chatProvider.getChatList(userIdx);
            return new BaseResponse<>(chatList);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


}
