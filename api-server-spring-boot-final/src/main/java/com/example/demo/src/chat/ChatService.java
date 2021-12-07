package com.example.demo.src.chat;

import com.example.demo.config.BaseException;
import com.example.demo.src.chat.model.*;
import com.example.demo.src.product.ProductDao;
import com.example.demo.src.product.model.GetProductsRes;
import com.example.demo.src.product.model.PostProductReq;
import com.example.demo.src.product.model.PostProductRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatDao chatDao;

    //채팅 보내기
    public PostChatRes createChat(PostChatReq postChatReq, int roomIdx) throws BaseException{
        ChatUser chatUser = chatDao.checkUser(roomIdx);

        //채팅방에 해당하는 유저가 아니라면
        if(chatUser.getBuyerIdx() != postChatReq.getUserIdx() && chatUser.getSellerIdx() != postChatReq.getUserIdx()){
            throw new BaseException(POST_CHAT_NOT_USER);
        }
        try {
            int chat = chatDao.createChat(postChatReq,roomIdx);
            return new PostChatRes(chat);

        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //채팅방 생성
    public PostChatRoomRes createChatRoom(int userIdx, int productIdx) throws BaseException{
        if (chatDao.checkSeller(productIdx)==0){
            throw new BaseException(NOT_MATCH_PRODUCT);
        }
        try {
            int chatRoom = chatDao.createChatRoom(userIdx, productIdx);
            return new PostChatRoomRes(chatRoom);
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
