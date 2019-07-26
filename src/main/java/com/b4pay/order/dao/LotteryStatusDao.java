package com.b4pay.order.dao;

import com.b4pay.order.entity.LotteryResult;
import com.b4pay.order.entity.LotteryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * @ClassName LotteryStatusDao
 * @Description 期数状态确认
 * @Version 2.1
 **/


public interface LotteryStatusDao extends JpaRepository<LotteryStatus,Integer>, JpaSpecificationExecutor<LotteryStatus> {

    @Query(nativeQuery=true,value = "select * from dst_lottery_status ORDER BY period desc limit 1")
    LotteryStatus findLattestLottery();
}
