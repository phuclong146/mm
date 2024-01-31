package me.home.model;

import me.home.enums.BillState;
import me.home.enums.BillType;
import me.home.model.exception.AmountInvalidException;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

public class BillStorage {
    private static final AtomicLong genBillId = new AtomicLong();
    private static final Map<Long, Bill> bills = new ConcurrentSkipListMap<>();

    public static Bill createBill(BillType type, double amount, Date dueDate, String provider) throws Exception {
        if (amount < 0) throw new AmountInvalidException("The amount of the bill cannot be negative");
        long billId = genBillId.incrementAndGet();
        Bill bill = new Bill(billId, type, amount, dueDate, provider);
        bills.put(billId, bill);
        return bill;
    }

    public static void delete(long billId) throws Exception {
        bills.remove(billId);
    }
    public static Bill get(long billId){
        return bills.get(billId);
    }

    public static List<Bill> getBills() {
        return new ArrayList<>(bills.values());
    }

    public static List<Bill> searchByProvider(String provider) {
        List<Bill> rs = new ArrayList<>();
        bills.forEach((id, bill) -> {
            if (bill.getProvider().contains(provider)) rs.add(bill);
        });
        return rs;
    }

    public static List<Bill> getNotPaidBillsOrderByDueDateAsc() {
        List<Bill> rs = new ArrayList<>();
        bills.forEach((id, bill) -> {
            if (bill.getState().equals(BillState.NOT_PAID)) rs.add(bill);
        });
        rs.sort(Comparator.comparing(Bill::getDueDate));
        return rs;
    }

    public static List<Bill> getPaymentBills() {
        List<Bill> rs = new ArrayList<>();
        bills.forEach((id, bill) -> {
            if (!bill.getState().equals(BillState.NOT_PAID)) rs.add(bill);
        });
        return rs;
    }
    public static List<Bill> filterNotPaidBillsOrderByDueDateAsc(List<Long> billIds) {
        List<Bill> rs = new ArrayList<>();
        bills.forEach((id, bill) -> {
            if (billIds.contains(id) && bill.getState().equals(BillState.NOT_PAID)) rs.add(bill);
        });
        rs.sort(Comparator.comparing(Bill::getDueDate));
        return rs;
    }
}
