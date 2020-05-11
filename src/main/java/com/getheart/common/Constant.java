package com.getheart.common;

/**
 * @author Json
 * @date 2020-28-12:08
 */
public interface Constant {

    /**
     * 状态码
     */
    Integer OK = 200;
    Integer ERROR = -1;
    /**
     * 菜单权限类型
     */
    String TYPE_MENU = "menu";
    String TYPE_PERMISSION = "permission";

    /**
     * 是否为可用状态
     */
    Integer AVAILABLE_TURE = 1;
    Integer AVAILABLE_FALSE = 0;

    /**
     * 用户类型
     */
    Integer USER_TYPE_SUPER = 0;//超级管理员
    Integer USER_TYPE_NORMAL = 1;//普通用户
    /**
     * 展开类型
     */
    Integer OPEN_TRUE = 1;
    Integer OPEN_FALSE = 0;

    String USER_DEFAULT_PWD = "123456";
}
