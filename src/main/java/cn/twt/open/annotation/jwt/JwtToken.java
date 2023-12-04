package cn.twt.open.annotation.jwt;


import cn.twt.open.constant.UserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Lino
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JwtToken {
    boolean required() default true;
    int[] roles() default {
        UserRole.NORMAL_STUDENT,
        UserRole.ASSISTANT,
        UserRole.NOTIFICATION_MAN
    };
}
