package com.getheart.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.getheart.common.DataGridView;
import com.getheart.common.json.ResultObj;
import com.getheart.pojo.Loginfo;
import com.getheart.pojo.Notice;
import com.getheart.pojo.User;
import com.getheart.service.NoticeService;
import com.getheart.util.WebUtil;
import com.getheart.vo.LoginfoVo;
import com.getheart.vo.NoticeVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Json
 * @since 2020-05-08
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    /**
     * 查询公告
     * @param noticeVo
     * @return
     */
    @RequestMapping("loadAllNotice")
    public DataGridView loadAllLoginfo(NoticeVo noticeVo){

        IPage<Notice> page = new Page<>(noticeVo.getPage(),noticeVo.getLimit());
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        //根据登录名查询
        queryWrapper.like(StringUtils.isNotBlank(noticeVo.getTitle()),
                "title",noticeVo.getTitle());
        //根据ip查询
        queryWrapper.like(StringUtils.isNotBlank(noticeVo.getOpername()),
                "opername",noticeVo.getOpername());
        //开始时间
        queryWrapper.ge(noticeVo.getStartTime() != null,
                "createtime",noticeVo.getStartTime());
        queryWrapper.le(noticeVo.getEndTime() != null,
                "createtime",noticeVo.getEndTime());
        //根据登录时间排序
        queryWrapper.orderByDesc("createtime");
        this.noticeService.page(page,queryWrapper);

        return new DataGridView(page.getTotal(),page.getRecords());
    }

    /**
     * 删除公告
     * @param id
     * @return
     */
    @RequestMapping("deleteNotice")
    public ResultObj deleteLoginfo(Integer id){
        try {
            this.noticeService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            return ResultObj.DELETE_FAIL;
        }
    }

    /**
     * 批量删除公告
     */
    @RequestMapping("batchDeleteNotice")
    public ResultObj batchDeleteLoginfo(NoticeVo noticeVo) {
        try {
            Collection<Serializable> idList=new ArrayList<>();
            for (Integer id : noticeVo.getIds()) {
                idList.add(id);
            }
            this.noticeService.removeByIds(idList);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_FAIL;
        }
    }
    /**
     * 添加公告
     * @param noticeVo
     * @return
     */
    @RequestMapping("addNotice")
    public ResultObj addNotice(NoticeVo noticeVo){
        try {
            noticeVo.setCreatetime(new Date());
            User user = (User) WebUtil.getSession().getAttribute("user");
            noticeVo.setOpername(user.getName());
            this.noticeService.saveOrUpdate(noticeVo);
            return ResultObj.ADD_SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            return ResultObj.ADD_FAIL;
        }
    }

    /**
     * 更新公告
     * @param noticeVo
     * @return
     */
    @RequestMapping("updateNotice")
    public ResultObj updateNotice(NoticeVo noticeVo){
        try {
            this.noticeService.updateById(noticeVo);
            return ResultObj.UPDATE_SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            return ResultObj.UPDATE_FAIL;
        }
    }
}

