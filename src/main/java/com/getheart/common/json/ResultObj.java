package com.getheart.common.json;

import com.getheart.common.Constant;

/**
 * @author Json
 * @date 2020-28-11:38
 */

public class ResultObj {

    public static final ResultObj LOGIN_SUCCESS = new ResultObj(Constant.OK,"登录成功！");
    public static final ResultObj LOGIN_ERROR_PASS = new ResultObj(Constant.ERROR,"密码不正确！");
    public static final ResultObj LOGIN_ERROR_NAME = new ResultObj(Constant.ERROR,"用户名不正确！");
    public static final ResultObj LOGIN_ERROR_CODE = new ResultObj(Constant.ERROR,"验证码不正确！！");

    public static final ResultObj DELETE_SUCCESS = new ResultObj(Constant.OK,"删除成功！");
    public static final ResultObj DELETE_FAIL = new ResultObj(Constant.ERROR,"删除失败！");

    public static final ResultObj UPDATE_SUCCESS = new ResultObj(Constant.OK,"更新成功！");
    public static final ResultObj UPDATE_FAIL = new ResultObj(Constant.ERROR,"更新失败！");

    public static final ResultObj ADD_SUCCESS = new ResultObj(Constant.OK,"添加成功！");
    public static final ResultObj ADD_FAIL = new ResultObj(Constant.ERROR,"添加失败！");

    public static final ResultObj RESET_SUCCESS = new ResultObj(Constant.OK,"重置成功！");
    public static final ResultObj RESET_FAIL = new ResultObj(Constant.ERROR,"重置失败！");

    public static final ResultObj DISPATCH_SUCCESS = new ResultObj(Constant.OK,"分配成功！");
    public static final ResultObj DISPATCH_FAIL = new ResultObj(Constant.ERROR,"分配失败！");



    private Integer code;
    private String msg;

    public ResultObj() {
    }

    public ResultObj(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
