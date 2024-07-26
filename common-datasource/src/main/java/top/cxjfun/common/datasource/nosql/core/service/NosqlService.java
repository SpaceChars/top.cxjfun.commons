package top.cxjfun.common.datasource.nosql.core.service;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface NosqlService<T> {

    default boolean save(T entity) {
        return saveBatch(entity);
    }

    default boolean saveBatch(T... entityList) {
        return saveBatch(Arrays.stream(entityList).collect(Collectors.toList()));
    }

    boolean saveBatch(Collection<T> entityList);

    default boolean remove(T entity) {
        return removeBatch(entity);
    }

    default boolean removeBatch(T... entityList) {
        return removeBatch(Arrays.stream(entityList).collect(Collectors.toList()));
    }

    boolean removeBatch(Collection<T> entityList);

    default boolean removeById(Object id) {
        return removeBatchByIds(id);
    }

    default boolean removeBatchByIds(Object... ids) {
        return removeBatchByIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    boolean removeBatchByIds(Collection<Object> ids);

    default boolean update(T entity) {
        return updateBatch(entity);
    }

    default boolean updateBatch(T... entityList) {
        return updateBatch(Arrays.stream(entityList).collect(Collectors.toList()));
    }

    boolean updateBatch(List<T> entityList);

    List<T> findAll();

    default T findById(Object id) {
        List<T> list = this.findByIds(id);
        return list.size() > 0 ? list.get(0) : null;
    }

    default List<T> findByIds(Object... ids) {
        return findByIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    List<T> findByIds(List<?> ids);

}
