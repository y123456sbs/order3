package com.b4pay.order.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName Lottery
 * @Description
 * @Version 2.1
 **/
@Entity
@Table(name = "dst_lotteryResult")
public class LotteryResult implements Serializable {

    @Id
    private Integer period;

    private Integer numberOne;

    private BigDecimal numberOneMoney;

    private Integer numberTwo;

    private BigDecimal numberTwoMoney;

    private Integer numberThree;

    private BigDecimal numberThreeMoney;

    private Integer numberFour;

    private BigDecimal numberFourMoney;

    private Integer numberFive;

    private BigDecimal numberFiveMoney;

    private Integer numberSix;

    private BigDecimal numberSixMoney;

    private Integer numberSeven;

    private BigDecimal numberSevenMoney;

    //@JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;



    public LotteryResult() {

    }

    public LotteryResult(Integer period, Integer numberOne, BigDecimal numberOneMoney, Integer numberTwo, BigDecimal numberTwoMoney, Integer numberThree, BigDecimal numberThreeMoney, Integer numberFour, BigDecimal numberFourMoney, Integer numberFive, BigDecimal numberFiveMoney, Integer numberSix, BigDecimal numberSixMoney, Integer numberSeven, BigDecimal numberSevenMoney, Date createTime) {
        this.period = period;
        this.numberOne = numberOne;
        this.numberOneMoney = numberOneMoney;
        this.numberTwo = numberTwo;
        this.numberTwoMoney = numberTwoMoney;
        this.numberThree = numberThree;
        this.numberThreeMoney = numberThreeMoney;
        this.numberFour = numberFour;
        this.numberFourMoney = numberFourMoney;
        this.numberFive = numberFive;
        this.numberFiveMoney = numberFiveMoney;
        this.numberSix = numberSix;
        this.numberSixMoney = numberSixMoney;
        this.numberSeven = numberSeven;
        this.numberSevenMoney = numberSevenMoney;
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
}
