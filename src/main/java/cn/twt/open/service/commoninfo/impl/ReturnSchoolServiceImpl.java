package cn.twt.open.service.commoninfo.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.twt.open.cache.redis.RedisCacheManager;
import cn.twt.open.constant.ErrorCode;
import cn.twt.open.constant.ReportStatusEnum;
import cn.twt.open.dao.commoninfo.ReturnSchoolDao;
import cn.twt.open.dao.user.AssistantStudentRelationDao;
import cn.twt.open.dao.user.UserDao;
import cn.twt.open.dto.commoninfo.ReportedStudentDto;
import cn.twt.open.dto.commoninfo.ReturnSchoolInfoDto;
import cn.twt.open.dto.user.UserDto;
import cn.twt.open.exception.FileEmptyException;
import cn.twt.open.pojo.commoninfo.HealthInfoExport;
import cn.twt.open.pojo.commoninfo.ReturnSchoolInfo;
import cn.twt.open.pojo.user.User;
import cn.twt.open.service.commoninfo.ReturnSchoolService;
import cn.twt.open.service.user.UserService;
import cn.twt.open.utils.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author Lino
 */
@Service
public class ReturnSchoolServiceImpl implements ReturnSchoolService {

    private static final ReentrantLock lock = new ReentrantLock();

    private static final String ALL = "*";

    private static final String REDIS_KEY_PREFIX = "healthInfo-";

    @Resource
    ReturnSchoolDao returnSchoolDao;

    @Resource
    AssistantStudentRelationDao assistantStudentRelationDao;

    @Resource
    UserDao userDao;

    @Resource
    UserService userService;

    @Resource
    CacheUtils cacheUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public APIResponse collectInformation(String token, ReturnSchoolInfoDto returnSchoolInfoDto) {
        String userNumber = JwtUtils.getUserNumber(token);
        lock.lock();
        try {
            APIResponse res = userService.fetchSingleUser(userNumber);
            if (res.getError_code() == NumberUtils.INTEGER_ZERO){
                UserDto userDto = (UserDto) res.getResult();
                try {
                    ReturnSchoolInfo returnSchoolInfo = ReturnSchoolInfo.toReturnSchoolInfo(userDto, returnSchoolInfoDto);

                    if (Objects.isNull(returnSchoolInfo)){
                        return APIResponse.error(ErrorCode.FIELD_EMPTY);
                    }
                    returnSchoolDao.insertOneRecord(returnSchoolInfo);
                    return APIResponse.success(null);
                } catch (FileEmptyException e){
                    return APIResponse.error(ErrorCode.IMAGE_ILLEGAL);
                }
            } else {
                return APIResponse.error(ErrorCode.RETURN_SCHOOL_SUBMIT_ERROR);
            }
        } finally {
            lock.unlock();
            cacheUtils.cacheDel(REDIS_KEY_PREFIX+userNumber);
        }
    }

    @Override
    public APIResponse getInfo(String token) {
        try {
            String userNumber = JwtUtils.getUserNumber(token);
            List data = cacheUtils.cacheGet(REDIS_KEY_PREFIX+userNumber, List.class);
            if (Objects.nonNull(data)) {
                System.out.println("cache");
                return APIResponse.success(data);
            }
            data = returnSchoolDao.fetchRecordByUserNumber(userNumber);
            if (CollectionUtils.isNotEmpty(data)) {
                cacheUtils.cacheSet(REDIS_KEY_PREFIX+userNumber, data, 60*60);
            }
            return APIResponse.success(data);
        } catch (Exception e) {
            LOGGER.error("exception", e);
            return APIResponse.error(ErrorCode.LOGIC_ERROR);
        }
    }

    @Override
    public APIResponse todayReportStatus(String token) {
        try {
            String userNumber = JwtUtils.getUserNumber(token);
            ReturnSchoolInfo data = returnSchoolDao.fetchLatestRecordByUserNumber(userNumber);
            if (Objects.isNull(data)){
                return APIResponse.success(ReportStatusEnum.UN_REPORT.getCode());
            }
            System.out.println(data);
            if (checkReportStatus(data.getUploadAt().getTime())){
                return APIResponse.success(ReportStatusEnum.REPORT.getCode());
            } else {
                return APIResponse.success(ReportStatusEnum.UN_REPORT.getCode());
            }
        } catch (Exception e){
            LOGGER.error("exception", e);
            return APIResponse.error(ErrorCode.LOGIC_ERROR);
        }
    }

