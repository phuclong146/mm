package me.home.model.exception;

public class BalanceNotEnoughException extends Exception{
    public BalanceNotEnoughException(String mess) {
        super(mess);
    }
}
