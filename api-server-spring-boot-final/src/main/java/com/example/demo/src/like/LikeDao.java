package com.example.demo.src.like;

import com.example.demo.src.like.model.GetLikeRes;
import com.example.demo.src.like.model.PostLikeReq;
import com.example.demo.src.product.model.GetProductRes;
import com.example.demo.src.product.model.GetProductsRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

@Repository
public class LikeDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //좋아요
    public int createLike(int productIdx, PostLikeReq postLikeReq){
        String createLikeQuery = "insert into `Like` (userIdx, productIdx) VALUES (?,?)";
        Object[] createLikeParams = new Object[]{productIdx, postLikeReq.getUserIdx()};
        this.jdbcTemplate.update(createLikeQuery, createLikeParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    //좋아요 삭제
    public void deleteLike(int productIdx, PostLikeReq postLikeReq){
        String deleteLikeQuery = "delete from `Like` where productIdx = ? and userIdx = ?";
        Object[] deleteLikeParams = new Object[]{productIdx, postLikeReq.getUserIdx()};
        this.jdbcTemplate.update(deleteLikeQuery, deleteLikeParams);
    }

    //좋아요 상품 조회
    public List<GetProductsRes> getLikeProducts(int userIdx) {
        //이미지 가져오기

        return this.jdbcTemplate.query("select t.productIdx, t.categoryName, t.title, t.productStatus, t.imgUrl, t.name, t.price, t.updateCount, t.roomCnt, t.likeCnt, t.updateDate\n" +
                        "from\n" +
                        "              (select p.productIdx as productIdx, C.name as categoryName, p.title, productStatus, IFNULL(GROUP_CONCAT(I.imgUrl),'') as imgUrl,\n" +
                        "       Region.name,p.price,p.updateCount,  IFNULL(roomCnt, 0) as roomCnt, IFNULL(likeCnt, 0) as likeCnt, p.updateAt, User.userIdx,\n" +
                        "       case when TIMESTAMPDIFF(MINUTE,p.updateAt,now()) < 60 then concat(TIMESTAMPDIFF(MINUTE,p.updateAt,now()), '분 전')\n" +
                        "            when TIMESTAMPDIFF(HOUR, p.updateAt, now()) < 24 then concat(TIMESTAMPDIFF(HOUR, p.updateAt,now()), '시간 전')\n" +
                        "            when TIMESTAMPDIFF(DAY, p.updateAt,now()) >= 1 then concat (TIMESTAMPDIFF(DAY, p.updateAt,now()), '일 전')\n" +
                        "           else DATE_FORMAT(p.updateAt, '%Y년 %m월 %d일') end as updateDate\n" +
                        "from Product p\n" +
                        "    inner join User on p.userIdx = User.userIdx\n" +
                        "    inner join Region on User.userIdx = Region.userIdx\n" +
                        "    inner join Category C on p.categoryIdx = C.categoryIdx\n" +
                        "    left outer join (select count(productIdx) as roomCnt, productIdx from Room group by productIdx) RoomCnt\n" +
                        "        on p.productIdx = RoomCnt.productIdx\n" +
                        "    left outer join (select count(productIdx) as likeCnt, productIdx from `Like` group by productIdx) likeCnt\n" +
                        "        on p.productIdx = likeCnt.productIdx\n" +
                        "     left outer join Image I on p.productIdx = I.productIdx\n" +
                        "GROUP BY p.productIdx\n" +
                        "\n" +
                        "    ) t\n" +
                        "inner join `Like` L on t.productIdx = L.productIdx\n" +
                        "WHERE L.userIdx = ?\n" +
                        "ORDER BY t.updateAt DESC;",
                (rs, rowNum) -> {
                    return new GetProductsRes(
                            rs.getInt("productIdx"),
                            rs.getString("categoryName"),
                            rs.getString("title"),
                            rs.getString("productStatus"),
                            Arrays.asList(rs.getString("imgUrl").split(",")),
                            rs.getString("name"),
                            rs.getInt("price"),
                            rs.getInt("updateCount"),
                            rs.getInt("roomCnt"),
                            rs.getInt("likeCnt"),
                            rs.getString("updateDate")
                    );
                }, userIdx);
    }

}
