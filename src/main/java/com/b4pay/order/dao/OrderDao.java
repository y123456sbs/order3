package com.b4pay.order.dao;

import com.b4pay.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @ClassName OrderDao
 * @Description 下注
 * @Version 2.1
 **/
public interface OrderDao extends JpaRepository<Order,String>, JpaSpecificationExecutor<Order> {

    @Query("from Order where agencyId = ?1 and period > ?2 order by period desc")
    List<Order> findByAgencyIdGreaterThanOrderByPeriodDesc(String agencyId,Integer period);

}
