package com.opening.service;

import com.opening.dao.RegionDAO;
import com.opening.entity.Region;
import com.opening.utils.PageResult;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegionService {

    private RegionDAO regionDAO = new RegionDAO();

    public Region findById(Long id) throws SQLException {
        return regionDAO.findById(id);
    }

    public Region findByRegionCode(String regionCode) throws SQLException {
        return regionDAO.findByRegionCode(regionCode);
    }

    public List<Region> findByParentId(Long parentId) throws SQLException {
        return regionDAO.findByParentId(parentId);
    }

    public List<Region> findByLevel(Integer level) throws SQLException {
        return regionDAO.findByLevel(level);
    }

    public List<Region> findAll() throws SQLException {
        return regionDAO.findAll();
    }

    public PageResult<Region> findByPage(int pageNum, int pageSize, String regionName, Integer level, Long parentId, Integer status) throws SQLException {
        long total = regionDAO.count(regionName, level, parentId, status);
        List<Region> list = regionDAO.findByPage(pageNum, pageSize, regionName, level, parentId, status);
        return new PageResult<>(pageNum, pageSize, total, list);
    }

    public boolean save(Region region) throws SQLException {
        if (region.getSort() == null) {
            region.setSort(0);
        }
        if (region.getStatus() == null) {
            region.setStatus(1);
        }
        return regionDAO.save(region) > 0;
    }

    public boolean update(Region region) throws SQLException {
        Region existRegion = regionDAO.findById(region.getId());
        if (existRegion == null) {
            return false;
        }
        return regionDAO.update(region) > 0;
    }

    public boolean delete(Long id) throws SQLException {
        long childCount = regionDAO.countByParentId(id);
        if (childCount > 0) {
            throw new SQLException("存在下级区域，无法删除");
        }
        return regionDAO.delete(id) > 0;
    }

    public List<Region> findProvinceList() throws SQLException {
        return regionDAO.findByLevel(1);
    }

    public List<Region> findCityList(Long provinceId) throws SQLException {
        return regionDAO.findByParentId(provinceId);
    }

    public List<Region> findDistrictList(Long cityId) throws SQLException {
        return regionDAO.findByParentId(cityId);
    }

    public Map<String, String> getRegionPath(Long id) throws SQLException {
        Map<String, String> path = new HashMap<>();
        Region region = findById(id);
        if (region == null) {
            return path;
        }
        if (region.getLevel() == 3) {
            path.put("district", region.getRegionName());
            Region city = findById(region.getParentId());
            if (city != null) {
                path.put("city", city.getRegionName());
                Region province = findById(city.getParentId());
                if (province != null) {
                    path.put("province", province.getRegionName());
                }
            }
        } else if (region.getLevel() == 2) {
            path.put("city", region.getRegionName());
            Region province = findById(region.getParentId());
            if (province != null) {
                path.put("province", province.getRegionName());
            }
        } else if (region.getLevel() == 1) {
            path.put("province", region.getRegionName());
        }
        return path;
    }

    public String getParentName(Long parentId) throws SQLException {
        if (parentId == null || parentId == 0) {
            return "顶级";
        }
        Region parent = findById(parentId);
        return parent != null ? parent.getRegionName() : "未知";
    }
}
