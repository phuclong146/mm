import me.home.controller.WalletController;
import me.home.enums.ErrorCode;
import me.home.model.WalletStorage;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WalletTestCase {
    @Test //RED
    public void test_01_cashIn() {
        WalletController cc = new WalletController();
        ErrorCode errorCode = cc.cashIn(WalletStorage.getMyWallet(), 1000000d);
        Assert.assertEquals(ErrorCode.OK, errorCode);

    }

    @Test //GREEN
    public void test_02_cashIn_error() {
        WalletController cc = new WalletController();
        ErrorCode errorCode = cc.cashIn(WalletStorage.getMyWallet(), -100000d);
        Assert.assertEquals(ErrorCode.AMOUNT_INVALID, errorCode);
    }

    @Test //RED
    public void test_03_getBalance() {
        Double expectedBalance = 1000000d;
        WalletController cc = new WalletController();
        Double balance = cc.getBalance(WalletStorage.getMyWallet());
        Assert.assertEquals(expectedBalance, balance);
    }
}
