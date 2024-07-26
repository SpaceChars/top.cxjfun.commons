package top.cxjfun.common.web.geo;

import cn.hutool.core.io.IoUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GeoAutoConfig {

    @Bean
    public GeoIpSearcher searcher() {
        try {
            // 创建GEO数据库
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("geo/ip2region.xdb");
            return new GeoIpSearcher(null, null, IoUtil.readBytes(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
