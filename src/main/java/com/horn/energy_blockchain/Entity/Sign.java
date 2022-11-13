package com.horn.energy_blockchain.Entity;

import java.io.Serializable;

public class Sign implements Serializable {
    private Long id;

    private String name;

    /**
     * 浜岃繘鍒朵繚瀛樻瘡涓湀鐨勭鍒?
     *
     * @mbg.generated
     */
    private String signCode;

    private Integer total;

    private String month;

    /**
     * 瑙掕壊0 鏅€氱敤鎴?瑙掕壊1 绛惧埌鍙戣捣鑰?
     *
     * @mbg.generated
     */
    private Integer role;

    /**
     * 签到发起者标识的特殊编码
     *
     * @mbg.generated
     */
    private String adminCode;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignCode() {
        return signCode;
    }

    public void setSignCode(String signCode) {
        this.signCode = signCode;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getAdminCode() {
        return adminCode;
    }

    public void setAdminCode(String adminCode) {
        this.adminCode = adminCode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", signCode=").append(signCode);
        sb.append(", total=").append(total);
        sb.append(", month=").append(month);
        sb.append(", role=").append(role);
        sb.append(", adminCode=").append(adminCode);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}