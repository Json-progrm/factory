package com.getheart.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.getheart.common.Constant;
import com.getheart.common.DataGridView;
import com.getheart.common.json.ResultObj;
import com.getheart.common.json.TreeNode;
import com.getheart.pojo.Notice;
import com.getheart.pojo.Permission;
import com.getheart.pojo.Role;
import com.getheart.pojo.User;
import com.getheart.service.PermissionService;
import com.getheart.service.RoleService;
import com.getheart.util.WebUtil;
import com.getheart.vo.NoticeVo;
import com.getheart.vo.RoleVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Json
 * @since 2020-05-10
 */
@RestController
@RequestMapping("/role")
public class RoleController {


    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;
    /**
     * 查询角色
     * @param roleVo
     * @return
     */
    @RequestMapping("loadAllRole")
    public DataGridView loadAllLoginfo(RoleVo roleVo){

        IPage<Role> page = new Page<>(roleVo.getPage(),roleVo.getLimit());
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        //根据角色名查询
        queryWrapper.like(StringUtils.isNotBlank(roleVo.getName()),
                "name",roleVo.getName());
        //根据角色备注查询
        queryWrapper.like(StringUtils.isNotBlank(roleVo.getRemark()),
                "remark",roleVo.getRemark());
        queryWrapper.eq(roleVo.getAvailable()!=null,
                "available",roleVo.getAvailable());
        //根据登录时间排序
        queryWrapper.orderByDesc("createtime");
        this.roleService.page(page,queryWrapper);

        return new DataGridView(page.getTotal(),page.getRecords());
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @RequestMapping("deleteRole")
    public ResultObj deleteLoginfo(Integer id){
        try {
            this.roleService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            return ResultObj.DELETE_FAIL;
        }
    }

    /**
     * 添加角色
     * @param roleVo
     * @return
     */
    @RequestMapping("addRole")
    public ResultObj addNotice(RoleVo roleVo){
        try {
            roleVo.setCreatetime(new Date());
            this.roleService.saveOrUpdate(roleVo);
            return ResultObj.ADD_SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            return ResultObj.ADD_FAIL;
        }
    }

    /**
     * 更新角色
     * @param roleVo
     * @return
     */
    @RequestMapping("updateRole")
    public ResultObj updateNotice(RoleVo roleVo){
        try {
            this.roleService.updateById(roleVo);
            return ResultObj.UPDATE_SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            return ResultObj.UPDATE_FAIL;
        }
    }


    /**
     * 根据角色ID加载菜单和权限的树的json串
     */
    @RequestMapping("initPermissionRoleId")
    public DataGridView initPermissionRoleId(Integer roleId){
        //查询所有可用的菜单个权限
        QueryWrapper<Permission> queryWrapper =new QueryWrapper<>() ;
        queryWrapper.eq("available", Constant.AVAILABLE_TURE);
        List<Permission> allpermissions = permissionService.list(queryWrapper);
        /**
         * 1、根据角色ID查询当前角色拥有的所有权限或菜单ID
         * 2、根据查询出来的菜单ID，去查询当前角色拥有的菜单数据
         */
        //获得当前角色的ID
        List<Integer> currRolePermissions = roleService.queryRolePermissionIdsByRid(roleId);
        List<Permission> currPermissions = null;
        if (currRolePermissions.size() > 0){//如果有 ID 就去查询，没有就返回空的集合
            queryWrapper.in("id",currRolePermissions);
             //获得当前角色所拥有的权限
            currPermissions = permissionService.list(queryWrapper);
        }else {
            currPermissions = new ArrayList<>();
        }
        //构造List<TreeNode>
        List<TreeNode> nodes = new ArrayList<>();
        for (Permission p1 : allpermissions) {//循环遍历所有的权限
            String checkArr = "0";
            for (Permission p2 : currPermissions) {//循环遍历当前角色所拥有的权限
                if (p1.getId().equals(p2.getId())){
                    checkArr = "1";
                    break;
                }
            }
            Boolean spread = (p1.getOpen()==null||p1.getOpen().equals(Constant.OPEN_TRUE)) ? true : false;
            nodes.add(new TreeNode(p1.getId(),p1.getPid(),p1.getTitle(),spread,checkArr));

        }
        return new DataGridView(nodes);
    }

    /**
     * 保存角色和菜单权限之间的关系
     */
    @RequestMapping("saveRolePermission")
    public ResultObj saveRolePermission(Integer roleId,Integer ids[]){

        try {
            roleService.saveRolePermission(roleId,ids);
            return ResultObj.DISPATCH_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DISPATCH_FAIL;
        }


    }
}

