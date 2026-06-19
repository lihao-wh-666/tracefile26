package com.opening.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AuditRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long customerId;
    private String auditType;
    private Integer auditStatus;
    private String auditResult;
    private Long auditBy;
    private LocalDateTime auditTime;
    private LocalDateTime createTime;

    private String customerName;
    private String customerNo;
    private String auditorName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public String getAuditResult() {
        return auditResult;
    }

    public void setAuditResult(String auditResult) {
        this.auditResult = auditResult;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Long getAuditBy() {
        return auditBy;
    }

    public void setAuditBy(Long auditBy) {
        this.auditBy = auditBy;
    }

    public LocalDateTime getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(LocalDateTime auditTime) {
        this.auditTime = auditTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getAuditorName() {
        return auditorName;
    }

    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }

    public String getAuditTypeDesc() {
        if ("CUSTOMER_INFO".equals(auditType)) {
            return "客户信息审核";
        } else if ("ID_VERIFY".equals(auditType)) {
            return "身份认证";
        } else if ("OPEN_ACCOUNT".equals(auditType)) {
            return "开户审核";
        }
        return "未知";
    }

    public String getAuditStatusDesc() {
        if (auditStatus == null) return "未知";
        switch (auditStatus) {
            case 0: return "待审核";
            case 1: return "审核通过";
            case 2: return "审核拒绝";
            default: return "未知";
        }
    }
}
