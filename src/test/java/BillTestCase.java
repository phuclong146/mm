import me.home.controller.BillController;
import me.home.enums.BillType;
import me.home.enums.ErrorCode;
import me.home.model.Bill;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BillTestCase {
    @Test //RED
    public void test_01_create() throws ParseException {
        Bill expectedBill = new Bill(1, BillType.ELECTRIC, 100000, new SimpleDateFormat("dd/MM/yyyy").parse("31/01/2024"), "EVN HCM");
        BillController cc = new BillController();
        Bill bill = cc.create(BillType.ELECTRIC, 100000, new SimpleDateFormat("dd/MM/yyyy").parse("31/01/2024"), "EVN HCM");
        Assert.assertEquals(expectedBill, bill);

    }

    @Test //GREEN
    public void test_02_create_error() throws ParseException {
        BillController cc = new BillController();
        Bill bill = cc.create(BillType.ELECTRIC, -100000, new SimpleDateFormat("dd/MM/yyyy").parse("31/01/2024"), "EVN HCM");
        Assert.assertNull(bill);
    }

    @Test //RED
    public void test_03_list() {
        BillController cc = new BillController();
        List<Bill> bills = cc.list();
        Assert.assertEquals(1, bills.size());
    }

    @Test //RED
    public void test_04_listDueDate() {
        BillController cc = new BillController();
        List<Bill> bills = cc.listDueDate();
        Assert.assertEquals(1, bills.size());
    }

    @Test //RED
    public void test_05_listPayment() {
        BillController cc = new BillController();
        List<Bill> bills = cc.listPayment();
        Assert.assertEquals(0, bills.size());
    }

    @Test //RED
    public void test_06_search() {
        BillController cc = new BillController();
        List<Bill> bills = cc.search("");
        Assert.assertEquals(1, bills.size());
    }

    @Test //RED
    public void test_07_delete() {
        BillController cc = new BillController();
        ErrorCode errorCode = cc.delete(1);
        Assert.assertEquals(ErrorCode.OK, errorCode);
    }
}
