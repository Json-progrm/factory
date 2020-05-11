package com.getheart.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.getheart.common.Constant;
import com.getheart.common.DataGridView;
import com.getheart.common.json.ResultObj;
import com.getheart.common.json.TreeNode;
import com.getheart.pojo.Dept;
import com.getheart.pojo.Notice;
import com.getheart.pojo.User;
import com.getheart.service.DeptService;
import com.getheart.util.WebUtil;
import com.getheart.vo.DeptVo;
import com.getheart.vo.NoticeVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Json
 * @since 2020-05-09
 */
@RestController
@RequestMapping("/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    /**
     * 加载部门管理左边的部门树的json
     *
     * @return
     */
    @RequestMapping("loadDeptManagerLeftTreeJson")
    public DataGridView loadDeptManagerLeftTreeJson(DeptVo deptVo) {

        QueryWrapper<Dept> querywrapper = new QueryWrapper<>();
        List<Dept> list = this.deptService.list(querywrapper);

        List<TreeNode> treeNodes = new ArrayList<>();
        for (Dept dept : list) {
            Integer id = dept.getId();
            Integer pid = dept.getPid();
            String title = dept.getTitle();
            Boolean spread = dept.getOpen().equals(Constant.OPEN_TRUE) ? true : false;
            treeNodes.add(new TreeNode(id, pid, title, spread));
        }
        return new DataGridView(treeNodes);
    }

    /**
     * 添加部门
     *
     * @param deptVo
     * @return
     */
    @RequestMapping("addDept")
    public ResultObj addNotice(DeptVo deptVo) {
        try {
            deptVo.setCreatetime(new Date());
            this.deptService.save(deptVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_FAIL;
        }
    }

    /**
     * 删除公告
     *
     * @param id
     * @return
     */
    @RequestMapping("deleteDept")
    public ResultObj deleteDept(Integer id) {
        try {
            this.deptService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_FAIL;
        }
    }

    /**
     * 更新部门信息
     *
     * @param deptVo
     * @return
     */
    @RequestMapping("updateDept")
    public ResultObj updateDept(DeptVo deptVo) {
        try {
            this.deptService.updateById(deptVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_FAIL;
        }
    }

    /**
     * 查询部门
     *
     * @return
     */
    @RequestMapping("loadAllDept")
    public DataGridView loadAllDept(DeptVo deptVo) {

        IPage<Dept> page = new Page<>(deptVo.getPage(), deptVo.getLimit());
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();

        //根据部门名称查询
        queryWrapper.like(StringUtils.isNotBlank(deptVo.getTitle()),
                "title", deptVo.getTitle());
        //根据部门地址查询
        queryWrapper.like(deptVo.getAddress() != null,
                "address", deptVo.getAddress());

        queryWrapper.like(deptVo.getRemark() != null,
                "remark", deptVo.getRemark());
        queryWrapper
                .eq(deptVo.getId() != null, "id", deptVo.getId())
                .or()
                .eq(deptVo.getId() != null, "pid", deptVo.getId());
        //根据排序码排序
        queryWrapper.orderByAsc("ordernum");
        this.deptService.page(page, queryWrapper);

        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 加载最大的排序码
     *
     * @param
     * @return
     */
    @RequestMapping("loadDeptMaxOrderNum")
    public Map<String, Object> loadDeptMaxOrderNum() {
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
        //根据排序码降序排序，得到第一条最大数的排序码
        queryWrapper.orderByDesc("ordernum");
        IPage<Dept> page = new Page<>(1,1);
        List<Dept> list = this.deptService.page(page,queryWrapper).getRecords();
        if (list.size() > 0) {
            map.put("value", list.get(0).getOrdernum() + 1);
        } else {
            map.put("value", 1);
        }
        return map;
    }

    /**
     * 查询要删除的部门是否有子结点
     */
    @RequestMapping("checkDeptHasChildrenNode")
    public Map<String, Object> checkDeptHasChildrenNode(DeptVo deptVo) {
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pid", deptVo.getId());
        List<Dept> list = this.deptService.list(queryWrapper);
        if (list.size() > 0) {
            map.put("value", true);
        } else {
            map.put("value", false);
        }
        return map;
    }

}

