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

public class ActivityAddNewRam extends AppCompatActivity{
    BetterSpinner type;

    EditText manufacturer;
    EditText codename;
    EditText clock;
    EditText memorySize;
    EditText price;

    ArrayList<String> list;
    ArrayList<String> ids;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ram);
        initViews();
    }

    private void initViews(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        type = (BetterSpinner)findViewById(R.id.type);
        list = Globals.getSingleField(ActivityAddNewRam.this, "SELECT * FROM ramtypes", "rName");
        list.remove(1); list.remove(2);
        ids = Globals.getSingleField(ActivityAddNewRam.this, "SELECT * FROM ramtypes", "rid");
        ids.remove(1); ids.remove(2);

        ArrayAdapter<String> memoryTypeAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, list);
        type.setAdapter(memoryTypeAdapter);
        type.setText(list.get(0));

        manufacturer = (EditText)findViewById(R.id.manufacturerET);
        codename = (EditText)findViewById(R.id.codenameET);
        clock = (EditText)findViewById(R.id.clockET);
        memorySize = (EditText)findViewById(R.id.memorySizeET);
        price = (EditText)findViewById(R.id.priceET);

        setTitle("Добавление новой RAM");
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
                if (manufacturer.getText().toString().isEmpty() || codename.getText().toString().isEmpty() || clock.getText().toString().isEmpty() || memorySize.getText().toString().isEmpty() || price.getText().toString().isEmpty()) {
                    Toast.makeText(ActivityAddNewRam.this, "Необходимо заполнить обязательные все поля", Toast.LENGTH_LONG).show();
                } else {
                    DataBaseHelper mDatabaseHelper = new DataBaseHelper(ActivityAddNewRam.this);
                    SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

                    int selectedMemoryType = Integer.parseInt(ids.get(list.indexOf(type.getText().toString())));

                    String query = "INSERT INTO ram (manufacturer, codename, clock, memorySize, type, price) VALUES" +
                            "('" + manufacturer.getText().toString() + "', '" + codename.getText().toString() + "', " + clock.getText().toString() + ", " + memorySize.getText().toString() + ", " + selectedMemoryType + ", " + price.getText().toString() + ")";

                    db.execSQL(query);
                    finish();
                }
            }
        });
    }
}
