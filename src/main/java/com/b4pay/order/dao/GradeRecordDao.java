package com.b4pay.order.dao;

import com.b4pay.order.entity.GradeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * @ClassName GradeRecordDao
 * @Description
 * @Version 2.1
 **/
public interface GradeRecordDao extends JpaRepository<GradeRecord,String>, JpaSpecificationExecutor<GradeRecord> {

    List<GradeRecord> findByGradeIdAndTypeAndPeriod(String agencyId, String type, Integer period);

    @Query(nativeQuery=true,
            value = "SELECT grade_id,period," +
                    "sum( number_one_money ) numberOneMoney," +
                    "sum( number_two_money ) numberTwoMoney," +
                    "sum( number_three_money ) numberThreeMoney," +
                    "sum( number_four_money ) numberFourMoney," +
                    "sum( number_five_money ) numberFiveMoney," +
                    "sum( number_six_money ) numberSixMoney," +
                    "sum( number_seven_money ) numberSevenMoney," +
                    "sum( total_money ) totalMoney " +
                    "FROM dst_lottery_grade_record " +
                    "where period = ?1 and grade_id = ?2")
    Map findByPeriodAndAgencyId(Integer period,String agencyId);

    @Query(nativeQuery=true,
            value = "SELECT grade_id,period," +
                    "sum( number_one_money ) numberOneMoney," +
                    "sum( number_two_money ) numberTwoMoney," +
                    "sum( number_three_money ) numberThreeMoney," +
                    "sum( number_four_money ) numberFourMoney," +
                    "sum( number_five_money ) numberFiveMoney," +
                    "sum( number_six_money ) numberSixMoney," +
                    "sum( number_seven_money ) numberSevenMoney," +
                    "sum( total_money ) totalMoney " +
                    "FROM dst_lottery_grade_record " +
                    "where period = ?1 and grade_id = ?2 and SUBSTRING(order_time,1,10) = ?3")
    Map findByPeriodAndAgencyIdAndDate(Integer period,String agencyId,String date);


    @Query(nativeQuery=true,
            value = "SELECT *" +
                    "FROM dst_lottery_grade_record " +
                    "where grade_id = ?1 and type = ?2 and period = ?3 and SUBSTRING(order_time,1,10) = ?4")
    List<GradeRecord> findByGradeIdAndTypeAndPeriodAndOrderTime(String agencyId, String type, Integer period,String dateTime);

}
