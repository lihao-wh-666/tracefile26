package com.opening.servlet;

import com.opening.entity.Region;
import com.opening.service.RegionService;
import com.opening.utils.PageResult;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/manage/region")
public class RegionServlet extends HttpServlet {

    private RegionService regionService = new RegionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null || action.isEmpty()) {
            action = "list";
        }

        try {
            switch (action) {
                case "list":
                    listRegions(req, resp);
                    break;
                case "add":
                    showAddForm(req, resp);
                    break;
                case "edit":
                    showEditForm(req, resp);
                    break;
                case "view":
                    viewRegion(req, resp);
                    break;
                case "delete":
                    deleteRegion(req, resp);
                    break;
                default:
                    listRegions(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMsg", "操作失败：" + e.getMessage());
            try {
                listRegions(req, resp);
            } catch (Exception ex) {
                throw new ServletException(ex);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null || action.isEmpty()) {
            action = "list";
        }

        try {
            switch (action) {
                case "add":
                    addRegion(req, resp);
                    break;
                case "edit":
                    updateRegion(req, resp);
                    break;
                default:
                    listRegions(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMsg", "操作失败：" + e.getMessage());
            try {
                listRegions(req, resp);
            } catch (Exception ex) {
                throw new ServletException(ex);
            }
        }
    }

    private void listRegions(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String pageNumStr = req.getParameter("pageNum");
        String pageSizeStr = req.getParameter("pageSize");
        String regionName = req.getParameter("regionName");
        String levelStr = req.getParameter("level");
        String parentIdStr = req.getParameter("parentId");
        String statusStr = req.getParameter("status");

        int pageNum = 1;
        int pageSize = 20;
        Integer level = null;
        Long parentId = null;
        Integer status = null;

        if (pageNumStr != null && !pageNumStr.isEmpty()) {
            pageNum = Integer.parseInt(pageNumStr);
        }
        if (pageSizeStr != null && !pageSizeStr.isEmpty()) {
            pageSize = Integer.parseInt(pageSizeStr);
        }
        if (levelStr != null && !levelStr.isEmpty()) {
            level = Integer.parseInt(levelStr);
        }
        if (parentIdStr != null && !parentIdStr.isEmpty()) {
            parentId = Long.parseLong(parentIdStr);
        }
        if (statusStr != null && !statusStr.isEmpty()) {
            status = Integer.parseInt(statusStr);
        }

        PageResult<Region> pageResult = regionService.findByPage(pageNum, pageSize, regionName, level, parentId, status);
        List<Region> provinceList = regionService.findProvinceList();
        List<Region> allRegions = regionService.findAll();
        java.util.Map<Long, String> regionNameMap = new java.util.HashMap<>();
        for (Region r : allRegions) {
            regionNameMap.put(r.getId(), r.getRegionName());
        }

        req.setAttribute("pageResult", pageResult);
        req.setAttribute("regionName", regionName);
        req.setAttribute("level", level);
        req.setAttribute("parentId", parentId);
        req.setAttribute("status", status);
        req.setAttribute("provinceList", provinceList);
        req.setAttribute("regionNameMap", regionNameMap);

        req.getRequestDispatcher("/WEB-INF/jsp/manage/region/list.jsp").forward(req, resp);
    }

    private void showAddForm(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        List<Region> provinceList = regionService.findProvinceList();
        req.setAttribute("provinceList", provinceList);
        req.getRequestDispatcher("/WEB-INF/jsp/manage/region/form.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/manage/region");
            return;
        }

        Long id = Long.parseLong(idStr);
        Region region = regionService.findById(id);
        List<Region> provinceList = regionService.findProvinceList();

        List<Region> cityList = null;
        List<Region> districtList = null;
        if (region.getLevel() >= 2) {
            Long provinceId = region.getLevel() == 2 ? region.getParentId() :
                    (regionService.findById(region.getParentId()) != null ?
                            regionService.findById(region.getParentId()).getParentId() : null);
            if (provinceId != null) {
                cityList = regionService.findCityList(provinceId);
            }
        }
        if (region.getLevel() == 3) {
            districtList = regionService.findDistrictList(region.getParentId());
        }

        req.setAttribute("region", region);
        req.setAttribute("formAction", "edit");
        req.setAttribute("provinceList", provinceList);
        req.setAttribute("cityList", cityList);
        req.setAttribute("districtList", districtList);

        req.getRequestDispatcher("/WEB-INF/jsp/manage/region/form.jsp").forward(req, resp);
    }

    private void viewRegion(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/manage/region");
            return;
        }

        Long id = Long.parseLong(idStr);
        Region region = regionService.findById(id);
        String parentName = regionService.getParentName(region.getParentId());

        req.setAttribute("region", region);
        req.setAttribute("parentName", parentName);

        req.getRequestDispatcher("/WEB-INF/jsp/manage/region/view.jsp").forward(req, resp);
    }

    private void addRegion(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Region region = extractRegionFromRequest(req);

        boolean result = regionService.save(region);
        if (result) {
            resp.sendRedirect(req.getContextPath() + "/manage/region");
        } else {
            req.setAttribute("errorMsg", "添加区域失败");
            req.setAttribute("region", region);
            List<Region> provinceList = regionService.findProvinceList();
            req.setAttribute("provinceList", provinceList);
            req.getRequestDispatcher("/WEB-INF/jsp/manage/region/form.jsp").forward(req, resp);
        }
    }

    private void updateRegion(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/manage/region");
            return;
        }

        Region region = extractRegionFromRequest(req);
        region.setId(Long.parseLong(idStr));

        boolean result = regionService.update(region);
        if (result) {
            resp.sendRedirect(req.getContextPath() + "/manage/region");
        } else {
            req.setAttribute("errorMsg", "更新区域失败");
            req.setAttribute("region", region);
            req.setAttribute("formAction", "edit");
            List<Region> provinceList = regionService.findProvinceList();
            req.setAttribute("provinceList", provinceList);
            req.getRequestDispatcher("/WEB-INF/jsp/manage/region/form.jsp").forward(req, resp);
        }
    }

    private void deleteRegion(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/manage/region");
            return;
        }

        Long id = Long.parseLong(idStr);
        regionService.delete(id);
        resp.sendRedirect(req.getContextPath() + "/manage/region");
    }

    private Region extractRegionFromRequest(HttpServletRequest req) {
        Region region = new Region();
        region.setRegionCode(req.getParameter("regionCode"));
        region.setRegionName(req.getParameter("regionName"));

        String levelStr = req.getParameter("level");
        if (levelStr != null && !levelStr.isEmpty()) {
            region.setLevel(Integer.parseInt(levelStr));
        }

        String parentIdStr = req.getParameter("parentId");
        if (parentIdStr != null && !parentIdStr.isEmpty()) {
            region.setParentId(Long.parseLong(parentIdStr));
        } else {
            region.setParentId(0L);
        }

        String sortStr = req.getParameter("sort");
        if (sortStr != null && !sortStr.isEmpty()) {
            region.setSort(Integer.parseInt(sortStr));
        }

        String statusStr = req.getParameter("status");
        if (statusStr != null && !statusStr.isEmpty()) {
            region.setStatus(Integer.parseInt(statusStr));
        }

        return region;
    }
}
