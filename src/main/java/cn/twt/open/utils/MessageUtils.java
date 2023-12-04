package cn.twt.open.utils;

import cn.twt.open.constant.AuthConstant;
import cn.twt.open.constant.ErrorCode;
import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsSingleSend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Random;

/**
 * @author Lino
 */
@Slf4j
@Component
public class MessageUtils {

    @Value("${yunpian.auth.verify.msg.prefix:【天外天】您的验证码是}")
    private String VERIFY_MSG_TPL_PREFIX;

    @Value("${yunpian.auth.verify.msg.suffix:，30分钟内有效且具有一次性，非本人操作请忽略}")
    private String VERIFY_MSG_TPL_SUFFIX;

    @Value("${yunpian.health.msg.prefix:【天外天】老师您好，截至目前，您尚有}")
    private String HEALTH_MSG_TPL_PREFIX;

    @Value("${yunpian.health.msg.suffix:位学生未填报健康信息，请登录 https://i.twt.edu.cn/#/reportManage 查看并提醒未填报的学生错峰进行填报。}")
    private String HEALTH_MSG_TPL_SUFFIX;

    public Result<SmsSingleSend> sendVerificationCode(String phone, String code){
        YunpianClient client = new YunpianClient(AuthConstant.YUNPIAN_APIKEY).init();
        Map<String, String> param = client.newParam(2);
        param.put(YunpianClient.MOBILE, phone);
        param.put(YunpianClient.TEXT, VERIFY_MSG_TPL_PREFIX+code+VERIFY_MSG_TPL_SUFFIX);
        Result<SmsSingleSend> r = client.sms().single_send(param);
        client.close();
        client = null;
        return r;
    }

    public Result<SmsSingleSend> sendHealthReportConditionMessage(String phone, String num){
        YunpianClient client = new YunpianClient(AuthConstant.YUNPIAN_APIKEY).init();
        Map<String, String> param = client.newParam(2);
        param.put(YunpianClient.MOBILE, phone);
        param.put(YunpianClient.TEXT, HEALTH_MSG_TPL_PREFIX+num+HEALTH_MSG_TPL_SUFFIX);
        log.info("Sending message to {}, message content: {}", phone, HEALTH_MSG_TPL_PREFIX+num+HEALTH_MSG_TPL_SUFFIX);
        Result<SmsSingleSend> r = client.sms().single_send(param);
        client.close();
        client = null;
        return r;
    }

    public static APIResponse verifyCode(String phone, String code, HttpServletRequest httpServletRequest){
        try {
            String currentCode = (String) httpServletRequest.getSession().getAttribute(AuthConstant.CODE_NAME);
            String currentPhone = (String) httpServletRequest.getSession().getAttribute(AuthConstant.PHONE);
            log.info("destination_code: "+ currentCode +", source_code: "+code);
            log.info("destination_phone: "+ currentPhone +", source_phone: "+phone);
            if (code.equals(currentCode) && phone.equals(currentPhone)){
                httpServletRequest.getSession().removeAttribute(AuthConstant.CODE_NAME);
                httpServletRequest.getSession().removeAttribute(AuthConstant.PHONE);
                httpServletRequest.getSession().setAttribute(AuthConstant.IS_VERIFIED, true);
                return APIResponse.success(null);
            } else {
                return APIResponse.error(ErrorCode.VERIFICATION_FAILED);
            }
        } catch (Exception e){
            log.error("error", e);
            return APIResponse.error(ErrorCode.LOGIC_ERROR);
        }
    }

    public static String generateVerificationCode(int length){
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(100) % 10);
        }
        return sb.toString();
    }
}
