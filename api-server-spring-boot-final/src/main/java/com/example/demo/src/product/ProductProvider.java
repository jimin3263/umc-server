package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.src.chat.ChatDao;
import com.example.demo.src.product.model.GetProductRes;
import com.example.demo.src.product.model.GetProductsRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductProvider {

    private final ProductDao productDao;
    private final ChatDao chatDao;

    //제품 목록 조회
    public Page<GetProductsRes> getProducts(Pageable pageable) throws BaseException {
        try {
            Page<GetProductsRes> getProductRes = productDao.getProducts(pageable);
            return getProductRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //특정 제품 조회
    public GetProductRes getProduct(int productIdx) throws BaseException {
        try {
            return productDao.getProduct(productIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //상품 조회


    //검색
    public Page<GetProductsRes> getProductsByKeyword(String keyword, Pageable pageable) throws BaseException {
        try {
            return productDao.getProductsByKeyword(keyword, pageable);
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //지역별 조회
    public Page<GetProductsRes> getProductsByRegion(String region, Pageable pageable) throws BaseException{
        try {
            return productDao.getProductsByRegion(region, pageable);
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //유저별 조회
    public List<GetProductsRes> getProductsByUser(int userIdx, String status) throws BaseException{
        try {
            return productDao.getProductsByUser(userIdx, status);
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //채팅방 idx
    public int getChatRoom(int userIdx, int productIdx) throws BaseException{
        try {
            int findRoomidx = chatDao.getChatRoom(userIdx, productIdx);
            if( findRoomidx == 0){ //존재하지 않는 방이라면
                return chatDao.createChatRoom(userIdx,productIdx);
            } else {
                return findRoomidx;
            }
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
