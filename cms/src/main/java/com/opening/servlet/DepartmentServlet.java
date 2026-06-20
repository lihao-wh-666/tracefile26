package com.opening.servlet;

import com.opening.entity.Department;
import com.opening.entity.SysUser;
import com.opening.service.DepartmentService;
import com.opening.utils.PageResult;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/manage/department")
public class DepartmentServlet extends HttpServlet {

    private DepartmentService departmentService = new DepartmentService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null || action.isEmpty()) {
            action = "list";
        }

        try {
            switch (action) {
                case "list":
                    listDepartments(req, resp);
                    break;
                case "add":
                    showAddForm(req, resp);
                    break;
                case "edit":
                    showEditForm(req, resp);
                    break;
                case "view":
                    viewDepartment(req, resp);
                    break;
                case "delete":
                    deleteDepartment(req, resp);
                    break;
                default:
                    listDepartments(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMsg", "操作失败：" + e.getMessage());
            try {
                listDepartments(req, resp);
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
                    addDepartment(req, resp);
                    break;
                case "edit":
                    updateDepartment(req, resp);
                    break;
                default:
                    listDepartments(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMsg", "操作失败：" + e.getMessage());
            try {
                listDepartments(req, resp);
            } catch (Exception ex) {
                throw new ServletException(ex);
            }
        }
    }

    private void listDepartments(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String pageNumStr = req.getParameter("pageNum");
        String pageSizeStr = req.getParameter("pageSize");
        String deptName = req.getParameter("deptName");
        String province = req.getParameter("province");
        String city = req.getParameter("city");
        String statusStr = req.getParameter("status");

        int pageNum = 1;
        int pageSize = 10;
        Integer status = null;

        if (pageNumStr != null && !pageNumStr.isEmpty()) {
            pageNum = Integer.parseInt(pageNumStr);
        }
        if (pageSizeStr != null && !pageSizeStr.isEmpty()) {
            pageSize = Integer.parseInt(pageSizeStr);
        }
        if (statusStr != null && !statusStr.isEmpty()) {
            status = Integer.parseInt(statusStr);
        }

        PageResult<Department> pageResult = departmentService.findByPage(pageNum, pageSize, deptName, province, city, status);

        req.setAttribute("pageResult", pageResult);
        req.setAttribute("deptName", deptName);
        req.setAttribute("province", province);
        req.setAttribute("city", city);
        req.setAttribute("status", status);

        req.getRequestDispatcher("/WEB-INF/jsp/manage/department/list.jsp").forward(req, resp);
    }

    private void showAddForm(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.getRequestDispatcher("/WEB-INF/jsp/manage/department/form.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/manage/department");
            return;
        }

        Long id = Long.parseLong(idStr);
        Department department = departmentService.findById(id);
        req.setAttribute("department", department);
        req.setAttribute("formAction", "edit");

        req.getRequestDispatcher("/WEB-INF/jsp/manage/department/form.jsp").forward(req, resp);
    }

    private void viewDepartment(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/manage/department");
            return;
        }

        Long id = Long.parseLong(idStr);
        Department department = departmentService.findById(id);
        req.setAttribute("department", department);

        req.getRequestDispatcher("/WEB-INF/jsp/manage/department/view.jsp").forward(req, resp);
    }

    private void addDepartment(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Department department = extractDepartmentFromRequest(req);
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("loginUser") != null) {
            SysUser user = (SysUser) session.getAttribute("loginUser");
            department.setCreateBy(user.getId());
        }

        boolean result = departmentService.save(department);
        if (result) {
            resp.sendRedirect(req.getContextPath() + "/manage/department");
        } else {
            req.setAttribute("errorMsg", "添加营业部失败");
            req.setAttribute("department", department);
            req.getRequestDispatcher("/WEB-INF/jsp/manage/department/form.jsp").forward(req, resp);
        }
    }

    private void updateDepartment(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/manage/department");
            return;
        }

        Department department = extractDepartmentFromRequest(req);
        department.setId(Long.parseLong(idStr));

        boolean result = departmentService.update(department);
        if (result) {
            resp.sendRedirect(req.getContextPath() + "/manage/department");
        } else {
            req.setAttribute("errorMsg", "更新营业部失败");
            req.setAttribute("department", department);
            req.setAttribute("formAction", "edit");
            req.getRequestDispatcher("/WEB-INF/jsp/manage/department/form.jsp").forward(req, resp);
        }
    }

    private void deleteDepartment(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/manage/department");
            return;
        }

        Long id = Long.parseLong(idStr);
        departmentService.delete(id);
        resp.sendRedirect(req.getContextPath() + "/manage/department");
    }

    private Department extractDepartmentFromRequest(HttpServletRequest req) {
        Department department = new Department();
        department.setDeptNo(req.getParameter("deptNo"));
        department.setDeptName(req.getParameter("deptName"));
        department.setAddress(req.getParameter("address"));
        department.setPhone(req.getParameter("phone"));
        department.setIntroduction(req.getParameter("introduction"));
        department.setProvince(req.getParameter("province"));
        department.setCity(req.getParameter("city"));

        String statusStr = req.getParameter("status");
        if (statusStr != null && !statusStr.isEmpty()) {
            department.setStatus(Integer.parseInt(statusStr));
        }

        return department;
    }
}
