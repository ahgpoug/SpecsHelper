package ahgpoug.com.specshelper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import ahgpoug.com.specshelper.Objects.CPU;
import ahgpoug.com.specshelper.Objects.Cart;
import ahgpoug.com.specshelper.Objects.Motherboard;

public class Globals {
    public static boolean isAdmin = false;
    public static Cart cart = new Cart();

    public static ArrayList<String> getSingleField(Context context, String query, String field){
        ArrayList<String> list = new ArrayList<>();

        DataBaseHelper mDatabaseHelper = new DataBaseHelper(context);
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor .moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String value = cursor.getString(cursor.getColumnIndex(field));

                list.add(value);
                cursor.moveToNext();
            }
        }
        db.close();
        cursor.close();

        return list;
    }

    public static ArrayList<CPU> getCPUsFromQuery(Context context, String query){
        ArrayList<CPU> list = new ArrayList<>();

        DataBaseHelper mDatabaseHelper = new DataBaseHelper(context);
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor .moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String manufacturer = cursor.getString(cursor.getColumnIndex("manufacturer"));
                String codename = cursor.getString(cursor.getColumnIndex("codename"));
                String socket = cursor.getString(cursor.getColumnIndex("socketName"));
                int coresCount = cursor.getInt(cursor.getColumnIndex("coresCount"));
                int process = cursor.getInt(cursor.getColumnIndex("process"));
                int clock = cursor.getInt(cursor.getColumnIndex("clock"));
                int tdp = cursor.getInt(cursor.getColumnIndex("tdp"));
                String gpuType = cursor.getString(cursor.getColumnIndex("gpuType"));
                int release = cursor.getInt(cursor.getColumnIndex("release"));
                int price = cursor.getInt(cursor.getColumnIndex("price"));

                list.add(new CPU(id, manufacturer, codename, socket, coresCount, process, clock, tdp, gpuType, release, price));
                cursor.moveToNext();
            }
        }
        db.close();
        cursor.close();

        return list;
    }

    public static ArrayList<Motherboard> getMotherboardsFromQuery(Context context, String query){
        ArrayList<Motherboard> list = new ArrayList<>();

        DataBaseHelper mDatabaseHelper = new DataBaseHelper(context);
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor .moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String manufacturer = cursor.getString(cursor.getColumnIndex("manufacturer"));
                String codename = cursor.getString(cursor.getColumnIndex("codename"));
                String socket = cursor.getString(cursor.getColumnIndex("socketName"));
                String formFactor = cursor.getString(cursor.getColumnIndex("ffName"));
                String chipSet = cursor.getString(cursor.getColumnIndex("chipSet"));
                String ramType = cursor.getString(cursor.getColumnIndex("ramName"));
                int maxRamCount = cursor.getInt(cursor.getColumnIndex("maxRamCount"));
                int maxRamClock = cursor.getInt(cursor.getColumnIndex("maxRamClock"));
                int maxRamSize= cursor.getInt(cursor.getColumnIndex("maxRamSize"));
                int ideCount = cursor.getInt(cursor.getColumnIndex("ideCount"));
                int sata6count = cursor.getInt(cursor.getColumnIndex("sata6count"));
                int sata3count = cursor.getInt(cursor.getColumnIndex("sata3count"));
                int pcie16count = cursor.getInt(cursor.getColumnIndex("pcie16count"));
                int pcie1count = cursor.getInt(cursor.getColumnIndex("pcie1count"));
                int usb2count = cursor.getInt(cursor.getColumnIndex("usb2count"));
                int usb3count = cursor.getInt(cursor.getColumnIndex("usb3count"));
                int maxEthernetSpeed = cursor.getInt(cursor.getColumnIndex("maxEthernetSpeed"));
                boolean sli = false;
                if (cursor.getInt(cursor.getColumnIndex("sli")) == 0)
                    sli = true;
                boolean crossFire = false;
                if (cursor.getInt(cursor.getColumnIndex("crossFire")) == 0)
                    crossFire = true;
                int price = cursor.getInt(cursor.getColumnIndex("price"));

                list.add(new Motherboard(id, manufacturer, codename, socket, formFactor, chipSet, ramType, maxRamCount, maxRamSize, maxRamClock, ideCount, sata6count, sata3count, pcie16count, pcie1count, usb2count, usb3count, maxEthernetSpeed, sli, crossFire, price));
                cursor.moveToNext();
            }
        }
        db.close();
        cursor.close();

        return list;
    }

    public static void clearCpuFilter(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("cpuFilter.manufacturer", "");
        editor.putString("cpuFilter.codename", "");
        editor.putString("cpuFilter.socket", "Socket");
        editor.putString("cpuFilter.coresCountSpinner", "==");
        editor.putString("cpuFilter.coresCount", "");
        editor.putString("cpuFilter.clockSpinner", "==");
        editor.putString("cpuFilter.clock", "");
        editor.putString("cpuFilter.gpu", "Встроенный GPU");
        editor.putString("cpuFilter.processSpinner", "==");
        editor.putString("cpuFilter.process", "");
        editor.putString("cpuFilter.tdpSpinner", "==");
        editor.putString("cpuFilter.tdp", "");
        editor.putString("cpuFilter.releaseSpinner", "==");
        editor.putString("cpuFilter.release", "");
        editor.putString("cpuFilter.priceSpinner", "==");
        editor.putString("cpuFilter.price", "");
        editor.apply();
    }

    public static void showSignInForm(final Context context){
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title("Вход")
                .customView(R.layout.prompt_sign_in, true)
                .cancelable(true)
                .build();

        final EditText loginET = (EditText)dialog.findViewById(R.id.loginET);
        final EditText passwordET = (EditText)dialog.findViewById(R.id.passwordET);
        Button signInBtn = (Button)dialog.findViewById(R.id.signInBtn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loginET.getText().toString().equals("root") && passwordET.getText().toString().equals("root")) {
                    Globals.isAdmin = true;
                    ((Activity) context).finish();
                    context.startActivity(((Activity) context).getIntent());
                } else
                    Toast.makeText(context, "Неверный логин или пароль", Toast.LENGTH_LONG).show();
            }
        });

        dialog.show();
    }
}
