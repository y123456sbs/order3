package com.b4pay.order.controller;

import com.b4pay.order.entity.LotteryStatus;
import com.b4pay.order.entity.Order;
import com.b4pay.order.entity.Result;
import com.b4pay.order.entity.StatusCode;
import com.b4pay.order.service.OrderService;
import org.apache.commons.lang3.time.DateUtils;
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
        Date date = new Date();
        try {
            String agencyId = order.getAgencyId();
            Integer period = order.getPeriod();
            String type = order.getType();

            LotteryStatus lotteryStatus = orderService.findStatusByPeriod(period);
            if (lotteryStatus == null){
                lotteryStatus = new LotteryStatus();
                lotteryStatus.setPeriod(period);
                lotteryStatus.setStatus("open");
                lotteryStatus.setCreateTime(date);
                lotteryStatus.setUpdateTime(date);
                orderService.saveLotteryStatus(lotteryStatus);
            }

            String status = lotteryStatus.getStatus();
            if ("open".equals(status)){
                //Date date = DateUtils.addHours(new Date(), 24);
                boolean findOrder = orderService.findAll(agencyId, period,type,date);
                if (findOrder){
                    orderService.add(order,date);
                    logger.info(simpleDateFormat.format(date) + " " + agencyId + "下单成功");
                    return new Result(true, StatusCode.OK, "下单成功");
                } else {
                    return new Result(true, StatusCode.REPEAT,"您当天已经下注,请勿重复下注");
                }
            }  else if ("close".equals(status)){
                return new Result(true,StatusCode.ZERO,"当期已停止下注");
            } else {
                return new Result(true, StatusCode.ERROR,"系统修复中");
            }
        } catch (Exception e) {
            logger.error(simpleDateFormat.format(date) + "下单失败"+ e.getMessage());
            return new Result(false, StatusCode.ERROR,"系统修复中");
        }
    }


    //停止下注
    @RequestMapping(value = "/stopAdd", method = RequestMethod.GET)
    public Result stopAdd(Integer period) {
        try {
            orderService.countMoney(period);
            logger.info(simpleDateFormat.format(new Date()) + "停止下注成功");
            //调整期数状态码为close
            orderService.updateLotteryStatus(period);
            logger.info(simpleDateFormat.format(new Date()) + "关闭下注成功");

            return new Result(true, StatusCode.OK, "停止下注成功");
        } catch (Exception e) {
            logger.error(simpleDateFormat.format(new Date()) + "停止下注失败" + e.getMessage());
            return new Result(false, StatusCode.ERROR, "停止下注失败");
        }
    }


}
