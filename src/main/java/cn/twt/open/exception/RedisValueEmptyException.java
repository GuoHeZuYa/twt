package cn.twt.open.exception;

/**
 * @author Lino
 */
public class RedisValueEmptyException extends RuntimeException {
    public RedisValueEmptyException(String message) {
        super(message);
    }
}
