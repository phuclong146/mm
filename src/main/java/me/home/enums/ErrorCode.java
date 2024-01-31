package me.home.enums;

public enum ErrorCode {
    OK(0),
    UNKNOWN(1),
    AMOUNT_INVALID(2),
    BALANCE_NOT_ENOUGH(3),
    BILL_NOT_EXISTS(4),
    BILL_PAID(5),
    DATE_INVALID(6);

    ErrorCode(int code) {
    }
}
