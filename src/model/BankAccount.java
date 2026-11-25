package model;

import exceptions.InvalidPasswordException;
import exceptions.NegativeBalanceException;

import java.util.UUID;

public abstract class BankAccount {
    private String customerName;
    private String accountNumber;
    private Long userId;
    private String userPassword;
    private Double accountBalance;

    public BankAccount(String customerName, String accountNumber, Long userId, String userPassword) {
        setCustomerName(customerName);
        setAccountNumber(accountNumber);
        setUserId(userId);
        setUserPassword(userPassword);
        this.accountBalance = 0.0;
    }

    public BankAccount(String customerName, Long userId, String userPassword) {
        this(customerName, UUID.randomUUID().toString(), userId, userPassword);
    }

    public abstract void deposit(double amount);

    public abstract void withdraw(double amount);

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        if (userPassword == null || userPassword.length() < 8 || !userPassword.matches(".*\\d.*")) {
            throw new InvalidPasswordException(
                    "Password must be at least 8 characters long and contain at least one digit."
            );
        }
        this.userPassword = userPassword;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        if (accountBalance == null || accountBalance < 0) {
            throw new NegativeBalanceException("Account balance cannot be negative.");
        }
        this.accountBalance = accountBalance;
    }
}