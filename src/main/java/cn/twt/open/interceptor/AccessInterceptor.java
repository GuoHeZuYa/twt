package cn.twt.open.interceptor;

import cn.twt.open.constant.ErrorCode;
import cn.twt.open.utils.APIResponse;
import com.alibaba.fastjson.JSON;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

/**
 * @author Lino
 */
public class AccessInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getAttribute("app_key") == null){
            return false;
        } else {
            String app_key = (String) request.getAttribute("app_key");
            String ticket = app_key.concat(".").concat((String) request.getAttribute("app_secret"));
            String requestTicket = request.getHeader("ticket");
            String encodeTicket = Base64.getEncoder().encodeToString(ticket.getBytes());
            if (encodeTicket.equals(requestTicket)){
                return true;
            } else {
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().println(
                        JSON.toJSONString(
                                APIResponse.error(ErrorCode.ERROR_KEY_SECRET)
                        )
                );
                return false;
            }
        }
    }
}
