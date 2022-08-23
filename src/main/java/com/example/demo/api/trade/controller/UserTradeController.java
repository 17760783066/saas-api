package com.example.demo.api.trade.controller;

import com.example.demo.api.trade.entity.TradeWrapOption;
import com.example.demo.api.trade.model.Trade;
import com.example.demo.api.trade.qo.TradeQo;
import com.example.demo.api.trade.service.TradeService;
import com.example.demo.api.user.authority.UserContexts;
import com.example.demo.common.controller.BaseController;
import com.example.demo.common.controller.auth.AdminType;
import com.example.demo.common.controller.auth.RequiredAdminType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/usr/trade")
@RequiredAdminType(AdminType.USER)
public class UserTradeController extends BaseController{
    @Autowired
    private TradeService tradeService;

    @RequestMapping("/save")
    public ModelAndView save(String trade, Integer total,Integer totalAmoun ) {
        return feedback(tradeService.save(parseModel(trade, new Trade()), total,totalAmoun));
    }
    @RequestMapping("/trade")
    public ModelAndView trade(Integer id) throws Exception {
        return feedback(tradeService.findById(id));
    }
    @RequestMapping("/pay")
    public ModelAndView pay(Integer id, Byte type) {
        tradeService.updateType(id, type);
        return feedback();
    }
    @RequestMapping("/trades")
    public ModelAndView trades(String tradeQo) {
        TradeQo qo = parseModel(tradeQo, new TradeQo());
        qo.setUserId(UserContexts.requestUserId());
        return feedback(tradeService.trades(parseModel(tradeQo, new TradeQo()), TradeWrapOption.getUserListInstance()));
    }

}
