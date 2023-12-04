package cn.twt.open.utils;

import cn.twt.open.constant.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIResponse {

    private int error_code;
    private String message;
    private Object result;

    public APIResponse(int error_code, String message){
        this.error_code = error_code;
        this.message = message;
    }

    public APIResponse(int error_code, Object result){
        this.error_code = error_code;
        this.result = result;
    }

    public APIResponse(String message, Object result){
        this.message = message;
        this.result = result;
    }

    public static APIResponse error(String message){
        return new APIResponse(-1,message,null);
    }

    public static APIResponse error(ErrorCode err){
        return new APIResponse(err.getCode(), err.getMessage(), null);
    }

    public static APIResponse success(Object result){
        return new APIResponse(0,ErrorCode.OK.getMessage(),result);
    }
}
