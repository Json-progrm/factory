package com.getheart.mapper;

import com.getheart.pojo.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Json
 * @since 2020-05-08
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 根据权限或菜单ID删除权限表各和角色的关系表里面的数据
     * @param id
     */
    void deleteRolePermissionByPid(Serializable id);

}
