package com.example.demo.api.user.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.example.demo.api.merchant.model.Merchant;
import com.example.demo.api.merchant.service.MerchantService;
import com.example.demo.api.product.model.Product;
import com.example.demo.api.product.service.ProductService;
import com.example.demo.api.user.authority.UserContexts;
import com.example.demo.api.user.model.Shopping;
import com.example.demo.api.user.qo.ShoppingQo;
import com.example.demo.api.user.qo.ShoppingWo;
import com.example.demo.api.user.repository.ShoppingRepository;
import com.example.demo.common.cache.CacheOptions;
import com.example.demo.common.cache.KvCacheFactory;
import com.example.demo.common.cache.KvCacheWrap;
import com.example.demo.common.controller.auth.AdminType;
import com.example.demo.common.exception.ServiceException;
import com.example.demo.common.model.Constants;
import com.example.demo.common.model.ErrorCode;
import com.example.demo.common.util.StringUtils;
import com.sunnysuperman.kvcache.RepositoryProvider;
import com.sunnysuperman.kvcache.converter.BeanModelConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoppingServiceImpl implements ShoppingService {
    @Autowired
    private ShoppingRepository shoppingRepository;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private ProductService productService;
    @Autowired
    private KvCacheFactory kvCacheFactory;

    private KvCacheWrap<Integer, Shopping> cartCache;

    @PostConstruct
    public void init() {
        cartCache = kvCacheFactory.create(new CacheOptions("shopping", 1, Constants.CACHE_REDIS_EXPIRE),
                new RepositoryProvider<Integer, Shopping>() {

                    @Override
                    public Shopping findByKey(Integer id) throws ServiceException {
                        return shoppingRepository.findById(id).orElse(null);
                    }

                    @Override
                    public Map<Integer, Shopping> findByKeys(Collection<Integer> ids) throws ServiceException {
                        List<Integer> idlist = new ArrayList<>(ids);
                        List<Shopping> carts = shoppingRepository.findByIdIn(idlist);
                        return carts.stream().collect(Collectors.toMap(Shopping::getId, c -> c));
                    }
                }, new BeanModelConverter<>(Shopping.class));
    }

    @Override
    public Shopping shoppingSave(Shopping shopping) {
        int userId = UserContexts.requestUserId();
        Product product = productService.item(shopping.getProductId());
        Shopping exits = shoppingRepository.findByUserIdAndProductIdAndMerchantIdAndProductSno(userId,
                shopping.getProductId(), shopping.getMerchantId(), shopping.getProductSno());
        if (exits == null) {
            shopping.setCreatedAt(System.currentTimeMillis());
            shopping.setUserId(UserContexts.requestUserId());
            shopping.setStatus(Constants.STATUS_OK);
            shoppingRepository.save(shopping);
            return shopping;
        } else {
            if (exits.getNumber() <= 0) {
                shoppingRepository.deleteById(exits.getId());
                cartCache.remove(exits.getId());
            } else if (exits.getProductSno().equals(shopping.getProductSno())) {
                exits.setNumber(exits.getNumber() + shopping.getNumber());
                shoppingRepository.save(exits);
                cartCache.remove(exits.getId());
            }
            return exits;
        }

    }

    @Override
    public List<Shopping> listShopping(ShoppingQo qo, ShoppingWo wo) {
        List<Shopping> shoppings = shoppingRepository.findAll(qo);
        wrapShopping(shoppings, wo);
        return shoppings;
    }

    private void wrapShopping(List<Shopping> shoppings, ShoppingWo wo) {

        Map<Integer, Merchant> merchantMap = new HashMap<>();
        Map<Integer, Product> productMap = new HashMap<>();
        if (wo.isWithMerchant()) {
            Set<Integer> merchantIds = shoppings.stream().map(Shopping::getMerchantId).collect(Collectors.toSet());
            merchantMap = merchantService.findByIdIn(merchantIds);
        }
        if (wo.isWithProduct()) {
            Set<Integer> productIds = shoppings.stream().map(Shopping::getProductId).collect(Collectors.toSet());
            productMap = productService.findByIdIn(productIds);
        }
        for (Shopping item : shoppings) {
            if (wo.isWithMerchant()) {
                item.setMerchant(merchantMap.get(item.getMerchantId()));
            }

            if (wo.isWithProduct()) {
                item.setProduct(productMap.get(item.getProductId()));
            }
        }
    }

    private Shopping getById(Integer id) {
        Shopping item = shoppingRepository.findById(id).orElse(null);
        if (StringUtils.isNull(item)) {
            throw new ServiceException(ErrorCode.ERR_DATA_NOT_FOUND);
        }
        return item;
    }

    public Shopping cart(Integer id) {
        Shopping cart = cartCache.findByKey(id);
        if (cart == null) {
            throw new ArithmeticException("该商品不存在！");
        } else {
            cart.setProduct(productService.item(cart.getProductId()));
            cart.setMerchant(merchantService.merchant(cart.getMerchantId()));
            return cart;
        }
    }

    @Override
    public void updateNumber(Integer id, Integer number) {
        // if (!cart(id).getUserId().equals(UserContexts.requestUserId())) {
        // throw new ArithmeticException("您没有权限更改当前信息！");
        // } else {
        shoppingRepository.updateNumber(id, number);
        cartCache.remove(id);
        // }
    }

    @Override
    public void removeOne(Integer id) {
        // if (!cart(id).getUserId().equals(UserContexts.requestUserId())) {
        // throw new ArithmeticException("您没有权限删除当前信息！");
        // } else {
        shoppingRepository.delete(getById(id));
        cartCache.remove(id);
        // }
    }

    @Override
    public void updateParams(Integer id, String productSno) {

        // if (!cart(id).getUserId().equals(UserContexts.requestUserId())) {
        // throw new ArithmeticException("您没有权限更改当前信息！");
        // }
        Shopping exits = getById(id);
        Shopping newCart = new Shopping();
        if (!exits.getProductSno().equals(productSno)) {
            newCart.copy(exits, productSno);
            shoppingSave(newCart);
            removeOne(id);
            cartCache.remove(id);
        }
    }

    @Override
    public List<Shopping> findAll(ShoppingWo wo) {
        Integer userId = UserContexts.requestUserId();
        ShoppingQo qo = new ShoppingQo();
        qo.setUserId(userId);
        List<Shopping> shoppings = shoppingRepository.findAll(qo);
        wrapShopping(shoppings, wo);
        return null;
    }

    @Override
    public List<Shopping> findCartList(List<Integer> ids,ShoppingWo wo) {
        List<Shopping> shoppings = shoppingRepository.findByIdIn(ids);
        wrapShopping(shoppings, wo);
        return shoppings;
    }

    @Override
    public void removeList(List<Integer> cartIds) {
        shoppingRepository.deleteByIds(cartIds);
        cartCache.flush();
    }

}
