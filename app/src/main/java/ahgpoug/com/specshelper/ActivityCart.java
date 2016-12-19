package ahgpoug.com.specshelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import ahgpoug.com.specshelper.objects.CPU;
import ahgpoug.com.specshelper.objects.Cart;
import ahgpoug.com.specshelper.objects.GPU;
import ahgpoug.com.specshelper.objects.Motherboard;
import ahgpoug.com.specshelper.objects.RAM;
import ahgpoug.com.specshelper.util.Globals;

public class ActivityCart extends AppCompatActivity {
    private int total = 0;
    private Motherboard motherboard;
    private CPU cpu;
    private GPU gpu;
    private RAM ram;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initViews();
    }

    private void initViews(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView motherboardItem = (TextView) findViewById(R.id.motherboardItem);
        TextView cpuItem = (TextView) findViewById(R.id.cpuItem);
        TextView gpuItem = (TextView) findViewById(R.id.gpuItem);
        TextView ramItem = (TextView) findViewById(R.id.ramItem);

        if (Globals.cart.getMbID() != -1) {
            String query = "SELECT * FROM motherboard " +
                    "INNER JOIN sockets on motherboard.socket = sockets.sid " +
                    "INNER JOIN formfactors on motherboard.formFactor = formfactors.ffid " +
                    "INNER JOIN ramtypes on motherboard.ramType = ramtypes.rid " +
                    "WHERE id = " + Globals.cart.getMbID();
            motherboard = Globals.getMotherboardsFromQuery(ActivityCart.this, query).get(0);
            motherboardItem.setText(motherboard.getManufacturer() + " " + motherboard.getCodename() + ", " + motherboard.getPrice() + " руб.");
            total += motherboard.getPrice();
        }

        if (Globals.cart.getCpuID() != -1) {
            String query = "SELECT * FROM cpu INNER JOIN sockets on cpu.socket = sockets.sid WHERE id = " + Globals.cart.getCpuID();
            cpu = Globals.getCPUsFromQuery(ActivityCart.this, query).get(0);
            cpuItem.setText(cpu.getManufacturer() + " " + cpu.getCodename() + ", " + cpu.getPrice() + " руб.");
            total += cpu.getPrice();
        }

        if (Globals.cart.getGpuID() != -1) {
            String query = "SELECT * FROM gpu INNER JOIN memory on gpu.memoryType = memory.mid WHERE id = " + Globals.cart.getGpuID();
            gpu = Globals.getGPUsFromQuery(ActivityCart.this, query).get(0);
            gpuItem.setText(Globals.cart.getGpuAmount() + "x " + gpu.getManufacturer() + " " + gpu.getCodename() + ", " + gpu.getPrice()*Globals.cart.getGpuAmount() + " руб.");
            total += gpu.getPrice();
        }

        if (Globals.cart.getRamID() != -1) {
            String query = "SELECT * FROM ram INNER JOIN ramtypes on ram.type = ramtypes.rid WHERE id = " + Globals.cart.getRamID();
            ram = Globals.getRamFromQuery(ActivityCart.this, query).get(0);
            ramItem.setText(Globals.cart.getRamAmount() + "x " + ram.getManufacturer() + " " + ram.getCodename() + ", " + ram.getPrice()*Globals.cart.getRamAmount() + " руб.");
            total += ram.getPrice();
        }

        setTitle("Сумма: " + String.valueOf(total) + " руб.");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_check) {
            performCheck();
        } else if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void performCheck(){
        String conflicts = "Возможные конфликты:\n\n";
        Cart cart = Globals.cart;
        if (cart.getMbID() != -1 && cart.getCpuID() != -1){
            if (!motherboard.getSocket().equals(cpu.getSocket()))
                conflicts += "Несовместимый Socket\n";
        }

        if (cart.getMbID() != -1 && cart.getGpuID() != -1){
            if (motherboard.getPcie16count() < cart.getGpuAmount())
                conflicts += "У материнской платы недостаточно слотов PCI-E x16\n";

            if (gpu.getManufacturer().equals("AMD") && !gpu.getCodename().contains("GeForce")) {
                if (cart.getGpuAmount() > 1)
                    if (!motherboard.isCrossFire())
                        conflicts += "Материнская плата не поддерживает технолонию CrossFire\n";
            } else {
                if (cart.getGpuAmount() > 1)
                    if (!motherboard.isSli())
                        conflicts += "Материнская плата не поддерживает технолонию SLI\n";
            }
        }

        if (cart.getMbID() != -1 && cart.getRamID() != -1){
            if (!motherboard.getRamType().contains(ram.getType()))
                conflicts += "Несовместимый тип RAM\n";

            if (motherboard.getMaxRamCount() < cart.getRamAmount())
                conflicts += "В материнской плате недостаточно слотов RAM\n";

            if (motherboard.getMaxRamSize() < ram.getMemorySize()*cart.getRamAmount())
                conflicts += "Превышение максимального объема RAM\n";

            if (motherboard.getMaxRamClock() < ram.getClock())
                conflicts += "Превышение максимальной частоты RAM\n";
        }

        if (cart.getGpuID() != -1){
            if (!gpu.isSli() && cart.getGpuAmount() > 1)
                conflicts += "Видеокарта не поддерживает работу в SLI/CrossFire\n";
        }

        new MaterialDialog.Builder(ActivityCart.this)
                .title("Лог")
                .content(conflicts)
                .positiveText("Ок")
                .show();
    }
}
