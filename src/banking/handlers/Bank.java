package banking.handlers;

import banking.entities.Account;
import java.util.Scanner;
import static banking.handlers.DatabaseHandler.*;
import static banking.handlers.LUHNAlgorithm.*;

public class Bank {

    public static final Scanner INPUT = new Scanner(System.in);

    public void startServices() {
        while (true) {
            printChoices();
            int status = Integer.parseInt(INPUT.nextLine());
            switch (status) {
                case 1:
                    newAccount();
                    break;
                case 2:
                    logIn();
                    break;
                case 0:
                    System.out.println("\nBye!");
                    System.exit(0);
                default:
                    break;
            }
            System.out.println();
        }
    }

    private void newAccount() {
        Account account = Account.getNewAccount();
        addData(account);
        System.out.println("\nYour card has been created\n" +
                "Your card number:\n" +
                account.getNumber() + "\n" +
                "Your card PIN:\n" +
                account.getPin());
    }

    private void logIn() {
        System.out.println("\nEnter your card number:");
        String card = INPUT.nextLine().trim();
        System.out.println("Enter your PIN:");
        String pin = INPUT.nextLine().trim();

        Account account;

        int checkSum = getCheckSum(card);
        if (checkSum != -1
                && checkSum == Integer.parseInt("" + card.charAt(card.length() - 1))
                && !(account = getAccount(card, pin)).isEmpty())
            accountQueries(account);
        else
            System.out.println("\nWrong card number or PIN!");
    }

    private void accountQueries(Account account) {
        System.out.println("\nYou have successfully logged in!");
        while (true) {
            printAccountChoices();
            int choice = Integer.parseInt(INPUT.nextLine());
            switch (choice) {
                case 0:
                    System.out.println("\nBye!");
                    System.exit(0);
                case 1:
                    System.out.println("\nBalance: " + account.getBalance());
                    break;
                case 2:
                    System.out.println("\nEnter income:");
                    account.addToBalance(Integer.parseInt(INPUT.nextLine()));
                    updateAccount(account);
                    System.out.println("Income was added!");
                    break;
                case 3:
                    System.out.println("\nTransfer");
                    makeTransfer(account);
                    break;
                case 4:
                    deleteAccount(account);
                    System.out.println("\nThe account has been closed!");
                    return;
                case 5:
                    System.out.println("\nYou have successfully logged out!");
                    return;
                default:
                    break;
            }
        }
    }

    private void makeTransfer(Account account) {
        System.out.println("Enter card number:");
        String recipientNumber = INPUT.nextLine().strip();
        int checkSum = getCheckSum(recipientNumber);
        Account recipient;
        if (checkSum == -1 ||
                checkSum != Integer
                        .parseInt("" + recipientNumber.charAt(15)))
            System.out.println("Probably you made a mistake in the card number. " +
                    "Please try again!");
        else if ((recipient = getRecipientAccount(recipientNumber)).isEmpty())
            System.out.println("Such a card does not exist.");
        else {
            System.out.println("Enter how much money you want to transfer:");
            int amount = Integer.parseInt(INPUT.nextLine());
            if (account.getBalance() < amount)
                System.out.println("Not enough money!");
            else {
                account.addToBalance(-amount);
                recipient.addToBalance(amount);
                updateAccount(account);
                updateAccount(recipient);
                System.out.println("Success!");
            }
        }
    }

    private void printChoices() {
        System.out.println("1. Create an account\n" +
                "2. Log into account\n" +
                "0. Exit");
    }

    private void printAccountChoices() {
        System.out.println("\n" +
                "1. Balance\n" +
                "2. Add income\n" +
                "3. Do transfer\n" +
                "4. Close account\n" +
                "5. Log out\n" +
                "0. Exit");
    }
}
