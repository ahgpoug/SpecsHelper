package ahgpoug.com.specshelper;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.BetterSpinner;

import java.util.ArrayList;

import ahgpoug.com.specshelper.objects.CPU;
import ahgpoug.com.specshelper.util.DataBaseHelper;
import ahgpoug.com.specshelper.util.Globals;

public class ActivityAddNewCpu extends AppCompatActivity{
    BetterSpinner betterSpinner;
    EditText manufacturer;
    EditText codename;
    EditText coresCount;
    EditText coreClock;
    EditText gpu;
    EditText process;
    EditText tdp;
    EditText release;
    EditText price;

    ArrayList<String> list;
    ArrayList<String> ids;

    CPU cpu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cpu);

        cpu = (CPU) getIntent().getExtras().getSerializable("cpu");

        initViews();
    }

    private void initViews(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        betterSpinner = (BetterSpinner)findViewById(R.id.socket);
        list = Globals.getSingleField(ActivityAddNewCpu.this, "SELECT * FROM sockets", "socketName");
        ids = Globals.getSingleField(ActivityAddNewCpu.this, "SELECT * FROM sockets", "sid");

        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, list);
        betterSpinner.setAdapter(categoriesAdapter);
        betterSpinner.setText(list.get(0));

        manufacturer = (EditText)findViewById(R.id.manufacturerET);
        codename = (EditText)findViewById(R.id.codenameET);
        coresCount = (EditText)findViewById(R.id.coresCountET);
        coreClock = (EditText)findViewById(R.id.coreClockET);
        gpu = (EditText)findViewById(R.id.gpuET);
        process = (EditText)findViewById(R.id.processET);
        tdp = (EditText)findViewById(R.id.tdpET);
        release = (EditText)findViewById(R.id.releaseET);
        price = (EditText)findViewById(R.id.priceET);

        if (cpu != null){
            manufacturer.setText(cpu.getManufacturer());
            codename.setText(cpu.getCodename());
            coresCount.setText(String.valueOf(cpu.getCoresCount()));
            coreClock.setText(String.valueOf(cpu.getClock()));
            gpu.setText(cpu.getGpuType());
            process.setText(String.valueOf(cpu.getProcess()));
            tdp.setText(String.valueOf(cpu.getTdp()));
            release.setText(String.valueOf(cpu.getRelease()));
            price.setText(String.valueOf(cpu.getPrice()));

            betterSpinner.setText(cpu.getSocket());
        }

        setTitle("CPU");
        initEvents();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void initEvents(){
        findViewById(R.id.submitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (manufacturer.getText().toString().isEmpty() || codename.getText().toString().isEmpty() || coresCount.getText().toString().isEmpty() || coreClock.getText().toString().isEmpty() || process.getText().toString().isEmpty() || tdp.getText().toString().isEmpty() || release.getText().toString().isEmpty() || price.getText().toString().isEmpty()) {
                    Toast.makeText(ActivityAddNewCpu.this, "Необходимо заполнить обязательные все поля", Toast.LENGTH_LONG).show();
                } else {
                    DataBaseHelper mDatabaseHelper = new DataBaseHelper(ActivityAddNewCpu.this);
                    SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

                    int selectedSocket = Integer.parseInt(ids.get(list.indexOf(betterSpinner.getText().toString())));

                    String query = "";
                    if (cpu == null) {
                        query += "INSERT INTO cpu (manufacturer, codename, socket, coresCount, process, clock, tdp, gpuType, release, price) VALUES" +
                                "( '" + manufacturer.getText().toString() + "', '" + codename.getText().toString() +
                                "', " + selectedSocket + ", " + coresCount.getText().toString() +
                                ", " + process.getText().toString() + ", " + coreClock.getText().toString() +
                                ", " + tdp.getText().toString() + ", '" + gpu.getText().toString() +
                                "', " + release.getText().toString() + ", " + price.getText().toString() + ")";
                    } else {
                        query += "UPDATE cpu SET " +
                                "manufacturer = '" + manufacturer.getText().toString() + "', " +
                                "codename = '" + codename.getText().toString() + "', " +
                                "socket = " + selectedSocket + ", " +
                                "coresCount = " + coresCount.getText().toString() + ", " +
                                "process = " + process.getText().toString() + ", " +
                                "clock = " + coreClock.getText().toString() + ", " +
                                "tdp = " + tdp.getText().toString() + ", " +
                                "gpuType = '" + gpu.getText().toString() + "', " +
                                "release = " + release.getText().toString() + ", " +
                                "price = " + price.getText().toString() + " " +
                                "WHERE id = " + cpu.getId();
                    }

                    db.execSQL(query);
                    finish();
                }
            }
        });
    }
}
