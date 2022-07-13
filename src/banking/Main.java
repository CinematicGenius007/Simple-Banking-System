package banking;

import banking.handlers.Bank;
import banking.handlers.DatabaseHandler;

public class Main {
    public static void main(String[] args) {
        if (args.length == 2
                && args[0].equalsIgnoreCase("-fileName")) {
            DatabaseHandler.makeConnection(args[1]);

            Bank JetBrains = new Bank();
            JetBrains.startServices();
        }
    }
}