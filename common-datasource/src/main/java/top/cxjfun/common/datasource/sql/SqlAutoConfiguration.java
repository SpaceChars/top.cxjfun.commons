package top.cxjfun.common.datasource.sql;

import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@MapperScans({@MapperScan("top.cxjfun.**.mapper"), @MapperScan("classpath*:/mapper/**/*.xml")})
public class SqlAutoConfiguration {

    @Bean
    public ApplicationListener sqlServieListener() {
        return new SqlServieListener();
    }

    @Bean
    public FiledsFillMetaObjectHandler filedsFillMetaObjectHandler() {
        return new FiledsFillMetaObjectHandler();
    }

    @Bean
    public VendorDatabaseIdProvider vendorDatabaseIdProvider() {
        Properties properties = new Properties();
        properties.setProperty("MySQL", "mysql");
        properties.setProperty("ORACLE", "oracle");

        VendorDatabaseIdProvider provider = new VendorDatabaseIdProvider();
        provider.setProperties(properties);
        return provider;
    }
}
