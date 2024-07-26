package top.cxjfun.common.datasource.nosql.db;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;
import top.cxjfun.common.datasource.nosql.core.NosqlHelper;
import top.cxjfun.common.datasource.nosql.core.NosqlMetaObject;
import top.cxjfun.common.datasource.nosql.core.service.NosqlServiceImpl;

import java.util.*;
import java.util.stream.Collectors;

public class RedisNosqlServiceImpl<T> extends NosqlServiceImpl<T> implements RedisNoSqlService<T> {

    private final String TABLE_ROW_NAME = "NOSQL_REDIS_TABLE_ROWS";

    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public RedisTemplate<String, Object> getRedisTemplate() {
        if (ObjectUtil.isNotEmpty(redisTemplate)) {
            return redisTemplate;
        }

        this.redisTemplate = (RedisTemplate<String, Object>) this.getBeanFactory().getBean("redisNosqlTemplate");
        if (ObjectUtils.isEmpty(this.redisTemplate)) {
            throw new RuntimeException("The RedisTemplate instance does not exist!");
        }
        return redisTemplate;
    }

    /**
     * 获取MetaObject id
     *
     * @param metaObjectList 元信息
     * @return 主键值
     */
    private List<Object> getIdsByMetaObjects(List<NosqlMetaObject> metaObjectList) {
        return metaObjectList.stream().map(NosqlMetaObject::getPrimaryValue).collect(Collectors.toList());
    }

    /**
     * 转化为MetaObject
     *
     * @param entityList 实体对象
     * @return 元对象
     */
    private List<NosqlMetaObject> transformToMetaObject(Collection<T> entityList) {
        return entityList.stream().map(item -> {
            NosqlMetaObject metaObject = this.getMetaObject(item);
            //执行填充策略
            metaObject.fillRow(this.getMetaObjectHandler());
            return metaObject;
        }).collect(Collectors.toList());
    }

    /**
     * Hash putIfAbsent
     *
     * @param name  key
     * @param key   hash key
     * @param value value
     */
    @Override
    public void put(String name, String key, Object value) {
        HashOperations<String, Object, Object> opsedForHash = this.getRedisTemplate().opsForHash();
        if(ObjectUtil.isNotEmpty(opsedForHash.get(name, key))){
            opsedForHash.put(name, key, value);
        }
    }

    /**
     * Hash multiGet
     *
     * @param name   key
     * @param keys   hash keys
     * @param mClass 实体类
     */
    private <M> Collection<M> multiGet(String name, Collection<Object> keys, Class<M> mClass) {
        List<Object> list = this.getRedisTemplate().opsForHash().multiGet(name, keys);
        return list.stream().filter(Objects::nonNull).map(raw -> (M) raw).collect(Collectors.toList());
    }


    /**
     * List push
     *
     * @param name     key
     * @param position 开始位置 小于0在右边插入，大于等于0在左边插入
     * @param list     value
     */
    @Override
    public Long pushAll(String name, int position, Collection<Object> list) {
        if (position > 0) {
            return this.getRedisTemplate().opsForList().rightPushAll(name, list);
        } else {
            return this.getRedisTemplate().opsForList().leftPushAll(name, list);
        }
    }

    @Override
    public boolean saveBatch(Collection<T> entityList) {

        List<NosqlMetaObject> metaObjects = transformToMetaObject(entityList);
        Map<String, Object> objectMap = metaObjects.stream().collect(Collectors.toMap(metaObject -> (String) metaObject.getPrimaryValue(), NosqlMetaObject::getOriginalObject, (key1, key2) -> key1));
        this.getRedisTemplate().opsForHash().putAll(this.TABLE_ROW_NAME, objectMap);

        Long length = this.pushAll(this.getTableInfo().getTableName(), 1, this.getIdsByMetaObjects(metaObjects));
        return length >= metaObjects.size();
    }

    @Override
    public boolean removeBatch(Collection<T> entityList) {
        List<NosqlMetaObject> metaObjects = this.transformToMetaObject(entityList);
        return this.removeBatchByIds(this.getIdsByMetaObjects(metaObjects));
    }

    @Override
    public boolean removeBatchByIds(Collection<Object> ids) {
        //删除数据
        Long delete = this.getRedisTemplate().opsForHash().delete(this.TABLE_ROW_NAME, ids.toArray());
        //删除表关联数据
        ids.forEach(id -> this.getRedisTemplate().opsForList().remove(this.getTableInfo().getTableName(), 0, id));
        return delete >= ids.size();
    }

    @Override
    public boolean updateBatch(List<T> entityList) {
        List<NosqlMetaObject> metaObjects = this.transformToMetaObject(entityList);

        List<Object> range = this.getRedisTemplate().opsForList().range(this.getTableInfo().getTableName(), 0, -1);
        if (ObjectUtil.isNotEmpty(range)) {
            metaObjects.forEach(metaObject -> {
                if (range.contains(metaObject.getPrimaryValue())) {
                    this.put(this.TABLE_ROW_NAME, metaObject.getPrimaryValue().toString(), metaObject.getOriginalObject());
                }
            });
        }
        return true;
    }

    @Override
    public List<T> findAll() {
        return this.findByIds(this.getRedisTemplate().opsForList().range(this.getTableInfo().getTableName(), 0, -1));
    }

    @Override
    public List<T> findByIds(List<?> ids) {
        List<Object> range = this.getRedisTemplate().opsForList().range(this.getTableInfo().getTableName(), 0, -1);
        if (ObjectUtil.isEmpty(range)) {
            return new ArrayList<>();
        }
        return (List<T>) this.multiGet(this.TABLE_ROW_NAME, ids.stream().filter(id -> range.stream().anyMatch(el -> el.equals(id))).collect(Collectors.toList()), NosqlHelper.getTypeClass(this.entityClass));
    }
}
