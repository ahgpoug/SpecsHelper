package ahgpoug.com.specshelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import java.util.ArrayList;

import ahgpoug.com.specshelper.objects.RAM;
import ahgpoug.com.specshelper.adapters.RamRecyclerAdapter;
import ahgpoug.com.specshelper.util.FiltersHelper;
import ahgpoug.com.specshelper.util.Globals;

public class ActivityRam extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler);

        FiltersHelper.clearRamFilter(ActivityRam.this);

        initViews();
        initDrawer();
        initEvents();
    }

    @Override
    protected void onStop() {
        FiltersHelper.clearRamFilter(ActivityRam.this);
        super.onStop();
    }

    @Override
    protected void onResume() {
        initViews();
        super.onResume();
    }

    private void initViews(){
        FiltersHelper.clearRamFilter(ActivityRam.this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ActivityRam.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(false);

        String query = "SELECT * FROM ram INNER JOIN ramtypes on ram.type = ramtypes.rid";

        RamRecyclerAdapter adapter = new RamRecyclerAdapter(ActivityRam.this, Globals.getRamFromQuery(ActivityRam.this, query));
        recyclerView.setAdapter(adapter);
    }

    private void initDrawer(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Модули памяти (RAM)");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        header = navigationView.getHeaderView(0).findViewById(R.id.header);

        if (Globals.isAdmin){
            Menu nav_menu = navigationView.getMenu();
            nav_menu.findItem(R.id.signIn).setTitle("Выйти из пользователя");
            View header = navigationView.getHeaderView(0).findViewById(R.id.header);
            header.setVisibility(View.GONE);

        } else {
            Menu nav_menu = navigationView.getMenu();
            nav_menu.findItem(R.id.signIn).setTitle("Войти в пользователя");
        }
    }

    private void initEvents(){
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        initViews();
                    }
                }, 500);
            }
        });


        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityRam.this, ActivityCart.class);
                startActivity(intent);
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.cpu) {
            Intent intent = new Intent(ActivityRam.this, ActivityCpu.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.motherboard) {
            Intent intent = new Intent(ActivityRam.this, ActivityMotherboard.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.gpu) {
            Intent intent = new Intent(ActivityRam.this, ActivityGpu.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.signIn) {
            if (!Globals.isAdmin)
                Globals.showSignInForm(ActivityRam.this);
            else{
                Globals.isAdmin = false;
                finish();
                startActivity(getIntent());
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        if (!Globals.isAdmin)
            menu.findItem(R.id.action_add).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add){
            Intent intent = new Intent(ActivityRam.this, ActivityAddNewRam.class);
            startActivity(intent);
        } else if (id == R.id.action_filter){
            MaterialDialog dialog;
            String[] comparisonArray = new String[] {"<", "==", ">"};
            ArrayList<String> ramTypesList = Globals.getSingleField(ActivityRam.this, "SELECT * FROM ramtypes", "rName");
            ramTypesList.add(0, "Не важно");
            ramTypesList.remove(2);
            ramTypesList.remove(3);
            MaterialDialog.SingleButtonCallback callback;

            callback = new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    String query = "SELECT * FROM ram INNER JOIN ramtypes on ram.type = ramtypes.rid";

                    boolean isFirst = true;

                    View view = dialog.getView();
                    EditText manufacturerET = (EditText)view.findViewById(R.id.manufacturerET);
                    EditText codenameET = (EditText)view.findViewById(R.id.codenameET);
                    EditText clockET = (EditText)view.findViewById(R.id.clockET);
                    EditText memorySizeET = (EditText)view.findViewById(R.id.memorySizeET);
                    EditText priceET = (EditText)view.findViewById(R.id.priceET);

                    BetterSpinner clockSpinner = (BetterSpinner)view.findViewById(R.id.clockSpinner);
                    BetterSpinner memorySizeSpinner = (BetterSpinner)view.findViewById(R.id.memorySizeSpinner);
                    BetterSpinner type = (BetterSpinner)view.findViewById(R.id.type);
                    BetterSpinner priceSpinner = (BetterSpinner)view.findViewById(R.id.priceSpinner);

                    if (!manufacturerET.getText().toString().trim().isEmpty()) {
                        if (!isFirst)
                            query += " AND ";
                        else
                            query += " WHERE ";
                        query += ("manufacturer LIKE '%" +  manufacturerET.getText().toString().trim() + "%'");
                        isFirst = false;
                    }

                    if (!codenameET.getText().toString().trim().isEmpty()) {
                        if (!isFirst)
                            query += " AND ";
                        else
                            query += " WHERE ";
                        query += ("codename LIKE '%" +  codenameET.getText().toString().trim() + "%'");
                        isFirst = false;
                    }

                    if (!clockET.getText().toString().trim().isEmpty()) {
                        if (!isFirst)
                            query += " AND ";
                        else
                            query += " WHERE ";

                        String cmd = "";

                        switch(clockSpinner.getText().toString()) {
                            case "<":
                                cmd = "<";
                                break;
                            case "==":
                                cmd = "=";
                                break;
                            case ">":
                                cmd = ">";
                                break;
                        }

                        query += ("clock " + cmd + " " + clockET.getText().toString().trim());
                        isFirst = false;
                    }

                    if (!memorySizeET.getText().toString().trim().isEmpty()) {
                        if (!isFirst)
                            query += " AND ";
                        else
                            query += " WHERE ";

                        String cmd = "";

                        switch(memorySizeSpinner.getText().toString()) {
                            case "<":
                                cmd = "<";
                                break;
                            case "==":
                                cmd = "=";
                                break;
                            case ">":
                                cmd = ">";
                                break;
                        }

                        query += ("memorySize " + cmd + " " + memorySizeET.getText().toString().trim());
                        isFirst = false;
                    }

                    if (!type.getText().toString().equals("Тип памяти") && !type.getText().toString().equals("Не важно")) {
                        if (!isFirst)
                            query += " AND ";
                        else
                            query += " WHERE ";
                        query += ("rName = '" +  type.getText().toString().trim() + "'");
                        isFirst = false;
                    }

                    if (!priceET.getText().toString().trim().isEmpty()) {
                        if (!isFirst)
                            query += " AND ";
                        else
                            query += " WHERE ";

                        String cmd = "";

                        switch(priceSpinner.getText().toString()) {
                            case "<":
                                cmd = "<";
                                break;
                            case "==":
                                cmd = "=";
                                break;
                            case ">":
                                cmd = ">";
                                break;
                        }

                        query += ("price " + cmd + " " + priceET.getText().toString().trim());
                    }

                    ArrayList<RAM> list = Globals.getRamFromQuery(ActivityRam.this, query);

                    //Toast.makeText(ActivityMotherboard.this, query, Toast.LENGTH_LONG).show();
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(ActivityRam.this, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setHasFixedSize(false);

                    RamRecyclerAdapter adapter = new RamRecyclerAdapter(ActivityRam.this, list);
                    recyclerView.setAdapter(adapter);

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ActivityRam.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("ramFilter.manufacturer", manufacturerET.getText().toString().trim());
                    editor.putString("ramFilter.codename", codenameET.getText().toString().trim());
                    editor.putString("ramFilter.clock", clockET.getText().toString().trim());
                    editor.putString("ramFilter.memorySize", memorySizeET.getText().toString().trim());
                    editor.putString("ramFilter.price", priceET.getText().toString().trim());
                    editor.putString("ramFilter.clockSpinner", clockSpinner.getText().toString().trim());
                    editor.putString("ramFilter.memorySizeSpinner", memorySizeSpinner.getText().toString().trim());
                    editor.putString("ramFilter.type", type.getText().toString().trim());
                    editor.putString("ramFilter.priceSpinner", priceSpinner.getText().toString().trim());
                    editor.apply();
                }
            };

            dialog = new MaterialDialog.Builder(ActivityRam.this)
                    .title("Фильтр по материнским платам")
                    .customView(R.layout.prompt_ram_filter, true)
                    .positiveText("Применить")
                    .negativeText("Отмена")
                    .onPositive(callback)
                    .cancelable(true)
                    .build();

            BetterSpinner clockSpinner = (BetterSpinner)dialog.findViewById(R.id.clockSpinner);
            BetterSpinner memorySizeSpinner = (BetterSpinner)dialog.findViewById(R.id.memorySizeSpinner);
            BetterSpinner type = (BetterSpinner)dialog.findViewById(R.id.type);
            BetterSpinner priceSpinner = (BetterSpinner)dialog.findViewById(R.id.priceSpinner);

            EditText manufacturerET = (EditText)dialog.findViewById(R.id.manufacturerET);
            EditText codenameET = (EditText)dialog.findViewById(R.id.codenameET);
            EditText clockET = (EditText)dialog.findViewById(R.id.clockET);
            EditText memorySizeET = (EditText)dialog.findViewById(R.id.memorySizeET);
            EditText priceET = (EditText)dialog.findViewById(R.id.priceET);

            clockSpinner.setAdapter(new ArrayAdapter<>(ActivityRam.this, android.R.layout.simple_dropdown_item_1line, comparisonArray));
            memorySizeSpinner.setAdapter(new ArrayAdapter<>(ActivityRam.this, android.R.layout.simple_dropdown_item_1line, comparisonArray));
            priceSpinner.setAdapter(new ArrayAdapter<>(ActivityRam.this, android.R.layout.simple_dropdown_item_1line, comparisonArray));
            type.setAdapter(new ArrayAdapter<>(ActivityRam.this, android.R.layout.simple_dropdown_item_1line, ramTypesList));

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ActivityRam.this);
            manufacturerET.setText(settings.getString("ramFilter.manufacturer",""));
            codenameET.setText(settings.getString("ramFilter.codename",""));
            clockET.setText(settings.getString("ramFilter.clock",""));
            memorySizeET.setText(settings.getString("ramFilter.memorySize",""));
            priceET.setText(settings.getString("ramFilter.price",""));
            clockSpinner.setText(settings.getString("ramFilter.clockSpinner",comparisonArray[1]));
            memorySizeSpinner.setText(settings.getString("ramFilter.memorySizeSpinner", comparisonArray[1]));
            priceSpinner.setText(settings.getString("ramFilter.priceSpinner",comparisonArray[1]));
            type.setText(settings.getString("ramFilter.type", "Тип памяти"));

            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }
}
