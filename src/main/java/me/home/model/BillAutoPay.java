package me.home.model;

import me.home.enums.BillState;
import me.home.model.exception.BillNotExistsException;
import me.home.model.exception.BillPaidException;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public class BillAutoPay {
    private static final Map<Long, Date> bills = new ConcurrentSkipListMap<>();

    public static void setAt(long billId, Date payDate) throws Exception {
        Bill bill = BillStorage.get(billId);
        if (bill == null) throw new BillNotExistsException("Sorry! Not found a bill with such id");
        if (bill.getState() != BillState.NOT_PAID) throw new BillPaidException("Sorry! This bill is/has been paid");
        bills.put(billId, payDate);
    }

    public static void off(long billId) {
        bills.remove(billId);
    }

    public static Map<Long, Date> getBills() {
        return bills;
    }
}
