package com.b4pay.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName GradeRecord
 * @Description
 * @Version 2.1
 **/
@Entity
@Table(name = "dst_lottery_grade_record")
public class GradeRecord implements Serializable {

    @Id
    private String id;

    private Integer period;

    private String agencyId;

    private String grade;

    private String gradeId;

    private String gradeName;

    private BigDecimal numberOneMoney;

    private BigDecimal numberTwoMoney;

    private BigDecimal numberThreeMoney;

    private BigDecimal numberFourMoney;

    private BigDecimal numberFiveMoney;

    private BigDecimal numberSixMoney;

    private BigDecimal numberSevenMoney;

    private BigDecimal totalMoney;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date orderTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String type;

    public GradeRecord() {

    }

    public GradeRecord(String id, Integer period, String agencyId, String grade, String gradeId, String gradeName, BigDecimal numberOneMoney, BigDecimal numberTwoMoney, BigDecimal numberThreeMoney, BigDecimal numberFourMoney, BigDecimal numberFiveMoney, BigDecimal numberSixMoney, BigDecimal numberSevenMoney, BigDecimal totalMoney, Date orderTime, Date createTime, String type) {
        this.id = id;
        this.period = period;
        this.agencyId = agencyId;
        this.grade = grade;
        this.gradeId = gradeId;
        this.gradeName = gradeName;
        this.numberOneMoney = numberOneMoney;
        this.numberTwoMoney = numberTwoMoney;
        this.numberThreeMoney = numberThreeMoney;
        this.numberFourMoney = numberFourMoney;
        this.numberFiveMoney = numberFiveMoney;
        this.numberSixMoney = numberSixMoney;
        this.numberSevenMoney = numberSevenMoney;
        this.totalMoney = totalMoney;
        this.orderTime = orderTime;
        this.createTime = createTime;
        this.type = type;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
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

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }
}
