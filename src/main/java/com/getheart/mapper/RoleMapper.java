package com.getheart.mapper;

import com.getheart.pojo.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Json
 * @since 2020-05-10
 */
@Repository
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据角色ID删除sys_role_permission
     * @param id
     */
    void deleteRolePermissionByRid(Serializable id);

    /**
     * 根据角色ID删除sys_role_user
     * @param id
     */
    void deleteRoleUserByRid(Serializable id);

    /**
     * 根据角色ID查询当前角色拥有的所有权限或菜单ID
     * @param roleId
     */
    List<Integer> queryRolePermissionIdsByRid(Integer roleId);

    /**
     * 保存角色和菜单权限之间的关系
     * @param roleId
     * @param pid
     */
    void saveRolePermission(@Param("roleId") Integer roleId, @Param("pid") Integer pid);


    /**
     * 根据用户ID，删除用户角色sys_role_user中间表数据
     * @param id
     */
    void deleteRoleUserByUid(Serializable id);

    /**
     * 根据用户ID，查询角色并选中以拥有的角色
     * @param uid
     * @return
     */
    List<Integer> queryUserRoleIdsByUid(Integer uid);

    /**
     * 保存用户和角色之间的关系
     * @param uid
     * @param rid
     */
    void saveUseRole(@Param("uid") Integer uid, @Param("rid") Integer rid);
}
