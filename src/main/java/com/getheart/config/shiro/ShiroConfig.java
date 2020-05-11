package com.getheart.config.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import lombok.Data;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Json
 * @date 2020-08-11:04
 */

@Configuration
public class ShiroConfig {

    private String hashAlgorithmName = "md5";//加密方式
    private int hashIterations = 2;// 散列次数
    private String loginUrl = "/sys/toLogin";

    /**
     * 声明凭证匹配器
     * @return
     */
    @Bean("credentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher matcher  = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName(hashAlgorithmName);
        matcher.setHashIterations(hashIterations);
        return matcher;
    }
    /**
     * 创建 Realm 对象， 需要自定义
     * @return
     */
    @Bean
    public UserRealm userRealm(@Qualifier("credentialsMatcher") HashedCredentialsMatcher credentialsMatcher){

        UserRealm userRealm = new UserRealm();
        //注入凭证匹配器
        userRealm.setCredentialsMatcher(credentialsMatcher);
        return userRealm;
    }

    /**
     * 创建DafaultWebSecurityMannager
     */
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getdefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联Realm
        securityManager.setRealm(userRealm);

        return securityManager;

    }

    /**
     * shiro过滤器
     * 创建ShiroFilterFactoryBean
     * @param defaultWebManager
     * @return
     * /**
     */
    @Bean
    public ShiroFilterFactoryBean shiroFactory(@Qualifier("securityManager") DefaultWebSecurityManager defaultWebManager){

        ShiroFilterFactoryBean shiroFactoryBean = new ShiroFilterFactoryBean( );
        //设置安全管理器
        shiroFactoryBean.setSecurityManager(defaultWebManager);
        //设置登录路径
        shiroFactoryBean.setLoginUrl(loginUrl);

        /*  添加shrio的内置过滤器
        * anon: 无需认证就可以访问
        * authc: 必须认证才能访问
        * user： 必须拥有 记住我 功能才能用
        * perms: 拥有对某一资源的权限才能访问
        * role ：拥有某个角色才能访问
        */
        Map<String,String> filterMap = new LinkedHashMap<>();
        filterMap.put("/sys/index", "authc");
        filterMap.put("/sys/toLogin","anon");
        filterMap.put("/admin/login","anon");
        filterMap.put("/sys/loginout", "logout");
        //过滤静态资源
        filterMap.put("/images/**","anon");
        filterMap.put("/static/**","anon");
        filterMap.put("/css/**","anon");
        filterMap.put("/page/**","anon");
        filterMap.put("/js/**","anon");
        filterMap.put("/layui/**","anon");
        filterMap.put("/layui_ext/**","anon");

        //其他接口，需要认证才能访问
        filterMap.put("/**", "authc");
        shiroFactoryBean.setFilterChainDefinitionMap(filterMap);

        return shiroFactoryBean;
    }
    /**
     * 配置ShiroSialect，用于thymeleaf和shrio标签配合使用
     * @return
     */
    @Bean
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }
    /**
     * 开启aop注解支持
     * @param
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * 开启aop注解支持
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager")DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }


}
