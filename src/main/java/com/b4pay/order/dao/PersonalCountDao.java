package com.b4pay.order.dao;

import com.b4pay.order.entity.PersonalCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PersonalCountDao extends JpaRepository<PersonalCount,String>, JpaSpecificationExecutor<PersonalCount> {


}
