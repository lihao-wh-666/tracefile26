package com.opening.servlet;

import com.opening.entity.AuditRecord;
import com.opening.service.AuditRecordService;
import com.opening.utils.PageResult;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/manage/audit")
public class AuditRecordServlet extends HttpServlet {

    private AuditRecordService auditRecordService = new AuditRecordService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null || action.isEmpty()) {
            action = "list";
        }

        try {
            switch (action) {
                case "list":
                    listRecords(req, resp);
                    break;
                case "view":
                    viewRecord(req, resp);
                    break;
                default:
                    listRecords(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMsg", "操作失败：" + e.getMessage());
            try {
                listRecords(req, resp);
            } catch (Exception ex) {
                throw new ServletException(ex);
            }
        }
    }

    private void listRecords(HttpServletRequest req, HttpServletResponse resp) throws Exception {
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

        PageResult<AuditRecord> pageResult = auditRecordService.findByPage(pageNum, pageSize, keyword, auditStatus);

        req.setAttribute("pageResult", pageResult);
        req.setAttribute("keyword", keyword);
        req.setAttribute("auditStatus", auditStatus);

        req.getRequestDispatcher("/WEB-INF/jsp/manage/audit/list.jsp").forward(req, resp);
    }

    private void viewRecord(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/manage/audit");
            return;
        }

        Long id = Long.parseLong(idStr);
        AuditRecord record = auditRecordService.findById(id);
        req.setAttribute("record", record);

        req.getRequestDispatcher("/WEB-INF/jsp/manage/audit/view.jsp").forward(req, resp);
    }
}
