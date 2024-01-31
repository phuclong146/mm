package me.home.model;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class WalletStorage {
    static long myWalletId = 1;
    private static final Map<Long, Wallet> wallets = new ConcurrentSkipListMap<>();
    static {
        wallets.put(myWalletId, new Wallet(myWalletId));
    }
    public static Wallet getMyWallet(){
        return wallets.get(myWalletId);
    }
}
