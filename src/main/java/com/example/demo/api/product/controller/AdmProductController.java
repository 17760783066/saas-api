package com.example.demo.api.product.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.api.admin.entity.AdminPermission;
import com.example.demo.api.product.model.Brand;
import com.example.demo.api.product.model.ProductCategory;
import com.example.demo.api.product.model.ProductTemplate;
import com.example.demo.api.product.qo.BrandQo;
import com.example.demo.api.product.qo.ProductQo;
import com.example.demo.api.product.qo.ProductWo;
import com.example.demo.api.product.service.BrandService;
import com.example.demo.api.product.service.ProductCategoryService;
import com.example.demo.api.product.service.ProductService;
import com.example.demo.api.product.service.ProductTemplateService;
import com.example.demo.common.controller.BaseController;
import com.example.demo.common.controller.auth.AdminType;
import com.example.demo.common.controller.auth.RequiredAdminType;
import com.example.demo.common.controller.auth.RequiredPermission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/adm/product")
@RequiredAdminType(AdminType.ADMIN)

public class AdmProductController extends BaseController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private ProductTemplateService productTemplateService;

    // product
    @RequestMapping(value = "/products")
    @RequiredPermission(AdminPermission.PRODUCT_EDIT)
    public ModelAndView products(String productQo) {
        return feedback(productService.page(parseModel(productQo, new ProductQo()), AdminType.MERCHANT));
    }

    // category

    @RequestMapping(value = "/save_category")
    @RequiredPermission(AdminPermission.PRODUCT_EDIT)
    public ModelAndView saveCategory(String productCategory) {
        productCategoryService.save(parseModel(productCategory, new ProductCategory()));
        return feedback();
    }

    @RequestMapping(value = "/categories")
    @RequiredPermission(AdminPermission.PRODUCT_EDIT)
    public ModelAndView productCategories() {
        return feedback(productCategoryService.tree(AdminType.ADMIN));
    }

    @RequestMapping(value = "/status_category")
    @RequiredPermission(AdminPermission.PRODUCT_EDIT)
    public ModelAndView statusCategory(Integer id, Byte status) {
        productCategoryService.status(id, status);
        return feedback();
    }

    @RequestMapping(value = "/list_category")
    @RequiredPermission(AdminPermission.PRODUCT_EDIT)
    public ModelAndView listCategory() {
        return feedback(productCategoryService.list(AdminType.ADMIN));
    }

    // template

    @RequestMapping(value = "/templates")
    @RequiredPermission(AdminPermission.PRODUCT_EDIT)
    public ModelAndView productTemplates(String productQo) {
        return feedback(
                productTemplateService.page(parseModel(productQo, new ProductQo()), AdminType.ADMIN));
    }

    @RequestMapping(value = "/save_template")
    @RequiredPermission(AdminPermission.PRODUCT_EDIT)
    public ModelAndView saveTemplate(String productTemplate) {
        productTemplateService.save(parseModel(productTemplate, new ProductTemplate()));
        return feedback();
    }

    @RequestMapping(value = "/remove_template")
    @RequiredPermission(AdminPermission.PRODUCT_EDIT)
    public ModelAndView remove(String ids) {
        productTemplateService.remove(JSON.parseArray(ids, Integer.class));
        return feedback();
    }

    @RequestMapping(value = "/product_template")
    @RequiredPermission(AdminPermission.PRODUCT_EDIT)
    public ModelAndView productTemplate(Integer id) {
        return feedback(productTemplateService.item(id));
    }

    @RequestMapping(value = "/product_template_three")
    @RequiredPermission(AdminPermission.PRODUCT_EDIT)
    public ModelAndView three(Integer id) {
        return feedback(productTemplateService.three(id));
    }

    @RequestMapping(value = "/status_product_template")
    @RequiredPermission(AdminPermission.PRODUCT_EDIT)
    public ModelAndView statusProductTemplate(Integer id, Byte status) {
        productTemplateService.status(id, status);
        return feedback();
    }

    // brand

    @RequestMapping(value = "/brand")
    @RequiredPermission(AdminPermission.PRODUCT_EDIT)
    public ModelAndView brand(Integer id) {
        return feedback(brandService.item(id));
    }

    @RequestMapping(value = "/save_brand")
    @RequiredPermission(AdminPermission.PRODUCT_EDIT)
    public ModelAndView saveBrand(String brand) {
        brandService.save(parseModel(brand, new Brand()));
        return feedback();
    }

    @RequestMapping(value = "/brands")
    @RequiredPermission(AdminPermission.PRODUCT_EDIT)
    public ModelAndView brands(String brandQo) {
        return feedback(
                brandService.page(parseModel(brandQo, new BrandQo()), AdminType.ADMIN));
    }

    @RequestMapping(value = "/list_brand")
    @RequiredPermission(AdminPermission.PRODUCT_EDIT)
    public ModelAndView listBrand() {
        return feedback(
                brandService.list());
    }

    @RequestMapping(value = "/status_brand")
    @RequiredPermission(AdminPermission.PRODUCT_EDIT)
    public ModelAndView statusBrand(Integer id, Byte status) {
        brandService.status(id, status);
        return feedback();

    }
}
