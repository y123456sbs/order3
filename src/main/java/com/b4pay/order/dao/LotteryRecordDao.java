package com.b4pay.order.dao;

import com.b4pay.order.entity.Lottery;
import com.b4pay.order.entity.LotteryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;


public interface LotteryRecordDao extends JpaRepository<LotteryRecord,String>, JpaSpecificationExecutor<LotteryRecord> {

    List<LotteryRecord> findByAgencyIdAndPeriod(String agencyId, Integer period);

    List<LotteryRecord> findByAgencyIdAndPeriodAndType(String agencyId, Integer period, String type);


    @Query(nativeQuery=true,
            value = "SELECT agency_id agencyId,period," +
                    "sum( number_one_money ) numberOneMoney," +
                    "sum( number_two_money ) numberTwoMoney," +
                    "sum( number_three_money ) numberThreeMoney," +
                    "sum( number_four_money ) numberFourMoney," +
                    "sum( number_five_money ) numberFiveMoney," +
                    "sum( number_six_money ) numberSixMoney," +
                    "sum( number_seven_money ) numberSevenMoney," +
                    "sum( total_money ) totalMoney " +
                    "FROM dst_lottery_record " +
                    "WHERE period = ?1 AND agency_id = ?2")
    Map findByPeriodAndAgencyId(Integer period, String agencyId);


    //Map<Object,Object>
}
