package top.cxjfun.common.datasource.nosql.db;

import org.springframework.data.redis.core.RedisTemplate;
import top.cxjfun.common.datasource.nosql.core.service.NosqlService;

import java.util.Collection;
import java.util.Map;


public interface RedisNoSqlService<T> extends NosqlService<T> {
    RedisTemplate<String, Object> getRedisTemplate();

    void put(String name, String key, Object value);

    Long pushAll(String name, int position, Collection<Object> list);

}
