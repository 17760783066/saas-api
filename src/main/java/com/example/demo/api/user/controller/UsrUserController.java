package com.example.demo.api.user.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.api.common.service.CommonService;
import com.example.demo.api.merchant.service.MerchantService;
import com.example.demo.api.product.qo.BrandQo;
import com.example.demo.api.product.qo.ProductQo;
import com.example.demo.api.product.qo.ProductWo;
import com.example.demo.api.product.service.BrandService;
import com.example.demo.api.product.service.ProductCategoryService;
import com.example.demo.api.product.service.ProductService;
import com.example.demo.api.ui.service.UIService;
import com.example.demo.api.user.entity.UserConstants;
import com.example.demo.api.user.model.Address;
import com.example.demo.api.user.model.Shopping;
import com.example.demo.api.user.model.User;
import com.example.demo.api.user.qo.ShoppingQo;
import com.example.demo.api.user.qo.ShoppingWo;
import com.example.demo.api.user.qo.UserCollectQo;
import com.example.demo.api.user.service.AddressService;
import com.example.demo.api.user.service.ShoppingService;
import com.example.demo.api.user.service.UserCollectService;
import com.example.demo.api.user.service.UserService;
import com.example.demo.common.controller.BaseController;
import com.example.demo.common.controller.auth.AdminType;
import com.example.demo.common.controller.auth.RequiredAdminType;
import com.example.demo.common.controller.auth.Touchable;
import com.example.demo.common.model.ValCode;
import com.example.demo.common.util.CollectionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/usr/user")
@RequiredAdminType(AdminType.USER)
public class UsrUserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private ShoppingService shoppingService;

    @Autowired
    private UserCollectService userCollectService;

    @Autowired
    private ProductService productService;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private UIService uiService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private AddressService addressService;
    //address
    @RequestMapping("/addresses")
    public ModelAndView addresses() {
        return feedback(addressService.addresses());
    }
    @RequestMapping("/address-save")
    public ModelAndView save(String address) {
        addressService.saveAddress(parseModel(address, new Address()));
        return feedback();
    }
    @RequestMapping("/address")
    public ModelAndView address(Integer id) {
        return feedback(addressService.address(id));
    }

    @RequestMapping(value = "/address-remove")
    public ModelAndView addressRemove(Integer id) {
        addressService.removeOne(id);
        return feedback();
    }

    // ui
    @RequestMapping(value = "/ui")
    public ModelAndView one() {
        return feedback(uiService.one());
    }

    // shopping
    @RequestMapping(value = "/save")
    public ModelAndView shoppingSave(String shopping) {
        return feedback(shoppingService.shoppingSave(parseModel(shopping, new Shopping())));
    }

    @RequestMapping(value = "/remove")
    public ModelAndView removeOne(Integer id) {
        shoppingService.removeOne(id);
        return feedback();
    }

    @RequestMapping(value = "/update_number")
    public ModelAndView update_number(Integer id, Integer number) {
        shoppingService.updateNumber(id, number);
        return feedback();
    }

    @RequestMapping(value = "/carts")
    public ModelAndView findAll() {
        return feedback(shoppingService.findAll(ShoppingWo.getProductDetailInstance()));
    }

    @RequestMapping(value = "/cart_list")
    public ModelAndView findCartList(String ids) {
        return feedback(shoppingService.findCartList(JSON.parseArray(ids, Integer.class),
                ShoppingWo.getProductDetailInstance()));
    }

    @RequestMapping(value = "/update_params")
    public ModelAndView updateParams(Integer id, String productSno) {
        shoppingService.updateParams(id, productSno);
        return feedback();
    }

    @RequestMapping(value = "/shoppingQo")
    public ModelAndView shoppings(String shoppingQo) {
        return feedback(shoppingService.listShopping(parseModel(shoppingQo, new ShoppingQo()),
                ShoppingWo.getProductDetailInstance()));
    }

    // product
    @RequestMapping(value = "/products")
    public ModelAndView products() {
        return feedback(
                productService.list());
    }

    @RequestMapping(value = "/productQo")
    public ModelAndView products(String productQo) {
        return feedback(
                productService.list(parseModel(productQo, new ProductQo())));
    }

    @RequestMapping(value = "/product")
    public ModelAndView product(Integer id) {
        return feedback(
                productService.item(id, ProductWo.getProductDetailInstance()));
    }

    @RequestMapping(value = "/list_category")
    public ModelAndView listCategory() {
        return feedback(productCategoryService.tree(AdminType.USER));
    }

    // merchant
    @RequestMapping(value = "/merchant")
    public ModelAndView merchant(int id) {
        return feedback(merchantService.merchant(id));
    }

    @RequestMapping(value = "/props")
    public ModelAndView props() {
        return feedback(CollectionUtils.arrayAsMap("professions", UserConstants.PROFESSIONS, "educations",
                UserConstants.EDUCATIONS));
    }

    // brand
    @RequestMapping(value = "/all_brands")
    public ModelAndView brands() {
        return feedback(
                brandService.list());
    }

    @RequestMapping(value = "/brands")
    public ModelAndView brands(String brandQo) {
        return feedback(
                brandService.pageUser(parseModel(brandQo, new BrandQo())));
    }

    // user
    @RequestMapping(value = "/profile")
    public ModelAndView profile() {
        return feedback(userService.profile());
    }

    @RequestMapping(value = "/touch_profile")
    @Touchable(true)
    public ModelAndView touchProfile() {
        return feedback(userService.profile());
    }

    @RequestMapping(value = "/save_profile")
    public ModelAndView saveProfile(String user) {
        userService.saveProfile(parseModel(user, new User()));
        return feedback(null);
    }

    @RequestMapping(value = "/signup")
    @RequiredAdminType(AdminType.NONE)
    public ModelAndView signup(String user, String valCode) {
        userService.signup(parseModel(user, new User()), parseModel(valCode, new ValCode()));
        return feedback(null);
    }

    @RequestMapping(value = "/signin")
    @RequiredAdminType(AdminType.NONE)
    public ModelAndView signin(String valCode) {
        return feedback(userService.signin(parseModel(valCode, new ValCode())));
    }

    @RequestMapping(value = "/is_collected")
    public ModelAndView isCollected(Byte collectType, Integer collectId) {
        return feedback(userCollectService.isCollected(collectType, collectId));
    }

    @RequestMapping(value = "/collect")
    public ModelAndView collect(Byte collectType, Integer collectId) {
        return feedback(userCollectService.collect(collectType, collectId));
    }

    @RequestMapping(value = "/my_collects")
    public ModelAndView myCollects(String userCollectQo) {
        return feedback(userCollectService.userCollects(parseModel(userCollectQo, new UserCollectQo())));
    }

    // 此方法可使用collect实现，仅为模拟wrtiable示例
    @RequestMapping(value = "/remove_my_collect")
    public ModelAndView removeMyCollect(Integer id) {
        userCollectService.removeMyCollect(id);
        return feedback();
    }

}
