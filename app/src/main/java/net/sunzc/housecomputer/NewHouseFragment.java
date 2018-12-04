package net.sunzc.housecomputer;

import android.os.Bundle;
import android.support.annotation.Nullable;

import net.sunzc.housecomputer.compute.NewHouseLoan;
import net.sunzc.housecomputer.entity.HouseType;
import net.sunzc.housecomputer.entity.KVObject;
import net.sunzc.housecomputer.utils.ViewUtils;

import java.util.List;

/**
 * @author Administrator
 * @date 2018-12-04 11:52
 **/
public class NewHouseFragment extends HouseFragment {

    public static NewHouseFragment newInstance(HouseType house) {
        NewHouseFragment fragment = new NewHouseFragment();
        Bundle args = new Bundle();
        args.putSerializable(HOUSE, house);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLoanCompute(new NewHouseLoan());
    }

    @Override
    protected void onItemClick(List<KVObject> list, KVObject item) {
        final HouseType houseType = getHouseType();
        if ("首付分期数".equals(item.K)) {
            ViewUtils.showInputDialog(getContext(), "请输入分期数", item.V, new ViewUtils.InputListener() {
                @Override
                public void onInput(String text) {
                    int num = Integer.valueOf(text);
                    houseType.setLoanNum(num);
                    onChange(houseType);
                }
            });
        } else {
            super.onItemClick(list, item);
        }
    }
}
