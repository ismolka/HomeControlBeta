package com.example.irek.homecontrolbetaversion.ui.connect;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;

import com.example.irek.homecontrolbetaversion.R;
import com.example.irek.homecontrolbetaversion.ui.children.ChildrenActivity;
import com.example.irek.homecontrolbetaversion.ui.garden.GardenActivity;
import com.example.irek.homecontrolbetaversion.ui.home.HomeActivity;
import com.example.irek.homecontrolbetaversion.ui.login.LogonActivity;
import com.example.irek.homecontrolbetaversion.ui.settings.SettingsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConnectActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ConnectView {
    @BindView(R.id.switchBtState) Switch switchBT;
    @BindView(R.id.buttonConnection) Button buttonConnect;
    @BindView(R.id.BtClientList) ListView clientsBT;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;

    private ConnectPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        ButterKnife.bind(this);

        initPresenter();
        initView();
    }

    private void initView() {
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initPresenter() {
        presenter = new ConnectPresenter();
        presenter.onAttach(this);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_garden) {
            Intent intent = new Intent(this, GardenActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_children) {
            Intent intent = new Intent(this, ChildrenActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }/* else if (id == R.id.nav_connect) {
            //Intent intent = new Intent(this, ConnectActivity.class);
            //startActivity(intent);
        } else if (id == R.id.nav_logon) {
            Intent intent = new Intent(this, LogonActivity.class);
            startActivity(intent);
        }*/

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
