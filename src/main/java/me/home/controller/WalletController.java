package me.home.controller;

import me.home.model.Wallet;
import me.home.model.exception.AmountInvalidException;
import me.home.enums.ErrorCode;

public class WalletController {
    public ErrorCode cashIn(Wallet wallet, double amount){
        ErrorCode errorCode;
        try{
            wallet.add(amount);
            errorCode = ErrorCode.OK;
        } catch (AmountInvalidException e){
            errorCode = ErrorCode.AMOUNT_INVALID;
            e.printStackTrace();
        } catch (Exception e){
            errorCode = ErrorCode.UNKNOWN;
            e.printStackTrace();
        }
        return errorCode;
    }
    public double getBalance(Wallet wallet){
        return wallet.getBalance();
    }
}
