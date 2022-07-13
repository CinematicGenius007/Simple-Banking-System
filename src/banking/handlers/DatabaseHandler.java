package banking.handlers;


import banking.entities.Account;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {

    private static SQLiteDataSource dataSource = null;
    private static final String INSERT_DATA = "INSERT INTO card " +
            "(number, pin, balance) VALUES (?, ?, ?);";
    private static final String UPDATE_DATA = "UPDATE card SET " +
            "balance = ? WHERE number = ?;";
    private static final String DELETE_DATA = "DELETE FROM card " +
            "WHERE number = ?;";

    public static void makeConnection(String database) {
        dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:" + database);
        try (Connection con = dataSource.getConnection()) {
            try (Statement stmt = con.createStatement()) {
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "number VARCHAR(16) NOT NULL, " +
                        "pin VARCHAR(4) NOT NULL, " +
                        "balance INTEGER DEFAULT 0" +
                        ");");
            } catch (SQLException se) {se.printStackTrace();}
        } catch (SQLException sqe) {sqe.printStackTrace();}
    }

    public static void addData(Account account) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement stmt = con.prepareStatement(INSERT_DATA)) {
                stmt.setString(1, account.getNumber());
                stmt.setString(2, account.getPin());
                stmt.setInt(3, account.getBalance());
                stmt.executeUpdate();
            } catch (SQLException sqe) {sqe.printStackTrace();}
        } catch (SQLException sqe) {sqe.printStackTrace();}

    }

    public static void updateAccount(Account account) {
        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement stmt = con.prepareStatement(UPDATE_DATA)) {
                stmt.setInt(1, account.getBalance());
                stmt.setString(2, account.getNumber());

                stmt.executeUpdate();

                con.commit();
            } catch (SQLException sqe) {
                con.rollback();
                sqe.printStackTrace();
            }
        } catch (SQLException sqe) {sqe.printStackTrace();}
    }

    public static void deleteAccount(Account account) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement stmt = con.prepareStatement(DELETE_DATA)) {
                stmt.setString(1, account.getNumber());
                stmt.executeUpdate();
            }
        } catch (SQLException sqe) {sqe.printStackTrace();}
    }

    public static List<String> getAllUniqueNumbers() {
        List<String> numbers = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            try (Statement stmt = con.createStatement()) {
                try (ResultSet rs = stmt.executeQuery("SELECT number FROM card;")) {
                    while (rs.next()) {
                        numbers.add(rs.getString("number"));
                    }
                }
            } catch (SQLException sqe) {sqe.printStackTrace();}
        } catch (SQLException sqe) {sqe.printStackTrace();}
        return numbers;
    }

    public static Account getAccount(String number, String pin) {
        try (Connection con = dataSource.getConnection()) {
            try (Statement stmt = con.createStatement()) {
                try (ResultSet rs = stmt.executeQuery("SELECT number, pin, balance FROM card;")) {
                    while (rs.next()) {
                        String cardNumber = rs.getString("number");
                        String cardPin = rs.getString("pin");
                        if (number.equals(cardNumber) && pin.equals(cardPin))
                            return new Account.Builder()
                                    .addNumber(cardNumber)
                                    .addPin(cardPin)
                                    .addBalance(rs.getInt("balance"))
                                    .build();
                    }
                }
            } catch (SQLException sqe) {sqe.printStackTrace();}
        } catch (SQLException sqe) {sqe.printStackTrace();}

        return Account.empty();
    }

    public static Account getRecipientAccount(String number) {
        try (Connection con = dataSource.getConnection()) {
            try (Statement stmt = con.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT number, pin, balance FROM card;");
                while (rs.next()) {
                    String cardNumber = rs.getString("number");
                    if (number.equals(cardNumber))
                        return new Account.Builder()
                                .addNumber(cardNumber)
                                .addPin(rs.getString("pin"))
                                .addBalance(rs.getInt("balance"))
                                .build();
                }
            } catch (SQLException sqe) {sqe.printStackTrace();}
        } catch (SQLException sqe) {sqe.printStackTrace();}

        return Account.empty();
    }
}
