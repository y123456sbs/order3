package com.b4pay.order.service;

import com.b4pay.order.dao.*;
import com.b4pay.order.entity.Count;
import com.b4pay.order.entity.Lottery;
import com.b4pay.order.entity.PersonalCount;
import com.b4pay.order.entity.PersonalRecord;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @ClassName WealthService
 * @Description
 * @Version 2.1
 **/

@Service
@Transactional(rollbackFor = Exception.class)
public class WealthService {

    private static final Logger logger = LoggerFactory.getLogger(WealthService.class);


    @Autowired
    private PersonalRecordDao personalRecordDao;

    @Autowired
    private PersonalCountDao personalCountDao;

    @Autowired
    private LotteryRecordDao lotteryRecordDao;

    @Autowired
    private GradeRecordDao gradeRecordDao;

    @Autowired
    private LotteryDao lotteryDao;

    @Autowired
    private CountDao countDao;


    public Map findMyInfo(Integer period, String agencyId) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<>();
        //查找累积个人总金额
        PersonalCount personalCount = personalCountDao.findById(agencyId).get();

        BigDecimal countMoney = personalCount.getCountMoney();//个人金额总额

        //查找当期个人中奖
        Map lotteryRecordMap = lotteryRecordDao.findByPeriodAndAgencyId(period, agencyId);

        //查找当期下级分润
        Map gradeRecordMap = gradeRecordDao.findByPeriodAndAgencyId(period, agencyId);

        //查找当期中奖号码
        Lottery lottery = lotteryDao.queryByPeriod(period);

        //中奖号码
        Integer numberOne = lottery.getNumberOne();
        Integer numberTwo = lottery.getNumberTwo();
        Integer numberThree = lottery.getNumberThree();
        Integer numberFour = lottery.getNumberFour();
        Integer numberFive = lottery.getNumberFive();
        Integer numberSix = lottery.getNumberSix();
        Integer numberSeven = lottery.getNumberSeven();
        Date time = lottery.getCreateTime();


        //个人中奖金额
        BigDecimal numberOneMoney = (BigDecimal) lotteryRecordMap.get("numberOneMoney");
        if (numberOneMoney == null) {
            numberOneMoney = new BigDecimal("0");
        }

        BigDecimal numberTwoMoney = (BigDecimal) lotteryRecordMap.get("numberTwoMoney");
        if (numberTwoMoney == null) {
            numberTwoMoney = new BigDecimal("0");
        }

        BigDecimal numberThreeMoney = (BigDecimal) lotteryRecordMap.get("numberThreeMoney");
        if (numberThreeMoney == null) {
            numberThreeMoney = new BigDecimal("0");
        }

        BigDecimal numberFourMoney = (BigDecimal) lotteryRecordMap.get("numberFourMoney");
        if (numberFourMoney == null) {
            numberFourMoney = new BigDecimal("0");
        }

        BigDecimal numberFiveMoney = (BigDecimal) lotteryRecordMap.get("numberFiveMoney");
        if (numberFiveMoney == null) {
            numberFiveMoney = new BigDecimal("0");
        }

        BigDecimal numberSixMoney = (BigDecimal) lotteryRecordMap.get("numberSixMoney");
        if (numberSixMoney == null) {
            numberSixMoney = new BigDecimal("0");
        }

        BigDecimal numberSevenMoney = (BigDecimal) lotteryRecordMap.get("numberSevenMoney");
        if (numberSevenMoney == null) {
            numberSevenMoney = new BigDecimal("0");
        }

        BigDecimal totalMoney = (BigDecimal) lotteryRecordMap.get("totalMoney");
        if (totalMoney == null) {
            totalMoney = new BigDecimal("0");
        }

        //分润数据
        BigDecimal gradenumberOneMoney = (BigDecimal) gradeRecordMap.get("numberOneMoney");
        if (gradenumberOneMoney == null) {
            gradenumberOneMoney = new BigDecimal("0");
        }

        BigDecimal gradenumberTwoMoney = (BigDecimal) gradeRecordMap.get("numberTwoMoney");
        if (gradenumberTwoMoney == null) {
            gradenumberTwoMoney = new BigDecimal("0");
        }

        BigDecimal gradenumberThreeMoney = (BigDecimal) gradeRecordMap.get("numberThreeMoney");
        if (gradenumberThreeMoney == null) {
            gradenumberThreeMoney = new BigDecimal("0");
        }

        BigDecimal gradenumberFourMoney = (BigDecimal) gradeRecordMap.get("numberFourMoney");
        if (gradenumberFourMoney == null) {
            gradenumberFourMoney = new BigDecimal("0");
        }

