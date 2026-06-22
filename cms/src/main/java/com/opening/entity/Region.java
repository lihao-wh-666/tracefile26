package com.opening.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Region implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String regionCode;
    private String regionName;
    private Integer level;
    private Long parentId;
    private Integer sort;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getLevelDesc() {
        if (level == null) return "未知";
        switch (level) {
            case 1: return "省份";
            case 2: return "城市";
            case 3: return "区县";
            default: return "未知";
        }
    }

    public String getStatusDesc() {
        if (status == null) return "未知";
        switch (status) {
            case 0: return "禁用";
            case 1: return "启用";
            default: return "未知";
        }
    }
}
