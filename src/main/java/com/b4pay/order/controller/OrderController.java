package com.b4pay.order.controller;

import com.b4pay.order.entity.Order;
import com.b4pay.order.entity.Result;
import com.b4pay.order.entity.StatusCode;
import com.b4pay.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @ClassName OrderController
 * @Description 下单
 * @Version 2.1
 **/

@RestController
@RequestMapping("/order")
@CrossOrigin // 解决前端跨域访问后端问题
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);


    @Autowired
    private OrderService orderService;

    @Autowired
    private SimpleDateFormat simpleDateFormat;

    //下单
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestBody Order order) {
        try {
            String agencyId = order.getAgencyId();
            Integer period = order.getPeriod();
            Date date = new Date();
            boolean findOrder = orderService.findAll(agencyId, period,date);
            if (findOrder){
                orderService.add(order,date);
                logger.info(simpleDateFormat.format(date) + " " + agencyId + "下单成功");
                return new Result(true, StatusCode.OK, "下单成功");
            } else {
                return new Result(true, StatusCode.ERROR,"您当天已经下注,请勿重复下注");
            }
        } catch (Exception e) {
            return new Result(false, StatusCode.ERROR, e.getMessage());
        }
    }


    //停止下注
    @RequestMapping(value = "/stopAdd", method = RequestMethod.GET)
    public Result stopAdd(Integer period) {
        try {
            orderService.countMoney(period);
            logger.info(simpleDateFormat.format(new Date()) + "停止下注成功");
            return new Result(true, StatusCode.OK, "停止下注成功");
        } catch (Exception e) {
            logger.error(simpleDateFormat.format(new Date()) + "停止下注失败" + e.getMessage());
            return new Result(false, StatusCode.ERROR, "停止下注失败");
        }
    }


}