    @Override
    public APIResponse getStudentsReportByAssistant(String token, String date) {
        try {
            String assistantNumber = JwtUtils.getUserNumber(token);
            List<ReportedStudentDto> allStudents = getAllStudentAboutHealthReport(assistantNumber, date);
            return APIResponse.success(allStudents);
        } catch (Exception e) {
            LOGGER.error("ReturnSchoolServiceImpl getStudentsReportByAssistant error",e);
            return APIResponse.error(ErrorCode.LOGIC_ERROR);
        }
    }

    @Override
    public void exportHealthInfoExcel(String token, String start, String end, HttpServletResponse response) {
        try {
            String userNumber = JwtUtils.getUserNumber(token);
            List<HealthInfoExport> exportData = Lists.newArrayList();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date s = simpleDateFormat.parse(start);
            Date e = simpleDateFormat.parse(end);
            long sTimestamp = s.getTime();
            long eTimestamp = e.getTime();
            for (long i = sTimestamp; i <= eTimestamp; i += DateUtils.ONE_DAY) {
                exportData.addAll(getExportHealthInfo(userNumber, i));
            }
            String date = simpleDateFormat.format(new Date());
            String filename = "学生健康信息填报情况-".concat(date);
            ExcelUtils.exportExcel(exportData, HealthInfoExport.class, filename, response);
        } catch (Exception e) {
            LOGGER.error("export failed", e);
        }

    }

    /**
     * 检查填报状态
     * @return
     */
    private boolean checkReportStatus(long timestamp){
        long timeInternal = DateUtils.getTodayZeroTimestamp()-timestamp;
        return timeInternal > 0 && timeInternal < DateUtils.ONE_DAY;
    }

    public List<ReportedStudentDto> getAllStudentAboutHealthReport(String assistantNumber, String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date time = simpleDateFormat.parse(date);
        Timestamp startTime = new Timestamp(time.getTime());
        Timestamp endTime = new Timestamp(time.getTime()+DateUtils.ONE_DAY);
        LOGGER.info("start time: {}", startTime);
        LOGGER.info("end time: {}", endTime);
        List<String> managedStudentUserNumber = assistantStudentRelationDao.getManagedStudentsByAssistantUserNumber(assistantNumber);
        // 超管，所有人
        if (managedStudentUserNumber.contains(ALL)) {
            managedStudentUserNumber = assistantStudentRelationDao.getAllUserNumber();
            LOGGER.info("all student number: {}", managedStudentUserNumber.size());
        }

        if (CollectionUtils.isEmpty(managedStudentUserNumber)){
            LOGGER.info("Assistant managed no student, assistant user number: {}", assistantNumber);
            return Lists.newArrayList();
        }
        List<ReturnSchoolInfo> currentDateStudents = returnSchoolDao.getRecordByUserNumberListAndDate(managedStudentUserNumber, startTime, endTime);

        List<String> currentDateStudentsUserNumber = currentDateStudents.stream().map(ReturnSchoolInfo::getUserNumber).collect(Collectors.toList());
        LOGGER.info("Assistant managed {} student, assistant user number: {}", managedStudentUserNumber.size(), assistantNumber);
        List<User> users = userDao.batchQueryUserBaseInfo(managedStudentUserNumber);
        Map<String, User> userMap = Maps.newConcurrentMap();
        users.forEach(user -> userMap.put(user.getUserNumber(), user));
        List<ReportedStudentDto> allStudents = currentDateStudents
                .stream()
                .map(returnSchoolInfo -> {
                    User user = Optional.ofNullable(userMap.get(returnSchoolInfo.getUserNumber())).orElse(new User());
                    // 逻辑上user不可能为null
                    return ReportedStudentDto
                            .builder()
                            .userNumber(returnSchoolInfo.getUserNumber())
                            .name(user.getRealname())
                            .temperature(returnSchoolInfo.getTemperature())
                            .address(returnSchoolInfo.getAddress())
                            .reportStatus(ReportStatusEnum.REPORT.getDescription())
                            .healthCodeUrl(returnSchoolInfo.getHealthCodeUrl())
                            .travelCodeUrl(returnSchoolInfo.getTravelCodeUrl())
                            .uploadAt(returnSchoolInfo.getUploadAt())
                            .build();
                }).collect(Collectors.toList());
        // 把没填报的添加进来
        allStudents.addAll(managedStudentUserNumber
                .stream()
                .filter(userNumber -> !currentDateStudentsUserNumber.contains(userNumber))
                .map(userNumber -> {
                    User user = Optional.ofNullable(userMap.get(userNumber)).orElse(new User());
                    return ReportedStudentDto
                            .builder()
                            .userNumber(userNumber)
                            .name(user.getRealname())
                            .reportStatus(ReportStatusEnum.UN_REPORT.getDescription())
                            .uploadAt(null)
                            .build();
                }).collect(Collectors.toList()));
        return allStudents;
    }

