package top.cxjfun.common.web.security.service;

import top.cxjfun.common.datasource.nosql.core.service.NosqlService;
import top.cxjfun.common.web.security.entity.UserTokenCacheInfo;

public interface UserTokenCacheInfoService extends NosqlService<UserTokenCacheInfo> {


    UserTokenCacheInfo validateToken(String token);
    /**
     * 刷新token失效时间
     * @param info
     * @return
     */
    void refreshTokenExpires(UserTokenCacheInfo info);

    /**
     * 构建token
     * @param userTokenCacheInfo
     * @return
     */
    String generateToken(UserTokenCacheInfo userTokenCacheInfo);
}
