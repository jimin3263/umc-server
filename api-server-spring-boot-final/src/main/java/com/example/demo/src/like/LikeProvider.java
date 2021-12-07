package com.example.demo.src.like;

import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.GetProductsRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class LikeProvider {
    private final LikeDao likeDao;

    public List<GetProductsRes> getLikeList(int userIdx) throws BaseException {
        try {
            List<GetProductsRes> likeProducts = likeDao.getLikeProducts(userIdx);
            return likeProducts;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
