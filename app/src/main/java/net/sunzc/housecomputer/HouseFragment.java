package net.sunzc.housecomputer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Pair;
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
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private HouseAdapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new HouseAdapter(getContext());
        setListAdapter(adapter);
    }

    public void refresh(List<Pair<String, String>> list) {
        if (adapter != null)
            adapter.refresh(list);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    private static class HouseAdapter extends BaseAdapter {
        private final LayoutInflater inflact;
        private List<Pair<String, String>> list = new ArrayList<>();

        public HouseAdapter(Context context) {
            this.inflact = LayoutInflater.from(context);
        }

        public void refresh(List<Pair<String, String>> list) {
            this.list.clear();
            this.list.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Pair<String, String> getItem(int position) {
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
            Pair<String, String> item = getItem(position);
            t1.setText(item.first);
            t2.setText(item.second);
            return convertView;
        }
    }
}
