package com.opening.servlet;

import com.opening.entity.Account;
import com.opening.entity.Customer;
import com.opening.entity.SysUser;
import com.opening.service.AccountService;
import com.opening.service.CustomerService;
import com.opening.utils.PageResult;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/manage/account")
public class AccountServlet extends HttpServlet {

    private AccountService accountService = new AccountService();
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
                    listAccounts(req, resp);
                    break;
                case "add":
                    showAddForm(req, resp);
                    break;
                case "edit":
                    showEditForm(req, resp);
                    break;
                case "view":
                    viewAccount(req, resp);
                    break;
                case "delete":
                    deleteAccount(req, resp);
                    break;
                default:
                    listAccounts(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMsg", "操作失败：" + e.getMessage());
            try {
                listAccounts(req, resp);
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
                    addAccount(req, resp);
                    break;
                case "edit":
                    updateAccount(req, resp);
                    break;
                default:
                    listAccounts(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMsg", "操作失败：" + e.getMessage());
            try {
                listAccounts(req, resp);
            } catch (Exception ex) {
                throw new ServletException(ex);
            }
        }
    }

    private void listAccounts(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String pageNumStr = req.getParameter("pageNum");
        String pageSizeStr = req.getParameter("pageSize");
        String keyword = req.getParameter("keyword");
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

        PageResult<Account> pageResult = accountService.findByPage(pageNum, pageSize, keyword, status);

        req.setAttribute("pageResult", pageResult);
        req.setAttribute("keyword", keyword);
        req.setAttribute("status", status);

        req.getRequestDispatcher("/WEB-INF/jsp/manage/account/list.jsp").forward(req, resp);
    }

    private void showAddForm(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        List<Customer> approvedCustomers = customerService.findByPage(1, Integer.MAX_VALUE, null, 2).getList();
        req.setAttribute("customers", approvedCustomers);

        String customerIdStr = req.getParameter("customerId");
        if (customerIdStr != null && !customerIdStr.isEmpty()) {
            req.setAttribute("selectedCustomerId", Long.parseLong(customerIdStr));
        }

        req.getRequestDispatcher("/WEB-INF/jsp/manage/account/form.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/manage/account");
            return;
        }

        Long id = Long.parseLong(idStr);
        Account account = accountService.findById(id);
        req.setAttribute("account", account);
        req.setAttribute("formAction", "edit");

        List<Customer> approvedCustomers = customerService.findByPage(1, Integer.MAX_VALUE, null, 2).getList();
        req.setAttribute("customers", approvedCustomers);

        req.getRequestDispatcher("/WEB-INF/jsp/manage/account/form.jsp").forward(req, resp);
    }

    private void viewAccount(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/manage/account");
            return;
        }

        Long id = Long.parseLong(idStr);
        Account account = accountService.findById(id);
        List<Account> customerAccounts = accountService.findByCustomerId(account.getCustomerId());

        req.setAttribute("account", account);
        req.setAttribute("customerAccounts", customerAccounts);

        req.getRequestDispatcher("/WEB-INF/jsp/manage/account/view.jsp").forward(req, resp);
    }

    private void addAccount(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Account account = extractAccountFromRequest(req);

        HttpSession session = req.getSession(false);
        Long auditBy = null;
        if (session != null && session.getAttribute("loginUser") != null) {
            SysUser user = (SysUser) session.getAttribute("loginUser");
            auditBy = user.getId();
        }

        boolean result = accountService.save(account, auditBy);
        if (result) {
            resp.sendRedirect(req.getContextPath() + "/manage/account");
        } else {
            req.setAttribute("errorMsg", "开户失败，请确保客户已通过审核");
            List<Customer> approvedCustomers = customerService.findByPage(1, Integer.MAX_VALUE, null, 2).getList();
            req.setAttribute("customers", approvedCustomers);
            req.setAttribute("account", account);
            req.getRequestDispatcher("/WEB-INF/jsp/manage/account/form.jsp").forward(req, resp);
        }
    }

    private void updateAccount(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/manage/account");
            return;
        }

        Account account = extractAccountFromRequest(req);
        account.setId(Long.parseLong(idStr));

        boolean result = accountService.update(account);
        if (result) {
            resp.sendRedirect(req.getContextPath() + "/manage/account");
        } else {
            req.setAttribute("errorMsg", "更新账户失败");
            req.setAttribute("account", account);
            req.setAttribute("formAction", "edit");
            List<Customer> approvedCustomers = customerService.findByPage(1, Integer.MAX_VALUE, null, 2).getList();
            req.setAttribute("customers", approvedCustomers);
            req.getRequestDispatcher("/WEB-INF/jsp/manage/account/form.jsp").forward(req, resp);
        }
    }

    private void deleteAccount(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/manage/account");
            return;
        }

        Long id = Long.parseLong(idStr);
        accountService.delete(id);
        resp.sendRedirect(req.getContextPath() + "/manage/account");
    }

    private Account extractAccountFromRequest(HttpServletRequest req) {
        Account account = new Account();
        String customerIdStr = req.getParameter("customerId");
        if (customerIdStr != null && !customerIdStr.isEmpty()) {
            account.setCustomerId(Long.parseLong(customerIdStr));
        }
        account.setAccountType(req.getParameter("accountType"));
        String balanceStr = req.getParameter("balance");
        if (balanceStr != null && !balanceStr.isEmpty()) {
            account.setBalance(new BigDecimal(balanceStr));
        }
        account.setCurrency(req.getParameter("currency"));
        String statusStr = req.getParameter("status");
        if (statusStr != null && !statusStr.isEmpty()) {
            account.setStatus(Integer.parseInt(statusStr));
        }
        account.setOpenWay(req.getParameter("openWay"));
        account.setOpenChannel(req.getParameter("openChannel"));
        account.setRemark(req.getParameter("remark"));
        return account;
    }
}
