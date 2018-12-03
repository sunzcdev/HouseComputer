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

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private HouseFragment newHouseFragment;
    private HouseFragment oldHouseFragment;
    private HouseFragment compareFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        newHouseFragment = new HouseFragment();
        oldHouseFragment = new HouseFragment();
        compareFragment = new HouseFragment();
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
                    Computer computer = new Computer(squareStr, priceStr);
                    newHouseFragment.refresh(computer.toNewHouse(numStr));
                    oldHouseFragment.refresh(computer.toOldHouse(Double.valueOf(highPriceStr.toString())));
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
