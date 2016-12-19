package ahgpoug.com.specshelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import ahgpoug.com.specshelper.objects.CPU;
import ahgpoug.com.specshelper.adapters.CpuRecyclerAdapter;
import ahgpoug.com.specshelper.util.FiltersHelper;
import ahgpoug.com.specshelper.util.Globals;

public class ActivityCpu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler);

        FiltersHelper.clearCpuFilter(ActivityCpu.this);

        initViews();
        initDrawer();
        initEvents();
    }

    @Override
    protected void onStop() {
        FiltersHelper.clearCpuFilter(ActivityCpu.this);
        super.onStop();
    }

    @Override
    protected void onResume() {
        initViews();
        super.onResume();
    }

    private void initViews(){
        FiltersHelper.clearCpuFilter(ActivityCpu.this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ActivityCpu.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(false);

        String query = "SELECT * FROM cpu INNER JOIN sockets on cpu.socket = sockets.sid";

        CpuRecyclerAdapter adapter = new CpuRecyclerAdapter(ActivityCpu.this, Globals.getCPUsFromQuery(ActivityCpu.this, query));
        recyclerView.setAdapter(adapter);
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
                Intent intent = new Intent(ActivityCpu.this, ActivityCart.class);
                startActivity(intent);
            }
        });
    }

    private void initDrawer(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Процессоры (CPU)");
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.motherboard) {
            Intent intent = new Intent(ActivityCpu.this, ActivityMotherboard.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.gpu) {
            Intent intent = new Intent(ActivityCpu.this, ActivityGpu.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.ram) {
            Intent intent = new Intent(ActivityCpu.this, ActivityRam.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.signIn) {
            if (!Globals.isAdmin)
                Globals.showSignInForm(ActivityCpu.this);
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
            Intent intent = new Intent(ActivityCpu.this, ActivityAddNewCpu.class);
            startActivity(intent);
        } else if (id == R.id.action_filter){
            MaterialDialog dialog;
            String[] comparisonArray = new String[] {"<", "==", ">"};
            ArrayList<String> socketList = Globals.getSingleField(ActivityCpu.this, "SELECT * FROM sockets", "socketName");
            socketList.add(0, "Не важно");
            MaterialDialog.SingleButtonCallback callback;

            callback = new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    String query = "SELECT * FROM cpu INNER JOIN sockets ON cpu.socket = sockets.id";

                    boolean isFirst = true;

                    View view = dialog.getView();
                    EditText manufacturerET = (EditText)view.findViewById(R.id.manufacturerET);
                    EditText codenameET = (EditText)view.findViewById(R.id.codenameET);
                    EditText coresCountET = (EditText)view.findViewById(R.id.coresCountET);
                    EditText clockET = (EditText)view.findViewById(R.id.clockET);
                    EditText processET = (EditText)view.findViewById(R.id.processET);
                    EditText tdpET = (EditText)view.findViewById(R.id.tdpET);
                    EditText releaseET = (EditText)view.findViewById(R.id.releaseET);
                    EditText priceET = (EditText)view.findViewById(R.id.priceET);

                    BetterSpinner socket = (BetterSpinner)view.findViewById(R.id.socket);
                    BetterSpinner coresSpinner = (BetterSpinner)view.findViewById(R.id.coresSpinner);
                    BetterSpinner clockSpinner = (BetterSpinner)view.findViewById(R.id.clockSpinner);
                    BetterSpinner processSpinner = (BetterSpinner)view.findViewById(R.id.processSpinner);
                    BetterSpinner tdpSpinner = (BetterSpinner)view.findViewById(R.id.tdpSpinner);
                    BetterSpinner releaseSpinner = (BetterSpinner)view.findViewById(R.id.releaseSpinner);
                    BetterSpinner priceSpinner = (BetterSpinner)view.findViewById(R.id.priceSpinner);
                    BetterSpinner gpu = (BetterSpinner)view.findViewById(R.id.gpu);

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

                    if (!socket.getText().toString().equals("Socket") && !socket.getText().toString().equals("Не важно")) {
                        if (!isFirst)
                            query += " AND ";
                        else
                            query += " WHERE ";
                        query += ("socketName = '" +  socket.getText().toString().trim() + "'");
                        isFirst = false;
                    }

                    if (!coresCountET.getText().toString().trim().isEmpty()) {
                        if (!isFirst)
                            query += " AND ";
                        else
                            query += " WHERE ";

                        String cmd = "";

                        switch(coresSpinner.getText().toString()) {
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

                        query += ("coresCount " + cmd + " " + coresCountET.getText().toString().trim());
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

                    if (!gpu.getText().toString().equals("Встроенный GPU") && !gpu.getText().toString().equals("Не важно")) {
                        if (!isFirst)
                            query += " AND ";
                        else
                            query += " WHERE ";

                        if (gpu.getText().toString().equals("Да"))
                            query += ("gpuType <> ''");
                        else
                            query += ("gpuType = ''");
                        isFirst = false;
                    }

                    if (!processET.getText().toString().trim().isEmpty()) {
                        if (!isFirst)
                            query += " AND ";
                        else
                            query += " WHERE ";

                        String cmd = "";

                        switch(processSpinner.getText().toString()) {
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

                        query += ("process " + cmd + " " + processET.getText().toString().trim());
                        isFirst = false;
                    }

                    if (!tdpET.getText().toString().trim().isEmpty()) {
                        if (!isFirst)
                            query += " AND ";
                        else
                            query += " WHERE ";

                        String cmd = "";

                        switch(tdpSpinner.getText().toString()) {
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

                        query += ("tdp " + cmd + " " + tdpET.getText().toString().trim());
                        isFirst = false;
                    }

                    if (!releaseET.getText().toString().trim().isEmpty()) {
                        if (!isFirst)
                            query += " AND ";
                        else
                            query += " WHERE ";

                        String cmd = "";

                        switch(releaseSpinner.getText().toString()) {
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

                        query += ("tdp " + cmd + " " + releaseET.getText().toString().trim());
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

                    ArrayList<CPU> list = Globals.getCPUsFromQuery(ActivityCpu.this, query);

                    //Toast.makeText(ActivityCpu.this, query, Toast.LENGTH_LONG).show();
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(ActivityCpu.this, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setHasFixedSize(false);

                    CpuRecyclerAdapter adapter = new CpuRecyclerAdapter(ActivityCpu.this, list);
                    recyclerView.setAdapter(adapter);

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ActivityCpu.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("cpuFilter.manufacturer", manufacturerET.getText().toString().trim());
                    editor.putString("cpuFilter.codename", codenameET.getText().toString().trim());
                    editor.putString("cpuFilter.socket", socket.getText().toString().trim());
                    editor.putString("cpuFilter.coresCountSpinner", coresSpinner.getText().toString().trim());
                    editor.putString("cpuFilter.coresCount", coresCountET.getText().toString().trim());
                    editor.putString("cpuFilter.clockSpinner", clockSpinner.getText().toString().trim());
                    editor.putString("cpuFilter.clock", clockET.getText().toString().trim());
                    editor.putString("cpuFilter.gpu", gpu.getText().toString().trim());
                    editor.putString("cpuFilter.processSpinner", processSpinner.getText().toString().trim());
                    editor.putString("cpuFilter.process", processET.getText().toString().trim());
                    editor.putString("cpuFilter.tdpSpinner", tdpSpinner.getText().toString().trim());
                    editor.putString("cpuFilter.tdp", tdpET.getText().toString().trim());
                    editor.putString("cpuFilter.releaseSpinner", releaseSpinner.getText().toString().trim());
                    editor.putString("cpuFilter.release", releaseET.getText().toString().trim());
                    editor.putString("cpuFilter.priceSpinner", priceSpinner.getText().toString().trim());
                    editor.putString("cpuFilter.price", priceET.getText().toString().trim());
                    editor.apply();
                }
            };

            dialog = new MaterialDialog.Builder(ActivityCpu.this)
                    .title("Фильтр по CPU")
                    .customView(R.layout.prompt_cpu_filter, true)
                    .positiveText("Применить")
                    .negativeText("Отмена")
                    .onPositive(callback)
                    .cancelable(true)
                    .build();

            BetterSpinner socket = (BetterSpinner)dialog.findViewById(R.id.socket);
            BetterSpinner coresSpinner = (BetterSpinner)dialog.findViewById(R.id.coresSpinner);
            BetterSpinner clockSpinner = (BetterSpinner)dialog.findViewById(R.id.clockSpinner);
            BetterSpinner processSpinner = (BetterSpinner)dialog.findViewById(R.id.processSpinner);
            BetterSpinner tdpSpinner = (BetterSpinner)dialog.findViewById(R.id.tdpSpinner);
            BetterSpinner releaseSpinner = (BetterSpinner)dialog.findViewById(R.id.releaseSpinner);
            BetterSpinner priceSpinner = (BetterSpinner)dialog.findViewById(R.id.priceSpinner);
            BetterSpinner gpu = (BetterSpinner)dialog.findViewById(R.id.gpu);

            EditText manufacturerET = (EditText)dialog.findViewById(R.id.manufacturerET);
            EditText codenameET = (EditText)dialog.findViewById(R.id.codenameET);
            EditText coresCountET = (EditText)dialog.findViewById(R.id.coresCountET);
            EditText clockET = (EditText)dialog.findViewById(R.id.clockET);
            EditText processET = (EditText)dialog.findViewById(R.id.processET);
            EditText tdpET = (EditText)dialog.findViewById(R.id.tdpET);
            EditText releaseET = (EditText)dialog.findViewById(R.id.releaseET);
            EditText priceET = (EditText)dialog.findViewById(R.id.priceET);

            coresSpinner.setAdapter(new ArrayAdapter<>(ActivityCpu.this, android.R.layout.simple_dropdown_item_1line, comparisonArray));
            clockSpinner.setAdapter(new ArrayAdapter<>(ActivityCpu.this, android.R.layout.simple_dropdown_item_1line, comparisonArray));
            processSpinner.setAdapter(new ArrayAdapter<>(ActivityCpu.this, android.R.layout.simple_dropdown_item_1line, comparisonArray));
            tdpSpinner.setAdapter(new ArrayAdapter<>(ActivityCpu.this, android.R.layout.simple_dropdown_item_1line, comparisonArray));
            releaseSpinner.setAdapter(new ArrayAdapter<>(ActivityCpu.this, android.R.layout.simple_dropdown_item_1line, comparisonArray));
            priceSpinner.setAdapter(new ArrayAdapter<>(ActivityCpu.this, android.R.layout.simple_dropdown_item_1line, comparisonArray));
            socket.setAdapter(new ArrayAdapter<>(ActivityCpu.this, android.R.layout.simple_dropdown_item_1line, socketList));
            gpu.setAdapter(new ArrayAdapter<>(ActivityCpu.this, android.R.layout.simple_dropdown_item_1line, new String[] {"Да", "Нет", "Не важно"}));

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ActivityCpu.this);
            manufacturerET.setText(settings.getString("cpuFilter.manufacturer",""));
            codenameET.setText(settings.getString("cpuFilter.codename",""));
            socket.setText(settings.getString("cpuFilter.socket","Socket"));
            coresSpinner.setText(settings.getString("cpuFilter.coresCountSpinner",comparisonArray[1]));
            coresCountET.setText(settings.getString("cpuFilter.coresCount",""));
            clockSpinner.setText(settings.getString("cpuFilter.clockSpinner",comparisonArray[1]));
            clockET.setText(settings.getString("cpuFilter.clock",""));
            gpu.setText(settings.getString("cpuFilter.gpu","Встроенный GPU"));
            processSpinner.setText(settings.getString("cpuFilter.processSpinner",comparisonArray[1]));
            processET.setText(settings.getString("cpuFilter.process",""));
            tdpSpinner.setText(settings.getString("cpuFilter.tdpSpinner",comparisonArray[1]));
            tdpET.setText(settings.getString("cpuFilter.tdp",""));
            releaseSpinner.setText(settings.getString("cpuFilter.releaseSpinner",comparisonArray[1]));
            releaseET.setText(settings.getString("cpuFilter.release",""));
            priceSpinner.setText(settings.getString("cpuFilter.priceSpinner",comparisonArray[1]));
            priceET.setText(settings.getString("cpuFilter.price",""));

            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }
}
