package com.example.demo.api.bill.service;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.example.demo.api.bill.entity.BillStatus;
import com.example.demo.api.bill.model.Bill;
import com.example.demo.api.bill.repository.BillRepository;
import com.example.demo.common.cache.CacheOptions;
import com.example.demo.common.cache.KvCacheFactory;
import com.example.demo.common.cache.KvCacheWrap;
import com.example.demo.common.exception.ServiceException;
import com.example.demo.common.model.Constants;
import com.sunnysuperman.kvcache.RepositoryProvider;
import com.sunnysuperman.kvcache.converter.BeanModelConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillServiceImpl implements BillService{
    @Autowired
    private BillRepository billRepository;


    @Autowired
    private KvCacheFactory kvCacheFactory;

    private KvCacheWrap<Integer, Bill> billCache;

    @PostConstruct
    public void init() {
        billCache = kvCacheFactory.create(new CacheOptions("billCache", 1, Constants.CACHE_REDIS_EXPIRE),
                new RepositoryProvider<Integer, Bill>() {

                    @Override
                    public Bill findByKey(Integer id) throws ServiceException {
                        return billRepository.findById(id).orElse(null);
                    }

                    @Override
                    public Map<Integer, Bill> findByKeys(Collection<Integer> ids) throws
                            ServiceException {
                        return billRepository.findByIdIn(ids).stream().collect(Collectors.toMap(Bill::getId, item -> item));
                    }
                }, new BeanModelConverter<>(Bill.class));
    }

    public void save(Bill bill) {
        billRepository.save(bill);
    }

    @Override
    public void updateStatus(String orderNumber, Byte status) {
        Bill bill = billRepository.findByPayNumber(orderNumber);
        billRepository.updateBillStatus(orderNumber, status);
        if (status == BillStatus.PAIED.value()) {
            save(bill);
        }
        billCache.remove(bill.getId());
    }
}
