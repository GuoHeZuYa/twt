package cn.twt.open.service.notification.impl;

import cn.twt.open.constant.ErrorCode;
import cn.twt.open.dao.notification.CidDao;
import cn.twt.open.dao.notification.HeSuanNoticeDao;
import cn.twt.open.dao.notification.NotificationDao;
import cn.twt.open.dto.notification.*;
import cn.twt.open.pojo.notification.HeSuanNotification;
import cn.twt.open.pojo.notification.Notification;
import cn.twt.open.service.notification.NotificationService;
import cn.twt.open.service.user.UserService;
import cn.twt.open.utils.APIResponse;
import cn.twt.open.utils.GtNotificationUtils;
import cn.twt.open.utils.JwtUtils;
import com.getui.push.v2.sdk.common.ApiResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.*;

import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Resource
    NotificationDao notificationDao;
    @Resource
    CidDao cidDao;
    @Resource
    HeSuanNoticeDao hesuanDao;
    @Resource
    UserService userService;

    private static ReentrantLock lock = new ReentrantLock();

    @Transactional
    @Override
    public APIResponse sendNotification2AllUsers(String token, NotificationDto notificationDto) {
        try {
            //拿到用户名
            String userNumber = JwtUtils.getUserNumber(token);
            //组装notification
            Notification notification = getNotification(notificationDto,userNumber);
            //设置向所有人发送
            notification.setTargetUser("all");
            //拿到所有人id
            List<String> userCid = cidDao.getAllCid();
            if(userCid.size()==0){
                return APIResponse.error(ErrorCode.NO_SUCH_USER);
            }
            //发送
            return sendNotification(userCid.stream().distinct().collect(Collectors.toList()), notification);
        } catch (Exception e){
            LOGGER.error("发送通知失败", e);
            if(null!=notificationDto){
                LOGGER.info("DEBUG::::::"+notificationDto.toString());
            }
            return APIResponse.error(ErrorCode.LOGIC_ERROR);
        }
    }

    @Transactional
    @Override
    public APIResponse sendNotification2SpecificUsers(String token, SpecificNotificationDto specificNotificationDto) {
        try {
            String userNumber = JwtUtils.getUserNumber(token);
            NotificationDto notificationDto = specificNotificationDto.getNotificationDto();
            StuTypeFilterDto stuTypeFilterDto = specificNotificationDto.getStuTypeFilterDto();
            Notification notification = getNotification(notificationDto,userNumber);
            List<String> userCid = cidDao.getSpecificCid(handleFilter(stuTypeFilterDto));
            List<String> userNumbers = userService.getSpecificUserNumber(handleFilter(stuTypeFilterDto));
            if(userCid.size()==0 || userNumbers.size()==0){
                return APIResponse.error(ErrorCode.NO_SUCH_USER);
            }
            return sendNotification(userCid.stream().distinct().collect(Collectors.toList()), notification,userNumbers);
        } catch (Exception e){
            LOGGER.error("发送通知失败", e);
            if(null != specificNotificationDto){
                LOGGER.error("DEBUG::::::"+specificNotificationDto.toString());
            }
            return APIResponse.error(ErrorCode.LOGIC_ERROR);
        }
    }

    @Override
    public APIResponse storeCid(String cid, String token) {
        lock.lock();
        try {
            String userNumber = JwtUtils.getUserNumber(token);
            if(cidDao.countUserNumber(userNumber) > 0){
                cidDao.updateCid(cid,userNumber);
            } else {
                cidDao.insertCid(cid,userNumber);
            }
            return APIResponse.success(null);
        } catch (Exception e){
            LOGGER.error("数据库错误",e);
            return APIResponse.error(ErrorCode.LOGIC_ERROR);
        } finally {
            lock.unlock();
        }
    }


    @Override
    public APIResponse getHistoryNotification() {
        try {
            return APIResponse.success(cidDao.getHistoryNotification());
        } catch (Exception e){
            LOGGER.error("ERROR",e);
            return APIResponse.error(ErrorCode.LOGIC_ERROR);
        }
    }

    @Override
    public APIResponse getHistoryNotificationByPage(int pageNum,int pageSize) {
        try {
            PageHelper.startPage(pageNum,pageSize);
            List<Notification> notificationList = cidDao.getHistoryNotification();
            PageInfo<Notification> pageInfo = new PageInfo<>(notificationList);
            return APIResponse.success(notificationList);
        }catch (Exception e ){
            LOGGER.error("分页获取历史通知失败",e);
            return APIResponse.error(ErrorCode.LOGIC_ERROR);
        }
    }

    @Override
    public APIResponse getMyNotification(String token) {
        try {
            String userNumber = JwtUtils.getUserNumber(token);
            List<NotificationDto> data = notificationDao.getNotificationByUserNumber(userNumber);
            return APIResponse.success(data);
        } catch (Exception e){
            LOGGER.error("ERROR",e);
            return APIResponse.error(ErrorCode.LOGIC_ERROR);
        }
    }

    @Override
    public APIResponse sendNotification2Users(String token, UserNumberNotificationDto userNumberNotificationDto) {
        try {
            String senderUserNumber = JwtUtils.getUserNumber(token);
            String userNumbersSeq = userNumberNotificationDto.getUserNumbers();
            List<String> userNumbers = Lists.newArrayList(userNumbersSeq.replace(" ","").split(";|；",-1));
            System.out.println(userNumbers);
            NotificationDto notificationDto = new NotificationDto();
            notificationDto.setTitle(userNumberNotificationDto.getTitle());
            notificationDto.setContent(userNumberNotificationDto.getContent());
            notificationDto.setUrl(userNumberNotificationDto.getUrl());
            Notification notification = getNotification(notificationDto,senderUserNumber);
            List<String> userCid = cidDao.getCidByUserNumber(userNumbers);
            if(userCid.size()==0 || userNumbers.size()==0){
                return APIResponse.error(ErrorCode.NO_SUCH_USER);
            }
            return sendNotification(userCid.stream().distinct().collect(Collectors.toList()), notification,userNumbers);
        } catch (Exception e) {
            LOGGER.error("发送通知失败",e);
            if (Objects.nonNull(userNumberNotificationDto)) {
                LOGGER.error("DEBUG::::::"+userNumberNotificationDto.toString());
            }
            return APIResponse.error(ErrorCode.LOGIC_ERROR);
        }
    }

    @Override
    public APIResponse heSuanNotice(String token, HeSuanNoticeDto heSuanNoticeDto) {
        try {
            String senderUserNumber = JwtUtils.getUserNumber(token);
            String campus = heSuanNoticeDto.getCampus();
            String percentage = heSuanNoticeDto.getPercentage();
            String type = heSuanNoticeDto.getType();
            List<String> userNumbers = hesuanDao.getUserNumbers(campus, percentage, type);

//            if(Objects.equals(campus, "卫津路") && Objects.equals(percentage, "20%")){
//                userNumbers = hesuanDao.getUserNumbers(heSuanNoticeDto.getType());
//            }else if (Objects.equals(campus, "卫津路") && Objects.equals(percentage, "33%")){
//                userNumbers = hesuanDao.getUserNumbers(heSuanNoticeDto.getType());
//            }else if (Objects.equals(campus, "北洋园") && Objects.equals(percentage, "33%")){
//                userNumbers = hesuanDao.getUserNumbers(heSuanNoticeDto.getType());
//            }else if (Objects.equals(campus, "北洋园") && Objects.equals(percentage, "20%")){
//                userNumbers = hesuanDao.getUserNumbers(heSuanNoticeDto.getType());
//            }
//            System.out.println(userNumbers);
//            return APIResponse.success(userNumbers.toString()+ heSuanNoticeDto);
            HeSuanNotification heSuanNotification = new HeSuanNotification();
            BeanUtils.copyProperties(heSuanNoticeDto, heSuanNotification);
            heSuanNotification.setOperator(senderUserNumber);
            hesuanDao.insertHeSuanNotificationLog(heSuanNotification);

            NotificationDto notificationDto = new NotificationDto();
            notificationDto.setTitle(heSuanNoticeDto.getTitle());
            notificationDto.setContent(heSuanNoticeDto.getContent());
            notificationDto.setUrl(heSuanNoticeDto.getUrl());
            Notification notification = getNotification(notificationDto,senderUserNumber);
            List<String> userCid = cidDao.getCidByUserNumber(userNumbers);
            if(userCid.size()==0 || userNumbers.size()==0){
                return APIResponse.error(ErrorCode.NO_SUCH_USER);
            }
            return sendNotification(userCid.stream().distinct().collect(Collectors.toList()), notification,userNumbers);
        } catch (Exception e) {
            LOGGER.error("发送通知失败",e);
            if (Objects.nonNull(heSuanNoticeDto)) {
                LOGGER.error("DEBUG::::::"+heSuanNoticeDto.toString());
            }
            return APIResponse.error(ErrorCode.LOGIC_ERROR);
        }
    }

    private Notification getNotification(NotificationDto notificationDto, String userNumber){
        Notification notification = new Notification();
        BeanUtils.copyProperties(notificationDto, notification);
        notification.setOperator(userNumber);
        return notification;
    }

    private APIResponse sendNotification(List<String> userCid, Notification notification){
        ApiResult apiResult = GtNotificationUtils.sendNotificationToUsers(userCid,notification);
        if (apiResult.isSuccess()){
            notificationDao.insertNotificationLog(notification,1);
            notificationDao.insertNotification2All(notification);
            return APIResponse.success(null);
        } else {
            notificationDao.insertNotificationLog(notification,0);
            return APIResponse.error("发送失败，请查看个推文档排查错误。"+"错误码: "+apiResult.getCode()+" 错误信息: "+apiResult.getMsg());
        }
    }

    private APIResponse sendNotification(List<String> userCid, Notification notification, List<String> userNumbers){
        ApiResult apiResult = GtNotificationUtils.sendNotificationToUsers(userCid,notification);
        if (apiResult.isSuccess()){
            notificationDao.insertNotificationLog(notification,1);
            notificationDao.insertNotification(notification,userNumbers);
            return APIResponse.success(null);
        } else {
            notificationDao.insertNotificationLog(notification,0);
            return APIResponse.error("发送失败，请查看个推文档排查错误。"+"错误码: "+apiResult.getCode()+" 错误信息: "+apiResult.getMsg());
        }
    }

    /**
     * todo:这是做什么
     * @param stuTypeFilterDto
     * @return
     */
    private StuTypeFilterDto handleFilter(StuTypeFilterDto stuTypeFilterDto){
        if (stuTypeFilterDto.getGrade() != null && stuTypeFilterDto.getGrade().size()>0){
            List<String> grade = stuTypeFilterDto.getGrade();
            StringBuilder range = new StringBuilder();
            for (String s: grade){
                range.append(s.substring(2)).append("|");
            }
            range.deleteCharAt(range.length()-1);
            // ^[0-7][0-2]18[0-9]+
            stuTypeFilterDto.setTmpGrade("^[0-7][0-9](".concat(range.toString())+")[0-9]+");
        }
        return stuTypeFilterDto;
    }
}
