package io.github.vadman1.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private long id;
    private String login;
    private List<Account> accountList = new ArrayList<>();

    private static long lastId = 1;

    public User(String login) {
        this.id = lastId++;
        this.login = login;
    }

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void addAccount(Account account) {
        this.getAccountList().add(account);
    }

    public void removeAccount(Account account) {
        this.getAccountList().remove(account);
    }

    public boolean isContainsOneAccount() {
        return accountList.size() <= 1;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", accountList=" + accountList +
                '}';
    }
}