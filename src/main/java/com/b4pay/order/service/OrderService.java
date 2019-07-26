package com.b4pay.order.service;

import com.b4pay.order.dao.CountDao;
import com.b4pay.order.dao.LotteryStatusDao;
import com.b4pay.order.dao.OrderDao;
import com.b4pay.order.entity.Count;
import com.b4pay.order.entity.LotteryStatus;
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
import java.util.NoSuchElementException;

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

    @Autowired
    private LotteryStatusDao lotteryStatusDao;


    private static final String NUMBER = "number";
    private static final String ZODIAC = "zodiac";
    private static final String SIZE = "size";
    private static final String COLOR = "color";
    private static final String SD = "sd";


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

    public LotteryStatus findStatusByPeriod(Integer period) {
        LotteryStatus lotteryStatus = null;
        try {
            lotteryStatus = lotteryStatusDao.findById(period).get();
        } catch (NoSuchElementException e) {
            return null;
        } catch (Exception e){
            throw new RuntimeException();
        }
        return lotteryStatus;
    }

    /**
     * 添加
     */
    @Transactional
    public void add(Order order, Date date) {
        //设置id
        order.setId(idWorker.nextId() + "");

        Integer nums = 0;
        if (order.getNumberOne() != null && order.getNumberOne() != 0 && !"".equals(order.getNumberOne())) {
            nums++;
        }
        if (order.getNumberTwo() != null && order.getNumberTwo() != 0 && !"".equals(order.getNumberTwo())) {
            nums++;
        }
        if (order.getNumberThree() != null && order.getNumberThree() != 0 && !"".equals(order.getNumberThree())) {
            nums++;
        }
        if (order.getNumberFour() != null && order.getNumberFour() != 0 && !"".equals(order.getNumberFour())) {
            nums++;
        }
        if (order.getNumberFive() != null && order.getNumberFive() != 0 && !"".equals(order.getNumberFive())) {
            nums++;
        }
        if (order.getNumberSix() != null && order.getNumberSix() != 0 && !"".equals(order.getNumberSix())) {
            nums++;
        }
        if (order.getNumberSeven() != null && order.getNumberSeven() != 0 && !"".equals(order.getNumberSeven())) {
            nums++;
        }
        BigDecimal money = new BigDecimal(nums * 10);

        order.setMoney(money);
        order.setCreateTime(date);

        orderDao.save(order);
    }

    /**
     * 修改
     */
    @Transactional
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
    public Boolean findAll(String agencyId, Integer period, String type, Date date) {
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

                if (type != null) {
                    list.add(cb.equal(root.get("type").as(String.class), type));
                }

                Predicate[] p = new Predicate[list.size()];
                return cb.and(list.toArray(p));
            }
        });
        for (Order order : orderList) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String orderDate = simpleDateFormat.format(order.getCreateTime());
            String newOrderDate = simpleDateFormat.format(date);
            if (orderDate.equals(newOrderDate)) {
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

        this.save(orderList, NUMBER, period);
        this.save(orderList, ZODIAC, period);
        this.save(orderList, SIZE, period);
        this.save(orderList, COLOR, period);
        this.save(orderList, SD, period);


    }

    public List<Order> findByAgencyId(String agecncyId, Integer period) {
        List<Order> orderList = orderDao.findByAgencyIdGreaterThanOrderByPeriodDesc(agecncyId, period);
        return orderList;
    }

    private void save(List<Order> orderList, String type, Integer period) {
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
            //判断类型
            if (order.getType().equals(type)) {
                BigDecimal money = order.getMoney();
                if (money != null) {
                    countMoney = countMoney.add(money);
                }
                if (order.getNumberOne() != null && order.getNumberOne() != 0) {
                    numberOneMoney = numberOneMoney.add(new BigDecimal("10"));
                }
                if (order.getNumberTwo() != null && order.getNumberTwo() != 0) {
                    numberTwoMoney = numberTwoMoney.add(new BigDecimal("10"));
                }
                if (order.getNumberThree() != null && order.getNumberThree() != 0) {
                    numberThreeMoney = numberThreeMoney.add(new BigDecimal("10"));
                }
                if (order.getNumberFour() != null && order.getNumberFour() != 0) {
                    numberFourMoney = numberFourMoney.add(new BigDecimal("10"));
                }
                if (order.getNumberFive() != null && order.getNumberFive() != 0) {
                    numberFiveMoney = numberFiveMoney.add(new BigDecimal("10"));
                }
                if (order.getNumberSix() != null && order.getNumberSix() != 0) {
                    numberSixMoney = numberSixMoney.add(new BigDecimal("10"));
                }
                if (order.getNumberSeven() != null && order.getNumberSeven() != 0) {
                    numberSevenMoney = numberSevenMoney.add(new BigDecimal("10"));
                }
            }
        }

        Count count = countDao.findByPeriodAndType(period, type);

        String id = idWorker.nextId() + "";
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
            count.setType(type);
            countDao.save(count);
        } else {
            //不存在，新建
            count = new Count();
            count.setId(id);
            count.setPeriod(period);
            count.setCountMoney(countMoney);
            count.setNumberOneTotalMoney(numberOneMoney);
            count.setNumberTwoTotalMoney(numberTwoMoney);
            count.setNumberThreeTotalMoney(numberThreeMoney);
            count.setNumberFourTotalMoney(numberFourMoney);
            count.setNumberFiveTotalMoney(numberFiveMoney);
            count.setNumberSixTotalMoney(numberSixMoney);
            count.setNumberSevenTotalMoney(numberSevenMoney);
            count.setType(type);
            countDao.save(count);
        }
    }

    @Transactional
    public void saveLotteryStatus(LotteryStatus lotteryStatus) {
        lotteryStatusDao.save(lotteryStatus);
    }

    @Transactional
    public void updateLotteryStatus(Integer period) {
        LotteryStatus lotteryStatus = lotteryStatusDao.findById(period).get();
        lotteryStatus.setStatus("close");
        lotteryStatus.setUpdateTime(new Date());
        lotteryStatusDao.save(lotteryStatus);
    }
}
