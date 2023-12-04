package cn.twt.open.interceptor;

import cn.twt.open.annotation.jwt.JwtToken;
import cn.twt.open.constant.ErrorCode;
import cn.twt.open.constant.UserRole;
import cn.twt.open.dao.auth.AuthDao;
import cn.twt.open.exception.token.TokenErrorException;
import cn.twt.open.pojo.auth.Credential;
import cn.twt.open.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    AuthDao authDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(JwtUtils.HEADER);
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod =(HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(JwtToken.class)){
            JwtToken jwtToken = method.getAnnotation(JwtToken.class);
            if (jwtToken.required()){
                if (token == null || "".equals(token)) {
                    throw new TokenErrorException(ErrorCode.TOKEN_ERROR);
                } else {
                    // 如果有token，则先判断合规性
                    JwtUtils.checkToken(token);
                    // 鉴权
                    String userNumber = JwtUtils.getUserNumber(token);
                    List<Credential> credential = authDao.getUserCredentialByAccount(userNumber);
                    if (credential.size() == 0) {
                        throw new TokenErrorException(ErrorCode.NO_SUCH_USER);
                    }
                    List<Integer> allowRoles = Arrays
                            .stream(jwtToken.roles())
                            .boxed()
                            .collect(Collectors.toList());
                    int role = credential.get(0).getRole();
                    if (allowRoles.contains(role) || role == UserRole.TWT_ADMIN){
                        return true;
                    } else {
                        throw new TokenErrorException(ErrorCode.PERMISSION_DENIED);
                    }
                }
            }
        }

        return true;
    }
}
