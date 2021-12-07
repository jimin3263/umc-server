package com.example.demo.src.chat;

import com.example.demo.src.chat.model.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@Repository
public class ChatDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    //채팅방 내용 불러오기
    public GetChatRes getChat(int chatRoom){
        List<ChatContent> chat = getChatContents(chatRoom);
        return this.jdbcTemplate.queryForObject("select Product.title, productStatus, C.name,Product.price, U.nickname, U.mannerTemp\n" +
                        "from Product\n" +
                        "inner join Room R on Product.productIdx = R.productIdx\n" +
                        "inner join Category C on Product.categoryIdx = C.categoryIdx\n" +
                        "inner join User U on Product.userIdx = U.userIdx\n" +
                        "where roomIdx=?;",
                (rs, rowNum) -> {
                    return new GetChatRes(
                            rs.getString("title"),
                            rs.getString("productStatus"),
                            rs.getString("name"),
                            rs.getInt("price"),
                            rs.getString("nickname"),
                            rs.getFloat("mannerTemp"),
                            chat
                    );
                },chatRoom);
    }

    @NotNull
    private List<ChatContent> getChatContents(int chatRoom) {
        List<ChatContent> chat = this.jdbcTemplate.query("select message,userIdx,Chat.createAt\n" +
                "from Chat\n" +
                "inner join Room R on Chat.roomIdx = R.roomIdx\n" +
                "where R.roomIdx =?\n" +
                "order by Chat.createAt;",
                (rs, rowNum) -> {
                    return new ChatContent(
                            rs.getInt("userIdx"),
                            rs.getString("message"),
                            rs.getTimestamp("createAt")
                    );
                }, chatRoom);
        return chat;
    }

    //채팅보내기
    public int createChat(PostChatReq postChatReq, int roomIdx){
        String createChatQuery = "insert into Chat(message, userIdx, roomIdx) VALUES (?,?,?)";
        Object[] createChatParam = new Object[]{postChatReq.getMessage(), postChatReq.getUserIdx(), roomIdx};
        this.jdbcTemplate.update(createChatQuery,createChatParam);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    //채팅방에 있는 회원
    public ChatUser checkUser(int roomIdx){
        return this.jdbcTemplate.queryForObject("select sellerIdx, buyerIdx\n" +
                        "        from Room\n" +
                        "        where roomIdx = ?;",
                (rs, rowNum) -> {
            return new ChatUser(
                    rs.getInt("sellerIdx"),
                    rs.getInt("buyerIdx")
            );

                },roomIdx);
    }

    //채팅방 생성
    public int createChatRoom(int buyerIdx, int productIdx){
        String getSellerQuery = "select userIdx\n" +
                "from Product\n" +
                "where productIdx =?;";
        Object[] getSellerParam = new Object[]{productIdx};
        Integer sellerIdx = jdbcTemplate.queryForObject(getSellerQuery, int.class, getSellerParam);

        String createChatQuery = "insert into Room(productIdx, buyerIdx, sellerIdx) VALUES (?,?,?)";
        Object[] createChatParam = new Object[]{productIdx, buyerIdx, sellerIdx};
        this.jdbcTemplate.update(createChatQuery,createChatParam);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int getChatRoom(int userIdx, int productIdx){
        String createChatQuery = "select roomIdx\n" +
                "from Room\n" +
                "where Room.buyerIdx =? and Room.productIdx =?;";
        Object[] getChatParam = new Object[]{userIdx, productIdx};
        return this.jdbcTemplate.queryForObject(createChatQuery,
                int.class,
                getChatParam);
    }

    //product의 판매자 idx 가져오기
    public int checkSeller(int productIdx){
        String checkProductQuery = "select userIdx from Product where productIdx = ?"; // User Table에 해당 email 값을 갖는 유저 정보가 존재하는가?
        return this.jdbcTemplate.queryForObject(checkProductQuery,
                int.class,
                productIdx);
    }

    //채팅방 목록 조회
    public List<GetChatListRes> getChatList(int userIdx){
        return this.jdbcTemplate.query("select * from(\n" +
                        "select * from (select U.imgUrl, U.nickname, U.userIdx, message, C.createAt,Room.roomIdx\n" +
                        "from Room\n" +
                        "left outer join Chat C on Room.roomIdx = C.roomIdx\n" +
                        "left outer join User U on Room.sellerIdx = U.userIdx\n" +
                        "where Room.buyerIdx =?\n" +
                        "order by C.createAt DESC) t\n" +
                        "group by t.roomIdx\n" +
                        "union\n" +
                        "select * from (select U.imgUrl, U.nickname, U.userIdx, message, C.createAt,Room.roomIdx\n" +
                        "from Room\n" +
                        "left outer join Chat C on Room.roomIdx = C.roomIdx\n" +
                        "left outer join User U on Room.buyerIdx = U.userIdx\n" +
                        "where Room.sellerIdx =?\n" +
                        "order by C.createAt DESC) t\n" +
                        "group by t.roomIdx) chatlist\n" +
                        "order by chatlist.createAt DESC ;",
                (rs, rowNum) -> {
                    return new GetChatListRes(
                            rs.getString("imgUrl"),
                            rs.getString("nickName"),
                            rs.getInt("userIdx"),
                            rs.getInt("roomIdx"),
                            rs.getString("message"),
                            rs.getTimestamp("createAt")
                    );
                },userIdx, userIdx);
    }



}
