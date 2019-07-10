package com.b4pay.order.service;

import com.b4pay.order.dao.CountDao;
import com.b4pay.order.dao.LotteryDao;
import com.b4pay.order.dao.LotteryRecordDao;
import com.b4pay.order.dao.LotteryResultDao;
import com.b4pay.order.entity.*;
import com.b4pay.order.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @ClassName LotteryService
 * @Description
 * @Version 2.1
 **/
@Service
public class LotteryService {

    @Autowired
    private LotteryDao lotteryDao;

    @Autowired
    private CountDao countDao;

    @Autowired
    private LotteryResultDao lotteryResultDao;

    @Autowired
    private LotteryRecordDao lotteryRecordDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private SimpleDateFormat simpleDateFormat;

    /**
     * 查询所有
     */
    public List<Lottery> findAll() {
        return lotteryDao.findAll();
    }

    /**
     * 查询一个
     */
    public Lottery findById(Integer period) {
        return lotteryDao.findById(period).get();
    }

    public Lottery queryByPeriod(Integer period) {
        return lotteryDao.queryByPeriod(period);
    }

    /***/
    public void save(Integer period, List<Integer> numsList,Date date) {
        Lottery lottery = new Lottery();
        lottery.setPeriod(period);
        lottery.setNumberOne(numsList.get(0));
        lottery.setNumberTwo(numsList.get(1));
        lottery.setNumberThree(numsList.get(2));
        lottery.setNumberFour(numsList.get(3));
        lottery.setNumberFive(numsList.get(4));
        lottery.setNumberSix(numsList.get(5));
        lottery.setNumberSeven(numsList.get(6));
        lottery.setCreateTime(date);
        lotteryDao.save(lottery);
    }

