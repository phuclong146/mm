package me.home.view;

import me.home.controller.AutoPayController;
import me.home.controller.BillController;
import me.home.controller.PayController;
import me.home.controller.WalletController;
import me.home.enums.BillType;
import me.home.enums.ErrorCode;
import me.home.model.Bill;
import me.home.model.BillStorage;
import me.home.model.WalletStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ControlShell {
    public static PayController payController = new PayController();

    public static void process(String data) {
        try {
            data = data.toUpperCase();
            String[] params = data.split("\\s+(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            for (int i = 0; i < params.length; i++) {
                params[i] = params[i].replace("\"", "");
            }
            String cmd = params[0].trim();
            switch (cmd) {
                case "EXIT" -> exit();
                case "HELP" -> help();
                case "CASH_IN" -> cashIn(params);
                case "BALANCE" -> balance(params);
                case "CREATE_BILL" -> createBill(params);
                case "DELETE_BILL" -> deleteBill(params);
                case "LIST_BILL" -> listBill(params);
                case "SEARCH_BILL_BY_PROVIDER" -> searchBillByProvider(params);
                case "DUE_DATE" -> listDueDateBill(params);
                case "PAY" -> pay(params);
                case "SCHEDULE" -> scheduleOn(params);
                case "LIST_PAYMENT" -> listPayment(params);
                default -> System.out.println("This command is not supported, type HELP to see a list of commands");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Wrong formatting or invalid parameter");
            help();
        }
    }

    public static void help() {
        System.out.println("CASH_IN amount: top up wallet");
        System.out.println("BALANCE: wallet balance lookup");
        System.out.println("CREATE_BILL type amount due_date(dd/MM/yyyy) provider: create a bill");
        System.out.println("DELETE_BILL billId: delete bill by billId");
        System.out.println("PAY billId1 billId2...: pay bills");
        System.out.println("SCHEDULE billId due_date(dd/MM/yyyy): schedule automatic payments");
        System.out.println("LIST_BILL: view list of all bills");
        System.out.println("SEARCH_BILL_BY_PROVIDER provider: find bills by provider");
        System.out.println("DUE_DATE: view a list of bills due");
        System.out.println("LIST_PAYMENT: view bill payment history");
        System.out.println("HELP: list of commands");
        System.out.println("EXIT: exit program");
        System.out.println("-------------------------------------------------------------------");
        System.out.println("The following types of bills are supported: ELECTRIC, WATER, INTERNET");
        System.out.println("Use the \"\" sign if the provider name has multiple words, example: \"EVN HCM\"");
        System.out.println("Type the command below...");
    }

    public static void exit() {
        System.exit(0);
    }

    private static void cashIn(String[] params) throws Exception {
        if (params.length < 2) throw new Exception("Wrong formatting or invalid parameter");
        new WalletController().cashIn(WalletStorage.getMyWallet(), Double.parseDouble(params[1]));
        System.out.println("Your available balance: " + WalletStorage.getMyWallet().getBalance());
    }

    private static void balance(String[] params) throws Exception {
        if (params.length < 1) throw new Exception("Wrong formatting or invalid parameter");
        System.out.println("Your available balance: " + WalletStorage.getMyWallet().getBalance());
    }

    private static void createBill(String[] params) throws Exception {
        if (params.length < 5) throw new Exception("Wrong formatting or invalid parameter");
        BillType type = BillType.valueOf(params[1]);
        double amount = Double.parseDouble(params[2]);
        Date dueDate = new SimpleDateFormat("dd/MM/yyyy").parse(params[3]);
        String provider = params[4];
        Bill bill = new BillController().create(type, amount, dueDate, provider);
        if (bill != null) {
            System.out.println("Successful bill creation: " + bill);
        } else {
            System.out.println("Bill creation failed!");
        }
    }

    private static void deleteBill(String[] params) throws Exception {
        if (params.length < 2) throw new Exception("Wrong formatting or invalid parameter");
        long billId = Long.parseLong(params[1]);
        ErrorCode errorCode = new BillController().delete(billId);
        if (errorCode == ErrorCode.OK) {
            System.out.println("Deleted bill: " + billId);
        } else {
            System.out.println("Bill deletion failure!");
        }
    }

    private static void pay(String[] params) throws Exception {
        if (params.length < 2) throw new Exception("Wrong formatting or invalid parameter");
        else if (params.length == 2) {
            long billId = Long.parseLong(params[1]);
            ErrorCode errorCode = payController.pay(WalletStorage.getMyWallet(), billId);
            switch (errorCode) {
                case OK -> {
                    System.out.printf("Payment has been completed for Bill with id %d.%n", billId);
                    System.out.println("Your available balance: " + WalletStorage.getMyWallet().getBalance());
                }
                case BILL_NOT_EXISTS -> System.out.println("Sorry! Not found a bill with such id");
                case BILL_PAID -> System.out.println("Sorry! This bill has been paid or is being processed.");
                case AMOUNT_INVALID -> System.out.println("Sorry! Amount is invalid.");
                case BALANCE_NOT_ENOUGH -> System.out.println("Sorry! Not enough fund to proceed with payment.");
                default -> System.out.println("Sorry! Failed bill payment.");
            }
        } else {
            List<Long> billIds = new ArrayList<>();
            for (int i = 1; i < params.length; i++) {
                billIds.add(Long.parseLong(params[i]));
            }
            payController.pay(WalletStorage.getMyWallet(), billIds);
            // so simple I don't show result when pay multi bills. I show list payment only
            System.out.println("List of your paid/pending bills:");
            BillStorage.getPaymentBills().forEach(System.out::println);
        }
    }

    private static void scheduleOn(String[] params) throws Exception {
        if (params.length < 3) throw new Exception("Wrong formatting or invalid parameter");
        long billId = Long.parseLong(params[1]);
        Date dueDate = new SimpleDateFormat("dd/MM/yyyy").parse(params[2]);
        ErrorCode errorCode = new AutoPayController().scheduleOn(billId, dueDate);
        switch (errorCode) {
            case OK -> System.out.printf("Payment for bill id %d is scheduled on %s%n", billId, params[2]);
            case BILL_NOT_EXISTS -> System.out.println("Sorry! Not found a bill with such id");
            case BILL_PAID -> System.out.println("Sorry! This invoice has been paid or is being processed.");
            default -> System.out.println("Sorry! Scheduling failed");
        }
    }

    private static void listBill(String[] params) throws Exception {
        if (params.length < 1) throw new Exception("Wrong formatting or invalid parameter");
        System.out.println("List of your bills:");
        BillStorage.getBills().forEach(System.out::println);
    }

    private static void searchBillByProvider(String[] params) throws Exception {
        if (params.length < 2) throw new Exception("Wrong formatting or invalid parameter");
        System.out.println("List of your bills:");
        BillStorage.searchByProvider(params[1]).forEach(System.out::println);
    }

    private static void listDueDateBill(String[] params) throws Exception {
        if (params.length < 1) throw new Exception("Wrong formatting or invalid parameter");
        System.out.println("Your list of bills due:");
        BillStorage.getNotPaidBillsOrderByDueDateAsc().forEach(System.out::println);
    }

    private static void listPayment(String[] params) throws Exception {
        if (params.length < 1) throw new Exception("Wrong formatting or invalid parameter");
        System.out.println("List of your paid/pending bills:");
        BillStorage.getPaymentBills().forEach(System.out::println);
    }
}
