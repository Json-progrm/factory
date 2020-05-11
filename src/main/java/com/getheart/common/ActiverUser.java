package com.getheart.common;

import com.getheart.pojo.User;

import java.util.List;

/**
 * @author Json
 * @date 2020-08-11:01
 */
public class ActiverUser {

    private User user;

    private List<String> roles;

    private List<String> permissions;

    public ActiverUser() {
    }

    public ActiverUser(User user, List<String> roles, List<String> permissions) {
        this.user = user;
        this.roles = roles;
        this.permissions = permissions;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}
