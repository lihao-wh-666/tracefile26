package com.opening.servlet;

import com.opening.entity.Customer;
import com.opening.entity.SysUser;
import com.opening.service.CustomerService;
import com.opening.utils.PageResult;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet("/manage/customer")
public class CustomerServlet extends HttpServlet {

    private CustomerService customerService = new CustomerService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null || action.isEmpty()) {
            action = "list";
        }

        try {
            switch (action) {
                case "list":
                    listCustomers(req, resp);
                    break;
                case "add":
                    showAddForm(req, resp);
                    break;
                case "edit":
                    showEditForm(req, resp);
                    break;
                case "view":
                    viewCustomer(req, resp);
                    break;
                case "delete":
                    deleteCustomer(req, resp);
                    break;
                case "audit":
                    showAuditForm(req, resp);
                    break;
                default:
                    listCustomers(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMsg", "操作失败：" + e.getMessage());
            try {
                listCustomers(req, resp);
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
                    addCustomer(req, resp);
                    break;
                case "edit":
                    updateCustomer(req, resp);
                    break;
                case "audit":
                    auditCustomer(req, resp);
                    break;
                default:
                    listCustomers(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMsg", "操作失败：" + e.getMessage());
            try {
                listCustomers(req, resp);
            } catch (Exception ex) {
                throw new ServletException(ex);
            }
        }
    }

    private void listCustomers(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String pageNumStr = req.getParameter("pageNum");
        String pageSizeStr = req.getParameter("pageSize");
        String keyword = req.getParameter("keyword");
        String auditStatusStr = req.getParameter("auditStatus");

        int pageNum = 1;
        int pageSize = 10;
        Integer auditStatus = null;

        if (pageNumStr != null && !pageNumStr.isEmpty()) {
            pageNum = Integer.parseInt(pageNumStr);
        }
        if (pageSizeStr != null && !pageSizeStr.isEmpty()) {
            pageSize = Integer.parseInt(pageSizeStr);
        }
        if (auditStatusStr != null && !auditStatusStr.isEmpty()) {
            auditStatus = Integer.parseInt(auditStatusStr);
        }

        PageResult<Customer> pageResult = customerService.findByPage(pageNum, pageSize, keyword, auditStatus);

        req.setAttribute("pageResult", pageResult);
        req.setAttribute("keyword", keyword);
        req.setAttribute("auditStatus", auditStatus);

        req.getRequestDispatcher("/WEB-INF/jsp/manage/customer/list.jsp").forward(req, resp);
    }

    private void showAddForm(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.getRequestDispatcher("/WEB-INF/jsp/manage/customer/form.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/manage/customer");
            return;
        }

        Long id = Long.parseLong(idStr);
        Customer customer = customerService.findById(id);
        req.setAttribute("customer", customer);
        req.setAttribute("formAction", "edit");

        req.getRequestDispatcher("/WEB-INF/jsp/manage/customer/form.jsp").forward(req, resp);
    }

    private void viewCustomer(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/manage/customer");
            return;
        }

        Long id = Long.parseLong(idStr);
        Customer customer = customerService.findById(id);
        req.setAttribute("customer", customer);

        req.getRequestDispatcher("/WEB-INF/jsp/manage/customer/view.jsp").forward(req, resp);
    }

    private void showAuditForm(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/manage/customer");
            return;
        }

        Long id = Long.parseLong(idStr);
        Customer customer = customerService.findById(id);
        req.setAttribute("customer", customer);

        req.getRequestDispatcher("/WEB-INF/jsp/manage/customer/audit.jsp").forward(req, resp);
    }

    private void addCustomer(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Customer customer = extractCustomerFromRequest(req);
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("loginUser") != null) {
            SysUser user = (SysUser) session.getAttribute("loginUser");
            customer.setCreateBy(user.getId());
        }

        boolean result = customerService.save(customer);
        if (result) {
            resp.sendRedirect(req.getContextPath() + "/manage/customer");
        } else {
            req.setAttribute("errorMsg", "添加客户失败");
            req.setAttribute("customer", customer);
            req.getRequestDispatcher("/WEB-INF/jsp/manage/customer/form.jsp").forward(req, resp);
        }
    }

    private void updateCustomer(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/manage/customer");
            return;
        }

        Customer customer = extractCustomerFromRequest(req);
        customer.setId(Long.parseLong(idStr));

        boolean result = customerService.update(customer);
        if (result) {
            resp.sendRedirect(req.getContextPath() + "/manage/customer");
        } else {
            req.setAttribute("errorMsg", "更新客户失败");
            req.setAttribute("customer", customer);
            req.setAttribute("formAction", "edit");
            req.getRequestDispatcher("/WEB-INF/jsp/manage/customer/form.jsp").forward(req, resp);
        }
    }

    private void auditCustomer(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String idStr = req.getParameter("id");
        String auditStatusStr = req.getParameter("auditStatus");
        String auditResult = req.getParameter("auditResult");

        if (idStr == null || idStr.isEmpty() || auditStatusStr == null || auditStatusStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/manage/customer");
            return;
        }

        Long id = Long.parseLong(idStr);
        Integer auditStatus = Integer.parseInt(auditStatusStr);

        HttpSession session = req.getSession(false);
        Long auditBy = null;
        if (session != null && session.getAttribute("loginUser") != null) {
            SysUser user = (SysUser) session.getAttribute("loginUser");
            auditBy = user.getId();
        }

        boolean result = customerService.audit(id, auditStatus, auditResult, auditBy);
        if (result) {
            resp.sendRedirect(req.getContextPath() + "/manage/customer");
        } else {
            req.setAttribute("errorMsg", "审核失败");
            showAuditForm(req, resp);
        }
    }

    private void deleteCustomer(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/manage/customer");
            return;
        }

        Long id = Long.parseLong(idStr);
        customerService.delete(id);
        resp.sendRedirect(req.getContextPath() + "/manage/customer");
    }

    private Customer extractCustomerFromRequest(HttpServletRequest req) {
        Customer customer = new Customer();
        customer.setRealName(req.getParameter("realName"));
        customer.setIdType(req.getParameter("idType"));
        customer.setIdNo(req.getParameter("idNo"));
        customer.setGender(req.getParameter("gender"));
        String birthDateStr = req.getParameter("birthDate");
        if (birthDateStr != null && !birthDateStr.isEmpty()) {
            customer.setBirthDate(LocalDate.parse(birthDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        customer.setPhone(req.getParameter("phone"));
        customer.setEmail(req.getParameter("email"));
        customer.setAddress(req.getParameter("address"));
        customer.setOccupation(req.getParameter("occupation"));
        customer.setNationality(req.getParameter("nationality"));
        customer.setRemark(req.getParameter("remark"));

        String idVerifiedStr = req.getParameter("idVerified");
        if (idVerifiedStr != null && !idVerifiedStr.isEmpty()) {
            customer.setIdVerified(Integer.parseInt(idVerifiedStr));
        }
        String faceVerifiedStr = req.getParameter("faceVerified");
        if (faceVerifiedStr != null && !faceVerifiedStr.isEmpty()) {
            customer.setFaceVerified(Integer.parseInt(faceVerifiedStr));
        }

        return customer;
    }
}
