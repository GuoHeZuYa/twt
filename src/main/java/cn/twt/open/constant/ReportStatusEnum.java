package cn.twt.open.constant;

public enum ReportStatusEnum {

    UN_REPORT(0, "未填报"),
    REPORT(1,"已填报");

    private final Integer code;
    private final String description;

    private ReportStatusEnum(int code, String message){
        this.code = code;
        this.description = message;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
