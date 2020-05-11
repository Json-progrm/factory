package com.getheart.service.Impl;

import com.getheart.pojo.Permission;
import com.getheart.mapper.PermissionMapper;
import com.getheart.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Json
 * @since 2020-05-08
 */
@Service
@Transactional
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {


    @Override
    public boolean removeById(Serializable id) {

        //根据权限或菜单ID删除权限表各和角色的关系表里面的数据
        baseMapper.deleteRolePermissionByPid(id);
        return super.removeById(id);//删除 权限表的数据
    }


}
