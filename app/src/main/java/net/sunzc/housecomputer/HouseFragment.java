package net.sunzc.housecomputer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.sunzc.housecomputer.compute.LoanCompute;
import net.sunzc.housecomputer.entity.HouseType;
import net.sunzc.housecomputer.entity.KVObject;
import net.sunzc.housecomputer.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @date 2018-12-03 14:55
 **/
public class HouseFragment extends Fragment implements HouseChangeListener {

    private RecyclerView recycleView;

    private HouseAdapter adapter;
    private HouseType house;
    private HouseChangeListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HouseChangeListener) {
            this.mListener = (HouseChangeListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    public static final String HOUSE = "house";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycleView = view.findViewById(R.id.list);
        adapter = new HouseAdapter(getContext());
        Bundle args = getArguments();
        if (args != null) {
            house = (HouseType) args.getSerializable(HOUSE);
            if (compute != null) {
                adapter.refresh(compute.compute(house));
            }
        }
        recycleView.addItemDecoration(new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL));
        recycleView.setAdapter(adapter);
    }

    private LoanCompute compute;

    protected void setLoanCompute(LoanCompute compute) {
        this.compute = compute;
    }

    public void refresh(HouseType houseType) {
        if (adapter != null) {
            adapter.refresh(compute.compute(houseType));
            house = houseType;
        }
    }

    @Override
    public void onChange(HouseType houseType) {
        this.mListener.onChange(houseType);
    }

    private static class HouseVH extends RecyclerView.ViewHolder {
        public final TextView tv1, tv2;

        public HouseVH(View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(android.R.id.text1);
            tv2 = itemView.findViewById(android.R.id.text2);
        }
    }

    protected HouseType getHouseType() {
        return this.house;
    }

    protected void onItemClick(List<KVObject> list, KVObject item) {
        switch (item.K) {
            case "名称":
                ViewUtils.showInputDialog(getContext(), "请输入户型名称", item.V, new ViewUtils.InputListener() {
                    @Override
                    public void onInput(String text) {
                        house.setName(text);
                        onChange(house);
                    }
                });
                break;
            case "房价":
                ViewUtils.showInputDialog(getContext(), "请输入房价", item.V, new ViewUtils.InputListener() {
                    @Override
                    public void onInput(String text) {
                        double price = Double.valueOf(text);
                        house.setPrice(price);
                        onChange(house);
                    }
                });
                break;
            case "面积":
                ViewUtils.showInputDialog(getContext(), "请输入面积", item.V, new ViewUtils.InputListener() {
                    @Override
                    public void onInput(String text) {
                        double square = Double.valueOf(text);
                        house.setSquare(square);
                        onChange(house);
                    }
                });
                break;
            default:
                ViewUtils.dialog(getContext(), item.K, item.V);
                break;
        }
    }

    private class HouseAdapter extends RecyclerView.Adapter<HouseVH> {
        private final LayoutInflater inflact;
        private List<KVObject> list = new ArrayList<>();

        public HouseAdapter(Context context) {
            this.inflact = LayoutInflater.from(context);
        }

        public void refresh(List<KVObject> list) {
            this.list.clear();
            this.list.addAll(list);
            recycleView.setVisibility(View.VISIBLE);
            notifyItemRangeChanged(0, getItemCount());
        }

        @NonNull
        @Override
        public HouseVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new HouseVH(inflact.inflate(android.R.layout.simple_list_item_2, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull HouseVH holder, int position) {
            final KVObject item = list.get(position);
            holder.tv1.setText(item.K);
            holder.tv2.setText(item.V);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(list, item);
                }
            });
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }
}
