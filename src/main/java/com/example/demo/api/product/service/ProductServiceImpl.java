package com.example.demo.api.product.service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.example.demo.api.admin.entity.AdminError;
import com.example.demo.api.merchant.authority.MerchantAdminContexts;
import com.example.demo.api.merchant.model.Merchant;
import com.example.demo.api.merchant.service.MerchantService;
import com.example.demo.api.product.model.Product;
import com.example.demo.api.product.qo.ProductQo;
import com.example.demo.api.product.qo.ProductWo;
import com.example.demo.api.product.repository.ProductRepository;
import com.example.demo.common.cache.CacheOptions;
import com.example.demo.common.cache.KvCacheFactory;
import com.example.demo.common.cache.KvCacheWrap;
import com.example.demo.common.cache.SingleRepositoryProvider;
import com.example.demo.common.controller.auth.AdminType;
import com.example.demo.common.exception.ArgumentServiceException;
import com.example.demo.common.exception.ServiceException;
import com.example.demo.common.model.Constants;
import com.example.demo.common.model.ErrorCode;
import com.example.demo.common.util.CollectionUtils;
import com.example.demo.common.util.StringUtils;
import com.sunnysuperman.kvcache.RepositoryProvider;
import com.sunnysuperman.kvcache.converter.BeanModelConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService, AdminError {

    @Autowired
    private KvCacheFactory kvCacheFactory;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MerchantService merchantService;
    // 单条分类缓存（三种状态）
    private KvCacheWrap<Integer, Product> productCache;

    @PostConstruct
    public void init() {

        productCache = kvCacheFactory.create(new CacheOptions("product", 1, Constants.CACHE_REDIS_EXPIRE),
                new RepositoryProvider<Integer, Product>() {

                    @Override
                    public Product findByKey(Integer key) throws ServiceException {
                        return productRepository.findById(key).orElse(null);
                    }

                    @Override
                    public Map<Integer, Product> findByKeys(Collection<Integer> keys) throws Exception {
                        keys = keys.stream().filter(a -> a != null && a > 0).collect(Collectors.toSet());
                        if (CollectionUtils.isEmpty(keys)) {
                            return Collections.emptyMap();
                        }
                        List<Product> list = productRepository.findAllById(keys);
                        return list.stream().collect(Collectors.toMap(Product::getId, m -> m));
                    }
                }, new BeanModelConverter<>(Product.class));
    }

    @Override
    public Page<Product> page(ProductQo qo, AdminType adminType) {
        qo.setMerchantId(MerchantAdminContexts.requestMerchantId());

        return productRepository.findAll(qo);

    }

    @Override
    public List<Product> list(ProductQo qo) {
        Page<Product> page = productRepository.findAll(qo);
        List<Product> list = page.stream().filter(x -> x.getMerchantId() == qo.getMerchantId())
                .collect(Collectors.toList());
        List<Product> products = list;
        return products;
    }

    @Override
    public Product item(Integer productId) {
        Product product = findById(productId);
        return product;
    }

    @Override
    public Product item(Integer productId, ProductWo wo) {
        Product product = findById(productId);
        wrapProduct(Collections.singletonList(product), wo);
        return product;
    }

    private void wrapProduct(List<Product> products, ProductWo wo) {

        Map<Integer, Merchant> merchantMap = new HashMap<>();
        if (wo.isWithMerchant()) {
            Set<Integer> merchantIds = products.stream().map(Product::getMerchantId).collect(Collectors.toSet());
            merchantMap = merchantService.findByIdIn(merchantIds);
        }
        for (Product item : products) {
            if (wo.isWithMerchant()) {
                item.setMerchant(merchantMap.get(item.getMerchantId()));
            }
        }
    }

    private Product findById(Integer id) {
        Product product = productCache.findByKey(id);
        if (StringUtils.isNull(product)) {
            throw new ServiceException(ErrorCode.ERR_DATA_NOT_FOUND);
        }
        return product;
    }

    @Override
    public void save(Product product) {
        if (product.getCategoryId() == null) {
            throw new ArgumentServiceException("请选择分类");
        }
        Integer id = product.getId();
        if (id != null && id > 0) {// 编辑
            Product pro = findById(id);
            pro.setCreatedAt(System.currentTimeMillis());
            pro.setName(product.getName());
            pro.setSpecs(product.getSpecs());
            pro.setParams(product.getParams());
            pro.setContent(product.getContent());
            productRepository.save(pro);
            productCache.remove(id);
        } else {// 新建
            product.setCreatedAt(System.currentTimeMillis());
            product.setMerchantId(MerchantAdminContexts.requestMerchantId());
            productRepository.save(product);
        }
    }

    @Override
    public void remove(List<Integer> ids) {
        productRepository.deleteByIds(ids);
        productCache.flush();
    }

    @Override
    public Map<String, Object> three(Integer id) {
        Product product = findById(id);
        Product pre = productRepository.findFirstByIdBeforeOrderByIdDesc(id);
        Product next = productRepository.findFirstByIdAfterOrderByIdAsc(id);
        return CollectionUtils.arrayAsMap("product", product, "pre", pre, "next", next);
    }

    @Override
    public void status(Integer id, Byte status) {
        Product product = findById(id);
        product.setStatus(status);
        productRepository.save(product);

        productCache.remove(id);
    }

    @Override
    public List<Product> list() {
        return productRepository.findAll();
    }

    @Override
    public Map<Integer, Product> findByIdIn(Collection<Integer> ids) {
        return productCache.findByKeys(ids);
    }

    @Override
    public void saveAll(List<Product> products) {
        productRepository.saveAll(products);
        productCache.flush();
    }

}
