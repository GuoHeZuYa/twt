package cn.twt.open.config;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author lino
 */
@Component
public class ApolloConfigManager {

    private static final Logger log = LoggerFactory.getLogger(ApolloConfigManager.class);

    @ApolloConfig
    private Config config;

    private static final String SEND_HEALTH_INFO_MESSAGE_CRON_SWITCH = "send_health_info_message_cron_switch";

    public String getString(String key, String defaultValue) {
        try {
            return config.getProperty(key, defaultValue);
        } catch (Exception e) {
            log.error("getString Exception, key: {}", key, e);
            return defaultValue;
        }
    }

    private Boolean getBoolean(String key, Boolean defaultValue) {
        try {
            String value = config.getProperty(key, "false");
            return "true".equals(value);
        } catch (Exception e) {
            log.error("getBoolean Exception, key: {}", key, e);
            return defaultValue;
        }
    }

    public Integer getInteger(String key, Integer defaultValue) {
        try {
            String value = config.getProperty(key, "0");
            return Integer.parseInt(value);
        } catch (Exception e) {
            log.error("getBoolean Exception, key: {}", key, e);
            return defaultValue;
        }
    }

    public List<String> getList(String key, List<String> defaultValue) {
        try {
            String value = config.getProperty(key, "");
            return Lists.newArrayList(value.split(",",-1));
        } catch (Exception e) {
            log.error("getBoolean Exception, key: {}", key, e);
            return defaultValue;
        }
    }

    public Boolean getSendHealthInfoMessageCronSwitch() {
        return getBoolean(SEND_HEALTH_INFO_MESSAGE_CRON_SWITCH, false);
    }

}
