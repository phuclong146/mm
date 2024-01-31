package me.home.model;

import me.home.model.exception.AmountInvalidException;
import me.home.model.exception.BalanceNotEnoughException;

public class Wallet {
    private long walletId;
    private double balance;

    public Wallet(long walletId){
        this.walletId = walletId;
    }

    public double getBalance() {
        return balance;
    }
    public void add(double amount) throws Exception {
        if (amount < 0) throw new AmountInvalidException("The amount added to the wallet should not be negative");
        this.balance += amount;
    }
    public void spend(double amount) throws Exception {
        if (amount < 0) throw new AmountInvalidException("The amount added to the wallet should not be negative");
        if (amount > this.balance) throw new BalanceNotEnoughException("The amount spent should not be greater than the existing balance");
        this.balance -= amount;
    }
}
