package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.chat.ChatService;
import com.example.demo.src.chat.model.GetRoomRes;
import com.example.demo.src.chat.model.PostChatRoomReq;
import com.example.demo.src.chat.model.PostChatRoomRes;
import com.example.demo.src.product.model.*;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;


@RestController
@RequestMapping("/app/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductProvider productProvider;
    private final ProductService productService;
    private final ChatService chatService;
    private final JwtService jwtService;

    @GetMapping
    public BaseResponse<Page<GetProductsRes>> getProducts(@RequestParam(required = false) String region,
                                                          @PageableDefault(size = 5) Pageable pageable) {
        try {
            if(region == null){
                Page<GetProductsRes> products = productProvider.getProducts(pageable);
                return new BaseResponse<>(products);
            }
            Page<GetProductsRes> productsByRegion = productProvider.getProductsByRegion(region, pageable);
            return new BaseResponse<>(productsByRegion);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    @GetMapping("/{productIdx}")
    public BaseResponse<GetProductRes> getProduct(@PathVariable("productIdx") int productIdx){
        try {
            GetProductRes product = productProvider.getProduct(productIdx);
            return new BaseResponse<>(product);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
/*
    @PostMapping("/{productIdx}")
    public BaseResponse<PostChatRoomRes> postChatRoom(@PathVariable("productIdx") int productIdx,
                                                      @RequestBody PostChatRoomReq postChatRoomReq){
        try {
            PostChatRoomRes chatRoom = chatService.createChatRoom(postChatRoomReq, productIdx);
            return new BaseResponse<>(chatRoom);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

 */

    @PostMapping
    public BaseResponse<PostProductRes> createProduct(@RequestBody PostProductReq postProductReq){

        //제목 없음
        if (postProductReq.getTitle() == null || postProductReq.getContent() ==null || postProductReq.getImgUrl() ==null ){
            return new BaseResponse<>(POST_CHAT_EMPTY);
        }
        try {
            PostProductRes product = productService.createProduct(postProductReq);
            return new BaseResponse<>(product);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @PatchMapping("/{productIdx}")
    public BaseResponse<String> modifyUserName(@PathVariable("productIdx") int productIdx,
                                               @RequestBody PatchProductReq patchProductReq) {
        try {
            //jwt에서 idx 추출
            int userIdxByJwt = jwtService.getUserIdx();
            //수정하고자 하는 상품의 userIdx 가져와서 비교
            GetProductRes product = productProvider.getProduct(productIdx);
            if(product.getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if (patchProductReq.getContent() == null || patchProductReq.getTitle() == null || patchProductReq.getStatus() == null){
                return new BaseResponse<>(PATCH_PRODUCT_EMPTY);
            }
            productService.modifyProduct(patchProductReq, productIdx);
            String result = "상품내용이 수정되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    @GetMapping("/{productIdx}/chat")
    public BaseResponse<GetRoomRes> getRoomIdx(@PathVariable("productIdx") int productIdx){
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            int chatRoom = productProvider.getChatRoom(userIdxByJwt, productIdx);
            return new BaseResponse<>(new GetRoomRes(chatRoom));

        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @GetMapping("/search")
    public BaseResponse<Page<GetProductsRes>> getProductsByKeyword(@RequestParam String keyword,
                                                     @PageableDefault(size = 20) Pageable pageable){
        try {
            Page<GetProductsRes> productsByKeyword = productProvider.getProductsByKeyword(keyword, pageable);
            return new BaseResponse<>(productsByKeyword);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @PatchMapping("/{productIdx}/delete")
    public BaseResponse<String> deleteProduct(@PathVariable("productIdx") int productIdx) {
        try {
            productService.deleteProduct(productIdx);
            String result = "상품이 삭제되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
