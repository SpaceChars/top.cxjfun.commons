package top.cxjfun.common.web.security.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import top.cxjfun.common.datasource.nosql.core.annotation.NosqlTable;
import top.cxjfun.common.datasource.nosql.core.annotation.NosqlTableField;
import top.cxjfun.common.datasource.nosql.core.annotation.NosqlTablePrimary;
import top.cxjfun.common.datasource.nosql.core.enums.FieldFill;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
@NosqlTable
public class UserTokenCacheInfo {

    @NosqlTablePrimary
    private String id;

    private String userId;

    //角色
    private List<String> roles;

    //user信息
    private Map<String, Object> userInfo;

    //过期时间
    private Date expiresTime;

    @NosqlTableField(fill = FieldFill.INSERT)
    private Date createTime;

    @NosqlTableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
