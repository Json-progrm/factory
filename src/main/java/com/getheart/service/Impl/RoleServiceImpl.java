package com.getheart.service.Impl;

import com.getheart.pojo.Role;
import com.getheart.mapper.RoleMapper;
import com.getheart.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Json
 * @since 2020-05-10
 */
@Service
@Transactional
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {


    @Override
    public boolean removeById(Serializable id) {

        //根据角色ID删除sys_role_permission
        baseMapper.deleteRolePermissionByRid(id);

        //根据角色ID删除sys_role_user
        baseMapper.deleteRoleUserByRid(id);



        return super.removeById(id);
    }

    /**
     * 根据角色ID查询当前角色拥有的所有权限或菜单ID
     * @param roleId
     * @return
     */
    @Override
    public List<Integer> queryRolePermissionIdsByRid(Integer roleId) {


       return baseMapper.queryRolePermissionIdsByRid(roleId);

    }

    /**
     * 保存角色和菜单权限之间的关系
     * @param roleId
     * @param ids
     */
    @Override
    public void saveRolePermission(Integer roleId, Integer[] ids) {

        //保存之前，先查询是否已分配权限，如果分配了，就先删除再保存
        baseMapper.deleteRolePermissionByRid(roleId);
        if (ids != null && ids.length > 0){
            for (Integer pid : ids) {
                baseMapper.saveRolePermission(roleId,pid);
            }
        }


    }

    /**
     * 根据用户ID，查询角色并选中以拥有的角色
     * @param uid
     * @return
     */
    @Override
    public List<Integer> queryUserRoleIdsByUid(Integer uid) {

        return baseMapper.queryUserRoleIdsByUid(uid);
    }
}
