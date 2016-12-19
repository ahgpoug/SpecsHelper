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

import java.io.Serializable;
import java.util.ArrayList;

import ahgpoug.com.specshelper.objects.Motherboard;
import ahgpoug.com.specshelper.adapters.MotherboardRecyclerAdapter;
import ahgpoug.com.specshelper.util.FiltersHelper;
import ahgpoug.com.specshelper.util.Globals;

public class ActivityMotherboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler);

        FiltersHelper.clearMotherboardFilter(ActivityMotherboard.this);

        initViews();
        initDrawer();
        initEvents();
    }

    @Override
    protected void onStop() {
        FiltersHelper.clearMotherboardFilter(ActivityMotherboard.this);
        super.onStop();
    }

    @Override
    protected void onResume() {
        initViews();
        super.onResume();
    }

    private void initViews(){
        FiltersHelper.clearMotherboardFilter(ActivityMotherboard.this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ActivityMotherboard.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(false);

        String query = "SELECT * FROM motherboard " +
                "INNER JOIN sockets on motherboard.socket = sockets.sid " +
                "INNER JOIN formfactors on motherboard.formFactor = formfactors.ffid " +
                "INNER JOIN ramtypes on motherboard.ramType = ramtypes.rid";

        MotherboardRecyclerAdapter adapter = new MotherboardRecyclerAdapter(ActivityMotherboard.this, Globals.getMotherboardsFromQuery(ActivityMotherboard.this, query));
        recyclerView.setAdapter(adapter);
    }

    private void initDrawer(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Материнские платы (Motherboards)");
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
                Intent intent = new Intent(ActivityMotherboard.this, ActivityCart.class);
                startActivity(intent);
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.cpu) {
            Intent intent = new Intent(ActivityMotherboard.this, ActivityCpu.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.gpu) {
            Intent intent = new Intent(ActivityMotherboard.this, ActivityGpu.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.ram) {
            Intent intent = new Intent(ActivityMotherboard.this, ActivityRam.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.signIn) {
            if (!Globals.isAdmin)
                Globals.showSignInForm(ActivityMotherboard.this);
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
            Intent intent = new Intent(ActivityMotherboard.this, ActivityAddNewMotherboard.class);
            intent.putExtra("mb", (Serializable) null);
            startActivity(intent);
        } else if (id == R.id.action_filter){
            MaterialDialog dialog;
            String[] comparisonArray = new String[] {"<", "==", ">"};
            ArrayList<String> socketList = Globals.getSingleField(ActivityMotherboard.this, "SELECT * FROM sockets", "socketName");
            ArrayList<String> formFactorList = Globals.getSingleField(ActivityMotherboard.this, "SELECT * FROM formfactors", "ffName");
            ArrayList<String> ramTypesList = Globals.getSingleField(ActivityMotherboard.this, "SELECT * FROM ramtypes", "rName");
            socketList.add(0, "Не важно");
            formFactorList.add(0, "Не важно");
            ramTypesList.add(0, "Не важно");
            MaterialDialog.SingleButtonCallback callback;

            callback = new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    String query = "SELECT * FROM motherboard " +
                            "INNER JOIN sockets on motherboard.socket = sockets.sid " +
                            "INNER JOIN formfactors on motherboard.formFactor = formfactors.ffid " +
                            "INNER JOIN ramtypes on motherboard.ramType = ramtypes.rid";

                    boolean isFirst = true;

                    View view = dialog.getView();
                    EditText manufacturerET = (EditText)view.findViewById(R.id.manufacturerET);
                    EditText codenameET = (EditText)view.findViewById(R.id.codenameET);
                    EditText chipSetET = (EditText)view.findViewById(R.id.chipSetET);
                    EditText maxRamCountET = (EditText)view.findViewById(R.id.maxRamCountET);
                    EditText maxRamSizeET = (EditText)view.findViewById(R.id.maxRamSizeET);
                    EditText maxRamClockET = (EditText)view.findViewById(R.id.maxRamClockET);
                    EditText priceET = (EditText)view.findViewById(R.id.priceET);

                    BetterSpinner socket = (BetterSpinner)view.findViewById(R.id.socket);
                    BetterSpinner formFactor = (BetterSpinner)view.findViewById(R.id.formFactor);
                    BetterSpinner sli = (BetterSpinner)view.findViewById(R.id.sli);
                    BetterSpinner cfire = (BetterSpinner)view.findViewById(R.id.cfire);
                    BetterSpinner ramType = (BetterSpinner)view.findViewById(R.id.ramType);
                    BetterSpinner maxRamCountSpinner = (BetterSpinner)view.findViewById(R.id.maxRamCountSpinner);
                    BetterSpinner maxRamSizeSpinner = (BetterSpinner)view.findViewById(R.id.maxRamSizeSpinner);
                    BetterSpinner maxRamClockSpinner = (BetterSpinner)view.findViewById(R.id.maxRamClockSpinner);
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

                    if (!socket.getText().toString().equals("Socket") && !socket.getText().toString().equals("Не важно")) {
                        if (!isFirst)
                            query += " AND ";
                        else
                            query += " WHERE ";
                        query += ("socketName = '" +  socket.getText().toString().trim() + "'");
                        isFirst = false;
                    }

                    if (!formFactor.getText().toString().equals("Форм-фактор") && !formFactor.getText().toString().equals("Не важно")) {
                        if (!isFirst)
                            query += " AND ";
                        else
                            query += " WHERE ";
                        query += ("ffName = '" +  formFactor.getText().toString().trim() + "'");
                        isFirst = false;
                    }

                    if (!chipSetET.getText().toString().trim().isEmpty()) {
                        if (!isFirst)
                            query += " AND ";
                        else
                            query += " WHERE ";
                        query += ("chipSet LIKE '%" +  chipSetET.getText().toString().trim() + "%'");
                        isFirst = false;
                    }

                    if (!ramType.getText().toString().equals("Тип RAM") && !ramType.getText().toString().equals("Не важно")) {
                        if (!isFirst)
                            query += " AND ";
                        else
                            query += " WHERE ";
                        query += ("rName = '" +  ramType.getText().toString().trim() + "'");
                        isFirst = false;
                    }

                    if (!maxRamCountET.getText().toString().trim().isEmpty()) {
                        if (!isFirst)
                            query += " AND ";
                        else
                            query += " WHERE ";

                        String cmd = "";

                        switch(maxRamCountSpinner.getText().toString()) {
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

                        query += ("maxRamCount " + cmd + " " + maxRamCountET.getText().toString().trim());
                        isFirst = false;
                    }

                    if (!maxRamSizeET.getText().toString().trim().isEmpty()) {
                        if (!isFirst)
                            query += " AND ";
                        else
                            query += " WHERE ";

                        String cmd = "";

                        switch(maxRamSizeSpinner.getText().toString()) {
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

                        query += ("maxRamSize " + cmd + " " + maxRamSizeET.getText().toString().trim());
                        isFirst = false;
                    }

                    if (!maxRamClockET.getText().toString().trim().isEmpty()) {
                        if (!isFirst)
                            query += " AND ";
                        else
                            query += " WHERE ";

                        String cmd = "";

                        switch(maxRamClockSpinner.getText().toString()) {
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

                        query += ("maxRamClock " + cmd + " " + maxRamClockET.getText().toString().trim());
                        isFirst = false;
                    }

                    if (!sli.getText().toString().equals("Поддержка SLI") && !sli.getText().toString().equals("Не важно")) {
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

                    if (!cfire.getText().toString().equals("Поддержка CrossFire") && !cfire.getText().toString().equals("Не важно")) {
                        if (!isFirst)
                            query += " AND ";
                        else
                            query += " WHERE ";

                        if (cfire.getText().toString().equals("Да"))
                            query += ("crossFire = 1");
                        else
                            query += ("crossFire = 0");
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

                    ArrayList<Motherboard> list = Globals.getMotherboardsFromQuery(ActivityMotherboard.this, query);

                    //Toast.makeText(ActivityMotherboard.this, query, Toast.LENGTH_LONG).show();
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(ActivityMotherboard.this, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setHasFixedSize(false);

                    MotherboardRecyclerAdapter adapter = new MotherboardRecyclerAdapter(ActivityMotherboard.this, list);
                    recyclerView.setAdapter(adapter);

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ActivityMotherboard.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("mbFilter.manufacturer", manufacturerET.getText().toString().trim());
                    editor.putString("mbFilter.codename", codenameET.getText().toString().trim());
                    editor.putString("mbFilter.socket", socket.getText().toString().trim());
                    editor.putString("mbFilter.formFactor", formFactor.getText().toString().trim());
                    editor.putString("mbFilter.chipSet", chipSetET.getText().toString().trim());
                    editor.putString("mbFilter.ramType", ramType.getText().toString().trim());
                    editor.putString("mbFilter.maxRamCount", maxRamCountET.getText().toString().trim());
                    editor.putString("mbFilter.maxRamSize", maxRamSizeET.getText().toString().trim());
                    editor.putString("mbFilter.maxRamClock", maxRamClockET.getText().toString().trim());
                    editor.putString("mbFilter.maxRamCountSpinner", maxRamCountSpinner.getText().toString().trim());
                    editor.putString("mbFilter.maxRamSizeSpinner", maxRamSizeSpinner.getText().toString().trim());
                    editor.putString("mbFilter.maxRamClockSpinner", maxRamClockSpinner.getText().toString().trim());
                    editor.putString("mbFilter.sli", sli.getText().toString().trim());
                    editor.putString("mbFilter.cfire", cfire.getText().toString().trim());
                    editor.putString("mbFilter.price", priceET.getText().toString().trim());
                    editor.putString("mbFilter.priceSpinner", priceSpinner.getText().toString().trim());
                    editor.apply();
                }
            };

            dialog = new MaterialDialog.Builder(ActivityMotherboard.this)
                    .title("Фильтр по материнским платам")
                    .customView(R.layout.prompt_motherboard_filter, true)
                    .positiveText("Применить")
                    .negativeText("Отмена")
                    .onPositive(callback)
                    .cancelable(true)
                    .build();

            BetterSpinner socket = (BetterSpinner)dialog.findViewById(R.id.socket);
            BetterSpinner formFactor = (BetterSpinner)dialog.findViewById(R.id.formFactor);
            BetterSpinner sli = (BetterSpinner)dialog.findViewById(R.id.sli);
            BetterSpinner cfire = (BetterSpinner)dialog.findViewById(R.id.cfire);
            BetterSpinner ramType = (BetterSpinner)dialog.findViewById(R.id.ramType);
            BetterSpinner maxRamCountSpinner = (BetterSpinner)dialog.findViewById(R.id.maxRamCountSpinner);
            BetterSpinner maxRamSizeSpinner = (BetterSpinner)dialog.findViewById(R.id.maxRamSizeSpinner);
            BetterSpinner maxRamClockSpinner = (BetterSpinner)dialog.findViewById(R.id.maxRamClockSpinner);
            BetterSpinner priceSpinner = (BetterSpinner)dialog.findViewById(R.id.priceSpinner);

            EditText manufacturerET = (EditText)dialog.findViewById(R.id.manufacturerET);
            EditText codenameET = (EditText)dialog.findViewById(R.id.codenameET);
            EditText chipSetET = (EditText)dialog.findViewById(R.id.chipSetET);
            EditText maxRamCountET = (EditText)dialog.findViewById(R.id.maxRamCountET);
            EditText maxRamSizeET = (EditText)dialog.findViewById(R.id.maxRamSizeET);
            EditText maxRamClockET = (EditText)dialog.findViewById(R.id.maxRamClockET);
            EditText priceET = (EditText)dialog.findViewById(R.id.priceET);

            maxRamCountSpinner.setAdapter(new ArrayAdapter<>(ActivityMotherboard.this, android.R.layout.simple_dropdown_item_1line, comparisonArray));
            maxRamSizeSpinner.setAdapter(new ArrayAdapter<>(ActivityMotherboard.this, android.R.layout.simple_dropdown_item_1line, comparisonArray));
            maxRamClockSpinner.setAdapter(new ArrayAdapter<>(ActivityMotherboard.this, android.R.layout.simple_dropdown_item_1line, comparisonArray));
            priceSpinner.setAdapter(new ArrayAdapter<>(ActivityMotherboard.this, android.R.layout.simple_dropdown_item_1line, comparisonArray));
            socket.setAdapter(new ArrayAdapter<>(ActivityMotherboard.this, android.R.layout.simple_dropdown_item_1line, socketList));
            formFactor.setAdapter(new ArrayAdapter<>(ActivityMotherboard.this, android.R.layout.simple_dropdown_item_1line, formFactorList));
            ramType.setAdapter(new ArrayAdapter<>(ActivityMotherboard.this, android.R.layout.simple_dropdown_item_1line, ramTypesList));
            sli.setAdapter(new ArrayAdapter<>(ActivityMotherboard.this, android.R.layout.simple_dropdown_item_1line, new String[] {"Да", "Нет", "Не важно"}));
            cfire.setAdapter(new ArrayAdapter<>(ActivityMotherboard.this, android.R.layout.simple_dropdown_item_1line, new String[] {"Да", "Нет", "Не важно"}));

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ActivityMotherboard.this);
            manufacturerET.setText(settings.getString("mbFilter.manufacturer",""));
            codenameET.setText(settings.getString("mbFilter.codename",""));
            socket.setText(settings.getString("mbFilter.socket","Socket"));
            formFactor.setText(settings.getString("mbFilter.formFactor","Форм-фактор"));
            ramType.setText(settings.getString("mbFilter.ramType","Тип RAM"));
            chipSetET.setText(settings.getString("mbFilter.chipSet",""));
            maxRamCountSpinner.setText(settings.getString("mbFilter.maxRamCountSpinner",comparisonArray[1]));
            maxRamCountET.setText(settings.getString("mbFilter.maxRamCount", ""));
            maxRamSizeSpinner.setText(settings.getString("mbFilter.maxRamSizeSpinner",comparisonArray[1]));
            maxRamSizeET.setText(settings.getString("mbFilter.maxRamSize", ""));
            maxRamClockSpinner.setText(settings.getString("mbFilter.maxRamClockSpinner",comparisonArray[1]));
            maxRamClockET.setText(settings.getString("mbFilter.maxRamClock", ""));
            sli.setText(settings.getString("mbFilter.sli","Поддержка SLI"));
            cfire.setText(settings.getString("mbFilter.cfire","Поддержка CrossFire"));
            priceSpinner.setText(settings.getString("mbFilter.priceSpinner",comparisonArray[1]));
            priceET.setText(settings.getString("mbFilter.price",""));

            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }
}
