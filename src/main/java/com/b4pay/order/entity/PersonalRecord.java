package com.b4pay.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName PersonalRecord
 * @Description
 * @Version 2.1
 **/

@Entity
@Table(name = "dst_personal_record")
public class PersonalRecord {

    @Id
    private String id;

    private String agencyId;

    private Integer period;

    private String grade;

    private BigDecimal numberOneTotalMoney;//1总金额

    private BigDecimal numberOneCnTotalMoney;//1不可提金额

    private BigDecimal numberTwoTotalMoney;

    private BigDecimal numberTwoCnTotalMoney;

    private BigDecimal numberThreeTotalMoney;

    private BigDecimal numberThreeCnTotalMoney;

    private BigDecimal numberFourTotalMoney;

    private BigDecimal numberFourCnTotalMoney;

    private BigDecimal numberFiveTotalMoney;

    private BigDecimal numberFiveCnTotalMoney;

    private BigDecimal numberSixTotalMoney;

    private BigDecimal numberSixCnTotalMoney;

    private BigDecimal numberSevenTotalMoney;

    private BigDecimal numberSevenCnTotalMoney;

    private BigDecimal TotalCnMoney;

    private BigDecimal TotalMoney;//累积金额

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public PersonalRecord() {
    }

    public PersonalRecord(String agencyId, Integer period, String grade, BigDecimal numberOneTotalMoney, BigDecimal numberOneCnTotalMoney, BigDecimal numberTwoTotalMoney, BigDecimal numberTwoCnTotalMoney, BigDecimal numberThreeTotalMoney, BigDecimal numberThreeCnTotalMoney, BigDecimal numberFourTotalMoney, BigDecimal numberFourCnTotalMoney, BigDecimal numberFiveTotalMoney, BigDecimal numberFiveCnTotalMoney, BigDecimal numberSixTotalMoney, BigDecimal numberSixCnTotalMoney, BigDecimal numberSevenTotalMoney, BigDecimal numberSevenCnTotalMoney, BigDecimal totalCnMoney, BigDecimal totalMoney, Date createTime) {
        this.agencyId = agencyId;
        this.period = period;
        this.grade = grade;
        this.numberOneTotalMoney = numberOneTotalMoney;
        this.numberOneCnTotalMoney = numberOneCnTotalMoney;
        this.numberTwoTotalMoney = numberTwoTotalMoney;
        this.numberTwoCnTotalMoney = numberTwoCnTotalMoney;
        this.numberThreeTotalMoney = numberThreeTotalMoney;
        this.numberThreeCnTotalMoney = numberThreeCnTotalMoney;
        this.numberFourTotalMoney = numberFourTotalMoney;
        this.numberFourCnTotalMoney = numberFourCnTotalMoney;
        this.numberFiveTotalMoney = numberFiveTotalMoney;
        this.numberFiveCnTotalMoney = numberFiveCnTotalMoney;
        this.numberSixTotalMoney = numberSixTotalMoney;
        this.numberSixCnTotalMoney = numberSixCnTotalMoney;
        this.numberSevenTotalMoney = numberSevenTotalMoney;
        this.numberSevenCnTotalMoney = numberSevenCnTotalMoney;
        TotalCnMoney = totalCnMoney;
        TotalMoney = totalMoney;
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public BigDecimal getNumberOneTotalMoney() {
        return numberOneTotalMoney;
    }

    public void setNumberOneTotalMoney(BigDecimal numberOneTotalMoney) {
        this.numberOneTotalMoney = numberOneTotalMoney;
    }

    public BigDecimal getNumberOneCnTotalMoney() {
        return numberOneCnTotalMoney;
    }

    public void setNumberOneCnTotalMoney(BigDecimal numberOneCnTotalMoney) {
        this.numberOneCnTotalMoney = numberOneCnTotalMoney;
    }

    public BigDecimal getNumberTwoTotalMoney() {
        return numberTwoTotalMoney;
    }

    public void setNumberTwoTotalMoney(BigDecimal numberTwoTotalMoney) {
        this.numberTwoTotalMoney = numberTwoTotalMoney;
    }

    public BigDecimal getNumberTwoCnTotalMoney() {
        return numberTwoCnTotalMoney;
    }

    public void setNumberTwoCnTotalMoney(BigDecimal numberTwoCnTotalMoney) {
        this.numberTwoCnTotalMoney = numberTwoCnTotalMoney;
    }

    public BigDecimal getNumberThreeTotalMoney() {
        return numberThreeTotalMoney;
    }

    public void setNumberThreeTotalMoney(BigDecimal numberThreeTotalMoney) {
        this.numberThreeTotalMoney = numberThreeTotalMoney;
    }

    public BigDecimal getNumberThreeCnTotalMoney() {
        return numberThreeCnTotalMoney;
    }

    public void setNumberThreeCnTotalMoney(BigDecimal numberThreeCnTotalMoney) {
        this.numberThreeCnTotalMoney = numberThreeCnTotalMoney;
    }

    public BigDecimal getNumberFourTotalMoney() {
        return numberFourTotalMoney;
    }

    public void setNumberFourTotalMoney(BigDecimal numberFourTotalMoney) {
        this.numberFourTotalMoney = numberFourTotalMoney;
    }

    public BigDecimal getNumberFourCnTotalMoney() {
        return numberFourCnTotalMoney;
    }

    public void setNumberFourCnTotalMoney(BigDecimal numberFourCnTotalMoney) {
        this.numberFourCnTotalMoney = numberFourCnTotalMoney;
    }

    public BigDecimal getNumberFiveTotalMoney() {
        return numberFiveTotalMoney;
    }

    public void setNumberFiveTotalMoney(BigDecimal numberFiveTotalMoney) {
        this.numberFiveTotalMoney = numberFiveTotalMoney;
    }

    public BigDecimal getNumberFiveCnTotalMoney() {
        return numberFiveCnTotalMoney;
    }

    public void setNumberFiveCnTotalMoney(BigDecimal numberFiveCnTotalMoney) {
        this.numberFiveCnTotalMoney = numberFiveCnTotalMoney;
    }

    public BigDecimal getNumberSixTotalMoney() {
        return numberSixTotalMoney;
    }

    public void setNumberSixTotalMoney(BigDecimal numberSixTotalMoney) {
        this.numberSixTotalMoney = numberSixTotalMoney;
    }

    public BigDecimal getNumberSixCnTotalMoney() {
        return numberSixCnTotalMoney;
    }

    public void setNumberSixCnTotalMoney(BigDecimal numberSixCnTotalMoney) {
        this.numberSixCnTotalMoney = numberSixCnTotalMoney;
    }

    public BigDecimal getNumberSevenTotalMoney() {
        return numberSevenTotalMoney;
    }

    public void setNumberSevenTotalMoney(BigDecimal numberSevenTotalMoney) {
        this.numberSevenTotalMoney = numberSevenTotalMoney;
    }

    public BigDecimal getNumberSevenCnTotalMoney() {
        return numberSevenCnTotalMoney;
    }

    public void setNumberSevenCnTotalMoney(BigDecimal numberSevenCnTotalMoney) {
        this.numberSevenCnTotalMoney = numberSevenCnTotalMoney;
    }

    public BigDecimal getTotalCnMoney() {
        return TotalCnMoney;
    }

    public void setTotalCnMoney(BigDecimal totalCnMoney) {
        TotalCnMoney = totalCnMoney;
    }

    public BigDecimal getTotalMoney() {
        return TotalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        TotalMoney = totalMoney;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
