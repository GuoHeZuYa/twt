package cn.twt.open.utils;

import cn.twt.open.constant.ErrorCode;
import cn.twt.open.exception.token.TokenErrorException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

/**
 * @author Lino
 */
public class JwtUtils {

    /**
     * 一小时
     */
    private static final long HOUR = 60 * 60 * 1000;
    /**
     * 30天
     */
    private static final Long EXPIRE_TIME = HOUR * 24 * 30;
    /**
     * secret
     */
    private static final String SECRET = "twtopen_iepKflsGJKwPt8";

    /**
     * header字段
     */
    public static final String HEADER = "token";

    /**
     * token清空
     */
    public static final String UNDEFINED = "undefined";

    /**
     * 生成jwt token
     * @param userNumber 学号
     * @param role 权限
     * @return token字符串
     */
    public static String genJwtToken(String userNumber, Integer role){
        try {
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            return JWT.create()
                    // 将 user id 保存到 token 里面
                    .withAudience(userNumber, String.valueOf(role))
                    // 30天后token过期
                    .withExpiresAt(date)
                    // token 的密钥
                    .sign(algorithm);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从token中获取学号
     * @param token
     * @return 学号
     */
    public static String getUserNumber(String token) {
        try {
            return JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException e) {
            throw new TokenErrorException(ErrorCode.TOKEN_EXPIRED);
        }
    }

    /**
     * 从token中获取用户权限
     * @param token
     * @return 用户权限数字
     */
    public static Integer getUserType(String token) {
        try {
            String roleStr = JWT.decode(token).getAudience().get(1);
            return Integer.parseInt(roleStr);
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 检验token的合规性
     * @param token
     */
    public static void checkToken(String token) {
        if (token.equals(UNDEFINED)) {
            throw new TokenErrorException(ErrorCode.PLEASE_LOGIN);
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
        } catch (JWTVerificationException exception) {
            throw new TokenErrorException(ErrorCode.TOKEN_EXPIRED);
        }
    }
}
