package net.sunzc.housecomputer;

import android.os.Bundle;
import android.support.annotation.Nullable;

import net.sunzc.housecomputer.compute.OldHouseCompute;
import net.sunzc.housecomputer.entity.HouseType;
import net.sunzc.housecomputer.entity.KVObject;
import net.sunzc.housecomputer.utils.ViewUtils;

import java.util.List;

/**
 * @author Administrator
 * @date 2018-12-04 11:52
 **/
public class OldHouseFragment extends HouseFragment {
    public static HouseFragment newInstance(HouseType house) {
        HouseFragment fragment = new HouseFragment();
        Bundle args = new Bundle();
        args.putSerializable(HOUSE, house);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLoanCompute(new OldHouseCompute());
    }

    @Override
    protected void onItemClick(List<KVObject> list, KVObject item) {
        final HouseType houseType = getHouseType();
        switch (item.K) {
            case "高评价格":
                ViewUtils.showInputDialog(getContext(), "请输入分期数", item.V, new ViewUtils.InputListener() {
                    @Override
                    public void onInput(String text) {
                        double price = Double.valueOf(text);
                        houseType.setPrice(price);
                        onChange(houseType);
                    }
                });
                break;
            default:
                super.onItemClick(list, item);
        }
    }
}
