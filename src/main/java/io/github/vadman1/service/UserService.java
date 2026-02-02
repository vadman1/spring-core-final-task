package io.github.vadman1.service;

import io.github.vadman1.exception.*;
import io.github.vadman1.model.Account;
import io.github.vadman1.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserService {

    private final AccountService accountService;

    public UserService(
            AccountService accountService
    ) {
        this.accountService = accountService;
    }

    private final List<User> users = new ArrayList<>();

    public User createUser(String login) {
        boolean isLoginExists = users.stream()
                .map(User::getLogin)
                .collect(Collectors.toSet())
                .contains(login);

        if (isLoginExists) {
            throw new UserAlreadyExistsException("Пользователь с логином " + login +" уже существует.");
        }

        User user = new User(login);
        Account account = accountService.createAccount(user.getId());
        addAccount(user, account);
        users.add(user);
        return user;
    }

    public User getUser(long id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("Пользователь по id " + id + " не найден!"));
    }

    public List<User> getUsers() {
        return users;
    }

    public Account getAccount(long id) {
        return users.stream()
                .flatMap(user -> user.getAccountList().stream())
                .filter(acc -> acc.getId() == id)
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException("Счёт по id " + id + " не найден!"));
    }

    public void addAccount(User user, Account account) {
        user.addAccount(account);
    }

    public void closeAccount(long id) {
        Account account = getAccount(id);
        long userId = account.getUserId();
        User user = getUser(userId);

        if (user.isContainsOneAccount()) {
            throw new SingleAccountClosureException("Пользователь c ID " + userId + " содержит один счёт!");
        }

        if (user.getAccountList().getFirst().getId() == id) {
            throw new PrimaryAccountClosureException("Попытка удалить первый счёт с ID " + id +
                    " у пользователь c ID " + userId);
        }

        Account firstAccount = user.getAccountList().getFirst();

        accountService.close(account, firstAccount);

        user.removeAccount(account);
    }
}