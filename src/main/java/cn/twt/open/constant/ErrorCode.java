package cn.twt.open.constant;

/**
 * @author Lino
 */

public enum ErrorCode {
    //错误码枚举
    OK(0,"成功"),
    TOKEN_ERROR(40001, "no token detected, please give token"),
    NO_SUCH_USER(40002, "该(类)用户不存在"),
    PERMISSION_DENIED(40003, "权限不足，拒绝访问"),
    PASSWORD_ERROR(40004, "用户名或密码错误"),
    TOKEN_EXPIRED(40005, "token失效，请重新登录"),
    RESOURCE_MISMATCH(40006, "未找到资源或请求参数类型不符要求"),
    PLEASE_LOGIN(40007, "请登录"),
    DATABASE_ERROR(50001,"数据库错误"),
    LOGIC_ERROR(50002,"逻辑错误或数据库错误"),
    NO_SUCH_URL(50003, "未绑定的url，请联系管理员"),
    ERROR_KEY_SECRET(50004, "错误的app_key或app_secret"),
    ID_NUMBER_NOT_MATCH(50005,"学号和身份证号不匹配"),
    NICKNAME_AND_EMAIL_DUPLICATED(50006,"用户名和邮箱已存在"),
    NICKNAME_DUPLICATED(50007,"该昵称已存在,请尝试更换其他昵称"),
    EMAIL_DUPLICATED(50008,"邮箱已存在"),
    NOT_A_PHONE(50009,"手机号码无效"),
    SEND_MSG_FAILED(50010, "发送失败"),
    VERIFICATION_FAILED(50011, "验证失败"),
    NOT_A_EMAIL_OR_PHONE(50012,"电子邮件或手机号格式不规范"),
    EMAIL_OR_PHONE_DUPLICATED(50013,"电子邮件或手机号重复"),
    PHONE_DUPLICATED(50014,"手机号已存在，如果已注册新账号，则无须注册或升级，请用相关学号尝试登录"),
    UPGRADE_FAILED(50015,"升级失败，目标升级账号信息不存在"),
    NO_SUCH_DEPARTMENT(50016,"无此学院"),
    HAVE_UNPASSED_APPLY(50017, "有审核中的申请，无法添加新申请"),
    NOT_A_EMAIL(50018, "电子邮件非法"),
    ILEGAL_NICKNAME(50019, "用户名中含有非法字符"),
    NICKNAME_TOO_LONG(50020, "用户名过长"),
    USER_EXIST(50021, "该学号所属用户已注册过"),
    USER_NOT_IN_SCHOOL(50022, "该身份证号未在系统中登记"),
    USERINFO_EXIST(50023, "该用户信息已经在数据库中登记"),
    RETURN_SCHOOL_SUBMIT_ERROR(50024, "提交失败"),
    NOT_REPEAT_SUBMIT(50025,"请勿短时间内重复点击提交"),
    IMAGE_ILLEGAL(50026,"图片上传失败：图片格式不合法或图片为空"),
    FIELD_EMPTY(50027,"不能有字段为空"),
    ERROR_NUMBER(50028,"错误的学号");


    private int code;
    private String message;

    private ErrorCode(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
