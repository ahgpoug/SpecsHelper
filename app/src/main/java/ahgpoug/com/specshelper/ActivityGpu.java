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

import ahgpoug.com.specshelper.objects.GPU;
import ahgpoug.com.specshelper.adapters.GpuRecyclerAdapter;
import ahgpoug.com.specshelper.util.FiltersHelper;
import ahgpoug.com.specshelper.util.Globals;

public class ActivityGpu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View header;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler);

        FiltersHelper.clearGpuFilter(ActivityGpu.this);

        initViews();
        initDrawer();
        initEvents();
    }

    @Override
    protected void onStop() {
        FiltersHelper.clearGpuFilter(ActivityGpu.this);
        super.onStop();
    }

    @Override
    protected void onResume() {
        initViews();
        super.onResume();
    }

    private void initViews(){
        FiltersHelper.clearGpuFilter(ActivityGpu.this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ActivityGpu.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(false);

        String query = "SELECT * FROM gpu INNER JOIN memory on gpu.memoryType = memory.mid";

        GpuRecyclerAdapter adapter = new GpuRecyclerAdapter(ActivityGpu.this, Globals.getGPUsFromQuery(ActivityGpu.this, query));
        recyclerView.setAdapter(adapter);
    }

    private void initDrawer(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Видоекарты (GPU)");
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
                Intent intent = new Intent(ActivityGpu.this, ActivityCart.class);
                startActivity(intent);
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.cpu) {
            Intent intent = new Intent(ActivityGpu.this, ActivityCpu.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.motherboard) {
            Intent intent = new Intent(ActivityGpu.this, ActivityMotherboard.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.ram) {
            Intent intent = new Intent(ActivityGpu.this, ActivityRam.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.signIn) {
            if (!Globals.isAdmin)
                Globals.showSignInForm(ActivityGpu.this);
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
            Intent intent = new Intent(ActivityGpu.this, ActivityAddNewGpu.class);
            startActivity(intent);
        } else if (id == R.id.action_filter){
            MaterialDialog dialog;
            String[] comparisonArray = new String[] {"<", "==", ">"};
            ArrayList<String> memoryTypeList = Globals.getSingleField(ActivityGpu.this, "SELECT * FROM memory", "mName");
            memoryTypeList.add(0, "Не важно");
            MaterialDialog.SingleButtonCallback callback;

            callback = new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    String query = "SELECT * FROM gpu INNER JOIN memory on gpu.memoryType = memory.mid";

                    boolean isFirst = true;

                    View view = dialog.getView();
                    EditText manufacturerET = (EditText)view.findViewById(R.id.manufacturerET);
                    EditText codenameET = (EditText)view.findViewById(R.id.codenameET);
                    EditText cClockET = (EditText)view.findViewById(R.id.cClockET);
                    EditText mClockET = (EditText)view.findViewById(R.id.mClockET);
                    EditText memorySizeET = (EditText)view.findViewById(R.id.memorySizeET);
                    EditText busET = (EditText)view.findViewById(R.id.busET);
                    EditText processET = (EditText)view.findViewById(R.id.processET);
                    EditText slotsET = (EditText)view.findViewById(R.id.slotsET);
                    EditText priceET = (EditText)view.findViewById(R.id.priceET);

                    BetterSpinner cClockSpinner = (BetterSpinner)view.findViewById(R.id.cClockSpinner);
                    BetterSpinner mClockSpinner = (BetterSpinner)view.findViewById(R.id.mClockSpinner);
                    BetterSpinner memorySizeSpinner = (BetterSpinner)view.findViewById(R.id.memorySizeSpinner);
                    BetterSpinner memoryType = (BetterSpinner)view.findViewById(R.id.memoryType);
                    BetterSpinner busSpinner = (BetterSpinner)view.findViewById(R.id.busSpinner);
                    BetterSpinner processSpinner = (BetterSpinner)view.findViewById(R.id.processSpinner);
                    BetterSpinner slotsSpinner = (BetterSpinner)view.findViewById(R.id.slotsSpinner);
                    BetterSpinner sli = (BetterSpinner)view.findViewById(R.id.sli);
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

                    if (!cClockET.getText().toString().trim().isEmpty()) {
                        if (!isFirst)
                            query += " AND ";
                        else
                            query += " WHERE ";

                        String cmd = "";

                        switch(cClockSpinner.getText().toString()) {
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

                        query += ("cClock " + cmd + " " + cClockET.getText().toString().trim());
                        isFirst = false;
                    }

                    if (!mClockET.getText().toString().trim().isEmpty()) {
                        if (!isFirst)
                            query += " AND ";
                        else
                            query += " WHERE ";

                        String cmd = "";

                        switch(mClockSpinner.getText().toString()) {
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

                        query += ("mClock " + cmd + " " + mClockET.getText().toString().trim());
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

                    if (!memoryType.getText().toString().equals("Тип памяти") && !memoryType.getText().toString().equals("Не важно")) {
                        if (!isFirst)
                            query += " AND ";
                        else
                            query += " WHERE ";
                        query += ("mName = '" +  memoryType.getText().toString().trim() + "'");
                        isFirst = false;
                    }

                    if (!busET.getText().toString().trim().isEmpty()) {
                        if (!isFirst)
                            query += " AND ";
                        else
                            query += " WHERE ";

                        String cmd = "";

                        switch(busSpinner.getText().toString()) {
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

                        query += ("bus " + cmd + " " + busET.getText().toString().trim());
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

                    if (!slotsET.getText().toString().trim().isEmpty()) {
                        if (!isFirst)
                            query += " AND ";
                        else
                            query += " WHERE ";

                        String cmd = "";

                        switch(slotsSpinner.getText().toString()) {
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

                        query += ("slots " + cmd + " " + slotsET.getText().toString().trim());
                        isFirst = false;
                    }

                    if (!sli.getText().toString().equals("Поддержка SLI/CrossFire") && !sli.getText().toString().equals("Не важно")) {
                        if (!isFirst)
                            query += " AND ";
                        else
                            query += " WHERE ";

                        if (sli.getText().toString().equals("Да"))
                            query += ("sli = 1");
                        else
                            query += ("sli = 0");
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

                    ArrayList<GPU> list = Globals.getGPUsFromQuery(ActivityGpu.this, query);

                    //Toast.makeText(ActivityMotherboard.this, query, Toast.LENGTH_LONG).show();
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(ActivityGpu.this, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setHasFixedSize(false);

                    GpuRecyclerAdapter adapter = new GpuRecyclerAdapter(ActivityGpu.this, list);
                    recyclerView.setAdapter(adapter);

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ActivityGpu.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("gpuFilter.manufacturer", manufacturerET.getText().toString().trim());
                    editor.putString("gpuFilter.codename", codenameET.getText().toString().trim());
                    editor.putString("gpuFilter.cClock", cClockET.getText().toString().trim());
                    editor.putString("gpuFilter.mClock", mClockET.getText().toString().trim());
                    editor.putString("gpuFilter.memorySize", memorySizeET.getText().toString().trim());
                    editor.putString("gpuFilter.bus", busET.getText().toString().trim());
                    editor.putString("gpuFilter.process", processET.getText().toString().trim());
                    editor.putString("gpuFilter.slots", slotsET.getText().toString().trim());
                    editor.putString("gpuFilter.price", priceET.getText().toString().trim());
                    editor.putString("gpuFilter.cClockSpinner", cClockSpinner.getText().toString().trim());
                    editor.putString("gpuFilter.mClockSpinner", mClockSpinner.getText().toString().trim());
                    editor.putString("gpuFilter.memorySizeSpinner", memorySizeSpinner.getText().toString().trim());
                    editor.putString("gpuFilter.memoryType", memoryType.getText().toString().trim());
                    editor.putString("gpuFilter.busSpinner", busSpinner.getText().toString().trim());
                    editor.putString("gpuFilter.processSpinner", processSpinner.getText().toString().trim());
                    editor.putString("gpuFilter.slotsSpinner", slotsSpinner.getText().toString().trim());
                    editor.putString("gpuFilter.sli", sli.getText().toString().trim());
                    editor.putString("gpuFilter.priceSpinner", priceSpinner.getText().toString().trim());
                    editor.apply();
                }
            };

            dialog = new MaterialDialog.Builder(ActivityGpu.this)
                    .title("Фильтр по видеокартам")
                    .customView(R.layout.prompt_gpu_filter, true)
                    .positiveText("Применить")
                    .negativeText("Отмена")
                    .onPositive(callback)
                    .cancelable(true)
                    .build();

            BetterSpinner cClockSpinner = (BetterSpinner)dialog.findViewById(R.id.cClockSpinner);
            BetterSpinner mClockSpinner = (BetterSpinner)dialog.findViewById(R.id.mClockSpinner);
            BetterSpinner memorySizeSpinner = (BetterSpinner)dialog.findViewById(R.id.memorySizeSpinner);
            BetterSpinner memoryType = (BetterSpinner)dialog.findViewById(R.id.memoryType);
            BetterSpinner busSpinner = (BetterSpinner)dialog.findViewById(R.id.busSpinner);
            BetterSpinner processSpinner = (BetterSpinner)dialog.findViewById(R.id.processSpinner);
            BetterSpinner slotsSpinner = (BetterSpinner)dialog.findViewById(R.id.slotsSpinner);
            BetterSpinner sli = (BetterSpinner)dialog.findViewById(R.id.sli);
            BetterSpinner priceSpinner = (BetterSpinner)dialog.findViewById(R.id.priceSpinner);

            EditText manufacturerET = (EditText)dialog.findViewById(R.id.manufacturerET);
            EditText codenameET = (EditText)dialog.findViewById(R.id.codenameET);
            EditText cClockET = (EditText)dialog.findViewById(R.id.cClockET);
            EditText mClockET = (EditText)dialog.findViewById(R.id.mClockET);
            EditText memorySizeET = (EditText)dialog.findViewById(R.id.memorySizeET);
            EditText busET = (EditText)dialog.findViewById(R.id.busET);
            EditText processET = (EditText)dialog.findViewById(R.id.processET);
            EditText slotsET = (EditText)dialog.findViewById(R.id.slotsET);
            EditText priceET = (EditText)dialog.findViewById(R.id.priceET);

            cClockSpinner.setAdapter(new ArrayAdapter<>(ActivityGpu.this, android.R.layout.simple_dropdown_item_1line, comparisonArray));
            mClockSpinner.setAdapter(new ArrayAdapter<>(ActivityGpu.this, android.R.layout.simple_dropdown_item_1line, comparisonArray));
            memorySizeSpinner.setAdapter(new ArrayAdapter<>(ActivityGpu.this, android.R.layout.simple_dropdown_item_1line, comparisonArray));
            priceSpinner.setAdapter(new ArrayAdapter<>(ActivityGpu.this, android.R.layout.simple_dropdown_item_1line, comparisonArray));
            memoryType.setAdapter(new ArrayAdapter<>(ActivityGpu.this, android.R.layout.simple_dropdown_item_1line, memoryTypeList));
            busSpinner.setAdapter(new ArrayAdapter<>(ActivityGpu.this, android.R.layout.simple_dropdown_item_1line, comparisonArray));
            processSpinner.setAdapter(new ArrayAdapter<>(ActivityGpu.this, android.R.layout.simple_dropdown_item_1line, comparisonArray));
            slotsSpinner.setAdapter(new ArrayAdapter<>(ActivityGpu.this, android.R.layout.simple_dropdown_item_1line, comparisonArray));
            sli.setAdapter(new ArrayAdapter<>(ActivityGpu.this, android.R.layout.simple_dropdown_item_1line, new String[] {"Да", "Нет", "Не важно"}));

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ActivityGpu.this);
            manufacturerET.setText(settings.getString("gpuFilter.manufacturer",""));
            codenameET.setText(settings.getString("gpuFilter.codename",""));
            cClockET.setText(settings.getString("gpuFilter.cClock",""));
            mClockET.setText(settings.getString("gpuFilter.mClock",""));
            memorySizeET.setText(settings.getString("gpuFilter.memorySize",""));
            busET.setText(settings.getString("gpuFilter.bus",""));
            processET.setText(settings.getString("gpuFilter.process",""));
            slotsET.setText(settings.getString("gpuFilter.slots", ""));
            priceET.setText(settings.getString("gpuFilter.price",""));
            sli.setText(settings.getString("gpuFilter.sli","Поддержка SLI/CrossFire"));
            cClockSpinner.setText(settings.getString("gpuFilter.cClockSpinner", comparisonArray[1]));
            mClockSpinner.setText(settings.getString("gpuFilter.mClockSpinner",comparisonArray[1]));
            memorySizeSpinner.setText(settings.getString("gpuFilter.memorySizeSpinner",comparisonArray[1]));
            memoryType.setText(settings.getString("gpuFilter.memoryType", "Тип памяти"));
            busSpinner.setText(settings.getString("gpuFilter.busSpinner",comparisonArray[1]));
            processSpinner.setText(settings.getString("gpuFilter.processSpinner",comparisonArray[1]));
            slotsSpinner.setText(settings.getString("gpuFilter.slotsSpinner",comparisonArray[1]));
            priceSpinner.setText(settings.getString("gpuFilter.priceSpinner",comparisonArray[1]));
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }
}
