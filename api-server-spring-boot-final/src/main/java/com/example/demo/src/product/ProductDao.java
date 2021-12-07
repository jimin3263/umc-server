package com.example.demo.src.product;

import com.example.demo.src.product.model.GetProductRes;
import com.example.demo.src.product.model.GetProductsRes;
import com.example.demo.src.product.model.PatchProductReq;
import com.example.demo.src.product.model.PostProductReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

@Repository
public class ProductDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Page<GetProductsRes> getProducts(Pageable pageable) {
        List<GetProductsRes> products = this.jdbcTemplate.query("select * from (select p.productIdx as productIdx, C.name as categoryName, p.title, productStatus, IFNULL(GROUP_CONCAT(I.imgUrl),'') as imgUrl,\n" +
                        "       Region.name,p.price,p.updateCount,  IFNULL(roomCnt, 0) as roomCnt, IFNULL(likeCnt, 0) as likeCnt, p.updateAt,\n" +
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
                        "\n" +
                        "    left outer join Image I on p.productIdx = I.productIdx\n" +
                        "GROUP BY p.productIdx) t\n" +
                        "ORDER BY updateAt DESC;",
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
                });
        return listToPage(products, pageable);
    }

    public Page<GetProductsRes> getProductsByRegion(String region, Pageable pageable){
        List<GetProductsRes> products =  this.jdbcTemplate.query("select * from (select User.userIdx as userIdx, p.productIdx as productIdx, C.name as categoryName, p.title, productStatus, IFNULL(GROUP_CONCAT(I.imgUrl),'') as imgUrl,\n" +
                        "       Region.name,p.price,p.updateCount,  IFNULL(roomCnt, 0) as roomCnt, IFNULL(likeCnt, 0) as likeCnt, p.updateAt,\n" +
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
                        "\n" +
                        "    left outer join Image I on p.productIdx = I.productIdx\n" +
                        "GROUP BY p.productIdx) t\n" +
                        "WHERE  t.name = ?\n" +
                        "ORDER BY updateAt DESC;",
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
                },region);
        return listToPage(products, pageable);

    }

    public List<GetProductsRes> getProductsByUser(int userIdx, String status){
        return this.jdbcTemplate.query("select * from (select User.userIdx as userIdx, p.productIdx as productIdx, C.name as categoryName, p.title, productStatus, IFNULL(GROUP_CONCAT(I.imgUrl),'') as imgUrl,\n" +
                        "       Region.name,p.price,p.updateCount,  IFNULL(roomCnt, 0) as roomCnt, IFNULL(likeCnt, 0) as likeCnt, p.updateAt,\n" +
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
                        "\n" +
                        "    left outer join Image I on p.productIdx = I.productIdx\n" +
                        "GROUP BY p.productIdx) t\n" +
                        "WHERE t.userIdx = ? and t.productStatus = ?\n" +
                        "ORDER BY updateAt DESC;",
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
                },userIdx, status);

    }

    public GetProductRes getProduct(int productId){
        return jdbcTemplate.queryForObject("select Product.productIdx as productIdx,User.userIdx,User.nickname, Product.title, Product.content, productStatus, IFNULL(GROUP_CONCAT(I.imgUrl),'') as imgUrl,\n" +
                        "       Region.name,Product.price,Product.updateCount,  IFNULL(roomCnt, 0) as roomCnt, IFNULL(likeCnt, 0) as likeCnt,\n" +
                        "       case when TIMESTAMPDIFF(MINUTE,Product.updateAt,now()) < 60 then concat(TIMESTAMPDIFF(MINUTE,Product.updateAt,now()), '분 전')\n" +
                        "            when TIMESTAMPDIFF(HOUR, Product.updateAt, now()) < 24 then concat(TIMESTAMPDIFF(HOUR, Product.updateAt,now()), '시간 전')\n" +
                        "            when TIMESTAMPDIFF(DAY, Product.updateAt,now()) >= 1 then concat (TIMESTAMPDIFF(DAY, Product.updateAt,now()), '일 전')\n" +
                        "           else DATE_FORMAT(Product.updateAt, '%Y년 %m월 %d일') end as updateDate\n" +
                        "from Product\n" +
                        "    inner join User on Product.userIdx = User.userIdx\n" +
                        "    inner join Region on User.userIdx = Region.userIdx\n" +
                        "    left outer join (select count(productIdx) as roomCnt, productIdx from Room group by productIdx) RoomCnt\n" +
                        "        on Product.productIdx = RoomCnt.productIdx\n" +
                        "    left outer join (select count(productIdx) as likeCnt, productIdx from `Like` group by productIdx) likeCnt\n" +
                        "        on Product.productIdx = likeCnt.productIdx\n" +
                        "    left outer join Image I on Product.productIdx = I.productIdx\n" +
                        "\n" +
                        "where Product.productIdx = ? \n" +
                        "GROUP BY Product.productIdx;",
                (rs, rowNum) -> {
                    return new GetProductRes(
                            rs.getInt("productIdx"),
                            rs.getInt("User.userIdx"),
                            rs.getString("User.nickname"),
                            rs.getString("Product.title"),
                            rs.getString("Product.content"),
                            rs.getString("productStatus"),
                            Arrays.asList(rs.getString("imgUrl").split(",")),
                            rs.getString("Region.name"),
                            rs.getInt("Product.price"),
                            rs.getInt("Product.updateCount"),
                            rs.getInt("roomCnt"),
                            rs.getInt("likeCnt"),
                            rs.getString("updateDate")
                    );
                }, productId);

    }

    public int createProduct(PostProductReq postProductReq){
        //상품 저장
        String createProductQuery = "insert into Product (title, categoryIdx, price, content, userIdx) VALUES (?,?,?,?,?)";
        Object[] createProductParams = new Object[]{postProductReq.getTitle(), postProductReq.getCategoryIdx(), postProductReq.getPrice(), postProductReq.getContent(), postProductReq.getUserIdx()};
        this.jdbcTemplate.update(createProductQuery, createProductParams);

        String lastInsertIdQuery = "select last_insert_id()";
        Integer productIdx = this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
        //이미지 저장
        String createImgQuery = "insert into Image (imgUrl, productIdx) VALUES (?, ?)";
        List<String> imgUrl = postProductReq.getImgUrl();

        for (String url : imgUrl) {
            Object[] createImgParams = new Object[]{url, productIdx};
            this.jdbcTemplate.update(createImgQuery, createImgParams);
        }

        return productIdx;
    }


    public int updateProduct(PatchProductReq patchProductReq, int productIdx){
        List<String> imgUrl = patchProductReq.getImgUrl();
        String modifyImgQuery = "update Image set imgUrl = ? where productIdx = ?";

        for (String url : imgUrl) {
            Object[] modifyImgParams = new Object[]{url, productIdx};
            this.jdbcTemplate.update(modifyImgQuery, modifyImgParams);
        }

        String modifyProductQuery = "update Product set productStatus = ?, title = ?, categoryIdx = ?, price = ?, content =?  where productIdx = ? ";
        Object[] modifyProductParams = new Object[]{patchProductReq.getStatus(), patchProductReq.getTitle(), patchProductReq.getCategoryIdx(),
                patchProductReq.getPrice(), patchProductReq.getContent(), productIdx};
        return this.jdbcTemplate.update(modifyProductQuery,modifyProductParams);
    }
    //상품 삭제
    public int deleteProduct(int productIdx){
        String deleteProductQuery = "update Product set postStatus = 'delete' where productIdx = ? ";
        Object[] deleteProductParam = new Object[]{productIdx};

        return this.jdbcTemplate.update(deleteProductQuery, deleteProductParam);

    }

    //상품 검색
    public Page<GetProductsRes> getProductsByKeyword (String keyword, Pageable pageable){
        String wrappedKeyword = "%" + keyword + "%";

        List<GetProductsRes> products = this.jdbcTemplate.query("select * from (select User.userIdx as userIdx, p.productIdx as productIdx, C.name as categoryName, p.title, productStatus, IFNULL(GROUP_CONCAT(I.imgUrl),'') as imgUrl,\n" +
                        "       Region.name,p.price,p.updateCount,  IFNULL(roomCnt, 0) as roomCnt, IFNULL(likeCnt, 0) as likeCnt, p.updateAt,\n" +
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
                        "\n" +
                        "    left outer join Image I on p.productIdx = I.productIdx\n" +
                        "GROUP BY p.productIdx) t\n" +
                        "WHERE t.title LIKE ?" +
                        "ORDER BY updateAt DESC;",
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
                }, wrappedKeyword);
        return listToPage(products, pageable);


    }

    private Page<GetProductsRes> listToPage(List<GetProductsRes> products, Pageable pageable) {
        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), products.size());
        return new PageImpl<>(products.subList(start, end), pageable, products.size());
    }


}
