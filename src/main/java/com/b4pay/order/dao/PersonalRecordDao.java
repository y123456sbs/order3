package com.b4pay.order.dao;

import com.b4pay.order.entity.PersonalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PersonalRecordDao extends JpaRepository<PersonalRecord,String>, JpaSpecificationExecutor<PersonalRecord> {

    PersonalRecord findByGradeAndPeriodAndAgencyId(String gradeName, Integer period, String agencyId);
}

