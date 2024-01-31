import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ WalletTestCase.class, BillTestCase.class, PayTestCase.class, AutoPayTestCase.class} )
public final class AllTestsSuite {
    public static void main(String[] args){
        JUnitCore.runClasses(AllTestsSuite.class);
    }
}
