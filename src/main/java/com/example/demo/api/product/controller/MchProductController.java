package com.example.demo.api.product.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.api.merchant.entity.MerchantPermission;
import com.example.demo.api.product.model.Product;
import com.example.demo.api.product.qo.ProductQo;
import com.example.demo.api.product.service.BrandService;
import com.example.demo.api.product.service.ProductCategoryService;
import com.example.demo.api.product.service.ProductService;
import com.example.demo.api.product.service.ProductTemplateService;
import com.example.demo.common.controller.BaseController;
import com.example.demo.common.controller.auth.AdminType;
import com.example.demo.common.controller.auth.RequiredAdminType;
import com.example.demo.common.controller.auth.RequiredMerchantPermission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/merchant/product")
@RequiredAdminType(AdminType.MERCHANT)
public class MchProductController extends BaseController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductTemplateService productTemplateService;

    // template

    
    @RequestMapping(value = "/templates")
    @RequiredMerchantPermission(MerchantPermission.PRODUCT_EDIT)
    public ModelAndView templates(String productQo) {
        return feedback(
                productTemplateService.page(parseModel(productQo, new ProductQo()), AdminType.MERCHANT));
    }

    @RequestMapping(value = "/template")
    @RequiredMerchantPermission(MerchantPermission.PRODUCT_EDIT)
    public ModelAndView template(Integer id) {
        return feedback(productTemplateService.item(id));
    }

    // category

    @RequestMapping(value = "/categories")
    @RequiredMerchantPermission(MerchantPermission.PRODUCT_EDIT)
    public ModelAndView productCategories() {
        return feedback(productCategoryService.merchantTree());
    }

    @RequestMapping(value = "/list_category")
    @RequiredMerchantPermission(MerchantPermission.PRODUCT_EDIT)
    public ModelAndView productCategoriesAll() {
        return feedback(productCategoryService.list(AdminType.ADMIN));
    }

    // brand

    @RequestMapping(value = "/all_brands")
    @RequiredMerchantPermission(MerchantPermission.PRODUCT_EDIT)
    public ModelAndView allBrands() {
        return feedback(brandService.list());
    }

    // product
    @RequestMapping(value = "/product")
    @RequiredMerchantPermission(MerchantPermission.PRODUCT_EDIT)
    public ModelAndView product(Integer id) {
        return feedback(productService.item(id));
    }

    @RequestMapping(value = "/save_product")
    @RequiredMerchantPermission(MerchantPermission.PRODUCT_EDIT)
    public ModelAndView saveProduct(String product) {
        productService.save(parseModel(product, new Product()));
        return feedback();
    }

    @RequestMapping(value = "/products")
    @RequiredMerchantPermission(MerchantPermission.PRODUCT_EDIT)
    public ModelAndView products(String productQo) {
        return feedback(productService.page(parseModel(productQo, new ProductQo()), AdminType.MERCHANT));
    }

    @RequestMapping(value = "/status_product")
    @RequiredMerchantPermission(MerchantPermission.PRODUCT_EDIT)
    public ModelAndView statusProduct(Integer id, Byte status) {
        productService.status(id, status);
        return feedback();
    }

    @RequestMapping(value = "/remove_product")
    @RequiredMerchantPermission(MerchantPermission.PRODUCT_EDIT)
    public ModelAndView remove(String ids) {
        productService.remove(JSON.parseArray(ids, Integer.class));
        return feedback();
    }

    @RequestMapping(value = "/product_three")
    @RequiredMerchantPermission(MerchantPermission.PRODUCT_EDIT)
    public ModelAndView three(Integer id) {
        return feedback(productService.three(id));
    }

    @RequestMapping(value = "/product_template_three")
    @RequiredMerchantPermission(MerchantPermission.PRODUCT_EDIT)
    public ModelAndView templateThree(Integer id) {
        return feedback(productTemplateService.three(id));
    }
}
