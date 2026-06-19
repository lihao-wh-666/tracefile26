package com.opening.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String customerNo;
    private String realName;
    private String idType;
    private String idNo;
    private String gender;
    private LocalDate birthDate;
    private String phone;
    private String email;
    private String address;
    private String occupation;
    private String nationality;
    private Integer status;
    private Integer auditStatus;
    private Integer idVerified;
    private Integer faceVerified;
    private String remark;
    private Long createBy;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Integer getIdVerified() {
        return idVerified;
    }

    public void setIdVerified(Integer idVerified) {
        this.idVerified = idVerified;
    }

    public Integer getFaceVerified() {
        return faceVerified;
    }

    public void setFaceVerified(Integer faceVerified) {
        this.faceVerified = faceVerified;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
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

    public String getIdTypeDesc() {
        if ("ID_CARD".equals(idType)) {
            return "身份证";
        } else if ("PASSPORT".equals(idType)) {
            return "护照";
        }
        return "其他";
    }

    public String getGenderDesc() {
        return "M".equals(gender) ? "男" : "F".equals(gender) ? "女" : "未知";
    }

    public String getStatusDesc() {
        if (status == null) return "未知";
        switch (status) {
            case 0: return "待审核";
            case 1: return "审核通过";
            case 2: return "审核拒绝";
            case 3: return "已冻结";
            default: return "未知";
        }
    }

    public String getAuditStatusDesc() {
        if (auditStatus == null) return "未知";
        switch (auditStatus) {
            case 0: return "待审核";
            case 1: return "审核中";
            case 2: return "已通过";
            case 3: return "已拒绝";
            default: return "未知";
        }
    }

    public String getIdVerifiedDesc() {
        return idVerified != null && idVerified == 1 ? "已认证" : "未认证";
    }

    public String getFaceVerifiedDesc() {
        return faceVerified != null && faceVerified == 1 ? "已认证" : "未认证";
    }
}
