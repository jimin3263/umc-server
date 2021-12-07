package com.example.demo.src.like;

import com.example.demo.config.BaseException;
import com.example.demo.src.like.model.DeleteLikeRes;
import com.example.demo.src.like.model.PostLikeReq;
import com.example.demo.src.like.model.PostLikeRes;
import com.example.demo.src.product.model.PatchProductReq;
import com.example.demo.src.product.model.PostProductReq;
import com.example.demo.src.product.model.PostProductRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.MODIFY_FAIL_PRODUCT;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeDao likeDao;

    //좋아요
    public PostLikeRes createLike(PostLikeReq postLikeReq, int productIdx) throws BaseException{
        try {
            int like = likeDao.createLike(productIdx, postLikeReq);
            return new PostLikeRes(like);
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteLike(PostLikeReq postLikeReq, int productIdx) throws BaseException{
        try {
            likeDao.deleteLike(productIdx, postLikeReq);
        }catch (Exception e){
        throw new BaseException(DATABASE_ERROR);
        }
    }
}
