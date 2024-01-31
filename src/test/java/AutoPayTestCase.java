import me.home.controller.AutoPayController;
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
public class AutoPayTestCase {
    @Test //RED
    public void test_01_scheduleOn() throws ParseException {
        // 1. create a bill
        BillController bc = new BillController();
        Bill bill = bc.create(BillType.ELECTRIC, 100000, new SimpleDateFormat("dd/MM/yyyy").parse("31/01/2024"), "EVN HCM");
        // 2. turn on auto-pay
        AutoPayController atpc = new AutoPayController();
        ErrorCode errorCode = atpc.scheduleOn(bill.getBillId(), new SimpleDateFormat("dd/MM/yyyy").parse("31/01/2024"));
        Assert.assertEquals(ErrorCode.OK, errorCode);
    }

    @Test //GREEN
    public void test_02_scheduleOn_error_bill_not_exists() throws ParseException {
        AutoPayController atpc = new AutoPayController();
        ErrorCode errorCode = atpc.scheduleOn(20000, new SimpleDateFormat("dd/MM/yyyy").parse("31/01/2024"));
        Assert.assertEquals(ErrorCode.BILL_NOT_EXISTS, errorCode);
    }

    @Test //GREEN
    public void test_03_scheduleOn_error_pay_bill_paid() throws ParseException {
        // 1. cash in to my wallet
        WalletController wc = new WalletController();
        wc.cashIn(WalletStorage.getMyWallet(), 1000000d);
        // 2. pay now
        PayController pc = new PayController();
        pc.pay(WalletStorage.getMyWallet(), 2);
        // 3. turn on auto-pay
        AutoPayController atpc = new AutoPayController();
        ErrorCode errorCode = atpc.scheduleOn(2, new SimpleDateFormat("dd/MM/yyyy").parse("31/01/2024"));
        Assert.assertEquals(ErrorCode.BILL_PAID, errorCode);
    }

    @Test //RED
    public void test_04_scheduleOff() {
        AutoPayController atpc = new AutoPayController();
        ErrorCode errorCode = atpc.scheduleOff(3);
        Assert.assertEquals(ErrorCode.OK, errorCode);
    }
}
