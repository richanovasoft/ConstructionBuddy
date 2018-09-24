package com.consturctionbuddy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.consturctionbuddy.Fragment.FirstFragment;
import com.consturctionbuddy.R;
import com.consturctionbuddy.Utility.UserUtils;
import com.consturctionbuddy.custom.CustomBoldTextView;
import com.consturctionbuddy.custom.CustomRegularTextView;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int BackCount = 0;

    DrawerLayout drawer;
    private ImageView mImgUserProfile;
    private CustomRegularTextView mEtEmail;
    private CustomBoldTextView mEtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View headerView = navigationView.getHeaderView(0);
        mEtEmail = headerView.findViewById(R.id.tv_user_email);
        mEtName = headerView.findViewById(R.id.tv_username);
        mImgUserProfile = headerView.findViewById(R.id.imageView);

        setFragment(R.id.nav_dashboard);
    }


    private void setFragment(int nav_dashboard) {
        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (nav_dashboard) {
            case R.id.nav_dashboard:
                fragment = new FirstFragment();
                break;
            case R.id.nav_profile:
                break;
            case R.id.nav_leave:
                break;

            case R.id.nav_leave_management:

                break;

            case R.id.nav_daily_work:

                break;


            case R.id.nav_list_site:

                break;


            case R.id.nav_chat:
                //
                break;

            case R.id.nav_change_pass:
                Intent intent2 = new Intent(NavigationActivity.this, ChangePasswordActivity.class);
                startActivity(intent2);
                break;

            case R.id.nav_logout:
                UserUtils.getInstance().clearAll(NavigationActivity.this);
                Intent intent = new Intent(NavigationActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame, fragment);
            ft.commit();
        }

        drawer.closeDrawer(GravityCompat.START);
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            showExitAlertDialog();
        }
    }

    private void showExitAlertDialog() {

        if (BackCount == 1) {
            BackCount = 0;
            finish();
        } else {
            Toast.makeText(this, "Press Back again to exit.", Toast.LENGTH_SHORT).show();
            BackCount++;
        }
    }

}
