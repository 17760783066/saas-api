package com.example.demo.api.product.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.example.demo.api.admin.entity.AdminError;
import com.example.demo.api.merchant.authority.MerchantAdminContexts;
import com.example.demo.api.product.model.ProductCategory;
import com.example.demo.api.product.model.ProductConstants;
import com.example.demo.api.product.qo.ProductCategoryQo;
import com.example.demo.api.product.repository.ProductCategoryRepository;
import com.example.demo.common.cache.CacheOptions;
import com.example.demo.common.cache.KvCacheFactory;
import com.example.demo.common.cache.KvCacheWrap;
import com.example.demo.common.cache.SingleRepositoryProvider;
import com.example.demo.common.controller.auth.AdminType;
import com.example.demo.common.exception.ServiceException;
import com.example.demo.common.model.Constants;
import com.example.demo.common.model.ErrorCode;
import com.example.demo.common.util.CollectionUtils;
import com.example.demo.common.util.StringUtils;
import com.sunnysuperman.kvcache.converter.BeanModelConverter;
import com.sunnysuperman.kvcache.converter.ListModelConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService, AdminError {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private KvCacheFactory kvCacheFactory;

    // 单条分类缓存（三种状态）
    private KvCacheWrap<Integer, ProductCategory> categoryCache;

    // 过滤器专用分类缓存（三种状态）
    private KvCacheWrap<Byte, List<ProductCategory>> categoryListCache;

    // 结构化分类缓存（两种状态）
    private KvCacheWrap<Byte, List<ProductCategory>> structProductCategoryCache;

    // 机构分类缓存(过滤后仅支持上架状态分类)
    private KvCacheWrap<String, List<ProductCategory>> merchantProductCategoryCache;

    @PostConstruct
    public void init() {

        categoryCache = kvCacheFactory.create(new CacheOptions("product_category", 1, Constants.CACHE_REDIS_EXPIRE),
                new SingleRepositoryProvider<Integer, ProductCategory>() {

                    @Override
                    public ProductCategory findByKey(Integer key) throws ServiceException {
                        return productCategoryRepository.findById(key).orElse(null);
                    }

                }, new BeanModelConverter<>(ProductCategory.class));

        categoryListCache = kvCacheFactory.create(
                new CacheOptions("product_category_list", 2, Constants.CACHE_REDIS_EXPIRE),
                new SingleRepositoryProvider<Byte, List<ProductCategory>>() {

                    @Override
                    public List<ProductCategory> findByKey(Byte key) throws ServiceException {
                        return productCategoryRepository.findAll(new ProductCategoryQo(key));
                    }

                }, new ListModelConverter<>(ProductCategory.class));

        structProductCategoryCache = kvCacheFactory.create(
                new CacheOptions("struct_product_category", 1, Constants.CACHE_REDIS_EXPIRE),
                new SingleRepositoryProvider<Byte, List<ProductCategory>>() {

                    @Override
                    public List<ProductCategory> findByKey(Byte key) throws ServiceException {
                        return list2Tree(
                                productCategoryRepository.findAll(new ProductCategoryQo(key)));
                    }

                }, new ListModelConverter<>(ProductCategory.class));

        merchantProductCategoryCache = kvCacheFactory.create(
                new CacheOptions("merchant_product_category", 2, 120),
                new SingleRepositoryProvider<String, List<ProductCategory>>() {

                    @Override
                    public List<ProductCategory> findByKey(String key) throws ServiceException {
                        return filterProductCategories(key);
                    }

                }, new ListModelConverter<>(ProductCategory.class));
    }

    private List<ProductCategory> filterProductCategories(String key) {

        String[] sequences = key.split(",");
        return list2Tree(sequences2List(Arrays.asList(sequences)));
    }

    public List<ProductCategory> sequences2List(List<String> ss) {

        List<ProductCategory> list = categoryListCache.findByKey((byte) 1);
        List<ProductCategory> ret = new ArrayList<>(list.size());

        for (ProductCategory category : list) {
            boolean find = false;
            String sequence = category.getSequence();
            for (String s : ss) {
                if ((s.length() == 2 && s.equals(sequence.substring(0, 2)))
                        || (sequence.length() == 2 && sequence.equals(s.substring(0, 2)))
                        || (s.length() == 4 && sequence.length() > 3 && s.equals(sequence.substring(0, 4)))
                        || (sequence.length() == 4 && s.length() > 3 && sequence.equals(s.substring(0, 4)))
                        || s.equals(sequence)) {
                    find = true;
                    break;
                }
            }
            if (find) {
                ret.add(category);
            }
        }
        return ret;
    }

    private List<ProductCategory> list2Tree(List<ProductCategory> list) {
        Map<Integer, List<ProductCategory>> map = list.stream()
                .collect(Collectors.groupingBy(ProductCategory::getParentId));
        List<ProductCategory> ret = map.get(0);// 一级分类
        if (CollectionUtils.isEmpty(ret)) {
            return Collections.emptyList();
        }
        for (ProductCategory c : ret) {
            List<ProductCategory> children = map.get(c.getId());
            if (CollectionUtils.isNotEmpty(children)) {
                for (ProductCategory c2 : children) {
                    List<ProductCategory> children2 = map.get(c2.getId());
                    c2.setChildren(children2);
                }
                c.setChildren(children);
            }
        }
        return ret;
    }

    @Override
    public void save(ProductCategory category) {
        Integer id = category.getId();
        if (id != null && id > 0) {

            ProductCategory exist = getById(id);

            exist.setIcon(category.getIcon());
            exist.setName(category.getName());
            exist.setPriority(category.getPriority());

            productCategoryRepository.save(category);
            clear(category.getId());
        } else {

            category.setStatus(ProductConstants.PRODUCT_STATUS_ON);
            category.setSequence(maxSequence(category.getParentId()));
            productCategoryRepository.save(category);
            clear(9999);
        }
    }

    private Byte adminType2Key(AdminType adminType) {

        Byte key = ProductConstants.PRODUCT_STATUS_ON;

        if (adminType == AdminType.SYSTEM) {
            key = ProductConstants.PRODUCT_STATUS_REMOVE;
        } else if (adminType == AdminType.ADMIN) {
            key = ProductConstants.PRODUCT_STATUS_OFF;
        }
        return key;
    }

    private String maxSequence(int parentId) {

        List<ProductCategory> all = list(AdminType.SYSTEM);
        if (parentId == 0 && CollectionUtils.isEmpty(all)) {
            return "01";
        }
        List<ProductCategory> list = all.stream().filter(m -> m.getParentId() == parentId).collect(Collectors.toList());

        ProductCategory parent = null;
        if (parentId > 0) {
            parent = getById(parentId);
            if (CollectionUtils.isEmpty(list)) {
                return parent.getSequence() + "01";
            }
        }

        list.sort(new Comparator<ProductCategory>() {
            @Override
            public int compare(ProductCategory c1, ProductCategory c2) {
                return c2.getSequence().compareTo(c1.getSequence());
            }
        });

        ProductCategory category = list.get(0);
        String sequence = category.getSequence();
        if (parentId == 0) {
            int maxIndex = Integer.parseInt(sequence.substring(0, 2));
            return pad(++maxIndex, 2);
        } else {
            int maxIndex = Integer.parseInt(sequence.substring(2, 4));
            return parent.getSequence() + pad(++maxIndex, 2);
        }
    }

    public ProductCategory getById(Integer id) {
        ProductCategory category = findById(id);
        if (category == null) {
            throw new ServiceException(ErrorCode.ERR_DATA_NOT_FOUND);
        }
        return category;
    }

    private ProductCategory findById(Integer id) {
        return categoryCache.findByKey(id);
    }

    private String pad(int n, int length) {
        String zero = "000000";

        if (n < 10) {
            String s = zero + n;
            return s.substring(s.length() - length, s.length());
        }
        return String.valueOf(n);
    }

    @Override
    public List<ProductCategory> tree(AdminType adminType) {
        return structProductCategoryCache.findByKey(adminType2Key(adminType));
    }

    @Override
    public List<ProductCategory> list(AdminType adminType) {
        return categoryListCache.findByKey(adminType2Key(adminType));
    }

    @Override
    public void status(Integer id, Byte status) {
        ProductCategory product = findById(id);
        if (status == ProductConstants.PRODUCT_STATUS_OFF) {
            productCategoryRepository.status(ProductConstants.PRODUCT_STATUS_OFF, product.getSequence());
            clear(0);
        } else {
            product.setStatus(ProductConstants.PRODUCT_STATUS_ON);
            ProductCategory par = findById(product.getParentId());
            if (par != null) {
                par.setStatus(ProductConstants.PRODUCT_STATUS_ON);
                ProductCategory parPar = findById(par.getParentId());
                if (parPar != null) {
                     parPar.setStatus(ProductConstants.PRODUCT_STATUS_ON);
                      productCategoryRepository.save(parPar);
                }
                productCategoryRepository.save(par);
            }
            productCategoryRepository.save(product);
            clear(id);
        }

    }

    private void clear(Integer id) {
        if (id > 0) {
            categoryCache.remove(id);
        } else {
            categoryCache.flush();
        }

        categoryListCache.flush();
        structProductCategoryCache.flush();
        merchantProductCategoryCache.flush();

    }

    @Override
    public List<ProductCategory> merchantTree() {
        List<String> sequences = MerchantAdminContexts.requestProductCategorySequence();
        Collections.sort(sequences);
        String key = StringUtils.join(sequences, ",");
        return merchantProductCategoryCache.findByKey(key);
    }

}