    @Transactional
    public void match(Integer period, List<Integer> numsList, List<Order> orderList, Date date) {
        Integer num1 = numsList.get(0);
        Integer num2 = numsList.get(1);
        Integer num3 = numsList.get(2);
        Integer num4 = numsList.get(3);
        Integer num5 = numsList.get(4);
        Integer num6 = numsList.get(5);
        Integer num7 = numsList.get(6);
        //奖池结果
        LotteryResult lotteryResult = new LotteryResult();
        lotteryResult.setPeriod(period);
        lotteryResult.setNumberOne(num1);
        lotteryResult.setNumberTwo(num2);
        lotteryResult.setNumberThree(num3);
        lotteryResult.setNumberFour(num4);
        lotteryResult.setNumberFive(num5);
        lotteryResult.setNumberSix(num6);
        lotteryResult.setNumberSeven(num7);

        BigDecimal count1 = new BigDecimal("0");
        BigDecimal count2 = new BigDecimal("0");
        BigDecimal count3 = new BigDecimal("0");
        BigDecimal count4 = new BigDecimal("0");
        BigDecimal count5 = new BigDecimal("0");
        BigDecimal count6 = new BigDecimal("0");
        BigDecimal count7 = new BigDecimal("0");

        for (Order order : orderList) {
            if (order.getNumberOne() != null && num1 == order.getNumberOne()) {
                count1 = count1.add(new BigDecimal("1"));
            }
            if (order.getNumberTwo() != null && num2 == order.getNumberTwo()) {
                count2 = count2.add(new BigDecimal("1"));
            }
            if (order.getNumberThree() != null && num3 == order.getNumberThree()) {
                count3 = count3.add(new BigDecimal("1"));
            }
            if (order.getNumberFour() != null && num4 == order.getNumberFour()) {
                count4 = count4.add(new BigDecimal("1"));
            }
            if (order.getNumberFive() != null && num5 == order.getNumberFive()) {
                count5 = count5.add(new BigDecimal("1"));
            }
            if (order.getNumberSix() != null && num6 == order.getNumberSix()) {
                count6 = count6.add(new BigDecimal("1"));
            }
            if (order.getNumberSeven() != null && num7 == order.getNumberSeven()) {
                count7 = count7.add(new BigDecimal("1"));
            }
        }

        Count count = countDao.queryByPeriod(period);

        //记录每个奖池平分金额
        BigDecimal numberOneMoney = null;
        BigDecimal numberTwoMoney = null;
        BigDecimal numberThreeMoney = null;
        BigDecimal numberFourMoney = null;
        BigDecimal numberFiveMoney = null;
        BigDecimal numberSixMoney = null;
        BigDecimal numberSevenMoney = null;
        if (!count1.equals(BigDecimal.ZERO)) {
            numberOneMoney = count.getNumberOneTotalMoney().divide(count1, 2, BigDecimal.ROUND_HALF_UP);
        }
        if (!count2.equals(BigDecimal.ZERO)) {
            numberTwoMoney = count.getNumberOneTotalMoney().divide(count2, 2, BigDecimal.ROUND_HALF_UP);
        }
        if (!count3.equals(BigDecimal.ZERO)) {
            numberThreeMoney = count.getNumberOneTotalMoney().divide(count3, 2, BigDecimal.ROUND_HALF_UP);
        }
        if (!count4.equals(BigDecimal.ZERO)) {
            numberFourMoney = count.getNumberOneTotalMoney().divide(count4, 2, BigDecimal.ROUND_HALF_UP);
        }
        if (!count5.equals(BigDecimal.ZERO)) {
            numberFiveMoney = count.getNumberOneTotalMoney().divide(count5, 2, BigDecimal.ROUND_HALF_UP);
        }
        if (!count6.equals(BigDecimal.ZERO)) {
            numberSixMoney = count.getNumberOneTotalMoney().divide(count6, 2, BigDecimal.ROUND_HALF_UP);
        }
        if (!count7.equals(BigDecimal.ZERO)) {
            numberSevenMoney = count.getNumberOneTotalMoney().divide(count7, 2, BigDecimal.ROUND_HALF_UP);
        }


        lotteryResult.setNumberOneMoney(numberOneMoney);
        lotteryResult.setNumberTwoMoney(numberTwoMoney);
        lotteryResult.setNumberThreeMoney(numberThreeMoney);
        lotteryResult.setNumberFourMoney(numberFourMoney);
        lotteryResult.setNumberFiveMoney(numberFiveMoney);
        lotteryResult.setNumberSixMoney(numberSixMoney);
        lotteryResult.setNumberSevenMoney(numberSevenMoney);

        lotteryResult.setCreateTime(date);

        lotteryResultDao.save(lotteryResult);


        //记录每个用户中奖金额
        for (Order order : orderList) {
            LotteryRecord lotteryRecord = new LotteryRecord();
            lotteryRecord.setId(idWorker.nextId()+"");
            lotteryRecord.setPeriod(period);
            lotteryRecord.setAgencyId(order.getAgencyId());
            BigDecimal totalMoney = new BigDecimal("0");
            if (order.getNumberOne() != null && num1 == order.getNumberOne()) {
                lotteryRecord.setNumberOneMoney(numberOneMoney);
                totalMoney = totalMoney.add(numberOneMoney);
            }
            if (order.getNumberTwo() != null && num2 == order.getNumberTwo()) {
                lotteryRecord.setNumberTwoMoney(numberTwoMoney);
                totalMoney = totalMoney.add(numberTwoMoney);
            }
            if (order.getNumberThree() != null && num3 == order.getNumberThree()) {
                lotteryRecord.setNumberThreeMoney(numberThreeMoney);
                totalMoney = totalMoney.add(numberThreeMoney);
            }
            if (order.getNumberFour() != null && num4 == order.getNumberFour()) {
                lotteryRecord.setNumberFourMoney(numberFourMoney);
                totalMoney = totalMoney.add(numberFourMoney);
            }
            if (order.getNumberFive() != null && num5 == order.getNumberFive()) {
                lotteryRecord.setNumberFiveMoney(numberFiveMoney);
                totalMoney = totalMoney.add(numberFiveMoney);
            }
            if (order.getNumberSix() != null && num6 == order.getNumberSix()) {
                lotteryRecord.setNumberSixMoney(numberSixMoney);
                totalMoney = totalMoney.add(numberSixMoney);
            }
            if (order.getNumberSeven() != null && num7 == order.getNumberSeven()) {
                lotteryRecord.setNumberSevenMoney(numberSevenMoney);
                totalMoney = totalMoney.add(numberSevenMoney);
            }
            lotteryRecord.setTotalMoney(totalMoney);
            lotteryRecord.setCreateTime(date);
            lotteryRecordDao.save(lotteryRecord);
        }

    }


    public List<Lottery> findAllOrderByPeriod() {
        /*Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "period"));
        List<Lottery> lotteryList = lotteryDao.findAll(sort);*/
        List<Lottery> lotteryList = lotteryDao.findAllOrderByPeriodDesc();
        return lotteryList;
    }

    public List<Lottery> findByDate(Date startDate,Date endDate) {
        List<Lottery> lotteryList = lotteryDao.findAllBetweenStartAndEnd(startDate,endDate);
        return lotteryList;
    }

    public Lottery findLattestLottery() {
        Lottery lottery = lotteryDao.findLatterLottery();
        return lottery;
    }
}

