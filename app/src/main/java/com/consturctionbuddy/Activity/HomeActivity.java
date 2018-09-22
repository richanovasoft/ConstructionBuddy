package com.consturctionbuddy.Activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.consturctionbuddy.Adapter.RecyclerAdapter;
import com.consturctionbuddy.Fragment.FirstFragment;
import com.consturctionbuddy.R;
import com.consturctionbuddy.Utility.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements RecyclerAdapter.ItemClickChild {


    RecyclerView recyclerView;
    private DrawerLayout drawerLayout;
    String names[] = Constant.name;
    String subNames[] = Constant.subName;
    Toolbar toolbar;
    FirstFragment fragment;
    private static ActionBarDrawerToggle actionbarToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(true);

            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
            getSupportActionBar().setTitle("Construction Buddy");
            //toolbar.setNavigationIcon(null);
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        recyclerView = findViewById(R.id.recyclerView);

        actionbarToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);

        actionbarToggle.syncState();

        // Setting drawer listener
        drawerLayout.setDrawerListener(actionbarToggle);

        List list = getList();
        RecyclerAdapter adapter = new RecyclerAdapter(this, list, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        // recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        setFragment();
    }

    private void setFragment() {
        fragment = new FirstFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment, "TitleFragment").commit();
    }

    private List getList() {
        List list = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            List subTitles = new ArrayList<>();
            for (int j = 0; j < subNames.length; j++) {
                RecyclerAdapter.SubTitle subTitle = new RecyclerAdapter.SubTitle(subNames[j]);
                subTitles.add(subTitle);
            }
            RecyclerAdapter.TitleMenu model = new RecyclerAdapter.TitleMenu(names[i], subTitles, null);
            list.add(model);
        }
        return list;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChildClick(int position) {
        String name = subNames[position];
        drawerLayout.closeDrawers();
        //fragment.setTitle(name);
    }


}
