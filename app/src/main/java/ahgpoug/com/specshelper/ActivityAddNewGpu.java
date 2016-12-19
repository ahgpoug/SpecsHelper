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

import ahgpoug.com.specshelper.util.DataBaseHelper;
import ahgpoug.com.specshelper.util.Globals;

public class ActivityAddNewGpu extends AppCompatActivity{
    BetterSpinner memoryType;
    BetterSpinner sli;

    EditText manufacturer;
    EditText codename;
    EditText cClock;
    EditText mClock;
    EditText memorySize;
    EditText bus;
    EditText process;
    EditText slots;
    EditText price;

    ArrayList<String> list;
    ArrayList<String> ids;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gpu);
        initViews();
    }

    private void initViews(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        memoryType = (BetterSpinner)findViewById(R.id.memoryType);
        list = Globals.getSingleField(ActivityAddNewGpu.this, "SELECT * FROM memory", "mName");
        ids = Globals.getSingleField(ActivityAddNewGpu.this, "SELECT * FROM memory", "mid");

        ArrayAdapter<String> memoryTypeAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, list);
        memoryType.setAdapter(memoryTypeAdapter);
        memoryType.setText(list.get(0));

        sli = (BetterSpinner)findViewById(R.id.sli);
        ArrayAdapter<String> sliAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, new String[] {"Нет", "Да"});
        sli.setAdapter(sliAdapter);
        sli.setText("Нет");

        manufacturer = (EditText)findViewById(R.id.manufacturerET);
        codename = (EditText)findViewById(R.id.codenameET);
        cClock = (EditText)findViewById(R.id.cClockET);
        mClock = (EditText)findViewById(R.id.mClockET);
        memorySize = (EditText)findViewById(R.id.memorySizeET);
        process = (EditText)findViewById(R.id.processET);
        bus = (EditText)findViewById(R.id.busET);
        process = (EditText)findViewById(R.id.processET);
        slots = (EditText)findViewById(R.id.slotsET);
        price = (EditText)findViewById(R.id.priceET);

        setTitle("Добавление нового GPU");
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
                if (manufacturer.getText().toString().isEmpty() || codename.getText().toString().isEmpty() || cClock.getText().toString().isEmpty() || mClock.getText().toString().isEmpty() || memorySize.getText().toString().isEmpty() || bus.getText().toString().isEmpty() || process.getText().toString().isEmpty() || slots.getText().toString().isEmpty() || price.getText().toString().isEmpty()) {
                    Toast.makeText(ActivityAddNewGpu.this, "Необходимо заполнить обязательные все поля", Toast.LENGTH_LONG).show();
                } else {
                    DataBaseHelper mDatabaseHelper = new DataBaseHelper(ActivityAddNewGpu.this);
                    SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

                    int selectedMemoryType = Integer.parseInt(ids.get(list.indexOf(memoryType.getText().toString())));

                    int sliState = 0;

                    if (sli.getText().toString().equals("Да"))
                        sliState = 1;

                    String query = "INSERT INTO gpu (manufacturer, codename, cClock, mClock, memorySize, bus, process, slots, sli, memoryType, price) VALUES" +
                            "('" + manufacturer.getText().toString() + "', '" + codename.getText().toString() + "', " + cClock.getText().toString() + ", " + mClock.getText().toString() + ", " + memorySize.getText().toString() + ", " + bus.getText().toString() + ", " + process.getText().toString() + ", " + slots.getText().toString() + ", " + sliState + ", " + selectedMemoryType + ", " + price.getText().toString() + ")";

                    db.execSQL(query);
                    finish();
                }
            }
        });
    }
}
