package cn.twt.open.annotation.manage;

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
public @interface Manage {
    boolean required() default true;
    int[] roles() default UserRole.TWT_ADMIN;
}
