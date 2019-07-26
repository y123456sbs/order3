package com.b4pay.order.dao;

import com.b4pay.order.entity.PersonalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

public interface PersonalRecordDao extends JpaRepository<PersonalRecord,String>, JpaSpecificationExecutor<PersonalRecord> {

    PersonalRecord findByGradeAndPeriodAndAgencyId(String gradeName, Integer period, String agencyId);

    @Query(nativeQuery=true,
            value = "SELECT agency_id agencyId," +
                    "sum(total_cn_money) totalCnMoney, " +
                    "sum(total_money) totalMoney " +
                    "FROM dst_personal_record " +
                    "where agency_id = ?1 and grade = ?2")
    Map findSumCnAndTotal(String agencyId, String grade);
}

