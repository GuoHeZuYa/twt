package cn.twt.open;

import cn.twt.open.constant.ErrorCode;
import cn.twt.open.dao.user.UserDao;
import cn.twt.open.dto.user.UserDto;
import cn.twt.open.pojo.user.User;
import cn.twt.open.pojo.user.UserDo;
import cn.twt.open.utils.APIResponse;
import cn.twt.open.utils.EncryptUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

@SpringBootTest
class OpenApplicationTests {

    @Autowired
    UserDao userDao;

    // app_key和app_secret生成器
    @Test
    void contextLoads() {
        // localhost  MTIzLmU0YmVlNDhlYjViMzg0ZWNlNGNmN2RjYWU5Y2MzYTliZDgxMjg0OTE=
        // testopen.twt.edu.cn  Y2hvY29sYXRlLmM5ZGE1MjA3ZjFkZmZhNDVkOWRhYzM2ZTJhMjU3OTJkM2Y5MzQ1MzM=
        // 文档密码 twtstudio!@#
        String app_key = "52Hz";
        String domain = "52Hz.twt.edu.cn";
        System.out.println("app_key="+app_key);
        String app_secret = EncryptUtils.getSHA1String(app_key+domain);
        System.out.println("app_secret="+app_secret);
        String ticket = app_key.concat(".").concat(app_secret);
        String encode = Base64.getEncoder().encodeToString(ticket.getBytes());
        System.out.println("ticket="+encode);
        System.out.println("domain="+domain);
        System.out.println(new String(Base64.getDecoder().decode(encode)));
    }

    @Test
    void testSomeClass() throws JsonProcessingException {
        ObjectMapper noticeMapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        map.put("type",2);
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("title", "xxxx");
        dataMap.put("content", "xxxx");
        dataMap.put("url", "xxxx");
        map.put("data", dataMap);
        System.out.println(noticeMapper.writeValueAsString(map));
    }

}
