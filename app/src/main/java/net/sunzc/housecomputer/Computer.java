package net.sunzc.housecomputer;

import android.util.Pair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * @author Administrator
 * @date 2018-12-03 16:11
 **/
public class Computer {
    private double square, price;

    public Computer(float square, float price) {
        this.square = (double) square;
        this.price = (double) price;
    }

    public ArrayList<Pair<String, String>> toNewHouse(int num) {
        ArrayList<Pair<String, String>> result = new ArrayList<>();
        result.add(new Pair<>("面积", String.valueOf(square)));
        result.add(new Pair<>("房价", String.valueOf(price)));
        double amount = square * price / 10000;
        result.add(new Pair<>("总款", String.format(Locale.CHINA, "%.2f万元", amount)));
        double rate = 0.06;
        result.add(new Pair<>("利率", String.format(Locale.CHINA, "%f", rate * 100)));
        result.add(new Pair<>("贷款总额", String.format(Locale.CHINA, "%.2f万元", amount * 0.7)));
        double monthRate = rate / (100 * 12);
        double month = (amount * 0.7 * 10000 * monthRate * Math.pow((1 + monthRate), 30)) / (Math.pow((1 + monthRate), 30) - 1);
        result.add(new Pair<>("月供", String.format(Locale.CHINA, "%.1f元", month)));
        result.add(new Pair<>("总计首付", String.format(Locale.CHINA, "%.4f万元", amount * 0.3)));
        result.add(new Pair<>("首付分期数", String.format(Locale.CHINA, "%d期", num)));
        double yearAmount = amount * 0.1;
        result.add(new Pair<>("首付首期", String.format(Locale.CHINA, "%.4f万元", yearAmount)));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
        Calendar cal = Calendar.getInstance();
        for (int i = 1; i <= num; i++) {
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 6);
            result.add(new Pair<>(
                    String.format(Locale.CHINA, "首付余款第%d期付", i),
                    String.format(Locale.CHINA, "%s 付 %.4f万元", format.format(cal.getTime()), yearAmount / num))
            );
        }
        result.add(new Pair<>("首付余款年均付", String.format(Locale.CHINA, "%.4f万元", yearAmount)));
        double averageMonth = yearAmount / 12f * 10000 + 3000f + 1500f + month;
        result.add(new Pair<>("月均支出", String.format(Locale.CHINA, "%.1f元", averageMonth)));
        result.add(new Pair<>("年均支出", String.format(Locale.CHINA, "%.4f万元", averageMonth * 12 / 10000)));
        return result;
    }

    public ArrayList<Pair<String, String>> toOldHouse(double highPrice) {
        ArrayList<Pair<String, String>> result = new ArrayList<>();
        result.add(new Pair<>("面积", String.valueOf(square)));
        result.add(new Pair<>("房价", String.valueOf(price)));
        double amount = square * price / 10000;
        result.add(new Pair<>("总款", String.format(Locale.CHINA, "%.2f万元", amount)));
        double rate = 0.06;
        result.add(new Pair<>("利率", String.format(Locale.CHINA, "%f", rate * 100)));
        result.add(new Pair<>("高评房价", String.valueOf(highPrice)));
        double highAmount = highPrice * square;
        result.add(new Pair<>("贷款总额", String.format(Locale.CHINA, "%.4f万元", highAmount * 0.7)));
        result.add(new Pair<>("总计首付", String.format(Locale.CHINA, "%.4f万元", amount - highAmount * 0.7)));
        double monthRate = rate / (100 * 12);
        double month = (highAmount * 0.7 * 10000 * monthRate * Math.pow((1 + monthRate), 30)) / (Math.pow((1 + monthRate), 30) - 1);
        result.add(new Pair<>("月供", String.format(Locale.CHINA, "%.1f元", month)));
        double averageMonth = 3000f + 1500f + month;
        result.add(new Pair<>("月均支出", String.format(Locale.CHINA, "%.1f元", averageMonth)));
        result.add(new Pair<>("年均支出", String.format(Locale.CHINA, "%.4f万元", averageMonth * 12 / 10000)));
        return result;
    }
}
