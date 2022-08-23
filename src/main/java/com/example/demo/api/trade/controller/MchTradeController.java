package com.example.demo.api.trade.controller;

import com.example.demo.api.merchant.authority.MerchantAdminContexts;
import com.example.demo.api.trade.entity.TradeWrapOption;
import com.example.demo.api.trade.qo.TradeQo;
import com.example.demo.api.trade.service.TradeService;
import com.example.demo.common.controller.BaseController;
import com.example.demo.common.controller.auth.AdminType;
import com.example.demo.common.controller.auth.RequiredAdminType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/merchant/trade")
@RequiredAdminType(AdminType.MERCHANT)
public class MchTradeController extends BaseController {

    @Autowired
    private TradeService tradeService;

    @RequestMapping("/trades")
    public ModelAndView trades(String tradeQo) {
        return feedback(tradeService.trades(parseModel(tradeQo, new TradeQo()), TradeWrapOption.getMerchantListInstance()));
    }

    @RequestMapping("/trade")
    public ModelAndView trade(Integer id) {
        return feedback(tradeService.warpUserFindById(id));
    }

    @RequestMapping("/send")
    public ModelAndView updateType(Integer id, Byte type) {
        tradeService.updateType(id, type);
        return feedback();
    }

}
