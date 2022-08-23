package com.example.demo.api.trade.entity;

public enum TradeType {
    NO_PAY(1),  //待支付
    NO_SEND(2), //待发货
    NO_GET(3), //待收货
    NO_COMMENT(4), //待评论
    CALL_OFF_TRADE(5),//订单取消
    FINISHED(6);//已完成
    private byte value;

    private TradeType(int value) {
        this.value = (byte) value;
    }

    public byte value() {
        return value;
    }

    public static TradeType findTrade(Byte value) {
        if (value == null) {
            return null;
        }
        for (TradeType tradeType : TradeType.values()) {
            if (value.equals(tradeType.value)) {
                return tradeType;
            }
        }
        return null;
    }


}
