package com.example.demo.src.like;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.like.model.PostLikeReq;
import com.example.demo.src.product.model.GetProductsRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;
    private final LikeProvider likeProvider;

    @PostMapping("/{productIdx}")
    public BaseResponse<String> like(@PathVariable("productIdx") int productIdx, @RequestBody PostLikeReq postLikeReq) {
        try {
            likeService.createLike(postLikeReq,productIdx);
            String result = "좋아요";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @DeleteMapping ("/{productIdx}")
    public BaseResponse<String> disLike(@PathVariable("productIdx") int productIdx, @RequestBody PostLikeReq postLikeReq) {
        try {
            likeService.deleteLike(postLikeReq, productIdx);
            String result = "좋아요 취소";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @GetMapping("/{userIdx}")
    public BaseResponse<List<GetProductsRes>> getLikeProducts(@PathVariable("userIdx") int userIdx){
        try {
            List<GetProductsRes> likeList = likeProvider.getLikeList(userIdx);
            return new BaseResponse<>(likeList);
        }
        catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

}
