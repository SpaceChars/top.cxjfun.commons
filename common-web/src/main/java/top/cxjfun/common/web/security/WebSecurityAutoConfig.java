package top.cxjfun.common.web.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import top.cxjfun.common.web.security.service.impl.UserTokenCacheInfoServiceImpl;

@EnableWebSecurity
@EnableConfigurationProperties(WebSecurityProperties.class)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityAutoConfig {

    public WebSecurityAutoConfig(WebSecurityProperties webSecurityProperties) {
        AuthorizationHelper.properties = webSecurityProperties;
    }

    @Bean
    public UserTokenCacheInfoServiceImpl userCacheInfoService() {
        UserTokenCacheInfoServiceImpl service = new UserTokenCacheInfoServiceImpl();
        AuthorizationHelper.setUserCacheInfoService(service);
        return service;
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(userCacheInfoService());
    }

    ;

    /**
     * 配置安全过滤连
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    @Order(10)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //禁用CSRF
        http.csrf().disable();

        //禁用Session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //禁用表单登录
        http.formLogin().disable();

        //添加Token Filter
        http.addFilterAt(tokenAuthenticationFilter(), BasicAuthenticationFilter.class);

        //配置资源访问策略
        http.authorizeHttpRequests(request -> request.mvcMatchers(AuthorizationHelper.properties.getIgnoreUrl()).permitAll()
                .antMatchers(AuthorizationHelper.properties.getIgnoreUrl()).permitAll()
                .anyRequest().authenticated());

        return http.build();
    }


}
