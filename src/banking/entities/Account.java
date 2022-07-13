package banking.entities;

import java.util.Random;

public class Account {

    private final String number;
    private final String pin;
    private int balance;

    public static Account getNewAccount() {
        return new Account(Number.numberGenerator(), pinGenerator(), 0);
    }

    public Account(String number, String pin, int balance) {
        this.number = number;
        this.pin = pin;
        this.balance = balance;
    }

    public static Account empty() {
        return new Account("", "", 0);
    }

    public boolean isEmpty() {
        return this.pin.isEmpty() && this.number.isEmpty();
    }

    public String getPin() {
        return pin;
    }

    public int getBalance() {
        return balance;
    }

    public void addToBalance(long amount) {
        this.balance += amount;
    }

    public String getNumber() {
        return number;
    }

    private static String pinGenerator() {
        Random random = new Random();
        return "" + (random.nextInt(8999) + 1000);
    }

    public static class Builder {
        private String number;
        private String pin;
        private int balance;

        public Builder addNumber(String number) {
            this.number = number;
            return this;
        }

        public Builder addPin(String pin) {
            this.pin = pin;
            return this;
        }

        public Builder addBalance(int balance) {
            this.balance = balance;
            return this;
        }

        public Account build() {
            return new Account(number, pin, balance);
        }

    }

}
