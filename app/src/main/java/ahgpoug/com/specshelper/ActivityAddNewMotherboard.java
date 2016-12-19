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

public class ActivityAddNewMotherboard extends AppCompatActivity{
    BetterSpinner socket;
    BetterSpinner formFactor;
    BetterSpinner ramType;

    BetterSpinner sli;
    BetterSpinner crossFire;

    EditText manufacturer;
    EditText codename;
    EditText chipSet;
    EditText maxRamCount;
    EditText maxRamSize;
    EditText maxRamClock;
    EditText ideCount;
    EditText sata6count;
    EditText sata3count;
    EditText pcie16count;
    EditText pcie1count;
    EditText usb2count;
    EditText usb3count;
    EditText maxEthernetSpeed;
    EditText price;

    ArrayList<String> socketList;
    ArrayList<String> socketIds;

    ArrayList<String> formFactorList;
    ArrayList<String> formFactorIds;

    ArrayList<String> ramTypeList;
    ArrayList<String> ramTypeIds;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_motherboard);
        initViews();
    }

    private void initViews(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        socket = (BetterSpinner)findViewById(R.id.socket);
        socketList = Globals.getSingleField(ActivityAddNewMotherboard.this, "SELECT * FROM sockets", "socketName");
        socketIds = Globals.getSingleField(ActivityAddNewMotherboard.this, "SELECT * FROM sockets", "sid");

        ArrayAdapter<String> socketAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, socketList);
        socket.setAdapter(socketAdapter);
        socket.setText(socketList.get(0));

        formFactor = (BetterSpinner)findViewById(R.id.formFactor);
        formFactorList = Globals.getSingleField(ActivityAddNewMotherboard.this, "SELECT * FROM formfactors", "ffName");
        formFactorIds = Globals.getSingleField(ActivityAddNewMotherboard.this, "SELECT * FROM formfactors", "ffid");

        ArrayAdapter<String> formFactorAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, formFactorList);
        formFactor.setAdapter(formFactorAdapter);
        formFactor.setText(formFactorList.get(0));

        ramType = (BetterSpinner)findViewById(R.id.ramType);
        ramTypeList = Globals.getSingleField(ActivityAddNewMotherboard.this, "SELECT * FROM ramtypes", "rName");
        ramTypeIds = Globals.getSingleField(ActivityAddNewMotherboard.this, "SELECT * FROM ramtypes", "rid");

        ArrayAdapter<String> ramTypeAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, ramTypeList);
        ramType.setAdapter(ramTypeAdapter);
        ramType.setText(ramTypeList.get(0));

        sli = (BetterSpinner)findViewById(R.id.sli);
        ArrayAdapter<String> sliAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, new String[] {"Нет", "Да"});
        sli.setAdapter(sliAdapter);
        sli.setText("Нет");

        crossFire = (BetterSpinner)findViewById(R.id.cfire);
        ArrayAdapter<String> cfireAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, new String[] {"Нет", "Да"});
        crossFire.setAdapter(cfireAdapter);
        crossFire.setText("Нет");

        manufacturer = (EditText)findViewById(R.id.manufacturerET);
        codename = (EditText)findViewById(R.id.codenameET);
        chipSet = (EditText)findViewById(R.id.chipSetET);
        maxRamCount = (EditText)findViewById(R.id.maxRamCountET);
        maxRamSize = (EditText)findViewById(R.id.maxRamSizeET);
        maxRamClock = (EditText)findViewById(R.id.maxRamClockET);
        ideCount = (EditText)findViewById(R.id.ideCountET);
        sata6count = (EditText)findViewById(R.id.sata6countET);
        sata3count = (EditText)findViewById(R.id.sata3countET);
        pcie16count = (EditText)findViewById(R.id.pcie16countET);
        pcie1count = (EditText)findViewById(R.id.pcie1countET);
        usb2count = (EditText)findViewById(R.id.usb2countET);
        usb3count = (EditText)findViewById(R.id.usb3countET);
        maxEthernetSpeed = (EditText)findViewById(R.id.maxEthernetSpeedET);
        price = (EditText)findViewById(R.id.priceET);

        setTitle("Добавление новой Motherboard");
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
                if (manufacturer.getText().toString().isEmpty() || codename.getText().toString().isEmpty() || chipSet.getText().toString().isEmpty() || maxRamCount.getText().toString().isEmpty() || maxRamSize.getText().toString().isEmpty() || maxRamClock.getText().toString().isEmpty() || ideCount.getText().toString().isEmpty() || sata3count.getText().toString().isEmpty() || pcie16count.getText().toString().isEmpty() || pcie1count.getText().toString().isEmpty() || usb2count.getText().toString().isEmpty() || usb3count.getText().toString().isEmpty() || maxEthernetSpeed.getText().toString().isEmpty() || price.getText().toString().isEmpty()) {
                    Toast.makeText(ActivityAddNewMotherboard.this, "Необходимо заполнить обязательные все поля", Toast.LENGTH_LONG).show();
                } else {
                    DataBaseHelper mDatabaseHelper = new DataBaseHelper(ActivityAddNewMotherboard.this);
                    SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

                    int selectedSocket = Integer.parseInt(socketIds.get(socketList.indexOf(socket.getText().toString())));
                    int selectedFormFactor = Integer.parseInt(formFactorIds.get(formFactorList.indexOf(formFactor.getText().toString())));
                    int selectedRamType = Integer.parseInt(ramTypeIds.get(ramTypeList.indexOf(ramType.getText().toString())));

                    int sliState = 0;
                    int cfireState = 0;

                    if (sli.getText().toString().equals("Да"))
                        sliState = 1;

                    if (crossFire.getText().toString().equals("Да"))
                        cfireState = 1;

                    String query = "INSERT INTO motherboard (manufacturer, codename, socket, formFactor, chipSet, ramType, maxRamCount, maxRamSize, maxRamClock, ideCount, sata6count, sata3count, pcie16count, pcie1count, usb2count, usb3count, maxEthernetSpeed, sli, crossFire, price) VALUES" +
                            "( '" + manufacturer.getText().toString() + "', '" + codename.getText().toString() + "', " + selectedSocket + ", " + selectedFormFactor + ", '" + chipSet.getText().toString() + "', " + selectedRamType + ", " + maxRamCount.getText().toString() + ", '" + maxRamSize.getText().toString() + "', " + maxRamClock.getText().toString() + ", " + ideCount.getText().toString() + ", " + sata6count.getText().toString() + ", " + sata3count.getText().toString() + ", " + pcie16count.getText().toString() + ", " + pcie1count.getText().toString() + ", " + usb2count.getText().toString() + ", " + usb3count.getText().toString() + ", " + maxEthernetSpeed.getText().toString() + ", " + sliState + ", " + cfireState + ", " + price.getText().toString() + ")";

                    db.execSQL(query);
                    finish();
                }
            }
        });
    }
}
