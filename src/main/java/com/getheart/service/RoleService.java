package com.getheart.service;

import com.getheart.pojo.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Json
 * @since 2020-05-10
 */
public interface RoleService extends IService<Role> {

    /**
     * 根据角色ID查询当前角色拥有的所有权限或菜单ID
     * @param roleId
     * @return
     */
    List<Integer> queryRolePermissionIdsByRid(Integer roleId);

    /**
     * 保存角色和菜单权限之间的关系
     * @param roleId
     * @param ids
     */
    void saveRolePermission(Integer roleId, Integer[] ids);

    /**
     * 根据用户ID，查询角色并选中以拥有的角色
     * @param uid
     * @return
     */
    List<Integer> queryUserRoleIdsByUid(Integer uid);
}
