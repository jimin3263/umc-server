package com.example.demo.src.chat;

import com.example.demo.config.BaseException;
import com.example.demo.src.chat.model.GetChatListRes;
import com.example.demo.src.chat.model.GetChatRes;
import com.example.demo.src.product.model.GetProductsRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class ChatProvider {

    private final ChatDao chatDao;

    //채팅 조회
    public GetChatRes getchat(int chatRoom) throws BaseException {
        try {
            GetChatRes chat = chatDao.getChat(chatRoom);
            return chat;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetChatListRes> getChatList(int userIdx) throws BaseException {
        try {
            List<GetChatListRes> chatList = chatDao.getChatList(userIdx);
            return chatList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
