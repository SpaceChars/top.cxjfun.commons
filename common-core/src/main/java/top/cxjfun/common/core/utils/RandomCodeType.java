package top.cxjfun.common.core.utils;

import cn.hutool.core.util.ArrayUtil;

public enum RandomCodeType {

    NUMBER(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"}),
    ENGLISH_LETTERS_UPPER(new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"}),
    ENGLISH_LETTERS_LOWER(new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"}),
    SYMBOL(new String[]{".", "&", "/", "+", "-", "*", "^", "%", "!", "_"}),
    ALL(ArrayUtil.addAll(NUMBER.value, ENGLISH_LETTERS_UPPER.value, ENGLISH_LETTERS_LOWER.value, SYMBOL.value));


    RandomCodeType(String[] value) {
        this.value = value;
    }

    private String[] value;

    public String[] getValue() {
        return value;
    }


}
