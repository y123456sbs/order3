package com.b4pay.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.b4pay.order.entity.Lottery;
import com.b4pay.order.entity.Result;
import com.b4pay.order.entity.StatusCode;
import com.b4pay.order.service.LotteryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName HistoryController
 * @Description  历史开奖
 * @Version 2.1
 **/

@RestController
@RequestMapping("/history")
@CrossOrigin
public class HistoryLotteryController {


    private static final Logger logger = LoggerFactory.getLogger(LotteryController.class);


    @Autowired
    private SimpleDateFormat simpleDateFormat;

    @Autowired
    private LotteryService lotteryService;

    @RequestMapping(value = "/lottery", method = RequestMethod.GET)
    public Result lottery() {
        try {
            List<Lottery> allOrderByPeriod = lotteryService.findAllOrderByPeriod();
            logger.info("查询所有历史开奖");
            return new Result(true, StatusCode.OK,"查询所有开奖成功",allOrderByPeriod);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new Result(false, StatusCode.ERROR,"查询所有开奖失败");
        }
    }


    /**
     * @ClassName
     * @Description  日期查询
     * @Version 2.1
     **/
    @RequestMapping(value = "/findLottery", method = RequestMethod.POST)
    public Result findLottery(@RequestBody String jsondata) throws ParseException {
        Map<String,String> map = JSONObject.parseObject(jsondata, Map.class);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(map.get("startDate"));
        Date endDate = sdf.parse(map.get("endDate"));
        try {
            if (startDate == null){
                return new Result(true,StatusCode.ZERO,"查询日期不能为空");
            }
            List<Lottery> allOrderByPeriod = lotteryService.findByDate(startDate,endDate);
            /*for (Lottery lottery : allOrderByPeriod) {
                System.out.println(lottery);
            }*/
            //System.out.println(jsondata);
            return new Result(true,StatusCode.OK,"查询成功",allOrderByPeriod);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new Result(false, StatusCode.ERROR,"查询失败");
        }
    }



}