        BigDecimal gradenumberFiveMoney = (BigDecimal) gradeRecordMap.get("numberFiveMoney");
        if (gradenumberFiveMoney == null) {
            gradenumberFiveMoney = new BigDecimal("0");
        }

        BigDecimal gradenumberSixMoney = (BigDecimal) gradeRecordMap.get("numberSixMoney");
        if (gradenumberSixMoney == null) {
            gradenumberSixMoney = new BigDecimal("0");
        }

        BigDecimal gradenumberSevenMoney = (BigDecimal) gradeRecordMap.get("numberSevenMoney");
        if (gradenumberSevenMoney == null) {
            gradenumberSevenMoney = new BigDecimal("0");
        }

        BigDecimal gradetotalMoney = (BigDecimal) gradeRecordMap.get("totalMoney");
        if (gradetotalMoney == null) {
            gradetotalMoney = new BigDecimal("0");
        }

        BigDecimal numberOneTotalMoney = numberOneMoney.add(gradenumberOneMoney);
        BigDecimal numberTwoTotalMoney = numberTwoMoney.add(gradenumberTwoMoney);
        BigDecimal numberThreeTotalMoney = numberThreeMoney.add(gradenumberThreeMoney);
        BigDecimal numberFourTotalMoney = numberFourMoney.add(gradenumberFourMoney);
        BigDecimal numberFiveTotalMoney = numberFiveMoney.add(gradenumberFiveMoney);
        BigDecimal numberSixTotalMoney = numberSixMoney.add(gradenumberSixMoney);
        BigDecimal numberSevenTotalMoney = numberSevenMoney.add(gradenumberSevenMoney);
        BigDecimal totalMoneys = totalMoney.add(gradetotalMoney);


        resultMap.put("countMoney", countMoney);
        resultMap.put("period", period);
        resultMap.put("agencyId", agencyId);

        resultMap.put("numberOne", numberOne);
        resultMap.put("numberTwo", numberTwo);
        resultMap.put("numberThree", numberThree);
        resultMap.put("numberFour", numberFour);
        resultMap.put("numberFive", numberFive);
        resultMap.put("numberSix", numberSix);
        resultMap.put("numberSeven", numberSeven);

        resultMap.put("numberOneTotalMoney", numberOneTotalMoney);
        resultMap.put("numberTwoTotalMoney", numberTwoTotalMoney);
        resultMap.put("numberThreeTotalMoney", numberThreeTotalMoney);
        resultMap.put("numberFourTotalMoney", numberFourTotalMoney);
        resultMap.put("numberFiveTotalMoney", numberFiveTotalMoney);
        resultMap.put("numberSixTotalMoney", numberSixTotalMoney);
        resultMap.put("numberSevenTotalMoney", numberSevenTotalMoney);

        resultMap.put("totalMoneys", totalMoneys);
        resultMap.put("time", time);


