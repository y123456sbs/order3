package com.b4pay.order.service;

import com.b4pay.order.dao.CountDao;
import com.b4pay.order.dao.OrderDao;
import com.b4pay.order.entity.Count;
import com.b4pay.order.entity.Order;
import com.b4pay.order.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName OrderService
 * @Description 下单
 * @Version 2.1
 **/

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private CountDao countDao;


    /**
     * 查询所有
     */
    public List<Order> findAll() {
        return orderDao.findAll();
    }

    /**
     * 查询一个
     */
    public Order findById(String id) {
        return orderDao.findById(id).get();
    }

    /**
     * 添加
     */
    @Transactional
    public void add(Order order,Date date) {
        //设置id
        order.setId(idWorker.nextId() + "");

        Integer nums = 0;
        if (order.getNumberOne() != null && !"".equals(order.getNumberOne())) {
            nums++;
        }
        if (order.getNumberTwo() != null && !"".equals(order.getNumberTwo())) {
            nums++;
        }
        if (order.getNumberThree() != null && !"".equals(order.getNumberThree())) {
            nums++;
        }
        if (order.getNumberFour() != null && !"".equals(order.getNumberFour())) {
            nums++;
        }
        if (order.getNumberFive() != null && !"".equals(order.getNumberFive())) {
            nums++;
        }
        if (order.getNumberSix() != null && !"".equals(order.getNumberSix())) {
            nums++;
        }
        if (order.getNumberSeven() != null && !"".equals(order.getNumberSeven())) {
            nums++;
        }
        BigDecimal money = new BigDecimal(nums*10);

        order.setMoney(money);
        order.setCreateTime(date);

        orderDao.save(order);
    }

    /**
     * 修改
     */
    public void update(Order order) {  // 必须包含数据库存在的ID
        orderDao.save(order);
    }

    /**
     * 删除
     */
    @Transactional
    public void delete(String id) {
        orderDao.deleteById(id);
    }


    /**
     * 查找是否当天重复下注
     */
    public Boolean findAll(String agencyId, Integer period,Date date) {
        List<Order> orderList = orderDao.findAll(new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();

                if (agencyId != null) {
                    list.add(cb.equal(root.get("agencyId").as(String.class), agencyId));
                }

                if (period != null) {
                    list.add(cb.equal(root.get("period").as(Integer.class), period));
                }

                Predicate[] p = new Predicate[list.size()];
                return cb.and(list.toArray(p));
            }
        });
        for (Order order : orderList) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String orderDate = simpleDateFormat.format(order.getCreateTime());
            String newOrderDate = simpleDateFormat.format(date);
            if (orderDate.equals(newOrderDate)){
                return false;
            }
        }
        return true;
    }

    /**
     * 根据期数和客户id查找
     */
    public List<Order> findAll(String agencyId, Integer period) {
        List<Order> orderList = orderDao.findAll(new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();

                if (agencyId != null) {
                    list.add(cb.equal(root.get("agencyId").as(String.class), agencyId));
                }

                if (period != null) {
                    list.add(cb.equal(root.get("period").as(Integer.class), period));
                }

                Predicate[] p = new Predicate[list.size()];
                return cb.and(list.toArray(p));
            }
        });

        return orderList;
    }

    /**
     * 统计金额
     */
    @Transactional
    public void countMoney(Integer period) {
        List<Order> orderList = orderDao.findAll(new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();

                if (period != null) {
                    list.add(cb.equal(root.get("period").as(Integer.class), period));
                }

                Predicate[] p = new Predicate[list.size()];
                return cb.and(list.toArray(p));
            }
        });

        //统计期数金额
        BigDecimal countMoney = new BigDecimal(0);
        BigDecimal numberOneMoney = new BigDecimal(0);
        BigDecimal numberTwoMoney = new BigDecimal(0);
        BigDecimal numberThreeMoney = new BigDecimal(0);
        BigDecimal numberFourMoney = new BigDecimal(0);
        BigDecimal numberFiveMoney = new BigDecimal(0);
        BigDecimal numberSixMoney = new BigDecimal(0);
        BigDecimal numberSevenMoney = new BigDecimal(0);

        for (Order order : orderList) {
            BigDecimal money = order.getMoney();
            if (money != null) {
                countMoney = countMoney.add(money);
            }
            if (order.getNumberOne() != null){
                numberOneMoney = numberOneMoney.add(new BigDecimal("10"));
            }
            if (order.getNumberTwo() != null){
                numberTwoMoney = numberTwoMoney.add(new BigDecimal("10"));
            }
            if (order.getNumberThree() != null){
                numberThreeMoney = numberThreeMoney.add(new BigDecimal("10"));
            }
            if (order.getNumberFour() != null){
                numberFourMoney = numberFourMoney.add(new BigDecimal("10"));
            }
            if (order.getNumberFive() != null){
                numberFiveMoney = numberFiveMoney.add(new BigDecimal("10"));
            }
            if (order.getNumberSix() != null){
                numberSixMoney = numberSixMoney.add(new BigDecimal("10"));
            }
            if (order.getNumberSeven() != null){
                numberSevenMoney = numberSevenMoney.add(new BigDecimal("10"));
            }
        }

        Count count = countDao.queryByPeriod(period);
        //存在，更新
        if (count != null) {
            count.setCountMoney(countMoney);
            count.setNumberOneTotalMoney(numberOneMoney);
            count.setNumberTwoTotalMoney(numberTwoMoney);
            count.setNumberThreeTotalMoney(numberThreeMoney);
            count.setNumberFourTotalMoney(numberFourMoney);
            count.setNumberFiveTotalMoney(numberFiveMoney);
            count.setNumberSixTotalMoney(numberSixMoney);
            count.setNumberSevenTotalMoney(numberSevenMoney);
            countDao.save(count);
        } else {
            //不存在，新建
            count = new Count();
            count.setPeriod(period);
            count.setCountMoney(countMoney);
            count.setNumberOneTotalMoney(numberOneMoney);
            count.setNumberTwoTotalMoney(numberTwoMoney);
            count.setNumberThreeTotalMoney(numberThreeMoney);
            count.setNumberFourTotalMoney(numberFourMoney);
            count.setNumberFiveTotalMoney(numberFiveMoney);
            count.setNumberSixTotalMoney(numberSixMoney);
            count.setNumberSevenTotalMoney(numberSevenMoney);
            countDao.save(count);
        }
    }

    public List<Order> findByAgencyId(String agecncyId,Integer period) {
        List<Order> orderList = orderDao.findByAgencyIdGreaterThanOrderByPeriodDesc(agecncyId,period);
        return orderList;
    }
}
