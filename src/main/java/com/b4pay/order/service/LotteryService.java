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
import java.util.Map;

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
    public void match(Integer period, List<Integer> numsList, List<Order> orderList, Date date, Map<String,String> map) {
        //保存号码奖池
        this.saveNumberLotteryResult(period, numsList, orderList, date,map);
        //保存生肖奖池
        this.saveZodiacLotteryResult(period, numsList, orderList, date,map);

        //保存颜色奖池 红 蓝 绿
        this.saveColorLotteryResult(period, numsList, orderList, date,map);

        //保存单双奖池 1是双 2是单
        this.saveSDLotteryResult(period, numsList, orderList, date,map);

        //保存大小奖池 1-24小 26-49大
        this.saveSizeLotteryResult(period, numsList, orderList, date,map);


    }

    //保存生肖奖池
    private void saveZodiacLotteryResult(Integer period, List<Integer> numsList, List<Order> orderList, Date date,Map<String,String> map) {
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
        lotteryResult.setId(idWorker.nextId()+"");
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
                if (order.getNumberOne() != null && order.getNumberOne() != 0 && this.judgeZodiac(num1) == order.getNumberOne()) {
                    count1 = count1.add(new BigDecimal("1"));
                }
                if (order.getNumberTwo() != null && order.getNumberOne() != 0 && this.judgeZodiac(num2) == order.getNumberTwo()) {
                    count2 = count2.add(new BigDecimal("1"));
                }
                if (order.getNumberThree() != null && order.getNumberOne() != 0 && this.judgeZodiac(num3) == order.getNumberThree()) {
                    count3 = count3.add(new BigDecimal("1"));
                }
                if (order.getNumberFour() != null && order.getNumberOne() != 0 && this.judgeZodiac(num4) == order.getNumberFour()) {
                    count4 = count4.add(new BigDecimal("1"));
                }
                if (order.getNumberFive() != null && order.getNumberOne() != 0 && this.judgeZodiac(num5) == order.getNumberFive()) {
                    count5 = count5.add(new BigDecimal("1"));
                }
                if (order.getNumberSix() != null && order.getNumberOne() != 0 && this.judgeZodiac(num6) == order.getNumberSix()) {
                    count6 = count6.add(new BigDecimal("1"));
                }
                if (order.getNumberSeven() != null && order.getNumberOne() != 0 && this.judgeZodiac(num7) == order.getNumberSeven()) {
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

        BigDecimal discount = new BigDecimal("0.8");

        BigDecimal gradeThree = new BigDecimal("0.7");
        BigDecimal gradeTwo = new BigDecimal("0.3");
        BigDecimal gradeOne = new BigDecimal("0.5");



        //记录每个用户中奖金额
        for (Order order : orderList) {
            if (order.getType().equals(ZODIAC)) {
                //保存个人中奖信息
                LotteryRecord lotteryRecord = new LotteryRecord();
                lotteryRecord.setId(idWorker.nextId() + "");
                lotteryRecord.setPeriod(period);
                lotteryRecord.setAgencyId(order.getAgencyId());
                lotteryRecord.setType(ZODIAC);
                BigDecimal totalMoney = new BigDecimal("0");

                //TODO 查找三级

                //保存三级
                GradeRecord gradeThreeUser = null;
                GradeRecord gradeTwoUser = null;
                GradeRecord gradeOneUser = null;

                if (map.get("gradeThree") != null && !"".equals(map.get("gradeThree"))){
                    gradeThreeUser = new GradeRecord();
                    gradeThreeUser.setId(idWorker.nextId() + "");
                    gradeThreeUser.setPeriod(period);
                    gradeThreeUser.setAgencyId(order.getAgencyId());
                    gradeThreeUser.setGrade("gradeThree");
                    gradeThreeUser.setGradeId(map.get("gradeThree"));
                    gradeThreeUser.setType(ZODIAC);
                }
                if (map.get("gradeTwoUser") != null && !"".equals(map.get("gradeTwoUser"))){
                    gradeTwoUser = new GradeRecord();
                    gradeTwoUser.setId(idWorker.nextId() + "");
                    gradeTwoUser.setPeriod(period);
                    gradeTwoUser.setAgencyId(order.getAgencyId());
                    gradeTwoUser.setGrade("gradeTwoUser");
                    gradeTwoUser.setGradeId(map.get("gradeTwoUser"));
                    gradeTwoUser.setType(ZODIAC);
                }
                if (map.get("gradeOneUser") != null && !"".equals(map.get("gradeOneUser"))){
                    gradeOneUser = new GradeRecord();
                    gradeOneUser.setId(idWorker.nextId() + "");
                    gradeOneUser.setPeriod(period);
                    gradeOneUser.setAgencyId(order.getAgencyId());
                    gradeOneUser.setGrade("gradeOneUser");
                    gradeOneUser.setGradeId(map.get("gradeOneUser"));
                    gradeOneUser.setType(ZODIAC);
                }



                if (order.getNumberOne() != null && order.getNumberOne() != 0 && this.judgeZodiac(num1) == order.getNumberOne()) {
                    lotteryRecord.setNumberOneMoney(numberOneMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberOneMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberOneMoney(numberOneMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberOneMoney(numberOneMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberOneMoney(numberOneMoney.multiply(gradeOne));
                    }

                }

                if (order.getNumberTwo() != null && order.getNumberTwo() != 0 && this.judgeZodiac(num2) == order.getNumberTwo()) {
                    lotteryRecord.setNumberTwoMoney(numberTwoMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberTwoMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberTwoMoney(numberTwoMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberTwoMoney(numberTwoMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberTwoMoney(numberTwoMoney.multiply(gradeOne));
                    }
                }

                if (order.getNumberThree() != null && order.getNumberThree() != 0 && this.judgeZodiac(num3) == order.getNumberThree()) {
                    lotteryRecord.setNumberThreeMoney(numberThreeMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberThreeMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberThreeMoney(numberThreeMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberThreeMoney(numberThreeMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberThreeMoney(numberThreeMoney.multiply(gradeOne));
                    }
                }

                if (order.getNumberFour() != null && order.getNumberFour() != 0 && this.judgeZodiac(num4) == order.getNumberFour()) {
                    lotteryRecord.setNumberFourMoney(numberFourMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberFourMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberFourMoney(numberFourMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberFourMoney(numberFourMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberFourMoney(numberFourMoney.multiply(gradeOne));
                    }
                }

                if (order.getNumberFive() != null && order.getNumberFive() != 0 && this.judgeZodiac(num5) == order.getNumberFive()) {
                    lotteryRecord.setNumberFiveMoney(numberFiveMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberFiveMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberFiveMoney(numberFiveMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberFiveMoney(numberFiveMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberFiveMoney(numberFiveMoney.multiply(gradeOne));
                    }
                }

                if (order.getNumberSix() != null && order.getNumberSix() != 0 && this.judgeZodiac(num6) == order.getNumberSix()) {
                    lotteryRecord.setNumberSixMoney(numberSixMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberSixMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberSixMoney(numberSixMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberSixMoney(numberSixMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberSixMoney(numberSixMoney.multiply(gradeOne));
                    }
                }

                if (order.getNumberSeven() != null && order.getNumberSeven() != 0 && this.judgeZodiac(num7) == order.getNumberSeven()) {
                    lotteryRecord.setNumberSevenMoney(numberSevenMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberSevenMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberSevenMoney(numberSevenMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberSevenMoney(numberSevenMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberSevenMoney(numberSevenMoney.multiply(gradeOne));
                    }
                }
                lotteryRecord.setTotalMoney(totalMoney);
                lotteryRecord.setCreateTime(date);
                lotteryRecordDao.save(lotteryRecord);
            }
        }
    }

    //保存颜色奖池
    private void saveColorLotteryResult(Integer period, List<Integer> numsList, List<Order> orderList, Date date,Map<String,String> map) {
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
        lotteryResult.setId(idWorker.nextId()+"");
        lotteryResult.setNumberOne(num1);
        lotteryResult.setNumberTwo(num2);
        lotteryResult.setNumberThree(num3);
        lotteryResult.setNumberFour(num4);
        lotteryResult.setNumberFive(num5);
        lotteryResult.setNumberSix(num6);
        lotteryResult.setNumberSeven(num7);
        lotteryResult.setType(COLOR);

        BigDecimal count1 = new BigDecimal("0");
        BigDecimal count2 = new BigDecimal("0");
        BigDecimal count3 = new BigDecimal("0");
        BigDecimal count4 = new BigDecimal("0");
        BigDecimal count5 = new BigDecimal("0");
        BigDecimal count6 = new BigDecimal("0");
        BigDecimal count7 = new BigDecimal("0");




        for (Order order : orderList) {
            if (order.getType().equals(COLOR)) {
                if (order.getNumberOne() != null && order.getNumberOne() != 0 && this.judgeColor(num1) == order.getNumberOne()) {
                    count1 = count1.add(new BigDecimal("1"));
                }
                if (order.getNumberTwo() != null && order.getNumberOne() != 0 && this.judgeColor(num2) == order.getNumberTwo()) {
                    count2 = count2.add(new BigDecimal("1"));
                }
                if (order.getNumberThree() != null && order.getNumberOne() != 0 && this.judgeColor(num3) == order.getNumberThree()) {
                    count3 = count3.add(new BigDecimal("1"));
                }
                if (order.getNumberFour() != null && order.getNumberOne() != 0 && this.judgeColor(num4) == order.getNumberFour()) {
                    count4 = count4.add(new BigDecimal("1"));
                }
                if (order.getNumberFive() != null && order.getNumberOne() != 0 && this.judgeColor(num5) == order.getNumberFive()) {
                    count5 = count5.add(new BigDecimal("1"));
                }
                if (order.getNumberSix() != null && order.getNumberOne() != 0 && this.judgeColor(num6) == order.getNumberSix()) {
                    count6 = count6.add(new BigDecimal("1"));
                }
                if (order.getNumberSeven() != null && order.getNumberOne() != 0 && this.judgeColor(num7) == order.getNumberSeven()) {
                    count7 = count7.add(new BigDecimal("1"));
                }
            }
        }

        Count count = countDao.queryByPeriod(period, COLOR);

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


        BigDecimal discount = new BigDecimal("0.8");

        BigDecimal gradeThree = new BigDecimal("0.7");
        BigDecimal gradeTwo = new BigDecimal("0.3");
        BigDecimal gradeOne = new BigDecimal("0.5");

        //记录每个用户中奖金额
        for (Order order : orderList) {
            if (order.getType().equals(COLOR)) {
                LotteryRecord lotteryRecord = new LotteryRecord();
                lotteryRecord.setId(idWorker.nextId() + "");
                lotteryRecord.setPeriod(period);
                lotteryRecord.setAgencyId(order.getAgencyId());
                lotteryRecord.setType(COLOR);
                BigDecimal totalMoney = new BigDecimal("0");

                //查找三级

                //保存三级
                GradeRecord gradeThreeUser = null;
                GradeRecord gradeTwoUser = null;
                GradeRecord gradeOneUser = null;

                if (map.get("gradeThree") != null && !"".equals(map.get("gradeThree"))){
                    gradeThreeUser = new GradeRecord();
                    gradeThreeUser.setId(idWorker.nextId() + "");
                    gradeThreeUser.setPeriod(period);
                    gradeThreeUser.setAgencyId(order.getAgencyId());
                    gradeThreeUser.setGrade("gradeThree");
                    gradeThreeUser.setGradeId(map.get("gradeThree"));
                    gradeThreeUser.setType(COLOR);
                }
                if (map.get("gradeTwoUser") != null && !"".equals(map.get("gradeTwoUser"))){
                    gradeTwoUser = new GradeRecord();
                    gradeTwoUser.setId(idWorker.nextId() + "");
                    gradeTwoUser.setPeriod(period);
                    gradeTwoUser.setAgencyId(order.getAgencyId());
                    gradeTwoUser.setGrade("gradeTwoUser");
                    gradeTwoUser.setGradeId(map.get("gradeTwoUser"));
                    gradeTwoUser.setType(COLOR);
                }

                if (map.get("gradeOneUser") != null && !"".equals(map.get("gradeOneUser"))){
                    gradeOneUser = new GradeRecord();
                    gradeOneUser.setId(idWorker.nextId() + "");
                    gradeOneUser.setPeriod(period);
                    gradeOneUser.setAgencyId(order.getAgencyId());
                    gradeOneUser.setGrade("gradeOneUser");
                    gradeOneUser.setGradeId(map.get("gradeOneUser"));
                    gradeOneUser.setType(COLOR);
                }

                if (order.getNumberOne() != null && order.getNumberOne() != 0 && this.judgeColor(num1) == order.getNumberOne()) {
                    lotteryRecord.setNumberOneMoney(numberOneMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberOneMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberOneMoney(numberOneMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberOneMoney(numberOneMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberOneMoney(numberOneMoney.multiply(gradeOne));
                    }
                }
                if (order.getNumberTwo() != null && order.getNumberTwo() != 0 && this.judgeColor(num2) == order.getNumberTwo()) {
                    lotteryRecord.setNumberTwoMoney(numberTwoMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberTwoMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberTwoMoney(numberTwoMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberTwoMoney(numberTwoMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberTwoMoney(numberTwoMoney.multiply(gradeOne));
                    }
                }

                if (order.getNumberThree() != null && order.getNumberThree() != 0 && this.judgeColor(num3) == order.getNumberThree()) {
                    lotteryRecord.setNumberThreeMoney(numberThreeMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberThreeMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberThreeMoney(numberThreeMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberThreeMoney(numberThreeMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberThreeMoney(numberThreeMoney.multiply(gradeOne));
                    }
                }

                if (order.getNumberFour() != null && order.getNumberFour() != 0 && this.judgeColor(num4) == order.getNumberFour()) {
                    lotteryRecord.setNumberFourMoney(numberFourMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberFourMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberFourMoney(numberFourMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberFourMoney(numberFourMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberFourMoney(numberFourMoney.multiply(gradeOne));
                    }
                }

                if (order.getNumberFive() != null && order.getNumberFive() != 0 && this.judgeColor(num5) == order.getNumberFive()) {
                    lotteryRecord.setNumberFiveMoney(numberFiveMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberFiveMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberFiveMoney(numberFiveMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberFiveMoney(numberFiveMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberFiveMoney(numberFiveMoney.multiply(gradeOne));
                    }
                }

                if (order.getNumberSix() != null && order.getNumberSix() != 0 && this.judgeColor(num6) == order.getNumberSix()) {
                    lotteryRecord.setNumberSixMoney(numberSixMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberSixMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberSixMoney(numberSixMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberSixMoney(numberSixMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberSixMoney(numberSixMoney.multiply(gradeOne));
                    }
                }

                if (order.getNumberSeven() != null && order.getNumberSeven() != 0 && this.judgeColor(num7) == order.getNumberSeven()) {
                    lotteryRecord.setNumberSevenMoney(numberSevenMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberSevenMoney).multiply(discount);
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberSevenMoney(numberSevenMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberSevenMoney(numberSevenMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberSevenMoney(numberSevenMoney.multiply(gradeOne));
                    }
                }
                lotteryRecord.setTotalMoney(totalMoney);
                lotteryRecord.setCreateTime(date);
                lotteryRecordDao.save(lotteryRecord);
            }
        }
    }

    //保存大小奖池
    private void saveSizeLotteryResult(Integer period, List<Integer> numsList, List<Order> orderList, Date date,Map<String,String> map) {
        Integer num1 = numsList.get(0);
        Integer num2 = numsList.get(1);
        Integer num3 = numsList.get(2);
        Integer num4 = numsList.get(3);
        Integer num5 = numsList.get(4);
        Integer num6 = numsList.get(5);
        Integer num7 = numsList.get(6);
        //奖池结果
        LotteryResult lotteryResult = new LotteryResult();
        lotteryResult.setId(idWorker.nextId()+"");
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
                if (order.getNumberOne() != null && order.getNumberOne() != 0 && this.judgeSize(num1) == order.getNumberOne() && num1 != 25) {
                    count1 = count1.add(new BigDecimal("1"));
                }
                if (order.getNumberTwo() != null && order.getNumberOne() != 0 && this.judgeSize(num2) == order.getNumberTwo() && num2 != 25) {
                    count2 = count2.add(new BigDecimal("1"));
                }
                if (order.getNumberThree() != null && order.getNumberOne() != 0 && this.judgeSize(num3) == order.getNumberThree() && num3 != 25) {
                    count3 = count3.add(new BigDecimal("1"));
                }
                if (order.getNumberFour() != null && order.getNumberOne() != 0 && this.judgeSize(num4) == order.getNumberFour() && num4 != 25) {
                    count4 = count4.add(new BigDecimal("1"));
                }
                if (order.getNumberFive() != null && order.getNumberOne() != 0 && this.judgeSize(num5) == order.getNumberFive() && num5 != 25) {
                    count5 = count5.add(new BigDecimal("1"));
                }
                if (order.getNumberSix() != null && order.getNumberOne() != 0 && this.judgeSize(num6) == order.getNumberSix() && num6 != 25) {
                    count6 = count6.add(new BigDecimal("1"));
                }
                if (order.getNumberSeven() != null && order.getNumberOne() != 0 && this.judgeSize(num7) == order.getNumberSeven() && num7 != 25) {
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

        BigDecimal discount = new BigDecimal("0.8");

        BigDecimal gradeThree = new BigDecimal("0.7");
        BigDecimal gradeTwo = new BigDecimal("0.3");
        BigDecimal gradeOne = new BigDecimal("0.5");

        //记录每个用户中奖金额
        for (Order order : orderList) {
            if (order.getType().equals(SIZE)) {
                LotteryRecord lotteryRecord = new LotteryRecord();
                lotteryRecord.setId(idWorker.nextId() + "");
                lotteryRecord.setPeriod(period);
                lotteryRecord.setAgencyId(order.getAgencyId());
                lotteryRecord.setType(SIZE);
                BigDecimal totalMoney = new BigDecimal("0");

                //TODO 查找三级

                //保存三级
                GradeRecord gradeThreeUser = null;
                GradeRecord gradeTwoUser = null;
                GradeRecord gradeOneUser = null;


                if (map.get("gradeThree") != null && !"".equals(map.get("gradeThree"))){
                    gradeThreeUser = new GradeRecord();
                    gradeThreeUser.setId(idWorker.nextId() + "");
                    gradeThreeUser.setPeriod(period);
                    gradeThreeUser.setAgencyId(order.getAgencyId());
                    gradeThreeUser.setGrade("gradeThree");
                    gradeThreeUser.setGradeId(map.get("gradeThree"));
                    gradeThreeUser.setType(SIZE);
                }
                if (map.get("gradeTwoUser") != null && !"".equals(map.get("gradeTwoUser"))){
                    gradeTwoUser = new GradeRecord();
                    gradeTwoUser.setId(idWorker.nextId() + "");
                    gradeTwoUser.setPeriod(period);
                    gradeTwoUser.setAgencyId(order.getAgencyId());
                    gradeTwoUser.setGrade("gradeTwoUser");
                    gradeTwoUser.setGradeId(map.get("gradeTwoUser"));
                    gradeTwoUser.setType(SIZE);
                }
                if (map.get("gradeOneUser") != null && !"".equals(map.get("gradeOneUser"))){
                    gradeOneUser = new GradeRecord();
                    gradeOneUser.setId(idWorker.nextId() + "");
                    gradeOneUser.setPeriod(period);
                    gradeOneUser.setAgencyId(order.getAgencyId());
                    gradeOneUser.setGrade("gradeOneUser");
                    gradeOneUser.setGradeId(map.get("gradeOneUser"));
                    gradeOneUser.setType(SIZE);
                }


                if (order.getNumberOne() != null && order.getNumberOne() != 0 && this.judgeSize(num1) == order.getNumberOne() && num1 != 25) {
                    lotteryRecord.setNumberOneMoney(numberOneMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberOneMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberOneMoney(numberOneMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberOneMoney(numberOneMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberOneMoney(numberOneMoney.multiply(gradeOne));
                    }
                }

                if (order.getNumberTwo() != null && order.getNumberTwo() != 0 && this.judgeSize(num2) == order.getNumberTwo() && num2 != 25) {
                    lotteryRecord.setNumberTwoMoney(numberTwoMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberTwoMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberTwoMoney(numberTwoMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberTwoMoney(numberTwoMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberTwoMoney(numberTwoMoney.multiply(gradeOne));
                    }
                }
                if (order.getNumberThree() != null && order.getNumberThree() != 0 && this.judgeSize(num3) == order.getNumberThree() && num3 != 25) {
                    lotteryRecord.setNumberThreeMoney(numberThreeMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberThreeMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberThreeMoney(numberThreeMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberThreeMoney(numberThreeMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberThreeMoney(numberThreeMoney.multiply(gradeOne));
                    }
                }
                if (order.getNumberFour() != null && order.getNumberFour() != 0 && this.judgeSize(num4) == order.getNumberFour() && num4 != 25) {
                    lotteryRecord.setNumberFourMoney(numberFourMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberFourMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberFourMoney(numberFourMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberFourMoney(numberFourMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberFourMoney(numberFourMoney.multiply(gradeOne));
                    }
                }
                if (order.getNumberFive() != null && order.getNumberFive() != 0 && this.judgeSize(num5) == order.getNumberFive() && num5 != 25) {
                    lotteryRecord.setNumberFiveMoney(numberFiveMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberFiveMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberFiveMoney(numberFiveMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberFiveMoney(numberFiveMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberFiveMoney(numberFiveMoney.multiply(gradeOne));
                    }
                }
                if (order.getNumberSix() != null && order.getNumberSix() != 0 && this.judgeSize(num6) == order.getNumberSix() && num6 != 25) {
                    lotteryRecord.setNumberSixMoney(numberSixMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberSixMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberSixMoney(numberSixMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberSixMoney(numberSixMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberSixMoney(numberSixMoney.multiply(gradeOne));
                    }
                }

                if (order.getNumberSeven() != null && order.getNumberSeven() != 0 && this.judgeSize(num7) == order.getNumberSeven() && num7 != 25) {
                    lotteryRecord.setNumberSevenMoney(numberSevenMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberSevenMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberSevenMoney(numberSevenMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberSevenMoney(numberSevenMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberSevenMoney(numberSevenMoney.multiply(gradeOne));
                    }
                }
                lotteryRecord.setTotalMoney(totalMoney);
                lotteryRecord.setCreateTime(date);
                lotteryRecordDao.save(lotteryRecord);
            }
        }
    }

    //保存单双奖池
    private void saveSDLotteryResult(Integer period, List<Integer> numsList, List<Order> orderList, Date date,Map<String,String> map) {
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
        lotteryResult.setId(idWorker.nextId()+"");
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
                if (order.getNumberOne() != null && order.getNumberOne() != 0 && num1 % 2 + 1 == order.getNumberOne() && num1 != 49) {
                    count1 = count1.add(new BigDecimal("1"));
                }
                if (order.getNumberTwo() != null && order.getNumberOne() != 0 && num2 % 2 + 1 == order.getNumberTwo() && num2 != 49) {
                    count2 = count2.add(new BigDecimal("1"));
                }
                if (order.getNumberThree() != null && order.getNumberOne() != 0 && num3 % 2 + 1 == order.getNumberThree() && num3 != 49) {
                    count3 = count3.add(new BigDecimal("1"));
                }
                if (order.getNumberFour() != null && order.getNumberOne() != 0 && num4 % 2 + 1 == order.getNumberFour() && num4 != 49) {
                    count4 = count4.add(new BigDecimal("1"));
                }
                if (order.getNumberFive() != null && order.getNumberOne() != 0 && num5 % 2 + 1 == order.getNumberFive() && num5 != 49) {
                    count5 = count5.add(new BigDecimal("1"));
                }
                if (order.getNumberSix() != null && order.getNumberOne() != 0 && num6 % 2 + 1 == order.getNumberSix() && num6 != 49) {
                    count6 = count6.add(new BigDecimal("1"));
                }
                if (order.getNumberSeven() != null && order.getNumberOne() != 0 && num7 % 2 + 1 == order.getNumberSeven() && num7 != 49) {
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

        BigDecimal discount = new BigDecimal("0.8");

        BigDecimal gradeThree = new BigDecimal("0.7");
        BigDecimal gradeTwo = new BigDecimal("0.3");
        BigDecimal gradeOne = new BigDecimal("0.5");


        //记录每个用户中奖金额
        for (Order order : orderList) {
            if (order.getType().equals(SD)) {
                LotteryRecord lotteryRecord = new LotteryRecord();
                lotteryRecord.setId(idWorker.nextId() + "");
                lotteryRecord.setPeriod(period);
                lotteryRecord.setAgencyId(order.getAgencyId());
                lotteryRecord.setType(SD);
                BigDecimal totalMoney = new BigDecimal("0");

                //TODO 查找三级

                //保存三级
                GradeRecord gradeThreeUser = null;
                GradeRecord gradeTwoUser = null;
                GradeRecord gradeOneUser = null;

                if (map.get("gradeThree") != null && !"".equals(map.get("gradeThree"))){
                    gradeThreeUser = new GradeRecord();
                    gradeThreeUser.setId(idWorker.nextId() + "");
                    gradeThreeUser.setPeriod(period);
                    gradeThreeUser.setAgencyId(order.getAgencyId());
                    gradeThreeUser.setGrade("gradeThree");
                    gradeThreeUser.setGradeId(map.get("gradeThree"));
                    gradeThreeUser.setType(SD);
                }
                if (map.get("gradeTwoUser") != null && !"".equals(map.get("gradeTwoUser"))){
                    gradeTwoUser = new GradeRecord();
                    gradeTwoUser.setId(idWorker.nextId() + "");
                    gradeTwoUser.setPeriod(period);
                    gradeTwoUser.setAgencyId(order.getAgencyId());
                    gradeTwoUser.setGrade("gradeTwoUser");
                    gradeTwoUser.setGradeId(map.get("gradeTwoUser"));
                    gradeTwoUser.setType(SD);
                }
                if (map.get("gradeOneUser") != null && !"".equals(map.get("gradeOneUser"))){
                    gradeOneUser = new GradeRecord();
                    gradeOneUser.setId(idWorker.nextId() + "");
                    gradeOneUser.setPeriod(period);
                    gradeOneUser.setAgencyId(order.getAgencyId());
                    gradeOneUser.setGrade("gradeOneUser");
                    gradeOneUser.setGradeId(map.get("gradeOneUser"));
                    gradeOneUser.setType(SD);
                }

                if (order.getNumberOne() != null && order.getNumberOne() != 0 && num1 % 2 + 1== order.getNumberOne() && num1 != 49) {
                    lotteryRecord.setNumberOneMoney(numberOneMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberOneMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberOneMoney(numberOneMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberOneMoney(numberOneMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberOneMoney(numberOneMoney.multiply(gradeOne));
                    }
                }

                if (order.getNumberTwo() != null && order.getNumberTwo() != 0 && num2 % 2 + 1 == order.getNumberTwo() && num2 != 49) {
                    lotteryRecord.setNumberTwoMoney(numberTwoMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberTwoMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberTwoMoney(numberTwoMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberTwoMoney(numberTwoMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberTwoMoney(numberTwoMoney.multiply(gradeOne));
                    }
                }

                if (order.getNumberThree() != null && order.getNumberThree() != 0 && num3 % 2 + 1 == order.getNumberThree() && num3 != 49) {
                    lotteryRecord.setNumberThreeMoney(numberThreeMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberThreeMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberThreeMoney(numberThreeMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberThreeMoney(numberThreeMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberThreeMoney(numberThreeMoney.multiply(gradeOne));
                    }
                }

                if (order.getNumberFour() != null && order.getNumberFour() != 0 && num4 % 2 + 1== order.getNumberFour() && num4 != 49) {
                    lotteryRecord.setNumberFourMoney(numberFourMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberFourMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberFourMoney(numberFourMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberFourMoney(numberFourMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberFourMoney(numberFourMoney.multiply(gradeOne));
                    }
                }

                if (order.getNumberFive() != null && order.getNumberFive() != 0 && num5 % 2 + 1== order.getNumberFive() && num5 != 49) {
                    lotteryRecord.setNumberFiveMoney(numberFiveMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberFiveMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberFiveMoney(numberFiveMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberFiveMoney(numberFiveMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberFiveMoney(numberFiveMoney.multiply(gradeOne));
                    }
                }

                if (order.getNumberSix() != null && order.getNumberSix() != 0 && num6 % 2 + 1== order.getNumberSix() && num6 != 49) {
                    lotteryRecord.setNumberSixMoney(numberSixMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberSixMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberSixMoney(numberSixMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberSixMoney(numberSixMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberSixMoney(numberSixMoney.multiply(gradeOne));
                    }
                }

                if (order.getNumberSeven() != null && order.getNumberSeven() != 0 && num7 % 2 + 1== order.getNumberSeven() && num7 != 49) {
                    lotteryRecord.setNumberSevenMoney(numberSevenMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberSevenMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberSevenMoney(numberSevenMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberSevenMoney(numberSevenMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberSevenMoney(numberSevenMoney.multiply(gradeOne));
                    }
                }

                lotteryRecord.setTotalMoney(totalMoney);
                lotteryRecord.setCreateTime(date);
                lotteryRecordDao.save(lotteryRecord);
            }
        }

    }

    //保存号码奖池
    private void saveNumberLotteryResult(Integer period, List<Integer> numsList, List<Order> orderList, Date date,Map<String,String> map) {
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
        lotteryResult.setId(idWorker.nextId()+"");
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
                if (order.getNumberOne() != null && order.getNumberOne() != 0 && num1 == order.getNumberOne()) {
                    count1 = count1.add(new BigDecimal("1"));
                }
                if (order.getNumberTwo() != null && order.getNumberOne() != 0 && num2 == order.getNumberTwo()) {
                    count2 = count2.add(new BigDecimal("1"));
                }
                if (order.getNumberThree() != null && order.getNumberOne() != 0 && num3 == order.getNumberThree()) {
                    count3 = count3.add(new BigDecimal("1"));
                }
                if (order.getNumberFour() != null && order.getNumberOne() != 0 && num4 == order.getNumberFour()) {
                    count4 = count4.add(new BigDecimal("1"));
                }
                if (order.getNumberFive() != null && order.getNumberOne() != 0 && num5 == order.getNumberFive()) {
                    count5 = count5.add(new BigDecimal("1"));
                }
                if (order.getNumberSix() != null && order.getNumberOne() != 0 && num6 == order.getNumberSix()) {
                    count6 = count6.add(new BigDecimal("1"));
                }
                if (order.getNumberSeven() != null && order.getNumberOne() != 0 && num7 == order.getNumberSeven()) {
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

        BigDecimal discount = new BigDecimal("0.8");

        BigDecimal gradeThree = new BigDecimal("0.7");
        BigDecimal gradeTwo = new BigDecimal("0.3");
        BigDecimal gradeOne = new BigDecimal("0.5");


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

                //TODO 查找三级

                //保存三级
                GradeRecord gradeThreeUser = null;
                GradeRecord gradeTwoUser = null;
                GradeRecord gradeOneUser = null;
                if (map.get("gradeThree") != null && !"".equals(map.get("gradeThree"))){
                    gradeThreeUser = new GradeRecord();
                    gradeThreeUser.setId(idWorker.nextId() + "");
                    gradeThreeUser.setPeriod(period);
                    gradeThreeUser.setAgencyId(order.getAgencyId());
                    gradeThreeUser.setGrade("gradeThree");
                    gradeThreeUser.setGradeId(map.get("gradeThree"));
                    gradeThreeUser.setType(NUMBER);
                }
                if (map.get("gradeTwoUser") != null && !"".equals(map.get("gradeTwoUser"))){
                    gradeTwoUser = new GradeRecord();
                    gradeTwoUser.setId(idWorker.nextId() + "");
                    gradeTwoUser.setPeriod(period);
                    gradeTwoUser.setAgencyId(order.getAgencyId());
                    gradeTwoUser.setGrade("gradeTwoUser");
                    gradeTwoUser.setGradeId(map.get("gradeTwoUser"));
                    gradeTwoUser.setType(NUMBER);
                }
                if (map.get("gradeOneUser") != null && !"".equals(map.get("gradeOneUser"))){
                    gradeOneUser = new GradeRecord();
                    gradeOneUser.setId(idWorker.nextId() + "");
                    gradeOneUser.setPeriod(period);
                    gradeOneUser.setAgencyId(order.getAgencyId());
                    gradeOneUser.setGrade("gradeOneUser");
                    gradeOneUser.setGradeId(map.get("gradeOneUser"));
                    gradeOneUser.setType(NUMBER);
                }

                if (order.getNumberOne() != null && order.getNumberOne() != 0 && num1 == order.getNumberOne()) {
                    lotteryRecord.setNumberOneMoney(numberOneMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberOneMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberOneMoney(numberOneMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberOneMoney(numberOneMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberOneMoney(numberOneMoney.multiply(gradeOne));
                    }
                }

                if (order.getNumberTwo() != null && order.getNumberTwo() != 0 && num2 == order.getNumberTwo()) {
                    lotteryRecord.setNumberTwoMoney(numberTwoMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberTwoMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberTwoMoney(numberTwoMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberTwoMoney(numberTwoMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberTwoMoney(numberTwoMoney.multiply(gradeOne));
                    }
                }


                if (order.getNumberThree() != null && order.getNumberThree() != 0 && num3 == order.getNumberThree()) {
                    lotteryRecord.setNumberThreeMoney(numberThreeMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberThreeMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberThreeMoney(numberThreeMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberThreeMoney(numberThreeMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberThreeMoney(numberThreeMoney.multiply(gradeOne));
                    }
                }

                if (order.getNumberFour() != null && order.getNumberFour() != 0 && num4 == order.getNumberFour()) {
                    lotteryRecord.setNumberFourMoney(numberFourMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberFourMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberFourMoney(numberFourMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberFourMoney(numberFourMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberFourMoney(numberFourMoney.multiply(gradeOne));
                    }
                }

                if (order.getNumberFive() != null && order.getNumberFive() != 0 && num5 == order.getNumberFive()) {
                    lotteryRecord.setNumberFiveMoney(numberFiveMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberFiveMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberFiveMoney(numberFiveMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberFiveMoney(numberFiveMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberFiveMoney(numberFiveMoney.multiply(gradeOne));
                    }
                }

                if (order.getNumberSix() != null && order.getNumberSix() != 0 && num6 == order.getNumberSix()) {
                    lotteryRecord.setNumberSixMoney(numberSixMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberSixMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberSixMoney(numberSixMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberSixMoney(numberSixMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberSixMoney(numberSixMoney.multiply(gradeOne));
                    }
                }

                if (order.getNumberSeven() != null && order.getNumberSeven() != 0 && num7 == order.getNumberSeven()) {
                    lotteryRecord.setNumberSevenMoney(numberSevenMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberSevenMoney.multiply(discount));
                    //三级
                    if(gradeThreeUser != null){
                        gradeThreeUser.setNumberSevenMoney(numberSevenMoney.multiply(gradeThree));
                    }
                    //二级
                    if(gradeTwoUser != null){
                        gradeTwoUser.setNumberSevenMoney(numberSevenMoney.multiply(gradeTwo));
                    }
                    //一级
                    if(gradeOneUser != null){
                        gradeOneUser.setNumberSevenMoney(numberSevenMoney.multiply(gradeOne));
                    }
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
            return 1;
        } else if (num > 25 && num < 49) {
            return 2;
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

    private Integer judgeColor(Integer num) {
        Integer[] red = {1,2,7,8,12,13,18,19,23,24,29,30,34,35,40,45,46}; //红
        Integer[] blue = {3,4,9,10,14,15,20,25,26,31,36,37,41,42,47,48}; //蓝
        Integer[] green = {5,6,11,16,17,21,22,27,28,32,33,38,39,43,44,49}; //绿

        if (ArrayUtils.contains(red, num)) {
            return 1;
        } else if (ArrayUtils.contains(blue, num)) {
            return 2;
        } else if (ArrayUtils.contains(green, num)) {
            return 3;
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

