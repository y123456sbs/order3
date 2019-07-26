package com.b4pay.order.controller;

import com.b4pay.order.entity.Result;
import com.b4pay.order.entity.StatusCode;
import com.b4pay.order.schedul.BonusPoolScheduler;
import com.b4pay.order.service.WealthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @ClassName WealthController
 * @Description
 * @Version 2.1
 **/

@RestController
@RequestMapping("/wealth")
@CrossOrigin // 解决前端跨域访问后端问题
public class WealthController {
    private static final Logger logger = LoggerFactory.getLogger(WealthController.class);

    @Autowired
    private WealthService wealthService;

    @Autowired
    private SimpleDateFormat simpleDateFormat;


    @RequestMapping(value = "/myinfo", method = RequestMethod.POST)
    public Result myinfo(@RequestBody Map map) {
        try {
            Integer period = (Integer) map.get("period");
            String agencyId = (String) map.get("agencyId");
            //Integer periodNum = Integer.valueOf(period);
            Map myInfoMap = wealthService.findMyInfo(period, agencyId);
            logger.info(simpleDateFormat.format(new Date())+"查询我的中奖明细失败");
            return new Result(true, StatusCode.OK, "查询我的中奖明细成功", myInfoMap);
        } catch (Exception e) {
            logger.error(simpleDateFormat.format(new Date())+"查询我的中奖明细失败"+e.getMessage());
            return new Result(false, StatusCode.ERROR, "查询我的中奖明细失败");
        }
    }

    //查找下级数据
    @RequestMapping(value = "/grade", method = RequestMethod.POST)
    public Result gradeOne(@RequestBody Map map) {
        Integer period = (Integer) map.get("period");
        String agencyId = (String) map.get("agencyId");
        String grade = (String) map.get("grade");
        try {
            Map myInfoMap = wealthService.findGradeInfo(period, agencyId, grade);
            logger.info(simpleDateFormat.format(new Date())+"查询" + agencyId+"第"+grade + "明细成功");
            return new Result(true, StatusCode.OK, "查询我的" + grade + "明细成功", myInfoMap);
        } catch (Exception e) {
            logger.error(simpleDateFormat.format(new Date())+"查询" + agencyId+"第"+grade + "明细失败"+e.getMessage());
            return new Result(false, StatusCode.ERROR, "查询我的" + grade + "明细失败");
        }
    }

    //查找下注资金池数据
    @RequestMapping(value = "/capitalPool", method = RequestMethod.POST)
    public Result getCapitalPool(@RequestBody Map map) {
        String type = (String) map.get("type");
        Integer period = (Integer) map.get("period");
        String number = (String) map.get("number");
        try {
            Map<String, Object> maps = wealthService.findNumberTotalData(type, period, number);
            logger.info(simpleDateFormat.format(new Date())+"查询第"+period+"期" + type + number + "成功");
            return new Result(true, StatusCode.OK, "查询第"+period+"期" + type + number + "成功",maps);
        } catch (Exception e) {
            logger.error(simpleDateFormat.format(new Date())+"查询第"+period+"期" + type + number +"失败"+e.getMessage());
            return new Result(false, StatusCode.ERROR, "查询第"+period+"期" + type + number +"失败");
        }
    }

}