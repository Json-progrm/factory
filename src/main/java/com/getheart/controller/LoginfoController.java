package com.getheart.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.getheart.common.DataGridView;
import com.getheart.common.json.ResultObj;
import com.getheart.pojo.Loginfo;
import com.getheart.service.LoginfoService;
import com.getheart.vo.LoginfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.crypto.interfaces.PBEKey;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>
 *  日志管理前端控制器
 * </p>
 *
 * @author Json
 * @since 2020-05-08
 */
@RestController
@RequestMapping("/loginfo")
public class LoginfoController {

    @Autowired
    private LoginfoService loginfoService;

    /**
     * 日志管理查询
     * @param loginfoVo
     * @return
     */
    @RequestMapping("loadAllLoginfo")
    public DataGridView loadAllLoginfo(LoginfoVo loginfoVo){

        IPage<Loginfo> page = new Page<>(loginfoVo.getPage(),loginfoVo.getLimit());
        QueryWrapper<Loginfo> queryWrapper = new QueryWrapper<>();
        //根据登录名查询
        queryWrapper.like(StringUtils.isNotBlank(loginfoVo.getLoginname()),
                  "loginname",loginfoVo.getLoginname());
        //根据ip查询
        queryWrapper.like(StringUtils.isNotBlank(loginfoVo.getLoginip()),
                "loginip",loginfoVo.getLoginip());
        //开始时间
        queryWrapper.ge(loginfoVo.getStartTime() != null,
                "logintime",loginfoVo.getStartTime());
        queryWrapper.le(loginfoVo.getEndTime() != null,
                "logintime",loginfoVo.getEndTime());
        //根据登录时间排序
        queryWrapper.orderByDesc("logintime");
        this.loginfoService.page(page, queryWrapper);

        return new DataGridView(page.getTotal(),page.getRecords());
    }

    /**
     * 删除日志
     * @param id
     * @return
     */
    @RequestMapping("deleteLoginfo")
    public ResultObj deleteLoginfo(Integer id){
        try {
            this.loginfoService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            return ResultObj.DELETE_FAIL;
        }
    }
    /**
     * 批量删除
     */
    @RequestMapping("batchDeleteLoginfo")
    public ResultObj batchDeleteLoginfo(LoginfoVo loginfoVo) {
        try {
            Collection<Serializable> idList=new ArrayList<>();
            for (Integer id : loginfoVo.getIds()) {
                idList.add(id);
            }
            this.loginfoService.removeByIds(idList);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_FAIL;
        }
    }
}

