package com.b4pay.order.dao;

import com.b4pay.order.entity.Lottery;
import com.b4pay.order.entity.LotteryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;



public interface LotteryRecordDao extends JpaRepository<LotteryRecord,Integer>, JpaSpecificationExecutor<LotteryRecord> {

}
