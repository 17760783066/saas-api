package com.example.demo.api.trade.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.example.demo.api.bill.service.BillService;
import com.example.demo.api.merchant.model.Merchant;
import com.example.demo.api.merchant.service.MerchantService;
import com.example.demo.api.product.model.Product;
import com.example.demo.api.product.model.Spec;
import com.example.demo.api.product.service.ProductCategoryService;
import com.example.demo.api.product.service.ProductService;
import com.example.demo.api.trade.entity.TradeItem;
import com.example.demo.api.trade.entity.TradeType;
import com.example.demo.api.trade.entity.TradeWrapOption;
import com.example.demo.api.trade.model.Trade;
import com.example.demo.api.trade.qo.TradeQo;
import com.example.demo.api.trade.repository.TradeRepository;
import com.example.demo.api.user.authority.UserContexts;
import com.example.demo.api.user.model.User;
import com.example.demo.api.user.service.ShoppingService;
import com.example.demo.api.user.service.UserService;
import com.example.demo.common.cache.CacheOptions;
import com.example.demo.common.cache.KvCacheFactory;
import com.example.demo.common.cache.KvCacheWrap;
import com.example.demo.common.exception.ArgumentServiceException;
import com.example.demo.common.exception.ServiceException;
import com.example.demo.common.model.Constants;
import com.example.demo.common.util.StringUtils;
import com.sunnysuperman.kvcache.RepositoryProvider;
import com.sunnysuperman.kvcache.converter.BeanModelConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class TradeServiceImpl implements TradeService {
    @Autowired
    private TradeRepository tradeRepository;
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private ShoppingService shoppingService;
    @Autowired
    private ProductService productService;
    @Autowired
    private BillService billService;
    @Autowired
    private UserService userService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private KvCacheFactory kvCacheFactory;

    private KvCacheWrap<Integer, Trade> tradeCache;

    @PostConstruct
    public void init() {
        tradeCache = kvCacheFactory.create(new CacheOptions("tradeCache", 1, Constants.CACHE_REDIS_EXPIRE),
                new RepositoryProvider<Integer, Trade>() {

                    @Override
                    public Trade findByKey(Integer id) throws ServiceException {
                        return tradeRepository.findById(id).orElse(null);
                    }

                    @Override
                    public Map<Integer, Trade> findByKeys(Collection<Integer> ids) throws ServiceException {
                        return tradeRepository.findByIdIn(ids).stream()
                                .collect(Collectors.toMap(Trade::getId, item -> item));
                    }
                }, new BeanModelConverter<>(Trade.class));
    }

    @Override
    public Integer save(Trade trade, Integer total, Integer totalAmoun) {
        Integer userId = UserContexts.requestUserId();
        List<Integer> cartIds = trade.getTradeItems().stream().map(TradeItem::getId).collect(Collectors.toList());
        List<Product> products = trade.getTradeItems().stream().map(TradeItem::getProduct).collect(Collectors.toList());
        List<Integer> merchantIds = products.stream().map(Product::getMerchantId).collect(Collectors.toList());
        Integer merchantId = merchantIds.get(0);
        Integer couponAmount = 0;
        // totalPrice = totalPrice(products, trade);
        // if (userCouponId == 0) {
        // couponAmount = 0;
        // } else {
        // couponAmount = couponAmount(trade, userCouponId);
        // }
        trade.setUserId(userId);
        trade.setMerchantId(merchantId);
        trade.setCreatedAt(System.currentTimeMillis());
        trade.setType(TradeType.NO_PAY.value());
        trade.setOrderNumber(StringUtils.getOrderNumber());
        trade.setTotalPrice(total);
        trade.setCouponAmount(couponAmount);
        trade.setTotalAmount(totalAmoun);
        tradeRepository.save(trade);
        // userCouponService.updateStatus(userCouponId, CouponStatus.USED.value());
        // billService.createBillByTrade(trade);
        updateStock(trade);
        shoppingService.removeList(cartIds);
        return findByOrderNumber(trade.getOrderNumber()).getId();
    }

    public Trade findByOrderNumber(String orderNum) {
        Trade trade = tradeRepository.findByOrderNumber(orderNum);
        if (trade == null) {
            throw new ArgumentServiceException("订单不存在！");
        } else {
            return trade;
        }
    }

    void updateStock(Trade trade) {

        List<TradeItem> list = new ArrayList<>();
        Map<Integer, Integer> productCountMap = new HashMap<>();
        Map<Integer, String> productSpecMap = new HashMap<>();
        for (TradeItem tradeItem : trade.getTradeItems()) {
            Integer productId = tradeItem.getProduct().getId();
            productCountMap.put(productId, tradeItem.getNumber());
            productSpecMap.put(productId, tradeItem.getProductSno());
            tradeItem.setProduct(tradeItem.getProduct());
            list.add(tradeItem);
        }
        List<Product> products = trade.getTradeItems().stream().map(TradeItem::getProduct).collect(Collectors.toList());
        for (Product product : products) {
            Integer count = productCountMap.get(product.getId());
            List<Spec> specs = product.getSpecs();
            for (Spec spec : specs) {
                String s = productSpecMap.get(product.getId());
                if (spec.getSno().equals(s)) {
                    if (trade.getType() == TradeType.CALL_OFF_TRADE.value()) {
                        // 取消订单，返还库存
                        spec.setRepertory(spec.getRepertory() + count);
                    }
                    if (trade.getType() == TradeType.NO_PAY.value()) {
                        if ((spec.getRepertory() - count) < 0) {
                            throw new ArgumentServiceException("库存不足");
                        } else {
                            // 生成订单，扣库存
                            spec.setRepertory(spec.getRepertory() - count);
                        }
                    }
                }
            }
        }
        productService.saveAll(products);
    }

    @Override
    public Trade findById(Integer id) {
        Trade trade = tradeCache.findByKey(id);
        return trade;
    }

    @Override
    public void updateType(Integer id, Byte type) {
        checkType(type);
        tradeRepository.updateTradeType(id, type);
        tradeCache.remove(id);
        // if (type == TradeType.NO_SEND.value()) {
        // billService.updateStatus(findById(id).getOrderNumber(),
        // BillStatus.PAIED.value());
        // }
        // if (type == TradeType.CALL_OFF_TRADE.value()) {
        // Trade trade = findById(id);
        // billService.updateStatus(trade.getOrderNumber(), BillStatus.CALLOFF.value());
        // updateStock(trade);
        // }
    }

    public TradeType checkType(Byte type) {
        TradeType tradeType = TradeType.findTrade(type);
        if (tradeType == null) {
            throw new ArgumentServiceException("type");
        }
        return tradeType;
    }

    @Override
    public Trade warpUserFindById(Integer id) {
        Trade trade = tradeCache.findByKey(id);
        trade.setUser(userService.user(trade.getUserId()));
        return trade;

    }

    @Override
    public Page<Trade> trades(TradeQo qo, TradeWrapOption option) {
        Page<Trade> page = tradeRepository.findAll(qo);
        wrapTrade(page.getContent(), option);
        return page;
    }

    public void wrapTrade(List<Trade> list, TradeWrapOption option) {
        if (option.isWithMerchant()) {
            Set<Integer> merchantIds = list.stream().map(Trade::getMerchantId).collect(Collectors.toSet());
            Map<Integer, Merchant> merchantMap = merchantService.findByIds(merchantIds);
            for (Trade trade : list) {
                trade.setMerchant(merchantMap.get(trade.getMerchantId()));
            }
        }

        if (option.isWithUser()) {
            Set<Integer> userIds = list.stream().map(Trade::getUserId).collect(Collectors.toSet());
            Map<Integer, User> userMap = userService.findByIdIn(userIds);
            for (Trade trade : list) {
                trade.setUser(userMap.get(trade.getUserId()));
            }
        }
    }
}
