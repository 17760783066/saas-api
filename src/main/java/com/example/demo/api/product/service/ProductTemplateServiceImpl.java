package com.example.demo.api.product.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.demo.api.admin.entity.AdminError;
import com.example.demo.api.merchant.authority.MerchantAdminContexts;
import com.example.demo.api.product.model.ProductCategory;
import com.example.demo.api.product.model.ProductTemplate;
import com.example.demo.api.product.qo.ProductQo;
import com.example.demo.api.product.repository.ProductTemplateRepository;
import com.example.demo.common.controller.auth.AdminType;
import com.example.demo.common.exception.ArgumentServiceException;
import com.example.demo.common.exception.ServiceException;
import com.example.demo.common.model.ErrorCode;
import com.example.demo.common.util.CollectionUtils;
import com.example.demo.common.util.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class ProductTemplateServiceImpl implements ProductTemplateService, AdminError {

    @Autowired
    private ProductTemplateRepository productTemplateRepository;

    @Autowired
    private ProductCategoryService productCategoryService;

    public List<Integer> mixed2Three(List<Integer> mixtureCategoryIds, AdminType adminType) {
        List<Integer> threeCategoryIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(mixtureCategoryIds)) {
            List<ProductCategory> tree = productCategoryService.tree(adminType);
            for (ProductCategory c : tree) {
                boolean with1 = mixtureCategoryIds.contains(c.getId());
                List<ProductCategory> c2 = c.getChildren();
                for (ProductCategory c3 : c2) {
                    boolean with2 = mixtureCategoryIds.contains(c3.getId());
                    List<ProductCategory> c4 = c3.getChildren();
                    for (ProductCategory c5 : c4) {
                        boolean with3 = mixtureCategoryIds.contains(c5.getId());
                        if (with1 || with2 || with3) {
                            threeCategoryIds.add(c5.getId());
                        }
                    }
                }
            }
        }

        return threeCategoryIds;
    }

    @Override
    public Page<ProductTemplate> page(ProductQo qo, AdminType adminType) {

        List<Integer> mixtureCategoryIds = qo.getMixtureCategoryIds();
        boolean isAdmin = adminType == AdminType.ADMIN;

        List<Integer> threeCategoryIds = mixed2Three(mixtureCategoryIds, adminType);

        boolean isEmpty = CollectionUtils.isEmpty(threeCategoryIds);
        if (isAdmin) {
            if (isEmpty) {
                threeCategoryIds = null;
            }
        } else {
            List<String> seqs = MerchantAdminContexts.requestProductCategorySequence();
            List<ProductCategory> tmp = productCategoryService.sequences2List(seqs);
            List<Integer> grantIds = tmp.stream().map(ProductCategory::getId).collect(Collectors.toList());
            if (isEmpty) {
                threeCategoryIds = grantIds;
            } else {
                threeCategoryIds = threeCategoryIds.stream().filter(it -> grantIds.contains(it))
                        .collect(Collectors.toList());
            }
            if (CollectionUtils.isEmpty(threeCategoryIds)) {
                threeCategoryIds = Collections.singletonList(0);
            }
        }
        qo.setCategoryIds(threeCategoryIds);
        return productTemplateRepository.findAll(qo);
    }

    @Override
    public void status(Integer id, Byte status) {
        ProductTemplate item = getById(id);
        item.setStatus(status);
        productTemplateRepository.save(item);
    }

    private ProductTemplate getById(Integer id) {
        ProductTemplate item = productTemplateRepository.findById(id).orElse(null);
        if (StringUtils.isNull(item)) {
            throw new ServiceException(ErrorCode.ERR_DATA_NOT_FOUND);
        }
        return item;
    }

    @Override
    public void save(ProductTemplate productTemplate) {
        if (productTemplate.getId() != null && productTemplate.getId() > 0) {// 编辑
            productTemplateRepository.save(productTemplate);
        } else {// 新建
            productTemplate.setCreatedAt(System.currentTimeMillis());
            if (productTemplate.getCategoryId() == null) {
                throw new ArgumentServiceException("请选择分类");
            }
            productTemplateRepository.save(productTemplate);
        }
    }

    @Override
    public void remove(List<Integer> ids) {
        productTemplateRepository.deleteByIds(ids);
    }

    @Override
    public ProductTemplate item(Integer id) {
        ProductTemplate productTemplate = getById(id);
        return productTemplate;
    }

    @Override
    public Map<String, Object> three(Integer id) {
        ProductTemplate productTemplate = getById(id);
        ProductTemplate pre = productTemplateRepository.findFirstByIdBeforeOrderByIdDesc(id);
        ProductTemplate next = productTemplateRepository.findFirstByIdAfterOrderByIdAsc(id);
        return CollectionUtils.arrayAsMap("productTemplate", productTemplate, "pre", pre, "next", next);
    }

}
