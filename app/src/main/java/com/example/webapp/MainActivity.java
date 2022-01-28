package com.example.webapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.webapp.data.SubCategoryProvider;
import com.example.webapp.utils.ISetup;
import com.example.webapp.utils.ProductRA;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;

import java.util.List;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView _recyclerView;
    private LinearLayout _linearLayout;
    private Spinner _dropdown = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolBar();
        setUpViews();
        setUpRecyclerView();
    }

    private void setUpViews() {
        _recyclerView = (RecyclerView) findViewById(R.id.main_recycler);
        _linearLayout = (LinearLayout) findViewById(R.id.main_linearLayout);
        _dropdown = new Spinner(MainActivity.this);
        initiateSpinner();
    }

    private void setUpToolBar() {
        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.main_nav);

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setUpRecyclerView() {
        try {
            _recyclerView.setAdapter(new ProductRA(MainActivity.this, "ALL"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        _recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    private void initiateSpinner() {
        SubCategoryProvider.load(this, new ISetup() {
            @Override
            public void setup(List<String> subCategorise) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_spinner_item, subCategorise);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                _dropdown.setAdapter(adapter);
                _dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        try {
                            _recyclerView.swapAdapter(new ProductRA(MainActivity.this, adapter.getItem(i)), true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        });
    }

    private void dismissSpinner() {
        _linearLayout.removeView(_dropdown);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.sort_by_price:
                    dismissSpinner();
                    _recyclerView.swapAdapter(new ProductRA(this, "PRICE"), true);
                    break;
                case R.id.sort_by_all:
                    dismissSpinner();
                    _recyclerView.swapAdapter(new ProductRA(this, "ALL"), true);
                    break;
                case R.id.sort_by_category:
                    _linearLayout.addView(_dropdown, 0, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }
}