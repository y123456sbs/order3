package com.b4pay.order.dao;

import com.b4pay.order.entity.Lottery;
import com.b4pay.order.entity.LotteryResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface LotteryResultDao extends JpaRepository<LotteryResult,Integer>, JpaSpecificationExecutor<LotteryResult> {

}
