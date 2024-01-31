package me.home.controller;

import me.home.enums.BillType;
import me.home.enums.ErrorCode;
import me.home.model.Bill;
import me.home.model.BillAutoPay;
import me.home.model.BillStorage;
import java.util.List;
import java.util.Date;

public class BillController {
    public Bill create(BillType type, double amount, Date dueDate, String provider){
        Bill bill = null;
        try {
            bill = BillStorage.createBill(type, amount, dueDate, provider);
        } catch (Exception e){
            e.printStackTrace();
        }
        return bill;
    }
    public ErrorCode delete(long billId){
        ErrorCode errorCode;
        try {
            BillStorage.delete(billId);
            BillAutoPay.off(billId);
            errorCode = ErrorCode.OK;
        } catch (Exception e){
            errorCode = ErrorCode.UNKNOWN;
            e.printStackTrace();
        }
        return errorCode;
    }

    public List<Bill> list(){
        return BillStorage.getBills();
    }
    public List<Bill> search(String provider){
        return BillStorage.searchByProvider(provider);
    }
    public List<Bill> listDueDate(){
        return BillStorage.getNotPaidBillsOrderByDueDateAsc();
    }
    public List<Bill> listPayment(){
        return BillStorage.getPaymentBills();
    }
}
