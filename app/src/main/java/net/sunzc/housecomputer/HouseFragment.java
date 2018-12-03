package net.sunzc.housecomputer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @date 2018-12-03 14:55
 **/
public class HouseFragment extends ListFragment {

    public static HouseFragment newInstance(ArrayList<KVObject> list) {
        HouseFragment fragment = new HouseFragment();
        Bundle args = new Bundle();
        args.putSerializable("list", list);
        fragment.setArguments(args);
        return fragment;
    }

    private HouseAdapter adapter;

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
        setListAdapter(adapter);
    }

    public void refresh(List<KVObject> list) {
        if (adapter != null)
            adapter.refresh(list);
    }

    private static class HouseAdapter extends BaseAdapter {
        private final LayoutInflater inflact;
        private List<KVObject> list = new ArrayList<>();

        public HouseAdapter(Context context) {
            this.inflact = LayoutInflater.from(context);
        }

        public void refresh(List<KVObject> list) {
            this.list.clear();
            this.list.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public KVObject getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflact.inflate(android.R.layout.simple_list_item_2, null);
            }
            TextView t1 = convertView.findViewById(android.R.id.text1);
            TextView t2 = convertView.findViewById(android.R.id.text2);
            KVObject item = getItem(position);
            t1.setText(item.K);
            t2.setText(item.V);
            return convertView;
        }
    }
}
