package com.example.demo.api.bill.entity;

public enum BillStatus {
    NOPAY(1), PAIED(2), CALLOFF(3);

    private byte value;

    private BillStatus(int value) {
        this.value = (byte) value;
    }

    public byte value() {
        return value;
    }

    public BillStatus findCoupon(Byte value) {
        if (value == null) {
            return null;
        }
        for (BillStatus billStatus : BillStatus.values()) {
            if (value.equals(billStatus.value)) {
                return billStatus;
            }
        }
        return null;
    }

}
