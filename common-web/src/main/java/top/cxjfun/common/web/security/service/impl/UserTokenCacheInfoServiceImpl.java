package top.cxjfun.common.web.security.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSigner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import top.cxjfun.common.web.security.jwt.JWTSmSignerUtil;
import top.cxjfun.common.datasource.nosql.db.RedisNosqlServiceImpl;
import top.cxjfun.common.web.security.entity.UserTokenCacheInfo;
import top.cxjfun.common.web.security.WebSecurityProperties;
import top.cxjfun.common.web.security.service.UserTokenCacheInfoService;

import java.nio.charset.StandardCharsets;
import java.util.Date;


public class UserTokenCacheInfoServiceImpl extends RedisNosqlServiceImpl<UserTokenCacheInfo> implements UserTokenCacheInfoService {

    @Autowired
    private WebSecurityProperties webSecurityProperties;

    private static final String CACHE_ID_KEY = "cache_id";


    /**
     * @param token
     * @return 用户token信息
     */
    @Override
    public UserTokenCacheInfo validateToken(String token) {
        JWT jwt = JWT.of(token);
        //验证签名
        JWTValidator.of(jwt).validateAlgorithm(getSigner());
        return validateExpires(jwt.getPayload());
    }

    /**
     * 获取签名
     *
     * @return
     */
    private JWTSigner getSigner() {
        return JWTSmSignerUtil.sm4(webSecurityProperties.getTokenSignerKey().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 验证失效日期
     *
     * @param payload
     * @return
     */
    private UserTokenCacheInfo validateExpires(JWTPayload payload) {
        Object cacheId = payload.getClaim(CACHE_ID_KEY);

        if (ObjectUtil.isEmpty(cacheId)) {
            throw new ValidateException("The {} payload is must be", new Object[]{CACHE_ID_KEY});
        }

        Date now = DateUtil.date();
        //生效时间
        Date dateToCheck = payload.getClaimsJson().getDate(JWTPayload.NOT_BEFORE);
        if (ObjectUtil.isNotEmpty(dateToCheck) && dateToCheck.after(now)) {
            throw new ValidateException("'{}':[{}] is after now:[{}]", new Object[]{JWTPayload.NOT_BEFORE, DateUtil.date(dateToCheck), DateUtil.date((Date) now)});
        }

        //获取用户token信息
        UserTokenCacheInfo info = this.findById(cacheId);
        if (ObjectUtil.isEmpty(info)) {
            throw new ValidateException("Token info is empty");
        }

        //失效时间
        Date expiresTime = info.getExpiresTime();
        if (expiresTime.before(now)) {
            throw new ValidateException("'{}':[{}] is before now:[{}]", new Object[]{JWTPayload.EXPIRES_AT, DateUtil.date(expiresTime), DateUtil.date((Date) now)});
        }

        return info;
    }

    @Override
    public void refreshTokenExpires(UserTokenCacheInfo info) {
        info.setExpiresTime(DateUtil.offsetMillisecond(info.getExpiresTime(), webSecurityProperties.getTokenExpires()));
        this.update(info);
    }

    @Override
    public String generateToken(UserTokenCacheInfo cacheInfo) {
        Assert.notNull(cacheInfo, "userCacheInfo is null");

        Date now = DateUtil.date();

        //更新时效日期
        cacheInfo.setExpiresTime(DateUtil.offsetMillisecond(now, webSecurityProperties.getTokenExpires()));

        if (ObjectUtil.isEmpty(cacheInfo.getId())) this.save(cacheInfo);

        //基础信息
        return JWT.create()
                .setSigner(getSigner())
                .setNotBefore(DateUtil.offsetMillisecond(now, webSecurityProperties.getTokenNotBefore()))
                .setIssuedAt(DateUtil.date())
                .setPayload(CACHE_ID_KEY, cacheInfo.getId())
                .sign();
    }
}
