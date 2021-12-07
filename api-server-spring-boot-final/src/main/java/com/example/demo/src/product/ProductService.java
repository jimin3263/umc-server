package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.product.model.PatchProductReq;
import com.example.demo.src.product.model.PostProductReq;
import com.example.demo.src.product.model.PostProductRes;
import com.example.demo.src.user.model.PatchUserReq;
import com.example.demo.src.user.model.PostUserReq;
import com.example.demo.src.user.model.PostUserRes;
import com.example.demo.utils.AES128;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductDao productDao;

    @Transactional
    public PostProductRes createProduct(PostProductReq postProductReq) throws BaseException {
        try {
            int product = productDao.createProduct(postProductReq);
            return new PostProductRes(product);
        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //상품정보 수정
    @Transactional
    public void modifyProduct(PatchProductReq patchProductReq, int productIdx) throws BaseException {
        try {
            int result = productDao.updateProduct(patchProductReq, productIdx);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_PRODUCT);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //상품 삭제
    @Transactional
    public void deleteProduct(int productIdx) throws BaseException {
        try {
            int result = productDao.deleteProduct(productIdx);
            if (result == 0) {
                throw new BaseException(DELETE_FATL_PRODUCT);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }



}
