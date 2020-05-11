package com.getheart.controller;

import com.getheart.common.ActiverUser;
import com.getheart.common.json.ResultObj;
import com.getheart.pojo.Loginfo;
import com.getheart.service.LoginfoService;
import com.getheart.util.WebUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author Json
 * @date 2020-21-16:35
 */
@RestController
@RequestMapping("admin")
public class loginController {

    @Autowired
    private LoginfoService loginfoService;

    /**
     * 实现登录操作
     * @param loginname
     * @param pwd
     * @return
     */
    @RequestMapping("login")
    public ResultObj login(String loginname, String pwd){

        UsernamePasswordToken token = new UsernamePasswordToken(loginname, pwd);

        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            ActiverUser activerUser = (ActiverUser)subject.getPrincipal();
            WebUtil.getSession().setAttribute("user", activerUser.getUser());

            //记录登录日志
            Loginfo loginfo = new Loginfo();
            loginfo.setLoginname(activerUser.getUser().getName()+"-"+activerUser.getUser().getLoginname());
            loginfo.setLoginip(WebUtil.getRequest().getRemoteAddr());
            loginfo.setLogintime(new Date());
            loginfoService.save(loginfo);
            return ResultObj.LOGIN_SUCCESS;
        }catch (UnknownAccountException e){//用户名不正确
            return ResultObj.LOGIN_ERROR_NAME;
        }catch (IncorrectCredentialsException e){//密码不正确
            return ResultObj.LOGIN_ERROR_PASS;
        }
    }
}
