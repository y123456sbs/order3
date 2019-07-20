package com.b4pay.order.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName PersonalCount
 * @Description
 * @Version 2.1
 **/

@Entity
@Table(name = "dst_personal_count")
public class PersonalCount {

    @Id
    private String agencyId;

    private BigDecimal cnMoney;

    private BigDecimal totalMoney;

    private BigDecimal countMoney;

    private Date createTime;

    public PersonalCount(String agencyId, BigDecimal cnMoney, BigDecimal totalMoney, BigDecimal countMoney, Date createTime) {
        this.agencyId = agencyId;
        this.cnMoney = cnMoney;
        this.totalMoney = totalMoney;
        this.countMoney = countMoney;
        this.createTime = createTime;
    }

    public PersonalCount() {
    }


    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
    }

    public BigDecimal getCnMoney() {
        return cnMoney;
    }

    public void setCnMoney(BigDecimal cnMoney) {
        this.cnMoney = cnMoney;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public BigDecimal getCountMoney() {
        return countMoney;
    }

    public void setCountMoney(BigDecimal countMoney) {
        this.countMoney = countMoney;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
