package cn.twt.open.exception;

/**
 * @author Lino
 */
public class RedisNegativeOffsetException extends RuntimeException {
    public RedisNegativeOffsetException() {
        super("偏移量不能小于零");
    }
}
