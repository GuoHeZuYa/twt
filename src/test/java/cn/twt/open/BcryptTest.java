package cn.twt.open;

import at.favre.lib.crypto.bcrypt.BCrypt;
import cn.twt.open.utils.EncryptUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class BcryptTest {

    @Test
    public void test(){
        String password = "123456";
        String raw = "$2y$10$QVd8pczWoM7.N51HlkK5a.e8ncA5rpbLhdNwKmSKm7u0I6I3WZRPO";
        BCrypt.Result res = BCrypt.verifyer().verify(password.toCharArray(), raw);
        System.out.println(res.verified);
//        $2y$10$o2uKC5AlUbN6K.1el4vClerSyBvkxT15Rw1X58jJo09Riia6a7E2C
//        String hash = EncryptUtils.getBCryptString(password);
//        System.out.println(hash);
    }
}
