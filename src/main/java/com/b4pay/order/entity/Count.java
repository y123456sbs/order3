package com.b4pay.order.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName Count
 * @Version 2.1
 **/
@Entity
@Table(name = "dst_count")
public class Count implements Serializable {

    @Id
    private Integer period;

    private BigDecimal numberOneTotalMoney;

    private BigDecimal numberTwoTotalMoney;

    private BigDecimal numberThreeTotalMoney;

    private BigDecimal numberFourTotalMoney;

    private BigDecimal numberFiveTotalMoney;

    private BigDecimal numberSixTotalMoney;

    private BigDecimal numberSevenTotalMoney;

    private BigDecimal countMoney;

    public Count() {
    }

    public Count(Integer period, BigDecimal numberOneTotalMoney, BigDecimal numberTwoTotalMoney, BigDecimal numberThreeTotalMoney, BigDecimal numberFourTotalMoney, BigDecimal numberFiveTotalMoney, BigDecimal numberSixTotalMoney, BigDecimal numberSevenTotalMoney, BigDecimal countMoney) {
        this.period = period;
        this.numberOneTotalMoney = numberOneTotalMoney;
        this.numberTwoTotalMoney = numberTwoTotalMoney;
        this.numberThreeTotalMoney = numberThreeTotalMoney;
        this.numberFourTotalMoney = numberFourTotalMoney;
        this.numberFiveTotalMoney = numberFiveTotalMoney;
        this.numberSixTotalMoney = numberSixTotalMoney;
        this.numberSevenTotalMoney = numberSevenTotalMoney;
        this.countMoney = countMoney;
    }

    public BigDecimal getNumberOneTotalMoney() {
        return numberOneTotalMoney;
    }

    public void setNumberOneTotalMoney(BigDecimal numberOneTotalMoney) {
        this.numberOneTotalMoney = numberOneTotalMoney;
    }

    public BigDecimal getNumberTwoTotalMoney() {
        return numberTwoTotalMoney;
    }

    public void setNumberTwoTotalMoney(BigDecimal numberTwoTotalMoney) {
        this.numberTwoTotalMoney = numberTwoTotalMoney;
    }

    public BigDecimal getNumberThreeTotalMoney() {
        return numberThreeTotalMoney;
    }

    public void setNumberThreeTotalMoney(BigDecimal numberThreeTotalMoney) {
        this.numberThreeTotalMoney = numberThreeTotalMoney;
    }

    public BigDecimal getNumberFourTotalMoney() {
        return numberFourTotalMoney;
    }

    public void setNumberFourTotalMoney(BigDecimal numberFourTotalMoney) {
        this.numberFourTotalMoney = numberFourTotalMoney;
    }

    public BigDecimal getNumberFiveTotalMoney() {
        return numberFiveTotalMoney;
    }

    public void setNumberFiveTotalMoney(BigDecimal numberFiveTotalMoney) {
        this.numberFiveTotalMoney = numberFiveTotalMoney;
    }

    public BigDecimal getNumberSixTotalMoney() {
        return numberSixTotalMoney;
    }

    public void setNumberSixTotalMoney(BigDecimal numberSixTotalMoney) {
        this.numberSixTotalMoney = numberSixTotalMoney;
    }

    public BigDecimal getNumberSevenTotalMoney() {
        return numberSevenTotalMoney;
    }

    public void setNumberSevenTotalMoney(BigDecimal numberSevenTotalMoney) {
        this.numberSevenTotalMoney = numberSevenTotalMoney;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public BigDecimal getCountMoney() {
        return countMoney;
    }

    public void setCountMoney(BigDecimal countMoney) {
        this.countMoney = countMoney;
    }


}
