package banking.entities;

import banking.handlers.DatabaseHandler;
import banking.handlers.LUHNAlgorithm;

import java.util.List;
import java.util.Random;

public class Number {

    static StringBuilder initialNumber = new StringBuilder("400000");
    static Random random = new Random();

    public static String numberGenerator() {
        List<String> numbers = DatabaseHandler.getAllUniqueNumbers();
        while (true) {
            StringBuilder newCardNumber = new StringBuilder(initialNumber);
            newCardNumber.append(random.nextInt(899999999) + 100000000);
            newCardNumber.append(LUHNAlgorithm.getCheckSum(newCardNumber.toString()));

            if (numbers.contains(newCardNumber.toString()))
                continue;

            return newCardNumber.toString();
        }
    }

}
