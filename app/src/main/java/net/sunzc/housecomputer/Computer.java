package net.sunzc.housecomputer;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author Administrator
 * @date 2018-12-03 16:11
 **/
public class Computer {
    private double square, price;
    private double rate = 0.06;

    public Computer(float square, float price) {
        this.square = (double) square;
        this.price = (double) price;
    }

    public ArrayList<KVObject> toNewHouse(int num) {
        ArrayList<KVObject> result = new ArrayList<>();
        result.add(new KVObject("面积", String.format(Locale.CHINA, "%s平米", format(square, 2))));
        result.add(new KVObject("房价", String.format(Locale.CHINA, "%s万元/平米", format(price, 4))));
        double amount = square * price;
        result.add(new KVObject("总款", String.format(Locale.CHINA, "%s万元", format(amount, 2))));
        result.add(new KVObject("利率", (rate * 100) + "%"));
        double loanAmount = amount * 0.7;
        result.add(new KVObject("贷款总额", String.format(Locale.CHINA, "%s万元", format(loanAmount, 2))));
        double month = month(loanAmount * 10000, rate, 360);
        result.add(new KVObject("月供", String.format(Locale.CHINA, "%s元", format(month, 1))));
        result.add(new KVObject("总计首付", String.format(Locale.CHINA, "%s万元", format(amount * 0.3, 4))));
        result.add(new KVObject("首付分期数", String.format(Locale.CHINA, "%d期", num)));
        double yearAmount = amount * 0.1;
        result.add(new KVObject("首付首期", String.format(Locale.CHINA, "%s万元", format(yearAmount, 4))));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
        Calendar cal = Calendar.getInstance();
        for (int i = 1; i <= num; i++) {
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 6);
            result.add(new KVObject(
                    String.format(Locale.CHINA, "首付余款第%d期付", i),
                    String.format(Locale.CHINA, "%s 付 %s万元", format.format(cal.getTime()), format(yearAmount / num, 4)))
            );
        }
        result.add(new KVObject("首付余款年均付", String.format(Locale.CHINA, "%s万元", format(yearAmount, 4))));
        double averageMonth = yearAmount / 12f * 10000 + 3000f + 1500f + month;
        result.add(new KVObject("月均支出", String.format(Locale.CHINA, "%s元", format(averageMonth, 1))));
        result.add(new KVObject("年均支出", String.format(Locale.CHINA, "%s万元", format(averageMonth * 12 / 10000, 4))));
        return result;
    }

    public static double month(double amount, double rate, int num) {
        double monthRate = rate / 12;
        double pow = Math.pow(1 + monthRate, num);
        return Math.round(amount * monthRate * pow / (pow - 1));
    }

    public ArrayList<KVObject> toOldHouse(double highPrice) {
        ArrayList<KVObject> result = new ArrayList<>();
        result.add(new KVObject("面积", String.format(Locale.CHINA, "%s平米", format(square, 2))));
        result.add(new KVObject("房价", String.format(Locale.CHINA, "%s万元/平米", format(price, 4))));
        double amount = square * price;
        result.add(new KVObject("总款", String.format(Locale.CHINA, "%s万元", format(amount, 2))));
        result.add(new KVObject("利率", (rate * 100) + "%"));
        result.add(new KVObject("高评房价", String.format(Locale.CHINA, "%s万元/平米", format(highPrice, 3))));
        double highAmount = highPrice * square;
        double loanAmount = highAmount * 0.7;
        result.add(new KVObject("贷款总额", String.format(Locale.CHINA, "%s万元", format(loanAmount, 2))));
        result.add(new KVObject("总计首付", String.format(Locale.CHINA, "%s万元", format(amount - loanAmount, 4))));
        double month = month(loanAmount * 10000, rate, 360);
        result.add(new KVObject("月供", String.format(Locale.CHINA, "%s元", format(month, 1))));
        double averageMonth = 3000f + 1500f + month;
        result.add(new KVObject("月均支出", String.format(Locale.CHINA, "%s元", format(averageMonth, 1))));
        result.add(new KVObject("年均支出", String.format(Locale.CHINA, "%s万元", format(averageMonth * 12 / 10000, 4))));
        return result;
    }

    public static double format(double number, int newScale) {
        BigDecimal b = new BigDecimal(number);
        return b.setScale(newScale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
