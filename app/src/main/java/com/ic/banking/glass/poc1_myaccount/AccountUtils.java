package com.ic.banking.glass.poc1_myaccount;

import android.util.Pair;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;

public class AccountUtils {

    public static Pair<Integer, Integer> randomBalanceAndAccountNumbers() {
        Random random = new Random();
        Integer balance = random.nextInt(80000);
        Integer account = 10000000 + random.nextInt(90000000);

        return Pair.create(balance, account);
    }

    public static String maskAllButLastFour(int accountNumber) {
        String accountString = String.valueOf(accountNumber);
        int length = accountString.length();

        if (length > 4) {
            String maskedNumbers = StringUtils.repeat("*", length - 4);
            String numbersToShow = accountString.substring(length - 4);
            return maskedNumbers + numbersToShow;
        }
        else {
            throw new IllegalArgumentException("Account number must me longer than 4");
        }
    }
}
