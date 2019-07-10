package com.b4pay.order.dao;

import com.b4pay.order.entity.Lottery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;


public interface LotteryDao extends JpaRepository<Lottery,Integer>, JpaSpecificationExecutor<Lottery> {

    @Query(value = "from Lottery where period = ?1")
    public Lottery queryByPeriod(Integer period);


    @Query(value = "from Lottery order by period desc ")
    List<Lottery> findAllOrderByPeriodDesc();

    @Query(value = "from Lottery where createTime between ?1 and ?2")
    List<Lottery> findAllBetweenStartAndEnd(Date startDate, Date endDate);

    @Query(nativeQuery=true,value = "select * from dst_lottery ORDER BY period desc limit 1")
    Lottery findLatterLottery();
}
