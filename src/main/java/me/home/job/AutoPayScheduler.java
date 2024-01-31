package me.home.job;

import me.home.controller.PayController;
import me.home.model.BillAutoPay;
import me.home.model.BillStorage;
import me.home.model.WalletStorage;
import me.home.view.ControlShell;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AutoPayScheduler {
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public static void run() {
        scheduler.scheduleAtFixedRate(() -> {
            // 1. get all auto-pay bills
            List<Long> billIds = new ArrayList<>(BillAutoPay.getBills().keySet());
            // 2. filter and sort by due date
            BillStorage.filterNotPaidBillsOrderByDueDateAsc(billIds).forEach(bill -> {
                // 3. process auto-pay for each
                ControlShell.payController.pay(WalletStorage.getMyWallet(), bill);
            });
        }, 5, 5, TimeUnit.MINUTES);
    }
}
