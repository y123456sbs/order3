package com.b4pay.order.dao;

import com.b4pay.order.entity.Count;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CountDao extends JpaRepository<Count,String>, JpaSpecificationExecutor<Count> {

    @Query(nativeQuery=true,value = "select * from dst_lottery ORDER BY period desc limit 1")
    public Count queryByPeriod();

    Count findByPeriodAndType(Integer period, String type);
}
