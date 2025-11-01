package com.example.actividadmenulateral;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Cargar fragment por defecto
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ButtonFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_button);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        int id = item.getItemId();
        if (id == R.id.nav_button) {
            fragment = new ButtonFragment();
        } else if (id == R.id.nav_edittext) {
            fragment = new EditTextFragment();
        } else if (id == R.id.nav_radiobutton) {
            fragment = new RadioButtonFragment();
        } else if (id == R.id.nav_checkbox) {
            fragment = new CheckBoxFragment();
        } else if (id == R.id.nav_switch) {
            fragment = new SwitchFragment();
        } else if (id == R.id.nav_spinner) {
            fragment = new SpinnerFragment();
        } else if (id == R.id.nav_seekbar) {
            fragment = new SeekBarFragment();
        } else if (id == R.id.nav_textview) {
            fragment = new TextViewFragment();
        } else if (id == R.id.nav_imageview) {
            fragment = new ImageViewFragment();
        } else if (id == R.id.nav_progressbar) {
            fragment = new ProgressBarFragment();
        }

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }

        drawerLayout.closeDrawers();
        return true;
    }
}
