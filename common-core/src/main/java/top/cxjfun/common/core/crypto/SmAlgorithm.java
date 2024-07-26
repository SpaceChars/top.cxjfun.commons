package top.cxjfun.common.core.crypto;

public enum SmAlgorithm {

    SM2("SM2"),
    SM3("HmacSM3"),
    SM4("SM4");

    private final String value;

    private SmAlgorithm(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
