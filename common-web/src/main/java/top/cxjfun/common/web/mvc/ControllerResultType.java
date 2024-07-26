package top.cxjfun.common.web.mvc;

public enum ControllerResultType {
    SUCCESS(1, "success", "成功"),
    ERROR(2, "error", "错误"),
    WARN(3, "warn", "警告");

    private Integer value;
    private String label;
    private String desc;

    ControllerResultType(Integer value, String label, String desc) {
        this.label = label;
        this.value = value;
        this.desc = desc;
    }
}
