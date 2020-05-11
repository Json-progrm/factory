package com.getheart.vo;

import com.getheart.pojo.Dept;
import com.getheart.pojo.User;
import lombok.EqualsAndHashCode;

/**
 * @author Json
 * @date 2020-08-14:20
 */
@EqualsAndHashCode(callSuper = false)
public class UserVo extends User {

    private Integer page;

    private Integer limit;

    public UserVo() {
    }

    public UserVo(Integer page, Integer limit) {
        this.page = page;
        this.limit = limit;

    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }


}