        return resultMap;


    }

    //查找下级数据
    public Map findGradeInfo(Integer period, String agencyId, String grade) {
        HashMap<String, Object> resultMap = new HashMap<>();

        // 当期可提一二三数据
        PersonalRecord personalRecord = personalRecordDao.findByGradeAndPeriodAndAgencyId(grade, period, agencyId);
        if (personalRecord == null) {//该下级没有分润
            return null;
        }

        Lottery lottery = lotteryDao.queryByPeriod(period);

        //中奖号码
        Integer numberOne = lottery.getNumberOne();
        Integer numberTwo = lottery.getNumberTwo();
        Integer numberThree = lottery.getNumberThree();
        Integer numberFour = lottery.getNumberFour();
        Integer numberFive = lottery.getNumberFive();
        Integer numberSix = lottery.getNumberSix();
        Integer numberSeven = lottery.getNumberSeven();
        Date time = lottery.getCreateTime();

        resultMap.put("period", period);
        resultMap.put("agencyId", agencyId);

        resultMap.put("numberOne", numberOne);
        resultMap.put("numberTwo", numberTwo);
        resultMap.put("numberThree", numberThree);
        resultMap.put("numberFour", numberFour);
        resultMap.put("numberFive", numberFive);
        resultMap.put("numberSix", numberSix);
        resultMap.put("numberSeven", numberSeven);
        resultMap.put("time", time);


        BigDecimal totalCnMoney = personalRecord.getTotalCnMoney();//当期不可提总金额
        BigDecimal totalMoney = personalRecord.getTotalMoney();//当期可提总金额

        //当期可提数字金额
        BigDecimal numberOneTotalMoney = personalRecord.getNumberOneTotalMoney();
        BigDecimal numberTwoTotalMoney = personalRecord.getNumberTwoTotalMoney();
        BigDecimal numberThreeTotalMoney = personalRecord.getNumberThreeTotalMoney();
        BigDecimal numberFourTotalMoney = personalRecord.getNumberFourTotalMoney();
        BigDecimal numberFiveTotalMoney = personalRecord.getNumberFiveTotalMoney();
        BigDecimal numberSixTotalMoney = personalRecord.getNumberSixTotalMoney();
        BigDecimal numberSevenTotalMoney = personalRecord.getNumberSevenTotalMoney();

        //当期不可提数字金额
        BigDecimal numberOneCnTotalMoney = personalRecord.getNumberOneCnTotalMoney();
        BigDecimal numberTwoCnTotalMoney = personalRecord.getNumberTwoCnTotalMoney();
        BigDecimal numberThreeCnTotalMoney = personalRecord.getNumberThreeCnTotalMoney();
        BigDecimal numberFourCnTotalMoney = personalRecord.getNumberFourCnTotalMoney();
        BigDecimal numberFiveCnTotalMoney = personalRecord.getNumberFiveCnTotalMoney();
        BigDecimal numberSixCnTotalMoney = personalRecord.getNumberSixCnTotalMoney();
        BigDecimal numberSevenCnTotalMoney = personalRecord.getNumberSevenCnTotalMoney();

        //统计该级可提与不可提金额历史总额
        Map sumCnAndTotal = personalRecordDao.findSumCnAndTotal(agencyId, grade);
        BigDecimal totalCnCountMoney = (BigDecimal) sumCnAndTotal.get("totalCnMoney");
        BigDecimal totalCountMoney = (BigDecimal) sumCnAndTotal.get("totalMoney");

        resultMap.put("numberOneTotalMoney", numberOneTotalMoney);
        resultMap.put("numberTwoTotalMoney", numberTwoTotalMoney);
        resultMap.put("numberThreeTotalMoney", numberThreeTotalMoney);
        resultMap.put("numberFourTotalMoney", numberFourTotalMoney);
        resultMap.put("numberFiveTotalMoney", numberFiveTotalMoney);
        resultMap.put("numberSixTotalMoney", numberSixTotalMoney);
        resultMap.put("numberSevenTotalMoney", numberSevenTotalMoney);

        resultMap.put("numberOneCnTotalMoney", numberOneCnTotalMoney);
        resultMap.put("numberTwoCnTotalMoney", numberTwoCnTotalMoney);
        resultMap.put("numberThreeCnTotalMoney", numberThreeCnTotalMoney);
        resultMap.put("numberFourCnTotalMoney", numberFourCnTotalMoney);
        resultMap.put("numberFiveCnTotalMoney", numberFiveCnTotalMoney);
        resultMap.put("numberSixCnTotalMoney", numberSixCnTotalMoney);
        resultMap.put("numberSevenCnTotalMoney", numberSevenCnTotalMoney);

        resultMap.put("totalCnMoney", totalCnMoney);
        resultMap.put("totalMoney", totalMoney);

        resultMap.put("totalCnCountMoney", totalCnCountMoney);
        resultMap.put("totalCountMoney", totalCountMoney);

        return resultMap;
    }

    //查找奖池数据
    public Map<String, Object> findNumberTotalData(String type, Integer period, String number) throws Exception{
        Count count = countDao.findByPeriodAndType(period, type);
        if (count == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();



        //获得资金池总额
        BigDecimal numberTotalMoney = this.getNumber(count, number);
        map.put("numberTotalMoney", numberTotalMoney);

        if ("number".equals(type)) {
            StringBuilder st = new StringBuilder();

            map.put("01", RandomUtils.nextInt(1,8) + "%");
            map.put("02", RandomUtils.nextInt(1,8) + "%");
            map.put("03", RandomUtils.nextInt(1,8) + "%");
            map.put("04", RandomUtils.nextInt(1,8) + "%");
            map.put("05", RandomUtils.nextInt(1,8) + "%");
            map.put("06", RandomUtils.nextInt(1,8) + "%");
            map.put("07", RandomUtils.nextInt(1,8) + "%");
            map.put("08", RandomUtils.nextInt(1,8) + "%");
            map.put("09", RandomUtils.nextInt(1,8) + "%");
            map.put("10", RandomUtils.nextInt(1,8) + "%");
            map.put("11", RandomUtils.nextInt(1,8) + "%");
            map.put("12", RandomUtils.nextInt(1,8) + "%");
            map.put("13", RandomUtils.nextInt(1,8) + "%");
            map.put("14", RandomUtils.nextInt(1,8) + "%");
            map.put("15", RandomUtils.nextInt(1,8) + "%");
            map.put("16", RandomUtils.nextInt(1,8) + "%");
            map.put("17", RandomUtils.nextInt(1,8) + "%");
            map.put("18", RandomUtils.nextInt(1,8) + "%");
            map.put("19", RandomUtils.nextInt(1,8) + "%");
            map.put("20", RandomUtils.nextInt(1,8) + "%");
            map.put("21", RandomUtils.nextInt(1,8) + "%");
            map.put("22", RandomUtils.nextInt(1,8) + "%");
            map.put("23", RandomUtils.nextInt(1,8) + "%");
            map.put("24", RandomUtils.nextInt(1,8) + "%");
            map.put("25", RandomUtils.nextInt(1,8) + "%");
            map.put("26", RandomUtils.nextInt(1,8) + "%");
            map.put("27", RandomUtils.nextInt(1,8) + "%");
            map.put("28", RandomUtils.nextInt(1,8) + "%");
            map.put("29", RandomUtils.nextInt(1,8) + "%");
            map.put("30", RandomUtils.nextInt(1,8) + "%");
            map.put("31", RandomUtils.nextInt(1,8) + "%");
            map.put("32", RandomUtils.nextInt(1,8) + "%");
            map.put("33", RandomUtils.nextInt(1,8) + "%");
            map.put("34", RandomUtils.nextInt(1,8) + "%");
            map.put("35", RandomUtils.nextInt(1,8) + "%");
            map.put("36", RandomUtils.nextInt(1,8) + "%");
            map.put("37", RandomUtils.nextInt(1,8) + "%");
            map.put("38", RandomUtils.nextInt(1,8) + "%");
            map.put("39", RandomUtils.nextInt(1,8) + "%");
            map.put("40", RandomUtils.nextInt(1,8) + "%");
            map.put("41", RandomUtils.nextInt(1,8) + "%");
            map.put("42", RandomUtils.nextInt(1,8) + "%");
            map.put("43", RandomUtils.nextInt(1,8) + "%");
            map.put("44", RandomUtils.nextInt(1,8) + "%");
            map.put("45", RandomUtils.nextInt(1,8) + "%");
            map.put("46", RandomUtils.nextInt(1,8) + "%");
            map.put("47", RandomUtils.nextInt(1,8) + "%");
            map.put("48", RandomUtils.nextInt(1,8) + "%");
            map.put("49", RandomUtils.nextInt(1,8) + "%");

        } else if ("zodiac".equals(type)) {
            map.put("鼠", RandomUtils.nextInt(8, 15) + "%");
            map.put("牛", RandomUtils.nextInt(8, 15) + "%");
            map.put("虎", RandomUtils.nextInt(8, 15) + "%");
            map.put("兔", RandomUtils.nextInt(8, 15) + "%");
            map.put("龙", RandomUtils.nextInt(8, 15) + "%");
            map.put("蛇", RandomUtils.nextInt(8, 15) + "%");
            map.put("马", RandomUtils.nextInt(8, 15) + "%");
            map.put("羊", RandomUtils.nextInt(8, 15) + "%");
            map.put("猴", RandomUtils.nextInt(8, 15) + "%");
            map.put("鸡", RandomUtils.nextInt(8, 15) + "%");
            map.put("狗", RandomUtils.nextInt(8, 15) + "%");
            map.put("猪", RandomUtils.nextInt(8, 15) + "%");

        } else if ("color".equals(type)) {
            int red = RandomUtils.nextInt(20, 40);
            int blue = RandomUtils.nextInt(20, 40);
            int green = 100 - red - blue;
            map.put("红", red + "%");
            map.put("蓝", blue + "%");
            map.put("绿", green + "%");
        } else if ("size".equals(type)) {
            int big = RandomUtils.nextInt(30, 60);
            int small = 100 - big;
            map.put("大", big + "%");
            map.put("小", small + "%");
        } else if ("sd".equals(type)) {
            int single = RandomUtils.nextInt(30, 60);
            int doubles = 100 - single;
            map.put("单", single + "%");
            map.put("双", doubles + "%");
        }


        return map;
    }

    //反射调用get方法
    private BigDecimal getNumber(Count count, String number) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        Class<? extends Count> countClass = count.getClass();
        PropertyDescriptor pd = new PropertyDescriptor(number, countClass);
        Method readMethod = pd.getReadMethod();
        BigDecimal numberMoney = (BigDecimal) readMethod.invoke(count);
        return numberMoney;
    }


}
