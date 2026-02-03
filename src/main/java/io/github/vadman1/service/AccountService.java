package io.github.vadman1.service;

import io.github.vadman1.config.AccountServiceProperties;
import io.github.vadman1.exception.InvalidAmountException;
import io.github.vadman1.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountService {

    private final AccountServiceProperties accountServiceProperties;

    public AccountService(
            AccountServiceProperties accountServiceProperties
    ) {
        this.accountServiceProperties = accountServiceProperties;
    }

    public Account createAccount(long userId) {
        return new Account(userId, accountServiceProperties.getDefaultAmount());
    }

    public void deposit(Account account, double amount) {
        validateAmount(amount);

        account.deposit(amount);
    }

    public void withdraw(Account account, double amount) {
        validateAmount(amount);

        account.withdraw(amount);
    }

    public void transfer(Account account1, Account account2, double amount) {
        validateAmount(amount);

        double commission = accountServiceProperties.getTransferCommission();
        if (account1.getUserId() == account2.getUserId()) {
            commission = 0;
        }

        account1.withdraw(amount);
        account2.deposit(amount - commission);
    }

    public void close(Account account, Account firstAccount) {
        double amount = account.getMoneyAmount();
        transfer(account, firstAccount, amount);
    }

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Передана некорректная сумма: " + amount);
        }
    }
}