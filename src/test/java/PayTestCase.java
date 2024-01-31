import me.home.controller.BillController;
import me.home.controller.PayController;
import me.home.controller.WalletController;
import me.home.enums.BillType;
import me.home.enums.ErrorCode;
import me.home.model.Bill;
import me.home.model.WalletStorage;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PayTestCase {
    @Test //RED
    public void test_01_pay() throws ParseException {
        // 1. create a bill
        BillController bc = new BillController();
        Bill bill = bc.create(BillType.ELECTRIC, 100000, new SimpleDateFormat("dd/MM/yyyy").parse("31/01/2024"), "EVN HCM");
        // 2. cash in to my wallet
        WalletController wc = new WalletController();
        wc.cashIn(WalletStorage.getMyWallet(), 1000000d);
        // 3. pay now
        PayController pc = new PayController();
        ErrorCode errorCode = pc.pay(WalletStorage.getMyWallet(), bill);
        Assert.assertEquals(ErrorCode.OK, errorCode);
    }

    @Test //GREEN
    public void test_02_pay_error_bill_not_exists() {
        PayController pc = new PayController();
        ErrorCode errorCode = pc.pay(WalletStorage.getMyWallet(), 1000);
        Assert.assertEquals(ErrorCode.BILL_NOT_EXISTS, errorCode);
    }

    @Test //GREEN
    public void test_03_pay_error_bill_paid() {
        PayController pc = new PayController();
        ErrorCode errorCode = pc.pay(WalletStorage.getMyWallet(), 2);
        Assert.assertEquals(ErrorCode.BILL_PAID, errorCode);
    }

    @Test //GREEN
    public void test_04_pay_error_balance_not_enough() throws ParseException {
        BillController bc = new BillController();
        Bill bill = bc.create(BillType.ELECTRIC, 20000000, new SimpleDateFormat("dd/MM/yyyy").parse("31/01/2024"), "EVN HCM");

        PayController pc = new PayController();
        ErrorCode errorCode = pc.pay(WalletStorage.getMyWallet(), bill);
        Assert.assertEquals(ErrorCode.BALANCE_NOT_ENOUGH, errorCode);
    }
}
