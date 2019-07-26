package com.b4pay.order.schedul;

import com.b4pay.order.dao.LotteryStatusDao;
import com.b4pay.order.entity.Lottery;
import com.b4pay.order.entity.LotteryStatus;
import com.b4pay.order.service.LotteryService;
import com.b4pay.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName BonusPoolSchedul
 * @Description
 * @Version 2.1
 **/
@Component
public class BonusPoolScheduler {

    private static final Logger logger = LoggerFactory.getLogger(BonusPoolScheduler.class);

    @Autowired
    private LotteryService lotteryService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private LotteryStatusDao lotteryStatusDao;

    @Autowired
    private SimpleDateFormat simpleDateFormat;

    @Scheduled(cron = "0 0 * * * ? ")
    public void UpdateBonusPool() {
        LotteryStatus lotteryStatus = lotteryStatusDao.findLattestLottery();
        //获得上期开奖期数
        Integer period = null;
        String status = lotteryStatus.getStatus();
        if ("open".equals(status)){
            period = lotteryStatus.getPeriod();
            orderService.countMoney(period);
        }else if ("close".equals(status)){
            period = lotteryStatus.getPeriod() + 1;
            orderService.countMoney(period);
        }
        logger.info("当前时间"+ simpleDateFormat.format(new Date())+ " 统计第"+period+"期资金池");
    }
}
