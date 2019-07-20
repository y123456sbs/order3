package com.b4pay.order.dao;

import com.b4pay.order.entity.GradeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @ClassName GradeRecordDao
 * @Description
 * @Version 2.1
 **/
public interface GradeRecordDao extends JpaRepository<GradeRecord,String>, JpaSpecificationExecutor<GradeRecord> {

    List<GradeRecord> findByGradeIdAndTypeAndPeriod(String agencyId, String type, Integer period);
}
