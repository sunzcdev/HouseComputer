package net.sunzc.housecomputer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private HouseFragment newHouseFragment;
    private HouseFragment oldHouseFragment;
    private SPMap map;
    private BlankFragment aboueFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bugly.init(this, "7b5ad35aef", BuildConfig.DEBUG);
        Beta.checkUpgrade();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });
        aboueFragment = new BlankFragment();

        map = SPMap.load(this, "input");
        if (map.contains("price")) {
            float price = map.get("price", 0f);
            float square = map.get("square", 0f);
            int num = map.get("num", 4);
            float highPrice = map.get("highPrice", price);
            Computer computer = new Computer(square, price);
            newHouseFragment = HouseFragment.newInstance(computer.toNewHouse(num));
            oldHouseFragment = HouseFragment.newInstance(computer.toOldHouse(highPrice));
        } else {
            newHouseFragment = new HouseFragment();
            oldHouseFragment = new HouseFragment();
            showInputDialog();
        }
    }

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("我要买什么样的房子");
        View view = getLayoutInflater().inflate(R.layout.input_dialog, null);
        final EditText priceEt = view.findViewById(R.id.price_et);
        final EditText squareEt = view.findViewById(R.id.square_et);
        final EditText numEt = view.findViewById(R.id.num_et);
        final EditText highPriceEt = view.findViewById(R.id.high_price_et);
        builder.setView(view);
        builder.setPositiveButton("计算", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    String priceStr = priceEt.getText().toString();
                    String squareStr = squareEt.getText().toString();
                    String numStr = numEt.getText().toString();
                    CharSequence highPriceStr = highPriceEt.getText();
                    if (TextUtils.isEmpty(highPriceStr)) {
                        highPriceStr = priceStr;
                    }
                    Float square = Float.valueOf(squareStr);
                    Float price = Float.valueOf(priceStr);
                    Computer computer = new Computer(square, price);
                    int num = Integer.parseInt(numStr);
                    newHouseFragment.refresh(computer.toNewHouse(num));
                    Double highPrice = Double.valueOf(highPriceStr.toString());
                    oldHouseFragment.refresh(computer.toOldHouse(highPrice));
                    map.put("square", square);
                    map.put("price", price);
                    map.put("num", num);
                    map.put("highPrice", highPrice);
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
                case 2:
                    return aboueFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
