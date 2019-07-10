package com.b4pay.order.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName LotteryRecored
 * @Description
 * @Version 2.1
 **/
@Entity
@Table(name = "dst_lotteryRecord")
public class LotteryRecord {

    @Id
    private String id;

    private Integer period;

    private String agencyId;

    private BigDecimal numberOneMoney;

    private BigDecimal numberTwoMoney;

    private BigDecimal numberThreeMoney;

    private BigDecimal numberFourMoney;

    private BigDecimal numberFiveMoney;

    private BigDecimal numberSixMoney;

    private BigDecimal numberSevenMoney;

    private BigDecimal totalMoney;

    //@JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public LotteryRecord() {
    }

    public LotteryRecord(String id, Integer period, String agencyId, BigDecimal numberOneMoney, BigDecimal numberTwoMoney, BigDecimal numberThreeMoney, BigDecimal numberFourMoney, BigDecimal numberFiveMoney, BigDecimal numberSixMoney, BigDecimal numberSevenMoney, BigDecimal totalMoney, Date createTime) {
        this.id = id;
        this.period = period;
        this.agencyId = agencyId;
        this.numberOneMoney = numberOneMoney;
        this.numberTwoMoney = numberTwoMoney;
        this.numberThreeMoney = numberThreeMoney;
        this.numberFourMoney = numberFourMoney;
        this.numberFiveMoney = numberFiveMoney;
        this.numberSixMoney = numberSixMoney;
        this.numberSevenMoney = numberSevenMoney;
        this.totalMoney = totalMoney;
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
    }

    public BigDecimal getNumberOneMoney() {
        return numberOneMoney;
    }

    public void setNumberOneMoney(BigDecimal numberOneMoney) {
        this.numberOneMoney = numberOneMoney;
    }

    public BigDecimal getNumberTwoMoney() {
        return numberTwoMoney;
    }

    public void setNumberTwoMoney(BigDecimal numberTwoMoney) {
        this.numberTwoMoney = numberTwoMoney;
    }

    public BigDecimal getNumberThreeMoney() {
        return numberThreeMoney;
    }

    public void setNumberThreeMoney(BigDecimal numberThreeMoney) {
        this.numberThreeMoney = numberThreeMoney;
    }

    public BigDecimal getNumberFourMoney() {
        return numberFourMoney;
    }

    public void setNumberFourMoney(BigDecimal numberFourMoney) {
        this.numberFourMoney = numberFourMoney;
    }

    public BigDecimal getNumberFiveMoney() {
        return numberFiveMoney;
    }

    public void setNumberFiveMoney(BigDecimal numberFiveMoney) {
        this.numberFiveMoney = numberFiveMoney;
    }

    public BigDecimal getNumberSixMoney() {
        return numberSixMoney;
    }

    public void setNumberSixMoney(BigDecimal numberSixMoney) {
        this.numberSixMoney = numberSixMoney;
    }

    public BigDecimal getNumberSevenMoney() {
        return numberSevenMoney;
    }

    public void setNumberSevenMoney(BigDecimal numberSevenMoney) {
        this.numberSevenMoney = numberSevenMoney;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }
}
