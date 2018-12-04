package net.sunzc.housecomputer.compute;

import net.sunzc.housecomputer.entity.HouseType;
import net.sunzc.housecomputer.entity.KVObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static net.sunzc.housecomputer.compute.Utils.format;
import static net.sunzc.housecomputer.compute.Utils.month;

/**
 * @author Administrator
 * @date 2018-12-04 13:50
 **/
public class OldHouseCompute implements LoanCompute {
    @Override
    public List<KVObject> compute(HouseType houseType) {
        ArrayList<KVObject> result = new ArrayList<>();
        result.add(new KVObject("面积", String.format(Locale.CHINA, "%s平米", format(houseType.getSquare(), 2))));
        result.add(new KVObject("房价", String.format(Locale.CHINA, "%s万元/平米", format(houseType.getPrice(), 4))));
        double amount = houseType.getSquare() * houseType.getPrice();
        result.add(new KVObject("总款", String.format(Locale.CHINA, "%s万元", format(amount, 2))));
        result.add(new KVObject("利率", (houseType.getLoanRate() * 100) + "%"));
        result.add(new KVObject("高评房价", String.format(Locale.CHINA, "%s万元/平米", format(houseType.getHighPrice(), 3))));
        double highAmount = houseType.getHighPrice() * houseType.getSquare();
        double loanAmount = highAmount * 0.7;
        result.add(new KVObject("贷款总额", String.format(Locale.CHINA, "%s万元", format(loanAmount, 2))));
        result.add(new KVObject("总计首付", String.format(Locale.CHINA, "%s万元", format(amount - loanAmount, 4))));
        double month = month(loanAmount * 10000, houseType.getLoanRate(), 360);
        result.add(new KVObject("月供", String.format(Locale.CHINA, "%s元", format(month, 1))));
        double averageMonth = 3000f + 1500f + month;
        result.add(new KVObject("月均支出", String.format(Locale.CHINA, "%s元", format(averageMonth, 1))));
        result.add(new KVObject("年均支出", String.format(Locale.CHINA, "%s万元", format(averageMonth * 12 / 10000, 4))));
        return result;
    }
}
