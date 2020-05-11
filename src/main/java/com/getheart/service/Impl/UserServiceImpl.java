package com.getheart.service.Impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.getheart.mapper.RoleMapper;
import com.getheart.pojo.User;
import com.getheart.mapper.UserMapper;
import com.getheart.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RoleMapper roleMapper;
    @Override
    public boolean save(User entity) {
        return super.save(entity);
    }

    @Override
    public boolean updateById(User entity) {
        return super.updateById(entity);
    }

    @Override
    public User getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    public boolean removeById(Serializable id) {

        //根据用户ID，删除用户角色sys_role_user中间表数据
        roleMapper.deleteRoleUserByUid(id);

        return super.removeById(id);
    }

    /**
     *  保存用户和角色之间的关系
     * @param uid
     * @param ids
     */
    @Override
    public void saveUserRole(Integer uid, Integer[] ids) {

        // 先根据用户ID，删除用户角色sys_role_user中间表数据
        roleMapper.deleteRoleUserByUid(uid);
        if (ids != null && ids.length > 0){
            for (Integer rid : ids) {
                roleMapper.saveUseRole(uid,rid);
            }
        }


    }
}
