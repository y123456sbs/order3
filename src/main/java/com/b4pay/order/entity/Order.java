package com.b4pay.order.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName Order
 * @Description
 * @Version 2.1
 **/

@Entity
@Table(name = "dst_order")
public class Order implements Serializable {

    @Id
    private String id;

    private String agencyId;

    private Integer period;  //期数

    private Integer numberOne;

    private Integer numberTwo;

    private Integer numberThree;

    private Integer numberFour;

    private Integer numberFive;

    private Integer numberSix;

    private Integer numberSeven;

    private BigDecimal money; //下注金额

    private Date createTime;





    public Order() {
    }

    public Order(String id, String agencyId, Integer period, Integer numberOne, Integer numberTwo, Integer numberThree, Integer numberFour, Integer numberFive, Integer numberSix, Integer numberSeven, BigDecimal money, Date createTime) {
        this.id = id;
        this.agencyId = agencyId;
        this.period = period;
        this.numberOne = numberOne;
        this.numberTwo = numberTwo;
        this.numberThree = numberThree;
        this.numberFour = numberFour;
        this.numberFive = numberFive;
        this.numberSix = numberSix;
        this.numberSeven = numberSeven;
        this.money = money;
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = new Date();
    }

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
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

    public Integer getNumberOne() {
        return numberOne;
    }

    public void setNumberOne(Integer numberOne) {
        this.numberOne = numberOne;
    }

    public Integer getNumberTwo() {
        return numberTwo;
    }

    public void setNumberTwo(Integer numberTwo) {
        this.numberTwo = numberTwo;
    }

    public Integer getNumberThree() {
        return numberThree;
    }

    public void setNumberThree(Integer numberThree) {
        this.numberThree = numberThree;
    }

    public Integer getNumberFour() {
        return numberFour;
    }

    public void setNumberFour(Integer numberFour) {
        this.numberFour = numberFour;
    }

    public Integer getNumberFive() {
        return numberFive;
    }

    public void setNumberFive(Integer numberFive) {
        this.numberFive = numberFive;
    }

    public Integer getNumberSix() {
        return numberSix;
    }

    public void setNumberSix(Integer numberSix) {
        this.numberSix = numberSix;
    }

    public Integer getNumberSeven() {
        return numberSeven;
    }

    public void setNumberSeven(Integer numberSeven) {
        this.numberSeven = numberSeven;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", agencyId='" + agencyId + '\'' +
                ", period=" + period +
                ", numberOne=" + numberOne +
                ", numberTwo=" + numberTwo +
                ", numberThree=" + numberThree +
                ", numberFour=" + numberFour +
                ", numberFive=" + numberFive +
                ", numberSix=" + numberSix +
                ", numberSeven=" + numberSeven +
                ", money=" + money +
                ", createTime=" + createTime +
                '}';
    }
}
