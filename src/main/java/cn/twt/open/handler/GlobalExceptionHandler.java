package cn.twt.open.handler;

import cn.twt.open.constant.ErrorCode;
import cn.twt.open.exception.token.TokenErrorException;
import cn.twt.open.utils.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Lino
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(TokenErrorException.class)
    public APIResponse tokenError(TokenErrorException e){
        return APIResponse.error(e.getError());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public APIResponse duplicateKeyError(DuplicateKeyException e){
        e.printStackTrace();
        log.error(e.toString());
        if (e.toString().contains("unique_email")){
            return APIResponse.error(ErrorCode.EMAIL_DUPLICATED);
        } else if (e.toString().contains("unique_telephone")){
            return APIResponse.error(ErrorCode.PHONE_DUPLICATED);
        } else if (e.toString().contains("unique_nickname")) {
            return APIResponse.error(ErrorCode.NICKNAME_DUPLICATED);
        } else if (e.toString().contains("unique_usernumber")){
            return APIResponse.error(ErrorCode.USER_EXIST);
        } else {
            return APIResponse.error(ErrorCode.EMAIL_OR_PHONE_DUPLICATED);
        }
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public APIResponse handleMismatchError(MethodArgumentTypeMismatchException e, HttpServletResponse response){
        log.error("mismatch",e);
        response.setStatus(404);
        return APIResponse.error(ErrorCode.RESOURCE_MISMATCH);
    }

    @ExceptionHandler(RuntimeException.class)
    public APIResponse otherError(RuntimeException e){
        log.error("Exception",e);
        return APIResponse.error(ErrorCode.LOGIC_ERROR);
    }

}
