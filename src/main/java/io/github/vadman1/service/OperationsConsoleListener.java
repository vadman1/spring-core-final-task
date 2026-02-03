package io.github.vadman1.service;

import io.github.vadman1.exception.*;
import io.github.vadman1.model.Account;
import io.github.vadman1.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class OperationsConsoleListener implements Runnable {

    private final UserService userService;
    private final AccountService accountService;

    private final Scanner scanner = new Scanner(System.in);

    private static final String MENU = """
            -ACCOUNT_CREATE
            -SHOW_ALL_USERS
            -ACCOUNT_CLOSE
            -ACCOUNT_WITHDRAW
            -ACCOUNT_DEPOSIT
            -ACCOUNT_TRANSFER
            -USER_CREATE
            """;

    public OperationsConsoleListener(
            UserService userService,
            AccountService accountService
    ) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("Выберите действие:");
            System.out.println(MENU);

            String input = scanner.nextLine();
            Command command;

            try {
                command = Command.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Введена некорректная команда: " + input);
                continue;
            }

            switch (command) {
                case USER_CREATE -> {
                    String login = readString("Введите логин: ");

                    try {
                        User user = userService.createUser(login);
                        System.out.println("Пользователь создан: " + user);
                    } catch (UserAlreadyExistsException e) {
                        System.out.println("Ошибка при выполнении команды " + command + ": " + e.getMessage());
                    }
                }
                case SHOW_ALL_USERS -> {
                    List<User> users = userService.getUsers();

                    if (users.isEmpty()) {
                        System.out.println("Список пользователей пока пуст.");
                    } else {
                        System.out.println(users);
                    }
                }
                case ACCOUNT_CREATE -> {
                    long id = readLong("Введите ID пользователя: ");

                    try {
                        User user = userService.getUser(id);
                        Account account = accountService.createAccount(user.getId());
                        userService.addAccount(user, account);
                        System.out.println("Создан новый счёт с ID: " + account.getId() +
                                " для пользователя: " + user.getLogin());
                    } catch (UserNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case ACCOUNT_CLOSE -> {
                    long id = readLong("Введите ID счёта: ");

                    try {
                        userService.closeAccount(id);
                        System.out.println("Счёт с ID " + id + " был закрыт.");
                    } catch (UserNotFoundException | AccountNotFoundException |
                             SingleAccountClosureException | PrimaryAccountClosureException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case ACCOUNT_DEPOSIT -> {
                    long id = readLong("Введите ID счёта:");

                    Account account;
                    try {
                        account = userService.getAccount(id);
                    } catch (AccountNotFoundException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }

                    double amount = readDouble("Введите сумму для внесения:");

                    try {
                        accountService.deposit(account, amount);
                        System.out.println("Сумма " + amount + " внесена на счёт с ID: " + id);
                    } catch (InvalidAmountException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case ACCOUNT_WITHDRAW -> {
                    long id = readLong("Введите ID счёта для снятия:");

                    Account account;
                    try {
                        account = userService.getAccount(id);
                    } catch (AccountNotFoundException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }

                    double amount = readDouble("Введите сумму для снятия:");

                    try {
                        accountService.withdraw(account, amount);
                        System.out.println("Сумма " + amount + " снята со счёта с ID: " + id +
                                ". Текущий баланс: " + account.getMoneyAmount());
                    } catch (InvalidAmountException | InsufficientFundsException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case ACCOUNT_TRANSFER -> {
                    long idSource = readLong("Введите ID счёта отправителя:");
                    Account accountSource;
                    try {
                        accountSource = userService.getAccount(idSource);
                    } catch (AccountNotFoundException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }

                    long idTarget = readLong("Введите ID счёта получателя:");
                    Account accountTarget;
                    try {
                        accountTarget = userService.getAccount(idTarget);
                    } catch (AccountNotFoundException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }

                    double amount = readDouble("Введите сумму для перевода:");

                    try {
                        accountService.transfer(accountSource, accountTarget, amount);
                        System.out.println("Сумма " + amount + " переведена счёта ID " + idSource + " на счёт ID " + idTarget);
                    } catch (InvalidAmountException | InsufficientFundsException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }

            System.out.println();
        }
    }

    private String readString(String message) {
        System.out.println(message);

        return scanner.nextLine();
    }

    private Long readLong(String message) {
        while (true) {
            System.out.println(message);

            String input = scanner.nextLine();
            try {
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Введено недопустимое значение: " + input + ". Значение должно быть целым числом.");
            }
        }
    }

    private Double readDouble(String message) {
        while (true) {
            System.out.println(message);

            String input = scanner.nextLine();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Введено недопустимое значение: " + input + ". Значение должно быть вещественным числом.");
            }
        }
    }

    private enum Command {
        ACCOUNT_CREATE,
        SHOW_ALL_USERS,
        ACCOUNT_CLOSE,
        ACCOUNT_WITHDRAW,
        ACCOUNT_DEPOSIT,
        ACCOUNT_TRANSFER,
        USER_CREATE
    }
}