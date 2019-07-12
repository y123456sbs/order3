package com.b4pay.order.service;

import com.b4pay.order.dao.CountDao;
import com.b4pay.order.dao.LotteryDao;
import com.b4pay.order.dao.LotteryRecordDao;
import com.b4pay.order.dao.LotteryResultDao;
import com.b4pay.order.entity.*;
import com.b4pay.order.utils.IdWorker;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final String NUMBER = "number";
    private static final String ZODIAC = "zodiac";
    private static final String SIZE = "size";
    private static final String COLOR = "color";
    private static final String SD = "sd";

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
    public void save(Integer period, List<Integer> numsList, Date date) {
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
        //保存号码奖池
        this.saveNumberLotteryResult(period, numsList, orderList, date);
        //保存生肖奖池
        this.saveZodiacLotteryResult(period, numsList, orderList, date);

        //TODO 保存颜色奖池

        //保存单双奖池 0是双 1是单
        this.saveSDLotteryResult(period, numsList, orderList, date);

        //保存大小奖池 1-24小 26-49大
        this.saveSizeLotteryResult(period, numsList, orderList, date);


    }

    //保存生肖奖池
    private void saveZodiacLotteryResult(Integer period, List<Integer> numsList, List<Order> orderList, Date date) {
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
        lotteryResult.setType(ZODIAC);

        BigDecimal count1 = new BigDecimal("0");
        BigDecimal count2 = new BigDecimal("0");
        BigDecimal count3 = new BigDecimal("0");
        BigDecimal count4 = new BigDecimal("0");
        BigDecimal count5 = new BigDecimal("0");
        BigDecimal count6 = new BigDecimal("0");
        BigDecimal count7 = new BigDecimal("0");


        for (Order order : orderList) {
            if (order.getType().equals(ZODIAC)) {
                if (order.getNumberOne() != null && this.judgeZodiac(num1) == order.getNumberOne()) {
                    count1 = count1.add(new BigDecimal("1"));
                }
                if (order.getNumberTwo() != null && this.judgeZodiac(num2) == order.getNumberTwo()) {
                    count2 = count2.add(new BigDecimal("1"));
                }
                if (order.getNumberThree() != null && this.judgeZodiac(num3) == order.getNumberThree()) {
                    count3 = count3.add(new BigDecimal("1"));
                }
                if (order.getNumberFour() != null && this.judgeZodiac(num4) == order.getNumberFour()) {
                    count4 = count4.add(new BigDecimal("1"));
                }
                if (order.getNumberFive() != null && this.judgeZodiac(num5) == order.getNumberFive()) {
                    count5 = count5.add(new BigDecimal("1"));
                }
                if (order.getNumberSix() != null && this.judgeZodiac(num6) == order.getNumberSix()) {
                    count6 = count6.add(new BigDecimal("1"));
                }
                if (order.getNumberSeven() != null && this.judgeZodiac(num7) == order.getNumberSeven()) {
                    count7 = count7.add(new BigDecimal("1"));
                }
            }
        }

        Count count = countDao.queryByPeriod(period, ZODIAC);

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
            if (order.getType().equals(ZODIAC)) {
                LotteryRecord lotteryRecord = new LotteryRecord();
                lotteryRecord.setId(idWorker.nextId() + "");
                lotteryRecord.setPeriod(period);
                lotteryRecord.setAgencyId(order.getAgencyId());
                lotteryRecord.setType(ZODIAC);
                BigDecimal totalMoney = new BigDecimal("0");
                if (order.getNumberOne() != null && this.judgeZodiac(num1) == order.getNumberOne()) {
                    lotteryRecord.setNumberOneMoney(numberOneMoney);
                    totalMoney = totalMoney.add(numberOneMoney);
                }
                if (order.getNumberTwo() != null && this.judgeZodiac(num2) == order.getNumberTwo()) {
                    lotteryRecord.setNumberTwoMoney(numberTwoMoney);
                    totalMoney = totalMoney.add(numberTwoMoney);
                }
                if (order.getNumberThree() != null && this.judgeZodiac(num3) == order.getNumberThree()) {
                    lotteryRecord.setNumberThreeMoney(numberThreeMoney);
                    totalMoney = totalMoney.add(numberThreeMoney);
                }
                if (order.getNumberFour() != null && this.judgeZodiac(num4) == order.getNumberFour()) {
                    lotteryRecord.setNumberFourMoney(numberFourMoney);
                    totalMoney = totalMoney.add(numberFourMoney);
                }
                if (order.getNumberFive() != null && this.judgeZodiac(num5) == order.getNumberFive()) {
                    lotteryRecord.setNumberFiveMoney(numberFiveMoney);
                    totalMoney = totalMoney.add(numberFiveMoney);
                }
                if (order.getNumberSix() != null && this.judgeZodiac(num6) == order.getNumberSix()) {
                    lotteryRecord.setNumberSixMoney(numberSixMoney);
                    totalMoney = totalMoney.add(numberSixMoney);
                }
                if (order.getNumberSeven() != null && this.judgeZodiac(num7) == order.getNumberSeven()) {
                    lotteryRecord.setNumberSevenMoney(numberSevenMoney);
                    totalMoney = totalMoney.add(numberSevenMoney);
                }
                lotteryRecord.setTotalMoney(totalMoney);
                lotteryRecord.setCreateTime(date);
                lotteryRecordDao.save(lotteryRecord);
            }
        }
    }


    //保存大小奖池
    private void saveSizeLotteryResult(Integer period, List<Integer> numsList, List<Order> orderList, Date date) {
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
        lotteryResult.setType(SIZE);

        BigDecimal count1 = new BigDecimal("0");
        BigDecimal count2 = new BigDecimal("0");
        BigDecimal count3 = new BigDecimal("0");
        BigDecimal count4 = new BigDecimal("0");
        BigDecimal count5 = new BigDecimal("0");
        BigDecimal count6 = new BigDecimal("0");
        BigDecimal count7 = new BigDecimal("0");


        for (Order order : orderList) {
            if (order.getType().equals(SIZE)) {
                if (order.getNumberOne() != null && this.judgeSize(num1) == order.getNumberOne() && num1 != 25) {
                    count1 = count1.add(new BigDecimal("1"));
                }
                if (order.getNumberTwo() != null && this.judgeSize(num2) == order.getNumberTwo() && num2 != 25) {
                    count2 = count2.add(new BigDecimal("1"));
                }
                if (order.getNumberThree() != null && this.judgeSize(num3) == order.getNumberThree() && num3 != 25) {
                    count3 = count3.add(new BigDecimal("1"));
                }
                if (order.getNumberFour() != null && this.judgeSize(num4) == order.getNumberFour() && num4 != 25) {
                    count4 = count4.add(new BigDecimal("1"));
                }
                if (order.getNumberFive() != null && this.judgeSize(num5) == order.getNumberFive() && num5 != 25) {
                    count5 = count5.add(new BigDecimal("1"));
                }
                if (order.getNumberSix() != null && this.judgeSize(num6) == order.getNumberSix() && num6 != 25) {
                    count6 = count6.add(new BigDecimal("1"));
                }
                if (order.getNumberSeven() != null && this.judgeSize(num7) == order.getNumberSeven() && num7 != 25) {
                    count7 = count7.add(new BigDecimal("1"));
                }
            }
        }

        Count count = countDao.queryByPeriod(period, SIZE);

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
            if (order.getType().equals(SIZE)) {
                LotteryRecord lotteryRecord = new LotteryRecord();
                lotteryRecord.setId(idWorker.nextId() + "");
                lotteryRecord.setPeriod(period);
                lotteryRecord.setAgencyId(order.getAgencyId());
                lotteryRecord.setType(SIZE);
                BigDecimal totalMoney = new BigDecimal("0");
                if (order.getNumberOne() != null && this.judgeSize(num1) == order.getNumberOne() && num1 != 25) {
                    lotteryRecord.setNumberOneMoney(numberOneMoney);
                    totalMoney = totalMoney.add(numberOneMoney);
                }
                if (order.getNumberTwo() != null && this.judgeSize(num2) == order.getNumberTwo() && num2 != 25) {
                    lotteryRecord.setNumberTwoMoney(numberTwoMoney);
                    totalMoney = totalMoney.add(numberTwoMoney);
                }
                if (order.getNumberThree() != null && this.judgeSize(num3) == order.getNumberThree() && num3 != 25) {
                    lotteryRecord.setNumberThreeMoney(numberThreeMoney);
                    totalMoney = totalMoney.add(numberThreeMoney);
                }
                if (order.getNumberFour() != null && this.judgeSize(num4) == order.getNumberFour() && num4 != 25) {
                    lotteryRecord.setNumberFourMoney(numberFourMoney);
                    totalMoney = totalMoney.add(numberFourMoney);
                }
                if (order.getNumberFive() != null && this.judgeSize(num5) == order.getNumberFive() && num5 != 25) {
                    lotteryRecord.setNumberFiveMoney(numberFiveMoney);
                    totalMoney = totalMoney.add(numberFiveMoney);
                }
                if (order.getNumberSix() != null && this.judgeSize(num6) == order.getNumberSix() && num6 != 25) {
                    lotteryRecord.setNumberSixMoney(numberSixMoney);
                    totalMoney = totalMoney.add(numberSixMoney);
                }
                if (order.getNumberSeven() != null && this.judgeSize(num7) == order.getNumberSeven() && num7 != 25) {
                    lotteryRecord.setNumberSevenMoney(numberSevenMoney);
                    totalMoney = totalMoney.add(numberSevenMoney);
                }
                lotteryRecord.setTotalMoney(totalMoney);
                lotteryRecord.setCreateTime(date);
                lotteryRecordDao.save(lotteryRecord);
            }
        }
    }

    //保存单双奖池
    private void saveSDLotteryResult(Integer period, List<Integer> numsList, List<Order> orderList, Date date) {
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
        lotteryResult.setType(SD);

        BigDecimal count1 = new BigDecimal("0");
        BigDecimal count2 = new BigDecimal("0");
        BigDecimal count3 = new BigDecimal("0");
        BigDecimal count4 = new BigDecimal("0");
        BigDecimal count5 = new BigDecimal("0");
        BigDecimal count6 = new BigDecimal("0");
        BigDecimal count7 = new BigDecimal("0");

        for (Order order : orderList) {
            if (order.getType().equals(SD)) {
                if (order.getNumberOne() != null && num1 % 2 == order.getNumberOne() && num1 != 49) {
                    count1 = count1.add(new BigDecimal("1"));
                }
                if (order.getNumberTwo() != null && num2 % 2 == order.getNumberTwo() && num2 != 49) {
                    count2 = count2.add(new BigDecimal("1"));
                }
                if (order.getNumberThree() != null && num3 % 2 == order.getNumberThree() && num3 != 49) {
                    count3 = count3.add(new BigDecimal("1"));
                }
                if (order.getNumberFour() != null && num4 % 2 == order.getNumberFour() && num4 != 49) {
                    count4 = count4.add(new BigDecimal("1"));
                }
                if (order.getNumberFive() != null && num5 % 2 == order.getNumberFive() && num5 != 49) {
                    count5 = count5.add(new BigDecimal("1"));
                }
                if (order.getNumberSix() != null && num6 % 2 == order.getNumberSix() && num6 != 49) {
                    count6 = count6.add(new BigDecimal("1"));
                }
                if (order.getNumberSeven() != null && num7 % 2 == order.getNumberSeven() && num7 != 49) {
                    count7 = count7.add(new BigDecimal("1"));
                }
            }

        }

        Count count = countDao.queryByPeriod(period, SD);

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
            if (order.getType().equals(SD)) {
                LotteryRecord lotteryRecord = new LotteryRecord();
                lotteryRecord.setId(idWorker.nextId() + "");
                lotteryRecord.setPeriod(period);
                lotteryRecord.setAgencyId(order.getAgencyId());
                lotteryRecord.setType(SD);
                BigDecimal totalMoney = new BigDecimal("0");
                if (order.getNumberOne() != null && num1 % 2 == order.getNumberOne() && num1 != 49) {
                    lotteryRecord.setNumberOneMoney(numberOneMoney);
                    totalMoney = totalMoney.add(numberOneMoney);
                }
                if (order.getNumberTwo() != null && num2 % 2 == order.getNumberTwo() && num2 != 49) {
                    lotteryRecord.setNumberTwoMoney(numberTwoMoney);
                    totalMoney = totalMoney.add(numberTwoMoney);
                }
                if (order.getNumberThree() != null && num3 % 2 == order.getNumberThree() && num3 != 49) {
                    lotteryRecord.setNumberThreeMoney(numberThreeMoney);
                    totalMoney = totalMoney.add(numberThreeMoney);
                }
                if (order.getNumberFour() != null && num4 % 2 == order.getNumberFour() && num4 != 49) {
                    lotteryRecord.setNumberFourMoney(numberFourMoney);
                    totalMoney = totalMoney.add(numberFourMoney);
                }
                if (order.getNumberFive() != null && num5 % 2 == order.getNumberFive() && num5 != 49) {
                    lotteryRecord.setNumberFiveMoney(numberFiveMoney);
                    totalMoney = totalMoney.add(numberFiveMoney);
                }
                if (order.getNumberSix() != null && num6 % 2 == order.getNumberSix() && num6 != 49) {
                    lotteryRecord.setNumberSixMoney(numberSixMoney);
                    totalMoney = totalMoney.add(numberSixMoney);
                }
                if (order.getNumberSeven() != null && num7 % 2 == order.getNumberSeven() && num7 != 49) {
                    lotteryRecord.setNumberSevenMoney(numberSevenMoney);
                    totalMoney = totalMoney.add(numberSevenMoney);
                }
                lotteryRecord.setTotalMoney(totalMoney);
                lotteryRecord.setCreateTime(date);
                lotteryRecordDao.save(lotteryRecord);
            }
        }

    }

    //保存号码奖池
    private void saveNumberLotteryResult(Integer period, List<Integer> numsList, List<Order> orderList, Date date) {
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
        lotteryResult.setType(NUMBER);

        //统计每个奖池中奖个数
        BigDecimal count1 = new BigDecimal("0");
        BigDecimal count2 = new BigDecimal("0");
        BigDecimal count3 = new BigDecimal("0");
        BigDecimal count4 = new BigDecimal("0");
        BigDecimal count5 = new BigDecimal("0");
        BigDecimal count6 = new BigDecimal("0");
        BigDecimal count7 = new BigDecimal("0");

        for (Order order : orderList) {
            if (order.getType().equals(NUMBER)) {
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
        }

        Count count = countDao.queryByPeriod(period, NUMBER);

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
            if (order.getType().equals(NUMBER)) {
                LotteryRecord lotteryRecord = new LotteryRecord();
                lotteryRecord.setId(idWorker.nextId() + "");
                lotteryRecord.setPeriod(period);
                lotteryRecord.setAgencyId(order.getAgencyId());
                lotteryRecord.setType(NUMBER);
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
    }

    //判断大小
    private Integer judgeSize(Integer num) {
        if (num > 0 && num < 25) {
            return 0;
        } else if (num > 25 && num < 49) {
            return 1;
        } else {
            return null;
        }
    }

    //判断生肖
    private Integer judgeZodiac(Integer num) {
        Integer[] mouse = {8, 20, 32, 44}; //老鼠
        Integer[] cattle = {7, 19, 31, 43}; //牛
        Integer[] tiger = {6, 18, 30, 42}; //牛
        Integer[] rabbit = {5, 17, 29, 41}; //兔
        Integer[] dragon = {4, 16, 28, 40}; //龙
        Integer[] snake = {3, 15, 27, 39}; //蛇
        Integer[] horse = {2, 14, 26, 38}; //马
        Integer[] goat = {1, 13, 25, 37, 49}; //羊
        Integer[] monkey = {12, 24, 36, 48}; //猴子
        Integer[] chicken = {11, 23, 35, 47}; //鸡
        Integer[] dog = {10, 22, 34, 46}; //狗
        Integer[] pig = {9, 21, 33, 45}; //狗

        if (ArrayUtils.contains(mouse, num)) {
            return 1;
        } else if (ArrayUtils.contains(cattle, num)) {
            return 2;
        } else if (ArrayUtils.contains(tiger, num)) {
            return 3;
        } else if (ArrayUtils.contains(rabbit, num)) {
            return 4;
        } else if (ArrayUtils.contains(dragon, num)) {
            return 5;
        } else if (ArrayUtils.contains(snake, num)) {
            return 6;
        } else if (ArrayUtils.contains(horse, num)) {
            return 7;
        } else if (ArrayUtils.contains(goat, num)) {
            return 8;
        } else if (ArrayUtils.contains(monkey, num)) {
            return 9;
        } else if (ArrayUtils.contains(chicken, num)) {
            return 10;
        } else if (ArrayUtils.contains(dog, num)) {
            return 11;
        } else if (ArrayUtils.contains(pig, num)) {
            return 12;
        } else {
            return null;
        }

    }


    public List<Lottery> findAllOrderByPeriod() {
        List<Lottery> lotteryList = lotteryDao.findAllOrderByPeriodDesc();
        return lotteryList;
    }

    public List<Lottery> findByDate(Date startDate, Date endDate) {
        List<Lottery> lotteryList = lotteryDao.findAllBetweenStartAndEnd(startDate, endDate);
        return lotteryList;
    }

    public Lottery findLattestLottery() {
        Lottery lottery = lotteryDao.findLatterLottery();
        return lottery;
    }
}

