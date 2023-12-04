package cn.twt.open.exception;


/**
 * @author Lino
 */
public class RedisKeyEmptyException extends RuntimeException {

    public RedisKeyEmptyException(String message) {
        super(message);
    }
}
