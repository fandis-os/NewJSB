package com.ngs_tech.newjsb.sub_act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;
import com.ngs_tech.newjsb.R;
import com.ngs_tech.newjsb.adapters.PageAdapter;
import com.ngs_tech.newjsb.fragments.FragGangguan;
import com.ngs_tech.newjsb.fragments.FragKecelakaan;

public class LaluLintas extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private int[] tabIcons = {
            R.drawable.kecelakaan,
            R.drawable.gangguan
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lalu_lintas);

        viewPager = findViewById(R.id.view_pager);
        setUpViewPager(viewPager);

        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();

    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }

    private void setUpViewPager(ViewPager viewPager) {
        PageAdapter adapter = new PageAdapter(getSupportFragmentManager());
        adapter.addFrag(new FragKecelakaan(), "");
        adapter.addFrag(new FragGangguan(), "");
        viewPager.setAdapter(adapter);
    }
}
