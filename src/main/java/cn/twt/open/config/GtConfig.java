package cn.twt.open.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author Lino
 */
@Configuration
public class GtConfig {

    @Value("${getui.appId:}")
    private String gtAppId;
    @Value("${getui.appKey:}")
    private String gtAppKey;
    @Value("${getui.masterSecret:}")
    private String gtMasterSecret;

    private static String appId;
    private static String appKey;
    private static String masterSecret;

    @PostConstruct
    public void getStaticProperties(){
        appId = this.gtAppId;
        appKey = this.gtAppKey;
        masterSecret = this.gtMasterSecret;
    }

    public static String getAppId() {
        return appId;
    }

    public static String getAppKey() {
        return appKey;
    }

    public static String getMasterSecret() {
        return masterSecret;
    }
}
