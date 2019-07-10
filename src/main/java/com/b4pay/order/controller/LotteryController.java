package com.b4pay.order.controller;

import com.b4pay.order.entity.Lottery;
import com.b4pay.order.entity.Order;
import com.b4pay.order.entity.Result;
import com.b4pay.order.entity.StatusCode;
import com.b4pay.order.service.LotteryService;
import com.b4pay.order.service.OrderService;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName LotteryController
 * @Version 2.1
 **/

@RestController
@RequestMapping("/lottery")
@CrossOrigin
public class LotteryController {

    private static final Logger logger = LoggerFactory.getLogger(LotteryController.class);



    @Autowired
    private LotteryService lotteryService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SimpleDateFormat simpleDateFormat;

    //开奖
    @RequestMapping(value = "/open", method = RequestMethod.GET)
    public Result lottery(Integer period) {
        try {
            Lottery lottery = lotteryService.queryByPeriod(period);
            Date date = new Date();
            if (lottery != null) {
                logger.info(simpleDateFormat.format(new Date())+"  第"+period+"期已开奖");
                return new Result(false, StatusCode.ERROR, "第"+period+"期已开奖");
            }
            // 生成 7 个不重复的随机数set 用来保存这些随机数
            RandomUtils randomUtils = new RandomUtils();
            Set<Integer> numsSet = new HashSet<>();
            while (numsSet.size() < 7) {
                numsSet.add(randomUtils.nextInt(1, 49));
            }
            List<Integer> numsList = new ArrayList<>(numsSet);
            //保存
            lotteryService.save(period, numsList,date);

            //匹配
            List<Order> orderList = orderService.findAll(null, period);
            lotteryService.match(period, numsList, orderList,date);
            logger.info(simpleDateFormat.format(new Date())+"  第"+period+"期开奖成功");
            return new Result(true, StatusCode.OK, "第"+period+"期开奖成功");
        } catch (Exception e) {
            logger.error(simpleDateFormat.format(new Date())+"  第"+period+"期开奖失败"+e.getMessage());
            return new Result(false, StatusCode.ERROR, "第"+period+"期开奖失败");
        }




    }
}
