package cn.twt.open.cronJob.task;

import cn.twt.open.config.ApolloConfigManager;
import cn.twt.open.cronJob.service.ReturnSchoolCronService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

/**
 * @author Lino
 */
@Configuration
@EnableScheduling
@Slf4j
public class ReturnSchoolCronTask {

    @Resource
    ReturnSchoolCronService returnSchoolCronService;

    @Resource
    ApolloConfigManager apolloConfigManager;

    @Scheduled(cron = "0 0 18 * * ?")
    public void sendMsg() {
        log.info("Start to send message to all assistant");
        try {
            if (apolloConfigManager.getSendHealthInfoMessageCronSwitch()) {
                returnSchoolCronService.sendMsgToAllAssistants();
                log.info("Send message to all assistant finished.");
            }
        } catch (Exception e) {
            log.error("Send message to all assistant failed.",e);
        }
    }

    @Scheduled(cron = "0 0 21 * * ?")
    public void sendMsg2(){
        log.info("Start to send message to all assistant");
        try {
            if (apolloConfigManager.getSendHealthInfoMessageCronSwitch()){
                returnSchoolCronService.sendMsgToAllAssistants();
                log.info("Send message to all assistant finished.");
            }
        } catch (Exception e) {
            log.error("Send message to all assistant failed.",e);
        }
    }
}
