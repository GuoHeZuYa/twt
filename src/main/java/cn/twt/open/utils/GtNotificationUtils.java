package cn.twt.open.utils;

import cn.twt.open.config.GtConfig;
import cn.twt.open.pojo.notification.Notification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.getui.push.v2.sdk.ApiHelper;
import com.getui.push.v2.sdk.GtApiConfiguration;
import com.getui.push.v2.sdk.api.PushApi;
import com.getui.push.v2.sdk.common.ApiResult;
import com.getui.push.v2.sdk.dto.req.Audience;
import com.getui.push.v2.sdk.dto.req.AudienceDTO;
import com.getui.push.v2.sdk.dto.req.Settings;
import com.getui.push.v2.sdk.dto.req.Strategy;
import com.getui.push.v2.sdk.dto.req.message.PushChannel;
import com.getui.push.v2.sdk.dto.req.message.PushDTO;
import com.getui.push.v2.sdk.dto.req.message.PushMessage;
import com.getui.push.v2.sdk.dto.req.message.android.AndroidDTO;
import com.getui.push.v2.sdk.dto.req.message.android.GTNotification;
import com.getui.push.v2.sdk.dto.req.message.android.ThirdNotification;
import com.getui.push.v2.sdk.dto.req.message.android.Ups;
import com.getui.push.v2.sdk.dto.req.message.ios.Alert;
import com.getui.push.v2.sdk.dto.req.message.ios.Aps;
import com.getui.push.v2.sdk.dto.req.message.ios.IosDTO;
import com.getui.push.v2.sdk.dto.res.TaskIdDTO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class GtNotificationUtils {

    private static PushApi pushApi = null;

    static {
        GtApiConfiguration apiConfiguration = new GtApiConfiguration();
        apiConfiguration.setAppId(GtConfig.getAppId());
        apiConfiguration.setAppKey(GtConfig.getAppKey());
        apiConfiguration.setMasterSecret(GtConfig.getMasterSecret());
        apiConfiguration.setDomain("https://restapi.getui.com/v2/");
        ApiHelper apiHelper = ApiHelper.build(apiConfiguration);
        pushApi = apiHelper.creatApi(PushApi.class);
    }

    public static ApiResult sendNotificationToUsers(List<String> userCid, Notification notice){
        ApiResult<TaskIdDTO> taskIdDto = createMsg(notice);
        if (!taskIdDto.isSuccess()){
            System.out.println("get taskId failed!");
            log.error("发送推送失败,错误码: "+taskIdDto.getCode()+", 错误信息: "+taskIdDto.getMsg());
            return taskIdDto;
        }
        String taskId = taskIdDto.getData().getTaskId();
        AudienceDTO audienceDTO = new AudienceDTO();
        audienceDTO.setAsync(true);
        audienceDTO.setTaskid(taskId);
        // 这里是为了突破单次限制1000条的限制，先这样写吧丑死了
        List<List<String>> cidGroup = Lists.partition(userCid, 900);
        ApiResult<Map<String, Map<String, String>>> apiResult = null;
        for (List<String> cids : cidGroup) {
            Audience audience = new Audience();
            for (String cid: cids) {
                audience.addCid(cid);
            }
            audienceDTO.setAudience(audience);
            apiResult = pushApi.pushListByCid(audienceDTO);
        }
        return apiResult;
    }

    /**
     * 根据个推官方文档参数设置的消息实体，修改请参考官方文档
     * @param notice
     * @return
     */
    private static ApiResult<TaskIdDTO> createMsg(Notification notice) {
        PushDTO pushDTO = new PushDTO();
        pushDTO.setRequestId(System.currentTimeMillis()+"");
        pushDTO.setGroupName("简单通知");
        // PushMessage
        PushMessage pushMessage = new PushMessage();
        ObjectMapper noticeMapper = new ObjectMapper();
        //外面的map
        Map<String, Object> map = new HashMap<>();
        map.put("type",2);
        //dataMap
        //将传入的自定义数据类型notice放入dataMap中
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("title", notice.getTitle());
        dataMap.put("content", notice.getContent());
        dataMap.put("url", notice.getUrl());
        map.put("data", dataMap);
        String noticeJson = "";
        try {
            noticeJson = noticeMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        pushMessage.setTransmission(noticeJson);
        pushDTO.setPushMessage(pushMessage);

        // Settings
        {
            Settings settings = new Settings();
            settings.setTtl(3600000);
            Strategy strategy = new Strategy();
            strategy.setDef(2);
            strategy.setIos(4);
            settings.setStrategy(strategy);
            pushDTO.setSettings(settings);
        }

        // PushChannel
        {
            PushChannel pushChannel = new PushChannel();


            //ios
            //todo:为什么ios又不行了
            IosDTO iosDTO = new IosDTO();
            iosDTO.setType("notify");
            iosDTO.setPayload("自定义消息");
            Aps aps = new Aps();
            Alert alert = new Alert();
            alert.setTitle(notice.getTitle());
            alert.setBody(notice.getContent());
            aps.setAlert(alert);
            aps.setContentAvailable(0);
            iosDTO.setAps(aps);


            //Android
            AndroidDTO androidDTO = new AndroidDTO();
            ThirdNotification thirdNotification = new ThirdNotification();
            thirdNotification.setTitle(notice.getTitle());
            thirdNotification.setBody(notice.getContent());
            thirdNotification.setClickType("url");
            thirdNotification.setUrl(notice.getUrl());
            Ups ups = new Ups();
            ups.setNotification(thirdNotification);
            androidDTO.setUps(ups);


            pushChannel.setIos(iosDTO);
            pushChannel.setAndroid(androidDTO);
            pushDTO.setPushChannel(pushChannel);
        }

        return pushApi.createMsg(pushDTO);
    }
}
