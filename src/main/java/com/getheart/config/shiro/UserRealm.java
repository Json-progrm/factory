package com.getheart.config.shiro;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.getheart.common.ActiverUser;
import com.getheart.pojo.User;
import com.getheart.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

/**
 * @author Json
 * @date 2020-08-11:22
 */
public class UserRealm extends AuthorizingRealm {

    Logger logger = LoggerFactory.getLogger(AuthorizingRealm.class);

    @Autowired
    @Lazy
    private UserService userService;

    /**
     * 2、执行授权
     *
     * @param principal
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
      return null;
    }

    /**
     * 1、执行认证
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        logger.info("--------------->执行认证");

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("loginname",token.getPrincipal().toString());
        User user = userService.getOne(queryWrapper);

        if (user != null){
            ActiverUser activerUser = new ActiverUser();

            activerUser.setUser(user);

            ByteSource credentilsSalt = ByteSource.Util.bytes(user.getSalt());
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(activerUser,
                    user.getPwd(),//数据库中的密码
                    credentilsSalt,
                    this.getName());
            return info;
        }
        return null;
    }

}
