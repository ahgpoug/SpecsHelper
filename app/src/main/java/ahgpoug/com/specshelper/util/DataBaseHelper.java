package ahgpoug.com.specshelper.util;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "SpecsHelper.db";

    private static final int DATABASE_VERSION = 1;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String DATABASE_CREATE_SCRIPT;

        DATABASE_CREATE_SCRIPT = "CREATE TABLE sockets (" +
                "sid INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "socketName TEXT NOT NULL)";

        db.execSQL(DATABASE_CREATE_SCRIPT);

        DATABASE_CREATE_SCRIPT = "CREATE TABLE ramtypes (" +
                "rid INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "rName TEXT NOT NULL)";

        db.execSQL(DATABASE_CREATE_SCRIPT);

        DATABASE_CREATE_SCRIPT = "CREATE TABLE formfactors (" +
                "ffid INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "ffName TEXT NOT NULL)";

        db.execSQL(DATABASE_CREATE_SCRIPT);

        DATABASE_CREATE_SCRIPT = "CREATE TABLE memory (" +
                "mid INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "mName TEXT NOT NULL)";

        db.execSQL(DATABASE_CREATE_SCRIPT);

        DATABASE_CREATE_SCRIPT = "CREATE TABLE motherboard (" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "manufacturer TEXT NOT NULL," +
                "codename TEXT NOT NULL," +
                "socket INTEGER NOT NULL," +
                "formFactor INTEGER NOT NULL," +
                "chipSet INTEGER NOT NULL," +
                "ramType INTEGER NOT NULL," +
                "maxRamCount INTEGER NOT NULL," +
                "maxRamClock INTEGER NOT NULL," +
                "maxRamSize INTEGER NOT NULL," +
                "ideCount INTEGER NOT NULL," +
                "sata6count INTEGER NOT NULL," +
                "sata3count INTEGER NOT NULL," +
                "pcie16count INTEGER NOT NULL," +
                "pcie1count INTEGER NOT NULL," +
                "usb2count INTEGER NOT NULL," +
                "usb3count INTEGER NOT NULL," +
                "maxEthernetSpeed INTEGER NOT NULL," +
                "sli INTEGER NOT NULL," +
                "crossFire INTEGER NOT NULL," +
                "price INTEGER NOT NULL)";

        db.execSQL(DATABASE_CREATE_SCRIPT);

        DATABASE_CREATE_SCRIPT = "CREATE TABLE cpu (" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "manufacturer TEXT NOT NULL," +
                "codename TEXT NOT NULL," +
                "socket INTEGER NOT NULL," +
                "coresCount INTEGER NOT NULL," +
                "process INTEGER NOT NULL," +
                "clock INTEGER NOT NULL," +
                "tdp INTEGER NOT NULL," +
                "gpuType TEXT," +
                "release INTEGER NOT NULL," +
                "price INTEGER NOT NULL)";

        db.execSQL(DATABASE_CREATE_SCRIPT);

        DATABASE_CREATE_SCRIPT = "CREATE TABLE gpu (" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "manufacturer TEXT NOT NULL," +
                "codename TEXT NOT NULL," +
                "cClock INTEGER NOT NULL," +
                "mClock INTEGER NOT NULL," +
                "memorySize INTEGER NOT NULL," +
                "memoryType INTEGER NOT NULL," +
                "bus INTEGER NOT NULL," +
                "process INTEGER NOT NULL," +
                "slots INTEGER NOT NULL," +
                "sli INTEGER NOT NULL," +
                "price INTEGER NOT NULL)";

        db.execSQL(DATABASE_CREATE_SCRIPT);

        DATABASE_CREATE_SCRIPT = "CREATE TABLE ram (" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "manufacturer TEXT NOT NULL," +
                "codename TEXT NOT NULL," +
                "clock INTEGER NOT NULL," +
                "memorySize INTEGER NOT NULL," +
                "type INTEGER NOT NULL," +
                "price INTEGER NOT NULL)";

        db.execSQL(DATABASE_CREATE_SCRIPT);

        initDefaults(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void initDefaults(SQLiteDatabase db){
        String DATABASE_INSERT_SCRIPT;

        DATABASE_INSERT_SCRIPT = "INSERT INTO ramtypes (rName) VALUES" +
                "('DDR DIMM')," +
                "('DDR/DDR2 DIMM')," +
                "('DDR2 DIMM')," +
                "('DDR2/DDR3 DIMM')," +
                "('DDR3 DIMM')," +
                "('DDR4 DIMM')";

        db.execSQL(DATABASE_INSERT_SCRIPT);

        DATABASE_INSERT_SCRIPT = "INSERT INTO formfactors (ffName) VALUES" +
                "('ATX')," +
                "('EATS')," +
                "('FlexATX')," +
                "('microATX')";

        db.execSQL(DATABASE_INSERT_SCRIPT);

        DATABASE_INSERT_SCRIPT = "INSERT INTO sockets (socketName) VALUES" +
                "('AM1')," +
                "('AM2')," +
                "('AM2+')," +
                "('AM3')," +
                "('AM3+')," +
                "('C32')," +
                "('FM1')," +
                "('FM2')," +
                "('FM2+')," +
                "('LGA1150')," +
                "('LGA1151')," +
                "('LGA1155')," +
                "('LGA1156')," +
                "('LGA1366')," +
                "('LGA1567')," +
                "('LGA2011')," +
                "('LGA2011-3')," +
                "('LGA771')," +
                "('LGA775')";

        db.execSQL(DATABASE_INSERT_SCRIPT);

        DATABASE_INSERT_SCRIPT = "INSERT INTO memory (mName) VALUES" +
                "('GDDR')," +
                "('GDDR2')," +
                "('GDDR3')," +
                "('GDDR4')," +
                "('GDDR5')," +
                "('GDDR5')," +
                "('HBM')";

        db.execSQL(DATABASE_INSERT_SCRIPT);

        DATABASE_INSERT_SCRIPT = "INSERT INTO motherboard (manufacturer, codename, socket, formFactor, chipSet, ramType, maxRamCount, maxRamSize, maxRamClock, ideCount, sata6count, sata3count, pcie16count, pcie1count, usb2count, usb3count, maxEthernetSpeed, sli, crossFire, price) VALUES" +
                "('MSI', 'H100M PRO-VD', 11, 4, 'Intel H110', 6, 4, 32, 2133, 0, 4, 0, 1, 2, 10, 4, 1000, 0, 0, 3790)," +
                "('ASUS', 'Z170-A', 11, 1, 'Intel Z170', 6, 4, 64, 3400, 0, 6, 0, 3, 3, 12, 8, 1000, 1, 1, 10690)";

        db.execSQL(DATABASE_INSERT_SCRIPT);

        DATABASE_INSERT_SCRIPT = "INSERT INTO cpu (manufacturer, codename, socket, coresCount, process, clock, tdp, gpuType, release, price) VALUES" +
                "('Intel', 'Core i7-6700K Skylake', 11, 4, 14, 4000, 91, 'HD Graphics 530 1150 МГц', 2015, 28490)," +
                "('Intel', 'Core i5-4460 Haswell', 10, 4, 22, 3200, 84, 'HD Graphics 4600 1100 МГц', 2013, 13790)";

        db.execSQL(DATABASE_INSERT_SCRIPT);

        DATABASE_INSERT_SCRIPT = "INSERT INTO gpu (manufacturer, codename, cClock, mClock, memorySize, memoryType, bus, process, slots, sli, price) VALUES" +
                "('Gigabyte', 'GeForce GTX 1070', 1695, 8316, 8192, 6, 256, 16, 2, 1, 39960)," +
                "('ASUS', 'GeForce GTX 750 Ti', 1072, 5400, 2048, 6, 128, 28, 2, 1, 7890)";

        db.execSQL(DATABASE_INSERT_SCRIPT);

        DATABASE_INSERT_SCRIPT = "INSERT INTO ram (manufacturer, codename, clock, memorySize, type, price) VALUES" +
                "('Kingston', 'HX318C10F', 1866, 8, 5, 4290)," +
                "('Samsung', '', 2133, 8, 6, 3690)";

        db.execSQL(DATABASE_INSERT_SCRIPT);
    }
}