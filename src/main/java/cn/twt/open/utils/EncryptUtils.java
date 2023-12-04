package cn.twt.open.utils;


import at.favre.lib.crypto.bcrypt.BCrypt;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author Lino
 */
public class EncryptUtils {
    private static final String COMMON_ENCRYPT_SALT = "TWT_STUDIO_eOp8EAw4ti";

    public static String getMD5String(String str){
        String text = str.concat(COMMON_ENCRYPT_SALT);
        return DigestUtils.md5Hex(text.getBytes());
    }

    public static String getSHA1String(String str){
        String text = str.concat(COMMON_ENCRYPT_SALT);
        return DigestUtils.sha1Hex(text.getBytes());
    }

    public static String getBCryptString(String str){
        return BCrypt.with(BCrypt.Version.VERSION_2Y).hashToString(10,str.toCharArray());
    }

    public static boolean verifyBCryptHash(String str, String hash){
        // 用来验证新用户和老用户的密码。。太艹了
        String text = str.concat(COMMON_ENCRYPT_SALT);
        BCrypt.Result res1 = BCrypt.verifyer().verify(text.toCharArray(), hash);
        BCrypt.Result res2 = BCrypt.verifyer().verify(str.toCharArray(), hash);
        return res1.verified || res2.verified;
    }
}
