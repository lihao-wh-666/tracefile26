package com.opening.service;

import com.opening.dao.ProvinceDAO;
import com.opening.dao.CityDAO;
import com.opening.dao.DistrictDAO;
import com.opening.entity.Province;
import com.opening.entity.City;
import com.opening.entity.District;
import com.opening.entity.Region;
import com.opening.utils.PageResult;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegionService {

    private ProvinceDAO provinceDAO = new ProvinceDAO();
    private CityDAO cityDAO = new CityDAO();
    private DistrictDAO districtDAO = new DistrictDAO();

    public Region findById(Long id) throws SQLException {
        if (id == null) return null;
        if (id < 10000) {
            Province province = provinceDAO.findById(id);
            return province != null ? convertToRegion(province) : null;
        } else if (id < 100000) {
            City city = cityDAO.findById(id);
            return city != null ? convertToRegion(city) : null;
        } else {
            District district = districtDAO.findById(id);
            return district != null ? convertToRegion(district) : null;
        }
    }

    public Region findByRegionCode(String regionCode) throws SQLException {
        if (regionCode == null) return null;
        Province province = provinceDAO.findByProvinceCode(regionCode);
        if (province != null) return convertToRegion(province);
        City city = cityDAO.findByCityCode(regionCode);
        if (city != null) return convertToRegion(city);
        District district = districtDAO.findByDistrictCode(regionCode);
        if (district != null) return convertToRegion(district);
        return null;
    }

    public List<Region> findByParentId(Long parentId) throws SQLException {
        if (parentId == null || parentId == 0) {
            return convertProvinceList(provinceDAO.findAll());
        }
        if (parentId < 10000) {
            return convertCityList(cityDAO.findByProvinceId(parentId));
        } else if (parentId < 100000) {
            return convertDistrictList(districtDAO.findByCityId(parentId));
        }
        return new ArrayList<>();
    }

    public List<Region> findByLevel(Integer level) throws SQLException {
        if (level == null) return new ArrayList<>();
        switch (level) {
            case 1:
                return convertProvinceList(provinceDAO.findAll());
            case 2:
                return convertCityList(cityDAO.findAll());
            case 3:
                return convertDistrictList(districtDAO.findAll());
            default:
                return new ArrayList<>();
        }
    }

    public List<Region> findAll() throws SQLException {
        List<Region> result = new ArrayList<>();
        result.addAll(convertProvinceList(provinceDAO.findAll()));
        result.addAll(convertCityList(cityDAO.findAll()));
        result.addAll(convertDistrictList(districtDAO.findAll()));
        return result;
    }

    public PageResult<Region> findByPage(int pageNum, int pageSize, String regionName, Integer level, Long parentId, Integer status) throws SQLException {
        if (level != null) {
            return findByPageByLevel(pageNum, pageSize, regionName, level, parentId, status);
        }
        return findByPageAllLevels(pageNum, pageSize, regionName, parentId, status);
    }

    private PageResult<Region> findByPageByLevel(int pageNum, int pageSize, String regionName, Integer level, Long parentId, Integer status) throws SQLException {
        long total = 0;
        List<Region> list = new ArrayList<>();

        switch (level) {
            case 1:
                total = provinceDAO.count(regionName, status);
                list = convertProvinceList(provinceDAO.findByPage(pageNum, pageSize, regionName, status));
                break;
            case 2:
                total = cityDAO.count(regionName, parentId, status);
                list = convertCityList(cityDAO.findByPage(pageNum, pageSize, regionName, parentId, status));
                break;
            case 3:
                total = districtDAO.count(regionName, parentId, status);
                list = convertDistrictList(districtDAO.findByPage(pageNum, pageSize, regionName, parentId, status));
                break;
        }

        return new PageResult<>(pageNum, pageSize, total, list);
    }

    private PageResult<Region> findByPageAllLevels(int pageNum, int pageSize, String regionName, Long parentId, Integer status) throws SQLException {
        List<Region> allList = new ArrayList<>();

        if (parentId == null || parentId == 0) {
            allList.addAll(convertProvinceList(provinceDAO.findByPage(1, 1000, regionName, status)));
        }

        Long provinceFilterId = null;
        Long cityFilterId = null;
        if (parentId != null && parentId > 0) {
            if (parentId < 10000) {
                provinceFilterId = parentId;
            } else if (parentId < 100000) {
                cityFilterId = parentId;
            }
        }

        if (cityFilterId == null) {
            allList.addAll(convertCityList(cityDAO.findByPage(1, 1000, regionName, provinceFilterId, status)));
        }

        allList.addAll(convertDistrictList(districtDAO.findByPage(1, 1000, regionName, cityFilterId, status)));

        long total = allList.size();
        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, allList.size());
        List<Region> pageList = fromIndex < total ? allList.subList(fromIndex, toIndex) : new ArrayList<>();

        return new PageResult<>(pageNum, pageSize, total, pageList);
    }

    public boolean save(Region region) throws SQLException {
        if (region.getSort() == null) {
            region.setSort(0);
        }
        if (region.getStatus() == null) {
            region.setStatus(1);
        }

        Integer level = region.getLevel();
        if (level == null) return false;

        switch (level) {
            case 1:
                return provinceDAO.save(convertToProvince(region)) > 0;
            case 2:
                return cityDAO.save(convertToCity(region)) > 0;
            case 3:
                return districtDAO.save(convertToDistrict(region)) > 0;
            default:
                return false;
        }
    }

    public boolean update(Region region) throws SQLException {
        Region existRegion = findById(region.getId());
        if (existRegion == null) {
            return false;
        }

        Integer level = region.getLevel();
        if (level == null) level = existRegion.getLevel();

        switch (level) {
            case 1:
                Province province = convertToProvince(region);
                province.setId(region.getId());
                return provinceDAO.update(province) > 0;
            case 2:
                City city = convertToCity(region);
                city.setId(region.getId());
                return cityDAO.update(city) > 0;
            case 3:
                District district = convertToDistrict(region);
                district.setId(region.getId());
                return districtDAO.update(district) > 0;
            default:
                return false;
        }
    }

    public boolean delete(Long id) throws SQLException {
        long childCount = countByParentId(id);
        if (childCount > 0) {
            throw new SQLException("存在下级区域，无法删除");
        }

        if (id < 10000) {
            return provinceDAO.delete(id) > 0;
        } else if (id < 100000) {
            return cityDAO.delete(id) > 0;
        } else {
            return districtDAO.delete(id) > 0;
        }
    }

    public long countByParentId(Long parentId) throws SQLException {
        if (parentId == null || parentId == 0) {
            return provinceDAO.count(null, null);
        }
        if (parentId < 10000) {
            return cityDAO.countByProvinceId(parentId);
        } else if (parentId < 100000) {
            return districtDAO.countByCityId(parentId);
        }
        return 0;
    }

    public List<Region> findProvinceList() throws SQLException {
        return convertProvinceList(provinceDAO.findAll());
    }

    public List<Region> findCityList(Long provinceId) throws SQLException {
        if (provinceId == null) {
            return convertCityList(cityDAO.findAll());
        }
        return convertCityList(cityDAO.findByProvinceId(provinceId));
    }

    public List<Region> findDistrictList(Long cityId) throws SQLException {
        if (cityId == null) {
            return convertDistrictList(districtDAO.findAll());
        }
        return convertDistrictList(districtDAO.findByCityId(cityId));
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

    private Region convertToRegion(Province province) {
        Region region = new Region();
        region.setId(province.getId());
        region.setRegionCode(province.getProvinceCode());
        region.setRegionName(province.getProvinceName());
        region.setLevel(1);
        region.setParentId(0L);
        region.setSort(province.getSort());
        region.setStatus(province.getStatus());
        region.setCreateTime(province.getCreateTime());
        region.setUpdateTime(province.getUpdateTime());
        return region;
    }

    private Region convertToRegion(City city) {
        Region region = new Region();
        region.setId(city.getId());
        region.setRegionCode(city.getCityCode());
        region.setRegionName(city.getCityName());
        region.setLevel(2);
        region.setParentId(city.getProvinceId());
        region.setSort(city.getSort());
        region.setStatus(city.getStatus());
        region.setCreateTime(city.getCreateTime());
        region.setUpdateTime(city.getUpdateTime());
        return region;
    }

    private Region convertToRegion(District district) {
        Region region = new Region();
        region.setId(district.getId());
        region.setRegionCode(district.getDistrictCode());
        region.setRegionName(district.getDistrictName());
        region.setLevel(3);
        region.setParentId(district.getCityId());
        region.setSort(district.getSort());
        region.setStatus(district.getStatus());
        region.setCreateTime(district.getCreateTime());
        region.setUpdateTime(district.getUpdateTime());
        return region;
    }

    private List<Region> convertProvinceList(List<Province> provinces) {
        List<Region> result = new ArrayList<>();
        for (Province p : provinces) {
            result.add(convertToRegion(p));
        }
        return result;
    }

    private List<Region> convertCityList(List<City> cities) {
        List<Region> result = new ArrayList<>();
        for (City c : cities) {
            result.add(convertToRegion(c));
        }
        return result;
    }

    private List<Region> convertDistrictList(List<District> districts) {
        List<Region> result = new ArrayList<>();
        for (District d : districts) {
            result.add(convertToRegion(d));
        }
        return result;
    }

    private Province convertToProvince(Region region) {
        Province province = new Province();
        province.setId(region.getId());
        province.setProvinceCode(region.getRegionCode());
        province.setProvinceName(region.getRegionName());
        province.setSort(region.getSort());
        province.setStatus(region.getStatus());
        province.setCreateTime(region.getCreateTime());
        province.setUpdateTime(region.getUpdateTime());
        return province;
    }

    private City convertToCity(Region region) {
        City city = new City();
        city.setId(region.getId());
        city.setCityCode(region.getRegionCode());
        city.setCityName(region.getRegionName());
        city.setProvinceId(region.getParentId());
        city.setSort(region.getSort());
        city.setStatus(region.getStatus());
        city.setCreateTime(region.getCreateTime());
        city.setUpdateTime(region.getUpdateTime());
        return city;
    }

    private District convertToDistrict(Region region) {
        District district = new District();
        district.setId(region.getId());
        district.setDistrictCode(region.getRegionCode());
        district.setDistrictName(region.getRegionName());
        district.setCityId(region.getParentId());
        district.setSort(region.getSort());
        district.setStatus(region.getStatus());
        district.setCreateTime(region.getCreateTime());
        district.setUpdateTime(region.getUpdateTime());
        return district;
    }
}
