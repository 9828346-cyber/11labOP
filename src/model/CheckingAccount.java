package model;

import exceptions.DailyLimitExceededException;
import exceptions.InsufficientFundsException;
import exceptions.InvalidAmountException;

public class CheckingAccount extends BankAccount {
    private double dailyWithdrawalLimit = 300.0;

    public CheckingAccount(String customerName, String accountNumber, Long userId, String userPassword) {
        super(customerName, accountNumber, userId, userPassword);
    }

    public CheckingAccount(String customerName, Long userId, String userPassword) {
        super(customerName, userId, userPassword);
    }

    public double getDailyWithdrawalLimit() {
        return dailyWithdrawalLimit;
    }

    public void setDailyWithdrawalLimit(double dailyWithdrawalLimit) {
        if (dailyWithdrawalLimit <= 0) {
            throw new InvalidAmountException("Daily withdrawal limit must be greater than 0.");
        }
        this.dailyWithdrawalLimit = dailyWithdrawalLimit;
    }

    @Override
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be greater than 0.");
        }
        setAccountBalance(getAccountBalance() + amount);
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than 0.");
        }
        if (amount > dailyWithdrawalLimit) {
            throw new DailyLimitExceededException(
                    "Withdrawal amount (" + amount + ") exceeds daily limit (" + dailyWithdrawalLimit + ")."
            );
        }
        if (amount > getAccountBalance()) {
            throw new InsufficientFundsException(
                    "Insufficient funds. Current balance: " + getAccountBalance()
            );
        }
        setAccountBalance(getAccountBalance() - amount);
    }
}