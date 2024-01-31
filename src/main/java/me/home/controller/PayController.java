package me.home.controller;

import me.home.enums.BillState;
import me.home.enums.ErrorCode;
import me.home.model.Bill;
import me.home.model.BillAutoPay;
import me.home.model.BillStorage;
import me.home.model.Wallet;
import me.home.model.exception.AmountInvalidException;
import me.home.model.exception.BalanceNotEnoughException;

import java.util.List;

public class PayController {
    public ErrorCode pay(Wallet wallet, long billId) {
        ErrorCode errorCode;
        Bill bill = BillStorage.get(billId);
        if (bill != null) {
            errorCode = pay(wallet, bill);
        } else
            errorCode = ErrorCode.BILL_NOT_EXISTS;
        return errorCode;
    }

    /**
     * I use "synchronized" here such as simple solution for race condition,
     * when has many threads/processes concurrent pay on one bill.
     * Race condition must be other solutions.
     * @param wallet
     * @param bill
     * @return
     */
    public synchronized ErrorCode pay(Wallet wallet, Bill bill) {
        ErrorCode errorCode = null;
        // 1. check bill not paid or not pending
        if (bill.getState() == BillState.NOT_PAID) {
            // 2. check balance
            if (wallet.getBalance() >= bill.getAmount()) {
                // 3. pay now
                try {
                    // 3.1 mark bill state to pending
                    bill.setState(BillState.PENDING);
                    // 3.2 spend wallet
                    wallet.spend(bill.getAmount());
                    // 3.3 set bill state to paid
                    bill.setState(BillState.PROCESSED);
                    errorCode = ErrorCode.OK;
                } catch (AmountInvalidException e) {
                    errorCode = ErrorCode.AMOUNT_INVALID;
                } catch (BalanceNotEnoughException e) {
                    errorCode = ErrorCode.BALANCE_NOT_ENOUGH;
                } catch (Exception e) {
                    errorCode = ErrorCode.UNKNOWN;
                    e.printStackTrace();
                } finally {
                    if (errorCode == ErrorCode.OK) {
                        // 4.1 turn off schedule auto-pay if bill paid successfully
                        BillAutoPay.off(bill.getBillId());
                    } else {
                        // 4.2 rollback if didn't spend successfully
                        bill.setState(BillState.NOT_PAID);
                    }
                }
            } else
                errorCode = ErrorCode.BALANCE_NOT_ENOUGH;
        } else
            errorCode = ErrorCode.BILL_PAID;
        return errorCode;
    }

    /**
     * In this method, I implementation other requirements of yours.
     * I don't check if my wallet balance is enough to pay my total bills,
     * but I process paying each bill to make sure more bills are paid as soon as possible.
     * @param wallet
     * @param billIds
     */
    public void pay(Wallet wallet, List<Long> billIds) {
        // 1. filler bills and sort by due date
        List<Bill> bills = BillStorage.filterNotPaidBillsOrderByDueDateAsc(billIds);
        // 2. pay now
        bills.forEach(bill -> pay(wallet, bill));
    }


}
