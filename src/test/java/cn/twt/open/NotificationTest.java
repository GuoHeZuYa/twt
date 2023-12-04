package cn.twt.open;

import cn.twt.open.pojo.notification.Notification;
import cn.twt.open.utils.GtNotificationUtils;
import com.getui.push.v2.sdk.common.ApiResult;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class NotificationTest {

    @Value("${getui.appId:}")
    String appId;
    @Value("${getui.appKey:}")
    String appKey;
    @Value("${getui.masterSecret:}")
    String masterSecret;

    @Test
    public void test(){
        System.out.println(appId);
        System.out.println(appKey);
        System.out.println(masterSecret);
        Notification notice = new Notification();
        notice.setTitle("测试ios");
        notice.setContent("测试ios");
        notice.setUrl("测试ios");
        ApiResult r = GtNotificationUtils
                .sendNotificationToUsers(Lists.newArrayList("cfbb5fb7600c6abbf9fe528f3cd2c4ee"), notice);
        System.out.println(r);
    }
}
