package me.home.controller;

import me.home.enums.ErrorCode;
import me.home.model.BillAutoPay;
import me.home.model.exception.BillNotExistsException;
import me.home.model.exception.BillPaidException;

import java.util.Date;

public class AutoPayController {
    public ErrorCode scheduleOn(long billId, Date payDate) {
        ErrorCode errorCode;
        try {
            BillAutoPay.setAt(billId, payDate);
            errorCode = ErrorCode.OK;
        } catch (BillNotExistsException e) {
            errorCode = ErrorCode.BILL_NOT_EXISTS;
            e.printStackTrace();
        } catch (BillPaidException e) {
            errorCode = ErrorCode.BILL_PAID;
            e.printStackTrace();
        } catch (Exception e) {
            errorCode = ErrorCode.UNKNOWN;
            e.printStackTrace();
        }
        return errorCode;
    }

    public ErrorCode scheduleOff(long billId) {
        ErrorCode errorCode;
        try {
            BillAutoPay.off(billId);
            errorCode = ErrorCode.OK;
        } catch (Exception e) {
            errorCode = ErrorCode.UNKNOWN;
            e.printStackTrace();
        }
        return errorCode;
    }
}
