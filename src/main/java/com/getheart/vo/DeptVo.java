package com.getheart.vo;

import com.getheart.pojo.Dept;
import com.getheart.pojo.Loginfo;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Json
 * @date 2020-08-14:20
 */
@EqualsAndHashCode(callSuper = false)
public class DeptVo extends Dept {

    private Integer page;

    private Integer limit;

    public DeptVo() {
    }

    public DeptVo(Integer page, Integer limit) {
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
