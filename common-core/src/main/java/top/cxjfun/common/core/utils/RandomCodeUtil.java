package top.cxjfun.common.core.utils;

import cn.hutool.core.util.RandomUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RandomCodeUtil {

    /**
     * 随机获取指定个数、指定类型的字符集
     * @param count
     * @param type
     * @return
     */
    public static List<String> random(int count,RandomCodeType ...type){

        List<String> chars=new ArrayList<>();
        Arrays.stream(type).forEach(t->{
            chars.addAll(Arrays.stream(t.getValue()).collect(Collectors.toList()));
        });

        return RandomUtil.randomEles(chars, count);
    }
}
