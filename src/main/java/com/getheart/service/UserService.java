package com.getheart.service;

import com.getheart.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Json
 * @since 2020-05-08
 */
public interface UserService extends IService<User> {

    /**
     *  保存用户和角色之间的关系
     * @param uid
     * @param ids
     */
    void saveUserRole(Integer uid, Integer[] ids);
}
