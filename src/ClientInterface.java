import exceptions.*;
import model.BankAccount;
import model.CheckingAccount;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientInterface {

    private final List<BankAccount> accounts = new ArrayList<>();

    private final Scanner scanner = new Scanner(System.in);

    private final List<String> options = List.of(
            "1 - Create account",
            "2 - Delete account",
            "3 - Make account deposit",
            "4 - Make account withdrawal",
            "5 - Print account info",
            "6 - Exit"
    );

    public void start() throws IOException {
        String option;
        while (true) {
            printMainMenu();
            option = scanner.next();

            try {
                int choice = Integer.parseInt(option);
                switch (choice) {
                    case 1:
                        addNewAccount();
                        break;
                    case 2:
                        deleteAccount();
                        break;
                    case 3:
                        deposit();
                        break;
                    case 4:
                        withdraw();
                        break;
                    case 5:
                        printInfo();
                        break;
                    case 6:
                        return;
                    default:
                        System.err.println("Error: Please enter a valid choice (1 thru 6).\n");
                }
            } catch (NumberFormatException e) {
                System.err.println("Error: Menu option must be a number (1 thru 6).\n");
            } catch (AccountException e) {
                System.err.println("Error: " + e.getMessage() + "\n");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n****************");
        System.out.println("Banking system: ");
        System.out.println("****************");
        options.forEach(System.out::println);
        System.out.print("\n>>> Choose your option: ");
    }

    private void addNewAccount() {
        System.out.println("\n****************");
        System.out.println("Creating new account: ");
        System.out.println("****************");

        System.out.print(">>> Enter customer name: ");
        var name = scanner.next();
        System.out.print(">>> Enter user id: ");
        var userIdStr = scanner.next();
        System.out.print(">>> Enter user password: ");
        String password = scanner.next();
        System.out.print(">>> Confirm password: ");
        String confirmPassword = scanner.next();

        Long userId = parseUserId(userIdStr);

        if (findAccountById(userId) != null) {
            throw new DuplicateUserIdException("Account with userId " + userId + " already exists.");
        }

        if (!password.equals(confirmPassword)) {
            throw new PasswordMismatchException("Password and confirmation password do not match.");
        }

        BankAccount newAccount = new CheckingAccount(name, userId, password);
        accounts.add(newAccount);

        System.out.println("\nAccount successfully created!");
    }

    private void deleteAccount() {
        System.out.println("\n****************");
        System.out.println("Deleting account: ");
        System.out.println("****************");

        System.out.print(">>> Enter user id: ");
        var userIdStr = scanner.next();
        System.out.print(">>> Enter user password: ");
        var password = scanner.next();

        BankAccount account = authenticate(userIdStr, password);

        if (account.getAccountBalance() != 0.0) {
            throw new NonZeroBalanceException(
                    "Cannot delete account with non-zero balance (" + account.getAccountBalance() + ")."
            );
        }

        accounts.remove(account);
        System.out.println("\nAccount successfully deleted!");
    }

    private void deposit() {
        System.out.println("\n****************");
        System.out.println("Making a deposit: ");
        System.out.println("****************");

        System.out.print(">>> Enter user id: ");
        var userIdStr = scanner.next();
        System.out.print(">>> Enter user password: ");
        var password = scanner.next();
        System.out.print(">>> Enter amount: ");
        var amountStr = scanner.next();

        BankAccount account = authenticate(userIdStr, password);
        double amount = parseAmount(amountStr);

        account.deposit(amount);
        System.out.println("\nDeposit completed successfully! New balance: " + account.getAccountBalance());
    }

    private void withdraw() {
        System.out.println("\n****************");
        System.out.println("Making a withdrawal: ");
        System.out.println("****************");

        System.out.print(">>> Enter user id: ");
        var userIdStr = scanner.next();
        System.out.print(">>> Enter user password: ");
        var password = scanner.next();
        System.out.print(">>> Enter amount: ");
        var amountStr = scanner.next();

        BankAccount account = authenticate(userIdStr, password);
        double amount = parseAmount(amountStr);

        account.withdraw(amount);
        System.out.println("\nWithdrawal completed successfully! New balance: " + account.getAccountBalance());
    }

    private void printInfo() {
        System.out.println("\n****************");
        System.out.println("Printing account info: ");
        System.out.println("****************");

        System.out.print(">>> Enter user id: ");
        var userIdStr = scanner.next();
        System.out.print(">>> Enter user password: ");
        var password = scanner.next();

        BankAccount account = authenticate(userIdStr, password);

        System.out.println("\nAccount info: ");
        System.out.println(" - customer name: " + account.getCustomerName());
        System.out.println(" - account balance: " + account.getAccountBalance());
        System.out.println(" - account type: Checking");
        if (account instanceof CheckingAccount) {
            System.out.println(" - account daily withdrawal limit: "
                    + ((CheckingAccount) account).getDailyWithdrawalLimit());
        }

        System.out.print("\nPress Enter to return to main menu...");

        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BankAccount authenticate(String userIdStr, String password) {
        Long userId = parseUserId(userIdStr);
        BankAccount account = findAccountById(userId);
        if (account == null) {
            throw new AccountNotFoundException("Account with userId " + userId + " was not found.");
        }
        if (!account.getUserPassword().equals(password)) {
            throw new AuthenticationException("Incorrect password for userId " + userId + ".");
        }
        return account;
    }

    private BankAccount findAccountById(Long userId) {
        for (BankAccount account : accounts) {
            if (account.getUserId().equals(userId)) {
                return account;
            }
        }
        return null;
    }

    private Long parseUserId(String userIdStr) {
        try {
            return Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            throw new InvalidInputFormatException("User ID must be a valid integer number.");
        }
    }

    private double parseAmount(String amountStr) {
        try {
            return Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            throw new InvalidInputFormatException("Amount must be a valid number.");
        }
    }
}