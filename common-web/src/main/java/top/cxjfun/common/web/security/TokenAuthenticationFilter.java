package top.cxjfun.common.web.security;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import top.cxjfun.common.web.security.entity.UserTokenCacheInfo;
import top.cxjfun.common.web.security.service.UserTokenCacheInfoService;
import top.cxjfun.common.web.security.service.impl.UserTokenCacheInfoServiceImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 重写Basic filter
 */

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private WebSecurityProperties webSecurityProperties;

    private final UserTokenCacheInfoService userTokenCacheInfoService;

    public TokenAuthenticationFilter(UserTokenCacheInfoServiceImpl userCacheInfoService) {
        this.userTokenCacheInfoService = userCacheInfoService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        try {
            //验证是否是忽略的请求地址
            String requestURI = request.getRequestURI();
            boolean match = PatternMatchUtils.simpleMatch(webSecurityProperties.getIgnoreUrl(), requestURI);
            if (match) {
                chain.doFilter(request, response);
                return;
            }

            String token = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (!StringUtils.hasText(token)) {
                this.logger.error("Did not process authentication request since failed to find Authorization header");
                chain.doFilter(request, response);
                return;
            }

            //验证token签发签名、过期时间、基础信息
            UserTokenCacheInfo info = userTokenCacheInfoService.validateToken(token);
            if (info == null) {
                this.logger.error("Authorization header validate failed");
                chain.doFilter(request, response);
                return;
            }

            //获取角色信息
            List<String> roles = info.getRoles();
            ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
            if (ObjectUtil.isNotEmpty(roles)) {
                roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(StrUtil.format("ROLE_{}", role))));
            }

            //创建Authentication并添加到上下文中
            Authentication authentication = new UsernamePasswordAuthenticationToken(info.getUserId(), info.getId(), authorities);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);

            //刷新有效时长
            userTokenCacheInfoService.refreshTokenExpires(info);

        } catch (Exception ex) {
            SecurityContextHolder.clearContext();
            chain.doFilter(request, response);
            this.logger.error("Failed to process authentication request", ex);
            return;
        }

        chain.doFilter(request, response);
    }
}
