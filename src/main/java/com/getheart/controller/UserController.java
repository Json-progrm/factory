package com.getheart.controller;


import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.getheart.common.Constant;
import com.getheart.common.DataGridView;
import com.getheart.common.json.ResultObj;
import com.getheart.pojo.Dept;
import com.getheart.pojo.Permission;
import com.getheart.pojo.Role;
import com.getheart.pojo.User;
import com.getheart.service.DeptService;
import com.getheart.service.RoleService;
import com.getheart.service.UserService;
import com.getheart.util.PinyinUtils;
import com.getheart.util.WebUtil;
import com.getheart.vo.DeptVo;
import com.getheart.vo.UserVo;
import net.sourceforge.pinyin4j.PinyinHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Json
 * @since 2020-05-08
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private RoleService roleService;


    /**
     * 查询用户
     */
    @RequestMapping("loadAllUser")
    public DataGridView loadAllUser(UserVo userVo){

        IPage<User> page = new Page<>(userVo.getPage(),userVo.getLimit());

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .like(StringUtils.isNotBlank(userVo.getName()),"name",userVo.getName())
                .or()
                .like(StringUtils.isNotBlank(userVo.getName()), "loginname", userVo.getName());
        queryWrapper.like(StringUtils.isNotBlank(userVo.getAddress()),"address",userVo.getAddress());
        //查询系统用户
        queryWrapper.eq("type", Constant.USER_TYPE_NORMAL);
        queryWrapper.eq(userVo.getDeptid() != null,"deptid",userVo.getDeptid());


        userService.page(page,queryWrapper);

        List<User> list = page.getRecords();
        for (User user : list) {

            Integer deptid = user.getDeptid();

            if (deptid != null){
                Dept dept = deptService.getById(deptid);
                user.setDeptname(dept.getTitle());//将所属部门的id翻译成部门名称
            }
            Integer mgr = user.getMgr();

            if (mgr != null){
                User userone = userService.getById(mgr);
                user.setLeadername(userone.getName());//将所属领导的id翻译成领导名字
            }else {
                user.setLeadername("无");
            }
        }
        return new DataGridView(page.getTotal(),list);
    }

    /**
     * 根据部门ID查询用户的所属领导
     * @param deptid
     * @return
     */
    @RequestMapping("loadUsersByDeptId")
    public DataGridView loadUsersByDeptId(Integer deptid){

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(deptid != null,"deptid",deptid);
        queryWrapper.eq("available",Constant.AVAILABLE_TURE);
        queryWrapper.eq("type",Constant.USER_TYPE_NORMAL);
        List<User> list = userService.list(queryWrapper);
        return new DataGridView(list);

    }

    /**
     * 添加用户
     *
     * @param userVo
     * @return
     */
    @RequestMapping("addUser")
    public ResultObj addNotice(UserVo userVo) {
        try {
            userVo.setType(Constant.USER_TYPE_NORMAL);//设置用户类型
            userVo.setHiredate(new Date());
            String salt = IdUtil.simpleUUID().toUpperCase();//生成UUID
            userVo.setSalt(salt);
            userVo.setPwd(new Md5Hash(Constant.USER_DEFAULT_PWD,salt,2).toString());
            this.userService.save(userVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_FAIL;
        }
    }

    /**
     * 更新用户信息
     *
     * @param userVo
     * @return
     */
    @RequestMapping("updateUser")
    public ResultObj updateDept(UserVo userVo) {
        try {
            this.userService.updateById(userVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_FAIL;
        }
    }


    /**
     * 删除用户信息
     */
    @RequestMapping("deleteUser")
    public ResultObj deleteUser(Integer id){

        try {
            this.userService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_FAIL;
        }
    }

    /**
     * 根据用户ID查询一个用户的直属领导
     */

    @RequestMapping("loadUserById")
    public DataGridView loadUserById(Integer id){

       if (null == id){
           id = 1;
       }
        return new DataGridView(userService.getById(id));
    }

    /**
     * 根据用户ID，查询角色并选中以拥有的角色
     */
    @RequestMapping("initRoleByUserId")
    public DataGridView initRoleByUserId(Integer uid){
        //1、查询所有可用的角色


        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        //查询可用的角色
        queryWrapper.eq("available",Constant.AVAILABLE_TURE);
        List<Map<String, Object>> listMaps = roleService.listMaps(queryWrapper);

        //查询当前用户拥有的角色ID集合
        List<Integer> currentUserRoleIds = roleService.queryUserRoleIdsByUid(uid);
        for (Map<String, Object> listMap : listMaps) {
            Boolean LAY_CHECKED = false;
            Integer roleId = (Integer) listMap.get("id");
            for (Integer rid : currentUserRoleIds) {
                if (roleId.equals(rid)){
                    LAY_CHECKED = true;
                    break;
                }
            }
            listMap.put("LAY_CHECKED",LAY_CHECKED);
        }

        return new DataGridView(Long.valueOf(listMaps.size()),listMaps);
    }


    /**
     * 保存用户和角色之间的关系
     */
    @RequestMapping("saveUserRole")
    public ResultObj saveUserRole(Integer uid ,Integer[] ids){

        try {

            userService.saveUserRole(uid,ids);
            return ResultObj.DISPATCH_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DISPATCH_FAIL;
        }

    }

    /**
     * 重置密码
     */
    @RequestMapping("resetPwd")
    public ResultObj resetPwd(Integer id){
        try {
            User user = new User();
            user.setId(id);
            String salt = IdUtil.simpleUUID().toUpperCase();//生成UUID
            user.setSalt(salt);
            user.setPwd(new Md5Hash(Constant.USER_DEFAULT_PWD,salt,2).toString());
            this.userService.updateById(user);
            return ResultObj.RESET_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.RESET_FAIL;
        }
    }
    /**
     * 把用户名转成拼音
     */
    @RequestMapping("changeChineseToPinyin")
    public Map<String, Object> changeChineseToPinyin(String username) {

        Map<String, Object> map = new HashMap<>();
        if (username != null){
            map.put("value", PinyinUtils.getPingYin(username));
        }else {
            map.put("value","");
        }
        return map;
    }

    /**
     * 加载最大的排序码
     *
     * @param
     * @return
     */
    @RequestMapping("loadUserMaxOrderNum")
    public Map<String, Object> loadUserMaxOrderNum() {
        Map<String, Object> map = new HashMap<String, Object>();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("ordernum");

        IPage<User> page = new Page<>(1, 1);

        List<User> list = this.userService.page(page, queryWrapper).getRecords();
        if (list.size() > 0) {
            map.put("value", list.get(0).getOrdernum() + 1);
        } else {
            map.put("value", 1);
        }
        return map;
    }


}

