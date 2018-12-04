package net.sunzc.housecomputer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.bugly.beta.Beta;

import net.sunzc.housecomputer.db.DBDao;
import net.sunzc.housecomputer.entity.HouseType;
import net.sunzc.housecomputer.utils.SPMap;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements HouseChangeListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private HouseFragment newHouseFragment;
    private HouseFragment oldHouseFragment;
    private SPMap map;
    protected DBDao mDBDao;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Beta.checkUpgrade();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        RecyclerView houseList = findViewById(R.id.house_list);
        List<HouseType> list = mDBDao.query(HouseType.class, null, null);
        houseList.setAdapter(new HouseListAdapter(list));
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = findViewById(R.id.edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });

        mDBDao = DBDao.getInstance();
        map = SPMap.load(this, "lastHistory");
        int id = map.get("id", 0);
        List<HouseType> houseType = mDBDao.query(HouseType.class, "id=?", new String[]{String.valueOf(id)});
        if (houseType.isEmpty()) {
            newHouseFragment = new HouseFragment();
            oldHouseFragment = new HouseFragment();
            showInputDialog();
        } else {
            HouseType house = houseType.get(0);
            oldHouseFragment = OldHouseFragment.newInstance(house);
            newHouseFragment = NewHouseFragment.newInstance(house);
        }

    }

    public void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("我要买什么样的房子");
        View view = getLayoutInflater().inflate(R.layout.input_dialog, null);
        final EditText priceEt = view.findViewById(R.id.price_et);
        final EditText nameEt = view.findViewById(R.id.name_et);
        final EditText squareEt = view.findViewById(R.id.square_et);
        builder.setView(view);
        builder.setCancelable(false);
        builder.setPositiveButton("计算", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    String name = nameEt.getText().toString();
                    String priceStr = priceEt.getText().toString();
                    String squareStr = squareEt.getText().toString();
                    Float square = Float.valueOf(squareStr);
                    Float price = Float.valueOf(priceStr);
                    HouseType houseType = new HouseType(name, price, square);
                    refresh(houseType);
                    mDBDao.replace(houseType);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "输入有误", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void refresh(HouseType houseType) {
        newHouseFragment.refresh(houseType);
        oldHouseFragment.refresh(houseType);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChange(HouseType houseType) {
        mDBDao.replace(houseType);
    }

    public class HouseVH extends RecyclerView.ViewHolder {
        private final TextView tv;

        public HouseVH(View itemView) {
            super(itemView);
            tv = itemView.findViewById(android.R.id.text1);
        }
    }

    public class HouseListAdapter extends RecyclerView.Adapter<HouseVH> {
        private List<HouseType> houseTypes = new ArrayList<>();

        HouseListAdapter(List<HouseType> houseTypes) {
            if (houseTypes != null && !houseTypes.isEmpty())
                this.houseTypes.addAll(houseTypes);
        }

        public void add(HouseType houseType) {
            this.houseTypes.add(houseType);
            notifyItemInserted(getItemCount() - 1);
        }

        public void update(HouseType houseType) {
        }

        public void del() {
        }

        @NonNull
        @Override
        public HouseVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new HouseVH(getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull HouseVH holder, int position) {
            final HouseType house = houseTypes.get(position);
            holder.tv.setText(house.getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refresh(house);
                    map.put("id", house.getId());
                    closeDrawer();
                }
            });
        }

        @Override
        public int getItemCount() {
            return this.houseTypes.size();
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return newHouseFragment;
                case 1:
                    return oldHouseFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
