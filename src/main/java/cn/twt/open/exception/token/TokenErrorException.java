package cn.twt.open.exception.token;

import cn.twt.open.constant.ErrorCode;

/**
 * @author Lino
 */
public class TokenErrorException extends RuntimeException {
    private ErrorCode error;

    public TokenErrorException(ErrorCode error){
        super(error.getMessage());
        this.error = error;
    }

    public ErrorCode getError() {
        return error;
    }
}
