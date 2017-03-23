package com.example.irek.homecontrolbetaversion;

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

import static com.example.irek.homecontrolbetaversion.R.id.BtClientList;
import static com.example.irek.homecontrolbetaversion.R.id.buttonConnection;
import static com.example.irek.homecontrolbetaversion.R.id.switchBtState;

public class ConnectActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Switch switchBT;
    Button buttonConnect;
    ListView clientsBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        switchBT = (Switch)findViewById(switchBtState);
        buttonConnect = (Button)findViewById(buttonConnection);
        clientsBT = (ListView)findViewById(BtClientList);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        } else if (id == R.id.nav_connect) {
            Intent intent = new Intent(this, ConnectActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logon) {
            Intent intent = new Intent(this, LogonActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
