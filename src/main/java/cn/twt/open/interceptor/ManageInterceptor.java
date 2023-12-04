package cn.twt.open.interceptor;

import cn.twt.open.annotation.manage.Manage;
import cn.twt.open.constant.ManageConstant;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author Lino
 */
public class ManageInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod =(HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(Manage.class)){
            Manage manage = method.getAnnotation(Manage.class);
            if (manage.required()){
                String url = request.getHeader("domain");
                if (url.equals(ManageConstant.HOST)){
                    return true;
                }
            }
        }

        return true;
    }
}
