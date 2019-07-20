package com.b4pay.order.dao;

import com.b4pay.order.entity.Lottery;
import com.b4pay.order.entity.LotteryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface LotteryRecordDao extends JpaRepository<LotteryRecord,String>, JpaSpecificationExecutor<LotteryRecord> {

    List<LotteryRecord> findByAgencyIdAndPeriod(String agencyId, Integer period);

    LotteryRecord findByAgencyIdAndPeriodAndType(String agencyId, Integer period, String type);
}
