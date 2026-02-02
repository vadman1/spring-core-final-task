package io.github.vadman1.model;

import io.github.vadman1.exception.InsufficientFundsException;

public class Account {
    private long id;
    private long userId;
    private double moneyAmount;

    private static long lastId = 1;

    public Account(long userId, double moneyAmount) {
        this.id = lastId++;
        this.userId = userId;
        this.moneyAmount = moneyAmount;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public double getMoneyAmount() {
        return moneyAmount;
    }

    public void deposit(double amount) {
        moneyAmount += amount;
    }

    public void withdraw(double amount) {
        if (amount > moneyAmount) {
            throw new InsufficientFundsException("Ошибка: недостаточно средств на счету ID " +
                    id + ". Доступная сумма: " + moneyAmount);
        }

        moneyAmount -= amount;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", userId=" + userId +
                ", moneyAmount=" + moneyAmount +
                '}';
    }
}