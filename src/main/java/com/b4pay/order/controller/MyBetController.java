package com.b4pay.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.b4pay.order.entity.Lottery;
import com.b4pay.order.entity.Order;
import com.b4pay.order.entity.Result;
import com.b4pay.order.entity.StatusCode;
import com.b4pay.order.service.LotteryService;
import com.b4pay.order.service.OrderService;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName MyBetController
 * @Description
 * @Version 2.1
 **/

@RestController
@RequestMapping("/mybet")
@CrossOrigin
public class MyBetController {

    private static final Logger logger = LoggerFactory.getLogger(LotteryController.class);


    @Autowired
    private LotteryService lotteryService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SimpleDateFormat simpleDateFormat;


    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public Result lottery(@RequestBody String agencyId) {
        try {
            //查找最新期数
            JSONObject jsonObject = JSONObject.parseObject(agencyId);
            String id = jsonObject.getString("agencyId");
            Lottery lottery = lotteryService.findLattestLottery();
            Integer period = lottery.getPeriod();
            logger.info("查询最新期数第"+lottery.getPeriod()+"期");

            //查询个人下注
            List<Order> orderList = orderService.findByAgencyId(id,period);
            for (Order order : orderList) {
                Date createTime = order.getCreateTime();
                long time = createTime.getTime()/1000;
                //String format = simpleDateFormat.format(createTime);
                order.setCreateTime(new Date(time));
            }
            logger.info("查询个人下注");

            Map<Object,Object> dataMap = new HashMap<>();
            dataMap.put("latestLottery",lottery);
            dataMap.put("orderList",orderList);

            return new Result(true, StatusCode.OK,"查询我的下注成功",dataMap);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new Result(false, StatusCode.ERROR,"查询我的下注失败");
        }
    }
}
