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
@Table(name = "dst_lottery")
public class Lottery implements Serializable {

    @Id
    private Integer period;

    private Integer numberOne;

    private Integer numberTwo;

    private Integer numberThree;

    private Integer numberFour;

    private Integer numberFive;

    private Integer numberSix;

    private Integer numberSeven;

    //@JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    public Lottery() {

    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
