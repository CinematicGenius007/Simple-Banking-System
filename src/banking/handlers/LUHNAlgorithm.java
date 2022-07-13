package banking.handlers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LUHNAlgorithm {

    public static int getCheckSum(String number) {
        if (number.length() < 15)
            return -1;

        List<Integer> num = Arrays
                .stream(number
                        .substring(0, 15)
                        .split(""))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        for (int i = 0; i < 15; i++) {
            if (i % 2 == 0)
                num.set(i, num.get(i) * 2);
            if (num.get(i) > 9)
                num.set(i, num.get(i) - 9);
        }

        int checkSum = num.stream().reduce(0, Integer::sum) % 10;

        return checkSum == 0 ? checkSum : 10 - checkSum;
    }
}
