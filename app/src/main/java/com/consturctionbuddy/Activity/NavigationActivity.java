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

import com.consturctionbuddy.Bean.UserResponse.User;
import com.consturctionbuddy.Fragment.DailyWorkInformationFragment;
import com.consturctionbuddy.Fragment.FirstFragment;
import com.consturctionbuddy.Fragment.LeaveManagement;
import com.consturctionbuddy.Fragment.ProfileFragment;
import com.consturctionbuddy.Fragment.RequestForLeaveFragment;
import com.consturctionbuddy.Fragment.SiteImageRequestFragment;
import com.consturctionbuddy.R;
import com.consturctionbuddy.Utility.UserUtils;
import com.consturctionbuddy.custom.CustomBoldTextView;
import com.consturctionbuddy.custom.CustomRegularTextView;
import com.squareup.picasso.Picasso;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int BackCount = 0;

    DrawerLayout drawer;
    private ImageView mImgUserProfile;
    private CustomRegularTextView mEtEmail;
    private CustomBoldTextView mEtName;
    private CustomBoldTextView action_toolbar_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        action_toolbar_name = toolbar.findViewById(R.id.action_toolbar_name);

        action_toolbar_name.setText(R.string.app_name);
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

        User user = UserUtils.getInstance().getUserInfo(NavigationActivity.this);
        if (user != null) {

            mEtEmail.setText(user.getLocal().getEmail());
            mEtName.setText(user.getmStrName());

            if (user.getImg().getPath() != null) {
                Picasso.with(NavigationActivity.this).load(user.getImg().getPath()).placeholder(R.drawable.ic_user_account).into(mImgUserProfile);
            } else {
                mImgUserProfile.setImageResource(R.drawable.ic_user_account);
            }
        }

        setFragment(R.id.nav_dashboard);
    }


    public void setTitle(String title) {
        action_toolbar_name.setText(title);
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
                fragment = new ProfileFragment();
                break;
            case R.id.nav_leave:
                fragment = new RequestForLeaveFragment();
                break;

            case R.id.nav_leave_management:

                fragment = new LeaveManagement();
                break;

            case R.id.nav_daily_work:

                fragment = new DailyWorkInformationFragment();
                break;


            case R.id.nav_list_site:

                fragment = new SiteImageRequestFragment();
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

        setFragment(item.getItemId());
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