    public List<HealthInfoExport> getExportHealthInfo(String assistantNumber, long dateTimestamp) throws ParseException {
        Timestamp startTime = new Timestamp(dateTimestamp);
        Timestamp endTime = new Timestamp(dateTimestamp+DateUtils.ONE_DAY);
        LOGGER.info("export start time: {}", startTime);
        LOGGER.info("export end time: {}", endTime);
        List<String> managedStudentUserNumber = assistantStudentRelationDao.getManagedStudentsByAssistantUserNumber(assistantNumber);
        // 超管，所有人
        if (managedStudentUserNumber.contains(ALL)) {
            managedStudentUserNumber = assistantStudentRelationDao.getAllUserNumber();
            LOGGER.info("all student number: {}", managedStudentUserNumber.size());
        }

        if (CollectionUtils.isEmpty(managedStudentUserNumber)){
            LOGGER.info("Assistant managed no student, assistant user number: {}", assistantNumber);
            return Lists.newArrayList();
        }
        List<ReturnSchoolInfo> currentDateStudents = returnSchoolDao.getRecordByUserNumberListAndDate(managedStudentUserNumber, startTime, endTime);

        List<String> currentDateStudentsUserNumber = currentDateStudents.stream().map(ReturnSchoolInfo::getUserNumber).collect(Collectors.toList());
        LOGGER.info("Assistant managed {} student, assistant user number: {}", managedStudentUserNumber.size(), assistantNumber);
        List<User> users = userDao.batchQueryUserBaseInfo(managedStudentUserNumber);
        Map<String, User> userMap = Maps.newConcurrentMap();
        users.forEach(user -> userMap.put(user.getUserNumber(), user));
        List<HealthInfoExport> allStudents = currentDateStudents
                .stream()
                .map(returnSchoolInfo -> {
                    User user = Optional.ofNullable(userMap.get(returnSchoolInfo.getUserNumber())).orElse(new User());
                    // 逻辑上user不可能为null
                    return HealthInfoExport
                            .builder()
                            .userNumber(returnSchoolInfo.getUserNumber())
                            .realname(user.getRealname())
                            .department(returnSchoolInfo.getDepartment())
                            .major(returnSchoolInfo.getMajor())
                            .provinceName(returnSchoolInfo.getProvinceName())
                            .cityName(returnSchoolInfo.getCityName())
                            .regionName(returnSchoolInfo.getRegionName())
                            .temperature(returnSchoolInfo.getTemperature())
                            .uploadAt(returnSchoolInfo.getUploadAt())
                            .build();
                }).collect(Collectors.toList());
        // 把没填报的添加进来
        allStudents.addAll(managedStudentUserNumber
                .stream()
                .filter(userNumber -> !currentDateStudentsUserNumber.contains(userNumber))
                .map(userNumber -> {
                    User user = Optional.ofNullable(userMap.get(userNumber)).orElse(new User());
                    return HealthInfoExport
                            .builder()
                            .userNumber(userNumber)
                            .realname(user.getRealname())
                            .uploadAt(null)
                            .build();
                }).collect(Collectors.toList()));
        return allStudents;
    }
}
