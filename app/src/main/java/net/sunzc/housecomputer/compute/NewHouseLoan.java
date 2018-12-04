package net.sunzc.housecomputer.compute;

import net.sunzc.housecomputer.entity.HouseType;
import net.sunzc.housecomputer.entity.KVObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static net.sunzc.housecomputer.compute.Utils.format;
import static net.sunzc.housecomputer.compute.Utils.month;


/**
 * @author Administrator
 * @date 2018-12-04 13:49
 **/
public class NewHouseLoan implements LoanCompute {
    @Override
    public List<KVObject> compute(HouseType houseType) {
        ArrayList<KVObject> result = new ArrayList<>();
        result.add(new KVObject("面积", String.format(Locale.CHINA, "%s平米", format(houseType.getSquare(), 2))));
        result.add(new KVObject("房价", String.format(Locale.CHINA, "%s万元/平米", format(houseType.getPrice(), 4))));
        double amount = houseType.getSquare() * houseType.getPrice();
        result.add(new KVObject("总款", String.format(Locale.CHINA, "%s万元", format(amount, 2))));
        result.add(new KVObject("利率", (houseType.getLoanRate() * 100) + "%"));
        double loanAmount = amount * 0.7;
        result.add(new KVObject("贷款总额", String.format(Locale.CHINA, "%s万元", format(loanAmount, 2))));
        double month = month(loanAmount * 10000, houseType.getLoanRate(), 360);
        result.add(new KVObject("月供", String.format(Locale.CHINA, "%s元", format(month, 1))));
        result.add(new KVObject("总计首付", String.format(Locale.CHINA, "%s万元", format(amount * 0.3, 4))));
        result.add(new KVObject("首付分期数", String.format(Locale.CHINA, "%d期", houseType.getLoanNum())));
        double yearAmount = amount * 0.1;
        result.add(new KVObject("首付首期", String.format(Locale.CHINA, "%s万元", format(yearAmount, 4))));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
        Calendar cal = Calendar.getInstance();
        for (int i = 1; i <= houseType.getLoanNum(); i++) {
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 6);
            result.add(new KVObject(
                    String.format(Locale.CHINA, "首付余款第%d期付", i),
                    String.format(Locale.CHINA, "%s 付 %s万元", format.format(cal.getTime()), format(yearAmount / houseType.getLoanNum(), 4)))
            );
        }
        result.add(new KVObject("首付余款年均付", String.format(Locale.CHINA, "%s万元", format(yearAmount, 4))));
        double averageMonth = yearAmount / 12f * 10000 + 3000f + 1500f + month;
        result.add(new KVObject("月均支出", String.format(Locale.CHINA, "%s元", format(averageMonth, 1))));
        result.add(new KVObject("年均支出", String.format(Locale.CHINA, "%s万元", format(averageMonth * 12 / 10000, 4))));
        return result;
    }
}
