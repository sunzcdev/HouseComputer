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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @date 2018-12-03 14:55
 **/
public class HouseFragment extends Fragment {

    public static HouseFragment newInstance(ArrayList<KVObject> list) {
        HouseFragment fragment = new HouseFragment();
        Bundle args = new Bundle();
        args.putSerializable("list", list);
        fragment.setArguments(args);
        return fragment;
    }

    private HouseAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new HouseAdapter(getContext());
        Bundle args = getArguments();
        if (args != null) {
            ArrayList<KVObject> list = (ArrayList<KVObject>) args.getSerializable("list");
            if (list != null && !list.isEmpty()) {
                adapter.refresh(list);
            }
        }
        RecyclerView recycleView = view.findViewById(R.id.list);
        recycleView.addItemDecoration(new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL));
        recycleView.setAdapter(adapter);
    }

    public void refresh(List<KVObject> list) {
        if (adapter != null)
            adapter.refresh(list);
    }

    private static class HouseVH extends RecyclerView.ViewHolder {
        public final TextView tv1, tv2;

        public HouseVH(View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(android.R.id.text1);
            tv2 = itemView.findViewById(android.R.id.text2);
        }
    }

    private static class HouseAdapter extends RecyclerView.Adapter<HouseVH> {
        private final LayoutInflater inflact;
        private List<KVObject> list = new ArrayList<>();

        public HouseAdapter(Context context) {
            this.inflact = LayoutInflater.from(context);
        }

        public void refresh(List<KVObject> list) {
            this.list.clear();
            this.list.addAll(list);
            notifyItemRangeChanged(0, getItemCount());
        }

        @NonNull
        @Override
        public HouseVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new HouseVH(inflact.inflate(android.R.layout.simple_list_item_2, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull HouseVH holder, int position) {
            KVObject item = list.get(position);
            holder.tv1.setText(item.K);
            holder.tv2.setText(item.V);
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
}
