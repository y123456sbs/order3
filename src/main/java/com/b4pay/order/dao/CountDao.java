package com.b4pay.order.dao;

import com.b4pay.order.entity.Count;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CountDao extends JpaRepository<Count,Integer>, JpaSpecificationExecutor<Count> {

    @Query(value = "from Count where period = ?1")
    public Count queryByPeriod(Integer period);
}
