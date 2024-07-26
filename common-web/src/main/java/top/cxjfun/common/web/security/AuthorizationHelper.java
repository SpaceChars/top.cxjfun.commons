package top.cxjfun.common.web.security;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import top.cxjfun.common.web.security.entity.UserTokenCacheInfo;
import top.cxjfun.common.web.security.service.impl.UserTokenCacheInfoServiceImpl;

import java.util.List;

public class AuthorizationHelper {

    /**
     * security properties 配置信息
     */
    public static WebSecurityProperties properties;

    private static UserTokenCacheInfoServiceImpl service;

    /**
     * 获取当前人的权限信息
     *
     * @returnr
     */
    public static Authentication getCurrntAuthorization() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取当前登录用户id
     *
     * @return
     */
    public static String getCurrentUserId() {
        Authentication authorization = AuthorizationHelper.getCurrntAuthorization();
        return ObjectUtil.isNotEmpty(authorization)?authorization.getPrincipal().toString():"";
    }

    /**
     * 获取当前登录用户id
     *
     * @return
     */
    public static String getCurrentUserTokenId() {
        Authentication authorization = AuthorizationHelper.getCurrntAuthorization();
        return ObjectUtil.isNotEmpty(authorization)?authorization.getCredentials().toString():"";
    }

    /**
     * 刷新角色信息
     *
     * @param roles
     */
    public static void refreshRoles(List<String> roles) {
        String tokenId = getCurrentUserTokenId();
        Assert.notNull(tokenId, "获取token令牌失败！");

        if (ObjectUtil.isNotEmpty(roles)) {
            UserTokenCacheInfo info = AuthorizationHelper.service.findById(tokenId);
            info.setRoles(roles);
        }
    }


    public static void setUserCacheInfoService(UserTokenCacheInfoServiceImpl service) {
        AuthorizationHelper.service = service;
    }
}
