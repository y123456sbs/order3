package com.b4pay.order.service;

import com.alibaba.fastjson.JSONObject;
import com.b4pay.order.dao.*;
import com.b4pay.order.entity.*;
import com.b4pay.order.utils.HttpClientUtil;
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
import java.util.NoSuchElementException;

/**
 * @ClassName LotteryService
 * @Description
 * @Version 2.1
 **/
@Service
@Transactional(rollbackFor = Exception.class)
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

    @Autowired
    private GradeRecordDao gradeRecordDao;

    @Autowired
    private PersonalRecordDao personalRecordDao;

    @Autowired
    private PersonalCountDao personalCountDao;

    private static final String NUMBER = "number";
    private static final String ZODIAC = "zodiac";
    private static final String SIZE = "size";
    private static final String COLOR = "color";
    private static final String SD = "sd";

    private static final String URL = "http://192.168.101.69:8083/data/app/getAgencyInfo";


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


    public void match(Integer period, List<Integer> numsList, List<Order> orderList, Date date) {


        //保存号码奖池
        this.saveNumberLotteryResult(period, numsList, orderList, date);

        //保存生肖奖池
        this.saveZodiacLotteryResult(period, numsList, orderList, date);

        //保存颜色奖池 红1 蓝2 绿3
        this.saveColorLotteryResult(period, numsList, orderList, date);

        //保存单双奖池 1是双 2是单
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
        lotteryResult.setId(idWorker.nextId() + "");
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
                if (order.getNumberTwo() != null && order.getNumberTwo() != 0 && this.judgeZodiac(num2) == order.getNumberTwo()) {
                    count2 = count2.add(new BigDecimal("1"));
                }
                if (order.getNumberThree() != null && order.getNumberThree() != 0 && this.judgeZodiac(num3) == order.getNumberThree()) {
                    count3 = count3.add(new BigDecimal("1"));
                }
                if (order.getNumberFour() != null && order.getNumberFour() != 0 && this.judgeZodiac(num4) == order.getNumberFour()) {
                    count4 = count4.add(new BigDecimal("1"));
                }
                if (order.getNumberFive() != null && order.getNumberFive() != 0 && this.judgeZodiac(num5) == order.getNumberFive()) {
                    count5 = count5.add(new BigDecimal("1"));
                }
                if (order.getNumberSix() != null && order.getNumberSix() != 0 && this.judgeZodiac(num6) == order.getNumberSix()) {
                    count6 = count6.add(new BigDecimal("1"));
                }
                if (order.getNumberSeven() != null && order.getNumberSeven() != 0 && this.judgeZodiac(num7) == order.getNumberSeven()) {
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
            numberTwoMoney = count.getNumberTwoTotalMoney().divide(count2, 2, BigDecimal.ROUND_HALF_UP);
        }
        if (!count3.equals(BigDecimal.ZERO)) {
            numberThreeMoney = count.getNumberThreeTotalMoney().divide(count3, 2, BigDecimal.ROUND_HALF_UP);
        }
        if (!count4.equals(BigDecimal.ZERO)) {
            numberFourMoney = count.getNumberFourTotalMoney().divide(count4, 2, BigDecimal.ROUND_HALF_UP);
        }
        if (!count5.equals(BigDecimal.ZERO)) {
            numberFiveMoney = count.getNumberFiveTotalMoney().divide(count5, 2, BigDecimal.ROUND_HALF_UP);
        }
        if (!count6.equals(BigDecimal.ZERO)) {
            numberSixMoney = count.getNumberSixTotalMoney().divide(count6, 2, BigDecimal.ROUND_HALF_UP);
        }
        if (!count7.equals(BigDecimal.ZERO)) {
            numberSevenMoney = count.getNumberSevenTotalMoney().divide(count7, 2, BigDecimal.ROUND_HALF_UP);
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

        BigDecimal partnerCount = new BigDecimal("0.05");
        BigDecimal gradeThree = new BigDecimal("0.05");
        BigDecimal gradeTwo = new BigDecimal("0.03");
        BigDecimal gradeOne = new BigDecimal("0.07");


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

                //查找三级
                Map<String, String> map = this.findGradeByAgencyId(order.getAgencyId());

                //保存三级

                GradeRecord gradeThreeUser = null;
                GradeRecord gradeTwoUser = null;
                GradeRecord gradeOneUser = null;

                GradeRecord partner = null;

                if (mapGetValue(map, "partnerId") != null && !"".equals(mapGetValue(map, "partnerId"))) {
                    partner = new GradeRecord();
                    partner.setId(idWorker.nextId() + "");
                    partner.setPeriod(period);
                    partner.setAgencyId(order.getAgencyId());
                    partner.setGrade("partner");
                    partner.setGradeId(map.get("partnerId"));
                    partner.setGradeName(map.get("partnerName"));
                    partner.setType(ZODIAC);
                    partner.setCreateTime(date);
                }
                if (mapGetValue(map, "gradeThreeUser") != null && !"".equals(mapGetValue(map, "gradeThreeUser"))) {
                    gradeThreeUser = new GradeRecord();
                    gradeThreeUser.setId(idWorker.nextId() + "");
                    gradeThreeUser.setPeriod(period);
                    gradeThreeUser.setAgencyId(order.getAgencyId());
                    gradeThreeUser.setGrade("gradeThreeUser");
                    gradeThreeUser.setGradeId(map.get("gradeThreeUser"));
                    gradeThreeUser.setGradeName(map.get("gradeThreeUserName"));
                    gradeThreeUser.setType(ZODIAC);
                    gradeThreeUser.setCreateTime(date);
                }
                if (mapGetValue(map, "gradeTwoUser") != null && !"".equals(mapGetValue(map, "gradeTwoUser"))) {
                    gradeTwoUser = new GradeRecord();
                    gradeTwoUser.setId(idWorker.nextId() + "");
                    gradeTwoUser.setPeriod(period);
                    gradeTwoUser.setAgencyId(order.getAgencyId());
                    gradeTwoUser.setGrade("gradeTwoUser");
                    gradeTwoUser.setGradeId(map.get("gradeTwoUser"));
                    gradeTwoUser.setGradeName(map.get("gradeTwoUserName"));
                    gradeTwoUser.setType(ZODIAC);
                    gradeTwoUser.setCreateTime(date);
                }
                if (mapGetValue(map, "gradeOneUser") != null && !"".equals(mapGetValue(map, "gradeOneUser"))) {
                    gradeOneUser = new GradeRecord();
                    gradeOneUser.setId(idWorker.nextId() + "");
                    gradeOneUser.setPeriod(period);
                    gradeOneUser.setAgencyId(order.getAgencyId());
                    gradeOneUser.setGrade("gradeOneUser");
                    gradeOneUser.setGradeId(map.get("gradeOneUser"));
                    gradeOneUser.setGradeName(map.get("gradeOneUserName"));
                    gradeOneUser.setType(ZODIAC);
                    gradeOneUser.setCreateTime(date);
                }


                if (order.getNumberOne() != null && order.getNumberOne() != 0 && this.judgeZodiac(num1) == order.getNumberOne()) {
                    lotteryRecord.setNumberOneMoney(numberOneMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberOneMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberOneMoney(numberOneMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberOneMoney(numberOneMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberOneMoney(numberOneMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberOneMoney(numberOneMoney.multiply(partnerCount));
                    }

                }

                if (order.getNumberTwo() != null && order.getNumberTwo() != 0 && this.judgeZodiac(num2) == order.getNumberTwo()) {
                    lotteryRecord.setNumberTwoMoney(numberTwoMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberTwoMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberTwoMoney(numberTwoMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberTwoMoney(numberTwoMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberTwoMoney(numberTwoMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberTwoMoney(numberTwoMoney.multiply(partnerCount));
                    }
                }

                if (order.getNumberThree() != null && order.getNumberThree() != 0 && this.judgeZodiac(num3) == order.getNumberThree()) {
                    lotteryRecord.setNumberThreeMoney(numberThreeMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberThreeMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberThreeMoney(numberThreeMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberThreeMoney(numberThreeMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberThreeMoney(numberThreeMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberThreeMoney(numberThreeMoney.multiply(partnerCount));
                    }
                }

                if (order.getNumberFour() != null && order.getNumberFour() != 0 && this.judgeZodiac(num4) == order.getNumberFour()) {
                    lotteryRecord.setNumberFourMoney(numberFourMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberFourMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberFourMoney(numberFourMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberFourMoney(numberFourMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberFourMoney(numberFourMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberFourMoney(numberFourMoney.multiply(partnerCount));
                    }
                }

                if (order.getNumberFive() != null && order.getNumberFive() != 0 && this.judgeZodiac(num5) == order.getNumberFive()) {
                    lotteryRecord.setNumberFiveMoney(numberFiveMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberFiveMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberFiveMoney(numberFiveMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberFiveMoney(numberFiveMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberFiveMoney(numberFiveMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberFiveMoney(numberFiveMoney.multiply(partnerCount));
                    }
                }

                if (order.getNumberSix() != null && order.getNumberSix() != 0 && this.judgeZodiac(num6) == order.getNumberSix()) {
                    lotteryRecord.setNumberSixMoney(numberSixMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberSixMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberSixMoney(numberSixMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberSixMoney(numberSixMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberSixMoney(numberSixMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberSixMoney(numberSixMoney.multiply(partnerCount));
                    }
                }

                if (order.getNumberSeven() != null && order.getNumberSeven() != 0 && this.judgeZodiac(num7) == order.getNumberSeven()) {
                    lotteryRecord.setNumberSevenMoney(numberSevenMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberSevenMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberSevenMoney(numberSevenMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberSevenMoney(numberSevenMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberSevenMoney(numberSevenMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberSevenMoney(numberSevenMoney.multiply(partnerCount));
                    }
                }
                this.saveRecord(gradeThreeUser, gradeTwoUser, gradeOneUser, partner);

                lotteryRecord.setTotalMoney(totalMoney);
                lotteryRecord.setCreateTime(date);
                lotteryRecordDao.save(lotteryRecord);
            }
        }
    }

    //保存颜色奖池
    private void saveColorLotteryResult(Integer period, List<Integer> numsList, List<Order> orderList, Date date) {
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
        lotteryResult.setId(idWorker.nextId() + "");
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
                if (order.getNumberTwo() != null && order.getNumberTwo() != 0 && this.judgeColor(num2) == order.getNumberTwo()) {
                    count2 = count2.add(new BigDecimal("1"));
                }
                if (order.getNumberThree() != null && order.getNumberThree() != 0 && this.judgeColor(num3) == order.getNumberThree()) {
                    count3 = count3.add(new BigDecimal("1"));
                }
                if (order.getNumberFour() != null && order.getNumberFour() != 0 && this.judgeColor(num4) == order.getNumberFour()) {
                    count4 = count4.add(new BigDecimal("1"));
                }
                if (order.getNumberFive() != null && order.getNumberFive() != 0 && this.judgeColor(num5) == order.getNumberFive()) {
                    count5 = count5.add(new BigDecimal("1"));
                }
                if (order.getNumberSix() != null && order.getNumberSix() != 0 && this.judgeColor(num6) == order.getNumberSix()) {
                    count6 = count6.add(new BigDecimal("1"));
                }
                if (order.getNumberSeven() != null && order.getNumberSeven() != 0 && this.judgeColor(num7) == order.getNumberSeven()) {
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
            numberTwoMoney = count.getNumberTwoTotalMoney().divide(count2, 2, BigDecimal.ROUND_HALF_UP);
        }
        if (!count3.equals(BigDecimal.ZERO)) {
            numberThreeMoney = count.getNumberThreeTotalMoney().divide(count3, 2, BigDecimal.ROUND_HALF_UP);
        }
        if (!count4.equals(BigDecimal.ZERO)) {
            numberFourMoney = count.getNumberFourTotalMoney().divide(count4, 2, BigDecimal.ROUND_HALF_UP);
        }
        if (!count5.equals(BigDecimal.ZERO)) {
            numberFiveMoney = count.getNumberFiveTotalMoney().divide(count5, 2, BigDecimal.ROUND_HALF_UP);
        }
        if (!count6.equals(BigDecimal.ZERO)) {
            numberSixMoney = count.getNumberSixTotalMoney().divide(count6, 2, BigDecimal.ROUND_HALF_UP);
        }
        if (!count7.equals(BigDecimal.ZERO)) {
            numberSevenMoney = count.getNumberSevenTotalMoney().divide(count7, 2, BigDecimal.ROUND_HALF_UP);
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

        BigDecimal partnerCount = new BigDecimal("0.05");
        BigDecimal gradeThree = new BigDecimal("0.05");
        BigDecimal gradeTwo = new BigDecimal("0.03");
        BigDecimal gradeOne = new BigDecimal("0.07");

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
                Map<String, String> map = this.findGradeByAgencyId(order.getAgencyId());

                //保存三级
                GradeRecord gradeThreeUser = null;
                GradeRecord gradeTwoUser = null;
                GradeRecord gradeOneUser = null;

                GradeRecord partner = null;

                if (mapGetValue(map, "partnerId") != null && !"".equals(mapGetValue(map, "partnerId"))) {
                    partner = new GradeRecord();
                    partner.setId(idWorker.nextId() + "");
                    partner.setPeriod(period);
                    partner.setAgencyId(order.getAgencyId());
                    partner.setGrade("partner");
                    partner.setGradeId(map.get("partnerId"));
                    partner.setGradeName(map.get("partnerName"));
                    partner.setType(COLOR);
                    partner.setCreateTime(date);
                }
                if (mapGetValue(map, "gradeThreeUser") != null && !"".equals(mapGetValue(map, "gradeThreeUser"))) {
                    gradeThreeUser = new GradeRecord();
                    gradeThreeUser.setId(idWorker.nextId() + "");
                    gradeThreeUser.setPeriod(period);
                    gradeThreeUser.setAgencyId(order.getAgencyId());
                    gradeThreeUser.setGrade("gradeThreeUser");
                    gradeThreeUser.setGradeId(map.get("gradeThreeUser"));
                    gradeThreeUser.setGradeName(map.get("gradeThreeUserName"));
                    gradeThreeUser.setType(COLOR);
                    gradeThreeUser.setCreateTime(date);
                }
                if (mapGetValue(map, "gradeTwoUser") != null && !"".equals(mapGetValue(map, "gradeTwoUser"))) {
                    gradeTwoUser = new GradeRecord();
                    gradeTwoUser.setId(idWorker.nextId() + "");
                    gradeTwoUser.setPeriod(period);
                    gradeTwoUser.setAgencyId(order.getAgencyId());
                    gradeTwoUser.setGrade("gradeTwoUser");
                    gradeTwoUser.setGradeId(map.get("gradeTwoUser"));
                    gradeTwoUser.setGradeName(map.get("gradeTwoUserName"));
                    gradeTwoUser.setType(COLOR);
                    gradeTwoUser.setCreateTime(date);
                }
                if (mapGetValue(map, "gradeOneUser") != null && !"".equals(mapGetValue(map, "gradeOneUser"))) {
                    gradeOneUser = new GradeRecord();
                    gradeOneUser.setId(idWorker.nextId() + "");
                    gradeOneUser.setPeriod(period);
                    gradeOneUser.setAgencyId(order.getAgencyId());
                    gradeOneUser.setGrade("gradeOneUser");
                    gradeOneUser.setGradeId(map.get("gradeOneUser"));
                    gradeOneUser.setGradeName(map.get("gradeOneUserName"));
                    gradeOneUser.setType(COLOR);
                    gradeOneUser.setCreateTime(date);
                }

                if (order.getNumberOne() != null && order.getNumberOne() != 0 && this.judgeColor(num1) == order.getNumberOne()) {
                    lotteryRecord.setNumberOneMoney(numberOneMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberOneMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberOneMoney(numberOneMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberOneMoney(numberOneMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberOneMoney(numberOneMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberOneMoney(numberOneMoney.multiply(partnerCount));
                    }
                }
                if (order.getNumberTwo() != null && order.getNumberTwo() != 0 && this.judgeColor(num2) == order.getNumberTwo()) {
                    lotteryRecord.setNumberTwoMoney(numberTwoMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberTwoMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberTwoMoney(numberTwoMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberTwoMoney(numberTwoMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberTwoMoney(numberTwoMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberTwoMoney(numberTwoMoney.multiply(partnerCount));
                    }
                }

                if (order.getNumberThree() != null && order.getNumberThree() != 0 && this.judgeColor(num3) == order.getNumberThree()) {
                    lotteryRecord.setNumberThreeMoney(numberThreeMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberThreeMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberThreeMoney(numberThreeMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberThreeMoney(numberThreeMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberThreeMoney(numberThreeMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberThreeMoney(numberThreeMoney.multiply(partnerCount));
                    }
                }

                if (order.getNumberFour() != null && order.getNumberFour() != 0 && this.judgeColor(num4) == order.getNumberFour()) {
                    lotteryRecord.setNumberFourMoney(numberFourMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberFourMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberFourMoney(numberFourMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberFourMoney(numberFourMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberFourMoney(numberFourMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberFourMoney(numberFourMoney.multiply(partnerCount));
                    }
                }

                if (order.getNumberFive() != null && order.getNumberFive() != 0 && this.judgeColor(num5) == order.getNumberFive()) {
                    lotteryRecord.setNumberFiveMoney(numberFiveMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberFiveMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberFiveMoney(numberFiveMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberFiveMoney(numberFiveMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberFiveMoney(numberFiveMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberFiveMoney(numberFiveMoney.multiply(partnerCount));
                    }
                }

                if (order.getNumberSix() != null && order.getNumberSix() != 0 && this.judgeColor(num6) == order.getNumberSix()) {
                    lotteryRecord.setNumberSixMoney(numberSixMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberSixMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberSixMoney(numberSixMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberSixMoney(numberSixMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberSixMoney(numberSixMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberSixMoney(numberSixMoney.multiply(partnerCount));
                    }
                }

                if (order.getNumberSeven() != null && order.getNumberSeven() != 0 && this.judgeColor(num7) == order.getNumberSeven()) {
                    lotteryRecord.setNumberSevenMoney(numberSevenMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberSevenMoney).multiply(discount);
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberSevenMoney(numberSevenMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberSevenMoney(numberSevenMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberSevenMoney(numberSevenMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberSevenMoney(numberSevenMoney.multiply(partnerCount));
                    }
                }

                this.saveRecord(gradeThreeUser, gradeTwoUser, gradeOneUser, partner);

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
        lotteryResult.setId(idWorker.nextId() + "");
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
                if (order.getNumberTwo() != null && order.getNumberTwo() != 0 && this.judgeSize(num2) == order.getNumberTwo() && num2 != 25) {
                    count2 = count2.add(new BigDecimal("1"));
                }
                if (order.getNumberThree() != null && order.getNumberThree() != 0 && this.judgeSize(num3) == order.getNumberThree() && num3 != 25) {
                    count3 = count3.add(new BigDecimal("1"));
                }
                if (order.getNumberFour() != null && order.getNumberFour() != 0 && this.judgeSize(num4) == order.getNumberFour() && num4 != 25) {
                    count4 = count4.add(new BigDecimal("1"));
                }
                if (order.getNumberFive() != null && order.getNumberFive() != 0 && this.judgeSize(num5) == order.getNumberFive() && num5 != 25) {
                    count5 = count5.add(new BigDecimal("1"));
                }
                if (order.getNumberSix() != null && order.getNumberSix() != 0 && this.judgeSize(num6) == order.getNumberSix() && num6 != 25) {
                    count6 = count6.add(new BigDecimal("1"));
                }
                if (order.getNumberSeven() != null && order.getNumberSeven() != 0 && this.judgeSize(num7) == order.getNumberSeven() && num7 != 25) {
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
            numberTwoMoney = count.getNumberTwoTotalMoney().divide(count2, 2, BigDecimal.ROUND_HALF_UP);
        }
        if (!count3.equals(BigDecimal.ZERO)) {
            numberThreeMoney = count.getNumberThreeTotalMoney().divide(count3, 2, BigDecimal.ROUND_HALF_UP);
        }
        if (!count4.equals(BigDecimal.ZERO)) {
            numberFourMoney = count.getNumberFourTotalMoney().divide(count4, 2, BigDecimal.ROUND_HALF_UP);
        }
        if (!count5.equals(BigDecimal.ZERO)) {
            numberFiveMoney = count.getNumberFiveTotalMoney().divide(count5, 2, BigDecimal.ROUND_HALF_UP);
        }
        if (!count6.equals(BigDecimal.ZERO)) {
            numberSixMoney = count.getNumberSixTotalMoney().divide(count6, 2, BigDecimal.ROUND_HALF_UP);
        }
        if (!count7.equals(BigDecimal.ZERO)) {
            numberSevenMoney = count.getNumberSevenTotalMoney().divide(count7, 2, BigDecimal.ROUND_HALF_UP);
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

        BigDecimal partnerCount = new BigDecimal("0.05");
        BigDecimal gradeThree = new BigDecimal("0.05");
        BigDecimal gradeTwo = new BigDecimal("0.03");
        BigDecimal gradeOne = new BigDecimal("0.07");

        //记录每个用户中奖金额
        for (Order order : orderList) {
            if (order.getType().equals(SIZE)) {
                LotteryRecord lotteryRecord = new LotteryRecord();
                lotteryRecord.setId(idWorker.nextId() + "");
                lotteryRecord.setPeriod(period);
                lotteryRecord.setAgencyId(order.getAgencyId());
                lotteryRecord.setType(SIZE);
                BigDecimal totalMoney = new BigDecimal("0");

                //查找三级
                Map<String, String> map = this.findGradeByAgencyId(order.getAgencyId());

                //保存三级
                GradeRecord gradeThreeUser = null;
                GradeRecord gradeTwoUser = null;
                GradeRecord gradeOneUser = null;

                GradeRecord partner = null;


                if (mapGetValue(map, "partnerId") != null && !"".equals(mapGetValue(map, "partnerId"))) {
                    partner = new GradeRecord();
                    partner.setId(idWorker.nextId() + "");
                    partner.setPeriod(period);
                    partner.setAgencyId(order.getAgencyId());
                    partner.setGrade("partner");
                    partner.setGradeId(map.get("partnerId"));
                    partner.setGradeName(map.get("partnerName"));
                    partner.setType(SIZE);
                    partner.setCreateTime(date);
                }
                if (mapGetValue(map, "gradeThreeUser") != null && !"".equals(mapGetValue(map, "gradeThreeUser"))) {
                    gradeThreeUser = new GradeRecord();
                    gradeThreeUser.setId(idWorker.nextId() + "");
                    gradeThreeUser.setPeriod(period);
                    gradeThreeUser.setAgencyId(order.getAgencyId());
                    gradeThreeUser.setGrade("gradeThreeUser");
                    gradeThreeUser.setGradeId(map.get("gradeThreeUser"));
                    gradeThreeUser.setGradeName(map.get("gradeThreeUserName"));
                    gradeThreeUser.setType(SIZE);
                    gradeThreeUser.setCreateTime(date);
                }
                if (mapGetValue(map, "gradeTwoUser") != null && !"".equals(mapGetValue(map, "gradeTwoUser"))) {
                    gradeTwoUser = new GradeRecord();
                    gradeTwoUser.setId(idWorker.nextId() + "");
                    gradeTwoUser.setPeriod(period);
                    gradeTwoUser.setAgencyId(order.getAgencyId());
                    gradeTwoUser.setGrade("gradeTwoUser");
                    gradeTwoUser.setGradeId(map.get("gradeTwoUser"));
                    gradeTwoUser.setGradeName(map.get("gradeTwoUserName"));
                    gradeTwoUser.setType(SIZE);
                    gradeTwoUser.setCreateTime(date);
                }
                if (mapGetValue(map, "gradeOneUser") != null && !"".equals(mapGetValue(map, "gradeOneUser"))) {
                    gradeOneUser = new GradeRecord();
                    gradeOneUser.setId(idWorker.nextId() + "");
                    gradeOneUser.setPeriod(period);
                    gradeOneUser.setAgencyId(order.getAgencyId());
                    gradeOneUser.setGrade("gradeOneUser");
                    gradeOneUser.setGradeId(map.get("gradeOneUser"));
                    gradeOneUser.setGradeName(map.get("gradeOneUserName"));
                    gradeOneUser.setType(SIZE);
                    gradeOneUser.setCreateTime(date);
                }


                if (order.getNumberOne() != null && order.getNumberOne() != 0 && this.judgeSize(num1) == order.getNumberOne() && num1 != 25) {
                    lotteryRecord.setNumberOneMoney(numberOneMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberOneMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberOneMoney(numberOneMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberOneMoney(numberOneMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberOneMoney(numberOneMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberOneMoney(numberOneMoney.multiply(partnerCount));
                    }
                }

                if (order.getNumberTwo() != null && order.getNumberTwo() != 0 && this.judgeSize(num2) == order.getNumberTwo() && num2 != 25) {
                    lotteryRecord.setNumberTwoMoney(numberTwoMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberTwoMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberTwoMoney(numberTwoMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberTwoMoney(numberTwoMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberTwoMoney(numberTwoMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberTwoMoney(numberTwoMoney.multiply(partnerCount));
                    }
                }
                if (order.getNumberThree() != null && order.getNumberThree() != 0 && this.judgeSize(num3) == order.getNumberThree() && num3 != 25) {
                    lotteryRecord.setNumberThreeMoney(numberThreeMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberThreeMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberThreeMoney(numberThreeMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberThreeMoney(numberThreeMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberThreeMoney(numberThreeMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberThreeMoney(numberThreeMoney.multiply(partnerCount));
                    }
                }
                if (order.getNumberFour() != null && order.getNumberFour() != 0 && this.judgeSize(num4) == order.getNumberFour() && num4 != 25) {
                    lotteryRecord.setNumberFourMoney(numberFourMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberFourMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberFourMoney(numberFourMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberFourMoney(numberFourMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberFourMoney(numberFourMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberFourMoney(numberFourMoney.multiply(partnerCount));
                    }
                }
                if (order.getNumberFive() != null && order.getNumberFive() != 0 && this.judgeSize(num5) == order.getNumberFive() && num5 != 25) {
                    lotteryRecord.setNumberFiveMoney(numberFiveMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberFiveMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberFiveMoney(numberFiveMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberFiveMoney(numberFiveMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberFiveMoney(numberFiveMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberFiveMoney(numberFiveMoney.multiply(partnerCount));
                    }
                }
                if (order.getNumberSix() != null && order.getNumberSix() != 0 && this.judgeSize(num6) == order.getNumberSix() && num6 != 25) {
                    lotteryRecord.setNumberSixMoney(numberSixMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberSixMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberSixMoney(numberSixMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberSixMoney(numberSixMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberSixMoney(numberSixMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberSixMoney(numberSixMoney.multiply(partnerCount));
                    }
                }

                if (order.getNumberSeven() != null && order.getNumberSeven() != 0 && this.judgeSize(num7) == order.getNumberSeven() && num7 != 25) {
                    lotteryRecord.setNumberSevenMoney(numberSevenMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberSevenMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberSevenMoney(numberSevenMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberSevenMoney(numberSevenMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberSevenMoney(numberSevenMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberSevenMoney(numberSevenMoney.multiply(partnerCount));
                    }
                }
                this.saveRecord(gradeThreeUser, gradeTwoUser, gradeOneUser, partner);

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
        lotteryResult.setId(idWorker.nextId() + "");
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
                if (order.getNumberTwo() != null && order.getNumberTwo() != 0 && num2 % 2 + 1 == order.getNumberTwo() && num2 != 49) {
                    count2 = count2.add(new BigDecimal("1"));
                }
                if (order.getNumberThree() != null && order.getNumberThree() != 0 && num3 % 2 + 1 == order.getNumberThree() && num3 != 49) {
                    count3 = count3.add(new BigDecimal("1"));
                }
                if (order.getNumberFour() != null && order.getNumberFour() != 0 && num4 % 2 + 1 == order.getNumberFour() && num4 != 49) {
                    count4 = count4.add(new BigDecimal("1"));
                }
                if (order.getNumberFive() != null && order.getNumberFive() != 0 && num5 % 2 + 1 == order.getNumberFive() && num5 != 49) {
                    count5 = count5.add(new BigDecimal("1"));
                }
                if (order.getNumberSix() != null && order.getNumberSix() != 0 && num6 % 2 + 1 == order.getNumberSix() && num6 != 49) {
                    count6 = count6.add(new BigDecimal("1"));
                }
                if (order.getNumberSeven() != null && order.getNumberSeven() != 0 && num7 % 2 + 1 == order.getNumberSeven() && num7 != 49) {
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
            numberTwoMoney = count.getNumberTwoTotalMoney().divide(count2, 2, BigDecimal.ROUND_HALF_UP);
        }
        if (!count3.equals(BigDecimal.ZERO)) {
            numberThreeMoney = count.getNumberThreeTotalMoney().divide(count3, 2, BigDecimal.ROUND_HALF_UP);
        }
        if (!count4.equals(BigDecimal.ZERO)) {
            numberFourMoney = count.getNumberFourTotalMoney().divide(count4, 2, BigDecimal.ROUND_HALF_UP);
        }
        if (!count5.equals(BigDecimal.ZERO)) {
            numberFiveMoney = count.getNumberFiveTotalMoney().divide(count5, 2, BigDecimal.ROUND_HALF_UP);
        }
        if (!count6.equals(BigDecimal.ZERO)) {
            numberSixMoney = count.getNumberSixTotalMoney().divide(count6, 2, BigDecimal.ROUND_HALF_UP);
        }
        if (!count7.equals(BigDecimal.ZERO)) {
            numberSevenMoney = count.getNumberSevenTotalMoney().divide(count7, 2, BigDecimal.ROUND_HALF_UP);
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

        BigDecimal partnerCount = new BigDecimal("0.05");
        BigDecimal gradeThree = new BigDecimal("0.05");
        BigDecimal gradeTwo = new BigDecimal("0.03");
        BigDecimal gradeOne = new BigDecimal("0.07");


        //记录每个用户中奖金额
        for (Order order : orderList) {
            if (order.getType().equals(SD)) {
                LotteryRecord lotteryRecord = new LotteryRecord();
                lotteryRecord.setId(idWorker.nextId() + "");
                lotteryRecord.setPeriod(period);
                lotteryRecord.setAgencyId(order.getAgencyId());
                lotteryRecord.setType(SD);
                BigDecimal totalMoney = new BigDecimal("0");

                //查找三级
                Map<String, String> map = this.findGradeByAgencyId(order.getAgencyId());

                //保存三级
                GradeRecord gradeThreeUser = null;
                GradeRecord gradeTwoUser = null;
                GradeRecord gradeOneUser = null;
                GradeRecord partner = null;

                if (mapGetValue(map, "partnerId") != null && !"".equals(mapGetValue(map, "partnerId"))) {
                    partner = new GradeRecord();
                    partner.setId(idWorker.nextId() + "");
                    partner.setPeriod(period);
                    partner.setAgencyId(order.getAgencyId());
                    partner.setGrade("partner");
                    partner.setGradeId(map.get("partnerId"));
                    partner.setGradeName(map.get("partnerName"));
                    partner.setType(SD);
                    partner.setCreateTime(date);
                }
                if (mapGetValue(map, "gradeThreeUser") != null && !"".equals(mapGetValue(map, "gradeThreeUser"))) {
                    gradeThreeUser = new GradeRecord();
                    gradeThreeUser.setId(idWorker.nextId() + "");
                    gradeThreeUser.setPeriod(period);
                    gradeThreeUser.setAgencyId(order.getAgencyId());
                    gradeThreeUser.setGrade("gradeThreeUser");
                    gradeThreeUser.setGradeId(map.get("gradeThreeUser"));
                    gradeThreeUser.setGradeName(map.get("gradeThreeUserName"));
                    gradeThreeUser.setType(SD);
                    gradeThreeUser.setCreateTime(date);
                }
                if (mapGetValue(map, "gradeTwoUser") != null && !"".equals(mapGetValue(map, "gradeTwoUser"))) {
                    gradeTwoUser = new GradeRecord();
                    gradeTwoUser.setId(idWorker.nextId() + "");
                    gradeTwoUser.setPeriod(period);
                    gradeTwoUser.setAgencyId(order.getAgencyId());
                    gradeTwoUser.setGrade("gradeTwoUser");
                    gradeTwoUser.setGradeId(map.get("gradeTwoUser"));
                    gradeTwoUser.setGradeName(map.get("gradeTwoUserName"));
                    gradeTwoUser.setType(SD);
                    gradeTwoUser.setCreateTime(date);
                }
                if (mapGetValue(map, "gradeOneUser") != null && !"".equals(mapGetValue(map, "gradeOneUser"))) {
                    gradeOneUser = new GradeRecord();
                    gradeOneUser.setId(idWorker.nextId() + "");
                    gradeOneUser.setPeriod(period);
                    gradeOneUser.setAgencyId(order.getAgencyId());
                    gradeOneUser.setGrade("gradeOneUser");
                    gradeOneUser.setGradeId(map.get("gradeOneUser"));
                    gradeOneUser.setGradeName(map.get("gradeOneUserName"));
                    gradeOneUser.setType(SD);
                    gradeOneUser.setCreateTime(date);
                }

                if (order.getNumberOne() != null && order.getNumberOne() != 0 && num1 % 2 + 1 == order.getNumberOne() && num1 != 49) {
                    lotteryRecord.setNumberOneMoney(numberOneMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberOneMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberOneMoney(numberOneMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberOneMoney(numberOneMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberOneMoney(numberOneMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberOneMoney(numberOneMoney.multiply(partnerCount));
                    }
                }

                if (order.getNumberTwo() != null && order.getNumberTwo() != 0 && num2 % 2 + 1 == order.getNumberTwo() && num2 != 49) {
                    lotteryRecord.setNumberTwoMoney(numberTwoMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberTwoMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberTwoMoney(numberTwoMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberTwoMoney(numberTwoMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberTwoMoney(numberTwoMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberTwoMoney(numberTwoMoney.multiply(partnerCount));
                    }
                }

                if (order.getNumberThree() != null && order.getNumberThree() != 0 && num3 % 2 + 1 == order.getNumberThree() && num3 != 49) {
                    lotteryRecord.setNumberThreeMoney(numberThreeMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberThreeMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberThreeMoney(numberThreeMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberThreeMoney(numberThreeMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberThreeMoney(numberThreeMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberThreeMoney(numberThreeMoney.multiply(partnerCount));
                    }
                }

                if (order.getNumberFour() != null && order.getNumberFour() != 0 && num4 % 2 + 1 == order.getNumberFour() && num4 != 49) {
                    lotteryRecord.setNumberFourMoney(numberFourMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberFourMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberFourMoney(numberFourMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberFourMoney(numberFourMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberFourMoney(numberFourMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberFourMoney(numberFourMoney.multiply(partnerCount));
                    }
                }

                if (order.getNumberFive() != null && order.getNumberFive() != 0 && num5 % 2 + 1 == order.getNumberFive() && num5 != 49) {
                    lotteryRecord.setNumberFiveMoney(numberFiveMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberFiveMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberFiveMoney(numberFiveMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberFiveMoney(numberFiveMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberFiveMoney(numberFiveMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberFiveMoney(numberFiveMoney.multiply(partnerCount));
                    }
                }

                if (order.getNumberSix() != null && order.getNumberSix() != 0 && num6 % 2 + 1 == order.getNumberSix() && num6 != 49) {
                    lotteryRecord.setNumberSixMoney(numberSixMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberSixMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberSixMoney(numberSixMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberSixMoney(numberSixMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberSixMoney(numberSixMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberSixMoney(numberSixMoney.multiply(partnerCount));
                    }
                }

                if (order.getNumberSeven() != null && order.getNumberSeven() != 0 && num7 % 2 + 1 == order.getNumberSeven() && num7 != 49) {
                    lotteryRecord.setNumberSevenMoney(numberSevenMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberSevenMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberSevenMoney(numberSevenMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberSevenMoney(numberSevenMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberSevenMoney(numberSevenMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberSevenMoney(numberSevenMoney.multiply(partnerCount));
                    }
                }
                this.saveRecord(gradeThreeUser, gradeTwoUser, gradeOneUser, partner);

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
        lotteryResult.setId(idWorker.nextId() + "");
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
                if (order.getNumberTwo() != null && order.getNumberTwo() != 0 && num2 == order.getNumberTwo()) {
                    count2 = count2.add(new BigDecimal("1"));
                }
                if (order.getNumberThree() != null && order.getNumberThree() != 0 && num3 == order.getNumberThree()) {
                    count3 = count3.add(new BigDecimal("1"));
                }
                if (order.getNumberFour() != null && order.getNumberFour() != 0 && num4 == order.getNumberFour()) {
                    count4 = count4.add(new BigDecimal("1"));
                }
                if (order.getNumberFive() != null && order.getNumberFive() != 0 && num5 == order.getNumberFive()) {
                    count5 = count5.add(new BigDecimal("1"));
                }
                if (order.getNumberSix() != null && order.getNumberSix() != 0 && num6 == order.getNumberSix()) {
                    count6 = count6.add(new BigDecimal("1"));
                }
                if (order.getNumberSeven() != null && order.getNumberSeven() != 0 && num7 == order.getNumberSeven()) {
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
            numberTwoMoney = count.getNumberTwoTotalMoney().divide(count2, 2, BigDecimal.ROUND_HALF_UP);
        }
        if (!count3.equals(BigDecimal.ZERO)) {
            numberThreeMoney = count.getNumberThreeTotalMoney().divide(count3, 2, BigDecimal.ROUND_HALF_UP);
        }
        if (!count4.equals(BigDecimal.ZERO)) {
            numberFourMoney = count.getNumberFourTotalMoney().divide(count4, 2, BigDecimal.ROUND_HALF_UP);
        }
        if (!count5.equals(BigDecimal.ZERO)) {
            numberFiveMoney = count.getNumberFiveTotalMoney().divide(count5, 2, BigDecimal.ROUND_HALF_UP);
        }
        if (!count6.equals(BigDecimal.ZERO)) {
            numberSixMoney = count.getNumberSixTotalMoney().divide(count6, 2, BigDecimal.ROUND_HALF_UP);
        }
        if (!count7.equals(BigDecimal.ZERO)) {
            numberSevenMoney = count.getNumberSevenTotalMoney().divide(count7, 2, BigDecimal.ROUND_HALF_UP);
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

        BigDecimal partnerCount = new BigDecimal("0.05");
        BigDecimal gradeThree = new BigDecimal("0.05");
        BigDecimal gradeTwo = new BigDecimal("0.03");
        BigDecimal gradeOne = new BigDecimal("0.07");


        //记录每个用户中奖金额
        for (Order order : orderList) {
            if (order.getType().equals(NUMBER)) {
                LotteryRecord lotteryRecord = new LotteryRecord();
                lotteryRecord.setId(idWorker.nextId() + "");
                lotteryRecord.setPeriod(period);
                lotteryRecord.setAgencyId(order.getAgencyId());
                lotteryRecord.setType(NUMBER);
                BigDecimal totalMoney = new BigDecimal("0");

                //查找三级
                Map<String, String> map = this.findGradeByAgencyId(order.getAgencyId());

                //保存三级
                GradeRecord gradeThreeUser = null;
                GradeRecord gradeTwoUser = null;
                GradeRecord gradeOneUser = null;

                GradeRecord partner = null;

                if (mapGetValue(map, "partnerId") != null && !"".equals(mapGetValue(map, "partnerId"))) {
                    partner = new GradeRecord();
                    partner.setId(idWorker.nextId() + "");
                    partner.setPeriod(period);
                    partner.setAgencyId(order.getAgencyId());
                    partner.setGrade("partner");
                    partner.setGradeId(map.get("partnerId"));
                    partner.setGradeName(map.get("partnerName"));
                    partner.setType(NUMBER);
                    partner.setCreateTime(date);
                }
                if (mapGetValue(map, "gradeThreeUser") != null && !"".equals(mapGetValue(map, "gradeThreeUser"))) {
                    gradeThreeUser = new GradeRecord();
                    gradeThreeUser.setId(idWorker.nextId() + "");
                    gradeThreeUser.setPeriod(period);
                    gradeThreeUser.setAgencyId(order.getAgencyId());
                    gradeThreeUser.setGrade("gradeThreeUser");
                    gradeThreeUser.setGradeId(map.get("gradeThreeUser"));
                    gradeThreeUser.setGradeName(map.get("gradeThreeUserName"));
                    gradeThreeUser.setType(NUMBER);
                    gradeThreeUser.setCreateTime(date);
                }
                if (mapGetValue(map, "gradeTwoUser") != null && !"".equals(mapGetValue(map, "gradeTwoUser"))) {
                    gradeTwoUser = new GradeRecord();
                    gradeTwoUser.setId(idWorker.nextId() + "");
                    gradeTwoUser.setPeriod(period);
                    gradeTwoUser.setAgencyId(order.getAgencyId());
                    gradeTwoUser.setGrade("gradeTwoUser");
                    gradeTwoUser.setGradeId(map.get("gradeTwoUser"));
                    gradeTwoUser.setGradeName(map.get("gradeTwoUserName"));
                    gradeTwoUser.setType(NUMBER);
                    gradeTwoUser.setCreateTime(date);
                }
                if (mapGetValue(map, "gradeOneUser") != null && !"".equals(mapGetValue(map, "gradeOneUser"))) {
                    gradeOneUser = new GradeRecord();
                    gradeOneUser.setId(idWorker.nextId() + "");
                    gradeOneUser.setPeriod(period);
                    gradeOneUser.setAgencyId(order.getAgencyId());
                    gradeOneUser.setGrade("gradeOneUser");
                    gradeOneUser.setGradeId(map.get("gradeOneUser"));
                    gradeOneUser.setGradeName(map.get("gradeOneUserName"));
                    gradeOneUser.setType(NUMBER);
                    gradeOneUser.setCreateTime(date);
                }

                if (order.getNumberOne() != null && order.getNumberOne() != 0 && num1 == order.getNumberOne()) {
                    lotteryRecord.setNumberOneMoney(numberOneMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberOneMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberOneMoney(numberOneMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberOneMoney(numberOneMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberOneMoney(numberOneMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberOneMoney(numberOneMoney.multiply(partnerCount));
                    }
                }

                if (order.getNumberTwo() != null && order.getNumberTwo() != 0 && num2 == order.getNumberTwo()) {
                    lotteryRecord.setNumberTwoMoney(numberTwoMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberTwoMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberTwoMoney(numberTwoMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberTwoMoney(numberTwoMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberTwoMoney(numberTwoMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberTwoMoney(numberTwoMoney.multiply(partnerCount));
                    }
                }


                if (order.getNumberThree() != null && order.getNumberThree() != 0 && num3 == order.getNumberThree()) {
                    lotteryRecord.setNumberThreeMoney(numberThreeMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberThreeMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberThreeMoney(numberThreeMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberThreeMoney(numberThreeMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberThreeMoney(numberThreeMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberThreeMoney(numberThreeMoney.multiply(partnerCount));
                    }
                }

                if (order.getNumberFour() != null && order.getNumberFour() != 0 && num4 == order.getNumberFour()) {
                    lotteryRecord.setNumberFourMoney(numberFourMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberFourMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberFourMoney(numberFourMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberFourMoney(numberFourMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberFourMoney(numberFourMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberFourMoney(numberFourMoney.multiply(partnerCount));
                    }
                }

                if (order.getNumberFive() != null && order.getNumberFive() != 0 && num5 == order.getNumberFive()) {
                    lotteryRecord.setNumberFiveMoney(numberFiveMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberFiveMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberFiveMoney(numberFiveMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberFiveMoney(numberFiveMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberFiveMoney(numberFiveMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberFiveMoney(numberFiveMoney.multiply(partnerCount));
                    }
                }

                if (order.getNumberSix() != null && order.getNumberSix() != 0 && num6 == order.getNumberSix()) {
                    lotteryRecord.setNumberSixMoney(numberSixMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberSixMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberSixMoney(numberSixMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberSixMoney(numberSixMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberSixMoney(numberSixMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberSixMoney(numberSixMoney.multiply(partnerCount));
                    }
                }

                if (order.getNumberSeven() != null && order.getNumberSeven() != 0 && num7 == order.getNumberSeven()) {
                    lotteryRecord.setNumberSevenMoney(numberSevenMoney.multiply(discount));
                    totalMoney = totalMoney.add(numberSevenMoney.multiply(discount));
                    //三级
                    if (gradeThreeUser != null) {
                        gradeThreeUser.setNumberSevenMoney(numberSevenMoney.multiply(gradeThree));
                    }
                    //二级
                    if (gradeTwoUser != null) {
                        gradeTwoUser.setNumberSevenMoney(numberSevenMoney.multiply(gradeTwo));
                    }
                    //一级
                    if (gradeOneUser != null) {
                        gradeOneUser.setNumberSevenMoney(numberSevenMoney.multiply(gradeOne));
                    }
                    //总代
                    if (partner != null) {
                        partner.setNumberSevenMoney(numberSevenMoney.multiply(partnerCount));
                    }
                }
                this.saveRecord(gradeThreeUser, gradeTwoUser, gradeOneUser, partner);

                lotteryRecord.setTotalMoney(totalMoney);
                lotteryRecord.setCreateTime(date);
                lotteryRecordDao.save(lotteryRecord);
            }
        }
    }


    //保存三级
    private void saveRecord(GradeRecord gradeThreeUser, GradeRecord gradeTwoUser, GradeRecord gradeOneUser, GradeRecord partner) {
        if (gradeThreeUser != null) {
            gradeRecordDao.save(gradeThreeUser);
        }
        if (gradeTwoUser != null) {
            gradeRecordDao.save(gradeTwoUser);
        }
        if (gradeOneUser != null) {
            gradeRecordDao.save(gradeOneUser);
        }
        if (partner != null) {
            gradeRecordDao.save(partner);
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
        Integer[] red = {1, 2, 7, 8, 12, 13, 18, 19, 23, 24, 29, 30, 34, 35, 40, 45, 46}; //红
        Integer[] blue = {3, 4, 9, 10, 14, 15, 20, 25, 26, 31, 36, 37, 41, 42, 47, 48}; //蓝
        Integer[] green = {5, 6, 11, 16, 17, 21, 22, 27, 28, 32, 33, 38, 39, 43, 44, 49}; //绿

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

    //根据id查找三级
    private Map findGradeByAgencyId(String agencyId) {
        try {
            HttpClientUtil httpClientUtil = new HttpClientUtil();
            String result = httpClientUtil.doPost(URL, agencyId, "utf-8");
            Map<Object, Object> map = JSONObject.parseObject(result, Map.class);
            //Map<String,String> gradeMap = (Map<String, String>) map.get("data");
            return map;
        } catch (Exception e) {
            e.printStackTrace();
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

    public static String mapGetValue(Map<String, String> map, String key) {
        try {
            String value = map.get(key);
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    //统计每个人的中奖金额
    public void savePersonalRecord(Integer period, List<Order> orderList, Date date) {
        for (Order order : orderList) {
            String agencyId = order.getAgencyId();
            String type = order.getType();

            //查找个人中奖金额
            LotteryRecord record = lotteryRecordDao.findByAgencyIdAndPeriodAndType(agencyId, period, type);

            //获得下级分润
            List<GradeRecord> gradeRecordList = gradeRecordDao.findByGradeIdAndTypeAndPeriod(agencyId, type, period);


            //遍历下级分润
            for (GradeRecord gradeRecord : gradeRecordList) {
                PersonalRecord personalRecord = this.judgeGrade(order, record, gradeRecord, period, agencyId, date);
                //更新个人中奖总额
                this.updatePersonalCount(personalRecord,agencyId,date);

            }




        }
    }

    //更新个人中奖总额
    private void updatePersonalCount(PersonalRecord personalRecord, String agencyId,Date date) {
        PersonalCount personalCount = null;
        try {
            personalCount = personalCountDao.findById(agencyId).get();
        } catch (NoSuchElementException e) {
            personalCount = new PersonalCount();
            personalCount.setAgencyId(agencyId);
            personalCount.setCreateTime(date);
            personalCount.setTotalMoney(new BigDecimal("0"));
            personalCount.setTotalMoney(new BigDecimal("0"));
            personalCount.setTotalMoney(new BigDecimal("0"));
        }
        //TODO


    }

    private PersonalRecord judgeGrade(Order order, LotteryRecord record, GradeRecord gradeRecord, Integer period, String agencyId, Date date) {
        //获得个人下注号码
        Integer numberOne = order.getNumberOne();
        Integer numberTwo = order.getNumberTwo();
        Integer numberThree = order.getNumberThree();
        Integer numberFour = order.getNumberFour();
        Integer numberFive = order.getNumberFive();
        Integer numberSix = order.getNumberSix();
        Integer numberSeven = order.getNumberSeven();

        //获取个人中奖金额
        BigDecimal numberOneMoney = record.getNumberOneMoney();
        if (numberOneMoney == null) {
            numberOneMoney = new BigDecimal("0");
        }

        BigDecimal numberTwoMoney = record.getNumberTwoMoney();
        if (numberTwoMoney == null) {
            numberTwoMoney = new BigDecimal("0");
        }

        BigDecimal numberThreeMoney = record.getNumberThreeMoney();
        if (numberThreeMoney == null) {
            numberThreeMoney = new BigDecimal("0");
        }

        BigDecimal numberFourMoney = record.getNumberFourMoney();
        if (numberFourMoney == null) {
            numberFourMoney = new BigDecimal("0");
        }

        BigDecimal numberFiveMoney = record.getNumberFiveMoney();
        if (numberFiveMoney == null) {
            numberFiveMoney = new BigDecimal("0");
        }

        BigDecimal numberSixMoney = record.getNumberSixMoney();
        if (numberSixMoney == null) {
            numberSixMoney = new BigDecimal("0");
        }

        BigDecimal numberSevenMoney = record.getNumberSevenMoney();
        if (numberSevenMoney == null) {
            numberSevenMoney = new BigDecimal("0");
        }
        //获得下级
        String gradeName = gradeRecord.getGradeName();

        String grade = null;
        if ("gradeThreeUser".equals(gradeName)) {
            grade = "gradeOne";
        } else if ("gradeTwoUser".equals(gradeName)) {
            grade = "gradeTwo";
        } else if ("gradeThreeUser".equals(gradeName)) {
            grade = "gradeOne";
        }

        //根据下级级别，获得下级个人分润记录
        PersonalRecord personalRecord = personalRecordDao.findByGradeAndPeriodAndAgencyId(grade, period, agencyId);
        //记录不为空
        if (personalRecord != null) {
            //判断是否有分润以及是否下注
            //1号
            if (gradeRecord.getNumberOneMoney() != null) {
                //有分润，
                numberOneMoney = numberOneMoney.add(gradeRecord.getNumberOneMoney());
                if (numberOne != 0) { //该类型有下注
                    personalRecord.setNumberOneTotalMoney(personalRecord.getNumberOneTotalMoney().add(numberOneMoney));
                } else if (numberOne == 0) { //该类型没下注
                    personalRecord.setNumberOneCnTotalMoney(personalRecord.getNumberOneCnTotalMoney().add(numberOneMoney));
                }
            }

            //2号
            if (gradeRecord.getNumberTwoMoney() != null) {
                //有分润，
                numberTwoMoney = numberTwoMoney.add(gradeRecord.getNumberTwoMoney());
                if (numberTwo != 0) { //该类型有下注
                    personalRecord.setNumberTwoTotalMoney(personalRecord.getNumberTwoTotalMoney().add(numberTwoMoney));
                } else if (numberTwo == 0) { //该类型没下注
                    personalRecord.setNumberTwoCnTotalMoney(personalRecord.getNumberTwoCnTotalMoney().add(numberTwoMoney));
                }
            }

            //3号
            if (gradeRecord.getNumberThreeMoney() != null) {
                //有分润，
                numberThreeMoney = numberThreeMoney.add(gradeRecord.getNumberThreeMoney());
                if (numberThree != 0) { //该类型有下注
                    personalRecord.setNumberThreeTotalMoney(personalRecord.getNumberThreeTotalMoney().add(numberThreeMoney));
                } else if (numberThree == 0) { //该类型没下注
                    personalRecord.setNumberThreeCnTotalMoney(personalRecord.getNumberThreeCnTotalMoney().add(numberThreeMoney));
                }
            }

            //4号
            if (gradeRecord.getNumberFourMoney() != null) {
                //有分润，
                numberFourMoney = numberFourMoney.add(gradeRecord.getNumberFourMoney());
                if (numberFour != 0) { //该类型有下注
                    personalRecord.setNumberFourTotalMoney(personalRecord.getNumberFourTotalMoney().add(numberFourMoney));
                } else if (numberFour == 0) { //该类型没下注
                    personalRecord.setNumberFourCnTotalMoney(personalRecord.getNumberFourCnTotalMoney().add(numberFourMoney));
                }
            }

            //5号
            if (gradeRecord.getNumberFiveMoney() != null) {
                //有分润，
                numberFiveMoney = numberFiveMoney.add(gradeRecord.getNumberFiveMoney());
                if (numberFive != 0) { //该类型有下注
                    personalRecord.setNumberFiveTotalMoney(personalRecord.getNumberFiveTotalMoney().add(numberFiveMoney));
                } else if (numberFive == 0) { //该类型没下注
                    personalRecord.setNumberFiveCnTotalMoney(personalRecord.getNumberFiveCnTotalMoney().add(numberFiveMoney));
                }
            }

            //6号
            if (gradeRecord.getNumberSixMoney() != null) {
                //有分润，
                numberSixMoney = numberSixMoney.add(gradeRecord.getNumberSixMoney());
                if (numberSix != 0) { //该类型有下注
                    personalRecord.setNumberSixTotalMoney(personalRecord.getNumberSixTotalMoney().add(numberSixMoney));
                } else if (numberSix == 0) { //该类型没下注
                    personalRecord.setNumberSixCnTotalMoney(personalRecord.getNumberSixCnTotalMoney().add(numberSixMoney));
                }
            }

            //7号
            if (gradeRecord.getNumberSevenMoney() != null) {
                //有分润，
                numberSevenMoney = numberSevenMoney.add(gradeRecord.getNumberSevenMoney());
                if (numberSeven != 0) { //该类型有下注
                    personalRecord.setNumberSevenTotalMoney(personalRecord.getNumberSevenTotalMoney().add(numberSevenMoney));
                } else if (numberSeven == 0) { //该类型没下注
                    personalRecord.setNumberSevenCnTotalMoney(personalRecord.getNumberSevenCnTotalMoney().add(numberSevenMoney));
                }
            }

            //计算非可提总额
            BigDecimal cnMoney = personalRecord.getTotalCnMoney();
            cnMoney = cnMoney.add(personalRecord.getNumberOneCnTotalMoney())
                    .add(personalRecord.getNumberTwoCnTotalMoney())
                    .add(personalRecord.getNumberThreeCnTotalMoney())
                    .add(personalRecord.getNumberFourCnTotalMoney())
                    .add(personalRecord.getNumberFiveCnTotalMoney())
                    .add(personalRecord.getNumberSixCnTotalMoney())
                    .add(personalRecord.getNumberSevenCnTotalMoney());
            personalRecord.setTotalCnMoney(cnMoney);

            //计算可提金额总额
            BigDecimal totalMoney = personalRecord.getTotalMoney();
            totalMoney = totalMoney.add(personalRecord.getNumberOneTotalMoney())
                    .add(personalRecord.getNumberTwoTotalMoney())
                    .add(personalRecord.getNumberThreeTotalMoney())
                    .add(personalRecord.getNumberFourTotalMoney())
                    .add(personalRecord.getNumberFiveTotalMoney())
                    .add(personalRecord.getNumberSixTotalMoney())
                    .add(personalRecord.getNumberSevenTotalMoney());
            personalRecord.setTotalMoney(totalMoney);

            personalRecordDao.save(personalRecord);

            return personalRecord;

        }
        else { //记录为空
            personalRecord = new PersonalRecord();
            personalRecord.setId(idWorker.nextId()+"");
            personalRecord.setAgencyId(agencyId);
            personalRecord.setPeriod(period);
            personalRecord.setGrade(grade);
            personalRecord.setCreateTime(date);

            //判断是否有分润以及是否下注
            //1号
            if (gradeRecord.getNumberOneMoney() != null) {
                //有分润，
                numberOneMoney = numberOneMoney.add(gradeRecord.getNumberOneMoney());
                if (numberOne != 0) { //该类型有下注
                    personalRecord.setNumberOneTotalMoney(personalRecord.getNumberOneTotalMoney().add(numberOneMoney));
                } else if (numberOne == 0) { //该类型没下注
                    personalRecord.setNumberOneCnTotalMoney(personalRecord.getNumberOneCnTotalMoney().add(numberOneMoney));
                }
            }

            //2号
            if (gradeRecord.getNumberTwoMoney() != null) {
                //有分润，
                numberTwoMoney = numberTwoMoney.add(gradeRecord.getNumberTwoMoney());
                if (numberTwo != 0) { //该类型有下注
                    personalRecord.setNumberTwoTotalMoney(personalRecord.getNumberTwoTotalMoney().add(numberTwoMoney));
                } else if (numberTwo == 0) { //该类型没下注
                    personalRecord.setNumberTwoCnTotalMoney(personalRecord.getNumberTwoCnTotalMoney().add(numberTwoMoney));
                }
            }

            //3号
            if (gradeRecord.getNumberThreeMoney() != null) {
                //有分润，
                numberThreeMoney = numberThreeMoney.add(gradeRecord.getNumberThreeMoney());
                if (numberThree != 0) { //该类型有下注
                    personalRecord.setNumberThreeTotalMoney(personalRecord.getNumberThreeTotalMoney().add(numberThreeMoney));
                } else if (numberThree == 0) { //该类型没下注
                    personalRecord.setNumberThreeCnTotalMoney(personalRecord.getNumberThreeCnTotalMoney().add(numberThreeMoney));
                }
            }

            //4号
            if (gradeRecord.getNumberFourMoney() != null) {
                //有分润，
                numberFourMoney = numberFourMoney.add(gradeRecord.getNumberFourMoney());
                if (numberFour != 0) { //该类型有下注
                    personalRecord.setNumberFourTotalMoney(personalRecord.getNumberFourTotalMoney().add(numberFourMoney));
                } else if (numberFour == 0) { //该类型没下注
                    personalRecord.setNumberFourCnTotalMoney(personalRecord.getNumberFourCnTotalMoney().add(numberFourMoney));
                }
            }

            //5号
            if (gradeRecord.getNumberFiveMoney() != null) {
                //有分润，
                numberFiveMoney = numberFiveMoney.add(gradeRecord.getNumberFiveMoney());
                if (numberFive != 0) { //该类型有下注
                    personalRecord.setNumberFiveTotalMoney(personalRecord.getNumberFiveTotalMoney().add(numberFiveMoney));
                } else if (numberFive == 0) { //该类型没下注
                    personalRecord.setNumberFiveCnTotalMoney(personalRecord.getNumberFiveCnTotalMoney().add(numberFiveMoney));
                }
            }

            //6号
            if (gradeRecord.getNumberSixMoney() != null) {
                //有分润，
                numberSixMoney = numberSixMoney.add(gradeRecord.getNumberSixMoney());
                if (numberSix != 0) { //该类型有下注
                    personalRecord.setNumberSixTotalMoney(personalRecord.getNumberSixTotalMoney().add(numberSixMoney));
                } else if (numberSix == 0) { //该类型没下注
                    personalRecord.setNumberSixCnTotalMoney(personalRecord.getNumberSixCnTotalMoney().add(numberSixMoney));
                }
            }

            //7号
            if (gradeRecord.getNumberSevenMoney() != null) {
                //有分润，
                numberSevenMoney = numberSevenMoney.add(gradeRecord.getNumberSevenMoney());
                if (numberSeven != 0) { //该类型有下注
                    personalRecord.setNumberSevenTotalMoney(personalRecord.getNumberSevenTotalMoney().add(numberSevenMoney));
                } else if (numberSeven == 0) { //该类型没下注
                    personalRecord.setNumberSevenCnTotalMoney(personalRecord.getNumberSevenCnTotalMoney().add(numberSevenMoney));
                }
            }

            //计算非可提总额
            BigDecimal cnMoney = new BigDecimal("0");
            cnMoney = cnMoney.add(personalRecord.getNumberOneCnTotalMoney())
                    .add(personalRecord.getNumberTwoCnTotalMoney())
                    .add(personalRecord.getNumberThreeCnTotalMoney())
                    .add(personalRecord.getNumberFourCnTotalMoney())
                    .add(personalRecord.getNumberFiveCnTotalMoney())
                    .add(personalRecord.getNumberSixCnTotalMoney())
                    .add(personalRecord.getNumberSevenCnTotalMoney());
            personalRecord.setTotalCnMoney(cnMoney);

            //计算可提金额总额
            BigDecimal totalMoney = new BigDecimal("0");
            totalMoney = totalMoney.add(personalRecord.getNumberOneTotalMoney())
                    .add(personalRecord.getNumberTwoTotalMoney())
                    .add(personalRecord.getNumberThreeTotalMoney())
                    .add(personalRecord.getNumberFourTotalMoney())
                    .add(personalRecord.getNumberFiveTotalMoney())
                    .add(personalRecord.getNumberSixTotalMoney())
                    .add(personalRecord.getNumberSevenTotalMoney());
            personalRecord.setTotalMoney(totalMoney);

            personalRecordDao.save(personalRecord);

            return personalRecord;
        }
    }
}

