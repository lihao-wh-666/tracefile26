package com.opening.servlet;

import com.opening.service.AccountService;
import com.opening.service.CustomerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/manage/index")
public class IndexServlet extends HttpServlet {

    private CustomerService customerService = new CustomerService();
    private AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Map<String, Long> customerStats = customerService.getStatistics();
            Map<String, Object> accountStats = accountService.getStatistics();

            req.setAttribute("customerStats", customerStats);
            req.setAttribute("accountStats", accountStats);

            req.getRequestDispatcher("/WEB-INF/jsp/manage/index.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMsg", "加载数据失败：" + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/manage/index.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
