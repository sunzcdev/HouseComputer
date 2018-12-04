package net.sunzc.housecomputer.compute;

import java.math.BigDecimal;

/**
 * @author Administrator
 * @date 2018-12-04 13:51
 **/
class Utils {
    static double month(double amount, double rate, int num) {
        double monthRate = rate / 12;
        double pow = Math.pow(1 + monthRate, num);
        return Math.round(amount * monthRate * pow / (pow - 1));
    }

    static double format(double number, int newScale) {
        BigDecimal b = new BigDecimal(number);
        return b.setScale(newScale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
