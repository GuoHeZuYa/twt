package cn.twt.open.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Lino
 * @description 简单的接口请求记录
 */
@Aspect
@Component
public class WebLogAspect {
    private Logger logger = LoggerFactory.getLogger(WebLogAspect.class);

    @Pointcut("execution(* cn.twt.open.controller..*.*(..))")
    public void apiOperationLog(){}

    @Before("apiOperationLog()")
    public void logBeforeInvokeApi(JoinPoint joinPoint){
        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
            String message = "Request Sender: "
                    .concat(request.getHeader("domain")).concat(" ")
                    .concat("Request URL: "+ request.getRequestURL().toString()).concat(" ")
                    .concat("Request Args: "+ Arrays.toString(joinPoint.getArgs()));
            logger.info(message);
        } catch (Exception e){
            logger.error("Request failed ",e);
        }
    }

//    @Around("")
//    public void logAroundInvokeApi(ProceedingJoinPoint joinPoint) throws Throwable {
//        long traceId = System.currentTimeMillis();
//        try {
//            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//            HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
//            String message = "Request Sender: "
//                    .concat(request.getHeader("domain")).concat(" ")
//                    .concat("Request URL: "+ request.getRequestURL().toString()).concat(" ")
//                    .concat("Request Args: "+ Arrays.toString(joinPoint.getArgs()));
//            Object result = joinPoint.proceed();
//            logger.info("TraceId: {}, Request Sender: {}, Request API: {}, Request Args: {}",
//                    traceId, request.getHeader("domain"), request.getRequestURL().toString(),Arrays.toString(joinPoint.getArgs()));
//        }  catch (Exception e) {
//            logger.error("TraceId: {}", traceId, e);
//            throw e;
//        }
//    }

    @AfterThrowing(value = "apiOperationLog()", throwing = "e")
    public void logAfterThrowingException(JoinPoint joinPoint, Throwable e){
        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
            String className = joinPoint.getTarget().getClass().getName();
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            String methodName = method.getName();
            String url = request.getRequestURL().toString();
            String args = Arrays.toString(joinPoint.getArgs());
            String message = "Exception thrown: "
                    .concat(url).concat(" ")
                    .concat(className).concat(" ")
                    .concat(methodName).concat(" ")
                    .concat(args);
            logger.error(message,e);
        } catch (Exception ee){
            logger.error("Request failed ",ee);
        }
    }
}
